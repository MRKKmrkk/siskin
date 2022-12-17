package org.esni.siskin.core.conf

import org.apache.flink.shaded.curator.org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.flink.shaded.curator.org.apache.curator.retry.ExponentialBackoffRetry
import org.apache.flink.shaded.zookeeper.org.apache.zookeeper.CreateMode
import org.esni.siskin.core.conf.listener.PropertiesListener

import java.util.Properties

/**
 * 基于zookeeper的配置中心
 */
class ZookeeperConf(private val client: CuratorFramework, private val zkConf: BaseConf) {

  lazy val PROPERTY_PREFIX: String = zkConf.getString("PROPERTY_PREFIX", "/siskin-conf/")
  lazy val PROPERTY_CHARSET: String = "utf-8"
  private lazy val prop: Properties = new Properties()
  var isListening: Boolean = false

  private def getPath(key: String): String = {

    PROPERTY_PREFIX + key.replace(".", "/")

  }

  // 更新配置项到zookeeper
  def setProperty(key: String, value: String): Unit = {

    val path = getPath(key)

    // 判断节点是否存在
    if (client.checkExists().forPath(path) != null) {
      client
        .setData()
        .forPath(path, value.getBytes(PROPERTY_CHARSET))
    }
    else {
      client
        .create()
        .creatingParentsIfNeeded()
//        .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
        .forPath(path, value.getBytes(PROPERTY_CHARSET))
    }

  }

  def getProperty(key: String): String = {

    // 若未启动监听器，则开始开始监听,并直接通过zk获取配置
    if (!isListening) {
      val listener = new PropertiesListener(client, prop, PROPERTY_PREFIX.length)
      listener.start()
      listener.join()
      isListening = true

      val path = getPath(key)
      if (client.checkExists().forPath(path) != null) {
        new String(
          client
            .getData
            .forPath(path)
        )
      }
      else {
        null
      }
    }
    else {
      prop.getProperty(key)
    }

  }

  def getProperty(key: String, defaultVal: String): String = {

    val value = getProperty(key)
    if (value == null) defaultVal else value

  }

  def getString(key: String, defaultVal: String): String = getProperty(key, defaultVal)

  def getString(key: String): String = getProperty(key)

  def getInt(key: String, defaultVal: Int): Int = getString(key, defaultVal.toString).toInt

  def getInt(key: String): Int = getProperty(key).toInt

  def getLong(key: String, defaultVal: Long): Long = getString(key, defaultVal.toString).toLong

  def getLong(key: String): Long = getProperty(key).toLong

  def close(): Unit = {

    if (client != null) {
      client.close()
    }

  }

}

