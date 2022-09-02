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
import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag
import scala.util.{Try, Using}

sealed trait Sql[+ROW] { self =>
  protected def runIO(db: JustSQL, connection: Connection, manager: Using.Manager): ROW

  def run()(implicit db: JustSQL): Try[ROW] =
    db connectAndRun {
      (connection, manager) =>
        runIO(db, connection, manager)
    }

  def map[B](f: ROW => B): Sql[B] =
    new Sql[B] {
      override protected def runIO(db: JustSQL, connection: Connection, manager: Using.Manager): B =
        f(self.runIO(db, connection, manager))
    }

  /**
   * Runs each [[Sql]] one after another.
   *
   * Eg: Calling flatMap twice will execute 2 queries in sequence */
  def flatMap[B](f: ROW => Sql[B]): Sql[B] =
    new Sql[B] {
      override protected def runIO(db: JustSQL, connection: Connection, manager: Using.Manager): B =
        f(self.runIO(db, connection, manager))
          .runIO(db, connection, manager)
    }

  def recoverWith[B >: ROW](pf: PartialFunction[Throwable, Sql[B]]): Sql[B] =
    new Sql[B] {
      override protected def runIO(db: JustSQL, connection: Connection, manager: Using.Manager): B =
        try
          self.runIO(db, connection, manager)
        catch {
          case throwable: Throwable =>
            pf(throwable).runIO(db, connection, manager)
        }
    }

  def recover[B >: ROW](pf: PartialFunction[Throwable, B]): Sql[B] =
    new Sql[B] {
      override protected def runIO(db: JustSQL, connection: Connection, manager: Using.Manager): B =
        try
          self.runIO(db, connection, manager)
        catch {
          case throwable: Throwable =>
            pf(throwable)
        }
    }

  def failed(): Sql[Throwable] =
    new Sql[Throwable] {
      override protected def runIO(db: JustSQL, connection: Connection, manager: Using.Manager): Throwable =
        Try(self.runIO(db, connection, manager)).failed.get
    }

  def filter(p: ROW => Boolean): Sql[ROW] =
    new Sql[ROW] {
      override protected def runIO(db: JustSQL, connection: Connection, manager: Using.Manager): ROW =
        Try(self.runIO(db, connection, manager)).filter(p).get
    }

}

sealed trait SimpleSQL[+ROW] extends Sql[ROW] { self =>
  def rawSQL: RawSQL

  private def combine[B >: ROW](operator: String, other: SimpleSQL[B]): SimpleSQL[B] =
    new SimpleSQL[ROW] {
      override def rawSQL: RawSQL = {
        RawSQL(
          sql = s"""${self.rawSQL.sql}\n$operator\n${other.rawSQL.sql}""",
          params = self.rawSQL.sql.params ++ other.rawSQL.sql.params
        )
      }

      override protected def runIO(db: JustSQL, connection: Connection, manager: Using.Manager): ROW =
        self.runIO(db, connection, manager)
    }

  def union[B >: ROW](other: SimpleSQL[B]): SimpleSQL[B] =
    combine("UNION", other)

  def unionAll[B >: ROW](other: SimpleSQL[B]): SimpleSQL[B] =
    combine("UNION ALL", other)

  def transactional[B >: ROW](): SimpleSQL[B] =
    new SimpleSQL[B] {
      override def rawSQL: RawSQL =
        self.rawSQL.copy(sql = s""""BEGIN;\n${self.rawSQL.sql.sql};\nCOMMIT;"""")

      override protected def runIO(db: JustSQL, connection: Connection, manager: Using.Manager): B =
        self.runIO(db, connection, manager)
    }
}

object Sql {

  def apply(f: Params => String): RawSQL = {
    val params = Params(ListBuffer.empty)
    val sql = f(params)
    RawSQL(sql, params)
  }

  @inline def apply(sql: String): RawSQL =
    RawSQL(sql, Params.empty)

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
                                          classTag: ClassTag[ROW]) extends SimpleSQL[Array[ROW]] { self =>

  def head(): Sql[ROW] =
    new Sql[ROW] {
      override protected def runIO(db: JustSQL, connection: Connection, manager: Using.Manager): ROW =
        self.runIO(db, connection, manager).head
    }

  def headOption(): Sql[Option[ROW]] =
    new Sql[Option[ROW]] {
      override protected def runIO(db: JustSQL, connection: Connection, manager: Using.Manager) =
        self.runIO(db, connection, manager).headOption
    }

  def headOrFail(): Sql[ROW] =
    new Sql[ROW] {
      override protected def runIO(db: JustSQL, connection: Connection, manager: Using.Manager): ROW = {
        val result = self.runIO(db, connection, manager)
        if (result.length == 0) {
          result.head
        } else {
          val rowOrRows = if (result.length > 1) "rows" else "row"
          throw new IllegalCallerException(s"Expected 1 row. Actual ${result.length} $rowOrRows.")
        }
      }
    }

  def map[B: ClassTag](f: ROW => B): SelectSQL[B] = {
    implicit val rowReader: RowReader[B] =
      (resultSet: ResultSet) =>
        f(self.rowReader(resultSet))

    SelectSQL[B](rawSQL)
  }

  override protected def runIO(db: JustSQL, connection: Connection, manager: Using.Manager): Array[ROW] =
    db.select[ROW](rawSQL)(connection, manager)
}

case class UpdateSQL(rawSQL: RawSQL) extends SimpleSQL[Int] { self =>

  override protected def runIO(db: JustSQL, connection: Connection, manager: Using.Manager): Int =
    db.update(rawSQL)(connection, manager)

}
