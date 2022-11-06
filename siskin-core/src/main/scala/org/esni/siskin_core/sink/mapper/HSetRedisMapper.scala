package org.esni.siskin_core.sink.mapper

import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}
import org.esni.siskin_core.bean.Spider

class HSetRedisMapper[T](hashSetKey: String, keyFunc: T => String, valueFunc: T => String) extends RedisMapper[T]{

  override def getCommandDescription: RedisCommandDescription = new RedisCommandDescription(RedisCommand.HSET, hashSetKey)

  override def getKeyFromData(data: T): String = keyFunc(data)

  override def getValueFromData(data: T): String = valueFunc(data)

}
