package org.esni.siskin_core.rule

class RuleGroupController(

                           ruleGroups: Array[RuleGroup],
                           logicalConnectors: Array[LogicalConnector]
                         ) extends Serializable {

  def getFlag(userAgentCount: Long, cookieCount: Long, visitCount: Long, miniNumAccessIntervalCount: Long): Boolean = {

    val flags: Array[Boolean] = ruleGroups
      .map {
        groupRule =>
          val values: Array[Long] = groupRule
            .getRecognizerNames
            .map{
              case "user_agent_times" => userAgentCount
              case "cookie_times" => cookieCount
              case "review_times" => visitCount
              case "interval_times" => miniNumAccessIntervalCount
            }
          groupRule.getFlag(values)
      }

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
