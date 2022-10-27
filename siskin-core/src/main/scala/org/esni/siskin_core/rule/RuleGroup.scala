package org.esni.siskin_core.rule

import org.esni.siskin_core.recognizer.Recognizer
import org.esni.siskin_core.recognizer.impl.RangeRecognizer

//class RuleGroup(rules: Array[Rule], logicalConnectors: Array[LogicalConnector]) {
//
//  def getFlag: Boolean = {
//
//    new Rule(
//      rules.map(_.getFlag),
//      logicalConnectors
//    )
//      .getFlag
//
//  }
//
//}

class RuleGroup(recognizers: Array[Recognizer], logicalConnectors: Array[LogicalConnector]) extends Serializable {

  def getFlag(indexes: Array[Long]): Boolean = {

    var cur = recognizers(0).getFlag(indexes(0))
    for (i <- 1 until recognizers.length){
      cur = logicalConnectors(i - 1) match {
        case LogicalConnector.AND =>  cur && recognizers(i).getFlag(indexes(i))
        case LogicalConnector.OR => cur || recognizers(i).getFlag(indexes(i))
      }
    }

    cur

  }

  def getRecognizerNames: Array[String] = {

    recognizers
      .map(_.getRecognizerName)

  }

}
