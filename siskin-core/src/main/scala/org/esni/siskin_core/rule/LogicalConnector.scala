package org.esni.siskin_core.rule

class LogicalConnector(val flag: String) {

}

object LogicalConnector {

  val AND: LogicalConnector = new LogicalConnector("and")

  val OR: LogicalConnector = new LogicalConnector("or")

}
