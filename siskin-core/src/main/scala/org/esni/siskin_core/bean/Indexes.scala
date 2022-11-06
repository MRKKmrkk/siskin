package org.esni.siskin_core.bean

import scala.collection.mutable

case class Indexes(
                    userAgents: mutable.Set[String],
                    cookies: mutable.Set[String],
                    var visitTimes: Long,
                    var miniNumAccessIntervalTimes: Long
                  )
