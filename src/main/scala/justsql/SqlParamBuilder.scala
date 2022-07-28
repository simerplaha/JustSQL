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
 * Single `?` indicates each parameter is comma seperated ?, ?, ?, ?.
 *
 * Double `??` indicates each parameter is comma seperated and within closed parentheses (?, ?), (?, ?).
 * */
case class SqlParamBuilder(params: ListBuffer[SqlParamWriter[_]],
                           placeholder: String = "?") {

  def ?[P](col: P)(implicit colWriter: SqlParam[P]): String = {
    params addOne SqlParamWriter(col, colWriter)
    Array.fill(colWriter.parametersCount())(placeholder).mkString(", ")
  }

  def ?[P](col: P*)(implicit colWriter: SqlParam[P]): String =
    col.map {
      param =>
        ?(param)(colWriter)
    }.mkString(", ")

  def ?[P](col: Iterable[P])(implicit colWriter: SqlParam[P]): String =
    col.map {
      param =>
        ?(param)(colWriter)
    }.mkString(", ")

  def ??[P](col: P)(implicit colWriter: SqlParam[P]): String = {
    params addOne SqlParamWriter(col, colWriter)
    Array.fill(colWriter.parametersCount())(placeholder).mkString("(", ", ", ")")
  }

  def ??[P](col: P*)(implicit colWriter: SqlParam[P]): String =
    col.map {
      param =>
        ??(param)(colWriter)
    }.mkString(", ")

  def ??[P](col: Iterable[P])(implicit colWriter: SqlParam[P]): String =
    col.map {
      param =>
        ??(param)(colWriter)
    }.mkString(", ")

  def apply[P](col: P)(implicit colWriter: SqlParam[P]): Array[String] = {
    params addOne SqlParamWriter(col, colWriter)
    Array.fill(colWriter.parametersCount())(placeholder)
  }

  def apply[P](col: P*)(implicit colWriter: SqlParam[P]): Seq[String] =
    col flatMap {
      param =>
        apply(param)(colWriter)
    }

  def apply[P](col: Iterable[P])(implicit colWriter: SqlParam[P]): Iterable[String] =
    col flatMap {
      param =>
        apply(param)(colWriter)
    }
}
