package org.esni.siskin.core.conf

import java.nio.file.Paths

object SiskinConf {

  val siskinCore = new BaseConf("sislin-core")

  // 用于展示如何使用BaseConf
  val TEST: String = siskinCore.getString("TEST", "DEF")

  // Jar包存放路径, 此路径不能包含中文
  val LIB_PATH: String = siskinCore.getString("LIB_PATH", Paths.get(System.getenv("SISKIN_HOME"), "lib").toString)

}
