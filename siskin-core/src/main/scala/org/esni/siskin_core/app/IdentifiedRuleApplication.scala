package org.esni.siskin_core.app

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.TimeCharacteristic
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.{SlidingEventTimeWindows, TumblingEventTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.esni.siskin_core.bean.{Data, IdentifiedRule, Spider}
import org.esni.siskin_core.conf.SiskinConfig
import org.esni.siskin_core.function.IndexProcessWindowFunction
import org.esni.siskin_core.rule.IndexAggregateFunction
import org.esni.siskin_core.source.RedisHashSource
import org.esni.siskin_core.source.impl.ChannelKafkaSource
import org.esni.siskin_core.util.{DateUtil, JedisConnectionPoolUtil, RedisSinkUtil, States}

import scala.collection.JavaConversions._
import scala.collection.mutable

object IdentifiedRuleApplication {


  def run(sirId: Int): Unit = {
    val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
//    val conf = new Configuration()
//    val env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(conf)

    env.setParallelism(1)
    env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime)

    val identifiedRulesBroadcastStream = env
      .addSource(new RedisHashSource[IdentifiedRule](
        "spider-identified-rules",
        x => IdentifiedRule(x._1.toInt, x._2)
      ))

    // 时间限制
    val timeIdentifiedRulesBroadcastStream = identifiedRulesBroadcastStream
      .filter{
        rule =>
          if (sirId != rule.id) {
            false
          }
          else{
            val time = JSON
              .parseObject(rule.rule)
              .getJSONObject("limit")
              .getJSONObject("time")

            val nowDateTime = DateUtil.getNowDate

            nowDateTime >= time.getString("starttime") && nowDateTime <= time.getString("endtime")
          }
      }
      .broadcast(States.identifiedRulesBroadcastMapStateDescriptor)


    // 获取数据源
    val channelKafkaSource = new ChannelKafkaSource()
    channelKafkaSource.open(env)

    // 过滤limit并分组
    val keyedStream = channelKafkaSource
      .getDataStream(env)
      .map {
        sdata =>

          val map = new mutable.HashMap[String, Any]()
          sdata.getMetaData.foreach(
            tup =>
              map.put(tup._1, tup._2)
          )

          Data(
            sdata.getRequest,
            sdata.getMethod,
            sdata.getRemoteAddress,
            sdata.getRequestParameter,
            sdata.getContentType,
            sdata.getCookie,
            sdata.getServerAddress,
            sdata.getReferer,
            sdata.getUserAgent,
            sdata.getTimeISO8601,
            sdata.getTimeLocal,
            map
          )
      }
      .connect(timeIdentifiedRulesBroadcastStream)
      .process(new BroadcastProcessFunction[Data, IdentifiedRule, Data] {
        override def processElement(value: Data, ctx: BroadcastProcessFunction[Data, IdentifiedRule, Data]#ReadOnlyContext, out: Collector[Data]): Unit = {
          val json = ctx
            .getBroadcastState(States.identifiedRulesBroadcastMapStateDescriptor)
            .get(sirId)

          val flag = JSON
            .parseObject(json)
            .getJSONObject("limit")
            .getJSONObject("route")
            .getJSONArray("links")
            .map(route => value.request.startsWith(route.toString.replace("*", "")))
            .contains(false)

          if (!flag) out.collect(value)

        }
        override def processBroadcastElement(value: IdentifiedRule, ctx: BroadcastProcessFunction[Data, IdentifiedRule, Data]#Context, out: Collector[Data]): Unit = {
          ctx.getBroadcastState(States.identifiedRulesBroadcastMapStateDescriptor).put(value.id, value.rule)
        }
      })
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[Data](Time.seconds(SiskinConfig.ETL_LINK_COUNT_WATERMARK)) {
        override def extractTimestamp(element: Data): Long = element.timeLocal.toLong
      })
      .keyBy(_.remoteAddress)


    // 开窗
    val json = JSON.parseObject(JedisConnectionPoolUtil.getJedis.hget("spider-identified-rules", sirId.toString))

    val windowTimeConfig: JSONObject = json
      .getJSONObject("limit")
      .getJSONObject("windowTime")

    val size: Time = DateUtil.getTime(
      windowTimeConfig.getInteger("hour"),
      windowTimeConfig.getInteger("min"),
      windowTimeConfig.getInteger("sec")
    )

    val recognizerNames = json
      .getJSONObject("identified")
      .getJSONArray("groups")
      .map(x => JSON.parseObject(x.toString))
      .flatMap {
        groups =>
          groups
            .getJSONArray("is")
            .map(x => JSON.parseObject(x.toString))
            .map(_.getString("name"))
      }
      .toSet

    val windowedStream: WindowedStream[Data, String, TimeWindow] = windowTimeConfig
      .getJSONObject("slide") match {
      case null => keyedStream.window(TumblingEventTimeWindows.of(size))
      case slideConfig =>
        keyedStream.window(
          SlidingEventTimeWindows.of(
            size,
            DateUtil.getTime(
              slideConfig.getInteger("hour"),
              slideConfig.getInteger("min"),
              slideConfig.getInteger("sec")
            )
          )
        )
    }

    // 识别爬虫
    val spiderStream: DataStream[Spider] = windowedStream
      .aggregate(
        new IndexAggregateFunction(
          recognizerNames.contains("user_agent_times"),
          recognizerNames.contains("cookie_times"),
          recognizerNames.contains("review_times"),
          recognizerNames.contains("interval_times")
        ),
        new IndexProcessWindowFunction(
          sirId
        )
      )

    // 爬虫入库
    spiderStream.addSink(RedisSinkUtil.getHSetRedisSink[Spider]("siskin-spiders", _.ip, _.sir_id.toString))

    env.execute(f"siskin-identified-application-$sirId")

  }

  def main(args: Array[String]): Unit = {

    run(args(0).toInt)

  }

}
