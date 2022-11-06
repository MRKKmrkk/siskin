package org.esni.siskin_core.service

import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows
import org.apache.flink.streaming.api.windowing.time.Time
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.esni.siskin_core.bean.{Data, Spider}

class SpiderStatisticService {

  def count(spidersStream: DataStream[Spider], inputStream: DataStream[Data]): Unit = {

    // 开窗
    val windowedStream: WindowedStream[Data, (String, String), TimeWindow] = inputStream
      .keyBy(x => (x.serverAddress, x.request))
      .window(TumblingEventTimeWindows.of(Time.hours(1)))

  }

}
