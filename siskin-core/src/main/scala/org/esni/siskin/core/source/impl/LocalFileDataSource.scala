package org.esni.siskin.core.source.impl

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.esni.siskin.core.source.DataSource

class LocalFileDataSource extends DataSource{
  override def start(env: StreamExecutionEnvironment): DataStream[String] = {

    val inputpath = "D:\\1\\gitproject\\siskin\\siskin-core\\src\\main\\resources\\testdata.txt"
    val stream2 = env.readTextFile(inputpath)
    stream2

  }

  override def collect(): String = ???

  override def setup(): Unit = ???

  override def close(): Unit = ???
}

