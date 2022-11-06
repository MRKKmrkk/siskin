package org.esni.siskin_core.wrapper.impl

import org.esni.siskin_core.bean.Data
import org.esni.siskin_core.wrapper.MetaDataWrapper

class DemoMetaDataWrapper extends MetaDataWrapper {

  override def wrap(data: Data): List[(String, Any)] = {
    List(("1", "1"))
  }

}
