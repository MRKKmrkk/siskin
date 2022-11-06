package org.esni.siskin_core.function

import org.apache.flink.streaming.api.functions.co.BroadcastProcessFunction
import org.apache.flink.util.Collector
import org.esni.siskin_core.bean.{Data, Spider}
import org.esni.siskin_core.util.States

class SpiderBroadcastProcessFunction extends BroadcastProcessFunction[Data, Spider, Data]{

  override def processElement(value: Data, ctx: BroadcastProcessFunction[Data, Spider, Data]#ReadOnlyContext, out: Collector[Data]): Unit = {

    if (ctx.getBroadcastState(States.spiderBroadcastMapDescriptor).contains(value.remoteAddress)) out.collect(value)

  }

  override def processBroadcastElement(value: Spider, ctx: BroadcastProcessFunction[Data, Spider, Data]#Context, out: Collector[Data]): Unit = {

    ctx
      .getBroadcastState(States.spiderBroadcastMapDescriptor)
      .put(value.ip, Unit)

  }

}
