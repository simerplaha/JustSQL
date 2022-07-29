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

trait Param[P] extends ((PreparedStatement, Int, P) => Unit) {
  /** Total number of parameters this [[Param]] writes to a [[PreparedStatement]] */
  def parametersCount(): Int

  override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: P): Unit
}

trait OneParam[P] extends Param[P] {

  final val parametersCount: Int = 1

}

object Param {
  implicit val intParam: OneParam[Int] = _.setInt(_, _)
  implicit val stringParam: OneParam[String] = _.setString(_, _)
  implicit val booleanParam: OneParam[Boolean] = _.setBoolean(_, _)
  implicit val longParam: OneParam[Long] = _.setLong(_, _)
  implicit val byteParam: OneParam[Byte] = _.setByte(_, _)
  implicit val doubleParam: OneParam[Double] = _.setDouble(_, _)
  implicit val floatParam: OneParam[Float] = _.setFloat(_, _)
  implicit val shortParam: OneParam[Short] = _.setShort(_, _)
  implicit val bigDecimalParam: OneParam[java.math.BigDecimal] = _.setBigDecimal(_, _)
  implicit val byteArrayParam: OneParam[Array[Byte]] = _.setBytes(_, _)

  //java.sql types
  implicit val timestampParam: OneParam[java.sql.Timestamp] = _.setTimestamp(_, _)
  implicit val blobParam: OneParam[java.sql.Blob] = _.setBlob(_, _)
  implicit val clobParam: OneParam[java.sql.Clob] = _.setClob(_, _)
  implicit val dateParam: OneParam[java.sql.Date] = _.setDate(_, _)
  implicit val timeParam: OneParam[java.sql.Time] = _.setTime(_, _)
  implicit val refParam: OneParam[java.sql.Ref] = _.setRef(_, _)
  implicit val nClobParam: OneParam[java.sql.NClob] = _.setNClob(_, _)
  implicit val rowIdParam: OneParam[java.sql.RowId] = _.setRowId(_, _)
  implicit val sqlXMLParam: OneParam[java.sql.SQLXML] = _.setSQLXML(_, _)

  //other types
  implicit val characterStreamParam: OneParam[java.io.Reader] = _.setCharacterStream(_, _)
  implicit val binaryStreamParam: OneParam[java.io.InputStream] = _.setBinaryStream(_, _)
  implicit val urlParam: OneParam[java.net.URL] = _.setURL(_, _)

  implicit def tuple2[T1, T2](implicit t1: Param[T1],
                              t2: Param[T2]): Param[(T1, T2)] =
    new Param[(T1, T2)] {
      override def parametersCount(): Int = 2

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
      }
    }

  implicit def tuple3[T1, T2, T3](implicit t1: Param[T1],
                                  t2: Param[T2],
                                  t3: Param[T3]): Param[(T1, T2, T3)] =
    new Param[(T1, T2, T3)] {
      override def parametersCount(): Int = 3

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
      }
    }

  implicit def tuple4[T1, T2, T3, T4](implicit t1: Param[T1],
                                      t2: Param[T2],
                                      t3: Param[T3],
                                      t4: Param[T4]): Param[(T1, T2, T3, T4)] =
    new Param[(T1, T2, T3, T4)] {
      override def parametersCount(): Int = 4

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
      }
    }

  implicit def tuple5[T1, T2, T3, T4, T5](implicit t1: Param[T1],
                                          t2: Param[T2],
                                          t3: Param[T3],
                                          t4: Param[T4],
                                          t5: Param[T5]): Param[(T1, T2, T3, T4, T5)] =
    new Param[(T1, T2, T3, T4, T5)] {
      override def parametersCount(): Int = 5

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4, T5)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
        t5(statement, paramIndex + 4, paramValue._5)
      }
    }

  implicit def tuple6[T1, T2, T3, T4, T5, T6](implicit t1: Param[T1],
                                              t2: Param[T2],
                                              t3: Param[T3],
                                              t4: Param[T4],
                                              t5: Param[T5],
                                              t6: Param[T6]): Param[(T1, T2, T3, T4, T5, T6)] =
    new Param[(T1, T2, T3, T4, T5, T6)] {
      override def parametersCount(): Int = 6

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
        t5(statement, paramIndex + 4, paramValue._5)
        t6(statement, paramIndex + 5, paramValue._6)
      }
    }

  implicit def tuple7[T1, T2, T3, T4, T5, T6, T7](implicit t1: Param[T1],
                                                  t2: Param[T2],
                                                  t3: Param[T3],
                                                  t4: Param[T4],
                                                  t5: Param[T5],
                                                  t6: Param[T6],
                                                  t7: Param[T7]): Param[(T1, T2, T3, T4, T5, T6, T7)] =
    new Param[(T1, T2, T3, T4, T5, T6, T7)] {
      override def parametersCount(): Int = 7

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
        t5(statement, paramIndex + 4, paramValue._5)
        t6(statement, paramIndex + 5, paramValue._6)
        t7(statement, paramIndex + 6, paramValue._7)
      }
    }

  implicit def tuple8[T1, T2, T3, T4, T5, T6, T7, T8](implicit t1: Param[T1],
                                                      t2: Param[T2],
                                                      t3: Param[T3],
                                                      t4: Param[T4],
                                                      t5: Param[T5],
                                                      t6: Param[T6],
                                                      t7: Param[T7],
                                                      t8: Param[T8]): Param[(T1, T2, T3, T4, T5, T6, T7, T8)] =
    new Param[(T1, T2, T3, T4, T5, T6, T7, T8)] {
      override def parametersCount(): Int = 8

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
        t5(statement, paramIndex + 4, paramValue._5)
        t6(statement, paramIndex + 5, paramValue._6)
        t7(statement, paramIndex + 6, paramValue._7)
        t8(statement, paramIndex + 7, paramValue._8)
      }
    }

  implicit def tuple9[T1, T2, T3, T4, T5, T6, T7, T8, T9](implicit t1: Param[T1],
                                                          t2: Param[T2],
                                                          t3: Param[T3],
                                                          t4: Param[T4],
                                                          t5: Param[T5],
                                                          t6: Param[T6],
                                                          t7: Param[T7],
                                                          t8: Param[T8],
                                                          t9: Param[T9]): Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9)] =
    new Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9)] {
      override def parametersCount(): Int = 9

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
        t5(statement, paramIndex + 4, paramValue._5)
        t6(statement, paramIndex + 5, paramValue._6)
        t7(statement, paramIndex + 6, paramValue._7)
        t8(statement, paramIndex + 7, paramValue._8)
        t9(statement, paramIndex + 8, paramValue._9)
      }
    }

  implicit def tuple10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](implicit t1: Param[T1],
                                                                t2: Param[T2],
                                                                t3: Param[T3],
                                                                t4: Param[T4],
                                                                t5: Param[T5],
                                                                t6: Param[T6],
                                                                t7: Param[T7],
                                                                t8: Param[T8],
                                                                t9: Param[T9],
                                                                t10: Param[T10]): Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)] =
    new Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)] {
      override def parametersCount(): Int = 10

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
        t5(statement, paramIndex + 4, paramValue._5)
        t6(statement, paramIndex + 5, paramValue._6)
        t7(statement, paramIndex + 6, paramValue._7)
        t8(statement, paramIndex + 7, paramValue._8)
        t9(statement, paramIndex + 8, paramValue._9)
        t10(statement, paramIndex + 9, paramValue._10)
      }
    }

  implicit def tuple11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](implicit t1: Param[T1],
                                                                     t2: Param[T2],
                                                                     t3: Param[T3],
                                                                     t4: Param[T4],
                                                                     t5: Param[T5],
                                                                     t6: Param[T6],
                                                                     t7: Param[T7],
                                                                     t8: Param[T8],
                                                                     t9: Param[T9],
                                                                     t10: Param[T10],
                                                                     t11: Param[T11]): Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)] =
    new Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)] {
      override def parametersCount(): Int = 11

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
        t5(statement, paramIndex + 4, paramValue._5)
        t6(statement, paramIndex + 5, paramValue._6)
        t7(statement, paramIndex + 6, paramValue._7)
        t8(statement, paramIndex + 7, paramValue._8)
        t9(statement, paramIndex + 8, paramValue._9)
        t10(statement, paramIndex + 9, paramValue._10)
        t11(statement, paramIndex + 10, paramValue._11)
      }
    }

  implicit def tuple12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](implicit t1: Param[T1],
                                                                          t2: Param[T2],
                                                                          t3: Param[T3],
                                                                          t4: Param[T4],
                                                                          t5: Param[T5],
                                                                          t6: Param[T6],
                                                                          t7: Param[T7],
                                                                          t8: Param[T8],
                                                                          t9: Param[T9],
                                                                          t10: Param[T10],
                                                                          t11: Param[T11],
                                                                          t12: Param[T12]): Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)] =
    new Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)] {
      override def parametersCount(): Int = 12

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
        t5(statement, paramIndex + 4, paramValue._5)
        t6(statement, paramIndex + 5, paramValue._6)
        t7(statement, paramIndex + 6, paramValue._7)
        t8(statement, paramIndex + 7, paramValue._8)
        t9(statement, paramIndex + 8, paramValue._9)
        t10(statement, paramIndex + 9, paramValue._10)
        t11(statement, paramIndex + 10, paramValue._11)
        t12(statement, paramIndex + 11, paramValue._12)
      }
    }

  implicit def tuple13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](implicit t1: Param[T1],
                                                                               t2: Param[T2],
                                                                               t3: Param[T3],
                                                                               t4: Param[T4],
                                                                               t5: Param[T5],
                                                                               t6: Param[T6],
                                                                               t7: Param[T7],
                                                                               t8: Param[T8],
                                                                               t9: Param[T9],
                                                                               t10: Param[T10],
                                                                               t11: Param[T11],
                                                                               t12: Param[T12],
                                                                               t13: Param[T13]): Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)] =
    new Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)] {
      override def parametersCount(): Int = 13

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
        t5(statement, paramIndex + 4, paramValue._5)
        t6(statement, paramIndex + 5, paramValue._6)
        t7(statement, paramIndex + 6, paramValue._7)
        t8(statement, paramIndex + 7, paramValue._8)
        t9(statement, paramIndex + 8, paramValue._9)
        t10(statement, paramIndex + 9, paramValue._10)
        t11(statement, paramIndex + 10, paramValue._11)
        t12(statement, paramIndex + 11, paramValue._12)
        t13(statement, paramIndex + 12, paramValue._13)
      }
    }

  implicit def tuple14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](implicit t1: Param[T1],
                                                                                    t2: Param[T2],
                                                                                    t3: Param[T3],
                                                                                    t4: Param[T4],
                                                                                    t5: Param[T5],
                                                                                    t6: Param[T6],
                                                                                    t7: Param[T7],
                                                                                    t8: Param[T8],
                                                                                    t9: Param[T9],
                                                                                    t10: Param[T10],
                                                                                    t11: Param[T11],
                                                                                    t12: Param[T12],
                                                                                    t13: Param[T13],
                                                                                    t14: Param[T14]): Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)] =
    new Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)] {
      override def parametersCount(): Int = 14

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
        t5(statement, paramIndex + 4, paramValue._5)
        t6(statement, paramIndex + 5, paramValue._6)
        t7(statement, paramIndex + 6, paramValue._7)
        t8(statement, paramIndex + 7, paramValue._8)
        t9(statement, paramIndex + 8, paramValue._9)
        t10(statement, paramIndex + 9, paramValue._10)
        t11(statement, paramIndex + 10, paramValue._11)
        t12(statement, paramIndex + 11, paramValue._12)
        t13(statement, paramIndex + 12, paramValue._13)
        t14(statement, paramIndex + 13, paramValue._14)
      }
    }

  implicit def tuple15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](implicit t1: Param[T1],
                                                                                         t2: Param[T2],
                                                                                         t3: Param[T3],
                                                                                         t4: Param[T4],
                                                                                         t5: Param[T5],
                                                                                         t6: Param[T6],
                                                                                         t7: Param[T7],
                                                                                         t8: Param[T8],
                                                                                         t9: Param[T9],
                                                                                         t10: Param[T10],
                                                                                         t11: Param[T11],
                                                                                         t12: Param[T12],
                                                                                         t13: Param[T13],
                                                                                         t14: Param[T14],
                                                                                         t15: Param[T15]): Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)] =
    new Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)] {
      override def parametersCount(): Int = 15

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
        t5(statement, paramIndex + 4, paramValue._5)
        t6(statement, paramIndex + 5, paramValue._6)
        t7(statement, paramIndex + 6, paramValue._7)
        t8(statement, paramIndex + 7, paramValue._8)
        t9(statement, paramIndex + 8, paramValue._9)
        t10(statement, paramIndex + 9, paramValue._10)
        t11(statement, paramIndex + 10, paramValue._11)
        t12(statement, paramIndex + 11, paramValue._12)
        t13(statement, paramIndex + 12, paramValue._13)
        t14(statement, paramIndex + 13, paramValue._14)
        t15(statement, paramIndex + 14, paramValue._15)
      }
    }

  implicit def tuple16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](implicit t1: Param[T1],
                                                                                              t2: Param[T2],
                                                                                              t3: Param[T3],
                                                                                              t4: Param[T4],
                                                                                              t5: Param[T5],
                                                                                              t6: Param[T6],
                                                                                              t7: Param[T7],
                                                                                              t8: Param[T8],
                                                                                              t9: Param[T9],
                                                                                              t10: Param[T10],
                                                                                              t11: Param[T11],
                                                                                              t12: Param[T12],
                                                                                              t13: Param[T13],
                                                                                              t14: Param[T14],
                                                                                              t15: Param[T15],
                                                                                              t16: Param[T16]): Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)] =
    new Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)] {
      override def parametersCount(): Int = 16

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
        t5(statement, paramIndex + 4, paramValue._5)
        t6(statement, paramIndex + 5, paramValue._6)
        t7(statement, paramIndex + 6, paramValue._7)
        t8(statement, paramIndex + 7, paramValue._8)
        t9(statement, paramIndex + 8, paramValue._9)
        t10(statement, paramIndex + 9, paramValue._10)
        t11(statement, paramIndex + 10, paramValue._11)
        t12(statement, paramIndex + 11, paramValue._12)
        t13(statement, paramIndex + 12, paramValue._13)
        t14(statement, paramIndex + 13, paramValue._14)
        t15(statement, paramIndex + 14, paramValue._15)
        t16(statement, paramIndex + 15, paramValue._16)
      }
    }

  implicit def tuple17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](implicit t1: Param[T1],
                                                                                                   t2: Param[T2],
                                                                                                   t3: Param[T3],
                                                                                                   t4: Param[T4],
                                                                                                   t5: Param[T5],
                                                                                                   t6: Param[T6],
                                                                                                   t7: Param[T7],
                                                                                                   t8: Param[T8],
                                                                                                   t9: Param[T9],
                                                                                                   t10: Param[T10],
                                                                                                   t11: Param[T11],
                                                                                                   t12: Param[T12],
                                                                                                   t13: Param[T13],
                                                                                                   t14: Param[T14],
                                                                                                   t15: Param[T15],
                                                                                                   t16: Param[T16],
                                                                                                   t17: Param[T17]): Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)] =
    new Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)] {
      override def parametersCount(): Int = 17

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
        t5(statement, paramIndex + 4, paramValue._5)
        t6(statement, paramIndex + 5, paramValue._6)
        t7(statement, paramIndex + 6, paramValue._7)
        t8(statement, paramIndex + 7, paramValue._8)
        t9(statement, paramIndex + 8, paramValue._9)
        t10(statement, paramIndex + 9, paramValue._10)
        t11(statement, paramIndex + 10, paramValue._11)
        t12(statement, paramIndex + 11, paramValue._12)
        t13(statement, paramIndex + 12, paramValue._13)
        t14(statement, paramIndex + 13, paramValue._14)
        t15(statement, paramIndex + 14, paramValue._15)
        t16(statement, paramIndex + 15, paramValue._16)
        t17(statement, paramIndex + 16, paramValue._17)
      }
    }

  implicit def tuple18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](implicit t1: Param[T1],
                                                                                                        t2: Param[T2],
                                                                                                        t3: Param[T3],
                                                                                                        t4: Param[T4],
                                                                                                        t5: Param[T5],
                                                                                                        t6: Param[T6],
                                                                                                        t7: Param[T7],
                                                                                                        t8: Param[T8],
                                                                                                        t9: Param[T9],
                                                                                                        t10: Param[T10],
                                                                                                        t11: Param[T11],
                                                                                                        t12: Param[T12],
                                                                                                        t13: Param[T13],
                                                                                                        t14: Param[T14],
                                                                                                        t15: Param[T15],
                                                                                                        t16: Param[T16],
                                                                                                        t17: Param[T17],
                                                                                                        t18: Param[T18]): Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)] =
    new Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)] {
      override def parametersCount(): Int = 18

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
        t5(statement, paramIndex + 4, paramValue._5)
        t6(statement, paramIndex + 5, paramValue._6)
        t7(statement, paramIndex + 6, paramValue._7)
        t8(statement, paramIndex + 7, paramValue._8)
        t9(statement, paramIndex + 8, paramValue._9)
        t10(statement, paramIndex + 9, paramValue._10)
        t11(statement, paramIndex + 10, paramValue._11)
        t12(statement, paramIndex + 11, paramValue._12)
        t13(statement, paramIndex + 12, paramValue._13)
        t14(statement, paramIndex + 13, paramValue._14)
        t15(statement, paramIndex + 14, paramValue._15)
        t16(statement, paramIndex + 15, paramValue._16)
        t17(statement, paramIndex + 16, paramValue._17)
        t18(statement, paramIndex + 17, paramValue._18)
      }
    }

  implicit def tuple19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](implicit t1: Param[T1],
                                                                                                             t2: Param[T2],
                                                                                                             t3: Param[T3],
                                                                                                             t4: Param[T4],
                                                                                                             t5: Param[T5],
                                                                                                             t6: Param[T6],
                                                                                                             t7: Param[T7],
                                                                                                             t8: Param[T8],
                                                                                                             t9: Param[T9],
                                                                                                             t10: Param[T10],
                                                                                                             t11: Param[T11],
                                                                                                             t12: Param[T12],
                                                                                                             t13: Param[T13],
                                                                                                             t14: Param[T14],
                                                                                                             t15: Param[T15],
                                                                                                             t16: Param[T16],
                                                                                                             t17: Param[T17],
                                                                                                             t18: Param[T18],
                                                                                                             t19: Param[T19]): Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)] =
    new Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)] {
      override def parametersCount(): Int = 19

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
        t5(statement, paramIndex + 4, paramValue._5)
        t6(statement, paramIndex + 5, paramValue._6)
        t7(statement, paramIndex + 6, paramValue._7)
        t8(statement, paramIndex + 7, paramValue._8)
        t9(statement, paramIndex + 8, paramValue._9)
        t10(statement, paramIndex + 9, paramValue._10)
        t11(statement, paramIndex + 10, paramValue._11)
        t12(statement, paramIndex + 11, paramValue._12)
        t13(statement, paramIndex + 12, paramValue._13)
        t14(statement, paramIndex + 13, paramValue._14)
        t15(statement, paramIndex + 14, paramValue._15)
        t16(statement, paramIndex + 15, paramValue._16)
        t17(statement, paramIndex + 16, paramValue._17)
        t18(statement, paramIndex + 17, paramValue._18)
        t19(statement, paramIndex + 18, paramValue._19)
      }
    }

  implicit def tuple20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](implicit t1: Param[T1],
                                                                                                                  t2: Param[T2],
                                                                                                                  t3: Param[T3],
                                                                                                                  t4: Param[T4],
                                                                                                                  t5: Param[T5],
                                                                                                                  t6: Param[T6],
                                                                                                                  t7: Param[T7],
                                                                                                                  t8: Param[T8],
                                                                                                                  t9: Param[T9],
                                                                                                                  t10: Param[T10],
                                                                                                                  t11: Param[T11],
                                                                                                                  t12: Param[T12],
                                                                                                                  t13: Param[T13],
                                                                                                                  t14: Param[T14],
                                                                                                                  t15: Param[T15],
                                                                                                                  t16: Param[T16],
                                                                                                                  t17: Param[T17],
                                                                                                                  t18: Param[T18],
                                                                                                                  t19: Param[T19],
                                                                                                                  t20: Param[T20]): Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)] =
    new Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)] {
      override def parametersCount(): Int = 20

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
        t5(statement, paramIndex + 4, paramValue._5)
        t6(statement, paramIndex + 5, paramValue._6)
        t7(statement, paramIndex + 6, paramValue._7)
        t8(statement, paramIndex + 7, paramValue._8)
        t9(statement, paramIndex + 8, paramValue._9)
        t10(statement, paramIndex + 9, paramValue._10)
        t11(statement, paramIndex + 10, paramValue._11)
        t12(statement, paramIndex + 11, paramValue._12)
        t13(statement, paramIndex + 12, paramValue._13)
        t14(statement, paramIndex + 13, paramValue._14)
        t15(statement, paramIndex + 14, paramValue._15)
        t16(statement, paramIndex + 15, paramValue._16)
        t17(statement, paramIndex + 16, paramValue._17)
        t18(statement, paramIndex + 17, paramValue._18)
        t19(statement, paramIndex + 18, paramValue._19)
        t20(statement, paramIndex + 19, paramValue._20)
      }
    }

  implicit def tuple21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](implicit t1: Param[T1],
                                                                                                                       t2: Param[T2],
                                                                                                                       t3: Param[T3],
                                                                                                                       t4: Param[T4],
                                                                                                                       t5: Param[T5],
                                                                                                                       t6: Param[T6],
                                                                                                                       t7: Param[T7],
                                                                                                                       t8: Param[T8],
                                                                                                                       t9: Param[T9],
                                                                                                                       t10: Param[T10],
                                                                                                                       t11: Param[T11],
                                                                                                                       t12: Param[T12],
                                                                                                                       t13: Param[T13],
                                                                                                                       t14: Param[T14],
                                                                                                                       t15: Param[T15],
                                                                                                                       t16: Param[T16],
                                                                                                                       t17: Param[T17],
                                                                                                                       t18: Param[T18],
                                                                                                                       t19: Param[T19],
                                                                                                                       t20: Param[T20],
                                                                                                                       t21: Param[T21]): Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)] =
    new Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)] {
      override def parametersCount(): Int = 21

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
        t5(statement, paramIndex + 4, paramValue._5)
        t6(statement, paramIndex + 5, paramValue._6)
        t7(statement, paramIndex + 6, paramValue._7)
        t8(statement, paramIndex + 7, paramValue._8)
        t9(statement, paramIndex + 8, paramValue._9)
        t10(statement, paramIndex + 9, paramValue._10)
        t11(statement, paramIndex + 10, paramValue._11)
        t12(statement, paramIndex + 11, paramValue._12)
        t13(statement, paramIndex + 12, paramValue._13)
        t14(statement, paramIndex + 13, paramValue._14)
        t15(statement, paramIndex + 14, paramValue._15)
        t16(statement, paramIndex + 15, paramValue._16)
        t17(statement, paramIndex + 16, paramValue._17)
        t18(statement, paramIndex + 17, paramValue._18)
        t19(statement, paramIndex + 18, paramValue._19)
        t20(statement, paramIndex + 19, paramValue._20)
        t21(statement, paramIndex + 20, paramValue._21)
      }
    }

  implicit def tuple22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](implicit t1: Param[T1],
                                                                                                                            t2: Param[T2],
                                                                                                                            t3: Param[T3],
                                                                                                                            t4: Param[T4],
                                                                                                                            t5: Param[T5],
                                                                                                                            t6: Param[T6],
                                                                                                                            t7: Param[T7],
                                                                                                                            t8: Param[T8],
                                                                                                                            t9: Param[T9],
                                                                                                                            t10: Param[T10],
                                                                                                                            t11: Param[T11],
                                                                                                                            t12: Param[T12],
                                                                                                                            t13: Param[T13],
                                                                                                                            t14: Param[T14],
                                                                                                                            t15: Param[T15],
                                                                                                                            t16: Param[T16],
                                                                                                                            t17: Param[T17],
                                                                                                                            t18: Param[T18],
                                                                                                                            t19: Param[T19],
                                                                                                                            t20: Param[T20],
                                                                                                                            t21: Param[T21],
                                                                                                                            t22: Param[T22]): Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)] =
    new Param[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)] {
      override def parametersCount(): Int = 22

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
        t5(statement, paramIndex + 4, paramValue._5)
        t6(statement, paramIndex + 5, paramValue._6)
        t7(statement, paramIndex + 6, paramValue._7)
        t8(statement, paramIndex + 7, paramValue._8)
        t9(statement, paramIndex + 8, paramValue._9)
        t10(statement, paramIndex + 9, paramValue._10)
        t11(statement, paramIndex + 10, paramValue._11)
        t12(statement, paramIndex + 11, paramValue._12)
        t13(statement, paramIndex + 12, paramValue._13)
        t14(statement, paramIndex + 13, paramValue._14)
        t15(statement, paramIndex + 14, paramValue._15)
        t16(statement, paramIndex + 15, paramValue._16)
        t17(statement, paramIndex + 16, paramValue._17)
        t18(statement, paramIndex + 17, paramValue._18)
        t19(statement, paramIndex + 18, paramValue._19)
        t20(statement, paramIndex + 19, paramValue._20)
        t21(statement, paramIndex + 20, paramValue._21)
        t22(statement, paramIndex + 21, paramValue._22)
      }
    }
}
