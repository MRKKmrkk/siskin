package org.esni.siskin_core.conf

import com.mrkk.sp.properties.SProperties

@Deprecated
object CoreConf {

  val properties: SProperties = SProperties.createSProperties("siskin-core.properties")

}
