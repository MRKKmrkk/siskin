package org.esni.siskin_core.service

import org.apache.flink.streaming.api.scala.DataStream
import org.apache.logging.log4j.scala.Logging
import org.esni.siskin_core.bean.Data
import org.esni.siskin_core.conf.CoreConf
import org.esni.siskin_core.filter.Filter
import org.esni.siskin_core.util.ReflectUtil

import scala.collection.JavaConverters._
import scala.collection.mutable

class DataFilterService extends Logging{

  logger.info("create service: DataFilterService")

  def filter(inputStream: DataStream[Data]): DataStream[Data] = {

    val filters: mutable.Seq[Filter] = CoreConf
      .properties
      .getListProperty("ETL_FILTERS")
      .asScala
      .map(ref => ReflectUtil.reflect[Filter](ref))

    var curStream = inputStream
    filters
      .foreach{
        filter =>
          curStream = curStream.filter(filter)
      }

    curStream

  }

}
