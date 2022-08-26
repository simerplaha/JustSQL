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

object Sql {

  def apply(f: Params => String): Sql = {
    val params = Params(ListBuffer.empty)
    val sql = f(params)
    new Sql(sql, params)
  }

  @inline def apply(sql: String): Sql =
    new Sql(sql, Params.empty)

}

case class Sql(sql: String, params: Params) {

  def select[ROW: ClassTag]()(implicit db: JustSQL,
                              rowReader: RowReader[ROW]): Try[Array[ROW]] =
    db.select(this)

  def selectOne[ROW: ClassTag]()(implicit db: JustSQL,
                                 rowReader: RowReader[ROW]): Try[Option[ROW]] =
    db.selectOne(this)

  def update()(implicit db: JustSQL): Try[Int] =
    db.update(this)

  def unsafeSelect[ROW: ClassTag](rowReader: ResultSet => ROW)(implicit db: JustSQL): Try[Array[ROW]] =
    db.unsafeSelect(this)(rowReader)

  def unsafeSelectOne[ROW: ClassTag](rowReader: ResultSet => ROW)(implicit db: JustSQL): Try[Option[ROW]] =
    db.unsafeSelectOne(this)(rowReader)

}