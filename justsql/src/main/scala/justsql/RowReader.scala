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


import java.sql.ResultSet

trait RowReader[ROW] extends Function[ResultSet, ROW] {
  def apply(resultSet: ResultSet): ROW
}

object RowReader {

  //primitive types
  implicit val intRowReader: RowReader[Int] = tuple1[Int]
  implicit val stringRowReader: RowReader[String] = tuple1[String]
  implicit val booleanRowReader: RowReader[Boolean] = tuple1[Boolean]
  implicit val longRowReader: RowReader[Long] = tuple1[Long]
  implicit val byteRowReader: RowReader[Byte] = tuple1[Byte]
  implicit val doubleRowReader: RowReader[Double] = tuple1[Double]
  implicit val floatRowReader: RowReader[Float] = tuple1[Float]
  implicit val shortRowReader: RowReader[Short] = tuple1[Short]
  implicit val bigDecimalRowReader: RowReader[BigDecimal] = tuple1[BigDecimal]
  implicit val byteArrayRowReader: RowReader[Array[Byte]] = tuple1[Array[Byte]]

  //java.sql types
  implicit val timestampRowReader: RowReader[java.sql.Timestamp] = tuple1[java.sql.Timestamp]
  implicit val blobRowReader: RowReader[java.sql.Blob] = tuple1[java.sql.Blob]
  implicit val clobRowReader: RowReader[java.sql.Clob] = tuple1[java.sql.Clob]
  implicit val dateRowReader: RowReader[java.sql.Date] = tuple1[java.sql.Date]
  implicit val timeRowReader: RowReader[java.sql.Time] = tuple1[java.sql.Time]
  implicit val refRowReader: RowReader[java.sql.Ref] = tuple1[java.sql.Ref]
  implicit val nClobRowReader: RowReader[java.sql.NClob] = tuple1[java.sql.NClob]
  implicit val rowIdRowReader: RowReader[java.sql.RowId] = tuple1[java.sql.RowId]
  implicit val sqlXMLRowReader: RowReader[java.sql.SQLXML] = tuple1[java.sql.SQLXML]

  //other types
  implicit val ioReaderRowReader: RowReader[java.io.Reader] = tuple1[java.io.Reader]
  implicit val ioInputStreamRowReader: RowReader[java.io.InputStream] = tuple1[java.io.InputStream]
  implicit val urlRowReader: RowReader[java.net.URL] = tuple1[java.net.URL]

  implicit def tuple1[T1](implicit t1: ColReader[T1]): RowReader[T1] =
    new RowReader[T1] {
      def apply(resultSet: ResultSet): T1 =
        t1(resultSet, 1)
    }

  implicit def tuple2[T1, T2](implicit t1: ColReader[T1],
                              t2: ColReader[T2]): RowReader[(T1, T2)] =
    new RowReader[(T1, T2)] {
      def apply(resultSet: ResultSet): (T1, T2) =
        (t1(resultSet, 1),
          t2(resultSet, 2))
    }

  implicit def tuple3[T1, T2, T3](implicit t1: ColReader[T1],
                                  t2: ColReader[T2],
                                  t3: ColReader[T3]): RowReader[(T1, T2, T3)] =
    new RowReader[(T1, T2, T3)] {
      def apply(resultSet: ResultSet): (T1, T2, T3) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3))
    }

  implicit def tuple4[T1, T2, T3, T4](implicit t1: ColReader[T1],
                                      t2: ColReader[T2],
                                      t3: ColReader[T3],
                                      t4: ColReader[T4]): RowReader[(T1, T2, T3, T4)] =
    new RowReader[(T1, T2, T3, T4)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4))
    }

  implicit def tuple5[T1, T2, T3, T4, T5](implicit t1: ColReader[T1],
                                          t2: ColReader[T2],
                                          t3: ColReader[T3],
                                          t4: ColReader[T4],
                                          t5: ColReader[T5]): RowReader[(T1, T2, T3, T4, T5)] =
    new RowReader[(T1, T2, T3, T4, T5)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5))
    }

  implicit def tuple6[T1, T2, T3, T4, T5, T6](implicit t1: ColReader[T1],
                                              t2: ColReader[T2],
                                              t3: ColReader[T3],
                                              t4: ColReader[T4],
                                              t5: ColReader[T5],
                                              t6: ColReader[T6]): RowReader[(T1, T2, T3, T4, T5, T6)] =
    new RowReader[(T1, T2, T3, T4, T5, T6)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5, T6) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6))
    }

  implicit def tuple7[T1, T2, T3, T4, T5, T6, T7](implicit t1: ColReader[T1],
                                                  t2: ColReader[T2],
                                                  t3: ColReader[T3],
                                                  t4: ColReader[T4],
                                                  t5: ColReader[T5],
                                                  t6: ColReader[T6],
                                                  t7: ColReader[T7]): RowReader[(T1, T2, T3, T4, T5, T6, T7)] =
    new RowReader[(T1, T2, T3, T4, T5, T6, T7)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5, T6, T7) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7))
    }

  implicit def tuple8[T1, T2, T3, T4, T5, T6, T7, T8](implicit t1: ColReader[T1],
                                                      t2: ColReader[T2],
                                                      t3: ColReader[T3],
                                                      t4: ColReader[T4],
                                                      t5: ColReader[T5],
                                                      t6: ColReader[T6],
                                                      t7: ColReader[T7],
                                                      t8: ColReader[T8]): RowReader[(T1, T2, T3, T4, T5, T6, T7, T8)] =
    new RowReader[(T1, T2, T3, T4, T5, T6, T7, T8)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5, T6, T7, T8) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8))
    }

  implicit def tuple9[T1, T2, T3, T4, T5, T6, T7, T8, T9](implicit t1: ColReader[T1],
                                                          t2: ColReader[T2],
                                                          t3: ColReader[T3],
                                                          t4: ColReader[T4],
                                                          t5: ColReader[T5],
                                                          t6: ColReader[T6],
                                                          t7: ColReader[T7],
                                                          t8: ColReader[T8],
                                                          t9: ColReader[T9]): RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9)] =
    new RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5, T6, T7, T8, T9) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9))
    }

  implicit def tuple10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](implicit t1: ColReader[T1],
                                                                t2: ColReader[T2],
                                                                t3: ColReader[T3],
                                                                t4: ColReader[T4],
                                                                t5: ColReader[T5],
                                                                t6: ColReader[T6],
                                                                t7: ColReader[T7],
                                                                t8: ColReader[T8],
                                                                t9: ColReader[T9],
                                                                t10: ColReader[T10]): RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)] =
    new RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10))
    }

  implicit def tuple11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](implicit t1: ColReader[T1],
                                                                     t2: ColReader[T2],
                                                                     t3: ColReader[T3],
                                                                     t4: ColReader[T4],
                                                                     t5: ColReader[T5],
                                                                     t6: ColReader[T6],
                                                                     t7: ColReader[T7],
                                                                     t8: ColReader[T8],
                                                                     t9: ColReader[T9],
                                                                     t10: ColReader[T10],
                                                                     t11: ColReader[T11]): RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)] =
    new RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11))
    }

  implicit def tuple12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](implicit t1: ColReader[T1],
                                                                          t2: ColReader[T2],
                                                                          t3: ColReader[T3],
                                                                          t4: ColReader[T4],
                                                                          t5: ColReader[T5],
                                                                          t6: ColReader[T6],
                                                                          t7: ColReader[T7],
                                                                          t8: ColReader[T8],
                                                                          t9: ColReader[T9],
                                                                          t10: ColReader[T10],
                                                                          t11: ColReader[T11],
                                                                          t12: ColReader[T12]): RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)] =
    new RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12))
    }

  implicit def tuple13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](implicit t1: ColReader[T1],
                                                                               t2: ColReader[T2],
                                                                               t3: ColReader[T3],
                                                                               t4: ColReader[T4],
                                                                               t5: ColReader[T5],
                                                                               t6: ColReader[T6],
                                                                               t7: ColReader[T7],
                                                                               t8: ColReader[T8],
                                                                               t9: ColReader[T9],
                                                                               t10: ColReader[T10],
                                                                               t11: ColReader[T11],
                                                                               t12: ColReader[T12],
                                                                               t13: ColReader[T13]): RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)] =
    new RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13))
    }

  implicit def tuple14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](implicit t1: ColReader[T1],
                                                                                    t2: ColReader[T2],
                                                                                    t3: ColReader[T3],
                                                                                    t4: ColReader[T4],
                                                                                    t5: ColReader[T5],
                                                                                    t6: ColReader[T6],
                                                                                    t7: ColReader[T7],
                                                                                    t8: ColReader[T8],
                                                                                    t9: ColReader[T9],
                                                                                    t10: ColReader[T10],
                                                                                    t11: ColReader[T11],
                                                                                    t12: ColReader[T12],
                                                                                    t13: ColReader[T13],
                                                                                    t14: ColReader[T14]): RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)] =
    new RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13),
          t14(resultSet, 14))
    }

  implicit def tuple15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](implicit t1: ColReader[T1],
                                                                                         t2: ColReader[T2],
                                                                                         t3: ColReader[T3],
                                                                                         t4: ColReader[T4],
                                                                                         t5: ColReader[T5],
                                                                                         t6: ColReader[T6],
                                                                                         t7: ColReader[T7],
                                                                                         t8: ColReader[T8],
                                                                                         t9: ColReader[T9],
                                                                                         t10: ColReader[T10],
                                                                                         t11: ColReader[T11],
                                                                                         t12: ColReader[T12],
                                                                                         t13: ColReader[T13],
                                                                                         t14: ColReader[T14],
                                                                                         t15: ColReader[T15]): RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)] =
    new RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13),
          t14(resultSet, 14),
          t15(resultSet, 15))
    }

  implicit def tuple16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](implicit t1: ColReader[T1],
                                                                                              t2: ColReader[T2],
                                                                                              t3: ColReader[T3],
                                                                                              t4: ColReader[T4],
                                                                                              t5: ColReader[T5],
                                                                                              t6: ColReader[T6],
                                                                                              t7: ColReader[T7],
                                                                                              t8: ColReader[T8],
                                                                                              t9: ColReader[T9],
                                                                                              t10: ColReader[T10],
                                                                                              t11: ColReader[T11],
                                                                                              t12: ColReader[T12],
                                                                                              t13: ColReader[T13],
                                                                                              t14: ColReader[T14],
                                                                                              t15: ColReader[T15],
                                                                                              t16: ColReader[T16]): RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)] =
    new RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13),
          t14(resultSet, 14),
          t15(resultSet, 15),
          t16(resultSet, 16))
    }

  implicit def tuple17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](implicit t1: ColReader[T1],
                                                                                                   t2: ColReader[T2],
                                                                                                   t3: ColReader[T3],
                                                                                                   t4: ColReader[T4],
                                                                                                   t5: ColReader[T5],
                                                                                                   t6: ColReader[T6],
                                                                                                   t7: ColReader[T7],
                                                                                                   t8: ColReader[T8],
                                                                                                   t9: ColReader[T9],
                                                                                                   t10: ColReader[T10],
                                                                                                   t11: ColReader[T11],
                                                                                                   t12: ColReader[T12],
                                                                                                   t13: ColReader[T13],
                                                                                                   t14: ColReader[T14],
                                                                                                   t15: ColReader[T15],
                                                                                                   t16: ColReader[T16],
                                                                                                   t17: ColReader[T17]): RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)] =
    new RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13),
          t14(resultSet, 14),
          t15(resultSet, 15),
          t16(resultSet, 16),
          t17(resultSet, 17))
    }

  implicit def tuple18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](implicit t1: ColReader[T1],
                                                                                                        t2: ColReader[T2],
                                                                                                        t3: ColReader[T3],
                                                                                                        t4: ColReader[T4],
                                                                                                        t5: ColReader[T5],
                                                                                                        t6: ColReader[T6],
                                                                                                        t7: ColReader[T7],
                                                                                                        t8: ColReader[T8],
                                                                                                        t9: ColReader[T9],
                                                                                                        t10: ColReader[T10],
                                                                                                        t11: ColReader[T11],
                                                                                                        t12: ColReader[T12],
                                                                                                        t13: ColReader[T13],
                                                                                                        t14: ColReader[T14],
                                                                                                        t15: ColReader[T15],
                                                                                                        t16: ColReader[T16],
                                                                                                        t17: ColReader[T17],
                                                                                                        t18: ColReader[T18]): RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)] =
    new RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13),
          t14(resultSet, 14),
          t15(resultSet, 15),
          t16(resultSet, 16),
          t17(resultSet, 17),
          t18(resultSet, 18))
    }

  implicit def tuple19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](implicit t1: ColReader[T1],
                                                                                                             t2: ColReader[T2],
                                                                                                             t3: ColReader[T3],
                                                                                                             t4: ColReader[T4],
                                                                                                             t5: ColReader[T5],
                                                                                                             t6: ColReader[T6],
                                                                                                             t7: ColReader[T7],
                                                                                                             t8: ColReader[T8],
                                                                                                             t9: ColReader[T9],
                                                                                                             t10: ColReader[T10],
                                                                                                             t11: ColReader[T11],
                                                                                                             t12: ColReader[T12],
                                                                                                             t13: ColReader[T13],
                                                                                                             t14: ColReader[T14],
                                                                                                             t15: ColReader[T15],
                                                                                                             t16: ColReader[T16],
                                                                                                             t17: ColReader[T17],
                                                                                                             t18: ColReader[T18],
                                                                                                             t19: ColReader[T19]): RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)] =
    new RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13),
          t14(resultSet, 14),
          t15(resultSet, 15),
          t16(resultSet, 16),
          t17(resultSet, 17),
          t18(resultSet, 18),
          t19(resultSet, 19))
    }

  implicit def tuple20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](implicit t1: ColReader[T1],
                                                                                                                  t2: ColReader[T2],
                                                                                                                  t3: ColReader[T3],
                                                                                                                  t4: ColReader[T4],
                                                                                                                  t5: ColReader[T5],
                                                                                                                  t6: ColReader[T6],
                                                                                                                  t7: ColReader[T7],
                                                                                                                  t8: ColReader[T8],
                                                                                                                  t9: ColReader[T9],
                                                                                                                  t10: ColReader[T10],
                                                                                                                  t11: ColReader[T11],
                                                                                                                  t12: ColReader[T12],
                                                                                                                  t13: ColReader[T13],
                                                                                                                  t14: ColReader[T14],
                                                                                                                  t15: ColReader[T15],
                                                                                                                  t16: ColReader[T16],
                                                                                                                  t17: ColReader[T17],
                                                                                                                  t18: ColReader[T18],
                                                                                                                  t19: ColReader[T19],
                                                                                                                  t20: ColReader[T20]): RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)] =
    new RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13),
          t14(resultSet, 14),
          t15(resultSet, 15),
          t16(resultSet, 16),
          t17(resultSet, 17),
          t18(resultSet, 18),
          t19(resultSet, 19),
          t20(resultSet, 20))
    }

  implicit def tuple21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](implicit t1: ColReader[T1],
                                                                                                                       t2: ColReader[T2],
                                                                                                                       t3: ColReader[T3],
                                                                                                                       t4: ColReader[T4],
                                                                                                                       t5: ColReader[T5],
                                                                                                                       t6: ColReader[T6],
                                                                                                                       t7: ColReader[T7],
                                                                                                                       t8: ColReader[T8],
                                                                                                                       t9: ColReader[T9],
                                                                                                                       t10: ColReader[T10],
                                                                                                                       t11: ColReader[T11],
                                                                                                                       t12: ColReader[T12],
                                                                                                                       t13: ColReader[T13],
                                                                                                                       t14: ColReader[T14],
                                                                                                                       t15: ColReader[T15],
                                                                                                                       t16: ColReader[T16],
                                                                                                                       t17: ColReader[T17],
                                                                                                                       t18: ColReader[T18],
                                                                                                                       t19: ColReader[T19],
                                                                                                                       t20: ColReader[T20],
                                                                                                                       t21: ColReader[T21]): RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)] =
    new RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13),
          t14(resultSet, 14),
          t15(resultSet, 15),
          t16(resultSet, 16),
          t17(resultSet, 17),
          t18(resultSet, 18),
          t19(resultSet, 19),
          t20(resultSet, 20),
          t21(resultSet, 21))
    }

  implicit def tuple22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](implicit t1: ColReader[T1],
                                                                                                                            t2: ColReader[T2],
                                                                                                                            t3: ColReader[T3],
                                                                                                                            t4: ColReader[T4],
                                                                                                                            t5: ColReader[T5],
                                                                                                                            t6: ColReader[T6],
                                                                                                                            t7: ColReader[T7],
                                                                                                                            t8: ColReader[T8],
                                                                                                                            t9: ColReader[T9],
                                                                                                                            t10: ColReader[T10],
                                                                                                                            t11: ColReader[T11],
                                                                                                                            t12: ColReader[T12],
                                                                                                                            t13: ColReader[T13],
                                                                                                                            t14: ColReader[T14],
                                                                                                                            t15: ColReader[T15],
                                                                                                                            t16: ColReader[T16],
                                                                                                                            t17: ColReader[T17],
                                                                                                                            t18: ColReader[T18],
                                                                                                                            t19: ColReader[T19],
                                                                                                                            t20: ColReader[T20],
                                                                                                                            t21: ColReader[T21],
                                                                                                                            t22: ColReader[T22]): RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)] =
    new RowReader[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13),
          t14(resultSet, 14),
          t15(resultSet, 15),
          t16(resultSet, 16),
          t17(resultSet, 17),
          t18(resultSet, 18),
          t19(resultSet, 19),
          t20(resultSet, 20),
          t21(resultSet, 21),
          t22(resultSet, 22))
    }

  def apply[T1, R](f: T1 => R)(implicit t1: ColReader[T1]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1))
    }

  def apply[T1, T2, R](f: ((T1, T2)) => R)(implicit t1: ColReader[T1],
                                           t2: ColReader[T2]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2)))
    }

  def apply[T1, T2, T3, R](f: ((T1, T2, T3)) => R)(implicit t1: ColReader[T1],
                                                   t2: ColReader[T2],
                                                   t3: ColReader[T3]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3)))
    }

  def apply[T1, T2, T3, T4, R](f: ((T1, T2, T3, T4)) => R)(implicit t1: ColReader[T1],
                                                           t2: ColReader[T2],
                                                           t3: ColReader[T3],
                                                           t4: ColReader[T4]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4)))
    }

  def apply[T1, T2, T3, T4, T5, R](f: ((T1, T2, T3, T4, T5)) => R)(implicit t1: ColReader[T1],
                                                                   t2: ColReader[T2],
                                                                   t3: ColReader[T3],
                                                                   t4: ColReader[T4],
                                                                   t5: ColReader[T5]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5)))
    }

  def apply[T1, T2, T3, T4, T5, T6, R](f: ((T1, T2, T3, T4, T5, T6)) => R)(implicit t1: ColReader[T1],
                                                                           t2: ColReader[T2],
                                                                           t3: ColReader[T3],
                                                                           t4: ColReader[T4],
                                                                           t5: ColReader[T5],
                                                                           t6: ColReader[T6]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6)))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, R](f: ((T1, T2, T3, T4, T5, T6, T7)) => R)(implicit t1: ColReader[T1],
                                                                                   t2: ColReader[T2],
                                                                                   t3: ColReader[T3],
                                                                                   t4: ColReader[T4],
                                                                                   t5: ColReader[T5],
                                                                                   t6: ColReader[T6],
                                                                                   t7: ColReader[T7]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7)))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8)) => R)(implicit t1: ColReader[T1],
                                                                                           t2: ColReader[T2],
                                                                                           t3: ColReader[T3],
                                                                                           t4: ColReader[T4],
                                                                                           t5: ColReader[T5],
                                                                                           t6: ColReader[T6],
                                                                                           t7: ColReader[T7],
                                                                                           t8: ColReader[T8]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8)))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9)) => R)(implicit t1: ColReader[T1],
                                                                                                   t2: ColReader[T2],
                                                                                                   t3: ColReader[T3],
                                                                                                   t4: ColReader[T4],
                                                                                                   t5: ColReader[T5],
                                                                                                   t6: ColReader[T6],
                                                                                                   t7: ColReader[T7],
                                                                                                   t8: ColReader[T8],
                                                                                                   t9: ColReader[T9]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9)))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)) => R)(implicit t1: ColReader[T1],
                                                                                                             t2: ColReader[T2],
                                                                                                             t3: ColReader[T3],
                                                                                                             t4: ColReader[T4],
                                                                                                             t5: ColReader[T5],
                                                                                                             t6: ColReader[T6],
                                                                                                             t7: ColReader[T7],
                                                                                                             t8: ColReader[T8],
                                                                                                             t9: ColReader[T9],
                                                                                                             t10: ColReader[T10]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10)))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)) => R)(implicit t1: ColReader[T1],
                                                                                                                       t2: ColReader[T2],
                                                                                                                       t3: ColReader[T3],
                                                                                                                       t4: ColReader[T4],
                                                                                                                       t5: ColReader[T5],
                                                                                                                       t6: ColReader[T6],
                                                                                                                       t7: ColReader[T7],
                                                                                                                       t8: ColReader[T8],
                                                                                                                       t9: ColReader[T9],
                                                                                                                       t10: ColReader[T10],
                                                                                                                       t11: ColReader[T11]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11)))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)) => R)(implicit t1: ColReader[T1],
                                                                                                                                 t2: ColReader[T2],
                                                                                                                                 t3: ColReader[T3],
                                                                                                                                 t4: ColReader[T4],
                                                                                                                                 t5: ColReader[T5],
                                                                                                                                 t6: ColReader[T6],
                                                                                                                                 t7: ColReader[T7],
                                                                                                                                 t8: ColReader[T8],
                                                                                                                                 t9: ColReader[T9],
                                                                                                                                 t10: ColReader[T10],
                                                                                                                                 t11: ColReader[T11],
                                                                                                                                 t12: ColReader[T12]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12)))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)) => R)(implicit t1: ColReader[T1],
                                                                                                                                           t2: ColReader[T2],
                                                                                                                                           t3: ColReader[T3],
                                                                                                                                           t4: ColReader[T4],
                                                                                                                                           t5: ColReader[T5],
                                                                                                                                           t6: ColReader[T6],
                                                                                                                                           t7: ColReader[T7],
                                                                                                                                           t8: ColReader[T8],
                                                                                                                                           t9: ColReader[T9],
                                                                                                                                           t10: ColReader[T10],
                                                                                                                                           t11: ColReader[T11],
                                                                                                                                           t12: ColReader[T12],
                                                                                                                                           t13: ColReader[T13]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13)))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)) => R)(implicit t1: ColReader[T1],
                                                                                                                                                     t2: ColReader[T2],
                                                                                                                                                     t3: ColReader[T3],
                                                                                                                                                     t4: ColReader[T4],
                                                                                                                                                     t5: ColReader[T5],
                                                                                                                                                     t6: ColReader[T6],
                                                                                                                                                     t7: ColReader[T7],
                                                                                                                                                     t8: ColReader[T8],
                                                                                                                                                     t9: ColReader[T9],
                                                                                                                                                     t10: ColReader[T10],
                                                                                                                                                     t11: ColReader[T11],
                                                                                                                                                     t12: ColReader[T12],
                                                                                                                                                     t13: ColReader[T13],
                                                                                                                                                     t14: ColReader[T14]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13),
          t14(resultSet, 14)))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)) => R)(implicit t1: ColReader[T1],
                                                                                                                                                               t2: ColReader[T2],
                                                                                                                                                               t3: ColReader[T3],
                                                                                                                                                               t4: ColReader[T4],
                                                                                                                                                               t5: ColReader[T5],
                                                                                                                                                               t6: ColReader[T6],
                                                                                                                                                               t7: ColReader[T7],
                                                                                                                                                               t8: ColReader[T8],
                                                                                                                                                               t9: ColReader[T9],
                                                                                                                                                               t10: ColReader[T10],
                                                                                                                                                               t11: ColReader[T11],
                                                                                                                                                               t12: ColReader[T12],
                                                                                                                                                               t13: ColReader[T13],
                                                                                                                                                               t14: ColReader[T14],
                                                                                                                                                               t15: ColReader[T15]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13),
          t14(resultSet, 14),
          t15(resultSet, 15)))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)) => R)(implicit t1: ColReader[T1],
                                                                                                                                                                         t2: ColReader[T2],
                                                                                                                                                                         t3: ColReader[T3],
                                                                                                                                                                         t4: ColReader[T4],
                                                                                                                                                                         t5: ColReader[T5],
                                                                                                                                                                         t6: ColReader[T6],
                                                                                                                                                                         t7: ColReader[T7],
                                                                                                                                                                         t8: ColReader[T8],
                                                                                                                                                                         t9: ColReader[T9],
                                                                                                                                                                         t10: ColReader[T10],
                                                                                                                                                                         t11: ColReader[T11],
                                                                                                                                                                         t12: ColReader[T12],
                                                                                                                                                                         t13: ColReader[T13],
                                                                                                                                                                         t14: ColReader[T14],
                                                                                                                                                                         t15: ColReader[T15],
                                                                                                                                                                         t16: ColReader[T16]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13),
          t14(resultSet, 14),
          t15(resultSet, 15),
          t16(resultSet, 16)))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)) => R)(implicit t1: ColReader[T1],
                                                                                                                                                                                   t2: ColReader[T2],
                                                                                                                                                                                   t3: ColReader[T3],
                                                                                                                                                                                   t4: ColReader[T4],
                                                                                                                                                                                   t5: ColReader[T5],
                                                                                                                                                                                   t6: ColReader[T6],
                                                                                                                                                                                   t7: ColReader[T7],
                                                                                                                                                                                   t8: ColReader[T8],
                                                                                                                                                                                   t9: ColReader[T9],
                                                                                                                                                                                   t10: ColReader[T10],
                                                                                                                                                                                   t11: ColReader[T11],
                                                                                                                                                                                   t12: ColReader[T12],
                                                                                                                                                                                   t13: ColReader[T13],
                                                                                                                                                                                   t14: ColReader[T14],
                                                                                                                                                                                   t15: ColReader[T15],
                                                                                                                                                                                   t16: ColReader[T16],
                                                                                                                                                                                   t17: ColReader[T17]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13),
          t14(resultSet, 14),
          t15(resultSet, 15),
          t16(resultSet, 16),
          t17(resultSet, 17)))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)) => R)(implicit t1: ColReader[T1],
                                                                                                                                                                                             t2: ColReader[T2],
                                                                                                                                                                                             t3: ColReader[T3],
                                                                                                                                                                                             t4: ColReader[T4],
                                                                                                                                                                                             t5: ColReader[T5],
                                                                                                                                                                                             t6: ColReader[T6],
                                                                                                                                                                                             t7: ColReader[T7],
                                                                                                                                                                                             t8: ColReader[T8],
                                                                                                                                                                                             t9: ColReader[T9],
                                                                                                                                                                                             t10: ColReader[T10],
                                                                                                                                                                                             t11: ColReader[T11],
                                                                                                                                                                                             t12: ColReader[T12],
                                                                                                                                                                                             t13: ColReader[T13],
                                                                                                                                                                                             t14: ColReader[T14],
                                                                                                                                                                                             t15: ColReader[T15],
                                                                                                                                                                                             t16: ColReader[T16],
                                                                                                                                                                                             t17: ColReader[T17],
                                                                                                                                                                                             t18: ColReader[T18]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13),
          t14(resultSet, 14),
          t15(resultSet, 15),
          t16(resultSet, 16),
          t17(resultSet, 17),
          t18(resultSet, 18)))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)) => R)(implicit t1: ColReader[T1],
                                                                                                                                                                                                       t2: ColReader[T2],
                                                                                                                                                                                                       t3: ColReader[T3],
                                                                                                                                                                                                       t4: ColReader[T4],
                                                                                                                                                                                                       t5: ColReader[T5],
                                                                                                                                                                                                       t6: ColReader[T6],
                                                                                                                                                                                                       t7: ColReader[T7],
                                                                                                                                                                                                       t8: ColReader[T8],
                                                                                                                                                                                                       t9: ColReader[T9],
                                                                                                                                                                                                       t10: ColReader[T10],
                                                                                                                                                                                                       t11: ColReader[T11],
                                                                                                                                                                                                       t12: ColReader[T12],
                                                                                                                                                                                                       t13: ColReader[T13],
                                                                                                                                                                                                       t14: ColReader[T14],
                                                                                                                                                                                                       t15: ColReader[T15],
                                                                                                                                                                                                       t16: ColReader[T16],
                                                                                                                                                                                                       t17: ColReader[T17],
                                                                                                                                                                                                       t18: ColReader[T18],
                                                                                                                                                                                                       t19: ColReader[T19]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13),
          t14(resultSet, 14),
          t15(resultSet, 15),
          t16(resultSet, 16),
          t17(resultSet, 17),
          t18(resultSet, 18),
          t19(resultSet, 19)))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)) => R)(implicit t1: ColReader[T1],
                                                                                                                                                                                                                 t2: ColReader[T2],
                                                                                                                                                                                                                 t3: ColReader[T3],
                                                                                                                                                                                                                 t4: ColReader[T4],
                                                                                                                                                                                                                 t5: ColReader[T5],
                                                                                                                                                                                                                 t6: ColReader[T6],
                                                                                                                                                                                                                 t7: ColReader[T7],
                                                                                                                                                                                                                 t8: ColReader[T8],
                                                                                                                                                                                                                 t9: ColReader[T9],
                                                                                                                                                                                                                 t10: ColReader[T10],
                                                                                                                                                                                                                 t11: ColReader[T11],
                                                                                                                                                                                                                 t12: ColReader[T12],
                                                                                                                                                                                                                 t13: ColReader[T13],
                                                                                                                                                                                                                 t14: ColReader[T14],
                                                                                                                                                                                                                 t15: ColReader[T15],
                                                                                                                                                                                                                 t16: ColReader[T16],
                                                                                                                                                                                                                 t17: ColReader[T17],
                                                                                                                                                                                                                 t18: ColReader[T18],
                                                                                                                                                                                                                 t19: ColReader[T19],
                                                                                                                                                                                                                 t20: ColReader[T20]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13),
          t14(resultSet, 14),
          t15(resultSet, 15),
          t16(resultSet, 16),
          t17(resultSet, 17),
          t18(resultSet, 18),
          t19(resultSet, 19),
          t20(resultSet, 20)))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)) => R)(implicit t1: ColReader[T1],
                                                                                                                                                                                                                           t2: ColReader[T2],
                                                                                                                                                                                                                           t3: ColReader[T3],
                                                                                                                                                                                                                           t4: ColReader[T4],
                                                                                                                                                                                                                           t5: ColReader[T5],
                                                                                                                                                                                                                           t6: ColReader[T6],
                                                                                                                                                                                                                           t7: ColReader[T7],
                                                                                                                                                                                                                           t8: ColReader[T8],
                                                                                                                                                                                                                           t9: ColReader[T9],
                                                                                                                                                                                                                           t10: ColReader[T10],
                                                                                                                                                                                                                           t11: ColReader[T11],
                                                                                                                                                                                                                           t12: ColReader[T12],
                                                                                                                                                                                                                           t13: ColReader[T13],
                                                                                                                                                                                                                           t14: ColReader[T14],
                                                                                                                                                                                                                           t15: ColReader[T15],
                                                                                                                                                                                                                           t16: ColReader[T16],
                                                                                                                                                                                                                           t17: ColReader[T17],
                                                                                                                                                                                                                           t18: ColReader[T18],
                                                                                                                                                                                                                           t19: ColReader[T19],
                                                                                                                                                                                                                           t20: ColReader[T20],
                                                                                                                                                                                                                           t21: ColReader[T21]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13),
          t14(resultSet, 14),
          t15(resultSet, 15),
          t16(resultSet, 16),
          t17(resultSet, 17),
          t18(resultSet, 18),
          t19(resultSet, 19),
          t20(resultSet, 20),
          t21(resultSet, 21)))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)) => R)(implicit t1: ColReader[T1],
                                                                                                                                                                                                                                     t2: ColReader[T2],
                                                                                                                                                                                                                                     t3: ColReader[T3],
                                                                                                                                                                                                                                     t4: ColReader[T4],
                                                                                                                                                                                                                                     t5: ColReader[T5],
                                                                                                                                                                                                                                     t6: ColReader[T6],
                                                                                                                                                                                                                                     t7: ColReader[T7],
                                                                                                                                                                                                                                     t8: ColReader[T8],
                                                                                                                                                                                                                                     t9: ColReader[T9],
                                                                                                                                                                                                                                     t10: ColReader[T10],
                                                                                                                                                                                                                                     t11: ColReader[T11],
                                                                                                                                                                                                                                     t12: ColReader[T12],
                                                                                                                                                                                                                                     t13: ColReader[T13],
                                                                                                                                                                                                                                     t14: ColReader[T14],
                                                                                                                                                                                                                                     t15: ColReader[T15],
                                                                                                                                                                                                                                     t16: ColReader[T16],
                                                                                                                                                                                                                                     t17: ColReader[T17],
                                                                                                                                                                                                                                     t18: ColReader[T18],
                                                                                                                                                                                                                                     t19: ColReader[T19],
                                                                                                                                                                                                                                     t20: ColReader[T20],
                                                                                                                                                                                                                                     t21: ColReader[T21],
                                                                                                                                                                                                                                     t22: ColReader[T22]): RowReader[R] =
    new RowReader[R] {
      def apply(resultSet: ResultSet): R =
        f((t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9),
          t10(resultSet, 10),
          t11(resultSet, 11),
          t12(resultSet, 12),
          t13(resultSet, 13),
          t14(resultSet, 14),
          t15(resultSet, 15),
          t16(resultSet, 16),
          t17(resultSet, 17),
          t18(resultSet, 18),
          t19(resultSet, 19),
          t20(resultSet, 20),
          t21(resultSet, 21),
          t22(resultSet, 22)))
    }

}
