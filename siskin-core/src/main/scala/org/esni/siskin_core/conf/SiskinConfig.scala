package org.esni.siskin_core.conf

import com.mrkk.sp.properties.SProperties
import org.apache.logging.log4j.scala.Logging

object SiskinConfig extends Logging{

  logger.info("create SiskinConfig")

  logger.debug("load property siskin-redis.properties")
  private val redisProperties: SProperties = SProperties.createSProperties("siskin-redis.properties")
  logger.debug("load property siskin-kafka.properties")
  private val kafkaProperties: SProperties = SProperties.createSProperties("siskin-kafka.properties")
  logger.debug("load property siskin-core.properties")
  private val coreProperties: SProperties = SProperties.createSProperties("siskin-core.properties")

  // redis连接地址
  val REDIS_HOST: String = redisProperties.getProperty("REDIS_HOST")
  logger.debug(f"load property REDIS_HOST: $REDIS_HOST")
  // redis连接端口
  val REDIS_PORT: Int = redisProperties.getIntProperty("REDIS_PORT", 6379)
  logger.debug(f"load property REDIS_PORT: $REDIS_PORT")
  // redis密码
  val REDIS_PASSWORD: String = redisProperties.getProperty("REDIS_PASSWORD")
  logger.debug(f"load property REDIS_PASSWORD: $REDIS_PASSWORD")
  // redis最大连接数
  val REDIS_MAX_CONNECTION: Int = redisProperties.getIntProperty("REDIS_MAX_CONNECTION")
  logger.debug(f"load property REDIS_MAX_CONNECTION: $REDIS_MAX_CONNECTION")
  // redis最大空闲数
  val REDIS_MAX_FREE_CONNECTION: Int = redisProperties.getIntProperty("REDIS_MAX_FREE_CONNECTION")
  logger.debug(f"load property REDIS_MAX_FREE_CONNECTION: $REDIS_MAX_FREE_CONNECTION")
  // 最小空闲连接数
  val REDIS_MIN_FREE_CONNECTION: Int = redisProperties.getIntProperty("REDIS_MIN_FREE_CONNECTION")
  logger.debug(f"load property REDIS_MIN_FREE_CONNECTION: $REDIS_MIN_FREE_CONNECTION")
  // 最长等待时间
  val REDIS_MAX_WAIT_MILLS: Int = redisProperties.getIntProperty("REDIS_MAX_WAIT_MILLS")
  logger.debug(f"load property REDIS_MAX_WAIT_MILLS: $REDIS_MAX_WAIT_MILLS")
  // 超时时间
  val REDIS_TIMEOUT: Int = redisProperties.getIntProperty("REDIS_TIMEOUT")
  logger.debug(f"load property REDIS_TIMEOUT: $REDIS_TIMEOUT")
  // Redis数据库
  val REDIS_DATABASE_INDEX: Int = redisProperties.getIntProperty("REDIS_DATABASE_INDEX")
  logger.debug(f"load property REDIS_DATABASE_INDEX: $REDIS_DATABASE_INDEX")


  // kafka 主机地址
  val KAFKA_BOOTSTRAP_SERVERS: String = kafkaProperties.getProperty("KAFKA_BOOTSTRAP_SERVERS")
  logger.debug(f"load property KAFKA_BOOTSTRAP_SERVERS: $KAFKA_BOOTSTRAP_SERVERS")
  // kafka 消费主题
  val KAFKA_COLLECT_CHANNEL_TOPIC: String = kafkaProperties.getProperty("KAFKA_COLLECT_CHANNEL_TOPIC")
  logger.debug(f"load property KAFKA_COLLECT_CHANNEL_TOPIC: $KAFKA_COLLECT_CHANNEL_TOPIC")
  // kafka etl channel主题
  val KAFKA_ETL_CHANNEL_TOPIC: String = kafkaProperties.getProperty("KAFKA_ETL_CHANNEL_TOPIC")
  logger.debug(f"load property KAFKA_ETL_CHANNEL_TOPIC: $KAFKA_ETL_CHANNEL_TOPIC")

  // 水位设置
  val ETL_LINK_COUNT_WATERMARK: Long = coreProperties.getLongProperty("ETL_LINK_COUNT_WATERMARK")
  logger.debug(f"load property ETL_LINK_COUNT_WATERMARK: $ETL_LINK_COUNT_WATERMARK")



}
