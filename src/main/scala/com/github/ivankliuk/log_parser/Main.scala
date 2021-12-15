package com.github.ivankliuk.log_parser

import cats.data.EitherT
import cats.implicits._
import com.github.ivankliuk.log_parser.Constants.TopUserCount

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object Main extends App {
  val program = for {
    path <- EitherT.fromEither[Future](Utils.getPathFromCmd(args))
    listOfFiles <- EitherT.fromEither[Future](Utils.getListOfFilenames(path))
    db <- EitherT(LogFileReader.readFiles(listOfFiles))
    combinedDb <- db.reduce(_ |+| _).pure[Future].attemptT
    userRepo <- new UserRepositoryInMemory(combinedDb).pure[Future].attemptT
    reportingService <- new ReportingServiceImpl(userRepo).pure[Future].attemptT
    view <- EitherT.fromEither[Future](new ReportingView(reportingService)(TopUserCount))
  } yield view

  val output = Await.result(program.value, 5.seconds)

  output match {
    case Right(value) => System.out.println(value)
    case Left(t) => System.out.println(s"\nERROR: ${t.getMessage}")
  }
}
