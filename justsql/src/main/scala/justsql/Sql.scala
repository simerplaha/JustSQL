/*
 * Copyright 2022 Simer JS Plaha (simer.j@gmail.com - @simerplaha)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package justsql

import java.sql.{Connection, ResultSet}
import scala.collection.Factory
import scala.collection.immutable.ArraySeq
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag
import scala.util.{Try, Using}

abstract class Sql[+RESULT] { self =>
  def runIO(connection: Connection,
            manager: Using.Manager): RESULT

  def runSync()(implicit db: JustSQL): Try[RESULT] =
    db connectAndRun runIO

  def runAsync()(implicit db: JustSQL,
                 ec: ExecutionContext): Future[RESULT] =
    Future.delegate(Future.fromTry(runSync()))

  def map[B](f: RESULT => B): Sql[B] =
    (connection: Connection, manager: Using.Manager) =>
      f(self.runIO(connection, manager))

  /**
   * Runs each [[Sql]] one after another.
   *
   * Eg: Calling flatMap twice will execute 2 queries in sequence within the same connection.
   * */

  def flatMap[B](f: RESULT => Sql[B]): Sql[B] =
    (connection: Connection, manager: Using.Manager) =>
      f(self.runIO(connection, manager))
        .runIO(connection, manager)

  def foreach[B](f: RESULT => B): Sql[Unit] =
    (connection: Connection, manager: Using.Manager) =>
      f(self.runIO(connection, manager))

  def recoverWith[B >: RESULT](pf: PartialFunction[Throwable, Sql[B]]): Sql[B] =
    (connection: Connection, manager: Using.Manager) =>
      try
        self.runIO(connection, manager)
      catch {
        case throwable: Throwable =>
          pf(throwable).runIO(connection, manager)
      }

  def recover[B >: RESULT](pf: PartialFunction[Throwable, B]): Sql[B] =
    (connection: Connection, manager: Using.Manager) =>
      try
        self.runIO(connection, manager)
      catch pf

  private[justsql] def toTracked[B >: RESULT](trackedSQL: String, trackedParams: Params): TrackedSQL[B] =
    new TrackedSQL[B] {
      override def sql: String =
        trackedSQL

      override def params: Params =
        trackedParams

      override def runIO(connection: Connection, manager: Using.Manager): B =
        self.runIO(connection, manager)
    }
}

sealed trait TrackedSQL[+RESULT] extends Sql[RESULT] { self =>

  def sql: String

  def params: Params

  override def map[B](f: RESULT => B): TrackedSQL[B] =
    super.map(f).toTracked(sql, params)

  override def foreach[B](f: RESULT => B): TrackedSQL[Unit] =
    super.foreach(f).toTracked(sql, params)

  override def recover[B >: RESULT](pf: PartialFunction[Throwable, B]): TrackedSQL[B] =
    super.recover(pf).toTracked(sql, params)

  private def withPrefix[C[+R] <: Iterable[R]](prefix: String)(implicit factory: Factory[String, C[String]]): Sql[C[String]] =
    new TrackedSQL[C[String]] {
      override def sql: String =
        prefix + self.sql

      override def params: Params =
        self.params

      override def runIO(connection: Connection, manager: Using.Manager): C[String] =
        JustSQL.select[String, C[String]](sql, params)(connection, manager)
    }

  def explain[C2[+R] <: Iterable[R]]()(implicit factory: Factory[String, C2[String]]): Sql[C2[String]] =
    withPrefix("EXPLAIN\n")

  def explainAnalyze[C2[+R] <: Iterable[R]]()(implicit factory: Factory[String, C2[String]]): Sql[C2[String]] =
    withPrefix("EXPLAIN ANALYZE\n")
}

object SelectSQL {

  @inline def apply[ROW](f: Params => String)(implicit rowReader: RowReader[ROW],
                                              classTag: ClassTag[ROW]): SelectSQL[ROW, ArraySeq] = {
    val params = Params()
    val sql = f(params)
    SelectSQL[ROW, ArraySeq](sql, params)
  }

  @inline def apply[ROW, C[+R] <: Iterable[R]](f: Params => String)(implicit rowReader: RowReader[ROW],
                                                                    factory: Factory[ROW, C[ROW]]): SelectSQL[ROW, C] = {
    val params = Params()
    val sql = f(params)
    SelectSQL[ROW, C](sql, params)
  }

  @inline def apply[ROW](sql: String)(implicit rowReader: RowReader[ROW],
                                      classTag: ClassTag[ROW]): SelectSQL[ROW, ArraySeq] =
    SelectSQL[ROW, ArraySeq](sql, Params())

  @inline def apply[ROW, C[+R] <: Iterable[R]](sql: String)(implicit rowReader: RowReader[ROW],
                                                            factory: Factory[ROW, C[ROW]]): SelectSQL[ROW, C] =
    SelectSQL[ROW, C](sql, Params())

  @inline def unsafe[ROW](sql: String)(rowParser: ResultSet => ROW)(implicit classTag: ClassTag[ROW]): SelectSQL[ROW, ArraySeq] = {
    implicit val parser: RowReader[ROW] = rowParser(_)
    SelectSQL(sql, Params())
  }

  @inline def unsafeC[ROW, C[+R] <: Iterable[R]](sql: String)(rowParser: ResultSet => ROW)(implicit factory: Factory[ROW, C[ROW]]): SelectSQL[ROW, C] = {
    implicit val parser: RowReader[ROW] = rowParser(_)
    SelectSQL[ROW, C](sql, Params())
  }
}

case class SelectSQL[ROW, C[R] <: Iterable[R]](sql: String,
                                               params: Params)(implicit rowReader: RowReader[ROW],
                                                               factory: Factory[ROW, C[ROW]]) extends TrackedSQL[C[ROW]] { select =>

  def headOption(): TrackedSQL[Option[ROW]] =
    new TrackedSQL[Option[ROW]] {
      override def sql: String =
        select.sql

      override def params: Params =
        select.params

      override def runIO(connection: Connection, manager: Using.Manager): Option[ROW] =
        select
          .runIO(connection, manager)
          .iterator
          .nextOption()
    }

  /**
   * For queries that always expect at most one row.
   *
   * Eg: `SELECT count(*) ...`
   * */
  def exactlyOne(): TrackedSQL[ROW] =
    new TrackedSQL[ROW] {
      override def sql: String =
        select.sql

      override def params: Params =
        select.params

      override def runIO(connection: Connection, manager: Using.Manager): ROW = {
        val result = select.runIO(connection, manager)
        val resultSize = result.size
        if (resultSize != 0)
          throw new Exception(s"Expect 1 row. Actual $resultSize")
        else
          result.head
      }
    }

  override def runIO(connection: Connection, manager: Using.Manager): C[ROW] =
    JustSQL.select[ROW, C[ROW]](sql, params)(connection, manager)
}

object UpdateSQL {

  @inline def apply(f: Params => String): UpdateSQL = {
    val params = Params()
    val sql = f(params)
    UpdateSQL(sql, params)
  }

  @inline def apply(sql: String): UpdateSQL =
    UpdateSQL(sql, Params())

}

case class UpdateSQL(sql: String, params: Params) extends TrackedSQL[Int] {
  override def runIO(connection: Connection, manager: Using.Manager): Int =
    JustSQL.update(sql, params)(connection, manager)
}
