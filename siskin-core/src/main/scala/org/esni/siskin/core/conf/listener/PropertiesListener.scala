package org.esni.siskin.core.conf.listener

import grizzled.slf4j.Logging
import org.apache.flink.shaded.curator.org.apache.curator.framework.CuratorFramework
import org.apache.flink.shaded.curator.org.apache.curator.framework.recipes.cache.{TreeCache, TreeCacheEvent, TreeCacheListener}

import java.util.{Optional, Properties}
import scala.collection.mutable

class PropertiesListener(client: CuratorFramework, prop: Properties, prefixLen: Int) extends Thread with Logging{

  var isRun: Boolean = true

  private def getKey(path: String): String = {

    path.substring(prefixLen, path.length).replace("/", ".")

  }

  def stopListener(): Unit = isRun = false

  override def run(): Unit = {

    logger.info("staring register properties listener")

    val treeCache = new TreeCache(client, "/siskin-conf")

    treeCache
      .getListenable
      .addListener(new TreeCacheListener {
        override def childEvent(curatorFramework: CuratorFramework, treeCacheEvent: TreeCacheEvent): Unit = {
          val eventType = Optional.of(treeCacheEvent.getType).orElse(null)

          // 开始监听节点事件
          if (eventType != null) {

            val data = treeCacheEvent.getData
            val path = data.getPath

            eventType match {
              case TreeCacheEvent.Type.NODE_ADDED => prop.put(getKey(path), new String(data.getData))
              case TreeCacheEvent.Type.NODE_UPDATED => prop.put(getKey(path), new String(data.getData))
              case TreeCacheEvent.Type.NODE_REMOVED => prop.remove(getKey(path))
            }
          }
        }

      })

    // 启动
    treeCache.start()

    logger.info("registered properties listener successfully")

  }

}
