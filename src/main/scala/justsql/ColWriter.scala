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

trait ColWriter[P] extends ((PreparedStatement, Int, P) => Unit) {

  override def apply(statement: PreparedStatement, index: Int, param: P): Unit

}

object ColWriter {

  implicit val intColWriter: ColWriter[Int] = _.setInt(_, _)
  implicit val stringColWriter: ColWriter[String] = _.setString(_, _)
  implicit val booleanColWriter: ColWriter[Boolean] = _.setBoolean(_, _)
  implicit val longColWriter: ColWriter[Long] = _.setLong(_, _)
  implicit val byteColWriter: ColWriter[Byte] = _.setByte(_, _)
  implicit val doubleColWriter: ColWriter[Double] = _.setDouble(_, _)
  implicit val floatColWriter: ColWriter[Float] = _.setFloat(_, _)
  implicit val shortColWriter: ColWriter[Short] = _.setShort(_, _)
  implicit val bigDecimalColWriter: ColWriter[java.math.BigDecimal] = _.setBigDecimal(_, _)
  implicit val byteArrayColWriter: ColWriter[Array[Byte]] = _.setBytes(_, _)

  //java.sql types
  implicit val timestampColWriter: ColWriter[java.sql.Timestamp] = _.setTimestamp(_, _)
  implicit val blobColWriter: ColWriter[java.sql.Blob] = _.setBlob(_, _)
  implicit val clobColWriter: ColWriter[java.sql.Clob] = _.setClob(_, _)
  implicit val dateColWriter: ColWriter[java.sql.Date] = _.setDate(_, _)
  implicit val timeColWriter: ColWriter[java.sql.Time] = _.setTime(_, _)
  implicit val refColWriter: ColWriter[java.sql.Ref] = _.setRef(_, _)
  implicit val nClobColWriter: ColWriter[java.sql.NClob] = _.setNClob(_, _)
  implicit val rowIdColWriter: ColWriter[java.sql.RowId] = _.setRowId(_, _)
  implicit val sqlXMLColWriter: ColWriter[java.sql.SQLXML] = _.setSQLXML(_, _)

  //other types
  implicit val characterStreamColWriter: ColWriter[java.io.Reader] = _.setCharacterStream(_, _)
  implicit val binaryStreamColWriter: ColWriter[java.io.InputStream] = _.setBinaryStream(_, _)
  implicit val urlColWriter: ColWriter[java.net.URL] = _.setURL(_, _)

}
