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

import scala.util.{Failure, Success, Try}

object TestUtil {

  def withDB[O](connector: SQLConnector)(f: JustSQL => O): O =
    cleanSlate(JustSQL(connector))(f)

  /** Drops existing tables before running the test */
  def cleanSlate[O](db: JustSQL)(f: JustSQL => O): O =
    dropPublicTables()(db) match {
      case Success(_) =>
        try
          f(db)
        finally
          db.close()

      case Failure(exception) =>
        throw exception
    }

  def dropPublicTables()(implicit db: JustSQL): Try[Int] =
    getPublicTables() flatMap dropTables

  def getPublicTables()(implicit db: JustSQL): Try[Array[String]] =
    """
      |select table_name
      |from information_schema.tables
      |where table_schema = 'public';
      |""".stripMargin.select()

  def dropTables(tableNames: Array[String])(implicit db: JustSQL): Try[Int] = {
    val drops =
      tableNames
        .map(name => s"drop table $name;")
        .mkString("\n")

    s"""
       |BEGIN;
       |$drops
       |COMMIT;
       |""".stripMargin.update()
  }
}
