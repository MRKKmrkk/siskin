package org.esni.siskin_core.util

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.{Input, Output}
import org.esni.siskin_core.bean.SerializeData

object SerializationUtil {

  private val kryo = new Kryo()
  kryo.register(classOf[SerializeData])

  def serialize(element: SerializeData): Array[Byte] = {

    val output = new Output(1024)
    kryo.writeObject(output, element)

    output.getBuffer

  }


  def deserialize(message: Array[Byte]): SerializeData = {

    kryo.readObject(
      new Input(message),
      classOf[SerializeData]
    )

  }

}
