package org.esni.siskin_core.util

import org.esni.siskin_core.conf.CoreConf

object RouteUtil {

  val domain: String = CoreConf.properties.getProperty("DOMAIN_NAME")

  def getFirstRoute(url: String): String = {

    val fields = ("https*://".r replaceFirstIn(url, "")).split("/")
    if (fields.isEmpty) ""

    fields(1)

  }

  def matchFirstRoute(route: String, url: String): Boolean = {

    (f"https*://$domain/$route".r findFirstMatchIn (url)).isDefined

  }


}
