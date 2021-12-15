package com.github.ivankliuk.log_parser

import cats.implicits._
import com.github.ivankliuk.log_parser.Constants.LogExtractPattern

import scala.concurrent.{ExecutionContext, Future}
import scala.io.Source
import scala.util.Try

object LogFileReader {
  def read(filename: String): ErrorOr[UserLogLineDB] =
    Try {
      val bufferedSource = Source.fromFile(filename)
      val sessionIterator = for {
        line <- bufferedSource.getLines
        regexMatch <- LogExtractPattern.findAllMatchIn(line)
      } yield Utils.getLogLine(regexMatch)

      val group = sessionIterator
        .collect { case Right(e) => e } // for simplicity reasons we skip the errors
        .toList
        .groupBy(_.userId)

      bufferedSource.close
      group
    }.toEither

  def readFiles(listOfFiles: List[String])(implicit ec: ExecutionContext): Future[ErrorOr[List[UserLogLineDB]]] =
    listOfFiles
      .map(filename => Future(LogFileReader.read(filename)))
      .sequence
      .map {
        results =>
          val (lefts, rights) = results.partitionMap(identity)
          lefts.headOption.toLeft(rights)
      }

}
