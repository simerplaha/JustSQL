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

object ParamBuilder {
  val empty: ParamBuilder =
    ParamBuilder(ListBuffer.empty)
}

/**
 * `?` indicates each parameter is comma seperated ?, ?, ?, ?
 * */
case class ParamBuilder(params: ListBuffer[ParamValueSetter[_]],
                        placeholder: String = "?") {

  @inline def ?[P](param: P)(implicit sqlParam: ParamSetter[P]): String =
    apply(param).mkString(", ")

  def ?[P](params: Iterable[P])(implicit sqlParam: ParamSetter[P]): String =
    params.map {
      param =>
        this ? param
    }.mkString(", ")

  def apply[P](param: P)(implicit sqlParam: ParamSetter[P]): Array[String] = {
    params addOne ParamValueSetter(param, sqlParam)
    Array.fill(sqlParam.paramCount())(placeholder)
  }

  @inline def apply[P](params: P*)(implicit sqlParam: ParamSetter[P]): Iterable[String] =
    apply(params)

  def apply[P](params: Iterable[P])(implicit sqlParam: ParamSetter[P]): Iterable[String] =
    params flatMap {
      param =>
        apply(param)(sqlParam)
    }
}
