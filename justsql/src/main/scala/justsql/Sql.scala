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

sealed trait Sql[+ROW] { self =>
  protected def runIO(connection: Connection, manager: Using.Manager): ROW

  def run()(implicit db: JustSQL): Try[ROW] =
    db connectAndRun {
      (connection, manager) =>
        runIO(connection, manager)
    }

  def map[B](f: ROW => B): Sql[B] =
    new Sql[B] {
      override protected def runIO(connection: Connection, manager: Using.Manager): B =
        f(self.runIO(connection, manager))
    }

  /**
   * Runs each [[Sql]] one after another.
   *
   * Eg: Calling flatMap twice will execute 2 queries in sequence within the same connection.
   * */
  def flatMap[B](f: ROW => Sql[B]): Sql[B] =
    new Sql[B] {
      override protected def runIO(connection: Connection, manager: Using.Manager): B =
        f(self.runIO(connection, manager))
          .runIO(connection, manager)
    }

  def recoverWith[B >: ROW](pf: PartialFunction[Throwable, Sql[B]]): Sql[B] =
    new Sql[B] {
      override protected def runIO(connection: Connection, manager: Using.Manager): B =
        try
          self.runIO(connection, manager)
        catch {
          case throwable: Throwable =>
            pf(throwable).runIO(connection, manager)
        }
    }

  def recover[B >: ROW](pf: PartialFunction[Throwable, B]): Sql[B] =
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

object Sql {

  @inline def apply(f: Params => String): RawSQL = {
    val params = Params()
    val sql = f(params)
    RawSQL(sql, params)
  }

  @inline def apply(sql: String): RawSQL =
    RawSQL(sql, Params())

}

sealed trait TypedRawSQL[ROW] extends Sql[ROW] { self =>

  type Self = TypedRawSQL[ROW]

  def rawSQL: RawSQL

  def copyRawSQL(rawSQL: RawSQL): Self

  def wrap(start: String, end: String): Self =
    copyRawSQL(self.rawSQL.copy(sql = s"""$start${self.rawSQL.sql}$end"""))

  def wrapBeginCommit(): Self =
    wrap("\nBEGIN;\n", "\nCOMMIT;\n")

  def combine[B >: ROW](separator: String, other: TypedRawSQL[B]): Self =
    copyRawSQL(
      RawSQL(
        sql = s"""${self.rawSQL.sql}$separator${other.rawSQL.sql}""",
        params = self.rawSQL.sql.params ++ other.rawSQL.sql.params
      )
    )

  def combineUnion[B >: ROW](other: TypedRawSQL[B]): Self =
    combine("\nUNION\n", other)

  def combineUnionAll[B >: ROW](other: TypedRawSQL[B]): Self =
    combine("\nUNION ALL\n", other)
}

case class RawSQL(sql: String, params: Params) {

  def update(): UpdateSQL =
    UpdateSQL(this)

  def select[ROW]()(implicit rowReader: RowReader[ROW],
                    classTag: ClassTag[ROW]): SelectSQL[ROW] =
    SelectSQL(this)

  def unsafeSelect[ROW](rowParser: ResultSet => ROW)(implicit classTag: ClassTag[ROW]): SelectSQL[ROW] =
    SelectSQL(this)(rowParser(_), classTag)

}

case class SelectSQL[ROW](rawSQL: RawSQL)(implicit rowReader: RowReader[ROW],
                                          classTag: ClassTag[ROW]) extends TypedRawSQL[Array[ROW]] { self =>

  override def copyRawSQL(rawSQL: RawSQL): SelectSQL[ROW] =
    self.copy(rawSQL = rawSQL)

  def map[B: ClassTag](f: ROW => B): SelectSQL[B] = {
    implicit val rowReader: RowReader[B] =
      (resultSet: ResultSet) =>
        f(self.rowReader(resultSet))

    SelectSQL[B](rawSQL)
  }

  def head(): Sql[ROW] =
    new Sql[ROW] {
      override protected def runIO(connection: Connection, manager: Using.Manager): ROW =
        self.runIO(connection, manager).head
    }

  def headOption(): Sql[Option[ROW]] =
    new Sql[Option[ROW]] {
      override protected def runIO(connection: Connection, manager: Using.Manager) =
        self.runIO(connection, manager).headOption
    }

  def headOrFail(): Sql[ROW] =
    new Sql[ROW] {
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
    JustSQL.select[ROW](rawSQL)(connection, manager)

}

case class UpdateSQL(rawSQL: RawSQL) extends TypedRawSQL[Int] { self =>

  override protected def runIO(connection: Connection, manager: Using.Manager): Int =
    JustSQL.update(rawSQL)(connection, manager)

  override def copyRawSQL(rawSQL: RawSQL): UpdateSQL =
    self.copy(rawSQL = rawSQL)
}
