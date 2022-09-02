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

import java.sql.ResultSet
import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag
import scala.util.Try

sealed trait Sql[+ROW] { self =>
  protected def executeIO(db: JustSQL, context: SqlContext): ROW

  def run()(implicit db: JustSQL): Try[ROW] =
    db.run {
      (connection, manager) =>
        executeIO(db, SqlContext(connection, manager))
    }

  def map[B](f: ROW => B): Sql[B] =
    new Sql[B] {
      override protected def executeIO(db: JustSQL, context: SqlContext): B =
        f(self.executeIO(db, context))
    }

  /**
   * Runs each [[Sql]] one after another.
   *
   * Eg: Calling flatMap twice will execute 2 queries in sequence */
  def flatMap[B](f: ROW => Sql[B]): Sql[B] =
    new Sql[B] {
      override protected def executeIO(db: JustSQL, context: SqlContext): B =
        f(self.executeIO(db, context))
          .executeIO(db, context)
    }

  def recoverWith[B >: ROW](pf: PartialFunction[Throwable, Try[B]]): Sql[B] =
    new Sql[B] {
      override protected def executeIO(db: JustSQL, context: SqlContext): B =
        Try(self.executeIO(db, context)).recoverWith(pf).get
    }

  def recover[B >: ROW](pf: PartialFunction[Throwable, B]): Sql[B] =
    new Sql[B] {
      override protected def executeIO(db: JustSQL, context: SqlContext): B =
        Try(self.executeIO(db, context)).recover(pf).get
    }

  def failed(): Sql[Throwable] =
    new Sql[Throwable] {
      override protected def executeIO(db: JustSQL, context: SqlContext): Throwable =
        Try(self.executeIO(db, context)).failed.get
    }

  def filter(p: ROW => Boolean): Sql[ROW] =
    new Sql[ROW] {
      override protected def executeIO(db: JustSQL, context: SqlContext): ROW =
        Try(self.executeIO(db, context)).filter(p).get
    }

}

sealed trait TrackedSQL[+ROW] extends Sql[ROW] { self =>
  def rawSQL: RawSQL

  private def combine[B >: ROW](operator: String, other: TrackedSQL[B]): TrackedSQL[B] =
    new TrackedSQL[ROW] {
      override def rawSQL: RawSQL = {
        RawSQL(
          sql = s"""${self.rawSQL.sql}\n$operator\n${other.rawSQL.sql}""",
          params = self.rawSQL.sql.params ++ other.rawSQL.sql.params
        )
      }

      override protected def executeIO(db: JustSQL, context: SqlContext): ROW =
        self.executeIO(db, context)
    }

  def union[B >: ROW](other: TrackedSQL[B]): TrackedSQL[B] =
    combine("UNION", other)

  def unionAll[B >: ROW](other: TrackedSQL[B]): TrackedSQL[B] =
    combine("UNION ALL", other)

  def transactional[B >: ROW](): TrackedSQL[B] =
    new TrackedSQL[B] {
      override def rawSQL: RawSQL =
        self.rawSQL.sql.copy(sql = s""""BEGIN;\n${self.rawSQL.sql.sql};\nCOMMIT;"""")

      override protected def executeIO(db: JustSQL, context: SqlContext): B =
        self.executeIO(db, context)
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
                                          classTag: ClassTag[ROW]) extends TrackedSQL[Array[ROW]] { self =>

  def head(): Sql[ROW] =
    new Sql[ROW] {
      override protected def executeIO(db: JustSQL, context: SqlContext): ROW =
        self.executeIO(db, context).head
    }

  def headOption(): Sql[Option[ROW]] =
    new Sql[Option[ROW]] {
      override protected def executeIO(db: JustSQL, context: SqlContext) =
        self.executeIO(db, context).headOption
    }

  def headOrFail(): Sql[ROW] =
    new Sql[ROW] {
      override protected def executeIO(db: JustSQL, context: SqlContext): ROW = {
        val result = self.executeIO(db, context)
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

  override protected def executeIO(db: JustSQL, context: SqlContext): Array[ROW] =
    db.select[ROW](rawSQL)(context)
}

case class UpdateSQL(rawSQL: RawSQL) extends TrackedSQL[Int] { self =>

  override protected def executeIO(db: JustSQL, context: SqlContext): Int =
    db.update(rawSQL)(context)

}
