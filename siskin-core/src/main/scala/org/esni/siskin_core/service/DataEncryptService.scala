package org.esni.siskin_core.service

import org.apache.flink.streaming.api.scala._
import org.apache.logging.log4j.scala.Logging
import org.esni.siskin_core.bean.Data
import org.esni.siskin_core.conf.CoreConf
import org.esni.siskin_core.encryptor.Encryptor
import org.esni.siskin_core.util.ReflectUtil

import scala.collection.JavaConverters._
import scala.collection.mutable

class DataEncryptService extends Logging{

  logger.info("create service: DataEncryptService")

  def encrypt(inputStream: DataStream[Data]): DataStream[Data] = {
    val encryptors: mutable.Seq[Encryptor] = CoreConf
      .properties
      .getListProperty("ETL_Encryptors")
      .asScala
      .map(ref => ReflectUtil.reflect[Encryptor](ref))

    var curStream = inputStream
    encryptors
      .foreach{
        encryptor =>
          curStream = curStream.map(encryptor)
      }

    curStream

  }

}
