package org.esni.siskin_core.encryptor

import org.apache.flink.api.common.functions.MapFunction
import org.esni.siskin_common.bean.Data

trait Encryptor extends MapFunction[Data, Data]{



}
