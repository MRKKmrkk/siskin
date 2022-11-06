package org.esni.siskin_core.function

import com.alibaba.fastjson.{JSON, JSONArray}
import org.apache.flink.api.common.state.MapStateDescriptor
import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction
import org.apache.flink.streaming.api.scala._
import org.apache.flink.util.Collector
import org.esni.siskin_core.bean.{Data, IdentifiedRule}
import org.esni.siskin_core.util.Tags

import scala.collection.JavaConversions._

class IdentifiedSplitStreamBroadcastProcessFunction extends BroadcastProcessFunction[Data, IdentifiedRule, Data] {

  override def processElement(value: Data, ctx: BroadcastProcessFunction[Data, IdentifiedRule, Data]#ReadOnlyContext, out: Collector[Data]): Unit = {

    ctx
      .getBroadcastState(new MapStateDescriptor[Int, String](
        "identifiedRulesBroadcastMapStateDescriptor",
        classOf[Int],
        classOf[String]
      ))
      .immutableEntries()
      .foreach {
        entity =>
          val routes: JSONArray = JSON
            .parseObject(entity.getValue)
            .getJSONObject("limit")
            .getJSONObject("route")
            .getJSONArray("links")

          for (route <- routes) {
            if (value.request.startsWith(route.toString.replace("*", ""))) {
              val tag = new OutputTag[Data](entity.getKey.toString)
              Tags.addTag(tag)
              ctx.output(tag, value)
            }
          }

      }



  }

  override def processBroadcastElement(value: IdentifiedRule, ctx: BroadcastProcessFunction[Data, IdentifiedRule, Data]#Context, out: Collector[Data]): Unit = {

    ctx
      .getBroadcastState(new MapStateDescriptor[Int, String](
        "identifiedRulesBroadcastMapStateDescriptor",
        classOf[Int],
        classOf[String]
      ))
      .put(value.id, value.rule)

  }

}
