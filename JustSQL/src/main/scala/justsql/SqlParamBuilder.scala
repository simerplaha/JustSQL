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

object SqlParamBuilder {
  val empty: SqlParamBuilder =
    SqlParamBuilder(ListBuffer.empty)
}

/**
 * `?` indicates each parameter is comma seperated ?, ?, ?, ?
 * */
case class SqlParamBuilder(params: ListBuffer[SqlParamWriter[_]],
                           placeholder: String = "?") {

  def ?[P](param: P)(implicit sqlParam: SqlParam[P]): String = {
    params addOne SqlParamWriter(param, sqlParam)
    Array.fill(sqlParam.parametersCount())(placeholder).mkString(", ")
  }

  def ?[P](params: Iterable[P])(implicit sqlParam: SqlParam[P]): String =
    params.map {
      param =>
        this ? param
    }.mkString(", ")

  def apply[P](param: P)(implicit sqlParam: SqlParam[P]): Array[String] = {
    params addOne SqlParamWriter(param, sqlParam)
    Array.fill(sqlParam.parametersCount())(placeholder)
  }

  def apply[P](params: P*)(implicit sqlParam: SqlParam[P]): Iterable[String] =
    apply(params)

  def apply[P](params: Iterable[P])(implicit sqlParam: SqlParam[P]): Iterable[String] =
    params flatMap {
      param =>
        apply(param)(sqlParam)
    }
}
