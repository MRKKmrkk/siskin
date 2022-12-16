package org.esni.siskin.core.source.impl

import org.apache.flink.streaming.api.scala.{DataStream, StreamExecutionEnvironment}
import org.esni.siskin.core.source.DataSource

import scala.io.{BufferedSource, Source}

class LocalFileDataSource extends DataSource{

  val inputpath = "src/main/resources/testdata.txt"
  var lines: List[String] = _
  var cur: Int = 0

  override def collect(): String = {

    if (cur >= lines.length) {
      Thread.sleep(Long.MaxValue)
    }

    val ans = lines(cur)
    cur += 1

    ans

  }

  override def setup(): Unit = {

    val source = Source.fromFile(inputpath)
    lines = source.getLines().toList
    source.close()

  }

  override def close(): Unit = {}
}

