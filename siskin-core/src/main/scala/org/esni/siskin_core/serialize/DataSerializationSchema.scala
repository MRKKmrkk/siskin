package org.esni.siskin_core.serialize

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryo.io.Output
import org.apache.flink.api.common.serialization.SerializationSchema
import org.esni.siskin_core.bean.{Data, SerializeData}
import org.esni.siskin_core.util.SerializationUtil

class DataSerializationSchema extends SerializationSchema[SerializeData]{

  override def serialize(element: SerializeData): Array[Byte] = {

    SerializationUtil.serialize(element)

  }

}
