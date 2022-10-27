package org.esni.siskin_core.transform.impl

import org.esni.siskin_common.bean.Data
import org.esni.siskin_core.transform.Transform

class NoTransform extends Transform{

  override def transform(data: Data): Data = {

    data

  }

}
