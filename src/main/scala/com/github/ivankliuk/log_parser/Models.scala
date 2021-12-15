package com.github.ivankliuk.log_parser

import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit.MINUTES

object Models {
  class UserId(val value: String) extends AnyVal {
    override def toString: String = value
  }

  case class LogLine(userId: UserId, timestamp: ZonedDateTime, path: String)

  case class Session(start: ZonedDateTime, end: ZonedDateTime) {
    val length: Long = MINUTES.between(end, start).abs
  }

  case class UserReport(userId: UserId, pageViews: Int, sessionCount: Int, longestSession: Long, shortestSession: Long)
}
