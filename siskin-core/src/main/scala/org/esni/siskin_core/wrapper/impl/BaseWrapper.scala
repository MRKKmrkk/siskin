package org.esni.siskin_core.wrapper.impl

import org.esni.siskin_core.bean.Data
import org.esni.siskin_core.wrapper.Wrapper

import scala.collection.mutable

/**
 * 进行最基本的数据封装
 */
class BaseWrapper extends Wrapper{

  override def wrap(msg: String): List[Data] = {

    val fields: Array[String] = msg.split("\t")

    List(
      Data(
        fields(0),
        fields(1),
        fields(2),
        fields(3),
        fields(4),
        fields(5),
        fields(6),
        fields(7),
        fields(8),
        fields(9),
        fields(10),
        mutable.Map()
      )
    )

  }

}
