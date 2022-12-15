package org.esni.siskin.core.source

import grizzled.slf4j.Logging
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}

trait DataSource extends Logging {

  def start(env: StreamExecutionEnvironment): DataStream[String]

  def collect(): String

  def setup()

  def close()

}
