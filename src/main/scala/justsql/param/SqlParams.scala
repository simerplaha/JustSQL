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

import scala.collection.mutable.ListBuffer

object SqlParams {
  val empty: SqlParams =
    SqlParams(ListBuffer.empty)
}

case class SqlParams(params: ListBuffer[SqlParamSetter[_]],
                     placeholder: String = "?") {

  def apply[P](param: P)(implicit setter: SqlParam[P]): String = {
    params addOne SqlParamSetter(param, setter)
    placeholder
  }

  def apply[P](params: P*)(implicit setter: SqlParam[P]): Seq[String] =
    params map apply[P]

  def apply[P](params: Iterable[P])(implicit setter: SqlParam[P]): Iterable[String] =
    params map apply[P]

  def rows[P](params: P*)(implicit setter: SqlParam[P]): String =
    params.map {
      param =>
        s"(${apply(param)})"
    }.mkString(", ")

  def rows[P](params: Iterable[P])(implicit setter: SqlParam[P]): String =
    params.map {
      param =>
        s"(${apply(param)})"
    }.mkString(", ")

}
