package org.esni.siskin_core.transform

import org.apache.flink.api.common.functions.MapFunction
import org.esni.siskin_core.bean.Data

trait Transform extends MapFunction[Data, Data]{

  def transform(data: Data): Data

  override def map(data: Data): Data = {

    transform(data)

  }

}
