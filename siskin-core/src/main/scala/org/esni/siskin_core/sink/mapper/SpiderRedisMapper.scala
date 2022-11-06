package org.esni.siskin_core.sink.mapper

import org.apache.flink.streaming.connectors.redis.common.mapper.{RedisCommand, RedisCommandDescription, RedisMapper}
import org.esni.siskin_core.bean.Spider

class SpiderRedisMapper(hashSetKey: String) extends RedisMapper[Spider]{

  override def getCommandDescription: RedisCommandDescription = new RedisCommandDescription(RedisCommand.HSET, hashSetKey)

  override def getKeyFromData(data: Spider): String = data.ip

  override def getValueFromData(data: Spider): String = data.sir_id.toString

}
