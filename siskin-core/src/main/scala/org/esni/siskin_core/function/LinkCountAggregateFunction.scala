package org.esni.siskin_core.function

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.logging.log4j.scala.Logging
import org.esni.siskin_core.bean.Data

class LinkCountAggregateFunction extends AggregateFunction[Data, Long, Long] with Logging{

  logger.info("create flink aggregate function in LinkStatisticService: LinkCountAggregateFunction")

  override def createAccumulator(): Long = 0L

  override def add(value: Data, accumulator: Long): Long = accumulator + 1

  override def getResult(accumulator: Long): Long = {

    logger.debug("close window")

    accumulator

  }

  override def merge(a: Long, b: Long): Long = a + b

}

