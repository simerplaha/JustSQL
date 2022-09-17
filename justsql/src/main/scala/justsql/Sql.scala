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

import justsql.util.CollectionUtil

import java.sql.ResultSet
import scala.collection.{BuildFrom, Factory}
import scala.collection.immutable.ArraySeq
import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try}

sealed trait Sql[+RESULT] { self =>
  protected def runIO(connectionManager: SQLConnectionManager): RESULT

  def runSync()(implicit db: JustSQL): Try[RESULT] =
    db connectAndRun runIO

  def runAsync()(implicit db: JustSQL,
                 ec: ExecutionContext): Future[RESULT] =
    Future.delegate(Future.fromTry(runSync()))

  def map[B](f: RESULT => B): Sql[B] =
    new Sql[B] {
      override protected def runIO(connectionManager: SQLConnectionManager): B =
        f(self.runIO(connectionManager))
    }

  /**
   * Runs each [[Sql]] one after another.
   *
   * Eg: Calling flatMap twice will execute 2 queries in sequence within the same connection.
   * */

  def flatMap[B](f: RESULT => Sql[B]): Sql[B] =
    new Sql[B] {
      override protected def runIO(connectionManager: SQLConnectionManager): B =
        f(self.runIO(connectionManager))
          .runIO(connectionManager)
    }

  def foreach[B](f: RESULT => B): Sql[Unit] =
    new Sql[Unit] {
      override protected def runIO(connectionManager: SQLConnectionManager): Unit =
        f(self.runIO(connectionManager))
    }

  def headOption[A]()(implicit evd: RESULT <:< Iterable[A]): Sql[Option[A]] =
    new Sql[Option[A]] {
      override protected def runIO(connectionManager: SQLConnectionManager): Option[A] =
        self.runIO(connectionManager).headOption
    }

  def head[A]()(implicit evd: RESULT <:< Iterable[A]): Sql[A] =
    new Sql[A] {
      override protected def runIO(connectionManager: SQLConnectionManager): A =
        self.runIO(connectionManager).head
    }

  /**
   * For queries that always expect at most one row.
   *
   * Eg: `SELECT count(*) ...`
   * */
  def exactlyOne[A]()(implicit evd: RESULT <:< Iterable[A]): Sql[A] =
    new Sql[A] {
      override protected def runIO(connectionManager: SQLConnectionManager): A =
        CollectionUtil.exactlyOne(self.runIO(connectionManager))
    }

  def recoverWith[B >: RESULT](pf: PartialFunction[Throwable, Sql[B]]): Sql[B] =
    new Sql[B] {
      override protected def runIO(connectionManager: SQLConnectionManager): B =
        try
          self.runIO(connectionManager)
        catch {
          case throwable: Throwable =>
            pf(throwable).runIO(connectionManager)
        }
    }

  def recover[B >: RESULT](pf: PartialFunction[Throwable, B]): Sql[B] =
    new Sql[B] {
      override protected def runIO(connectionManager: SQLConnectionManager): B =
        try
          self.runIO(connectionManager)
        catch pf
    }

  def zipWith[RESULT2, FINAL](that: Sql[RESULT2])(f: (RESULT, RESULT2) => FINAL): Sql[FINAL] =
    this flatMap {
      thisResult =>
        that map {
          thatResult =>
            f(thisResult, thatResult)
        }
    }

  def zip[RESULT2](that: Sql[RESULT2]): Sql[(RESULT, RESULT2)] =
    this flatMap {
      thisResult =>
        that map {
          thatResult =>
            (thisResult, thatResult)
        }
    }

  private[justsql] def toTracked[B >: RESULT](trackedSQL: String, trackedParams: Params): TrackedSQL[B] =
    new TrackedSQL[B] {
      override def sql: String =
        trackedSQL

      override def params: Params =
        trackedParams

      override def runIO(connectionManager: SQLConnectionManager): B =
        self.runIO(connectionManager)
    }
}

object Sql {
  def sequence[A, C[+X] <: IterableOnce[X], To](queries: C[Sql[A]])(implicit bf: BuildFrom[C[Sql[A]], A, To]): Sql[To] =
    queries.iterator.foldLeft(Sql.success(bf.newBuilder(queries))) {
      (builder, query) =>
        builder.zipWith(query)(_ addOne _)
    }.map(_.result())

  def sequence[R](queries: Sql[R]*): Sql[Seq[R]] =
    sequence(queries)

  def success[V](value: V): Sql[V] =
    new Sql[V] {
      override protected def runIO(connectionManager: SQLConnectionManager): V =
        value
    }

  def failure[R](throwable: Throwable): Sql[R] =
    new Sql[R] {
      override protected def runIO(connectionManager: SQLConnectionManager): R =
        throw throwable
    }

  def fromTry[R](tried: Try[R]): Sql[R] =
    tried match {
      case Failure(exception) => Sql.failure(exception)
      case Success(value)     => Sql.success(value)
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

  private def withPrefix[C[+R] <: Iterable[R]](prefix: String)(implicit factory: Factory[String, C[String]]): TrackedSQL[C[String]] =
    new TrackedSQL[C[String]] {
      override def sql: String =
        prefix + self.sql

      override def params: Params =
        self.params

      override def runIO(connectionManager: SQLConnectionManager): C[String] =
        JustSQL.select[String, C[String]](sql, params)(connectionManager)
    }

  def explain[C[+R] <: Iterable[R]]()(implicit factory: Factory[String, C[String]]): TrackedSQL[C[String]] =
    withPrefix("EXPLAIN\n")

  def explainAnalyze[C[+R] <: Iterable[R]]()(implicit factory: Factory[String, C[String]]): TrackedSQL[C[String]] =
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

case class SelectSQL[+ROW, C[+R] <: Iterable[R]](sql: String,
                                                 params: Params)(implicit rowReader: RowReader[ROW],
                                                                 factory: Factory[ROW, C[ROW]]) extends TrackedSQL[C[ROW]] {

  def headOption(): TrackedSQL[Option[ROW]] =
    super.headOption().toTracked(sql, params)

  def head(): TrackedSQL[ROW] =
    super.head().toTracked(sql, params)

  def exactlyOne(): TrackedSQL[ROW] =
    super.exactlyOne().toTracked(sql, params)

  override def runIO(connectionManager: SQLConnectionManager): C[ROW] =
    JustSQL.select[ROW, C[ROW]](sql, params)(connectionManager)
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
  override def runIO(connectionManager: SQLConnectionManager): Int =
    JustSQL.update(sql, params)(connectionManager)
}
