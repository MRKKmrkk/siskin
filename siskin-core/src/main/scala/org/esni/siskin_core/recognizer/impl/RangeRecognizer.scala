package org.esni.siskin_core.recognizer.impl

import org.esni.siskin_core.recognizer.Recognizer

class RangeRecognizer(recognizerName: String, min: Int, max: Int) extends Recognizer{

  override def getFlag(value: Long): Boolean = value >= min && value <= max

  override def getRecognizerName: String = recognizerName

}
