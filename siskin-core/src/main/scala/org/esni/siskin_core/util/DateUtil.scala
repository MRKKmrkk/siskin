package org.esni.siskin_core.util

import org.apache.flink.streaming.api.windowing.time.Time

import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

object DateUtil {

  private val sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

  def getNowDate: String = {

    sdf.format(
      new Date()
    )

  }

  def getTime(hour: Long, min: Long, sec: Long): Time = {

    Time.of(
      Time.hours(hour).toMilliseconds + Time.minutes(min).toMilliseconds + Time.seconds(sec).toMilliseconds,
      TimeUnit.MILLISECONDS
    )

  }

  def getTime(hour: Int, min: Int, sec: Int): Time = getTime(hour.toLong, min.toLong, sec.toLong)

}


