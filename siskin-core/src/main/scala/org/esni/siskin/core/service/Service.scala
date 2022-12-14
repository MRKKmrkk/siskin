package org.esni.siskin.core.service

import grizzled.slf4j.Logging

abstract class Service extends Logging {
  def start()

  def setup()

  //
  def closeup()
}
