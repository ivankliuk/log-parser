package com.github.ivankliuk.log_parser

import com.github.ivankliuk.log_parser.Constants.ZonedDateTimFormatter
import com.github.ivankliuk.log_parser.Models.{LogLine, UserId}

import java.io.File
import java.time.ZonedDateTime
import scala.util.Try
import scala.util.matching.Regex

object Utils {
  def getLogLine(regexMatch: Regex.Match): ErrorOr[LogLine] = Try {
    val timestamp = regexMatch.group(4)
    val path = regexMatch.group(6)
    val userId = new UserId(path.split("/")(3))
    val timestampParsed = ZonedDateTime.parse(timestamp, ZonedDateTimFormatter)
    Models.LogLine(userId, timestampParsed, path)
  }.toEither

  def getListOfFilenames(directory: String): ErrorOr[List[String]] = Try {
    val d = new File(directory)
    d.listFiles.filter(_.isFile).toList.map(_.getPath)
  }.recover {
    case _ => throw new Exception(s"Unable to read from $directory")
  }.toEither

  def getPathFromCmd(args: Array[String]): ErrorOr[String] = args match {
    case Array(path) => Right(path)
    case other => Left(
      new Exception(s"Please provide a directory to read from. You passed: ${other.mkString(" ")}")
    )

  }
}
