package org.esni.siskin.core.service

import grizzled.slf4j.Logging
import org.apache.flink.streaming.api.scala.DataStream

abstract class Service[IN,OUT] extends Logging {
  def start(ds:DataStream[IN]):DataStream[OUT]

  def setup()

  def closeup()
}
