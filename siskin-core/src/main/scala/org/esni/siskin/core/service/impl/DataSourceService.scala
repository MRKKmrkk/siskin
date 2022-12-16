package org.esni.siskin.core.service.impl

import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.esni.siskin.core.service.BaseService
import org.esni.siskin.core.source.function.DataSourceFunction

class DataSourceService extends BaseService {



  def start(env: StreamExecutionEnvironment): DataStream[String] = {

    val dataSourceFunction: DataSourceFunction = new DataSourceFunction()
    dataSourceFunction.init()

    env.addSource(dataSourceFunction)

  }

  override def setup(): Unit = {
  }

  override def close(): Unit = {}

}
