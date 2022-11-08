package org.esni.siskin_core.source

import org.apache.flink.configuration.Configuration
import org.apache.flink.streaming.api.functions.source.{RichSourceFunction, SourceFunction}
import org.esni.siskin_core.util.{JedisConnectionPoolUtil, SerializationUtil, SiskinDBConnectionPoolUtil}
import redis.clients.jedis.Jedis

import scala.collection.JavaConversions._

class RedisHashSource[T](key: String, func: ((String, String)) => T) extends RichSourceFunction[T]{

  private var jedis: Jedis = _
  private var isRunning = true
  private val updateInterval = 5000L

  override def open(parameters: Configuration): Unit = {

    jedis = JedisConnectionPoolUtil.getJedis

  }

  override def run(ctx: SourceFunction.SourceContext[T]): Unit = {

    while (isRunning) {

      jedis
        .hgetAll(key)
        .map(func)
        .foreach(ctx.collect)

      Thread.sleep(updateInterval)

    }

  }

  override def cancel(): Unit = {

    isRunning = false
    jedis.close()
    // todo: oom解决措施 - 回收连接池
    JedisConnectionPoolUtil.close()
    SiskinDBConnectionPoolUtil.close()
    SerializationUtil.clear()

  }

}
