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

import java.sql.PreparedStatement

trait SqlParam[P] extends ((PreparedStatement, Int, P) => Unit) {
  /** Total number of parameters this [[SqlParam]] writes to a [[PreparedStatement]] */
  def parametersCount(): Int

  override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: P): Unit
}

trait OneSqlParam[P] extends SqlParam[P] {

  final val parametersCount: Int = 1

}

object SqlParam {
  implicit val intColWriter: OneSqlParam[Int] = _.setInt(_, _)
  implicit val stringColWriter: OneSqlParam[String] = _.setString(_, _)
  implicit val booleanColWriter: OneSqlParam[Boolean] = _.setBoolean(_, _)
  implicit val longColWriter: OneSqlParam[Long] = _.setLong(_, _)
  implicit val byteColWriter: OneSqlParam[Byte] = _.setByte(_, _)
  implicit val doubleColWriter: OneSqlParam[Double] = _.setDouble(_, _)
  implicit val floatColWriter: OneSqlParam[Float] = _.setFloat(_, _)
  implicit val shortColWriter: OneSqlParam[Short] = _.setShort(_, _)
  implicit val bigDecimalColWriter: OneSqlParam[java.math.BigDecimal] = _.setBigDecimal(_, _)
  implicit val byteArrayColWriter: OneSqlParam[Array[Byte]] = _.setBytes(_, _)

  //java.sql types
  implicit val timestampColWriter: OneSqlParam[java.sql.Timestamp] = _.setTimestamp(_, _)
  implicit val blobColWriter: OneSqlParam[java.sql.Blob] = _.setBlob(_, _)
  implicit val clobColWriter: OneSqlParam[java.sql.Clob] = _.setClob(_, _)
  implicit val dateColWriter: OneSqlParam[java.sql.Date] = _.setDate(_, _)
  implicit val timeColWriter: OneSqlParam[java.sql.Time] = _.setTime(_, _)
  implicit val refColWriter: OneSqlParam[java.sql.Ref] = _.setRef(_, _)
  implicit val nClobColWriter: OneSqlParam[java.sql.NClob] = _.setNClob(_, _)
  implicit val rowIdColWriter: OneSqlParam[java.sql.RowId] = _.setRowId(_, _)
  implicit val sqlXMLColWriter: OneSqlParam[java.sql.SQLXML] = _.setSQLXML(_, _)

  //other types
  implicit val characterStreamColWriter: OneSqlParam[java.io.Reader] = _.setCharacterStream(_, _)
  implicit val binaryStreamColWriter: OneSqlParam[java.io.InputStream] = _.setBinaryStream(_, _)
  implicit val urlColWriter: OneSqlParam[java.net.URL] = _.setURL(_, _)

  implicit def tuple2[T1, T2](implicit t1: SqlParam[T1],
                              t2: SqlParam[T2]): SqlParam[(T1, T2)] =
    new SqlParam[(T1, T2)] {
      override def parametersCount(): Int = 2

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
      }
    }

  implicit def tuple3[T1, T2, T3](implicit t1: SqlParam[T1],
                                  t2: SqlParam[T2],
                                  t3: SqlParam[T3]): SqlParam[(T1, T2, T3)] =
    new SqlParam[(T1, T2, T3)] {
      override def parametersCount(): Int = 3

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
      }
    }

  implicit def tuple4[T1, T2, T3, T4](implicit t1: SqlParam[T1],
                                      t2: SqlParam[T2],
                                      t3: SqlParam[T3],
                                      t4: SqlParam[T4]): SqlParam[(T1, T2, T3, T4)] =
    new SqlParam[(T1, T2, T3, T4)] {
      override def parametersCount(): Int = 4

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
      }
    }

  implicit def tuple5[T1, T2, T3, T4, T5](implicit t1: SqlParam[T1],
                                          t2: SqlParam[T2],
                                          t3: SqlParam[T3],
                                          t4: SqlParam[T4],
                                          t5: SqlParam[T5]): SqlParam[(T1, T2, T3, T4, T5)] =
    new SqlParam[(T1, T2, T3, T4, T5)] {
      override def parametersCount(): Int = 5

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
      }
    }

  implicit def tuple6[T1, T2, T3, T4, T5, T6](implicit t1: SqlParam[T1],
                                              t2: SqlParam[T2],
                                              t3: SqlParam[T3],
                                              t4: SqlParam[T4],
                                              t5: SqlParam[T5],
                                              t6: SqlParam[T6]): SqlParam[(T1, T2, T3, T4, T5, T6)] =
    new SqlParam[(T1, T2, T3, T4, T5, T6)] {
      override def parametersCount(): Int = 6

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
      }
    }

  implicit def tuple7[T1, T2, T3, T4, T5, T6, T7](implicit t1: SqlParam[T1],
                                                  t2: SqlParam[T2],
                                                  t3: SqlParam[T3],
                                                  t4: SqlParam[T4],
                                                  t5: SqlParam[T5],
                                                  t6: SqlParam[T6],
                                                  t7: SqlParam[T7]): SqlParam[(T1, T2, T3, T4, T5, T6, T7)] =
    new SqlParam[(T1, T2, T3, T4, T5, T6, T7)] {
      override def parametersCount(): Int = 7

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        t7(statement, parameterIndex + 6, paramValue._7)
      }
    }

  implicit def tuple8[T1, T2, T3, T4, T5, T6, T7, T8](implicit t1: SqlParam[T1],
                                                      t2: SqlParam[T2],
                                                      t3: SqlParam[T3],
                                                      t4: SqlParam[T4],
                                                      t5: SqlParam[T5],
                                                      t6: SqlParam[T6],
                                                      t7: SqlParam[T7],
                                                      t8: SqlParam[T8]): SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8)] =
    new SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8)] {
      override def parametersCount(): Int = 8

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        t7(statement, parameterIndex + 6, paramValue._7)
        t8(statement, parameterIndex + 7, paramValue._8)
      }
    }

  implicit def tuple9[T1, T2, T3, T4, T5, T6, T7, T8, T9](implicit t1: SqlParam[T1],
                                                          t2: SqlParam[T2],
                                                          t3: SqlParam[T3],
                                                          t4: SqlParam[T4],
                                                          t5: SqlParam[T5],
                                                          t6: SqlParam[T6],
                                                          t7: SqlParam[T7],
                                                          t8: SqlParam[T8],
                                                          t9: SqlParam[T9]): SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9)] =
    new SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9)] {
      override def parametersCount(): Int = 9

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        t7(statement, parameterIndex + 6, paramValue._7)
        t8(statement, parameterIndex + 7, paramValue._8)
        t9(statement, parameterIndex + 8, paramValue._9)
      }
    }

  implicit def tuple10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](implicit t1: SqlParam[T1],
                                                                t2: SqlParam[T2],
                                                                t3: SqlParam[T3],
                                                                t4: SqlParam[T4],
                                                                t5: SqlParam[T5],
                                                                t6: SqlParam[T6],
                                                                t7: SqlParam[T7],
                                                                t8: SqlParam[T8],
                                                                t9: SqlParam[T9],
                                                                t10: SqlParam[T10]): SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)] =
    new SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)] {
      override def parametersCount(): Int = 10

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        t7(statement, parameterIndex + 6, paramValue._7)
        t8(statement, parameterIndex + 7, paramValue._8)
        t9(statement, parameterIndex + 8, paramValue._9)
        t10(statement, parameterIndex + 9, paramValue._10)
      }
    }

  implicit def tuple11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](implicit t1: SqlParam[T1],
                                                                     t2: SqlParam[T2],
                                                                     t3: SqlParam[T3],
                                                                     t4: SqlParam[T4],
                                                                     t5: SqlParam[T5],
                                                                     t6: SqlParam[T6],
                                                                     t7: SqlParam[T7],
                                                                     t8: SqlParam[T8],
                                                                     t9: SqlParam[T9],
                                                                     t10: SqlParam[T10],
                                                                     t11: SqlParam[T11]): SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)] =
    new SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)] {
      override def parametersCount(): Int = 11

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        t7(statement, parameterIndex + 6, paramValue._7)
        t8(statement, parameterIndex + 7, paramValue._8)
        t9(statement, parameterIndex + 8, paramValue._9)
        t10(statement, parameterIndex + 9, paramValue._10)
        t11(statement, parameterIndex + 10, paramValue._11)
      }
    }

  implicit def tuple12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](implicit t1: SqlParam[T1],
                                                                          t2: SqlParam[T2],
                                                                          t3: SqlParam[T3],
                                                                          t4: SqlParam[T4],
                                                                          t5: SqlParam[T5],
                                                                          t6: SqlParam[T6],
                                                                          t7: SqlParam[T7],
                                                                          t8: SqlParam[T8],
                                                                          t9: SqlParam[T9],
                                                                          t10: SqlParam[T10],
                                                                          t11: SqlParam[T11],
                                                                          t12: SqlParam[T12]): SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)] =
    new SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)] {
      override def parametersCount(): Int = 12

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        t7(statement, parameterIndex + 6, paramValue._7)
        t8(statement, parameterIndex + 7, paramValue._8)
        t9(statement, parameterIndex + 8, paramValue._9)
        t10(statement, parameterIndex + 9, paramValue._10)
        t11(statement, parameterIndex + 10, paramValue._11)
        t12(statement, parameterIndex + 11, paramValue._12)
      }
    }

  implicit def tuple13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](implicit t1: SqlParam[T1],
                                                                               t2: SqlParam[T2],
                                                                               t3: SqlParam[T3],
                                                                               t4: SqlParam[T4],
                                                                               t5: SqlParam[T5],
                                                                               t6: SqlParam[T6],
                                                                               t7: SqlParam[T7],
                                                                               t8: SqlParam[T8],
                                                                               t9: SqlParam[T9],
                                                                               t10: SqlParam[T10],
                                                                               t11: SqlParam[T11],
                                                                               t12: SqlParam[T12],
                                                                               t13: SqlParam[T13]): SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)] =
    new SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)] {
      override def parametersCount(): Int = 13

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        t7(statement, parameterIndex + 6, paramValue._7)
        t8(statement, parameterIndex + 7, paramValue._8)
        t9(statement, parameterIndex + 8, paramValue._9)
        t10(statement, parameterIndex + 9, paramValue._10)
        t11(statement, parameterIndex + 10, paramValue._11)
        t12(statement, parameterIndex + 11, paramValue._12)
        t13(statement, parameterIndex + 12, paramValue._13)
      }
    }

  implicit def tuple14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](implicit t1: SqlParam[T1],
                                                                                    t2: SqlParam[T2],
                                                                                    t3: SqlParam[T3],
                                                                                    t4: SqlParam[T4],
                                                                                    t5: SqlParam[T5],
                                                                                    t6: SqlParam[T6],
                                                                                    t7: SqlParam[T7],
                                                                                    t8: SqlParam[T8],
                                                                                    t9: SqlParam[T9],
                                                                                    t10: SqlParam[T10],
                                                                                    t11: SqlParam[T11],
                                                                                    t12: SqlParam[T12],
                                                                                    t13: SqlParam[T13],
                                                                                    t14: SqlParam[T14]): SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)] =
    new SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)] {
      override def parametersCount(): Int = 14

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        t7(statement, parameterIndex + 6, paramValue._7)
        t8(statement, parameterIndex + 7, paramValue._8)
        t9(statement, parameterIndex + 8, paramValue._9)
        t10(statement, parameterIndex + 9, paramValue._10)
        t11(statement, parameterIndex + 10, paramValue._11)
        t12(statement, parameterIndex + 11, paramValue._12)
        t13(statement, parameterIndex + 12, paramValue._13)
        t14(statement, parameterIndex + 13, paramValue._14)
      }
    }

  implicit def tuple15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](implicit t1: SqlParam[T1],
                                                                                         t2: SqlParam[T2],
                                                                                         t3: SqlParam[T3],
                                                                                         t4: SqlParam[T4],
                                                                                         t5: SqlParam[T5],
                                                                                         t6: SqlParam[T6],
                                                                                         t7: SqlParam[T7],
                                                                                         t8: SqlParam[T8],
                                                                                         t9: SqlParam[T9],
                                                                                         t10: SqlParam[T10],
                                                                                         t11: SqlParam[T11],
                                                                                         t12: SqlParam[T12],
                                                                                         t13: SqlParam[T13],
                                                                                         t14: SqlParam[T14],
                                                                                         t15: SqlParam[T15]): SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)] =
    new SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)] {
      override def parametersCount(): Int = 15

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        t7(statement, parameterIndex + 6, paramValue._7)
        t8(statement, parameterIndex + 7, paramValue._8)
        t9(statement, parameterIndex + 8, paramValue._9)
        t10(statement, parameterIndex + 9, paramValue._10)
        t11(statement, parameterIndex + 10, paramValue._11)
        t12(statement, parameterIndex + 11, paramValue._12)
        t13(statement, parameterIndex + 12, paramValue._13)
        t14(statement, parameterIndex + 13, paramValue._14)
        t15(statement, parameterIndex + 14, paramValue._15)
      }
    }

  implicit def tuple16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](implicit t1: SqlParam[T1],
                                                                                              t2: SqlParam[T2],
                                                                                              t3: SqlParam[T3],
                                                                                              t4: SqlParam[T4],
                                                                                              t5: SqlParam[T5],
                                                                                              t6: SqlParam[T6],
                                                                                              t7: SqlParam[T7],
                                                                                              t8: SqlParam[T8],
                                                                                              t9: SqlParam[T9],
                                                                                              t10: SqlParam[T10],
                                                                                              t11: SqlParam[T11],
                                                                                              t12: SqlParam[T12],
                                                                                              t13: SqlParam[T13],
                                                                                              t14: SqlParam[T14],
                                                                                              t15: SqlParam[T15],
                                                                                              t16: SqlParam[T16]): SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)] =
    new SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)] {
      override def parametersCount(): Int = 16

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        t7(statement, parameterIndex + 6, paramValue._7)
        t8(statement, parameterIndex + 7, paramValue._8)
        t9(statement, parameterIndex + 8, paramValue._9)
        t10(statement, parameterIndex + 9, paramValue._10)
        t11(statement, parameterIndex + 10, paramValue._11)
        t12(statement, parameterIndex + 11, paramValue._12)
        t13(statement, parameterIndex + 12, paramValue._13)
        t14(statement, parameterIndex + 13, paramValue._14)
        t15(statement, parameterIndex + 14, paramValue._15)
        t16(statement, parameterIndex + 15, paramValue._16)
      }
    }

  implicit def tuple17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](implicit t1: SqlParam[T1],
                                                                                                   t2: SqlParam[T2],
                                                                                                   t3: SqlParam[T3],
                                                                                                   t4: SqlParam[T4],
                                                                                                   t5: SqlParam[T5],
                                                                                                   t6: SqlParam[T6],
                                                                                                   t7: SqlParam[T7],
                                                                                                   t8: SqlParam[T8],
                                                                                                   t9: SqlParam[T9],
                                                                                                   t10: SqlParam[T10],
                                                                                                   t11: SqlParam[T11],
                                                                                                   t12: SqlParam[T12],
                                                                                                   t13: SqlParam[T13],
                                                                                                   t14: SqlParam[T14],
                                                                                                   t15: SqlParam[T15],
                                                                                                   t16: SqlParam[T16],
                                                                                                   t17: SqlParam[T17]): SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)] =
    new SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)] {
      override def parametersCount(): Int = 17

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        t7(statement, parameterIndex + 6, paramValue._7)
        t8(statement, parameterIndex + 7, paramValue._8)
        t9(statement, parameterIndex + 8, paramValue._9)
        t10(statement, parameterIndex + 9, paramValue._10)
        t11(statement, parameterIndex + 10, paramValue._11)
        t12(statement, parameterIndex + 11, paramValue._12)
        t13(statement, parameterIndex + 12, paramValue._13)
        t14(statement, parameterIndex + 13, paramValue._14)
        t15(statement, parameterIndex + 14, paramValue._15)
        t16(statement, parameterIndex + 15, paramValue._16)
        t17(statement, parameterIndex + 16, paramValue._17)
      }
    }

  implicit def tuple18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](implicit t1: SqlParam[T1],
                                                                                                        t2: SqlParam[T2],
                                                                                                        t3: SqlParam[T3],
                                                                                                        t4: SqlParam[T4],
                                                                                                        t5: SqlParam[T5],
                                                                                                        t6: SqlParam[T6],
                                                                                                        t7: SqlParam[T7],
                                                                                                        t8: SqlParam[T8],
                                                                                                        t9: SqlParam[T9],
                                                                                                        t10: SqlParam[T10],
                                                                                                        t11: SqlParam[T11],
                                                                                                        t12: SqlParam[T12],
                                                                                                        t13: SqlParam[T13],
                                                                                                        t14: SqlParam[T14],
                                                                                                        t15: SqlParam[T15],
                                                                                                        t16: SqlParam[T16],
                                                                                                        t17: SqlParam[T17],
                                                                                                        t18: SqlParam[T18]): SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)] =
    new SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)] {
      override def parametersCount(): Int = 18

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        t7(statement, parameterIndex + 6, paramValue._7)
        t8(statement, parameterIndex + 7, paramValue._8)
        t9(statement, parameterIndex + 8, paramValue._9)
        t10(statement, parameterIndex + 9, paramValue._10)
        t11(statement, parameterIndex + 10, paramValue._11)
        t12(statement, parameterIndex + 11, paramValue._12)
        t13(statement, parameterIndex + 12, paramValue._13)
        t14(statement, parameterIndex + 13, paramValue._14)
        t15(statement, parameterIndex + 14, paramValue._15)
        t16(statement, parameterIndex + 15, paramValue._16)
        t17(statement, parameterIndex + 16, paramValue._17)
        t18(statement, parameterIndex + 17, paramValue._18)
      }
    }

  implicit def tuple19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](implicit t1: SqlParam[T1],
                                                                                                             t2: SqlParam[T2],
                                                                                                             t3: SqlParam[T3],
                                                                                                             t4: SqlParam[T4],
                                                                                                             t5: SqlParam[T5],
                                                                                                             t6: SqlParam[T6],
                                                                                                             t7: SqlParam[T7],
                                                                                                             t8: SqlParam[T8],
                                                                                                             t9: SqlParam[T9],
                                                                                                             t10: SqlParam[T10],
                                                                                                             t11: SqlParam[T11],
                                                                                                             t12: SqlParam[T12],
                                                                                                             t13: SqlParam[T13],
                                                                                                             t14: SqlParam[T14],
                                                                                                             t15: SqlParam[T15],
                                                                                                             t16: SqlParam[T16],
                                                                                                             t17: SqlParam[T17],
                                                                                                             t18: SqlParam[T18],
                                                                                                             t19: SqlParam[T19]): SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)] =
    new SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)] {
      override def parametersCount(): Int = 19

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        t7(statement, parameterIndex + 6, paramValue._7)
        t8(statement, parameterIndex + 7, paramValue._8)
        t9(statement, parameterIndex + 8, paramValue._9)
        t10(statement, parameterIndex + 9, paramValue._10)
        t11(statement, parameterIndex + 10, paramValue._11)
        t12(statement, parameterIndex + 11, paramValue._12)
        t13(statement, parameterIndex + 12, paramValue._13)
        t14(statement, parameterIndex + 13, paramValue._14)
        t15(statement, parameterIndex + 14, paramValue._15)
        t16(statement, parameterIndex + 15, paramValue._16)
        t17(statement, parameterIndex + 16, paramValue._17)
        t18(statement, parameterIndex + 17, paramValue._18)
        t19(statement, parameterIndex + 18, paramValue._19)
      }
    }

  implicit def tuple20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](implicit t1: SqlParam[T1],
                                                                                                                  t2: SqlParam[T2],
                                                                                                                  t3: SqlParam[T3],
                                                                                                                  t4: SqlParam[T4],
                                                                                                                  t5: SqlParam[T5],
                                                                                                                  t6: SqlParam[T6],
                                                                                                                  t7: SqlParam[T7],
                                                                                                                  t8: SqlParam[T8],
                                                                                                                  t9: SqlParam[T9],
                                                                                                                  t10: SqlParam[T10],
                                                                                                                  t11: SqlParam[T11],
                                                                                                                  t12: SqlParam[T12],
                                                                                                                  t13: SqlParam[T13],
                                                                                                                  t14: SqlParam[T14],
                                                                                                                  t15: SqlParam[T15],
                                                                                                                  t16: SqlParam[T16],
                                                                                                                  t17: SqlParam[T17],
                                                                                                                  t18: SqlParam[T18],
                                                                                                                  t19: SqlParam[T19],
                                                                                                                  t20: SqlParam[T20]): SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)] =
    new SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)] {
      override def parametersCount(): Int = 20

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        t7(statement, parameterIndex + 6, paramValue._7)
        t8(statement, parameterIndex + 7, paramValue._8)
        t9(statement, parameterIndex + 8, paramValue._9)
        t10(statement, parameterIndex + 9, paramValue._10)
        t11(statement, parameterIndex + 10, paramValue._11)
        t12(statement, parameterIndex + 11, paramValue._12)
        t13(statement, parameterIndex + 12, paramValue._13)
        t14(statement, parameterIndex + 13, paramValue._14)
        t15(statement, parameterIndex + 14, paramValue._15)
        t16(statement, parameterIndex + 15, paramValue._16)
        t17(statement, parameterIndex + 16, paramValue._17)
        t18(statement, parameterIndex + 17, paramValue._18)
        t19(statement, parameterIndex + 18, paramValue._19)
        t20(statement, parameterIndex + 19, paramValue._20)
      }
    }

  implicit def tuple21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](implicit t1: SqlParam[T1],
                                                                                                                       t2: SqlParam[T2],
                                                                                                                       t3: SqlParam[T3],
                                                                                                                       t4: SqlParam[T4],
                                                                                                                       t5: SqlParam[T5],
                                                                                                                       t6: SqlParam[T6],
                                                                                                                       t7: SqlParam[T7],
                                                                                                                       t8: SqlParam[T8],
                                                                                                                       t9: SqlParam[T9],
                                                                                                                       t10: SqlParam[T10],
                                                                                                                       t11: SqlParam[T11],
                                                                                                                       t12: SqlParam[T12],
                                                                                                                       t13: SqlParam[T13],
                                                                                                                       t14: SqlParam[T14],
                                                                                                                       t15: SqlParam[T15],
                                                                                                                       t16: SqlParam[T16],
                                                                                                                       t17: SqlParam[T17],
                                                                                                                       t18: SqlParam[T18],
                                                                                                                       t19: SqlParam[T19],
                                                                                                                       t20: SqlParam[T20],
                                                                                                                       t21: SqlParam[T21]): SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)] =
    new SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)] {
      override def parametersCount(): Int = 21

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        t7(statement, parameterIndex + 6, paramValue._7)
        t8(statement, parameterIndex + 7, paramValue._8)
        t9(statement, parameterIndex + 8, paramValue._9)
        t10(statement, parameterIndex + 9, paramValue._10)
        t11(statement, parameterIndex + 10, paramValue._11)
        t12(statement, parameterIndex + 11, paramValue._12)
        t13(statement, parameterIndex + 12, paramValue._13)
        t14(statement, parameterIndex + 13, paramValue._14)
        t15(statement, parameterIndex + 14, paramValue._15)
        t16(statement, parameterIndex + 15, paramValue._16)
        t17(statement, parameterIndex + 16, paramValue._17)
        t18(statement, parameterIndex + 17, paramValue._18)
        t19(statement, parameterIndex + 18, paramValue._19)
        t20(statement, parameterIndex + 19, paramValue._20)
        t21(statement, parameterIndex + 20, paramValue._21)
      }
    }

  implicit def tuple22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](implicit t1: SqlParam[T1],
                                                                                                                            t2: SqlParam[T2],
                                                                                                                            t3: SqlParam[T3],
                                                                                                                            t4: SqlParam[T4],
                                                                                                                            t5: SqlParam[T5],
                                                                                                                            t6: SqlParam[T6],
                                                                                                                            t7: SqlParam[T7],
                                                                                                                            t8: SqlParam[T8],
                                                                                                                            t9: SqlParam[T9],
                                                                                                                            t10: SqlParam[T10],
                                                                                                                            t11: SqlParam[T11],
                                                                                                                            t12: SqlParam[T12],
                                                                                                                            t13: SqlParam[T13],
                                                                                                                            t14: SqlParam[T14],
                                                                                                                            t15: SqlParam[T15],
                                                                                                                            t16: SqlParam[T16],
                                                                                                                            t17: SqlParam[T17],
                                                                                                                            t18: SqlParam[T18],
                                                                                                                            t19: SqlParam[T19],
                                                                                                                            t20: SqlParam[T20],
                                                                                                                            t21: SqlParam[T21],
                                                                                                                            t22: SqlParam[T22]): SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)] =
    new SqlParam[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)] {
      override def parametersCount(): Int = 22

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        t7(statement, parameterIndex + 6, paramValue._7)
        t8(statement, parameterIndex + 7, paramValue._8)
        t9(statement, parameterIndex + 8, paramValue._9)
        t10(statement, parameterIndex + 9, paramValue._10)
        t11(statement, parameterIndex + 10, paramValue._11)
        t12(statement, parameterIndex + 11, paramValue._12)
        t13(statement, parameterIndex + 12, paramValue._13)
        t14(statement, parameterIndex + 13, paramValue._14)
        t15(statement, parameterIndex + 14, paramValue._15)
        t16(statement, parameterIndex + 15, paramValue._16)
        t17(statement, parameterIndex + 16, paramValue._17)
        t18(statement, parameterIndex + 17, paramValue._18)
        t19(statement, parameterIndex + 18, paramValue._19)
        t20(statement, parameterIndex + 19, paramValue._20)
        t21(statement, parameterIndex + 20, paramValue._21)
        t22(statement, parameterIndex + 21, paramValue._22)
      }
    }
}
