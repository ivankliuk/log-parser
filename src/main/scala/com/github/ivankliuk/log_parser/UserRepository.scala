package com.github.ivankliuk.log_parser

import com.github.ivankliuk.log_parser.Models.UserId

import java.time.ZonedDateTime

trait UserRepository {
  def getAllUserIds: Set[UserId]
  def getViewedPagesCount(userId: UserId): Option[Int]
  def getTimestampsSorted(userId: UserId): Seq[ZonedDateTime]

}
trait UserRepositoryMapBackend extends UserRepository {
  def backend: UserLogLineDB

  def getAllUserIds: Set[UserId] = backend.keySet

  def getViewedPagesCount(userId: UserId): Option[Int] =
    backend
      .get(userId)
      .map(_.length)

  def getTimestampsSorted(userId: UserId): Seq[ZonedDateTime] =
    backend
      .get(userId)
      .map(loglines => loglines.map(_.timestamp).sorted)
      .getOrElse(Seq.empty)

}

class UserRepositoryInMemory(val backend: UserLogLineDB) extends UserRepositoryMapBackend