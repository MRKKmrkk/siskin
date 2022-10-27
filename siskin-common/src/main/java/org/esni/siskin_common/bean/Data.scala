package org.esni.siskin_common.bean

import scala.collection.mutable

/**
 * 数据封装:
 *
 *  Requst 请求的连接
 *  Request Method 请求的方法
 *  Remote Address 客户端地址
 *  Request parameter 请求参数（包括 Form 表单）
 *  Content-Type "Content-Type" 请求头字段
 *  Cookie 请求 cookie
 *  Server Address 服务器地址
 *  Referer 跳转来源
 *  User-Agent 用户终端浏览器信息
 *  Time-Iso8601 访问时间 ISO 格式
 *  Time_local 访问时间
 */
case class Data(
               request: String,
               method: String,
               remoteAddress: String,
               requestParameter: String,
               contentType: String,
               cookie: String,
               serverAddress: String,
               Referer: String,
               userAgent: String,
               timeISO8601: String,
               timeLocal: String,
               metaData: mutable.Map[String, Any]
               )
