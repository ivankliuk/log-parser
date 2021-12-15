package com.github.ivankliuk.log_parser

import java.time.format.DateTimeFormatter
import scala.util.matching.Regex

object Constants {
  val ZonedDateTimFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyy:HH:mm:ss X")
  val LogExtractPattern: Regex = raw"""^(.+) (.+) (.+) (\d{2}/\w{3}/\d{4}:\d{2}:\d{2}:\d{2} [+-]\d{4}) "(.+) (.+) (HTTP/.+)"""".r
  val ReportingLineFormat = "%10s %10s %10s %10s %10s %n"
  val TopUserCount = 5
}
