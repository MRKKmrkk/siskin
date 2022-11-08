package org.esni.siskin_core.util

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import org.esni.siskin_core.bean.SerializeData
import org.esni.siskin_core.conf.SiskinConfig

object SerializationUtil {

  private val kryo = new Kryo()
  kryo.register(classOf[SerializeData])
  private val output = new Output(SiskinConfig.SERIALIZATION_BUFFER_MAX_SIZE)


  def serialize(element: SerializeData): Array[Byte] = {

    output.clear()
    kryo.writeObject(output, element)

    output.getBuffer

  }


  def deserialize(message: Array[Byte]): SerializeData = {

    kryo.readObject(
      new Input(message),
      classOf[SerializeData]
    )

  }

  def clear(): Unit = {

    output.clear()

  }

}
