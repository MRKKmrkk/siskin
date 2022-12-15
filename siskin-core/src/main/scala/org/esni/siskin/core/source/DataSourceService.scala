package org.esni.siskin.core.source

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.esni.siskin.core.service.BaseService

class DataSourceService extends BaseService {

  def start(env: StreamExecutionEnvironment): DataStream[String]={
    val value = env.addSource(new DataSourceFunction())
    value
  }

  override def setup(): Unit = ???

  override def close: Unit = ???
}
