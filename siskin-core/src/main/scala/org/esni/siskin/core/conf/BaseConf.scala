package org.esni.siskin.core.conf

import grizzled.slf4j.Logging

import java.io.{FileInputStream, InputStream}
import java.nio.file.Paths
import java.util.Properties

class BaseConf(confName: String) extends Logging{

  val DEBUG = true
  val prop = new Properties()
  load()

  /**
   * 从 $SISKIN_HOME/conf/confName.properties 中读取配置
   */
  def load(): Unit = {

    var source: InputStream = null
    var path: String = ""

    if (DEBUG) {
      source = Thread.currentThread()
        .getContextClassLoader
        .getResourceAsStream(f"${confName}.properties")
    }
    else {
      path = Paths.get(System.getenv("SISKIN_HOME"), "conf", f"${confName}.properties").toString
      source = new FileInputStream(path)
    }

    prop.load(
      source
    )
    logger.info(s"load configuration file: ${path}")

  }

  def getString(key: String, defaultVal: String): String = {
    val value = prop.getProperty(key)
    if (value == null) defaultVal else value
  }

  def getString(key: String): String = {
    prop.getProperty(key)
  }


  def getInt(key: String, defaultVal: Int): Int = {
    val value = prop.getProperty(key)
    if (value == null) defaultVal else value.toInt
  }

  def getInt(key: String): Int = {
    prop.getProperty(key).toInt
  }

  def getLong(key: String, defaultVal: Long): Long = {
    val value = prop.getProperty(key)
    if (value == null) defaultVal else value.toLong
  }

  def getLong(key: String): Long = {
    prop.getProperty(key).toLong
  }

}
