package com.github.ivankliuk.log_parser

import com.github.ivankliuk.log_parser.Models.{Session, UserId}

import java.time.temporal.ChronoUnit.MINUTES


trait ReportingService {
  def db: UserRepository

  def totalUniqueUsers: Int = db.getAllUserIds.size

  def takeTopUsersByPageHit(n: Int): Seq[(UserId, Int)] =
    db
      .getAllUserIds
      .map(userId => (userId, db.getViewedPagesCount(userId)))
      .collect { case (userId, Some(count)) => (userId, count) }
      .toVector
      .sortBy { case (_, hitCount) => hitCount }(Ordering[Int].reverse)
      .take(n)

  def getSessions(userId: UserId): Seq[Session] = {
    val timestampsSorted = db.getTimestampsSorted(userId)
    timestampsSorted.foldLeft(Vector[Session]()) {
      case (sessions, timestamp) if sessions.isEmpty =>
        Vector(Session(timestamp, timestamp))
      case (sessions, timestamp) if MINUTES.between(sessions.last.end, timestamp) <= 10 =>
        sessions.updated(sessions.length - 1, sessions.last.copy(end = timestamp))
      case (sessions, timestamp) =>
        sessions :+ Session(timestamp, timestamp)
    }

  }
}

class ReportingServiceImpl(val db: UserRepository) extends ReportingService
