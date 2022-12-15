package org.esni.siskin.core.service

import grizzled.slf4j.Logging
import org.apache.flink.streaming.api.scala.DataStream

trait Service[IN,OUT] extends Logging {

  //读取数据输出数据
  //ds：输入流
  //IN：输入流数据类型
  //OUT：输出流数据类型
  def start(ds:DataStream[IN]):DataStream[OUT]

  def setup()

  def closeup()
}
