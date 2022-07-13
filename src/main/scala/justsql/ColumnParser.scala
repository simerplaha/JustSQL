package justsql

import java.math.BigInteger
import java.sql.ResultSet

trait ColumnParser[C] extends ((ResultSet, Int) => C) { self =>

  def apply(resultSet: ResultSet, index: Int): C

  def map[T](f: C => T): ColumnParser[T] =
    (resultSet: ResultSet, index: Int) =>
      f(self(resultSet, index))

}

object ColumnParser {

  //primitive types
  implicit val intColumnParser: ColumnParser[Int] = _.getInt(_)
  implicit val stringColumnParser: ColumnParser[String] = _.getString(_)
  implicit val booleanColumnParser: ColumnParser[Boolean] = _.getBoolean(_)
  implicit val longColumnParser: ColumnParser[Long] = _.getLong(_)
  implicit val byteColumnParser: ColumnParser[Byte] = _.getByte(_)
  implicit val doubleColumnParser: ColumnParser[Double] = _.getDouble(_)
  implicit val floatColumnParser: ColumnParser[Float] = _.getFloat(_)
  implicit val shortColumnParser: ColumnParser[Short] = _.getShort(_)
  implicit val bigDecimalColumnParser: ColumnParser[BigDecimal] = _.getBigDecimal(_): BigDecimal
  implicit val byteArrayColumnParser: ColumnParser[Array[Byte]] = _.getBytes(_)
  implicit val bigIntegerColumnParser: ColumnParser[BigInteger] = bigDecimalColumnParser.map(_.toBigInt.bigInteger)

  //java.sql types
  implicit val timestampColumnParser: ColumnParser[java.sql.Timestamp] = _.getTimestamp(_)
  implicit val blobColumnParser: ColumnParser[java.sql.Blob] = _.getBlob(_)
  implicit val clobColumnParser: ColumnParser[java.sql.Clob] = _.getClob(_)
  implicit val dateColumnParser: ColumnParser[java.sql.Date] = _.getDate(_)
  implicit val timeColumnParser: ColumnParser[java.sql.Time] = _.getTime(_)
  implicit val refColumnParser: ColumnParser[java.sql.Ref] = _.getRef(_)
  implicit val nClobColumnParser: ColumnParser[java.sql.NClob] = _.getNClob(_)
  implicit val rowIdColumnParser: ColumnParser[java.sql.RowId] = _.getRowId(_)
  implicit val sqlXMLColumnParser: ColumnParser[java.sql.SQLXML] = _.getSQLXML(_)

}

