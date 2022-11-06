package org.esni.siskin_core.util

import org.apache.flink.api.common.state.{ListStateDescriptor, MapStateDescriptor}

object States {

  val identifiedRulesBroadcastMapStateDescriptor = new MapStateDescriptor[Int, String](
    "identifiedRulesBroadcastMapStateDescriptor",
    classOf[Int],
    classOf[String]
  )

  val spiderBroadcastMapDescriptor = new MapStateDescriptor[String, Unit](
    "spiderBroadcastMapDescriptor",
    classOf[String],
    classOf[Unit]
  )

}
