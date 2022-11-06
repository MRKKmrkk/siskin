package org.esni.siskin_core.service

import org.apache.flink.streaming.api.datastream.BroadcastStream
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.logging.log4j.scala.Logging
import org.esni.siskin_core.bean.{Data, Spider}
import org.esni.siskin_core.conf.SiskinConfig
import org.esni.siskin_core.function.{LinkStatisticRouteAggregateFunction, LinkStatisticRouteWindowFunction, SpiderBroadcastProcessFunction}
import org.esni.siskin_core.sink.{MysqlLinkStatisticRouteSink, MysqlSpiderLinkStatisticRouteSink}
import org.esni.siskin_core.util.States

import java.text.SimpleDateFormat
import java.util.Date

class LinkStatisticService extends Logging{

  logger.info("create service: LinkStatisticService")

  def linkCount(inputStream: DataStream[Data]): Unit = {

    // 开窗
    val windowedStream: WindowedStream[Data, (String, String), TimeWindow] = inputStream
      .keyBy(x => (x.serverAddress, x.request))
      .window(TumblingEventTimeWindows.of(Time.hours(1)))

    // 统计链路
    windowedStream
      .aggregate(
        new LinkStatisticRouteAggregateFunction(),
        new LinkStatisticRouteWindowFunction()
      )
      .addSink(new MysqlLinkStatisticRouteSink)

  }

  def spiderLinkCount(spidersStream: DataStream[Spider], inputStream: DataStream[Data]): Unit = {

    val spiderBroadcastStream: BroadcastStream[Spider] = spidersStream
      .broadcast(States.spiderBroadcastMapDescriptor)

    val spiderReqStream: DataStream[Data] = inputStream
      .connect(spiderBroadcastStream)
      .process(new SpiderBroadcastProcessFunction())

    // 开窗
    val windowedStream: WindowedStream[Data, (String, String), TimeWindow] = spiderReqStream
      .assignTimestampsAndWatermarks(new BoundedOutOfOrdernessTimestampExtractor[Data](Time.seconds(SiskinConfig.ETL_LINK_COUNT_WATERMARK)) {
        override def extractTimestamp(element: Data): Long = element.timeLocal.toLong
      })
      .keyBy(x => (x.serverAddress, x.request))
      .window(TumblingEventTimeWindows.of(Time.hours(1)))

    // 统计链路
    windowedStream
      .aggregate(
        new LinkStatisticRouteAggregateFunction(),
        new LinkStatisticRouteWindowFunction()
      )
      .addSink(new MysqlSpiderLinkStatisticRouteSink)

  }

}
