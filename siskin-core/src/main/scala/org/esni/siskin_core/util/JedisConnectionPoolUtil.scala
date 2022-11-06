package org.esni.siskin_core.util

import org.esni.siskin_core.conf.SiskinConfig
import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

object JedisConnectionPoolUtil {

  private var jedisPool: JedisPool = _
  init()

  def init(): Unit = {

    val jedisPoolConfig: JedisPoolConfig = new JedisPoolConfig()
    jedisPoolConfig.setMaxTotal(SiskinConfig.REDIS_MAX_CONNECTION)
    jedisPoolConfig.setMaxIdle(SiskinConfig.REDIS_MAX_FREE_CONNECTION)
    jedisPoolConfig.setMinIdle(SiskinConfig.REDIS_MIN_FREE_CONNECTION)
    jedisPoolConfig.setMaxWaitMillis(SiskinConfig.REDIS_MAX_WAIT_MILLS)

    jedisPool = new JedisPool(
      jedisPoolConfig,
      SiskinConfig.REDIS_HOST,
      SiskinConfig.REDIS_PORT,
      SiskinConfig.REDIS_TIMEOUT,
      SiskinConfig.REDIS_PASSWORD,
      SiskinConfig.REDIS_DATABASE_INDEX
    )

  }

  def getJedis: Jedis = {

    jedisPool.getResource

  }

}
