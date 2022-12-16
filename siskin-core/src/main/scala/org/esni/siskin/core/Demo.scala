package org.esni.siskin.core

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.esni.siskin.core.conf.SiskinConf
import org.esni.siskin.core.service.Service
import org.esni.siskin.core.service.impl.DataSourceService
import org.esni.siskin.core.source.function.DataSourceFunction
import org.esni.siskin.core.util.ReflectUtil

class TestService() extends Service[String, String]{
  override def start(ds: DataStream[String]): DataStream[String] = null
  override def setup(): Unit = println("setup")
  override def close(): Unit = {}
}

object Demo {

  def main(args: Array[String]): Unit = {

    println(SiskinConf.TEST)

    val ts = ReflectUtil.reflect[TestService]("org.esni.siskin.core.TestService")
    ts.setup()

    val service = ReflectUtil.reflectAndReload[Service[String, String]]("org.example.AutoService")
    service.setup()

    val env = StreamExecutionEnvironment.getExecutionEnvironment
    val testDataService = new DataSourceService()


    testDataService.setup()
    val value = testDataService.start(env)
    value.print()
    env.execute()

  }

}
