package org.esni.siskin_core.source.impl

import org.apache.flink.api.common.serialization.{DeserializationSchema, SimpleStringSchema}
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
import org.esni.siskin_core.bean.{Data, SerializeData}
import org.esni.siskin_core.conf.SiskinConfig
import org.esni.siskin_core.serialize.DataDeserializationSchema
import org.esni.siskin_core.source.Source

import java.util.Properties

class ChannelKafkaSource() extends Source[SerializeData]{

  var collectChannelProperties: Properties = _

  override def open(env: StreamExecutionEnvironment): Unit = {

    logger.info("create siskin source: KafkaSource")
    collectChannelProperties = new Properties()
    collectChannelProperties.setProperty("bootstrap.servers", SiskinConfig.KAFKA_BOOTSTRAP_SERVERS)

  }

  override def close(env: StreamExecutionEnvironment): Unit = {

    logger.info("close siskin source: KafkaSource")

  }

  override def getDataStream(env: StreamExecutionEnvironment): DataStream[SerializeData] = {

    env
      .addSource[SerializeData](
        new FlinkKafkaConsumer[SerializeData](
          SiskinConfig.KAFKA_ETL_CHANNEL_TOPIC,
          new DataDeserializationSchema(),
          collectChannelProperties
        ).setStartFromEarliest()
      )

  }

}
