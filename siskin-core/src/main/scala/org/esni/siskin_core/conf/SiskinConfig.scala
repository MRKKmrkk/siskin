package org.esni.siskin_core.conf

import com.mrkk.sp.properties.SProperties

object SiskinConfig {

  private val redisProperties: SProperties = SProperties.createSProperties("siskin-redis.properties")
  private val kafkaProperties: SProperties = SProperties.createSProperties("siskin-kafka.properties")
  private val coreProperties: SProperties = SProperties.createSProperties("siskin-core.properties")


  // redis连接地址
  val REDIS_HOST: String = redisProperties.getProperty("REDIS_HOST")
  // redis连接端口
  val REDIS_PORT: Int = redisProperties.getIntProperty("REDIS_PORT", 6379)
  // redis密码
  val REDIS_PASSWORD: String = redisProperties.getProperty("REDIS_PASSWORD")
  // redis 数据库
  val REDIS_DATABASE_INDEX: Int = redisProperties.getIntProperty("REDIS_DATABASE_INDEX")

  // kafka 主机地址
  val KAFKA_BOOTSTRAP_SERVERS: String = kafkaProperties.getProperty("KAFKA_BOOTSTRAP_SERVERS")
  // kafka 消费主题
  val KAFKA_COLLECT_CHANNEL_TOPIC: String = kafkaProperties.getProperty("KAFKA_COLLECT_CHANNEL_TOPIC")

  // 水位设置
  val ETL_LINK_COUNT_WATERMARK: Long = coreProperties.getLongProperty("ETL_LINK_COUNT_WATERMARK")



}
