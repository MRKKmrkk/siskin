package org.esni.siskin_core.function

import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.esni.siskin_core.view.LinkStatisticRouteView


class LinkStatisticRouteWindowFunction extends WindowFunction[(Long, Long), LinkStatisticRouteView, (String, String), TimeWindow] {

  override def apply(key: (String, String), window: TimeWindow, input: Iterable[(Long, Long)], out: Collector[LinkStatisticRouteView]): Unit = {

    var sum: Long = 0L
    var ts: Long = 0L

    input.foreach{
      tup =>
        sum += tup._1
        ts = Math.max(tup._2, ts)
    }

    out.collect(LinkStatisticRouteView(
      key._1,
      key._2,
      sum,
      window.getEnd
    ))

  }

}
