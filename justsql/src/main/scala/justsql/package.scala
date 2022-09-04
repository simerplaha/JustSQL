import java.sql.ResultSet
import scala.reflect.ClassTag

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

  implicit class StringImplicits(val sql: String) extends AnyVal {
    def update(): UpdateSQL =
      UpdateSQL(sql)

    def select[ROW]()(implicit rowReader: RowReader[ROW],
                      classTag: ClassTag[ROW]): SelectSQL[ROW] =
      SelectSQL(RawSQL(sql, Params()))

    def unsafeSelect[ROW](rowParser: ResultSet => ROW)(implicit classTag: ClassTag[ROW]): SelectSQL[ROW] =
      SelectSQL.unsafe(sql)(rowParser(_))
  }

  implicit class ParamImplicits[P](val param: P) extends AnyVal {
    def ?(implicit sqlParam: ParamWriter[P],
          builder: Params): String =
      builder ? param
  }

  implicit class MultiParamImplicits[P](val param: Iterable[P]) extends AnyVal {
    def ?(implicit sqlParam: ParamWriter[P],
          builder: Params): String =
      builder ? param
  }

  implicit class EmbedSqlActionImplicits[ROW](val sql: TypedRawSQL[ROW]) extends AnyVal {
    def embed(implicit builder: Params): String =
      builder embed sql.sql
  }

}
