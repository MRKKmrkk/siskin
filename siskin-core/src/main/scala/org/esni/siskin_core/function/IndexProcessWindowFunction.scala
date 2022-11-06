package org.esni.siskin_core.function

import com.alibaba.fastjson.JSON
import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.function.ProcessWindowFunction
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.apache.logging.log4j.scala.Logging
import org.esni.siskin_core.bean.{AggregateIndexes, Indexes, Spider}
import org.esni.siskin_core.recognizer.Recognizer
import org.esni.siskin_core.recognizer.impl.RangeRecognizer
import org.esni.siskin_core.rule.{LogicalConnector, RuleGroup, RuleGroupController}
import org.esni.siskin_core.util.JedisConnectionPoolUtil

import scala.collection.JavaConversions._

class IndexProcessWindowFunction(sirId: Int) extends ProcessWindowFunction[AggregateIndexes, Spider, String, TimeWindow] with Logging{

  logger.info("create flink window function in SpiderIdentifyService: IndexWindowFunction")

  var json: ValueState[String] = _
  var ruleGroupController: RuleGroupController = _

  def updateGroupController(): Unit = {

    val jedis = JedisConnectionPoolUtil.getJedis
    val newJson = jedis
      .hget("spider-identified-rules", sirId.toString)
    jedis.close()

    if (newJson == json.value()) return

    val ruleConfig = JSON.parseObject(newJson)
      .getJSONObject("identified")

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

    ruleGroupController = new RuleGroupController(ruleGroups, groupLogicalConnectors)
    json.update(newJson)

  }

  override def open(parameters: Configuration): Unit = {

    json = getRuntimeContext
      .getState[String](
        new ValueStateDescriptor[String](
          "json",
          classOf[String]
        )
      )

  }

  override def process(key: String, context: Context, elements: Iterable[AggregateIndexes], out: Collector[Spider]): Unit = {

    logger.debug("close window")

    updateGroupController()

    elements
      .foreach {
        aggregateIndexes =>
          if (ruleGroupController.getFlag(aggregateIndexes.userAgentCount, aggregateIndexes.cookieCount, aggregateIndexes.visitTimesCount, aggregateIndexes.miniNumAccessIntervalTimesCount)){
            out.collect(Spider(key, sirId, context.window.getStart))
          }
      }

  }

}
