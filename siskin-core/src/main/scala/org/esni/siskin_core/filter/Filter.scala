package org.esni.siskin_core.filter

import org.apache.flink.api.common.functions.FilterFunction
import org.apache.flink.streaming.api.scala.DataStream
import org.esni.siskin_common.bean.Data

trait Filter extends FilterFunction[Data] {


}
