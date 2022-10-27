package org.esni.siskin_core.bean

import scala.collection.mutable

case class AggregateIndexes(
                             userAgentCount: Long,
                             cookieCount: Long,
                             visitTimesCount: Long,
                             miniNumAccessIntervalTimesCount: Long
                           )
