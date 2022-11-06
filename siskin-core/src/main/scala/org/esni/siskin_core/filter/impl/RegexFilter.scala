package org.esni.siskin_core.filter.impl

import org.esni.siskin_core.bean.Data
import org.esni.siskin_core.filter.Filter
import org.esni.siskin_core.util.SiskinDBConnectionPoolUtil

import java.sql.{Connection, PreparedStatement}

// todo: 需要解决序列化问题
class RegexFilter extends Filter{

//  private val connection: Connection = SiskinDBConnectionPoolUtil.getConnection
//  private val ps: PreparedStatement = connection.prepareStatement("select filed, regex_rule from regex_filter_rules")

  private def isMatch(content: String, regex: String): Boolean = {

    val maybeMatch = regex.r findFirstMatchIn (content)
    maybeMatch.isDefined

  }

  override def filter(data: Data): Boolean = {

    val connection: Connection = SiskinDBConnectionPoolUtil.getConnection
    val ps: PreparedStatement = connection.prepareStatement("select field_name, regex_rule from regex_filter_rules")

    val rs = ps.executeQuery()
    while (rs.next()) {
      val flag: Boolean = rs.getString(1) match {
        case "request" => isMatch(data.request, rs.getString(2))
        case "method" => isMatch(data.method, rs.getString(2))
        case "remoteAddress" => isMatch(data.remoteAddress, rs.getString(2))
        case "requestParameter" => isMatch(data.requestParameter, rs.getString(2))
        case "contentType" => isMatch(data.contentType, rs.getString(2))
        case "cookie" => isMatch(data.cookie, rs.getString(2))
        case "serverAddress" => isMatch(data.serverAddress, rs.getString(2))
        case "Referer" => isMatch(data.Referer, rs.getString(2))
        case "userAgent" => isMatch(data.userAgent, rs.getString(2))
        case "timeISO8601" => isMatch(data.timeISO8601, rs.getString(2))
        case "timeLocal" => isMatch(data.timeLocal, rs.getString(2))
        case "*" => isMatch(data.toString, rs.getString(2))
        case _ => true

      }

      if (!flag) return flag

    }

    SiskinDBConnectionPoolUtil.closeAll(connection, ps, rs)

    true

  }

//  def close(): Unit = {
//
//    SiskinDBConnectionPoolUtil.closeStatement(ps)
//    SiskinDBConnectionPoolUtil.closeConn(connection)
//
//  }

}
