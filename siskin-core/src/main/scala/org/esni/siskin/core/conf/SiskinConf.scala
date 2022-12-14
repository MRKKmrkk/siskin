package org.esni.siskin.core.conf

object SiskinConf {

  val siskinCore = new BaseConf("sislin-core")

  // 用于展示如何使用BaseConf
  val TEST: String = siskinCore.getString("TEST", "DEF")

}
