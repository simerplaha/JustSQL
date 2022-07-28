import java.sql.ResultSet
import scala.reflect.ClassTag

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

import scala.util.Try

package object justsql {

  /** Query Implicits */
  implicit class SQLImplicits(val sql: Sql) extends AnyVal {
    def select[ROW: ClassTag]()(implicit db: JustSQL,
                                rowParser: RowParser[ROW]): Try[Array[ROW]] =
      db.select(sql)

    def selectHead[ROW: ClassTag]()(implicit db: JustSQL,
                                    rowParser: RowParser[ROW]): Try[ROW] =
      db.selectHead(sql)

    def selectMap[ROW, B: ClassTag](f: ROW => B)(implicit db: JustSQL,
                                                 rowParser: RowParser[ROW]): Try[Array[B]] =
      db.selectMap(sql)(f)

    def update()(implicit db: JustSQL): Try[Int] =
      db.update(sql)

    def unsafeSelect[ROW: ClassTag](rowParser: ResultSet => ROW)(implicit db: JustSQL): Try[Array[ROW]] =
      db.unsafeSelect(sql)(rowParser)

    def unsafeSelectHead[ROW: ClassTag](rowParser: ResultSet => ROW)(implicit db: JustSQL): Try[ROW] =
      db.unsafeSelectHead(sql)(rowParser)
  }

  implicit class StringSQLImplicits(val sql: String) extends AnyVal {
    def select[ROW: ClassTag]()(implicit db: JustSQL,
                                rowParser: RowParser[ROW]): Try[Array[ROW]] =
      Sql(sql).select()

    def selectHead[ROW: ClassTag]()(implicit db: JustSQL,
                                    rowParser: RowParser[ROW]): Try[ROW] =
      Sql(sql).selectHead()

    def selectMap[ROW, B: ClassTag](f: ROW => B)(implicit db: JustSQL,
                                                 rowParser: RowParser[ROW]): Try[Array[B]] =
      Sql(sql).selectMap(f)

    def update()(implicit db: JustSQL): Try[Int] =
      Sql(sql).update()

    def unsafeSelect[ROW: ClassTag](rowParser: ResultSet => ROW)(implicit db: JustSQL): Try[Array[ROW]] =
      Sql(sql).unsafeSelect(rowParser)

    def unsafeSelectHead[ROW: ClassTag](rowParser: ResultSet => ROW)(implicit db: JustSQL): Try[ROW] =
      Sql(sql).unsafeSelectHead(rowParser)
  }

}
