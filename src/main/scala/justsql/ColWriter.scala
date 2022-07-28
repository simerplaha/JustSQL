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

trait ColWriter[P] extends ((PreparedStatement, Int, P) => Int) {

  /**
   * Return the next index of the parameters
   *
   * For example if you have a tuple2
   * {{{
   *   implicit case object MyTupleColWriter extends ColWriter[(Int, String)] {
   *      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (Int, String)): Int = {
   *        statement.setInt(parameterIndex, paramValue._1)
   *        statement.setInt(parameterIndex + 1, paramValue._1)
   *        parameterIndex + 2
   *      }
   *    }
   * }}}
   *
   * */
  override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: P): Int

}

object ColWriter {

  implicit case object IntColWriter extends ColWriter[Int] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: Int): Int = {
      statement.setInt(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit case object StringColWriter extends ColWriter[String] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: String): Int = {
      statement.setString(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit case object BooleanColWriter extends ColWriter[Boolean] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: Boolean): Int = {
      statement.setBoolean(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit case object LongColWriter extends ColWriter[Long] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: Long): Int = {
      statement.setLong(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit case object ByteColWriter extends ColWriter[Byte] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: Byte): Int = {
      statement.setByte(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit case object DoubleColWriter extends ColWriter[Double] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: Double): Int = {
      statement.setDouble(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit case object FloatColWriter extends ColWriter[Float] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: Float): Int = {
      statement.setFloat(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit case object ShortColWriter extends ColWriter[Short] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: Short): Int = {
      statement.setShort(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit case object BigDecimalColWriter extends ColWriter[java.math.BigDecimal] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: java.math.BigDecimal): Int = {
      statement.setBigDecimal(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit case object ByteArrayColWriter extends ColWriter[Array[Byte]] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: Array[Byte]): Int = {
      statement.setBytes(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }


  //java.sql types
  implicit case object TimestampColWriter extends ColWriter[java.sql.Timestamp] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: java.sql.Timestamp): Int = {
      statement.setTimestamp(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit case object BlobColWriter extends ColWriter[java.sql.Blob] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: java.sql.Blob): Int = {
      statement.setBlob(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit case object ClobColWriter extends ColWriter[java.sql.Clob] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: java.sql.Clob): Int = {
      statement.setClob(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit case object DateColWriter extends ColWriter[java.sql.Date] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: java.sql.Date): Int = {
      statement.setDate(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit case object TimeColWriter extends ColWriter[java.sql.Time] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: java.sql.Time): Int = {
      statement.setTime(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit case object RefColWriter extends ColWriter[java.sql.Ref] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: java.sql.Ref): Int = {
      statement.setRef(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit case object NClobColWriter extends ColWriter[java.sql.NClob] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: java.sql.NClob): Int = {
      statement.setNClob(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit case object RowIdColWriter extends ColWriter[java.sql.RowId] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: java.sql.RowId): Int = {
      statement.setRowId(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit case object SqlXMLColWriter extends ColWriter[java.sql.SQLXML] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: java.sql.SQLXML): Int = {
      statement.setSQLXML(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }


  //other types
  implicit case object CharacterStreamColWriter extends ColWriter[java.io.Reader] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: java.io.Reader): Int = {
      statement.setCharacterStream(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit case object BinaryStreamColWriter extends ColWriter[java.io.InputStream] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: java.io.InputStream): Int = {
      statement.setBinaryStream(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit case object UrlColWriter extends ColWriter[java.net.URL] {
    override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: java.net.URL): Int = {
      statement.setURL(parameterIndex, paramValue)
      parameterIndex + 1
    }
  }

  implicit def tuple1[T1](implicit t1: ColWriter[T1]): ColWriter[T1] =
    new ColWriter[T1] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: T1): Int = {
        t1(statement, parameterIndex, paramValue)
        parameterIndex + 1
      }
    }

  implicit def tuple2[T1, T2](implicit t1: ColWriter[T1],
                              t2: ColWriter[T2]): ColWriter[(T1, T2)] =
    new ColWriter[(T1, T2)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2)): Int = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        parameterIndex + 2
      }
    }

  implicit def tuple3[T1, T2, T3](implicit t1: ColWriter[T1],
                                  t2: ColWriter[T2],
                                  t3: ColWriter[T3]): ColWriter[(T1, T2, T3)] =
    new ColWriter[(T1, T2, T3)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3)): Int = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        parameterIndex + 3
      }
    }

  implicit def tuple4[T1, T2, T3, T4](implicit t1: ColWriter[T1],
                                      t2: ColWriter[T2],
                                      t3: ColWriter[T3],
                                      t4: ColWriter[T4]): ColWriter[(T1, T2, T3, T4)] =
    new ColWriter[(T1, T2, T3, T4)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4)): Int = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        parameterIndex + 4
      }
    }

  implicit def tuple5[T1, T2, T3, T4, T5](implicit t1: ColWriter[T1],
                                          t2: ColWriter[T2],
                                          t3: ColWriter[T3],
                                          t4: ColWriter[T4],
                                          t5: ColWriter[T5]): ColWriter[(T1, T2, T3, T4, T5)] =
    new ColWriter[(T1, T2, T3, T4, T5)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5)): Int = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        parameterIndex + 5
      }
    }

  implicit def tuple6[T1, T2, T3, T4, T5, T6](implicit t1: ColWriter[T1],
                                              t2: ColWriter[T2],
                                              t3: ColWriter[T3],
                                              t4: ColWriter[T4],
                                              t5: ColWriter[T5],
                                              t6: ColWriter[T6]): ColWriter[(T1, T2, T3, T4, T5, T6)] =
    new ColWriter[(T1, T2, T3, T4, T5, T6)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6)): Int = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        parameterIndex + 6
      }
    }

  implicit def tuple7[T1, T2, T3, T4, T5, T6, T7](implicit t1: ColWriter[T1],
                                                  t2: ColWriter[T2],
                                                  t3: ColWriter[T3],
                                                  t4: ColWriter[T4],
                                                  t5: ColWriter[T5],
                                                  t6: ColWriter[T6],
                                                  t7: ColWriter[T7]): ColWriter[(T1, T2, T3, T4, T5, T6, T7)] =
    new ColWriter[(T1, T2, T3, T4, T5, T6, T7)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7)): Int = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        t7(statement, parameterIndex + 6, paramValue._7)
        parameterIndex + 7
      }
    }

  implicit def tuple8[T1, T2, T3, T4, T5, T6, T7, T8](implicit t1: ColWriter[T1],
                                                      t2: ColWriter[T2],
                                                      t3: ColWriter[T3],
                                                      t4: ColWriter[T4],
                                                      t5: ColWriter[T5],
                                                      t6: ColWriter[T6],
                                                      t7: ColWriter[T7],
                                                      t8: ColWriter[T8]): ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8)] =
    new ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8)): Int = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        t7(statement, parameterIndex + 6, paramValue._7)
        t8(statement, parameterIndex + 7, paramValue._8)
        parameterIndex + 8
      }
    }

  implicit def tuple9[T1, T2, T3, T4, T5, T6, T7, T8, T9](implicit t1: ColWriter[T1],
                                                          t2: ColWriter[T2],
                                                          t3: ColWriter[T3],
                                                          t4: ColWriter[T4],
                                                          t5: ColWriter[T5],
                                                          t6: ColWriter[T6],
                                                          t7: ColWriter[T7],
                                                          t8: ColWriter[T8],
                                                          t9: ColWriter[T9]): ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9)] =
    new ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9)): Int = {
        t1(statement, parameterIndex, paramValue._1)
        t2(statement, parameterIndex + 1, paramValue._2)
        t3(statement, parameterIndex + 2, paramValue._3)
        t4(statement, parameterIndex + 3, paramValue._4)
        t5(statement, parameterIndex + 4, paramValue._5)
        t6(statement, parameterIndex + 5, paramValue._6)
        t7(statement, parameterIndex + 6, paramValue._7)
        t8(statement, parameterIndex + 7, paramValue._8)
        t9(statement, parameterIndex + 8, paramValue._9)
        parameterIndex + 9
      }
    }

  implicit def tuple10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](implicit t1: ColWriter[T1],
                                                                t2: ColWriter[T2],
                                                                t3: ColWriter[T3],
                                                                t4: ColWriter[T4],
                                                                t5: ColWriter[T5],
                                                                t6: ColWriter[T6],
                                                                t7: ColWriter[T7],
                                                                t8: ColWriter[T8],
                                                                t9: ColWriter[T9],
                                                                t10: ColWriter[T10]): ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)] =
    new ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)): Int = {
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
        parameterIndex + 10
      }
    }

  implicit def tuple11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](implicit t1: ColWriter[T1],
                                                                     t2: ColWriter[T2],
                                                                     t3: ColWriter[T3],
                                                                     t4: ColWriter[T4],
                                                                     t5: ColWriter[T5],
                                                                     t6: ColWriter[T6],
                                                                     t7: ColWriter[T7],
                                                                     t8: ColWriter[T8],
                                                                     t9: ColWriter[T9],
                                                                     t10: ColWriter[T10],
                                                                     t11: ColWriter[T11]): ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)] =
    new ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)): Int = {
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
        parameterIndex + 11
      }
    }

  implicit def tuple12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](implicit t1: ColWriter[T1],
                                                                          t2: ColWriter[T2],
                                                                          t3: ColWriter[T3],
                                                                          t4: ColWriter[T4],
                                                                          t5: ColWriter[T5],
                                                                          t6: ColWriter[T6],
                                                                          t7: ColWriter[T7],
                                                                          t8: ColWriter[T8],
                                                                          t9: ColWriter[T9],
                                                                          t10: ColWriter[T10],
                                                                          t11: ColWriter[T11],
                                                                          t12: ColWriter[T12]): ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)] =
    new ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)): Int = {
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
        parameterIndex + 12
      }
    }

  implicit def tuple13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](implicit t1: ColWriter[T1],
                                                                               t2: ColWriter[T2],
                                                                               t3: ColWriter[T3],
                                                                               t4: ColWriter[T4],
                                                                               t5: ColWriter[T5],
                                                                               t6: ColWriter[T6],
                                                                               t7: ColWriter[T7],
                                                                               t8: ColWriter[T8],
                                                                               t9: ColWriter[T9],
                                                                               t10: ColWriter[T10],
                                                                               t11: ColWriter[T11],
                                                                               t12: ColWriter[T12],
                                                                               t13: ColWriter[T13]): ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)] =
    new ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)): Int = {
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
        parameterIndex + 13
      }
    }

  implicit def tuple14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](implicit t1: ColWriter[T1],
                                                                                    t2: ColWriter[T2],
                                                                                    t3: ColWriter[T3],
                                                                                    t4: ColWriter[T4],
                                                                                    t5: ColWriter[T5],
                                                                                    t6: ColWriter[T6],
                                                                                    t7: ColWriter[T7],
                                                                                    t8: ColWriter[T8],
                                                                                    t9: ColWriter[T9],
                                                                                    t10: ColWriter[T10],
                                                                                    t11: ColWriter[T11],
                                                                                    t12: ColWriter[T12],
                                                                                    t13: ColWriter[T13],
                                                                                    t14: ColWriter[T14]): ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)] =
    new ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)): Int = {
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
        parameterIndex + 14
      }
    }

  implicit def tuple15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](implicit t1: ColWriter[T1],
                                                                                         t2: ColWriter[T2],
                                                                                         t3: ColWriter[T3],
                                                                                         t4: ColWriter[T4],
                                                                                         t5: ColWriter[T5],
                                                                                         t6: ColWriter[T6],
                                                                                         t7: ColWriter[T7],
                                                                                         t8: ColWriter[T8],
                                                                                         t9: ColWriter[T9],
                                                                                         t10: ColWriter[T10],
                                                                                         t11: ColWriter[T11],
                                                                                         t12: ColWriter[T12],
                                                                                         t13: ColWriter[T13],
                                                                                         t14: ColWriter[T14],
                                                                                         t15: ColWriter[T15]): ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)] =
    new ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)): Int = {
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
        parameterIndex + 15
      }
    }

  implicit def tuple16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](implicit t1: ColWriter[T1],
                                                                                              t2: ColWriter[T2],
                                                                                              t3: ColWriter[T3],
                                                                                              t4: ColWriter[T4],
                                                                                              t5: ColWriter[T5],
                                                                                              t6: ColWriter[T6],
                                                                                              t7: ColWriter[T7],
                                                                                              t8: ColWriter[T8],
                                                                                              t9: ColWriter[T9],
                                                                                              t10: ColWriter[T10],
                                                                                              t11: ColWriter[T11],
                                                                                              t12: ColWriter[T12],
                                                                                              t13: ColWriter[T13],
                                                                                              t14: ColWriter[T14],
                                                                                              t15: ColWriter[T15],
                                                                                              t16: ColWriter[T16]): ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)] =
    new ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)): Int = {
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
        parameterIndex + 16
      }
    }

  implicit def tuple17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](implicit t1: ColWriter[T1],
                                                                                                   t2: ColWriter[T2],
                                                                                                   t3: ColWriter[T3],
                                                                                                   t4: ColWriter[T4],
                                                                                                   t5: ColWriter[T5],
                                                                                                   t6: ColWriter[T6],
                                                                                                   t7: ColWriter[T7],
                                                                                                   t8: ColWriter[T8],
                                                                                                   t9: ColWriter[T9],
                                                                                                   t10: ColWriter[T10],
                                                                                                   t11: ColWriter[T11],
                                                                                                   t12: ColWriter[T12],
                                                                                                   t13: ColWriter[T13],
                                                                                                   t14: ColWriter[T14],
                                                                                                   t15: ColWriter[T15],
                                                                                                   t16: ColWriter[T16],
                                                                                                   t17: ColWriter[T17]): ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)] =
    new ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)): Int = {
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
        parameterIndex + 17
      }
    }

  implicit def tuple18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](implicit t1: ColWriter[T1],
                                                                                                        t2: ColWriter[T2],
                                                                                                        t3: ColWriter[T3],
                                                                                                        t4: ColWriter[T4],
                                                                                                        t5: ColWriter[T5],
                                                                                                        t6: ColWriter[T6],
                                                                                                        t7: ColWriter[T7],
                                                                                                        t8: ColWriter[T8],
                                                                                                        t9: ColWriter[T9],
                                                                                                        t10: ColWriter[T10],
                                                                                                        t11: ColWriter[T11],
                                                                                                        t12: ColWriter[T12],
                                                                                                        t13: ColWriter[T13],
                                                                                                        t14: ColWriter[T14],
                                                                                                        t15: ColWriter[T15],
                                                                                                        t16: ColWriter[T16],
                                                                                                        t17: ColWriter[T17],
                                                                                                        t18: ColWriter[T18]): ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)] =
    new ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)): Int = {
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
        parameterIndex + 18
      }
    }

  implicit def tuple19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](implicit t1: ColWriter[T1],
                                                                                                             t2: ColWriter[T2],
                                                                                                             t3: ColWriter[T3],
                                                                                                             t4: ColWriter[T4],
                                                                                                             t5: ColWriter[T5],
                                                                                                             t6: ColWriter[T6],
                                                                                                             t7: ColWriter[T7],
                                                                                                             t8: ColWriter[T8],
                                                                                                             t9: ColWriter[T9],
                                                                                                             t10: ColWriter[T10],
                                                                                                             t11: ColWriter[T11],
                                                                                                             t12: ColWriter[T12],
                                                                                                             t13: ColWriter[T13],
                                                                                                             t14: ColWriter[T14],
                                                                                                             t15: ColWriter[T15],
                                                                                                             t16: ColWriter[T16],
                                                                                                             t17: ColWriter[T17],
                                                                                                             t18: ColWriter[T18],
                                                                                                             t19: ColWriter[T19]): ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)] =
    new ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)): Int = {
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
        parameterIndex + 19
      }
    }

  implicit def tuple20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](implicit t1: ColWriter[T1],
                                                                                                                  t2: ColWriter[T2],
                                                                                                                  t3: ColWriter[T3],
                                                                                                                  t4: ColWriter[T4],
                                                                                                                  t5: ColWriter[T5],
                                                                                                                  t6: ColWriter[T6],
                                                                                                                  t7: ColWriter[T7],
                                                                                                                  t8: ColWriter[T8],
                                                                                                                  t9: ColWriter[T9],
                                                                                                                  t10: ColWriter[T10],
                                                                                                                  t11: ColWriter[T11],
                                                                                                                  t12: ColWriter[T12],
                                                                                                                  t13: ColWriter[T13],
                                                                                                                  t14: ColWriter[T14],
                                                                                                                  t15: ColWriter[T15],
                                                                                                                  t16: ColWriter[T16],
                                                                                                                  t17: ColWriter[T17],
                                                                                                                  t18: ColWriter[T18],
                                                                                                                  t19: ColWriter[T19],
                                                                                                                  t20: ColWriter[T20]): ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)] =
    new ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)): Int = {
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
        parameterIndex + 20
      }
    }

  implicit def tuple21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](implicit t1: ColWriter[T1],
                                                                                                                       t2: ColWriter[T2],
                                                                                                                       t3: ColWriter[T3],
                                                                                                                       t4: ColWriter[T4],
                                                                                                                       t5: ColWriter[T5],
                                                                                                                       t6: ColWriter[T6],
                                                                                                                       t7: ColWriter[T7],
                                                                                                                       t8: ColWriter[T8],
                                                                                                                       t9: ColWriter[T9],
                                                                                                                       t10: ColWriter[T10],
                                                                                                                       t11: ColWriter[T11],
                                                                                                                       t12: ColWriter[T12],
                                                                                                                       t13: ColWriter[T13],
                                                                                                                       t14: ColWriter[T14],
                                                                                                                       t15: ColWriter[T15],
                                                                                                                       t16: ColWriter[T16],
                                                                                                                       t17: ColWriter[T17],
                                                                                                                       t18: ColWriter[T18],
                                                                                                                       t19: ColWriter[T19],
                                                                                                                       t20: ColWriter[T20],
                                                                                                                       t21: ColWriter[T21]): ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)] =
    new ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)): Int = {
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
        parameterIndex + 21
      }
    }

  implicit def tuple22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](implicit t1: ColWriter[T1],
                                                                                                                            t2: ColWriter[T2],
                                                                                                                            t3: ColWriter[T3],
                                                                                                                            t4: ColWriter[T4],
                                                                                                                            t5: ColWriter[T5],
                                                                                                                            t6: ColWriter[T6],
                                                                                                                            t7: ColWriter[T7],
                                                                                                                            t8: ColWriter[T8],
                                                                                                                            t9: ColWriter[T9],
                                                                                                                            t10: ColWriter[T10],
                                                                                                                            t11: ColWriter[T11],
                                                                                                                            t12: ColWriter[T12],
                                                                                                                            t13: ColWriter[T13],
                                                                                                                            t14: ColWriter[T14],
                                                                                                                            t15: ColWriter[T15],
                                                                                                                            t16: ColWriter[T16],
                                                                                                                            t17: ColWriter[T17],
                                                                                                                            t18: ColWriter[T18],
                                                                                                                            t19: ColWriter[T19],
                                                                                                                            t20: ColWriter[T20],
                                                                                                                            t21: ColWriter[T21],
                                                                                                                            t22: ColWriter[T22]): ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)] =
    new ColWriter[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)] {
      override def apply(statement: PreparedStatement, parameterIndex: Int, paramValue: (T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)): Int = {
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
        parameterIndex + 22
      }
    }

}
