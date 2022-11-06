package org.esni.siskin_core.filter

import org.apache.flink.api.common.functions.FilterFunction
import org.esni.siskin_core.bean.Data

trait Filter extends FilterFunction[Data] {


}
