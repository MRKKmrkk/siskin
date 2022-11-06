package org.esni.siskin_core.rule

class BooleanWithNull(var flag: Int)

object BooleanWithNull {

  val TRUE: BooleanWithNull = new BooleanWithNull(1)

  val FALSE: BooleanWithNull = new BooleanWithNull(0)

  val NULL: BooleanWithNull = new BooleanWithNull(-1)

}
