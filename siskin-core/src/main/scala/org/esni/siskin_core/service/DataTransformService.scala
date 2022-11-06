package org.esni.siskin_core.service

import org.apache.flink.streaming.api.scala._
import org.apache.logging.log4j.scala.Logging
import org.esni.siskin_core.bean.Data
import org.esni.siskin_core.conf.CoreConf
import org.esni.siskin_core.transform.Transform
import org.esni.siskin_core.util.ReflectUtil

import scala.collection.JavaConverters._
import scala.collection.mutable

class DataTransformService extends Logging{

  logger.info("create service: DataTransformService")

  def transform(inputStream: DataStream[Data]): DataStream[Data] = {
    val transforms: mutable.Seq[Transform] = CoreConf
      .properties
      .getListProperty("ETL_TRANSFORMS")
      .asScala
      .map(ref => ReflectUtil.reflect[Transform](ref))

    var curStream = inputStream
    transforms
      .foreach{
        transform =>
          curStream = curStream.map(transform)
      }

    curStream

  }

}
