package org.esni.siskin.core.source.function

import grizzled.slf4j.Logging
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.esni.siskin.core.conf.SiskinConf
import org.esni.siskin.core.source.DataSource
import org.esni.siskin.core.util.ReflectUtil

class DataSourceFunction() extends SourceFunction[String] with Logging {

  var dataSource: DataSource = _
  var dataSourceFCN: String = _
  var running = true

  def init(): Unit = {
    dataSourceFCN = SiskinConf.DATA_SOURCE_FCN
    dataSource = ReflectUtil.reflectAndReload[DataSource](dataSourceFCN)
    dataSource.setup()
  }


  override def run(ctx: SourceFunction.SourceContext[String]): Unit = {
    while (running) {
      if (!SiskinConf.DATA_SOURCE_FCN.equals(dataSourceFCN)) {
        dataSourceFCN = SiskinConf.DATA_SOURCE_FCN
        dataSource = ReflectUtil.reflectAndReload[DataSource](dataSourceFCN)
        dataSource.setup()
      }

      ctx.collect(dataSource.collect())


    }


  }


  override def cancel(): Unit = running = false
}
