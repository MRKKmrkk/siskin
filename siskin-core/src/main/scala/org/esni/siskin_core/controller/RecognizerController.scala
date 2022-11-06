package org.esni.siskin_core.controller

import org.apache.flink.streaming.api.scala.{DataStream, OutputTag, createTypeInformation}
import org.esni.siskin_core.bean.Data
import org.esni.siskin_core.util.Tags

import scala.collection.mutable.ArrayBuffer

class RecognizerController(splitStream: DataStream[Data]) extends Thread {

  private val runningTags: ArrayBuffer[OutputTag[Data]] = new ArrayBuffer[OutputTag[Data]]()



  override def run(): Unit = {

    while (true) {

      Tags
        .getTagList
        .foreach{
          tag =>
            if (!runningTags.contains(tag)) {
              splitStream
                .getSideOutput(tag)
                .keyBy(_.remoteAddress)
                .print()

              runningTags.append(tag)
            }
        }

    }

  }

}
