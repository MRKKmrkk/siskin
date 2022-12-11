package org.esni.siskin_core.conf

import grizzled.slf4j.Logging

import java.io.FileInputStream
import java.nio.file.Paths
import java.util.Properties

class BaseConf(confName: String) extends Logging{

  val prop = new Properties()
  load()

  /**
   * 从 $SISKIN_HOME/conf/confName.properties 中读取配置
   */
  def load(): Unit = {

    val path = Paths.get(System.getenv("SISKIN_HOME"), "conf", f"${confName}.properties").toString
    prop.load(
      new FileInputStream(path)
    )
    logger.info(s"load configuration file: ${path}")

  }

  /**
   * use specific key to get value from the properties
   * you can put a default value in your argument which can make sure you will got an default value when key not found in properties
   */
  def getValue[T](key: String, defaultVal: T)(func: String => T): T = {
    val value = prop.getProperty(key)
    if (value == null) {
      return defaultVal
    }

    func(value)

  }

  def getString(key: String, defaultVal: String = null): String = {
    getValue[String](key, defaultVal)(_ + "")
  }

  def getLong(key: String, defaultVal: Long = null): Long = {
    getValue[Long](key, defaultVal)(_.toLong)
  }

  def getInt(key: String, defaultVal: Int = null): Int = {
    getValue[Int](key, defaultVal)(_.toInt)
  }

}
