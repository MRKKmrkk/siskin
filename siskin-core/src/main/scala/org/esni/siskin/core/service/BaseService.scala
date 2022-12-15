package org.esni.siskin.core.service

import grizzled.slf4j.Logging

trait BaseService extends Logging {

  def setup

  def close

}
