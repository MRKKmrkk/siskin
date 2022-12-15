package org.esni.siskin.core

import org.apache.flink.streaming.api.scala.DataStream
import org.esni.siskin.core.conf.SiskinConf
import org.esni.siskin.core.service.Service
import org.esni.siskin.core.util.ReflectUtil

class TestService() extends Service[String, String]{
  override def start(ds: DataStream[String]): DataStream[String] = null
  override def setup(): Unit = println("setup")
  override def closeup(): Unit = {}
}

object Demo {

  def main(args: Array[String]): Unit = {

    println(SiskinConf.TEST)

    val ts = ReflectUtil.reflect[TestService]("org.esni.siskin.core.TestService")
    ts.setup()

    val service = ReflectUtil.reflectAndReload[Service[String, String]]("org.example.AutoService")
    service.setup()
    service.closeup()

  }

}
