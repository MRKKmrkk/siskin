package org.esni.siskin_core.service

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.{SlidingEventTimeWindows, TumblingEventTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.logging.log4j.scala.Logging
import org.esni.siskin_core.bean.{Data, Spider}
import org.esni.siskin_core.rule.{IndexAggregateFunction, IndexWindowFunction}
import org.esni.siskin_core.util.{DateUtil, SiskinDBConnectionPoolUtil}

import java.sql.{Connection, PreparedStatement}
import scala.collection.JavaConversions._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

class SpiderIdentifyService() extends Logging {

  logger.info("create service: SpiderIdentifyService")

  @Deprecated
  val routeStreamMap: mutable.Map[String, DataStream[Data]] = mutable.Map()
  val ruleStreams: mutable.ArrayBuffer[DataStream[Spider]] = new ArrayBuffer[DataStream[Spider]]()

  def identified(inputStream: DataStream[Data]): Unit = {

    val connection: Connection = SiskinDBConnectionPoolUtil.getConnection
    val nowTime = "\"" + DateUtil.getNowDate + "\""
    // 生效时间限制
    val ps: PreparedStatement = connection.prepareStatement(f"select identified_rules, sir_id from spider_identified_rules where $nowTime > start_time and $nowTime < end_time")

    val rs = ps.executeQuery()
    while (rs.next()) {

      val json: JSONObject = JSON.parseObject(rs.getString(1))

      // 路由限制
      // TODO: 暂未实现分流缓存
      var curStream: DataStream[Data] = inputStream
      json
        .getJSONObject("limit")
        .getJSONObject("route")
        .getJSONArray("links")
        .foreach{
          route =>
            curStream = curStream.filter{
              data =>
                data.request.startsWith(route.toString.replace("*", ""))
            }
        }

      val keyedStream: KeyedStream[Data, String] = curStream
        .keyBy(_.remoteAddress)

      // 开窗
      val windowTimeConfig: JSONObject = json
        .getJSONObject("limit")
        .getJSONObject("windowTime")

      val size: Time = DateUtil.getTime(
        windowTimeConfig.getInteger("hour"),
        windowTimeConfig.getInteger("min"),
        windowTimeConfig.getInteger("sec")
      )

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

      ruleStreams += windowedStream
        .aggregate(
          new IndexAggregateFunction(
            recognizerNames.contains("user_agent_times"),
            recognizerNames.contains("cookie_times"),
            recognizerNames.contains("review_times"),
            recognizerNames.contains("interval_times")
          ),
          new IndexWindowFunction(
            json.getJSONObject("identified"),
            rs.getInt(2)
          )
        )

    }

  }

}
