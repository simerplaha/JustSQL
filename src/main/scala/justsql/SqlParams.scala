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

object SqlParams {
  val empty: SqlParams =
    SqlParams(ListBuffer.empty)
}

case class SqlParams(params: ListBuffer[ColWriterPair[_]],
                     placeholder: String = "?") {

  def apply[P](col: P)(implicit colWriter: ColWriter[P]): String = {
    params addOne ColWriterPair(col, colWriter)
    placeholder
  }

  def apply[P](col: P*)(implicit colWriter: ColWriter[P]): Seq[String] =
    col map apply[P]

  def apply[P](col: Iterable[P])(implicit colWriter: ColWriter[P]): Iterable[String] =
    col map apply[P]

  def tupled[P](rows: P*)(implicit colWriter: ColWriter[P]): String =
    rows.map {
      param =>
        s"(${apply(param)})"
    }.mkString(", ")

  def tupled[P](rows: Iterable[P])(implicit colWriter: ColWriter[P]): String =
    rows.map {
      param =>
        s"(${apply(param)})"
    }.mkString(", ")

}
