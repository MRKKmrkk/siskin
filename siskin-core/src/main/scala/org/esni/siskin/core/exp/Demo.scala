package org.esni.siskin.core.exp

import org.apache.flink.streaming.api.scala.DataStream
import org.esni.siskin.core.service.Service
import org.esni.siskin.core.util.ReflectUtil

class TestService() extends Service[String, String]{
  override def start(ds: DataStream[String]): DataStream[String] = null
  override def setup(): Unit = println("setup")
  override def close(): Unit = {}
}

case class Cat(name: String)

object Demo {

  def main(args: Array[String]): Unit = {

    val cat = Cat("catlin")
    println(ReflectUtil.getFieldFromScalaCastClass[String, Cat](cat, "name"))

  }

}
