package org.esni.siskin_core.app

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer
import org.esni.siskin_core.bean.{Data, SerializeData, Spider}
import org.esni.siskin_core.conf.SiskinConfig
import org.esni.siskin_core.serialize.DataSerializationSchema
import org.esni.siskin_core.service.{DataEncryptService, DataFilterService, DataPackageService, LinkStatisticService, SpiderStatisticService}
import org.esni.siskin_core.source.RedisHashSource
import org.esni.siskin_core.source.impl.KafkaSource

import java.util
import scala.collection.JavaConversions._
import scala.collection.mutable

object ETLApplication {

  private val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
//  val conf = new Configuration()
//  val env: StreamExecutionEnvironment = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

  // 创建服务
  private val linkStatisticService: LinkStatisticService = new LinkStatisticService()
  private val dataFilterService: DataFilterService = new DataFilterService()
  private val dataPackageService = new DataPackageService()
  private val statisticService = new SpiderStatisticService()

  private val kafkaSource: KafkaSource = new KafkaSource()
  kafkaSource.open(env)
  env.setParallelism(1)

  // 设定时间语义
  env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

  def main(args: Array[String]): Unit = {

    // 获取源数据流
    val inputStream: DataStream[String] = kafkaSource.getDataStream(env)

    // 设置水位
    val eventTimeDataStream = dataPackageService.packageData(inputStream)
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[Data](Time.seconds(SiskinConfig.ETL_LINK_COUNT_WATERMARK)) {
        override def extractTimestamp(element: Data): Long = element.timeLocal.toLong
      })

    // 链路统计
    val spiderStream = env
      .addSource(new RedisHashSource[Spider](
        "siskin-spiders",
        x =>Spider(x._1, x._2.toInt, 0L)
      ))
    linkStatisticService.linkCount(eventTimeDataStream)
    linkStatisticService.spiderLinkCount(spiderStream, eventTimeDataStream)

    // 过滤并打断任务链
    val cleanStream: DataStream[Data] = dataFilterService
      .filter(eventTimeDataStream)
      .disableChaining()

    // 存入etl channel
    cleanStream
      .map {
        data =>
          val m = new util.HashMap[String, AnyRef]()
          for (elem <- data.metaData) {
            m.put(elem._1, elem._2.asInstanceOf[AnyRef])
          }

          new SerializeData(
            data.request,
            data.method,
            data.remoteAddress,
            data.requestParameter,
            data.contentType,
            data.cookie,
            data.serverAddress,
            data.Referer,
            data.userAgent,
            data.timeISO8601,
            data.timeLocal,
            m
          )
      }
      .addSink(new FlinkKafkaProducer[SerializeData](
        SiskinConfig.KAFKA_BOOTSTRAP_SERVERS,
        SiskinConfig.KAFKA_ETL_CHANNEL_TOPIC,
        new DataSerializationSchema()
      ))

    env.execute("siskin-etl-application")


  }

}
