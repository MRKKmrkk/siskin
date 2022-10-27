package org.esni.siskin_core.rule

import org.esni.siskin_core.recognizer.impl.RangeRecognizer

class Rule(flags: Array[Boolean], logicalConnectors: Array[LogicalConnector]) {

  def getFlag: Boolean = {

    var cur = flags(0)
    for (i <- 1 until flags.length){
      cur = logicalConnectors(i - 1) match {
        case LogicalConnector.AND =>  cur && flags(i)
        case LogicalConnector.OR => cur || flags(i)
      }
    }

    cur

  }

}
