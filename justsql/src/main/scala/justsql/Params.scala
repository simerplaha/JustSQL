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
}

case class Params(private val paramsMut: ListBuffer[ParamValueWriter[_]] = ListBuffer.empty) extends AnyVal { self =>

  /** Immutable params */
  def parameters(): Array[ParamValueWriter[_]] =
    paramsMut.toArray

  @inline def ?[P](param: P)(implicit writer: ParamWriter[P]): String =
    apply(param).mkString(", ")

  def ?[P](params: Iterable[P])(implicit writer: ParamWriter[P]): String =
    params.map {
      param =>
        self ? param
    }.mkString(", ")

  def apply[P](param: P)(implicit writer: ParamWriter[P]): Array[String] = {
    paramsMut addOne ParamValueWriter(param, writer)
    Array.fill(writer.paramCount())(placeholder)
  }

  @inline def apply[P](params: P*)(implicit writer: ParamWriter[P]): Iterable[String] =
    apply(params)

  def apply[P](params: Iterable[P])(implicit writer: ParamWriter[P]): Iterable[String] =
    params flatMap {
      param =>
        apply(param)(writer)
    }

  @inline def embed[A](typedSQL: TrackedSQL[A]): String = {
    paramsMut addAll typedSQL.params.paramsMut
    typedSQL.sql
  }

  def foreach[T](f: ParamValueWriter[_] => T): Unit =
    paramsMut foreach f

}
