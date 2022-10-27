package org.esni.siskin_core.util

import org.apache.flink.streaming.connectors.redis.RedisSink
import org.apache.flink.streaming.connectors.redis.common.config.FlinkJedisPoolConfig
import org.esni.siskin_core.conf.SiskinConfig
import org.esni.siskin_core.sink.mapper.HSetRedisMapper

object RedisSinkUtil {

  private val flinkJedisPoolConfig = new FlinkJedisPoolConfig.Builder()
    .setHost(SiskinConfig.REDIS_HOST)
    .setPort(SiskinConfig.REDIS_PORT)
    .setDatabase(SiskinConfig.REDIS_DATABASE_INDEX)
    .setPassword(SiskinConfig.REDIS_PASSWORD)
    .build()

  def getHSetRedisSink[T](hashSetKey: String, keyFunc: T => String, valueFunc: T => String): RedisSink[T] = {

    new RedisSink[T](
      flinkJedisPoolConfig,
      new HSetRedisMapper[T](
        hashSetKey,
        keyFunc,
        valueFunc
      )
    )

  }

}
