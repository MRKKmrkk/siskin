package org.esni.siskin_core.sink

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.sink.{RichSinkFunction, SinkFunction}
import org.esni.siskin_core.util.SiskinDBConnectionPoolUtil
import org.esni.siskin_core.view.LinkStatisticRouteView

import java.sql.{Connection, PreparedStatement, Timestamp}

class MysqlSpiderLinkStatisticRouteSink extends RichSinkFunction[LinkStatisticRouteView] {

  var connection: Connection = _
  var ps: PreparedStatement = _

  override def open(parameters: Configuration): Unit = {

    connection = SiskinDBConnectionPoolUtil.getConnection
    ps = connection.prepareStatement("insert into spider_link_statistic(serverAddress, request, statistic_count, statistic_time) values(?, ?, ?, ?)")

  }

  override def invoke(value: LinkStatisticRouteView, context: SinkFunction.Context[_]): Unit = {

    ps.setString(1, value.serverAddress)
    ps.setString(2, value.request)
    ps.setLong(3, value.count)
    ps.setTimestamp(4, new Timestamp(value.timestamp))

    ps.execute()

  }

  override def close(): Unit = connection.close()

}
