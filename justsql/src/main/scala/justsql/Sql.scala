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

abstract class Sql[+ROW, C[+R] <: IterableOnce[R]] { self =>
  protected def runIO(connection: Connection, manager: Using.Manager): C[ROW]

  def runSync()(implicit db: JustSQL): Try[C[ROW]] =
    db connectAndRun runIO

  def runAsync()(implicit db: JustSQL,
                 ec: ExecutionContext): Future[C[ROW]] =
    Future.delegate(Future.fromTry(runSync()))

  def map[B](f: ROW => B)(implicit factory: Factory[B, C[B]]): Sql[B, C] =
    (connection: Connection, manager: Using.Manager) =>
      self
        .runIO(connection, manager)
        .iterator
        .foldLeft(factory.newBuilder)(_ addOne f(_))
        .result()

  /**
   * Runs each [[Sql]] one after another.
   *
   * Eg: Calling flatMap twice will execute 2 queries in sequence within the same connection.
   * */

  def flatMap[B](f: ROW => Sql[B, C])(implicit factory: Factory[B, C[B]]): Sql[B, C] =
    (connection: Connection, manager: Using.Manager) =>
      self
        .runIO(connection, manager)
        .iterator
        .foldLeft(factory.newBuilder)(_ addAll f(_).runIO(connection, manager))
        .result()

  def recoverWith[B >: ROW](pf: PartialFunction[Throwable, Sql[B, C]]): Sql[B, C] =
    (connection: Connection, manager: Using.Manager) =>
      try
        self.runIO(connection, manager)
      catch {
        case throwable: Throwable =>
          pf(throwable).runIO(connection, manager)
      }

  def recover[B >: ROW](pf: PartialFunction[Throwable, C[B]]): Sql[B, C] =
    (connection: Connection, manager: Using.Manager) =>
      try
        self.runIO(connection, manager)
      catch pf

  def toTracked[B >: ROW](trackedSQL: String, trackedParams: Params): TrackedSQL[B, C] =
    new TrackedSQL[B, C] {
      override def sql: String =
        trackedSQL

      override def params: Params =
        trackedParams

      override protected def runIO(connection: Connection, manager: Using.Manager): C[B] =
        self.runIO(connection, manager)
    }
}

sealed trait TrackedSQL[+ROW, C[+R] <: IterableOnce[R]] extends Sql[ROW, C] { self =>

  def sql: String

  def params: Params

  override def map[B](f: ROW => B)(implicit factory: Factory[B, C[B]]): TrackedSQL[B, C] =
    super.map(f).toTracked(sql, params)

  override def recover[B >: ROW](pf: PartialFunction[Throwable, C[B]]): TrackedSQL[B, C] =
    super.recover(pf).toTracked(sql, params)

}

object SelectSQL {
  @inline def apply[ROW](f: Params => String)(implicit rowReader: RowReader[ROW],
                                              classTag: ClassTag[ROW]): SelectSQL[ROW, ArraySeq] = {
    val params = Params()
    val sql = f(params)
    SelectSQL[ROW, ArraySeq](sql, params)
  }

  @inline def apply[ROW, C[+R] <: IterableOnce[R]](f: Params => String)(implicit rowReader: RowReader[ROW],
                                                                        classTag: ClassTag[ROW],
                                                                        factory: Factory[ROW, C[ROW]]): SelectSQL[ROW, C] = {
    val params = Params()
    val sql = f(params)
    SelectSQL[ROW, C](sql, params)
  }

  @inline def apply[ROW](sql: String)(implicit rowReader: RowReader[ROW],
                                      classTag: ClassTag[ROW]): SelectSQL[ROW, ArraySeq] =
    SelectSQL[ROW, ArraySeq](sql, Params())

  @inline def apply[ROW, C[+R] <: IterableOnce[R]](sql: String)(implicit rowReader: RowReader[ROW],
                                                                classTag: ClassTag[ROW],
                                                                factory: Factory[ROW, C[ROW]]): SelectSQL[ROW, C] =
    SelectSQL[ROW, C](sql, Params())

  @inline def unsafe[ROW](sql: String)(rowParser: ResultSet => ROW)(implicit classTag: ClassTag[ROW]): SelectSQL[ROW, ArraySeq] = {
    implicit val parser: RowReader[ROW] = rowParser(_)
    SelectSQL(sql, Params())
  }

  @inline def unsafeC[ROW, C[+R] <: IterableOnce[R]](sql: String)(rowParser: ResultSet => ROW)(implicit classTag: ClassTag[ROW],
                                                                                               factory: Factory[ROW, C[ROW]]): SelectSQL[ROW, C] = {
    implicit val parser: RowReader[ROW] = rowParser(_)
    SelectSQL[ROW, C](sql, Params())
  }
}

case class SelectSQL[ROW, C[+R] <: IterableOnce[R]](sql: String, params: Params)(implicit rowReader: RowReader[ROW],
                                                                                 classTag: ClassTag[ROW],
                                                                                 factory: Factory[ROW, C[ROW]]) extends TrackedSQL[ROW, C] { self =>

  def headOption(): TrackedSQL[ROW, Option] =
    new TrackedSQL[ROW, Option] {
      override def sql: String =
        self.sql

      override def params: Params =
        self.params

      override protected def runIO(connection: Connection, manager: Using.Manager): Option[ROW] =
        self.runIO(connection, manager).iterator.nextOption()
    }

  override protected def runIO(connection: Connection, manager: Using.Manager): C[ROW] =
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

case class UpdateSQL(sql: String, params: Params) extends TrackedSQL[Int, Option] { self =>
  override protected def runIO(connection: Connection, manager: Using.Manager): Option[Int] = {
    val result = JustSQL.update(sql, params)(connection, manager)
    if (result == 0)
      None
    else
      Some(result)
  }
}
