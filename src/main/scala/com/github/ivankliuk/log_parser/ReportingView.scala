package com.github.ivankliuk.log_parser

import com.github.ivankliuk.log_parser.Constants.ReportingLineFormat
import com.github.ivankliuk.log_parser.Models.UserReport

import scala.util.Try

class ReportingView(reportingService: ReportingService) {
  def apply(takeUsersCount: Int): ErrorOr[String] = Try {
    val userReports = for {
      (userId, pageViews) <- reportingService.takeTopUsersByPageHit(takeUsersCount)
      sessions <- Seq(reportingService.getSessions(userId))
    } yield UserReport(userId, pageViews, sessions.length, sessions.maxBy(_.length).length, sessions.minBy(_.length).length)

    val totalUniqueUsers = reportingService.totalUniqueUsers
    val reportHeader =
      s"""
         |Total unique users: $totalUniqueUsers
         |Top $takeUsersCount users:
         |""".stripMargin

    val lines = userReports.map(
      ur => String.format(ReportingLineFormat, ur.userId, ur.pageViews, ur.sessionCount, ur.longestSession, ur.shortestSession)
    ).mkString("")

    reportHeader + String.format(ReportingLineFormat, "id", "# pages", "# sess", "longest", "shortest") + lines
  }.toEither
}