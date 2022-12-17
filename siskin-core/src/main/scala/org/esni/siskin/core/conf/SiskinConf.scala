package org.esni.siskin.core.conf

import org.apache.flink.shaded.curator.org.apache.curator.framework.{CuratorFramework, CuratorFrameworkFactory}
import org.apache.flink.shaded.curator.org.apache.curator.retry.ExponentialBackoffRetry

import scala.collection.JavaConversions._

class SiskinConf(private val client: CuratorFramework, private val zkConf: BaseConf) extends ZookeeperConf(client, zkConf) {

  // 将本地配置文件载入zk
  def loadPropertiesToZookeeper(): Unit = {

    val coreConf = new BaseConf("siskin-core")
    for (k <- coreConf.prop.keySet()) {
      setProperty(k.toString, coreConf.getString(k.toString))
    }

  }

}

object SiskinConf {

  private var SISKIN_CONF: SiskinConf = _

  def getOrCreate(): SiskinConf = {

    if (SiskinConf != null) {
      return SISKIN_CONF
    }

    val conf = new BaseConf("zookeeper")

    val client = CuratorFrameworkFactory
      .newClient(
        conf.getString(
          "ZOOKEEPER_CONNECT_URL",
          "127.0.0.1:2181"
        ),
        new ExponentialBackoffRetry(
          1000, 3
        )
      )
    client.start()

    SISKIN_CONF = new SiskinConf(client, conf)
    SISKIN_CONF

  }

}
