package org.esni.siskin_core.function

import org.apache.flink.streaming.api.scala.function.{ProcessWindowFunction, WindowFunction}
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector

class LinkCountWindowFunction extends WindowFunction[Long, (String, Long), String, TimeWindow]{
  override def apply(key: String, window: TimeWindow, input: Iterable[Long], out: Collector[(String, Long)]): Unit = {

    val value = input.iterator.next()
    println(key + ":" + value)
    out
      .collect((
        key,
        value
      ))

  }
}


