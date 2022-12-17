package org.esni.siskin.core

import org.esni.siskin.core.conf.SiskinConf

object Demo {

  def main(args: Array[String]): Unit = {

    val conf = SiskinConf()
    conf.loadPropertiesToZookeeper()

    conf.getString("lib.path", ".")

    conf.close()

  }

}
