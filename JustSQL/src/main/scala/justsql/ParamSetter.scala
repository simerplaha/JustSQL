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

trait ParamSetter[P] extends ((PreparedStatement, Int, P) => Unit) {
  /** Total number of parameters this [[ParamSetter]] writes to a [[PreparedStatement]] */
  def parametersCount(): Int

  override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: P): Unit
}

trait OneParamSetter[P] extends ParamSetter[P] {

  final val parametersCount: Int = 1

}

object ParamSetter {
  implicit val intParam: OneParamSetter[Int] = _.setInt(_, _)
  implicit val stringParam: OneParamSetter[String] = _.setString(_, _)
  implicit val booleanParam: OneParamSetter[Boolean] = _.setBoolean(_, _)
  implicit val longParam: OneParamSetter[Long] = _.setLong(_, _)
  implicit val byteParam: OneParamSetter[Byte] = _.setByte(_, _)
  implicit val doubleParam: OneParamSetter[Double] = _.setDouble(_, _)
  implicit val floatParam: OneParamSetter[Float] = _.setFloat(_, _)
  implicit val shortParam: OneParamSetter[Short] = _.setShort(_, _)
  implicit val bigDecimalParam: OneParamSetter[java.math.BigDecimal] = _.setBigDecimal(_, _)
  implicit val byteArrayParam: OneParamSetter[Array[Byte]] = _.setBytes(_, _)

  //java.sql types
  implicit val timestampParam: OneParamSetter[java.sql.Timestamp] = _.setTimestamp(_, _)
  implicit val blobParam: OneParamSetter[java.sql.Blob] = _.setBlob(_, _)
  implicit val clobParam: OneParamSetter[java.sql.Clob] = _.setClob(_, _)
  implicit val dateParam: OneParamSetter[java.sql.Date] = _.setDate(_, _)
  implicit val timeParam: OneParamSetter[java.sql.Time] = _.setTime(_, _)
  implicit val refParam: OneParamSetter[java.sql.Ref] = _.setRef(_, _)
  implicit val nClobParam: OneParamSetter[java.sql.NClob] = _.setNClob(_, _)
  implicit val rowIdParam: OneParamSetter[java.sql.RowId] = _.setRowId(_, _)
  implicit val sqlXMLParam: OneParamSetter[java.sql.SQLXML] = _.setSQLXML(_, _)

  //other types
  implicit val characterStreamParam: OneParamSetter[java.io.Reader] = _.setCharacterStream(_, _)
  implicit val binaryStreamParam: OneParamSetter[java.io.InputStream] = _.setBinaryStream(_, _)
  implicit val urlParam: OneParamSetter[java.net.URL] = _.setURL(_, _)

  implicit def tuple2[T1, T2](implicit t1: ParamSetter[T1],
                              t2: ParamSetter[T2]): ParamSetter[(T1, T2)] =
    new ParamSetter[(T1, T2)] {
      override def parametersCount(): Int = 2

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
      }
    }

  implicit def tuple3[T1, T2, T3](implicit t1: ParamSetter[T1],
                                  t2: ParamSetter[T2],
                                  t3: ParamSetter[T3]): ParamSetter[(T1, T2, T3)] =
    new ParamSetter[(T1, T2, T3)] {
      override def parametersCount(): Int = 3

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
      }
    }

  implicit def tuple4[T1, T2, T3, T4](implicit t1: ParamSetter[T1],
                                      t2: ParamSetter[T2],
                                      t3: ParamSetter[T3],
                                      t4: ParamSetter[T4]): ParamSetter[(T1, T2, T3, T4)] =
    new ParamSetter[(T1, T2, T3, T4)] {
      override def parametersCount(): Int = 4

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
      }
    }

  implicit def tuple5[T1, T2, T3, T4, T5](implicit t1: ParamSetter[T1],
                                          t2: ParamSetter[T2],
                                          t3: ParamSetter[T3],
                                          t4: ParamSetter[T4],
                                          t5: ParamSetter[T5]): ParamSetter[(T1, T2, T3, T4, T5)] =
    new ParamSetter[(T1, T2, T3, T4, T5)] {
      override def parametersCount(): Int = 5

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (T1, T2, T3, T4, T5)): Unit = {
        t1(statement, paramIndex, paramValue._1)
        t2(statement, paramIndex + 1, paramValue._2)
        t3(statement, paramIndex + 2, paramValue._3)
        t4(statement, paramIndex + 3, paramValue._4)
        t5(statement, paramIndex + 4, paramValue._5)
      }
    }

  implicit def tuple6[T1, T2, T3, T4, T5, T6](implicit t1: ParamSetter[T1],
                                              t2: ParamSetter[T2],
                                              t3: ParamSetter[T3],
                                              t4: ParamSetter[T4],
                                              t5: ParamSetter[T5],
                                              t6: ParamSetter[T6]): ParamSetter[(T1, T2, T3, T4, T5, T6)] =
    new ParamSetter[(T1, T2, T3, T4, T5, T6)] {
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

  implicit def tuple7[T1, T2, T3, T4, T5, T6, T7](implicit t1: ParamSetter[T1],
                                                  t2: ParamSetter[T2],
                                                  t3: ParamSetter[T3],
                                                  t4: ParamSetter[T4],
                                                  t5: ParamSetter[T5],
                                                  t6: ParamSetter[T6],
                                                  t7: ParamSetter[T7]): ParamSetter[(T1, T2, T3, T4, T5, T6, T7)] =
    new ParamSetter[(T1, T2, T3, T4, T5, T6, T7)] {
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

  implicit def tuple8[T1, T2, T3, T4, T5, T6, T7, T8](implicit t1: ParamSetter[T1],
                                                      t2: ParamSetter[T2],
                                                      t3: ParamSetter[T3],
                                                      t4: ParamSetter[T4],
                                                      t5: ParamSetter[T5],
                                                      t6: ParamSetter[T6],
                                                      t7: ParamSetter[T7],
                                                      t8: ParamSetter[T8]): ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8)] =
    new ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8)] {
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

  implicit def tuple9[T1, T2, T3, T4, T5, T6, T7, T8, T9](implicit t1: ParamSetter[T1],
                                                          t2: ParamSetter[T2],
                                                          t3: ParamSetter[T3],
                                                          t4: ParamSetter[T4],
                                                          t5: ParamSetter[T5],
                                                          t6: ParamSetter[T6],
                                                          t7: ParamSetter[T7],
                                                          t8: ParamSetter[T8],
                                                          t9: ParamSetter[T9]): ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9)] =
    new ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9)] {
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

  implicit def tuple10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](implicit t1: ParamSetter[T1],
                                                                t2: ParamSetter[T2],
                                                                t3: ParamSetter[T3],
                                                                t4: ParamSetter[T4],
                                                                t5: ParamSetter[T5],
                                                                t6: ParamSetter[T6],
                                                                t7: ParamSetter[T7],
                                                                t8: ParamSetter[T8],
                                                                t9: ParamSetter[T9],
                                                                t10: ParamSetter[T10]): ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)] =
    new ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)] {
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

  implicit def tuple11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](implicit t1: ParamSetter[T1],
                                                                     t2: ParamSetter[T2],
                                                                     t3: ParamSetter[T3],
                                                                     t4: ParamSetter[T4],
                                                                     t5: ParamSetter[T5],
                                                                     t6: ParamSetter[T6],
                                                                     t7: ParamSetter[T7],
                                                                     t8: ParamSetter[T8],
                                                                     t9: ParamSetter[T9],
                                                                     t10: ParamSetter[T10],
                                                                     t11: ParamSetter[T11]): ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)] =
    new ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)] {
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

  implicit def tuple12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](implicit t1: ParamSetter[T1],
                                                                          t2: ParamSetter[T2],
                                                                          t3: ParamSetter[T3],
                                                                          t4: ParamSetter[T4],
                                                                          t5: ParamSetter[T5],
                                                                          t6: ParamSetter[T6],
                                                                          t7: ParamSetter[T7],
                                                                          t8: ParamSetter[T8],
                                                                          t9: ParamSetter[T9],
                                                                          t10: ParamSetter[T10],
                                                                          t11: ParamSetter[T11],
                                                                          t12: ParamSetter[T12]): ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)] =
    new ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)] {
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

  implicit def tuple13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](implicit t1: ParamSetter[T1],
                                                                               t2: ParamSetter[T2],
                                                                               t3: ParamSetter[T3],
                                                                               t4: ParamSetter[T4],
                                                                               t5: ParamSetter[T5],
                                                                               t6: ParamSetter[T6],
                                                                               t7: ParamSetter[T7],
                                                                               t8: ParamSetter[T8],
                                                                               t9: ParamSetter[T9],
                                                                               t10: ParamSetter[T10],
                                                                               t11: ParamSetter[T11],
                                                                               t12: ParamSetter[T12],
                                                                               t13: ParamSetter[T13]): ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)] =
    new ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)] {
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

  implicit def tuple14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](implicit t1: ParamSetter[T1],
                                                                                    t2: ParamSetter[T2],
                                                                                    t3: ParamSetter[T3],
                                                                                    t4: ParamSetter[T4],
                                                                                    t5: ParamSetter[T5],
                                                                                    t6: ParamSetter[T6],
                                                                                    t7: ParamSetter[T7],
                                                                                    t8: ParamSetter[T8],
                                                                                    t9: ParamSetter[T9],
                                                                                    t10: ParamSetter[T10],
                                                                                    t11: ParamSetter[T11],
                                                                                    t12: ParamSetter[T12],
                                                                                    t13: ParamSetter[T13],
                                                                                    t14: ParamSetter[T14]): ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)] =
    new ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)] {
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

  implicit def tuple15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](implicit t1: ParamSetter[T1],
                                                                                         t2: ParamSetter[T2],
                                                                                         t3: ParamSetter[T3],
                                                                                         t4: ParamSetter[T4],
                                                                                         t5: ParamSetter[T5],
                                                                                         t6: ParamSetter[T6],
                                                                                         t7: ParamSetter[T7],
                                                                                         t8: ParamSetter[T8],
                                                                                         t9: ParamSetter[T9],
                                                                                         t10: ParamSetter[T10],
                                                                                         t11: ParamSetter[T11],
                                                                                         t12: ParamSetter[T12],
                                                                                         t13: ParamSetter[T13],
                                                                                         t14: ParamSetter[T14],
                                                                                         t15: ParamSetter[T15]): ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)] =
    new ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)] {
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

  implicit def tuple16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](implicit t1: ParamSetter[T1],
                                                                                              t2: ParamSetter[T2],
                                                                                              t3: ParamSetter[T3],
                                                                                              t4: ParamSetter[T4],
                                                                                              t5: ParamSetter[T5],
                                                                                              t6: ParamSetter[T6],
                                                                                              t7: ParamSetter[T7],
                                                                                              t8: ParamSetter[T8],
                                                                                              t9: ParamSetter[T9],
                                                                                              t10: ParamSetter[T10],
                                                                                              t11: ParamSetter[T11],
                                                                                              t12: ParamSetter[T12],
                                                                                              t13: ParamSetter[T13],
                                                                                              t14: ParamSetter[T14],
                                                                                              t15: ParamSetter[T15],
                                                                                              t16: ParamSetter[T16]): ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)] =
    new ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)] {
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

  implicit def tuple17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](implicit t1: ParamSetter[T1],
                                                                                                   t2: ParamSetter[T2],
                                                                                                   t3: ParamSetter[T3],
                                                                                                   t4: ParamSetter[T4],
                                                                                                   t5: ParamSetter[T5],
                                                                                                   t6: ParamSetter[T6],
                                                                                                   t7: ParamSetter[T7],
                                                                                                   t8: ParamSetter[T8],
                                                                                                   t9: ParamSetter[T9],
                                                                                                   t10: ParamSetter[T10],
                                                                                                   t11: ParamSetter[T11],
                                                                                                   t12: ParamSetter[T12],
                                                                                                   t13: ParamSetter[T13],
                                                                                                   t14: ParamSetter[T14],
                                                                                                   t15: ParamSetter[T15],
                                                                                                   t16: ParamSetter[T16],
                                                                                                   t17: ParamSetter[T17]): ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)] =
    new ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)] {
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

  implicit def tuple18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](implicit t1: ParamSetter[T1],
                                                                                                        t2: ParamSetter[T2],
                                                                                                        t3: ParamSetter[T3],
                                                                                                        t4: ParamSetter[T4],
                                                                                                        t5: ParamSetter[T5],
                                                                                                        t6: ParamSetter[T6],
                                                                                                        t7: ParamSetter[T7],
                                                                                                        t8: ParamSetter[T8],
                                                                                                        t9: ParamSetter[T9],
                                                                                                        t10: ParamSetter[T10],
                                                                                                        t11: ParamSetter[T11],
                                                                                                        t12: ParamSetter[T12],
                                                                                                        t13: ParamSetter[T13],
                                                                                                        t14: ParamSetter[T14],
                                                                                                        t15: ParamSetter[T15],
                                                                                                        t16: ParamSetter[T16],
                                                                                                        t17: ParamSetter[T17],
                                                                                                        t18: ParamSetter[T18]): ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)] =
    new ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)] {
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

  implicit def tuple19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](implicit t1: ParamSetter[T1],
                                                                                                             t2: ParamSetter[T2],
                                                                                                             t3: ParamSetter[T3],
                                                                                                             t4: ParamSetter[T4],
                                                                                                             t5: ParamSetter[T5],
                                                                                                             t6: ParamSetter[T6],
                                                                                                             t7: ParamSetter[T7],
                                                                                                             t8: ParamSetter[T8],
                                                                                                             t9: ParamSetter[T9],
                                                                                                             t10: ParamSetter[T10],
                                                                                                             t11: ParamSetter[T11],
                                                                                                             t12: ParamSetter[T12],
                                                                                                             t13: ParamSetter[T13],
                                                                                                             t14: ParamSetter[T14],
                                                                                                             t15: ParamSetter[T15],
                                                                                                             t16: ParamSetter[T16],
                                                                                                             t17: ParamSetter[T17],
                                                                                                             t18: ParamSetter[T18],
                                                                                                             t19: ParamSetter[T19]): ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)] =
    new ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)] {
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

  implicit def tuple20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](implicit t1: ParamSetter[T1],
                                                                                                                  t2: ParamSetter[T2],
                                                                                                                  t3: ParamSetter[T3],
                                                                                                                  t4: ParamSetter[T4],
                                                                                                                  t5: ParamSetter[T5],
                                                                                                                  t6: ParamSetter[T6],
                                                                                                                  t7: ParamSetter[T7],
                                                                                                                  t8: ParamSetter[T8],
                                                                                                                  t9: ParamSetter[T9],
                                                                                                                  t10: ParamSetter[T10],
                                                                                                                  t11: ParamSetter[T11],
                                                                                                                  t12: ParamSetter[T12],
                                                                                                                  t13: ParamSetter[T13],
                                                                                                                  t14: ParamSetter[T14],
                                                                                                                  t15: ParamSetter[T15],
                                                                                                                  t16: ParamSetter[T16],
                                                                                                                  t17: ParamSetter[T17],
                                                                                                                  t18: ParamSetter[T18],
                                                                                                                  t19: ParamSetter[T19],
                                                                                                                  t20: ParamSetter[T20]): ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)] =
    new ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)] {
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

  implicit def tuple21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](implicit t1: ParamSetter[T1],
                                                                                                                       t2: ParamSetter[T2],
                                                                                                                       t3: ParamSetter[T3],
                                                                                                                       t4: ParamSetter[T4],
                                                                                                                       t5: ParamSetter[T5],
                                                                                                                       t6: ParamSetter[T6],
                                                                                                                       t7: ParamSetter[T7],
                                                                                                                       t8: ParamSetter[T8],
                                                                                                                       t9: ParamSetter[T9],
                                                                                                                       t10: ParamSetter[T10],
                                                                                                                       t11: ParamSetter[T11],
                                                                                                                       t12: ParamSetter[T12],
                                                                                                                       t13: ParamSetter[T13],
                                                                                                                       t14: ParamSetter[T14],
                                                                                                                       t15: ParamSetter[T15],
                                                                                                                       t16: ParamSetter[T16],
                                                                                                                       t17: ParamSetter[T17],
                                                                                                                       t18: ParamSetter[T18],
                                                                                                                       t19: ParamSetter[T19],
                                                                                                                       t20: ParamSetter[T20],
                                                                                                                       t21: ParamSetter[T21]): ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)] =
    new ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)] {
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

  implicit def tuple22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](implicit t1: ParamSetter[T1],
                                                                                                                            t2: ParamSetter[T2],
                                                                                                                            t3: ParamSetter[T3],
                                                                                                                            t4: ParamSetter[T4],
                                                                                                                            t5: ParamSetter[T5],
                                                                                                                            t6: ParamSetter[T6],
                                                                                                                            t7: ParamSetter[T7],
                                                                                                                            t8: ParamSetter[T8],
                                                                                                                            t9: ParamSetter[T9],
                                                                                                                            t10: ParamSetter[T10],
                                                                                                                            t11: ParamSetter[T11],
                                                                                                                            t12: ParamSetter[T12],
                                                                                                                            t13: ParamSetter[T13],
                                                                                                                            t14: ParamSetter[T14],
                                                                                                                            t15: ParamSetter[T15],
                                                                                                                            t16: ParamSetter[T16],
                                                                                                                            t17: ParamSetter[T17],
                                                                                                                            t18: ParamSetter[T18],
                                                                                                                            t19: ParamSetter[T19],
                                                                                                                            t20: ParamSetter[T20],
                                                                                                                            t21: ParamSetter[T21],
                                                                                                                            t22: ParamSetter[T22]): ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)] =
    new ParamSetter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)] {
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
