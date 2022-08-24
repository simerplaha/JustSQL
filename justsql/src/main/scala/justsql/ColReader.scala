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

import java.math.BigInteger
import java.sql.ResultSet

trait ColReader[COL] extends ((ResultSet, Int) => COL) { self =>

  def apply(resultSet: ResultSet, index: Int): COL

  def map[T](f: COL => T): ColReader[T] =
    (resultSet: ResultSet, index: Int) =>
      f(self(resultSet, index))

}

object ColReader {

  //primitive types
  implicit val intColReader: ColReader[Int] = _.getInt(_)
  implicit val stringColReader: ColReader[String] = _.getString(_)
  implicit val booleanColReader: ColReader[Boolean] = _.getBoolean(_)
  implicit val longColReader: ColReader[Long] = _.getLong(_)
  implicit val byteColReader: ColReader[Byte] = _.getByte(_)
  implicit val doubleColReader: ColReader[Double] = _.getDouble(_)
  implicit val floatColReader: ColReader[Float] = _.getFloat(_)
  implicit val shortColReader: ColReader[Short] = _.getShort(_)
  implicit val bigDecimalColReader: ColReader[BigDecimal] = _.getBigDecimal(_): BigDecimal
  implicit val byteArrayColReader: ColReader[Array[Byte]] = _.getBytes(_)
  implicit val bigIntegerColReader: ColReader[BigInteger] = bigDecimalColReader.map(_.toBigInt.bigInteger)

  //java.sql types
  implicit val timestampColReader: ColReader[java.sql.Timestamp] = _.getTimestamp(_)
  implicit val blobColReader: ColReader[java.sql.Blob] = _.getBlob(_)
  implicit val clobColReader: ColReader[java.sql.Clob] = _.getClob(_)
  implicit val dateColReader: ColReader[java.sql.Date] = _.getDate(_)
  implicit val timeColReader: ColReader[java.sql.Time] = _.getTime(_)
  implicit val refColReader: ColReader[java.sql.Ref] = _.getRef(_)
  implicit val nClobColReader: ColReader[java.sql.NClob] = _.getNClob(_)
  implicit val rowIdColReader: ColReader[java.sql.RowId] = _.getRowId(_)
  implicit val sqlXMLColReader: ColReader[java.sql.SQLXML] = _.getSQLXML(_)

  //other types
  implicit val characterStreamColReader: ColReader[java.io.Reader] = _.getCharacterStream(_)
  implicit val binaryStreamColReader: ColReader[java.io.InputStream] = _.getBinaryStream(_)
  implicit val urlColReader: ColReader[java.net.URL] = _.getURL(_)

}
