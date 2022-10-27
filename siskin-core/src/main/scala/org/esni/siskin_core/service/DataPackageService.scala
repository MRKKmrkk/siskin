package org.esni.siskin_core.service

import org.apache.flink.streaming.api.scala.{DataStream, createTypeInformation}
import org.esni.siskin_common.bean.Data
import org.esni.siskin_common.util.ReflectUtil
import org.esni.siskin_core.conf.CoreConf
import org.esni.siskin_core.wrapper.{MetaDataWrapper, Wrapper}
import org.esni.siskin_core.wrapper.impl.BaseWrapper

import scala.collection.JavaConverters._

class DataPackageService extends Serializable {

  private val baseWrapper: BaseWrapper = new BaseWrapper

  def packageData(inputStream: DataStream[String]): DataStream[Data] = {

    val metaDataWrappers = CoreConf
      .properties
      .getListProperty("ETL_META_DATA_WRAPPERS")
      .asScala
      .map(ref => ReflectUtil.reflect[MetaDataWrapper](ref))

    inputStream
      .flatMap{
        line =>
          val datas = baseWrapper.wrap(line)

          datas
            .foreach{
              data =>
                metaDataWrappers
                  .foreach{
                    wrapper =>
                      val kvs = wrapper.wrap(data)
                      kvs.foreach{
                        case (k, v) =>
                          data
                          .metaData
                          .put(k, v)
                      }
                  }
            }

          datas

      }

  }

}
