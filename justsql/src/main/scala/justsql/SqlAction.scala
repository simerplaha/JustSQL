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

import scala.util.Try

case class SqlAction[ROW](sql: Sql,
                          private val runner: (JustSQL, Sql) => Try[ROW]) { self =>

  def run()(implicit db: JustSQL): Try[ROW] =
    runner(db, sql)

  def map[B](f: ROW => B): SqlAction[B] =
    copy(
      runner =
        (db, sql) =>
          self.runner(db, sql).map(f)
    )

  /**
   * Runs each [[SqlAction]] one after another.
   *
   * Eg: Calling flatMap twice will execute 2 queries in sequence */
  def flatMap[B](f: ROW => SqlAction[B]): SqlAction[B] =
    copy(
      runner =
        (db, sql) =>
          self.runner(db, sql) flatMap {
            row =>
              f(row).run()(db)
          }
    )

  private def combine(operator: String, other: SqlAction[ROW]): SqlAction[ROW] = {
    val combinedSql =
      Sql(
        sql = self.sql + s";\n$operator\n" + other.sql,
        params = self.sql.params ++ other.sql.params
      )

    copy(sql = combinedSql)
  }

  def union(other: SqlAction[ROW]): SqlAction[ROW] =
    combine("UNION", other)

  def unionAll(other: SqlAction[ROW]): SqlAction[ROW] =
    combine("UNION ALL", other)

  def transactional()(implicit evd: ROW =:= Int): SqlAction[ROW] =
    copy(sql = sql.copy(sql = "BEGIN;\n" + sql.sql + ";\nCOMMIT;"))

  def recoverWith[B >: ROW](pf: PartialFunction[Throwable, Try[B]]): SqlAction[B] =
    copy(
      runner =
        (db, sql) =>
          self.runner(db, sql).recoverWith(pf)
    )

  def recover[B >: ROW](pf: PartialFunction[Throwable, B]): SqlAction[B] =
    copy(
      runner =
        (db, sql) =>
          self.runner(db, sql).recover(pf)
    )

  def failed(): SqlAction[Throwable] =
    copy(
      runner =
        (db, sql) =>
          self.runner(db, sql).failed
    )

  def filter(p: ROW => Boolean): SqlAction[ROW] =
    copy(
      runner =
        (db, sql) =>
          self.runner(db, sql).filter(p)
    )

}
