import java.sql.ResultSet
import scala.collection.{mutable, Factory}
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
      SelectSQL(sql, Params())

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

  implicit class EmbedSqlActionImplicits[+ROW, C[+A] <: Iterable[A]](val sql: TrackedSQL[ROW, C]) extends AnyVal {
    def embed(implicit builder: Params): String =
      builder embed sql
  }

  implicit def optionFactory[T]: Factory[T, Option[T]] =
    new Factory[T, Option[T]] {
      override def fromSpecific(it: IterableOnce[T]): Option[T] =
        it.iterator.nextOption()

      override def newBuilder: mutable.Builder[T, Option[T]] =
        new mutable.Builder[T, Option[T]] {
          var item: T = _
          override def clear(): Unit =
            item = null.asInstanceOf[T]

          override def result(): Option[T] =
            Option(item)

          override def addOne(elem: T): this.type = {
            item = elem
            this
          }
        }
    }

}
