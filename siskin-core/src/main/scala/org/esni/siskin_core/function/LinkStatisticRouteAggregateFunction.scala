package org.esni.siskin_core.function

import org.apache.flink.api.common.functions.AggregateFunction
import org.esni.siskin_core.bean.Data
import org.esni.siskin_core.view.LinkStatisticRouteView

class LinkStatisticRouteAggregateFunction extends AggregateFunction[Data, (Long, Long), (Long, Long)]{

  override def createAccumulator(): (Long, Long) = (0L, 0L)

  override def add(value: Data, accumulator: (Long, Long)): (Long, Long) = (accumulator._1 + 1, Math.max(accumulator._2, value.timeLocal.toLong))

  override def getResult(accumulator: (Long, Long)): (Long, Long) = accumulator

  override def merge(a: (Long, Long), b: (Long, Long)): (Long, Long) = (a._1 + b._1, Math.max(a._2, b._2))

}
