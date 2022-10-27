package org.esni.siskin_core.rule

import com.alibaba.fastjson.{JSON, JSONObject}
import org.apache.flink.streaming.api.scala.function.WindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.esni.siskin_common.bean.Data
import org.esni.siskin_core.bean.{AggregateIndexes, Spider}
import org.esni.siskin_core.recognizer.Recognizer
import org.esni.siskin_core.recognizer.impl.RangeRecognizer

import scala.collection.JavaConversions._

// [IN, OUT, KEY, W <: Window]
class IndexWindowFunction(ruleConfig: JSONObject, sirId: Int) extends WindowFunction[AggregateIndexes, Spider, String, TimeWindow] {

  // 创建规则
  val ruleGroups: Array[RuleGroup] = ruleConfig
    .getJSONArray("groups")
    .map(x => JSON.parseObject(x.toString))
    .map {
      ruleGroupConfig =>

        val recognizers: Array[Recognizer] = ruleGroupConfig
          .getJSONArray("is")
          .map(x => JSON.parseObject(x.toString))
          .map {
            is =>
              new RangeRecognizer(is.getString("name"), is.getIntValue("min"), is.getIntValue("max"))
          }
          .toArray

        val logicalConnectors = ruleGroupConfig
          .getJSONArray("is_logical_connect")
          .map(_.toString)
          .map{
            case "and" => LogicalConnector.AND
            case "or" => LogicalConnector.OR
          }
          .toArray

        new RuleGroup(recognizers, logicalConnectors)

    }
    .toArray

  val groupLogicalConnectors: Array[LogicalConnector] = ruleConfig
    .getJSONArray("groups_logical_connect")
    .map(_.toString)
    .map {
      case "and" => LogicalConnector.AND
      case "or" => LogicalConnector.OR
    }
    .toArray

  val ruleGroupController: RuleGroupController = new RuleGroupController(ruleGroups, groupLogicalConnectors)

  override def apply(key: String, window: TimeWindow, input: Iterable[AggregateIndexes], out: Collector[Spider]): Unit = {

    input
      .foreach {
        aggregateIndexes =>
          if (ruleGroupController.getFlag(aggregateIndexes.userAgentCount, aggregateIndexes.cookieCount, aggregateIndexes.visitTimesCount, aggregateIndexes.miniNumAccessIntervalTimesCount)){
            out.collect(Spider(key, sirId))
          }
      }

  }

}
