package org.esni.siskin_core.function

import org.apache.flink.api.common.functions.AggregateFunction
import org.esni.siskin_common.bean.Data

class LinkCountAggregateFunction extends AggregateFunction[Data, Long, Long]{


  override def createAccumulator(): Long = 0L

  override def add(value: Data, accumulator: Long): Long = accumulator + 1

  override def getResult(accumulator: Long): Long = accumulator

  override def merge(a: Long, b: Long): Long = a + b

}

