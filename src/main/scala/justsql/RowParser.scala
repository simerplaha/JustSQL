package justsql


import java.sql.ResultSet

sealed trait RowParser[ROW] extends Function[ResultSet, ROW] {
  def apply(resultSet: ResultSet): ROW
}

object RowParser {

  //primitive types
  implicit val intRowParser: RowParser[Int] = tuple1[Int]
  implicit val stringRowParser: RowParser[String] = tuple1[String]
  implicit val booleanRowParser: RowParser[Boolean] = tuple1[Boolean]
  implicit val longRowParser: RowParser[Long] = tuple1[Long]
  implicit val byteRowParser: RowParser[Byte] = tuple1[Byte]
  implicit val doubleRowParser: RowParser[Double] = tuple1[Double]
  implicit val floatRowParser: RowParser[Float] = tuple1[Float]
  implicit val shortRowParser: RowParser[Short] = tuple1[Short]
  implicit val bigDecimalRowParser: RowParser[BigDecimal] = tuple1[BigDecimal]
  implicit val byteArrayRowParser: RowParser[Array[Byte]] = tuple1[Array[Byte]]

  //java.sql types
  implicit val timestampRowParser: RowParser[java.sql.Timestamp] = tuple1[java.sql.Timestamp]
  implicit val blobRowParser: RowParser[java.sql.Blob] = tuple1[java.sql.Blob]
  implicit val clobRowParser: RowParser[java.sql.Clob] = tuple1[java.sql.Clob]
  implicit val dateRowParser: RowParser[java.sql.Date] = tuple1[java.sql.Date]
  implicit val timeRowParser: RowParser[java.sql.Time] = tuple1[java.sql.Time]
  implicit val refRowParser: RowParser[java.sql.Ref] = tuple1[java.sql.Ref]
  implicit val nClobRowParser: RowParser[java.sql.NClob] = tuple1[java.sql.NClob]
  implicit val rowIdRowParser: RowParser[java.sql.RowId] = tuple1[java.sql.RowId]
  implicit val sqlXMLRowParser: RowParser[java.sql.SQLXML] = tuple1[java.sql.SQLXML]

  //other types
  implicit val ioReaderRowParser: RowParser[java.io.Reader] = tuple1[java.io.Reader]
  implicit val ioInputStreamRowParser: RowParser[java.io.InputStream] = tuple1[java.io.InputStream]
  implicit val urlRowParser: RowParser[java.net.URL] = tuple1[java.net.URL]

  implicit def tuple1[T1](implicit t1: ColParser[T1]): RowParser[T1] =
    new RowParser[T1] {
      def apply(resultSet: ResultSet): T1 =
        t1(resultSet, 1)
    }

  implicit def tuple2[T1, T2](implicit t1: ColParser[T1],
                              t2: ColParser[T2]): RowParser[(T1, T2)] =
    new RowParser[(T1, T2)] {
      def apply(resultSet: ResultSet): (T1, T2) =
        (t1(resultSet, 1),
          t2(resultSet, 2))
    }

  implicit def tuple3[T1, T2, T3](implicit t1: ColParser[T1],
                                  t2: ColParser[T2],
                                  t3: ColParser[T3]): RowParser[(T1, T2, T3)] =
    new RowParser[(T1, T2, T3)] {
      def apply(resultSet: ResultSet): (T1, T2, T3) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3))
    }

  implicit def tuple4[T1, T2, T3, T4](implicit t1: ColParser[T1],
                                      t2: ColParser[T2],
                                      t3: ColParser[T3],
                                      t4: ColParser[T4]): RowParser[(T1, T2, T3, T4)] =
    new RowParser[(T1, T2, T3, T4)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4))
    }

  implicit def tuple5[T1, T2, T3, T4, T5](implicit t1: ColParser[T1],
                                          t2: ColParser[T2],
                                          t3: ColParser[T3],
                                          t4: ColParser[T4],
                                          t5: ColParser[T5]): RowParser[(T1, T2, T3, T4, T5)] =
    new RowParser[(T1, T2, T3, T4, T5)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5))
    }

  implicit def tuple6[T1, T2, T3, T4, T5, T6](implicit t1: ColParser[T1],
                                              t2: ColParser[T2],
                                              t3: ColParser[T3],
                                              t4: ColParser[T4],
                                              t5: ColParser[T5],
                                              t6: ColParser[T6]): RowParser[(T1, T2, T3, T4, T5, T6)] =
    new RowParser[(T1, T2, T3, T4, T5, T6)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5, T6) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6))
    }

  implicit def tuple7[T1, T2, T3, T4, T5, T6, T7](implicit t1: ColParser[T1],
                                                  t2: ColParser[T2],
                                                  t3: ColParser[T3],
                                                  t4: ColParser[T4],
                                                  t5: ColParser[T5],
                                                  t6: ColParser[T6],
                                                  t7: ColParser[T7]): RowParser[(T1, T2, T3, T4, T5, T6, T7)] =
    new RowParser[(T1, T2, T3, T4, T5, T6, T7)] {
      def apply(resultSet: ResultSet): (T1, T2, T3, T4, T5, T6, T7) =
        (t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7))
    }

  implicit def tuple8[T1, T2, T3, T4, T5, T6, T7, T8](implicit t1: ColParser[T1],
                                                      t2: ColParser[T2],
                                                      t3: ColParser[T3],
                                                      t4: ColParser[T4],
                                                      t5: ColParser[T5],
                                                      t6: ColParser[T6],
                                                      t7: ColParser[T7],
                                                      t8: ColParser[T8]): RowParser[(T1, T2, T3, T4, T5, T6, T7, T8)] =
    new RowParser[(T1, T2, T3, T4, T5, T6, T7, T8)] {
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

  implicit def tuple9[T1, T2, T3, T4, T5, T6, T7, T8, T9](implicit t1: ColParser[T1],
                                                          t2: ColParser[T2],
                                                          t3: ColParser[T3],
                                                          t4: ColParser[T4],
                                                          t5: ColParser[T5],
                                                          t6: ColParser[T6],
                                                          t7: ColParser[T7],
                                                          t8: ColParser[T8],
                                                          t9: ColParser[T9]): RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9)] =
    new RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9)] {
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

  implicit def tuple10[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10](implicit t1: ColParser[T1],
                                                                t2: ColParser[T2],
                                                                t3: ColParser[T3],
                                                                t4: ColParser[T4],
                                                                t5: ColParser[T5],
                                                                t6: ColParser[T6],
                                                                t7: ColParser[T7],
                                                                t8: ColParser[T8],
                                                                t9: ColParser[T9],
                                                                t10: ColParser[T10]): RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)] =
    new RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)] {
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

  implicit def tuple11[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11](implicit t1: ColParser[T1],
                                                                     t2: ColParser[T2],
                                                                     t3: ColParser[T3],
                                                                     t4: ColParser[T4],
                                                                     t5: ColParser[T5],
                                                                     t6: ColParser[T6],
                                                                     t7: ColParser[T7],
                                                                     t8: ColParser[T8],
                                                                     t9: ColParser[T9],
                                                                     t10: ColParser[T10],
                                                                     t11: ColParser[T11]): RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)] =
    new RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)] {
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

  implicit def tuple12[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12](implicit t1: ColParser[T1],
                                                                          t2: ColParser[T2],
                                                                          t3: ColParser[T3],
                                                                          t4: ColParser[T4],
                                                                          t5: ColParser[T5],
                                                                          t6: ColParser[T6],
                                                                          t7: ColParser[T7],
                                                                          t8: ColParser[T8],
                                                                          t9: ColParser[T9],
                                                                          t10: ColParser[T10],
                                                                          t11: ColParser[T11],
                                                                          t12: ColParser[T12]): RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)] =
    new RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)] {
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

  implicit def tuple13[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13](implicit t1: ColParser[T1],
                                                                               t2: ColParser[T2],
                                                                               t3: ColParser[T3],
                                                                               t4: ColParser[T4],
                                                                               t5: ColParser[T5],
                                                                               t6: ColParser[T6],
                                                                               t7: ColParser[T7],
                                                                               t8: ColParser[T8],
                                                                               t9: ColParser[T9],
                                                                               t10: ColParser[T10],
                                                                               t11: ColParser[T11],
                                                                               t12: ColParser[T12],
                                                                               t13: ColParser[T13]): RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)] =
    new RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)] {
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

  implicit def tuple14[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14](implicit t1: ColParser[T1],
                                                                                    t2: ColParser[T2],
                                                                                    t3: ColParser[T3],
                                                                                    t4: ColParser[T4],
                                                                                    t5: ColParser[T5],
                                                                                    t6: ColParser[T6],
                                                                                    t7: ColParser[T7],
                                                                                    t8: ColParser[T8],
                                                                                    t9: ColParser[T9],
                                                                                    t10: ColParser[T10],
                                                                                    t11: ColParser[T11],
                                                                                    t12: ColParser[T12],
                                                                                    t13: ColParser[T13],
                                                                                    t14: ColParser[T14]): RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)] =
    new RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)] {
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

  implicit def tuple15[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15](implicit t1: ColParser[T1],
                                                                                         t2: ColParser[T2],
                                                                                         t3: ColParser[T3],
                                                                                         t4: ColParser[T4],
                                                                                         t5: ColParser[T5],
                                                                                         t6: ColParser[T6],
                                                                                         t7: ColParser[T7],
                                                                                         t8: ColParser[T8],
                                                                                         t9: ColParser[T9],
                                                                                         t10: ColParser[T10],
                                                                                         t11: ColParser[T11],
                                                                                         t12: ColParser[T12],
                                                                                         t13: ColParser[T13],
                                                                                         t14: ColParser[T14],
                                                                                         t15: ColParser[T15]): RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)] =
    new RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)] {
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

  implicit def tuple16[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16](implicit t1: ColParser[T1],
                                                                                              t2: ColParser[T2],
                                                                                              t3: ColParser[T3],
                                                                                              t4: ColParser[T4],
                                                                                              t5: ColParser[T5],
                                                                                              t6: ColParser[T6],
                                                                                              t7: ColParser[T7],
                                                                                              t8: ColParser[T8],
                                                                                              t9: ColParser[T9],
                                                                                              t10: ColParser[T10],
                                                                                              t11: ColParser[T11],
                                                                                              t12: ColParser[T12],
                                                                                              t13: ColParser[T13],
                                                                                              t14: ColParser[T14],
                                                                                              t15: ColParser[T15],
                                                                                              t16: ColParser[T16]): RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)] =
    new RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)] {
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

  implicit def tuple17[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17](implicit t1: ColParser[T1],
                                                                                                   t2: ColParser[T2],
                                                                                                   t3: ColParser[T3],
                                                                                                   t4: ColParser[T4],
                                                                                                   t5: ColParser[T5],
                                                                                                   t6: ColParser[T6],
                                                                                                   t7: ColParser[T7],
                                                                                                   t8: ColParser[T8],
                                                                                                   t9: ColParser[T9],
                                                                                                   t10: ColParser[T10],
                                                                                                   t11: ColParser[T11],
                                                                                                   t12: ColParser[T12],
                                                                                                   t13: ColParser[T13],
                                                                                                   t14: ColParser[T14],
                                                                                                   t15: ColParser[T15],
                                                                                                   t16: ColParser[T16],
                                                                                                   t17: ColParser[T17]): RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)] =
    new RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)] {
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

  implicit def tuple18[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18](implicit t1: ColParser[T1],
                                                                                                        t2: ColParser[T2],
                                                                                                        t3: ColParser[T3],
                                                                                                        t4: ColParser[T4],
                                                                                                        t5: ColParser[T5],
                                                                                                        t6: ColParser[T6],
                                                                                                        t7: ColParser[T7],
                                                                                                        t8: ColParser[T8],
                                                                                                        t9: ColParser[T9],
                                                                                                        t10: ColParser[T10],
                                                                                                        t11: ColParser[T11],
                                                                                                        t12: ColParser[T12],
                                                                                                        t13: ColParser[T13],
                                                                                                        t14: ColParser[T14],
                                                                                                        t15: ColParser[T15],
                                                                                                        t16: ColParser[T16],
                                                                                                        t17: ColParser[T17],
                                                                                                        t18: ColParser[T18]): RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)] =
    new RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)] {
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

  implicit def tuple19[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19](implicit t1: ColParser[T1],
                                                                                                             t2: ColParser[T2],
                                                                                                             t3: ColParser[T3],
                                                                                                             t4: ColParser[T4],
                                                                                                             t5: ColParser[T5],
                                                                                                             t6: ColParser[T6],
                                                                                                             t7: ColParser[T7],
                                                                                                             t8: ColParser[T8],
                                                                                                             t9: ColParser[T9],
                                                                                                             t10: ColParser[T10],
                                                                                                             t11: ColParser[T11],
                                                                                                             t12: ColParser[T12],
                                                                                                             t13: ColParser[T13],
                                                                                                             t14: ColParser[T14],
                                                                                                             t15: ColParser[T15],
                                                                                                             t16: ColParser[T16],
                                                                                                             t17: ColParser[T17],
                                                                                                             t18: ColParser[T18],
                                                                                                             t19: ColParser[T19]): RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)] =
    new RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)] {
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

  implicit def tuple20[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20](implicit t1: ColParser[T1],
                                                                                                                  t2: ColParser[T2],
                                                                                                                  t3: ColParser[T3],
                                                                                                                  t4: ColParser[T4],
                                                                                                                  t5: ColParser[T5],
                                                                                                                  t6: ColParser[T6],
                                                                                                                  t7: ColParser[T7],
                                                                                                                  t8: ColParser[T8],
                                                                                                                  t9: ColParser[T9],
                                                                                                                  t10: ColParser[T10],
                                                                                                                  t11: ColParser[T11],
                                                                                                                  t12: ColParser[T12],
                                                                                                                  t13: ColParser[T13],
                                                                                                                  t14: ColParser[T14],
                                                                                                                  t15: ColParser[T15],
                                                                                                                  t16: ColParser[T16],
                                                                                                                  t17: ColParser[T17],
                                                                                                                  t18: ColParser[T18],
                                                                                                                  t19: ColParser[T19],
                                                                                                                  t20: ColParser[T20]): RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)] =
    new RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)] {
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

  implicit def tuple21[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21](implicit t1: ColParser[T1],
                                                                                                                       t2: ColParser[T2],
                                                                                                                       t3: ColParser[T3],
                                                                                                                       t4: ColParser[T4],
                                                                                                                       t5: ColParser[T5],
                                                                                                                       t6: ColParser[T6],
                                                                                                                       t7: ColParser[T7],
                                                                                                                       t8: ColParser[T8],
                                                                                                                       t9: ColParser[T9],
                                                                                                                       t10: ColParser[T10],
                                                                                                                       t11: ColParser[T11],
                                                                                                                       t12: ColParser[T12],
                                                                                                                       t13: ColParser[T13],
                                                                                                                       t14: ColParser[T14],
                                                                                                                       t15: ColParser[T15],
                                                                                                                       t16: ColParser[T16],
                                                                                                                       t17: ColParser[T17],
                                                                                                                       t18: ColParser[T18],
                                                                                                                       t19: ColParser[T19],
                                                                                                                       t20: ColParser[T20],
                                                                                                                       t21: ColParser[T21]): RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)] =
    new RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)] {
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

  implicit def tuple22[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22](implicit t1: ColParser[T1],
                                                                                                                            t2: ColParser[T2],
                                                                                                                            t3: ColParser[T3],
                                                                                                                            t4: ColParser[T4],
                                                                                                                            t5: ColParser[T5],
                                                                                                                            t6: ColParser[T6],
                                                                                                                            t7: ColParser[T7],
                                                                                                                            t8: ColParser[T8],
                                                                                                                            t9: ColParser[T9],
                                                                                                                            t10: ColParser[T10],
                                                                                                                            t11: ColParser[T11],
                                                                                                                            t12: ColParser[T12],
                                                                                                                            t13: ColParser[T13],
                                                                                                                            t14: ColParser[T14],
                                                                                                                            t15: ColParser[T15],
                                                                                                                            t16: ColParser[T16],
                                                                                                                            t17: ColParser[T17],
                                                                                                                            t18: ColParser[T18],
                                                                                                                            t19: ColParser[T19],
                                                                                                                            t20: ColParser[T20],
                                                                                                                            t21: ColParser[T21],
                                                                                                                            t22: ColParser[T22]): RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)] =
    new RowParser[(T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)] {
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

  def apply[T1, R](f: T1 => R)(implicit t1: ColParser[T1]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1))
    }

  def apply[T1, T2, R](f: ((T1, T2)) => R)(implicit t1: ColParser[T1],
                                           t2: ColParser[T2]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
          t2(resultSet, 2))
    }

  def apply[T1, T2, T3, R](f: ((T1, T2, T3)) => R)(implicit t1: ColParser[T1],
                                                   t2: ColParser[T2],
                                                   t3: ColParser[T3]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3))
    }

  def apply[T1, T2, T3, T4, R](f: ((T1, T2, T3, T4)) => R)(implicit t1: ColParser[T1],
                                                           t2: ColParser[T2],
                                                           t3: ColParser[T3],
                                                           t4: ColParser[T4]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4))
    }

  def apply[T1, T2, T3, T4, T5, R](f: ((T1, T2, T3, T4, T5)) => R)(implicit t1: ColParser[T1],
                                                                   t2: ColParser[T2],
                                                                   t3: ColParser[T3],
                                                                   t4: ColParser[T4],
                                                                   t5: ColParser[T5]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5))
    }

  def apply[T1, T2, T3, T4, T5, T6, R](f: ((T1, T2, T3, T4, T5, T6)) => R)(implicit t1: ColParser[T1],
                                                                           t2: ColParser[T2],
                                                                           t3: ColParser[T3],
                                                                           t4: ColParser[T4],
                                                                           t5: ColParser[T5],
                                                                           t6: ColParser[T6]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, R](f: ((T1, T2, T3, T4, T5, T6, T7)) => R)(implicit t1: ColParser[T1],
                                                                                   t2: ColParser[T2],
                                                                                   t3: ColParser[T3],
                                                                                   t4: ColParser[T4],
                                                                                   t5: ColParser[T5],
                                                                                   t6: ColParser[T6],
                                                                                   t7: ColParser[T7]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8)) => R)(implicit t1: ColParser[T1],
                                                                                           t2: ColParser[T2],
                                                                                           t3: ColParser[T3],
                                                                                           t4: ColParser[T4],
                                                                                           t5: ColParser[T5],
                                                                                           t6: ColParser[T6],
                                                                                           t7: ColParser[T7],
                                                                                           t8: ColParser[T8]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9)) => R)(implicit t1: ColParser[T1],
                                                                                                   t2: ColParser[T2],
                                                                                                   t3: ColParser[T3],
                                                                                                   t4: ColParser[T4],
                                                                                                   t5: ColParser[T5],
                                                                                                   t6: ColParser[T6],
                                                                                                   t7: ColParser[T7],
                                                                                                   t8: ColParser[T8],
                                                                                                   t9: ColParser[T9]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
          t2(resultSet, 2),
          t3(resultSet, 3),
          t4(resultSet, 4),
          t5(resultSet, 5),
          t6(resultSet, 6),
          t7(resultSet, 7),
          t8(resultSet, 8),
          t9(resultSet, 9))
    }

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10)) => R)(implicit t1: ColParser[T1],
                                                                                                             t2: ColParser[T2],
                                                                                                             t3: ColParser[T3],
                                                                                                             t4: ColParser[T4],
                                                                                                             t5: ColParser[T5],
                                                                                                             t6: ColParser[T6],
                                                                                                             t7: ColParser[T7],
                                                                                                             t8: ColParser[T8],
                                                                                                             t9: ColParser[T9],
                                                                                                             t10: ColParser[T10]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
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

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11)) => R)(implicit t1: ColParser[T1],
                                                                                                                       t2: ColParser[T2],
                                                                                                                       t3: ColParser[T3],
                                                                                                                       t4: ColParser[T4],
                                                                                                                       t5: ColParser[T5],
                                                                                                                       t6: ColParser[T6],
                                                                                                                       t7: ColParser[T7],
                                                                                                                       t8: ColParser[T8],
                                                                                                                       t9: ColParser[T9],
                                                                                                                       t10: ColParser[T10],
                                                                                                                       t11: ColParser[T11]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
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

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12)) => R)(implicit t1: ColParser[T1],
                                                                                                                                 t2: ColParser[T2],
                                                                                                                                 t3: ColParser[T3],
                                                                                                                                 t4: ColParser[T4],
                                                                                                                                 t5: ColParser[T5],
                                                                                                                                 t6: ColParser[T6],
                                                                                                                                 t7: ColParser[T7],
                                                                                                                                 t8: ColParser[T8],
                                                                                                                                 t9: ColParser[T9],
                                                                                                                                 t10: ColParser[T10],
                                                                                                                                 t11: ColParser[T11],
                                                                                                                                 t12: ColParser[T12]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
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

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13)) => R)(implicit t1: ColParser[T1],
                                                                                                                                           t2: ColParser[T2],
                                                                                                                                           t3: ColParser[T3],
                                                                                                                                           t4: ColParser[T4],
                                                                                                                                           t5: ColParser[T5],
                                                                                                                                           t6: ColParser[T6],
                                                                                                                                           t7: ColParser[T7],
                                                                                                                                           t8: ColParser[T8],
                                                                                                                                           t9: ColParser[T9],
                                                                                                                                           t10: ColParser[T10],
                                                                                                                                           t11: ColParser[T11],
                                                                                                                                           t12: ColParser[T12],
                                                                                                                                           t13: ColParser[T13]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
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

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14)) => R)(implicit t1: ColParser[T1],
                                                                                                                                                     t2: ColParser[T2],
                                                                                                                                                     t3: ColParser[T3],
                                                                                                                                                     t4: ColParser[T4],
                                                                                                                                                     t5: ColParser[T5],
                                                                                                                                                     t6: ColParser[T6],
                                                                                                                                                     t7: ColParser[T7],
                                                                                                                                                     t8: ColParser[T8],
                                                                                                                                                     t9: ColParser[T9],
                                                                                                                                                     t10: ColParser[T10],
                                                                                                                                                     t11: ColParser[T11],
                                                                                                                                                     t12: ColParser[T12],
                                                                                                                                                     t13: ColParser[T13],
                                                                                                                                                     t14: ColParser[T14]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
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

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15)) => R)(implicit t1: ColParser[T1],
                                                                                                                                                               t2: ColParser[T2],
                                                                                                                                                               t3: ColParser[T3],
                                                                                                                                                               t4: ColParser[T4],
                                                                                                                                                               t5: ColParser[T5],
                                                                                                                                                               t6: ColParser[T6],
                                                                                                                                                               t7: ColParser[T7],
                                                                                                                                                               t8: ColParser[T8],
                                                                                                                                                               t9: ColParser[T9],
                                                                                                                                                               t10: ColParser[T10],
                                                                                                                                                               t11: ColParser[T11],
                                                                                                                                                               t12: ColParser[T12],
                                                                                                                                                               t13: ColParser[T13],
                                                                                                                                                               t14: ColParser[T14],
                                                                                                                                                               t15: ColParser[T15]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
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

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16)) => R)(implicit t1: ColParser[T1],
                                                                                                                                                                         t2: ColParser[T2],
                                                                                                                                                                         t3: ColParser[T3],
                                                                                                                                                                         t4: ColParser[T4],
                                                                                                                                                                         t5: ColParser[T5],
                                                                                                                                                                         t6: ColParser[T6],
                                                                                                                                                                         t7: ColParser[T7],
                                                                                                                                                                         t8: ColParser[T8],
                                                                                                                                                                         t9: ColParser[T9],
                                                                                                                                                                         t10: ColParser[T10],
                                                                                                                                                                         t11: ColParser[T11],
                                                                                                                                                                         t12: ColParser[T12],
                                                                                                                                                                         t13: ColParser[T13],
                                                                                                                                                                         t14: ColParser[T14],
                                                                                                                                                                         t15: ColParser[T15],
                                                                                                                                                                         t16: ColParser[T16]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
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

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17)) => R)(implicit t1: ColParser[T1],
                                                                                                                                                                                   t2: ColParser[T2],
                                                                                                                                                                                   t3: ColParser[T3],
                                                                                                                                                                                   t4: ColParser[T4],
                                                                                                                                                                                   t5: ColParser[T5],
                                                                                                                                                                                   t6: ColParser[T6],
                                                                                                                                                                                   t7: ColParser[T7],
                                                                                                                                                                                   t8: ColParser[T8],
                                                                                                                                                                                   t9: ColParser[T9],
                                                                                                                                                                                   t10: ColParser[T10],
                                                                                                                                                                                   t11: ColParser[T11],
                                                                                                                                                                                   t12: ColParser[T12],
                                                                                                                                                                                   t13: ColParser[T13],
                                                                                                                                                                                   t14: ColParser[T14],
                                                                                                                                                                                   t15: ColParser[T15],
                                                                                                                                                                                   t16: ColParser[T16],
                                                                                                                                                                                   t17: ColParser[T17]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
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

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18)) => R)(implicit t1: ColParser[T1],
                                                                                                                                                                                             t2: ColParser[T2],
                                                                                                                                                                                             t3: ColParser[T3],
                                                                                                                                                                                             t4: ColParser[T4],
                                                                                                                                                                                             t5: ColParser[T5],
                                                                                                                                                                                             t6: ColParser[T6],
                                                                                                                                                                                             t7: ColParser[T7],
                                                                                                                                                                                             t8: ColParser[T8],
                                                                                                                                                                                             t9: ColParser[T9],
                                                                                                                                                                                             t10: ColParser[T10],
                                                                                                                                                                                             t11: ColParser[T11],
                                                                                                                                                                                             t12: ColParser[T12],
                                                                                                                                                                                             t13: ColParser[T13],
                                                                                                                                                                                             t14: ColParser[T14],
                                                                                                                                                                                             t15: ColParser[T15],
                                                                                                                                                                                             t16: ColParser[T16],
                                                                                                                                                                                             t17: ColParser[T17],
                                                                                                                                                                                             t18: ColParser[T18]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
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

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19)) => R)(implicit t1: ColParser[T1],
                                                                                                                                                                                                       t2: ColParser[T2],
                                                                                                                                                                                                       t3: ColParser[T3],
                                                                                                                                                                                                       t4: ColParser[T4],
                                                                                                                                                                                                       t5: ColParser[T5],
                                                                                                                                                                                                       t6: ColParser[T6],
                                                                                                                                                                                                       t7: ColParser[T7],
                                                                                                                                                                                                       t8: ColParser[T8],
                                                                                                                                                                                                       t9: ColParser[T9],
                                                                                                                                                                                                       t10: ColParser[T10],
                                                                                                                                                                                                       t11: ColParser[T11],
                                                                                                                                                                                                       t12: ColParser[T12],
                                                                                                                                                                                                       t13: ColParser[T13],
                                                                                                                                                                                                       t14: ColParser[T14],
                                                                                                                                                                                                       t15: ColParser[T15],
                                                                                                                                                                                                       t16: ColParser[T16],
                                                                                                                                                                                                       t17: ColParser[T17],
                                                                                                                                                                                                       t18: ColParser[T18],
                                                                                                                                                                                                       t19: ColParser[T19]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
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

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20)) => R)(implicit t1: ColParser[T1],
                                                                                                                                                                                                                 t2: ColParser[T2],
                                                                                                                                                                                                                 t3: ColParser[T3],
                                                                                                                                                                                                                 t4: ColParser[T4],
                                                                                                                                                                                                                 t5: ColParser[T5],
                                                                                                                                                                                                                 t6: ColParser[T6],
                                                                                                                                                                                                                 t7: ColParser[T7],
                                                                                                                                                                                                                 t8: ColParser[T8],
                                                                                                                                                                                                                 t9: ColParser[T9],
                                                                                                                                                                                                                 t10: ColParser[T10],
                                                                                                                                                                                                                 t11: ColParser[T11],
                                                                                                                                                                                                                 t12: ColParser[T12],
                                                                                                                                                                                                                 t13: ColParser[T13],
                                                                                                                                                                                                                 t14: ColParser[T14],
                                                                                                                                                                                                                 t15: ColParser[T15],
                                                                                                                                                                                                                 t16: ColParser[T16],
                                                                                                                                                                                                                 t17: ColParser[T17],
                                                                                                                                                                                                                 t18: ColParser[T18],
                                                                                                                                                                                                                 t19: ColParser[T19],
                                                                                                                                                                                                                 t20: ColParser[T20]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
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

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21)) => R)(implicit t1: ColParser[T1],
                                                                                                                                                                                                                           t2: ColParser[T2],
                                                                                                                                                                                                                           t3: ColParser[T3],
                                                                                                                                                                                                                           t4: ColParser[T4],
                                                                                                                                                                                                                           t5: ColParser[T5],
                                                                                                                                                                                                                           t6: ColParser[T6],
                                                                                                                                                                                                                           t7: ColParser[T7],
                                                                                                                                                                                                                           t8: ColParser[T8],
                                                                                                                                                                                                                           t9: ColParser[T9],
                                                                                                                                                                                                                           t10: ColParser[T10],
                                                                                                                                                                                                                           t11: ColParser[T11],
                                                                                                                                                                                                                           t12: ColParser[T12],
                                                                                                                                                                                                                           t13: ColParser[T13],
                                                                                                                                                                                                                           t14: ColParser[T14],
                                                                                                                                                                                                                           t15: ColParser[T15],
                                                                                                                                                                                                                           t16: ColParser[T16],
                                                                                                                                                                                                                           t17: ColParser[T17],
                                                                                                                                                                                                                           t18: ColParser[T18],
                                                                                                                                                                                                                           t19: ColParser[T19],
                                                                                                                                                                                                                           t20: ColParser[T20],
                                                                                                                                                                                                                           t21: ColParser[T21]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
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

  def apply[T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22, R](f: ((T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21, T22)) => R)(implicit t1: ColParser[T1],
                                                                                                                                                                                                                                     t2: ColParser[T2],
                                                                                                                                                                                                                                     t3: ColParser[T3],
                                                                                                                                                                                                                                     t4: ColParser[T4],
                                                                                                                                                                                                                                     t5: ColParser[T5],
                                                                                                                                                                                                                                     t6: ColParser[T6],
                                                                                                                                                                                                                                     t7: ColParser[T7],
                                                                                                                                                                                                                                     t8: ColParser[T8],
                                                                                                                                                                                                                                     t9: ColParser[T9],
                                                                                                                                                                                                                                     t10: ColParser[T10],
                                                                                                                                                                                                                                     t11: ColParser[T11],
                                                                                                                                                                                                                                     t12: ColParser[T12],
                                                                                                                                                                                                                                     t13: ColParser[T13],
                                                                                                                                                                                                                                     t14: ColParser[T14],
                                                                                                                                                                                                                                     t15: ColParser[T15],
                                                                                                                                                                                                                                     t16: ColParser[T16],
                                                                                                                                                                                                                                     t17: ColParser[T17],
                                                                                                                                                                                                                                     t18: ColParser[T18],
                                                                                                                                                                                                                                     t19: ColParser[T19],
                                                                                                                                                                                                                                     t20: ColParser[T20],
                                                                                                                                                                                                                                     t21: ColParser[T21],
                                                                                                                                                                                                                                     t22: ColParser[T22]): RowParser[R] =
    new RowParser[R] {
      def apply(resultSet: ResultSet): R =
        f(t1(resultSet, 1),
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

}
