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
import scala.reflect.ClassTag
import scala.util.{Try, Using}

sealed trait Sql[+DATA] { self =>
  protected def runIO(connection: Connection, manager: Using.Manager): DATA

  def run()(implicit db: JustSQL): Try[DATA] =
    db connectAndRun {
      (connection, manager) =>
        runIO(connection, manager)
    }

  def map[B](f: DATA => B): Sql[B] =
    new Sql[B] {
      override protected def runIO(connection: Connection, manager: Using.Manager): B =
        f(self.runIO(connection, manager))
    }

  /**
   * Runs each [[Sql]] one after another.
   *
   * Eg: Calling flatMap twice will execute 2 queries in sequence within the same connection.
   * */
  def flatMap[B](f: DATA => Sql[B]): Sql[B] =
    new Sql[B] {
      override protected def runIO(connection: Connection, manager: Using.Manager): B =
        f(self.runIO(connection, manager))
          .runIO(connection, manager)
    }

  def recoverWith[B >: DATA](pf: PartialFunction[Throwable, Sql[B]]): Sql[B] =
    new Sql[B] {
      override protected def runIO(connection: Connection, manager: Using.Manager): B =
        try
          self.runIO(connection, manager)
        catch {
          case throwable: Throwable =>
            pf(throwable).runIO(connection, manager)
        }
    }

  def recover[B >: DATA](pf: PartialFunction[Throwable, B]): Sql[B] =
    new Sql[B] {
      override protected def runIO(connection: Connection, manager: Using.Manager): B =
        try
          self.runIO(connection, manager)
        catch
          pf
    }

  def failed(): Sql[Throwable] =
    new Sql[Throwable] {
      override protected def runIO(connection: Connection, manager: Using.Manager): Throwable =
        try {
          val result = self.runIO(connection, manager)
          new IllegalStateException(s"Expected failure. Actual: ${result.getClass.getSimpleName}")
        } catch {
          case throwable: Throwable =>
            throwable
        }
    }
}

sealed trait TrackedSQL[ROW] extends Sql[ROW] { self =>

  def sql: String

  def params: Params

  override def map[B](f: ROW => B): TrackedSQL[B] =
    new TrackedSQL[B] {
      override def sql: String =
        self.sql

      override def params: Params =
        self.params

      override protected def runIO(connection: Connection, manager: Using.Manager): B =
        f(self.runIO(connection, manager))
    }

  override def recover[B >: ROW](pf: PartialFunction[Throwable, B]): Sql[B] =
    new TrackedSQL[B] {
      override def sql: String =
        self.sql

      override def params: Params =
        self.params

      override protected def runIO(connection: Connection, manager: Using.Manager): B =
        try
          self.runIO(connection, manager)
        catch
          pf
    }

  override def failed(): Sql[Throwable] =
    new TrackedSQL[Throwable] {
      override def sql: String =
        self.sql

      override def params: Params =
        self.params

      override protected def runIO(connection: Connection, manager: Using.Manager): Throwable =
        try {
          val result = self.runIO(connection, manager)
          new IllegalStateException(s"Expected failure. Actual: ${result.getClass.getSimpleName}")
        } catch {
          case throwable: Throwable =>
            throwable
        }
    }
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
                                                       classTag: ClassTag[ROW]) extends TrackedSQL[Array[ROW]] { self =>

  def head(): TrackedSQL[ROW] =
    new TrackedSQL[ROW] {
      override def sql: String =
        self.sql

      override def params: Params =
        self.params

      override protected def runIO(connection: Connection, manager: Using.Manager): ROW =
        self.runIO(connection, manager).head
    }

  def headOption(): TrackedSQL[Option[ROW]] =
    new TrackedSQL[Option[ROW]] {
      override def sql: String =
        self.sql

      override def params: Params =
        self.params

      override protected def runIO(connection: Connection, manager: Using.Manager): Option[ROW] =
        self.runIO(connection, manager).headOption
    }

  def headOrFail(): TrackedSQL[ROW] =
    new TrackedSQL[ROW] {
      override def sql: String =
        self.sql

      override def params: Params =
        self.params

      override protected def runIO(connection: Connection, manager: Using.Manager): ROW = {
        val result = self.runIO(connection, manager)
        if (result.length == 0) {
          result.head
        } else {
          val rowOrRows = if (result.length > 1) "rows" else "row"
          throw new IllegalCallerException(s"Expected 1 row. Actual ${result.length} $rowOrRows.")
        }
      }
    }

  override protected def runIO(connection: Connection, manager: Using.Manager): Array[ROW] =
    JustSQL.select[ROW](sql, params)(connection, manager)

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

case class UpdateSQL(sql: String, params: Params) extends TrackedSQL[Int] { self =>

  override protected def runIO(connection: Connection, manager: Using.Manager): Int =
    JustSQL.update(sql, params)(connection, manager)
}
