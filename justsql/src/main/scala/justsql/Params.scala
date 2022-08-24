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

import justsql.Params._

import scala.collection.mutable.ListBuffer

object Params {
  final val placeholder: String =
    "?"

  final val empty: Params =
    Params(ListBuffer.empty)
}

/**
 * `?` indicates each parameter is comma seperated ?, ?, ?, ?
 * */
case class Params(params: ListBuffer[ParamValueWriter[_]]) extends AnyVal {

  @inline def ?[P](param: P)(implicit sqlParam: ParamWriter[P]): String =
    apply(param).mkString(", ")

  def ?[P](params: Iterable[P])(implicit sqlParam: ParamWriter[P]): String =
    params.map {
      param =>
        this ? param
    }.mkString(", ")

  def apply[P](param: P)(implicit sqlParam: ParamWriter[P]): Array[String] = {
    params addOne ParamValueWriter(param, sqlParam)
    Array.fill(sqlParam.paramCount())(placeholder)
  }

  @inline def apply[P](params: P*)(implicit sqlParam: ParamWriter[P]): Iterable[String] =
    apply(params)

  def apply[P](params: Iterable[P])(implicit sqlParam: ParamWriter[P]): Iterable[String] =
    params flatMap {
      param =>
        apply(param)(sqlParam)
    }
}
