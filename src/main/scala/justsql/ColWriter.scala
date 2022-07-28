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

import java.sql.PreparedStatement

trait ColWriter[P] extends ((PreparedStatement, Int, P) => Unit) {

  override def apply(statement: PreparedStatement, index: Int, param: P): Unit

}

object ColWriter {

  implicit object IntColWriter extends ColWriter[Int] {
    override def apply(statement: PreparedStatement, index: Int, param: Int): Unit =
      statement.setInt(index, param)
  }

  implicit object StringColWriter extends ColWriter[String] {
    override def apply(statement: PreparedStatement, index: Int, param: String): Unit =
      statement.setString(index, param)
  }

}
