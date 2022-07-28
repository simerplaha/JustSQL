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

package justsql.param

import java.sql.PreparedStatement

trait SqlParam[P] {

  def set(statement: PreparedStatement, index: Int, param: P): Unit

}

object SqlParam {

  implicit object IntSqlParam extends SqlParam[Int] {
    override def set(statement: PreparedStatement, index: Int, param: Int): Unit =
      statement.setInt(index, param)
  }

  implicit object StringSqlParam extends SqlParam[String] {
    override def set(statement: PreparedStatement, index: Int, param: String): Unit =
      statement.setString(index, param)
  }

}
