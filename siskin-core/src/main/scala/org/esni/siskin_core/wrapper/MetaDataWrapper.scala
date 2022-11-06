package org.esni.siskin_core.wrapper

import org.esni.siskin_core.bean.Data

trait MetaDataWrapper extends Serializable {

  def wrap(data: Data): List[(String, Any)]

}
