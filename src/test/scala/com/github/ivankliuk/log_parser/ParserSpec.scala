package com.github.ivankliuk.log_parser

import com.github.ivankliuk.log_parser.Constants.ZonedDateTimFormatter
import com.github.ivankliuk.log_parser.Models.{LogLine, Session, UserId}
import org.scalatest.funsuite.AnyFunSuite

import java.time.ZonedDateTime

class ParserSpec extends AnyFunSuite {

  import ParserSpec._

  test("Append a transaction") {
    val expected = Vector(
      Session(ts("07/Apr/2021:13:00:00 -0500"), ts("07/Apr/2021:13:14:00 -0500")),
      Session(ts("07/Apr/2021:14:11:00 -0500"), ts("07/Apr/2021:14:19:00 -0500"))
    )
    val actual = service.getSessions(userId)
    assert(actual == expected)
  }
}

object ParserSpec {
  val userId = new UserId("ivan")
  val path = "/dev/null"

  def ts(timestamp: String): ZonedDateTime = ZonedDateTime.parse(timestamp, ZonedDateTimFormatter)

  val backend: UserLogLineDB = Map(
    userId -> List(
      LogLine(userId, ts("07/Apr/2021:13:00:00 -0500"), path),
      LogLine(userId, ts("07/Apr/2021:13:05:00 -0500"), path),
      LogLine(userId, ts("07/Apr/2021:13:14:00 -0500"), path),
      LogLine(userId, ts("07/Apr/2021:14:11:00 -0500"), path),
      LogLine(userId, ts("07/Apr/2021:14:19:00 -0500"), path)
    )
  )

  val db = new UserRepositoryInMemory(backend)
  val service = new ReportingServiceImpl(db)
}