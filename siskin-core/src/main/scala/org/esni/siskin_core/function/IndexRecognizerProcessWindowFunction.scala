package org.esni.siskin_core.function

import org.apache.flink.api.common.state.{ValueState, ValueStateDescriptor}
import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.scala.function.{ProcessWindowFunction, WindowFunction}
import org.apache.flink.streaming.api.windowing.windows.TimeWindow
import org.apache.flink.util.Collector
import org.apache.logging.log4j.scala.Logging
import org.esni.siskin_core.bean.{AggregateIndexes, Spider}
import org.esni.siskin_core.rule.RuleGroupController
import org.esni.siskin_core.util.JedisConnectionPoolUtil

// 不保证原子性
class IndexRecognizerProcessWindowFunction(sirId: Int) extends ProcessWindowFunction[AggregateIndexes, Spider, String, TimeWindow] with Logging{

  logger.info("create flink window function in SpiderIdentifyService: IndexRecognizerWindowFunction")
  var updateTimestampValueState: ValueState[Long] = _
  var ruleGroupControllerValueState: ValueState[RuleGroupController] = _


  override def open(parameters: Configuration): Unit = {

    updateTimestampValueState = getRuntimeContext
      .getState[Long](
        new ValueStateDescriptor[Long](
          "updateTimestampValueState",
          classOf[Long]
        )
      )

    ruleGroupControllerValueState = getRuntimeContext
      .getState[RuleGroupController](
        new ValueStateDescriptor[RuleGroupController](
          "ruleGroupControllerValueState",
          classOf[RuleGroupController]
        )
      )

    val jedis = JedisConnectionPoolUtil.getJedis

    jedis
      .hget("spider-identified-rules", sirId.toString)

    updateTimestampValueState
      .update(jedis.hget("siskin-flags", "identifiedRulesUpdateTimestamp").toLong)

    jedis.close()

  }

  override def process(key: String, context: Context, elements: Iterable[AggregateIndexes], out: Collector[Spider]): Unit = {

    logger.debug("close window")

  }
}
