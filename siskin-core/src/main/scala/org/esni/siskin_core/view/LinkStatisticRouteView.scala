package org.esni.siskin_core.view

case class LinkStatisticRouteView(
                                   serverAddress: String,
                                   request: String,
                                   count: Long,
                                   timestamp: Long
                                 )
