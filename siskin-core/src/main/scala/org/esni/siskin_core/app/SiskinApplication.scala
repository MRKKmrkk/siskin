package org.esni.siskin_core.app

import com.mrkk.sp.properties.SProperties
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.esni.siskin_common.bean.Data
import org.esni.siskin_core.bean.Spider
import org.esni.siskin_core.conf.SiskinConfig
import org.esni.siskin_core.service.{DataClassficatService, DataEncryptService, DataFilterService, DataPackageService, DataTransformService, LinkStatisticService, SpiderIdentifyService}
import org.esni.siskin_core.util.RedisSinkUtil

import java.util.Properties

object SiskinApplication {

  // 配置文件
//  private val etlProperties: SProperties = SProperties.createSProperties("siskin-core.properties")
  private val env = StreamExecutionEnvironment.getExecutionEnvironment

  // 创建服务
  private val linkStatisticService: LinkStatisticService = new LinkStatisticService()
  private val dataFilterService: DataFilterService = new DataFilterService()
  private val dataPackageService = new DataPackageService()
  private val dataEncryptService = new DataEncryptService()
  private val dataTransformService = new DataTransformService()
  private val dataClassficationService = new DataClassficatService()
  private val spiderIdentifyService = new SpiderIdentifyService()

  // 设定时间语义
  env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)
  env.setParallelism(1)

  def main(args: Array[String]): Unit = {

    val collectChannelProperties = new Properties()
    collectChannelProperties.setProperty("bootstrap.servers", SiskinConfig.KAFKA_BOOTSTRAP_SERVERS)

    // 获取源数据流
    val inputStream: DataStream[String] = env
      .addSource[String](new FlinkKafkaConsumer[String](
        SiskinConfig.KAFKA_COLLECT_CHANNEL_TOPIC,
        new SimpleStringSchema(),
        collectChannelProperties
      ).setStartFromEarliest())

    /////////////////////////////////////////////////// 对数据进行封装 ///////////////////////////////////////////////////
    val packageStream = dataPackageService.packageData(inputStream)
    // 设置水位
    val eventTimeDataStream: DataStream[Data] = packageStream
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[Data](Time.seconds(SiskinConfig.ETL_LINK_COUNT_WATERMARK)) {
        override def extractTimestamp(element: Data): Long = element.timeLocal.toLong
      })

    /////////////////////////////////////////////////// 链路统计  ///////////////////////////////////////////////////////
//    linkStatisticService.linkCount(eventTimeDataStream)

    /////////////////////////////////////////////////// 数据 ETL ////////////////////////////////////////////////////////
    // 对数据进行过滤
//    val cleanStream = dataFilterService.filter(packageStream)
    // 对数据进行脱敏
//    val encryptStream = dataEncryptService.encrypt(cleanStream)
    // 对数据字段进行转换
//    val transformStream = dataTransformService.transform(encryptStream)
    // todo: 暂未实现
    // 对数据进行分类
//    val classficationStream = dataClassficationService.classficat(transformStream)

    /////////////////////////////////////////////////// 爬虫识别 ////////////////////////////////////////////////////////

    spiderIdentifyService.identified(eventTimeDataStream)
    spiderIdentifyService
      .ruleStreams(0)
      .print()
//      .foreach(_.addSink(RedisSinkUtil.getHSetRedisSink[Spider]("siskin-spiders", _.ip, _.sir_id.toString)))

    env.execute()

  }

}
