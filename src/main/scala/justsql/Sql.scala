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

import justsql.param.SqlWriter

import scala.collection.mutable.ListBuffer

object Sql {

  def apply(f: SqlWriter => String): Sql = {
    val params = SqlWriter(ListBuffer.empty)
    val sql = f(params)
    new Sql(sql, params)
  }

  def apply(sql: String): Sql =
    new Sql(sql, SqlWriter.empty)

}

case class Sql(query: String, params: SqlWriter)
