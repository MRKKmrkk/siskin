package org.esni.siskin_core.wrapper

import org.esni.siskin_common.bean.Data

trait Wrapper extends Serializable {

  def wrap(msg: String): List[Data]

}
