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

import java.sql.ResultSet
import scala.collection.{mutable, Factory}
import scala.collection.immutable.ArraySeq
import scala.reflect.ClassTag

package object justsql {

  implicit class StringImplicits(val sql: String) extends AnyVal {
    def update(): UpdateSQL =
      UpdateSQL(sql)

    def select[ROW]()(implicit rowReader: RowReader[ROW],
                      classTag: ClassTag[ROW]): SelectSQL[ROW, ArraySeq] =
      SelectSQL(sql, Params())

    def select[ROW, C[+R] <: Iterable[R]]()(implicit rowReader: RowReader[ROW],
                                            factory: Factory[ROW, C[ROW]]): SelectSQL[ROW, C] =
      SelectSQL[ROW, C](sql, Params())

    def explain[C[+R] <: Iterable[R]]()(implicit factory: Factory[String, C[String]]): SQL[C[String]] =
      SelectSQL[String, C](sql, Params()).explain()

    def explainAnalyze[C[+R] <: Iterable[R]]()(implicit factory: Factory[String, C[String]]): SQL[C[String]] =
      SelectSQL[String, C](sql, Params()).explainAnalyze()

    def unsafeSelect[ROW](rowParser: ResultSet => ROW)(implicit classTag: ClassTag[ROW]): SelectSQL[ROW, ArraySeq] =
      SelectSQL.unsafe(sql)(rowParser(_))

    def unsafeSelectC[ROW, C[+R] <: Iterable[R]](rowParser: ResultSet => ROW)(implicit factory: Factory[ROW, C[ROW]]): SelectSQL[ROW, C] =
      SelectSQL.unsafeC[ROW, C](sql)(rowParser(_))
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

  implicit class EmbedSqlActionImplicits[A](val sql: TrackedSQL[A]) extends AnyVal {
    def embed(implicit builder: Params): String =
      builder embed sql
  }

  implicit def optionFactory[A]: Factory[A, Option[A]] =
    new Factory[A, Option[A]] {
      override def fromSpecific(it: IterableOnce[A]): Option[A] =
        it.iterator.nextOption()

      override def newBuilder: mutable.Builder[A, Option[A]] =
        new mutable.Builder[A, Option[A]] {
          private var item: A = _

          override def clear(): Unit =
            item = null.asInstanceOf[A]

          override def result(): Option[A] =
            Option(item)

          override def addOne(elem: A): this.type = {
            item = elem
            this
          }
        }
    }

}
