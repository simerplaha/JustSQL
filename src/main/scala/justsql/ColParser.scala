package justsql

import java.math.BigInteger
import java.sql.ResultSet

trait ColParser[COL] extends ((ResultSet, Int) => COL) { self =>

  def apply(resultSet: ResultSet, index: Int): COL

  def map[T](f: COL => T): ColParser[T] =
    (resultSet: ResultSet, index: Int) =>
      f(self(resultSet, index))

}

object ColParser {

  //primitive types
  implicit val intColParser: ColParser[Int] = _.getInt(_)
  implicit val stringColParser: ColParser[String] = _.getString(_)
  implicit val booleanColParser: ColParser[Boolean] = _.getBoolean(_)
  implicit val longColParser: ColParser[Long] = _.getLong(_)
  implicit val byteColParser: ColParser[Byte] = _.getByte(_)
  implicit val doubleColParser: ColParser[Double] = _.getDouble(_)
  implicit val floatColParser: ColParser[Float] = _.getFloat(_)
  implicit val shortColParser: ColParser[Short] = _.getShort(_)
  implicit val bigDecimalColParser: ColParser[BigDecimal] = _.getBigDecimal(_): BigDecimal
  implicit val byteArrayColParser: ColParser[Array[Byte]] = _.getBytes(_)
  implicit val bigIntegerColParser: ColParser[BigInteger] = bigDecimalColParser.map(_.toBigInt.bigInteger)

  //java.sql types
  implicit val timestampColParser: ColParser[java.sql.Timestamp] = _.getTimestamp(_)
  implicit val blobColParser: ColParser[java.sql.Blob] = _.getBlob(_)
  implicit val clobColParser: ColParser[java.sql.Clob] = _.getClob(_)
  implicit val dateColParser: ColParser[java.sql.Date] = _.getDate(_)
  implicit val timeColParser: ColParser[java.sql.Time] = _.getTime(_)
  implicit val refColParser: ColParser[java.sql.Ref] = _.getRef(_)
  implicit val nClobColParser: ColParser[java.sql.NClob] = _.getNClob(_)
  implicit val rowIdColParser: ColParser[java.sql.RowId] = _.getRowId(_)
  implicit val sqlXMLColParser: ColParser[java.sql.SQLXML] = _.getSQLXML(_)

  //other types
  implicit val characterStreamColParser: ColParser[java.io.Reader] = _.getCharacterStream(_)
  implicit val binaryStreamColParser: ColParser[java.io.InputStream] = _.getBinaryStream(_)
  implicit val urlColParser: ColParser[java.net.URL] = _.getURL(_)

}

