package org.esni.siskin_core.recognizer

trait Recognizer extends Serializable {

  def getFlag(value: Long): Boolean

  def getRecognizerName: String

}
