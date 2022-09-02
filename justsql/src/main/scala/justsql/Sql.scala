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

import scala.collection.mutable.ListBuffer
import scala.reflect.ClassTag
import scala.util.{Try, Using}

sealed trait Sql[+ROW] { self =>
  protected def executeIO(db: JustSQL, context: SqlContext): ROW

  def run()(implicit db: JustSQL): Try[ROW] =
    Using.Manager {
      manager =>
        val connection = manager(db.getConnection())
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
}

sealed trait EmbeddableSQL {
  def rawSQL: RawSQL
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

case class RawSQL(sql: String, params: Params) extends EmbeddableSQL {

  override def rawSQL: RawSQL =
    this

  def select[ROW: ClassTag]()(implicit rowReader: RowReader[ROW]): SelectSQL[ROW] =
    SelectSQL(this)

  def update(): UpdateSQL =
    UpdateSQL(this)

}

case class SelectSQL[ROW: ClassTag](rawSQL: RawSQL)(implicit rowReader: RowReader[ROW]) extends Sql[Array[ROW]] with EmbeddableSQL { self =>

  override protected def executeIO(db: JustSQL, context: SqlContext): Array[ROW] =
    db.select[ROW](rawSQL)(context)
}


case class UpdateSQL(rawSQL: RawSQL) extends Sql[Int] with EmbeddableSQL { self =>

  override protected def executeIO(db: JustSQL, context: SqlContext): Int =
    db.update(rawSQL)(context)
}
