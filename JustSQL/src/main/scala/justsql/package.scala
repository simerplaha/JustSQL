

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

package object justsql {

  @inline implicit def convertStringToSQL(sql: String): Sql =
    Sql(sql)

  implicit class ParamImplicits[P](val param: P) extends AnyVal {
    def ?(implicit colWriter: SqlParam[P],
          builder: SqlParamBuilder): String =
      builder ? param
  }

  implicit class MultiParamImplicits[P](val param: Iterable[P]) extends AnyVal {
    def ?(implicit colWriter: SqlParam[P],
          builder: SqlParamBuilder): String =
      builder ? param
  }

}
