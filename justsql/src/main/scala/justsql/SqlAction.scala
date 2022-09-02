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

import scala.util.{Try, Using}

case class SqlAction[ROW](private val executeIO: (JustSQL, SqlContext) => ROW) { self =>

  def run()(implicit db: JustSQL): Try[ROW] =
    Using.Manager {
      manager =>
        val connection = manager(db.getConnection())
        executeIO(db, SqlContext(connection, manager))
    }

  def map[B](f: ROW => B): SqlAction[B] =
    SqlAction(
      (db, context) =>
        f(self.executeIO(db, context))
    )

  /**
   * Runs each [[SqlAction]] one after another.
   *
   * Eg: Calling flatMap twice will execute 2 queries in sequence */
  def flatMap[B](f: ROW => SqlAction[B]): SqlAction[B] =
    SqlAction(
      (db, context) =>
        f(self.executeIO(db, context))
          .executeIO(db, context)
    )

  //  def recoverWith[B >: ROW](pf: PartialFunction[Throwable, Try[B]]): SqlAction[B] =
  //    copy(
  //      executeIO =
  //        (db, context) =>
  //          Try(self.executeIO(db, context)).recoverWith(pf).get
  //    )
  //
  //  def recover[B >: ROW](pf: PartialFunction[Throwable, B]): SqlAction[B] =
  //    copy(
  //      executeIO =
  //        (db, context) =>
  //          Try(self.executeIO(db, context)).recover(pf).get
  //    )


}
