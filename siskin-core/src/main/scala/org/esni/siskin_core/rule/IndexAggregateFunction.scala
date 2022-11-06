package org.esni.siskin_core.rule

import org.apache.flink.api.common.functions.AggregateFunction
import org.apache.logging.log4j.scala.Logging
import org.esni.siskin_core.bean.{AggregateIndexes, Data, Indexes}

import scala.collection.mutable

// <IN, ACC, OUT>
class IndexAggregateFunction(
                              startUserAgentCount: Boolean,
                              startCookieCount: Boolean,
                              startVisitCount: Boolean,
                              startMiniNumAccessIntervalTimesCount: Boolean
                            ) extends AggregateFunction[Data, Indexes, AggregateIndexes] with Logging{

  logger.info("create flink aggregate function in SpiderIdentifyService: IndexAggregateFunction")

  override def createAccumulator(): Indexes = {

    Indexes(
      mutable.Set(),
      mutable.Set(),
      0L,
      0L
    )

  }

  override def add(value: Data, accumulator: Indexes): Indexes = {

    // ua 统计
    if (startUserAgentCount) accumulator.userAgents += value.userAgent

    // cookie 统计
    if (startCookieCount) accumulator.cookies += value.cookie

    // 访问次数统计
    if (startVisitCount) accumulator.visitTimes += 1L

    // 最小间隔统计
    // todo: 最小间隔统计未实现

    accumulator

  }

  override def getResult(accumulator: Indexes): AggregateIndexes = {

    logger.debug("close window")

    AggregateIndexes(
      accumulator.userAgents.size.toLong,
      accumulator.cookies.size.toLong,
      accumulator.visitTimes,
      accumulator.miniNumAccessIntervalTimes
    )

  }

  override def merge(a: Indexes, b: Indexes): Indexes = {

    Indexes(
      a.userAgents ++ b.userAgents,
      a.cookies ++ b.cookies,
      a.visitTimes + b.visitTimes,
      a.miniNumAccessIntervalTimes + b.miniNumAccessIntervalTimes
    )

  }

}