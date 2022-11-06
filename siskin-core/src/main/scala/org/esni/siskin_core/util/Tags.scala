package org.esni.siskin_core.util

import org.apache.flink.streaming.api.scala.{OutputTag, createTypeInformation}
import org.esni.siskin_core.bean.Data

import scala.collection.mutable.ArrayBuffer

object Tags {

  private val tags: ArrayBuffer[OutputTag[Data]] = new ArrayBuffer[OutputTag[Data]]()
  tags.append(new OutputTag[Data]("1"))

  def addTag(tag: OutputTag[Data]): Unit = {

    if (!tags.contains(tag)) tags.append(tag)

  }

  def getTagList: List[OutputTag[Data]] = tags.toList

}
