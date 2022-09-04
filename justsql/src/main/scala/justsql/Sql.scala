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
import scala.collection.{mutable, Factory}
import scala.collection.immutable.ArraySeq
import scala.reflect.ClassTag
import scala.util.{Try, Using}

abstract class Sql[+A, C[+ROW] <: IterableOnce[ROW]] { self =>
  protected def runIO(connection: Connection, manager: Using.Manager): C[A]

  def run()(implicit db: JustSQL): Try[C[A]] =
    db connectAndRun {
      (connection, manager) =>
        runIO(connection, manager)
    }

  def map[B](f: A => B)(implicit factory: Factory[B, C[B]]): Sql[B, C] =
    (connection: Connection, manager: Using.Manager) => {
      val builder: mutable.Builder[B, C[B]] = factory.newBuilder
      self.runIO(connection, manager).iterator foreach {
        item =>
          builder addOne f(item)
      }
      builder.result()
    }

  /**
   * Runs each [[Sql]] one after another.
   *
   * Eg: Calling flatMap twice will execute 2 queries in sequence within the same connection.
   * */

  def flatMap[B](f: A => Sql[B, C])(implicit factory: Factory[B, C[B]]): Sql[B, C] =
    (connection: Connection, manager: Using.Manager) => {
      val builder: mutable.Builder[B, C[B]] = factory.newBuilder
      self.runIO(connection, manager).iterator foreach {
        item =>
          builder addAll f(item).runIO(connection, manager)
      }
      builder.result()
    }

  def recoverWith[B >: A](pf: PartialFunction[Throwable, Sql[B, C]]): Sql[B, C] =
    (connection: Connection, manager: Using.Manager) =>
      try
        self.runIO(connection, manager)
      catch {
        case throwable: Throwable =>
          pf(throwable).runIO(connection, manager)
      }

  def recover[B >: A](pf: PartialFunction[Throwable, C[B]]): Sql[B, C] =
    (connection: Connection, manager: Using.Manager) =>
      try
        self.runIO(connection, manager)
      catch pf

  def toTracked[B >: A](trackedSQL: String, trackedParams: Params): TrackedSQL[B, C] =
    new TrackedSQL[B, C] {
      override def sql: String =
        trackedSQL

      override def params: Params =
        trackedParams

      override protected def runIO(connection: Connection, manager: Using.Manager): C[B] =
        self.runIO(connection, manager)
    }
}

sealed trait TrackedSQL[+RESULT, C[+ROW] <: IterableOnce[ROW]] extends Sql[RESULT, C] { self =>

  def sql: String

  def params: Params

  override def map[B](f: RESULT => B)(implicit factory: Factory[B, C[B]]): TrackedSQL[B, C] =
    super.map(f).toTracked(sql, params)

  override def recover[B >: RESULT](pf: PartialFunction[Throwable, C[B]]): TrackedSQL[B, C] =
    super.recover(pf).toTracked(sql, params)

}

object SelectSQL {
  @inline def apply[ROW](f: Params => String)(implicit rowReader: RowReader[ROW],
                                              classTag: ClassTag[ROW]): SelectSQL[ROW] = {
    val params = Params()
    val sql = f(params)
    SelectSQL[ROW](sql, params)
  }

  @inline def apply[ROW](sql: String)(implicit rowReader: RowReader[ROW],
                                      classTag: ClassTag[ROW]): SelectSQL[ROW] =
    SelectSQL[ROW](sql, Params())

  @inline def unsafe[ROW](sql: String)(rowParser: ResultSet => ROW)(implicit classTag: ClassTag[ROW]): SelectSQL[ROW] =
    SelectSQL(sql, Params())(rowParser(_), classTag)
}

case class SelectSQL[ROW](sql: String, params: Params)(implicit rowReader: RowReader[ROW],
                                                       classTag: ClassTag[ROW]) extends TrackedSQL[ROW, ArraySeq] { self =>

  def headOption(): TrackedSQL[ROW, Option] =
    new TrackedSQL[ROW, Option] {
      override def sql: String =
        self.sql

      override def params: Params =
        self.params

      override protected def runIO(connection: Connection, manager: Using.Manager): Option[ROW] =
        self.runIO(connection, manager).headOption
    }

  override protected def runIO(connection: Connection, manager: Using.Manager): ArraySeq[ROW] =
    JustSQL.select[ROW, ArraySeq[ROW]](sql, params)(connection, manager)

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

case class UpdateSQL(sql: String, params: Params) extends TrackedSQL[Int, Some] { self =>
  override protected def runIO(connection: Connection, manager: Using.Manager): Some[Int] =
    Some(JustSQL.update(sql, params)(connection, manager))
}
