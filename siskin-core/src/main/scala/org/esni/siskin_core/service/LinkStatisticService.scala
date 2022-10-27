package org.esni.siskin_core.service

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.{TumblingEventTimeWindows, TumblingProcessingTimeWindows}
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.triggers.ContinuousEventTimeTrigger
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.esni.siskin_common.bean.Data
import org.esni.siskin_core.function.{LinkCountAggregateFunction, LinkCountWindowFunction}
import org.esni.siskin_core.util.RedisSinkUtil

class LinkStatisticService {

  def linkCount(inputStream: DataStream[Data]): Unit = {

    // 开窗
    val windowedStream: WindowedStream[Data, String, TimeWindow] = inputStream
      .keyBy(_.serverAddress)
      .window(TumblingEventTimeWindows.of(Time.days(1), Time.hours(-8)))
//      .trigger(ContinuousEventTimeTrigger.of[TimeWindow](Time.days(2)))
//      .trigger(ContinuousEventTimeTrigger.of[TimeWindow](Time.days(1)))

    // 统计链路
    windowedStream
      .aggregate(new LinkCountAggregateFunction(), new LinkCountWindowFunction())
      .addSink(RedisSinkUtil.getHSetRedisSink[(String, Long)]("siskin-link-count", _._1, _._2.toString))

  }

}
