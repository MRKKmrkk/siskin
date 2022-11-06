package org.esni.siskin_core.serialize

import org.apache.flink.api.common.serialization.DeserializationSchema
import org.apache.flink.api.common.typeinfo.TypeInformation
import org.esni.siskin_core.bean.{Data, SerializeData}
import org.esni.siskin_core.util.SerializationUtil

class DataDeserializationSchema extends DeserializationSchema[SerializeData]{

  override def deserialize(message: Array[Byte]): SerializeData = SerializationUtil.deserialize(message)

  override def isEndOfStream(nextElement: SerializeData): Boolean = false

  override def getProducedType: TypeInformation[SerializeData] = TypeInformation.of(classOf[SerializeData])

}
