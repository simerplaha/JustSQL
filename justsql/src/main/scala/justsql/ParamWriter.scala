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

trait ParamWriter[PARAM] extends ((PreparedStatement, Int, PARAM) => Unit) {
  /** Total number of parameters this [[ParamWriter]] writes to a [[PreparedStatement]] */
  def paramCount(): Int

  override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: PARAM): Unit
}

trait OneParamWriter[P] extends ParamWriter[P] {

  final val paramCount: Int = 1

}

object ParamWriter {
  implicit val intParamWriter: OneParamWriter[Int] = _.setInt(_, _)
  implicit val stringParamWriter: OneParamWriter[String] = _.setString(_, _)
  implicit val booleanParamWriter: OneParamWriter[Boolean] = _.setBoolean(_, _)
  implicit val longParamWriter: OneParamWriter[Long] = _.setLong(_, _)
  implicit val byteParamWriter: OneParamWriter[Byte] = _.setByte(_, _)
  implicit val doubleParamWriter: OneParamWriter[Double] = _.setDouble(_, _)
  implicit val floatParamWriter: OneParamWriter[Float] = _.setFloat(_, _)
  implicit val shortParamWriter: OneParamWriter[Short] = _.setShort(_, _)
  implicit val bigDecimalParamWriter: OneParamWriter[java.math.BigDecimal] = _.setBigDecimal(_, _)
  implicit val byteArrayParamWriter: OneParamWriter[Array[Byte]] = _.setBytes(_, _)

  //java.sql types
  implicit val timestampParamWriter: OneParamWriter[java.sql.Timestamp] = _.setTimestamp(_, _)
  implicit val blobParamWriter: OneParamWriter[java.sql.Blob] = _.setBlob(_, _)
  implicit val clobParamWriter: OneParamWriter[java.sql.Clob] = _.setClob(_, _)
  implicit val dateParamWriter: OneParamWriter[java.sql.Date] = _.setDate(_, _)
  implicit val timeParamWriter: OneParamWriter[java.sql.Time] = _.setTime(_, _)
  implicit val refParamWriter: OneParamWriter[java.sql.Ref] = _.setRef(_, _)
  implicit val nClobParamWriter: OneParamWriter[java.sql.NClob] = _.setNClob(_, _)
  implicit val rowIdParamWriter: OneParamWriter[java.sql.RowId] = _.setRowId(_, _)
  implicit val sqlXMLParamWriter: OneParamWriter[java.sql.SQLXML] = _.setSQLXML(_, _)

  //other types
  implicit val characterStreamParamWriter: OneParamWriter[java.io.Reader] = _.setCharacterStream(_, _)
  implicit val binaryStreamParamWriter: OneParamWriter[java.io.InputStream] = _.setBinaryStream(_, _)
  implicit val urlParamWriter: OneParamWriter[java.net.URL] = _.setURL(_, _)

  implicit def tuple2[P1, P2](implicit p1: ParamWriter[P1],
                              p2: ParamWriter[P2]): ParamWriter[(P1, P2)] =
    new ParamWriter[(P1, P2)] {
      override def paramCount(): Int = 2

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
      }
    }

  implicit def tuple3[P1, P2, P3](implicit p1: ParamWriter[P1],
                                  p2: ParamWriter[P2],
                                  p3: ParamWriter[P3]): ParamWriter[(P1, P2, P3)] =
    new ParamWriter[(P1, P2, P3)] {
      override def paramCount(): Int = 3

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
      }
    }

  implicit def tuple4[P1, P2, P3, P4](implicit p1: ParamWriter[P1],
                                      p2: ParamWriter[P2],
                                      p3: ParamWriter[P3],
                                      p4: ParamWriter[P4]): ParamWriter[(P1, P2, P3, P4)] =
    new ParamWriter[(P1, P2, P3, P4)] {
      override def paramCount(): Int = 4

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3, P4)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
        p4(statement, paramIndex + 3, paramValue._4)
      }
    }

  implicit def tuple5[P1, P2, P3, P4, P5](implicit p1: ParamWriter[P1],
                                          p2: ParamWriter[P2],
                                          p3: ParamWriter[P3],
                                          p4: ParamWriter[P4],
                                          p5: ParamWriter[P5]): ParamWriter[(P1, P2, P3, P4, P5)] =
    new ParamWriter[(P1, P2, P3, P4, P5)] {
      override def paramCount(): Int = 5

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3, P4, P5)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
        p4(statement, paramIndex + 3, paramValue._4)
        p5(statement, paramIndex + 4, paramValue._5)
      }
    }

  implicit def tuple6[P1, P2, P3, P4, P5, P6](implicit p1: ParamWriter[P1],
                                              p2: ParamWriter[P2],
                                              p3: ParamWriter[P3],
                                              p4: ParamWriter[P4],
                                              p5: ParamWriter[P5],
                                              p6: ParamWriter[P6]): ParamWriter[(P1, P2, P3, P4, P5, P6)] =
    new ParamWriter[(P1, P2, P3, P4, P5, P6)] {
      override def paramCount(): Int = 6

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3, P4, P5, P6)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
        p4(statement, paramIndex + 3, paramValue._4)
        p5(statement, paramIndex + 4, paramValue._5)
        p6(statement, paramIndex + 5, paramValue._6)
      }
    }

  implicit def tuple7[P1, P2, P3, P4, P5, P6, P7](implicit p1: ParamWriter[P1],
                                                  p2: ParamWriter[P2],
                                                  p3: ParamWriter[P3],
                                                  p4: ParamWriter[P4],
                                                  p5: ParamWriter[P5],
                                                  p6: ParamWriter[P6],
                                                  p7: ParamWriter[P7]): ParamWriter[(P1, P2, P3, P4, P5, P6, P7)] =
    new ParamWriter[(P1, P2, P3, P4, P5, P6, P7)] {
      override def paramCount(): Int = 7

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3, P4, P5, P6, P7)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
        p4(statement, paramIndex + 3, paramValue._4)
        p5(statement, paramIndex + 4, paramValue._5)
        p6(statement, paramIndex + 5, paramValue._6)
        p7(statement, paramIndex + 6, paramValue._7)
      }
    }

  implicit def tuple8[P1, P2, P3, P4, P5, P6, P7, P8](implicit p1: ParamWriter[P1],
                                                      p2: ParamWriter[P2],
                                                      p3: ParamWriter[P3],
                                                      p4: ParamWriter[P4],
                                                      p5: ParamWriter[P5],
                                                      p6: ParamWriter[P6],
                                                      p7: ParamWriter[P7],
                                                      p8: ParamWriter[P8]): ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8)] =
    new ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8)] {
      override def paramCount(): Int = 8

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3, P4, P5, P6, P7, P8)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
        p4(statement, paramIndex + 3, paramValue._4)
        p5(statement, paramIndex + 4, paramValue._5)
        p6(statement, paramIndex + 5, paramValue._6)
        p7(statement, paramIndex + 6, paramValue._7)
        p8(statement, paramIndex + 7, paramValue._8)
      }
    }

  implicit def tuple9[P1, P2, P3, P4, P5, P6, P7, P8, P9](implicit p1: ParamWriter[P1],
                                                          p2: ParamWriter[P2],
                                                          p3: ParamWriter[P3],
                                                          p4: ParamWriter[P4],
                                                          p5: ParamWriter[P5],
                                                          p6: ParamWriter[P6],
                                                          p7: ParamWriter[P7],
                                                          p8: ParamWriter[P8],
                                                          p9: ParamWriter[P9]): ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9)] =
    new ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9)] {
      override def paramCount(): Int = 9

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3, P4, P5, P6, P7, P8, P9)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
        p4(statement, paramIndex + 3, paramValue._4)
        p5(statement, paramIndex + 4, paramValue._5)
        p6(statement, paramIndex + 5, paramValue._6)
        p7(statement, paramIndex + 6, paramValue._7)
        p8(statement, paramIndex + 7, paramValue._8)
        p9(statement, paramIndex + 8, paramValue._9)
      }
    }

  implicit def tuple10[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10](implicit p1: ParamWriter[P1],
                                                                p2: ParamWriter[P2],
                                                                p3: ParamWriter[P3],
                                                                p4: ParamWriter[P4],
                                                                p5: ParamWriter[P5],
                                                                p6: ParamWriter[P6],
                                                                p7: ParamWriter[P7],
                                                                p8: ParamWriter[P8],
                                                                p9: ParamWriter[P9],
                                                                p10: ParamWriter[P10]): ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10)] =
    new ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10)] {
      override def paramCount(): Int = 10

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
        p4(statement, paramIndex + 3, paramValue._4)
        p5(statement, paramIndex + 4, paramValue._5)
        p6(statement, paramIndex + 5, paramValue._6)
        p7(statement, paramIndex + 6, paramValue._7)
        p8(statement, paramIndex + 7, paramValue._8)
        p9(statement, paramIndex + 8, paramValue._9)
        p10(statement, paramIndex + 9, paramValue._10)
      }
    }

  implicit def tuple11[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11](implicit p1: ParamWriter[P1],
                                                                     p2: ParamWriter[P2],
                                                                     p3: ParamWriter[P3],
                                                                     p4: ParamWriter[P4],
                                                                     p5: ParamWriter[P5],
                                                                     p6: ParamWriter[P6],
                                                                     p7: ParamWriter[P7],
                                                                     p8: ParamWriter[P8],
                                                                     p9: ParamWriter[P9],
                                                                     p10: ParamWriter[P10],
                                                                     p11: ParamWriter[P11]): ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11)] =
    new ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11)] {
      override def paramCount(): Int = 11

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
        p4(statement, paramIndex + 3, paramValue._4)
        p5(statement, paramIndex + 4, paramValue._5)
        p6(statement, paramIndex + 5, paramValue._6)
        p7(statement, paramIndex + 6, paramValue._7)
        p8(statement, paramIndex + 7, paramValue._8)
        p9(statement, paramIndex + 8, paramValue._9)
        p10(statement, paramIndex + 9, paramValue._10)
        p11(statement, paramIndex + 10, paramValue._11)
      }
    }

  implicit def tuple12[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12](implicit p1: ParamWriter[P1],
                                                                          p2: ParamWriter[P2],
                                                                          p3: ParamWriter[P3],
                                                                          p4: ParamWriter[P4],
                                                                          p5: ParamWriter[P5],
                                                                          p6: ParamWriter[P6],
                                                                          p7: ParamWriter[P7],
                                                                          p8: ParamWriter[P8],
                                                                          p9: ParamWriter[P9],
                                                                          p10: ParamWriter[P10],
                                                                          p11: ParamWriter[P11],
                                                                          p12: ParamWriter[P12]): ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12)] =
    new ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12)] {
      override def paramCount(): Int = 12

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
        p4(statement, paramIndex + 3, paramValue._4)
        p5(statement, paramIndex + 4, paramValue._5)
        p6(statement, paramIndex + 5, paramValue._6)
        p7(statement, paramIndex + 6, paramValue._7)
        p8(statement, paramIndex + 7, paramValue._8)
        p9(statement, paramIndex + 8, paramValue._9)
        p10(statement, paramIndex + 9, paramValue._10)
        p11(statement, paramIndex + 10, paramValue._11)
        p12(statement, paramIndex + 11, paramValue._12)
      }
    }

  implicit def tuple13[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13](implicit p1: ParamWriter[P1],
                                                                               p2: ParamWriter[P2],
                                                                               p3: ParamWriter[P3],
                                                                               p4: ParamWriter[P4],
                                                                               p5: ParamWriter[P5],
                                                                               p6: ParamWriter[P6],
                                                                               p7: ParamWriter[P7],
                                                                               p8: ParamWriter[P8],
                                                                               p9: ParamWriter[P9],
                                                                               p10: ParamWriter[P10],
                                                                               p11: ParamWriter[P11],
                                                                               p12: ParamWriter[P12],
                                                                               p13: ParamWriter[P13]): ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13)] =
    new ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13)] {
      override def paramCount(): Int = 13

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
        p4(statement, paramIndex + 3, paramValue._4)
        p5(statement, paramIndex + 4, paramValue._5)
        p6(statement, paramIndex + 5, paramValue._6)
        p7(statement, paramIndex + 6, paramValue._7)
        p8(statement, paramIndex + 7, paramValue._8)
        p9(statement, paramIndex + 8, paramValue._9)
        p10(statement, paramIndex + 9, paramValue._10)
        p11(statement, paramIndex + 10, paramValue._11)
        p12(statement, paramIndex + 11, paramValue._12)
        p13(statement, paramIndex + 12, paramValue._13)
      }
    }

  implicit def tuple14[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14](implicit p1: ParamWriter[P1],
                                                                                    p2: ParamWriter[P2],
                                                                                    p3: ParamWriter[P3],
                                                                                    p4: ParamWriter[P4],
                                                                                    p5: ParamWriter[P5],
                                                                                    p6: ParamWriter[P6],
                                                                                    p7: ParamWriter[P7],
                                                                                    p8: ParamWriter[P8],
                                                                                    p9: ParamWriter[P9],
                                                                                    p10: ParamWriter[P10],
                                                                                    p11: ParamWriter[P11],
                                                                                    p12: ParamWriter[P12],
                                                                                    p13: ParamWriter[P13],
                                                                                    p14: ParamWriter[P14]): ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14)] =
    new ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14)] {
      override def paramCount(): Int = 14

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
        p4(statement, paramIndex + 3, paramValue._4)
        p5(statement, paramIndex + 4, paramValue._5)
        p6(statement, paramIndex + 5, paramValue._6)
        p7(statement, paramIndex + 6, paramValue._7)
        p8(statement, paramIndex + 7, paramValue._8)
        p9(statement, paramIndex + 8, paramValue._9)
        p10(statement, paramIndex + 9, paramValue._10)
        p11(statement, paramIndex + 10, paramValue._11)
        p12(statement, paramIndex + 11, paramValue._12)
        p13(statement, paramIndex + 12, paramValue._13)
        p14(statement, paramIndex + 13, paramValue._14)
      }
    }

  implicit def tuple15[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15](implicit p1: ParamWriter[P1],
                                                                                         p2: ParamWriter[P2],
                                                                                         p3: ParamWriter[P3],
                                                                                         p4: ParamWriter[P4],
                                                                                         p5: ParamWriter[P5],
                                                                                         p6: ParamWriter[P6],
                                                                                         p7: ParamWriter[P7],
                                                                                         p8: ParamWriter[P8],
                                                                                         p9: ParamWriter[P9],
                                                                                         p10: ParamWriter[P10],
                                                                                         p11: ParamWriter[P11],
                                                                                         p12: ParamWriter[P12],
                                                                                         p13: ParamWriter[P13],
                                                                                         p14: ParamWriter[P14],
                                                                                         p15: ParamWriter[P15]): ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15)] =
    new ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15)] {
      override def paramCount(): Int = 15

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
        p4(statement, paramIndex + 3, paramValue._4)
        p5(statement, paramIndex + 4, paramValue._5)
        p6(statement, paramIndex + 5, paramValue._6)
        p7(statement, paramIndex + 6, paramValue._7)
        p8(statement, paramIndex + 7, paramValue._8)
        p9(statement, paramIndex + 8, paramValue._9)
        p10(statement, paramIndex + 9, paramValue._10)
        p11(statement, paramIndex + 10, paramValue._11)
        p12(statement, paramIndex + 11, paramValue._12)
        p13(statement, paramIndex + 12, paramValue._13)
        p14(statement, paramIndex + 13, paramValue._14)
        p15(statement, paramIndex + 14, paramValue._15)
      }
    }

  implicit def tuple16[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16](implicit p1: ParamWriter[P1],
                                                                                              p2: ParamWriter[P2],
                                                                                              p3: ParamWriter[P3],
                                                                                              p4: ParamWriter[P4],
                                                                                              p5: ParamWriter[P5],
                                                                                              p6: ParamWriter[P6],
                                                                                              p7: ParamWriter[P7],
                                                                                              p8: ParamWriter[P8],
                                                                                              p9: ParamWriter[P9],
                                                                                              p10: ParamWriter[P10],
                                                                                              p11: ParamWriter[P11],
                                                                                              p12: ParamWriter[P12],
                                                                                              p13: ParamWriter[P13],
                                                                                              p14: ParamWriter[P14],
                                                                                              p15: ParamWriter[P15],
                                                                                              p16: ParamWriter[P16]): ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16)] =
    new ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16)] {
      override def paramCount(): Int = 16

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
        p4(statement, paramIndex + 3, paramValue._4)
        p5(statement, paramIndex + 4, paramValue._5)
        p6(statement, paramIndex + 5, paramValue._6)
        p7(statement, paramIndex + 6, paramValue._7)
        p8(statement, paramIndex + 7, paramValue._8)
        p9(statement, paramIndex + 8, paramValue._9)
        p10(statement, paramIndex + 9, paramValue._10)
        p11(statement, paramIndex + 10, paramValue._11)
        p12(statement, paramIndex + 11, paramValue._12)
        p13(statement, paramIndex + 12, paramValue._13)
        p14(statement, paramIndex + 13, paramValue._14)
        p15(statement, paramIndex + 14, paramValue._15)
        p16(statement, paramIndex + 15, paramValue._16)
      }
    }

  implicit def tuple17[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17](implicit p1: ParamWriter[P1],
                                                                                                   p2: ParamWriter[P2],
                                                                                                   p3: ParamWriter[P3],
                                                                                                   p4: ParamWriter[P4],
                                                                                                   p5: ParamWriter[P5],
                                                                                                   p6: ParamWriter[P6],
                                                                                                   p7: ParamWriter[P7],
                                                                                                   p8: ParamWriter[P8],
                                                                                                   p9: ParamWriter[P9],
                                                                                                   p10: ParamWriter[P10],
                                                                                                   p11: ParamWriter[P11],
                                                                                                   p12: ParamWriter[P12],
                                                                                                   p13: ParamWriter[P13],
                                                                                                   p14: ParamWriter[P14],
                                                                                                   p15: ParamWriter[P15],
                                                                                                   p16: ParamWriter[P16],
                                                                                                   p17: ParamWriter[P17]): ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17)] =
    new ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17)] {
      override def paramCount(): Int = 17

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
        p4(statement, paramIndex + 3, paramValue._4)
        p5(statement, paramIndex + 4, paramValue._5)
        p6(statement, paramIndex + 5, paramValue._6)
        p7(statement, paramIndex + 6, paramValue._7)
        p8(statement, paramIndex + 7, paramValue._8)
        p9(statement, paramIndex + 8, paramValue._9)
        p10(statement, paramIndex + 9, paramValue._10)
        p11(statement, paramIndex + 10, paramValue._11)
        p12(statement, paramIndex + 11, paramValue._12)
        p13(statement, paramIndex + 12, paramValue._13)
        p14(statement, paramIndex + 13, paramValue._14)
        p15(statement, paramIndex + 14, paramValue._15)
        p16(statement, paramIndex + 15, paramValue._16)
        p17(statement, paramIndex + 16, paramValue._17)
      }
    }

  implicit def tuple18[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18](implicit p1: ParamWriter[P1],
                                                                                                        p2: ParamWriter[P2],
                                                                                                        p3: ParamWriter[P3],
                                                                                                        p4: ParamWriter[P4],
                                                                                                        p5: ParamWriter[P5],
                                                                                                        p6: ParamWriter[P6],
                                                                                                        p7: ParamWriter[P7],
                                                                                                        p8: ParamWriter[P8],
                                                                                                        p9: ParamWriter[P9],
                                                                                                        p10: ParamWriter[P10],
                                                                                                        p11: ParamWriter[P11],
                                                                                                        p12: ParamWriter[P12],
                                                                                                        p13: ParamWriter[P13],
                                                                                                        p14: ParamWriter[P14],
                                                                                                        p15: ParamWriter[P15],
                                                                                                        p16: ParamWriter[P16],
                                                                                                        p17: ParamWriter[P17],
                                                                                                        p18: ParamWriter[P18]): ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18)] =
    new ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18)] {
      override def paramCount(): Int = 18

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
        p4(statement, paramIndex + 3, paramValue._4)
        p5(statement, paramIndex + 4, paramValue._5)
        p6(statement, paramIndex + 5, paramValue._6)
        p7(statement, paramIndex + 6, paramValue._7)
        p8(statement, paramIndex + 7, paramValue._8)
        p9(statement, paramIndex + 8, paramValue._9)
        p10(statement, paramIndex + 9, paramValue._10)
        p11(statement, paramIndex + 10, paramValue._11)
        p12(statement, paramIndex + 11, paramValue._12)
        p13(statement, paramIndex + 12, paramValue._13)
        p14(statement, paramIndex + 13, paramValue._14)
        p15(statement, paramIndex + 14, paramValue._15)
        p16(statement, paramIndex + 15, paramValue._16)
        p17(statement, paramIndex + 16, paramValue._17)
        p18(statement, paramIndex + 17, paramValue._18)
      }
    }

  implicit def tuple19[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19](implicit p1: ParamWriter[P1],
                                                                                                             p2: ParamWriter[P2],
                                                                                                             p3: ParamWriter[P3],
                                                                                                             p4: ParamWriter[P4],
                                                                                                             p5: ParamWriter[P5],
                                                                                                             p6: ParamWriter[P6],
                                                                                                             p7: ParamWriter[P7],
                                                                                                             p8: ParamWriter[P8],
                                                                                                             p9: ParamWriter[P9],
                                                                                                             p10: ParamWriter[P10],
                                                                                                             p11: ParamWriter[P11],
                                                                                                             p12: ParamWriter[P12],
                                                                                                             p13: ParamWriter[P13],
                                                                                                             p14: ParamWriter[P14],
                                                                                                             p15: ParamWriter[P15],
                                                                                                             p16: ParamWriter[P16],
                                                                                                             p17: ParamWriter[P17],
                                                                                                             p18: ParamWriter[P18],
                                                                                                             p19: ParamWriter[P19]): ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19)] =
    new ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19)] {
      override def paramCount(): Int = 19

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
        p4(statement, paramIndex + 3, paramValue._4)
        p5(statement, paramIndex + 4, paramValue._5)
        p6(statement, paramIndex + 5, paramValue._6)
        p7(statement, paramIndex + 6, paramValue._7)
        p8(statement, paramIndex + 7, paramValue._8)
        p9(statement, paramIndex + 8, paramValue._9)
        p10(statement, paramIndex + 9, paramValue._10)
        p11(statement, paramIndex + 10, paramValue._11)
        p12(statement, paramIndex + 11, paramValue._12)
        p13(statement, paramIndex + 12, paramValue._13)
        p14(statement, paramIndex + 13, paramValue._14)
        p15(statement, paramIndex + 14, paramValue._15)
        p16(statement, paramIndex + 15, paramValue._16)
        p17(statement, paramIndex + 16, paramValue._17)
        p18(statement, paramIndex + 17, paramValue._18)
        p19(statement, paramIndex + 18, paramValue._19)
      }
    }

  implicit def tuple20[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20](implicit p1: ParamWriter[P1],
                                                                                                                  p2: ParamWriter[P2],
                                                                                                                  p3: ParamWriter[P3],
                                                                                                                  p4: ParamWriter[P4],
                                                                                                                  p5: ParamWriter[P5],
                                                                                                                  p6: ParamWriter[P6],
                                                                                                                  p7: ParamWriter[P7],
                                                                                                                  p8: ParamWriter[P8],
                                                                                                                  p9: ParamWriter[P9],
                                                                                                                  p10: ParamWriter[P10],
                                                                                                                  p11: ParamWriter[P11],
                                                                                                                  p12: ParamWriter[P12],
                                                                                                                  p13: ParamWriter[P13],
                                                                                                                  p14: ParamWriter[P14],
                                                                                                                  p15: ParamWriter[P15],
                                                                                                                  p16: ParamWriter[P16],
                                                                                                                  p17: ParamWriter[P17],
                                                                                                                  p18: ParamWriter[P18],
                                                                                                                  p19: ParamWriter[P19],
                                                                                                                  p20: ParamWriter[P20]): ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20)] =
    new ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20)] {
      override def paramCount(): Int = 20

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
        p4(statement, paramIndex + 3, paramValue._4)
        p5(statement, paramIndex + 4, paramValue._5)
        p6(statement, paramIndex + 5, paramValue._6)
        p7(statement, paramIndex + 6, paramValue._7)
        p8(statement, paramIndex + 7, paramValue._8)
        p9(statement, paramIndex + 8, paramValue._9)
        p10(statement, paramIndex + 9, paramValue._10)
        p11(statement, paramIndex + 10, paramValue._11)
        p12(statement, paramIndex + 11, paramValue._12)
        p13(statement, paramIndex + 12, paramValue._13)
        p14(statement, paramIndex + 13, paramValue._14)
        p15(statement, paramIndex + 14, paramValue._15)
        p16(statement, paramIndex + 15, paramValue._16)
        p17(statement, paramIndex + 16, paramValue._17)
        p18(statement, paramIndex + 17, paramValue._18)
        p19(statement, paramIndex + 18, paramValue._19)
        p20(statement, paramIndex + 19, paramValue._20)
      }
    }

  implicit def tuple21[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21](implicit p1: ParamWriter[P1],
                                                                                                                       p2: ParamWriter[P2],
                                                                                                                       p3: ParamWriter[P3],
                                                                                                                       p4: ParamWriter[P4],
                                                                                                                       p5: ParamWriter[P5],
                                                                                                                       p6: ParamWriter[P6],
                                                                                                                       p7: ParamWriter[P7],
                                                                                                                       p8: ParamWriter[P8],
                                                                                                                       p9: ParamWriter[P9],
                                                                                                                       p10: ParamWriter[P10],
                                                                                                                       p11: ParamWriter[P11],
                                                                                                                       p12: ParamWriter[P12],
                                                                                                                       p13: ParamWriter[P13],
                                                                                                                       p14: ParamWriter[P14],
                                                                                                                       p15: ParamWriter[P15],
                                                                                                                       p16: ParamWriter[P16],
                                                                                                                       p17: ParamWriter[P17],
                                                                                                                       p18: ParamWriter[P18],
                                                                                                                       p19: ParamWriter[P19],
                                                                                                                       p20: ParamWriter[P20],
                                                                                                                       p21: ParamWriter[P21]): ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21)] =
    new ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21)] {
      override def paramCount(): Int = 21

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
        p4(statement, paramIndex + 3, paramValue._4)
        p5(statement, paramIndex + 4, paramValue._5)
        p6(statement, paramIndex + 5, paramValue._6)
        p7(statement, paramIndex + 6, paramValue._7)
        p8(statement, paramIndex + 7, paramValue._8)
        p9(statement, paramIndex + 8, paramValue._9)
        p10(statement, paramIndex + 9, paramValue._10)
        p11(statement, paramIndex + 10, paramValue._11)
        p12(statement, paramIndex + 11, paramValue._12)
        p13(statement, paramIndex + 12, paramValue._13)
        p14(statement, paramIndex + 13, paramValue._14)
        p15(statement, paramIndex + 14, paramValue._15)
        p16(statement, paramIndex + 15, paramValue._16)
        p17(statement, paramIndex + 16, paramValue._17)
        p18(statement, paramIndex + 17, paramValue._18)
        p19(statement, paramIndex + 18, paramValue._19)
        p20(statement, paramIndex + 19, paramValue._20)
        p21(statement, paramIndex + 20, paramValue._21)
      }
    }

  implicit def tuple22[P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21, P22](implicit p1: ParamWriter[P1],
                                                                                                                            p2: ParamWriter[P2],
                                                                                                                            p3: ParamWriter[P3],
                                                                                                                            p4: ParamWriter[P4],
                                                                                                                            p5: ParamWriter[P5],
                                                                                                                            p6: ParamWriter[P6],
                                                                                                                            p7: ParamWriter[P7],
                                                                                                                            p8: ParamWriter[P8],
                                                                                                                            p9: ParamWriter[P9],
                                                                                                                            p10: ParamWriter[P10],
                                                                                                                            p11: ParamWriter[P11],
                                                                                                                            p12: ParamWriter[P12],
                                                                                                                            p13: ParamWriter[P13],
                                                                                                                            p14: ParamWriter[P14],
                                                                                                                            p15: ParamWriter[P15],
                                                                                                                            p16: ParamWriter[P16],
                                                                                                                            p17: ParamWriter[P17],
                                                                                                                            p18: ParamWriter[P18],
                                                                                                                            p19: ParamWriter[P19],
                                                                                                                            p20: ParamWriter[P20],
                                                                                                                            p21: ParamWriter[P21],
                                                                                                                            p22: ParamWriter[P22]): ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21, P22)] =
    new ParamWriter[(P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21, P22)] {
      override def paramCount(): Int = 22

      override def apply(statement: PreparedStatement, paramIndex: Int, paramValue: (P1, P2, P3, P4, P5, P6, P7, P8, P9, P10, P11, P12, P13, P14, P15, P16, P17, P18, P19, P20, P21, P22)): Unit = {
        p1(statement, paramIndex, paramValue._1)
        p2(statement, paramIndex + 1, paramValue._2)
        p3(statement, paramIndex + 2, paramValue._3)
        p4(statement, paramIndex + 3, paramValue._4)
        p5(statement, paramIndex + 4, paramValue._5)
        p6(statement, paramIndex + 5, paramValue._6)
        p7(statement, paramIndex + 6, paramValue._7)
        p8(statement, paramIndex + 7, paramValue._8)
        p9(statement, paramIndex + 8, paramValue._9)
        p10(statement, paramIndex + 9, paramValue._10)
        p11(statement, paramIndex + 10, paramValue._11)
        p12(statement, paramIndex + 11, paramValue._12)
        p13(statement, paramIndex + 12, paramValue._13)
        p14(statement, paramIndex + 13, paramValue._14)
        p15(statement, paramIndex + 14, paramValue._15)
        p16(statement, paramIndex + 15, paramValue._16)
        p17(statement, paramIndex + 16, paramValue._17)
        p18(statement, paramIndex + 17, paramValue._18)
        p19(statement, paramIndex + 18, paramValue._19)
        p20(statement, paramIndex + 19, paramValue._20)
        p21(statement, paramIndex + 20, paramValue._21)
        p22(statement, paramIndex + 21, paramValue._22)
      }
    }
}
