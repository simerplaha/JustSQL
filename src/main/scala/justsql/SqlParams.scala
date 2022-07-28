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

/**
 * Single `?` indicates each parameter is comma seperated ?, ?, ?, ?.
 *
 * Double `??` indicates each parameter is comma seperated and within closed parentheses (?, ?), (?, ?).
 * */
case class SqlParams(params: ListBuffer[SqlParamWriterPair[_]],
                     placeholder: String = "?") {

  def ?[P](col: P)(implicit colWriter: SqlParamWriter[P]): String = {
    params addOne SqlParamWriterPair(col, colWriter)
    Array.fill(colWriter.parametersCount())(placeholder).mkString(", ")
  }

  def ?[P](col: P*)(implicit colWriter: SqlParamWriter[P]): String =
    col.map {
      param =>
        ?(param)(colWriter)
    }.mkString(", ")

  def ?[P](col: Iterable[P])(implicit colWriter: SqlParamWriter[P]): String =
    col.map {
      param =>
        ?(param)(colWriter)
    }.mkString(", ")

  def ??[P](col: P)(implicit colWriter: SqlParamWriter[P]): String = {
    params addOne SqlParamWriterPair(col, colWriter)
    Array.fill(colWriter.parametersCount())(placeholder).mkString("(", ", ", ")")
  }

  def ??[P](col: P*)(implicit colWriter: SqlParamWriter[P]): String =
    col.map {
      param =>
        ??(param)(colWriter)
    }.mkString(", ")

  def ??[P](col: Iterable[P])(implicit colWriter: SqlParamWriter[P]): String =
    col.map {
      param =>
        ??(param)(colWriter)
    }.mkString(", ")

  def apply[P](col: P)(implicit colWriter: SqlParamWriter[P]): Array[String] = {
    params addOne SqlParamWriterPair(col, colWriter)
    Array.fill(colWriter.parametersCount())(placeholder)
  }

  def apply[P](col: P*)(implicit colWriter: SqlParamWriter[P]): Seq[String] =
    col flatMap {
      param =>
        apply(param)(colWriter)
    }

  def apply[P](col: Iterable[P])(implicit colWriter: SqlParamWriter[P]): Iterable[String] =
    col flatMap {
      param =>
        apply(param)(colWriter)
    }
}
