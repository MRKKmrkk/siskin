package org.esni.siskin.core

import org.apache.flink.streaming.api.scala.DataStream
import org.esni.siskin.core.service.Service

class TestService() extends Service[String, String]{
  override def start(ds: DataStream[String]): DataStream[String] = null
  override def setup(): Unit = println("setup")
  override def close(): Unit = {}
}

object Demo {

  def main(args: Array[String]): Unit = {



  }

}
