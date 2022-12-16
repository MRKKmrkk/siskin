package org.esni.siskin.core.source.impl
import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.esni.siskin.core.source.DataSource

class KafkaDataSource extends DataSource {


  override def collect(): String = ???

  override def setup(): Unit = ???

  override def close(): Unit = ???

}
