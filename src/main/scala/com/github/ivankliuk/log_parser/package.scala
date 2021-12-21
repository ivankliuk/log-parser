package com.github.ivankliuk

import com.github.ivankliuk.log_parser.Models.{LogLine, UserId}

package object log_parser {
  type UserLogLineDB = Map[UserId, List[LogLine]]
  type ErrorOr[A] = Either[Throwable, A]
}
