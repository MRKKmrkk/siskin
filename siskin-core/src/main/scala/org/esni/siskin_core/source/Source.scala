package org.esni.siskin_core.source

import org.apache.flink.streaming.api.scala._
import org.apache.logging.log4j.scala.Logging

trait Source[T] extends Logging{

  def open(env: StreamExecutionEnvironment): Unit

  def close(env: StreamExecutionEnvironment): Unit

  def getDataStream(env: StreamExecutionEnvironment): DataStream[T]

}
