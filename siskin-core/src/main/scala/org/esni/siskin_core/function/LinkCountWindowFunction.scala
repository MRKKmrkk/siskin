package org.esni.siskin_core.function

import org.apache.flink.streaming.api.scala.function.{ProcessWindowFunction, WindowFunction}
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.apache.logging.log4j.scala.Logging

class LinkCountWindowFunction extends WindowFunction[Long, (String, Long), String, TimeWindow] with Logging{

  logger.info("create flink window function in LinkStatisticService: LinkCountWindowFunction")


  override def apply(key: String, window: TimeWindow, input: Iterable[Long], out: Collector[(String, Long)]): Unit = {

    logger.debug("close window")

    val value = input.iterator.next()
    out
      .collect((
        key,
        value
      ))

  }
}


