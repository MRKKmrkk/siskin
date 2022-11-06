package org.esni.siskin_core.encryptor.impl

import org.apache.commons.codec.digest.DigestUtils
import org.esni.siskin_core.bean.Data
import org.esni.siskin_core.encryptor.Encryptor
import org.esni.siskin_core.util.SiskinDBConnectionPoolUtil

import java.sql.{Connection, PreparedStatement}

class RegexEncryptor extends Encryptor {

  private def encryptByMethod(content: String, method: String): String = {

    method match {
      case "md5" => DigestUtils.md5Hex(content)
      case _ => content
    }

  }

  override def map(data: Data): Data = {

    val connection: Connection = SiskinDBConnectionPoolUtil.getConnection
    val ps: PreparedStatement = connection.prepareStatement("select field_name, encrypt_method from regex_encrypt_rules")

    var request: String = data.request
    var method: String = data.method
    var remoteAddress: String = data.remoteAddress
    var requestParameter: String = data.requestParameter
    var contentType: String = data.contentType
    var cookie: String = data.cookie
    var serverAddress: String = data.serverAddress
    var Referer: String = data.Referer
    var userAgent: String = data.userAgent
    var timeISO8601: String = data.timeISO8601
    var timeLocal: String = data.timeLocal

    val rs = ps.executeQuery()
    while (rs.next()) {
      rs.getString(1) match {
        case "request" => request = encryptByMethod(data.request, rs.getString(2))
        case "method" => method = encryptByMethod(data.method, rs.getString(2))
        case "remoteAddress" => remoteAddress = encryptByMethod(data.remoteAddress, rs.getString(2))
        case "requestParameter" => requestParameter = encryptByMethod(data.requestParameter, rs.getString(2))
        case "contentType" => contentType = encryptByMethod(data.contentType, rs.getString(2))
        case "cookie" => cookie = encryptByMethod(data.cookie, rs.getString(2))
        case "serverAddress" => serverAddress = encryptByMethod(data.serverAddress, rs.getString(2))
        case "Referer" => Referer = encryptByMethod(data.Referer, rs.getString(2))
        case "userAgent" => userAgent = encryptByMethod(data.userAgent, rs.getString(2))
        case "timeISO8601" => timeISO8601 = encryptByMethod(data.timeISO8601, rs.getString(2))
        case "timeLocal" => timeLocal = encryptByMethod(data.timeLocal, rs.getString(2))
      }
    }

    SiskinDBConnectionPoolUtil.closeAll(connection, ps, rs)

    Data(
      request,
      method,
      remoteAddress,
      requestParameter,
      contentType,
      cookie,
      serverAddress,
      Referer,
      userAgent,
      timeISO8601,
      timeLocal,
      data.metaData
    )

  }

}
