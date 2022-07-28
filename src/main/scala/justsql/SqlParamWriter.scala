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

trait SqlParamWriter[P] extends ((PreparedStatement, Int, P) => Unit) {
  /** Total number of parameters this [[SqlParamWriter]] writes to a [[PreparedStatement]] */
  def parametersCount(): Int

  override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: P): Unit
}

trait OneSqlParamWriter[P] extends SqlParamWriter[P] {

  final val parametersCount: Int = 1

}

object SqlParamWriter {
  implicit val intColWriter: OneSqlParamWriter[Int] = _.setInt(_, _)
  implicit val stringColWriter: OneSqlParamWriter[String] = _.setString(_, _)
  implicit val booleanColWriter: OneSqlParamWriter[Boolean] = _.setBoolean(_, _)
  implicit val longColWriter: OneSqlParamWriter[Long] = _.setLong(_, _)
  implicit val byteColWriter: OneSqlParamWriter[Byte] = _.setByte(_, _)
  implicit val doubleColWriter: OneSqlParamWriter[Double] = _.setDouble(_, _)
  implicit val floatColWriter: OneSqlParamWriter[Float] = _.setFloat(_, _)
  implicit val shortColWriter: OneSqlParamWriter[Short] = _.setShort(_, _)
  implicit val bigDecimalColWriter: OneSqlParamWriter[java.math.BigDecimal] = _.setBigDecimal(_, _)
  implicit val byteArrayColWriter: OneSqlParamWriter[Array[Byte]] = _.setBytes(_, _)

  //java.sql types
  implicit val timestampColWriter: OneSqlParamWriter[java.sql.Timestamp] = _.setTimestamp(_, _)
  implicit val blobColWriter: OneSqlParamWriter[java.sql.Blob] = _.setBlob(_, _)
  implicit val clobColWriter: OneSqlParamWriter[java.sql.Clob] = _.setClob(_, _)
  implicit val dateColWriter: OneSqlParamWriter[java.sql.Date] = _.setDate(_, _)
  implicit val timeColWriter: OneSqlParamWriter[java.sql.Time] = _.setTime(_, _)
  implicit val refColWriter: OneSqlParamWriter[java.sql.Ref] = _.setRef(_, _)
  implicit val nClobColWriter: OneSqlParamWriter[java.sql.NClob] = _.setNClob(_, _)
  implicit val rowIdColWriter: OneSqlParamWriter[java.sql.RowId] = _.setRowId(_, _)
  implicit val sqlXMLColWriter: OneSqlParamWriter[java.sql.SQLXML] = _.setSQLXML(_, _)

  //other types
  implicit val characterStreamColWriter: OneSqlParamWriter[java.io.Reader] = _.setCharacterStream(_, _)
  implicit val binaryStreamColWriter: OneSqlParamWriter[java.io.InputStream] = _.setBinaryStream(_, _)
  implicit val urlColWriter: OneSqlParamWriter[java.net.URL] = _.setURL(_, _)

  implicit def tuple2[T1, T2](implicit t1: SqlParamWriter[T1],
                              t2: SqlParamWriter[T2]): SqlParamWriter[(T1, T2)] =
    new SqlParamWriter[(T1, T2)] {
      override def parametersCount(): Int = 2

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
      }
    }

  implicit def tuple3[T1, T2, T3](implicit t1: SqlParamWriter[T1],
                                  t2: SqlParamWriter[T2],
                                  t3: SqlParamWriter[T3]): SqlParamWriter[(T1, T2, T3)] =
    new SqlParamWriter[(T1, T2, T3)] {
      override def parametersCount(): Int = 3

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
      }
    }

  implicit def tuple4[T1, T2, T3, T4](implicit t1: SqlParamWriter[T1],
                                      t2: SqlParamWriter[T2],
                                      t3: SqlParamWriter[T3],
                                      t4: SqlParamWriter[T4]): SqlParamWriter[(T1, T2, T3, T4)] =
    new SqlParamWriter[(T1, T2, T3, T4)] {
      override def parametersCount(): Int = 4

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
      }
    }

  implicit def tuple5[T1, T2, T3, T4, T5](implicit t1: SqlParamWriter[T1],
                                          t2: SqlParamWriter[T2],
                                          t3: SqlParamWriter[T3],
                                          t4: SqlParamWriter[T4],
                                          t5: SqlParamWriter[T5]): SqlParamWriter[(T1, T2, T3, T4, T5)] =
    new SqlParamWriter[(T1, T2, T3, T4, T5)] {
      override def parametersCount(): Int = 5

      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5)): Unit = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
      }
    }

  implicit def tuple6[T1, T2, T3, T4, T5, T6](implicit t1: SqlParamWriter[T1],
                                              t2: SqlParamWriter[T2],
                                              t3: SqlParamWriter[T3],
                                              t4: SqlParamWriter[T4],
                                              t5: SqlParamWriter[T5],
                                              t6: SqlParamWriter[T6]): SqlParamWriter[(T1, T2, T3, T4, T5, T6)] =
    new SqlParamWriter[(T1, T2, T3, T4, T5, T6)] {
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

  implicit def tuple7[T1, T2, T3, T4, T5, T6, T7](implicit t1: SqlParamWriter[T1],
                                                  t2: SqlParamWriter[T2],
                                                  t3: SqlParamWriter[T3],
                                                  t4: SqlParamWriter[T4],
                                                  t5: SqlParamWriter[T5],
                                                  t6: SqlParamWriter[T6],
                                                  t7: SqlParamWriter[T7]): SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7)] =
    new SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7)] {
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

  implicit def tuple8[T1, T2, T3, T4, T5, T6, T7, T8](implicit t1: SqlParamWriter[T1],
                                                      t2: SqlParamWriter[T2],
                                                      t3: SqlParamWriter[T3],
                                                      t4: SqlParamWriter[T4],
                                                      t5: SqlParamWriter[T5],
                                                      t6: SqlParamWriter[T6],
                                                      t7: SqlParamWriter[T7],
                                                      t8: SqlParamWriter[T8]): SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8)] =
    new SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8)] {
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

  implicit def tuple9[T1, T2, T3, T4, T5, T6, T7, T8, T9](implicit t1: SqlParamWriter[T1],
                                                          t2: SqlParamWriter[T2],
                                                          t3: SqlParamWriter[T3],
                                                          t4: SqlParamWriter[T4],
                                                          t5: SqlParamWriter[T5],
                                                          t6: SqlParamWriter[T6],
                                                          t7: SqlParamWriter[T7],
                                                          t8: SqlParamWriter[T8],
                                                          t9: SqlParamWriter[T9]): SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9)] =
    new SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9)] {
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

  implicit def tuple10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](implicit t1: SqlParamWriter[T1],
                                                                t2: SqlParamWriter[T2],
                                                                t3: SqlParamWriter[T3],
                                                                t4: SqlParamWriter[T4],
                                                                t5: SqlParamWriter[T5],
                                                                t6: SqlParamWriter[T6],
                                                                t7: SqlParamWriter[T7],
                                                                t8: SqlParamWriter[T8],
                                                                t9: SqlParamWriter[T9],
                                                                t10: SqlParamWriter[T10]): SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)] =
    new SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)] {
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

  implicit def tuple11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](implicit t1: SqlParamWriter[T1],
                                                                     t2: SqlParamWriter[T2],
                                                                     t3: SqlParamWriter[T3],
                                                                     t4: SqlParamWriter[T4],
                                                                     t5: SqlParamWriter[T5],
                                                                     t6: SqlParamWriter[T6],
                                                                     t7: SqlParamWriter[T7],
                                                                     t8: SqlParamWriter[T8],
                                                                     t9: SqlParamWriter[T9],
                                                                     t10: SqlParamWriter[T10],
                                                                     t11: SqlParamWriter[T11]): SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)] =
    new SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)] {
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

  implicit def tuple12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](implicit t1: SqlParamWriter[T1],
                                                                          t2: SqlParamWriter[T2],
                                                                          t3: SqlParamWriter[T3],
                                                                          t4: SqlParamWriter[T4],
                                                                          t5: SqlParamWriter[T5],
                                                                          t6: SqlParamWriter[T6],
                                                                          t7: SqlParamWriter[T7],
                                                                          t8: SqlParamWriter[T8],
                                                                          t9: SqlParamWriter[T9],
                                                                          t10: SqlParamWriter[T10],
                                                                          t11: SqlParamWriter[T11],
                                                                          t12: SqlParamWriter[T12]): SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)] =
    new SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)] {
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

  implicit def tuple13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](implicit t1: SqlParamWriter[T1],
                                                                               t2: SqlParamWriter[T2],
                                                                               t3: SqlParamWriter[T3],
                                                                               t4: SqlParamWriter[T4],
                                                                               t5: SqlParamWriter[T5],
                                                                               t6: SqlParamWriter[T6],
                                                                               t7: SqlParamWriter[T7],
                                                                               t8: SqlParamWriter[T8],
                                                                               t9: SqlParamWriter[T9],
                                                                               t10: SqlParamWriter[T10],
                                                                               t11: SqlParamWriter[T11],
                                                                               t12: SqlParamWriter[T12],
                                                                               t13: SqlParamWriter[T13]): SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)] =
    new SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)] {
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

  implicit def tuple14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](implicit t1: SqlParamWriter[T1],
                                                                                    t2: SqlParamWriter[T2],
                                                                                    t3: SqlParamWriter[T3],
                                                                                    t4: SqlParamWriter[T4],
                                                                                    t5: SqlParamWriter[T5],
                                                                                    t6: SqlParamWriter[T6],
                                                                                    t7: SqlParamWriter[T7],
                                                                                    t8: SqlParamWriter[T8],
                                                                                    t9: SqlParamWriter[T9],
                                                                                    t10: SqlParamWriter[T10],
                                                                                    t11: SqlParamWriter[T11],
                                                                                    t12: SqlParamWriter[T12],
                                                                                    t13: SqlParamWriter[T13],
                                                                                    t14: SqlParamWriter[T14]): SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)] =
    new SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)] {
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

  implicit def tuple15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](implicit t1: SqlParamWriter[T1],
                                                                                         t2: SqlParamWriter[T2],
                                                                                         t3: SqlParamWriter[T3],
                                                                                         t4: SqlParamWriter[T4],
                                                                                         t5: SqlParamWriter[T5],
                                                                                         t6: SqlParamWriter[T6],
                                                                                         t7: SqlParamWriter[T7],
                                                                                         t8: SqlParamWriter[T8],
                                                                                         t9: SqlParamWriter[T9],
                                                                                         t10: SqlParamWriter[T10],
                                                                                         t11: SqlParamWriter[T11],
                                                                                         t12: SqlParamWriter[T12],
                                                                                         t13: SqlParamWriter[T13],
                                                                                         t14: SqlParamWriter[T14],
                                                                                         t15: SqlParamWriter[T15]): SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)] =
    new SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)] {
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

  implicit def tuple16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](implicit t1: SqlParamWriter[T1],
                                                                                              t2: SqlParamWriter[T2],
                                                                                              t3: SqlParamWriter[T3],
                                                                                              t4: SqlParamWriter[T4],
                                                                                              t5: SqlParamWriter[T5],
                                                                                              t6: SqlParamWriter[T6],
                                                                                              t7: SqlParamWriter[T7],
                                                                                              t8: SqlParamWriter[T8],
                                                                                              t9: SqlParamWriter[T9],
                                                                                              t10: SqlParamWriter[T10],
                                                                                              t11: SqlParamWriter[T11],
                                                                                              t12: SqlParamWriter[T12],
                                                                                              t13: SqlParamWriter[T13],
                                                                                              t14: SqlParamWriter[T14],
                                                                                              t15: SqlParamWriter[T15],
                                                                                              t16: SqlParamWriter[T16]): SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)] =
    new SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)] {
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

  implicit def tuple17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](implicit t1: SqlParamWriter[T1],
                                                                                                   t2: SqlParamWriter[T2],
                                                                                                   t3: SqlParamWriter[T3],
                                                                                                   t4: SqlParamWriter[T4],
                                                                                                   t5: SqlParamWriter[T5],
                                                                                                   t6: SqlParamWriter[T6],
                                                                                                   t7: SqlParamWriter[T7],
                                                                                                   t8: SqlParamWriter[T8],
                                                                                                   t9: SqlParamWriter[T9],
                                                                                                   t10: SqlParamWriter[T10],
                                                                                                   t11: SqlParamWriter[T11],
                                                                                                   t12: SqlParamWriter[T12],
                                                                                                   t13: SqlParamWriter[T13],
                                                                                                   t14: SqlParamWriter[T14],
                                                                                                   t15: SqlParamWriter[T15],
                                                                                                   t16: SqlParamWriter[T16],
                                                                                                   t17: SqlParamWriter[T17]): SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)] =
    new SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)] {
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

  implicit def tuple18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](implicit t1: SqlParamWriter[T1],
                                                                                                        t2: SqlParamWriter[T2],
                                                                                                        t3: SqlParamWriter[T3],
                                                                                                        t4: SqlParamWriter[T4],
                                                                                                        t5: SqlParamWriter[T5],
                                                                                                        t6: SqlParamWriter[T6],
                                                                                                        t7: SqlParamWriter[T7],
                                                                                                        t8: SqlParamWriter[T8],
                                                                                                        t9: SqlParamWriter[T9],
                                                                                                        t10: SqlParamWriter[T10],
                                                                                                        t11: SqlParamWriter[T11],
                                                                                                        t12: SqlParamWriter[T12],
                                                                                                        t13: SqlParamWriter[T13],
                                                                                                        t14: SqlParamWriter[T14],
                                                                                                        t15: SqlParamWriter[T15],
                                                                                                        t16: SqlParamWriter[T16],
                                                                                                        t17: SqlParamWriter[T17],
                                                                                                        t18: SqlParamWriter[T18]): SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)] =
    new SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)] {
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

  implicit def tuple19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](implicit t1: SqlParamWriter[T1],
                                                                                                             t2: SqlParamWriter[T2],
                                                                                                             t3: SqlParamWriter[T3],
                                                                                                             t4: SqlParamWriter[T4],
                                                                                                             t5: SqlParamWriter[T5],
                                                                                                             t6: SqlParamWriter[T6],
                                                                                                             t7: SqlParamWriter[T7],
                                                                                                             t8: SqlParamWriter[T8],
                                                                                                             t9: SqlParamWriter[T9],
                                                                                                             t10: SqlParamWriter[T10],
                                                                                                             t11: SqlParamWriter[T11],
                                                                                                             t12: SqlParamWriter[T12],
                                                                                                             t13: SqlParamWriter[T13],
                                                                                                             t14: SqlParamWriter[T14],
                                                                                                             t15: SqlParamWriter[T15],
                                                                                                             t16: SqlParamWriter[T16],
                                                                                                             t17: SqlParamWriter[T17],
                                                                                                             t18: SqlParamWriter[T18],
                                                                                                             t19: SqlParamWriter[T19]): SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)] =
    new SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)] {
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

  implicit def tuple20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](implicit t1: SqlParamWriter[T1],
                                                                                                                  t2: SqlParamWriter[T2],
                                                                                                                  t3: SqlParamWriter[T3],
                                                                                                                  t4: SqlParamWriter[T4],
                                                                                                                  t5: SqlParamWriter[T5],
                                                                                                                  t6: SqlParamWriter[T6],
                                                                                                                  t7: SqlParamWriter[T7],
                                                                                                                  t8: SqlParamWriter[T8],
                                                                                                                  t9: SqlParamWriter[T9],
                                                                                                                  t10: SqlParamWriter[T10],
                                                                                                                  t11: SqlParamWriter[T11],
                                                                                                                  t12: SqlParamWriter[T12],
                                                                                                                  t13: SqlParamWriter[T13],
                                                                                                                  t14: SqlParamWriter[T14],
                                                                                                                  t15: SqlParamWriter[T15],
                                                                                                                  t16: SqlParamWriter[T16],
                                                                                                                  t17: SqlParamWriter[T17],
                                                                                                                  t18: SqlParamWriter[T18],
                                                                                                                  t19: SqlParamWriter[T19],
                                                                                                                  t20: SqlParamWriter[T20]): SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)] =
    new SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)] {
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

  implicit def tuple21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](implicit t1: SqlParamWriter[T1],
                                                                                                                       t2: SqlParamWriter[T2],
                                                                                                                       t3: SqlParamWriter[T3],
                                                                                                                       t4: SqlParamWriter[T4],
                                                                                                                       t5: SqlParamWriter[T5],
                                                                                                                       t6: SqlParamWriter[T6],
                                                                                                                       t7: SqlParamWriter[T7],
                                                                                                                       t8: SqlParamWriter[T8],
                                                                                                                       t9: SqlParamWriter[T9],
                                                                                                                       t10: SqlParamWriter[T10],
                                                                                                                       t11: SqlParamWriter[T11],
                                                                                                                       t12: SqlParamWriter[T12],
                                                                                                                       t13: SqlParamWriter[T13],
                                                                                                                       t14: SqlParamWriter[T14],
                                                                                                                       t15: SqlParamWriter[T15],
                                                                                                                       t16: SqlParamWriter[T16],
                                                                                                                       t17: SqlParamWriter[T17],
                                                                                                                       t18: SqlParamWriter[T18],
                                                                                                                       t19: SqlParamWriter[T19],
                                                                                                                       t20: SqlParamWriter[T20],
                                                                                                                       t21: SqlParamWriter[T21]): SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)] =
    new SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)] {
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

  implicit def tuple22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](implicit t1: SqlParamWriter[T1],
                                                                                                                            t2: SqlParamWriter[T2],
                                                                                                                            t3: SqlParamWriter[T3],
                                                                                                                            t4: SqlParamWriter[T4],
                                                                                                                            t5: SqlParamWriter[T5],
                                                                                                                            t6: SqlParamWriter[T6],
                                                                                                                            t7: SqlParamWriter[T7],
                                                                                                                            t8: SqlParamWriter[T8],
                                                                                                                            t9: SqlParamWriter[T9],
                                                                                                                            t10: SqlParamWriter[T10],
                                                                                                                            t11: SqlParamWriter[T11],
                                                                                                                            t12: SqlParamWriter[T12],
                                                                                                                            t13: SqlParamWriter[T13],
                                                                                                                            t14: SqlParamWriter[T14],
                                                                                                                            t15: SqlParamWriter[T15],
                                                                                                                            t16: SqlParamWriter[T16],
                                                                                                                            t17: SqlParamWriter[T17],
                                                                                                                            t18: SqlParamWriter[T18],
                                                                                                                            t19: SqlParamWriter[T19],
                                                                                                                            t20: SqlParamWriter[T20],
                                                                                                                            t21: SqlParamWriter[T21],
                                                                                                                            t22: SqlParamWriter[T22]): SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)] =
    new SqlParamWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)] {
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
