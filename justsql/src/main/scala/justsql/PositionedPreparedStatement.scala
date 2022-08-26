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

import java.io.{InputStream, Reader}
import java.net.URL
import java.sql
import java.sql._
import java.util.Calendar

case class PositionedPreparedStatement(stmt: PreparedStatement,
                                       private var _position: Int = 1) {

  def position(): Int =
    _position

  def executeQuery(): ResultSet =
    stmt.executeQuery()

  def executeUpdate(): Int =
    stmt.executeUpdate()

  def setNull(sqlType: Int): Unit = {
    stmt.setNull(_position, sqlType)
    _position += 1
  }

  def setBoolean(x: Boolean): Unit = {
    stmt.setBoolean(_position, x)
    _position += 1
  }

  def setByte(x: Byte): Unit = {
    stmt.setByte(_position, x)
    _position += 1
  }

  def setShort(x: Short): Unit = {
    stmt.setShort(_position, x)
    _position += 1
  }

  def setInt(x: Int): Unit = {
    stmt.setInt(_position, x)
    _position += 1
  }

  def setLong(x: Long): Unit = {
    stmt.setLong(_position, x)
    _position += 1
  }

  def setFloat(x: Float): Unit = {
    stmt.setFloat(_position, x)
    _position += 1
  }

  def setDouble(x: Double): Unit = {
    stmt.setDouble(_position, x)
    _position += 1
  }

  def setBigDecimal(x: java.math.BigDecimal): Unit = {
    stmt.setBigDecimal(_position, x)
    _position += 1
  }

  def setString(x: String): Unit = {
    stmt.setString(_position, x)
    _position += 1
  }

  def setBytes(x: scala.Array[Byte]): Unit = {
    stmt.setBytes(_position, x)
    _position += 1
  }

  def setDate(x: Date): Unit = {
    stmt.setDate(_position, x)
    _position += 1
  }

  def setTime(x: Time): Unit = {
    stmt.setTime(_position, x)
    _position += 1
  }

  def setTimestamp(x: Timestamp): Unit = {
    stmt.setTimestamp(_position, x)
    _position += 1
  }

  def setAsciiStream(x: InputStream, length: Int): Unit = {
    stmt.setAsciiStream(_position, x, length)
    _position += 1
  }

  def setBinaryStream(x: InputStream, length: Int): Unit = {
    stmt.setBinaryStream(_position, x, length)
    _position += 1
  }

  def clearParameters(): Unit =
    stmt.clearParameters()

  def setObject(x: Any, targetSqlType: Int): Unit = {
    stmt.setObject(_position, x, targetSqlType)
    _position += 1
  }

  def setObject(x: Any): Unit = {
    stmt.setObject(_position, x)
    _position += 1
  }

  def execute(): Boolean =
    stmt.execute()

  def addBatch(): Unit =
    stmt.addBatch()

  def setCharacterStream(reader: Reader, length: Int): Unit = {
    stmt.setCharacterStream(_position, reader, length)
    _position += 1
  }

  def setRef(x: Ref): Unit = {
    stmt.setRef(_position, x)
    _position += 1
  }

  def setBlob(x: Blob): Unit = {
    stmt.setBlob(_position, x)
    _position += 1
  }

  def setClob(x: Clob): Unit = {
    stmt.setClob(_position, x)
    _position += 1
  }

  def setArray(x: sql.Array): Unit = {
    stmt.setArray(_position, x)
    _position += 1
  }

  def getMetaData: ResultSetMetaData =
    stmt.getMetaData

  def setDate(x: Date, cal: Calendar): Unit = {
    stmt.setDate(_position, x, cal)
    _position += 1
  }

  def setTime(x: Time, cal: Calendar): Unit = {
    stmt.setTime(_position, x, cal)
    _position += 1
  }

  def setTimestamp(x: Timestamp, cal: Calendar): Unit = {
    stmt.setTimestamp(_position, x, cal)
    _position += 1
  }

  def setNull(sqlType: Int, typeName: String): Unit = {
    stmt.setNull(_position, sqlType, typeName: String)
    _position += 1
  }

  def setURL(x: URL): Unit = {
    stmt.setURL(_position, x)
    _position += 1
  }

  def getParameterMetaData: ParameterMetaData =
    stmt.getParameterMetaData

  def setRowId(x: RowId): Unit = {
    stmt.setRowId(_position, x)
    _position += 1
  }

  def setNString(value: String): Unit = {
    stmt.setNString(_position, value)
    _position += 1
  }

  def setNCharacterStream(value: Reader, length: Long): Unit = {
    stmt.setNCharacterStream(_position, value, length)
    _position += 1
  }

  def setNClob(value: NClob): Unit = {
    stmt.setNClob(_position, value)
    _position += 1
  }

  def setClob(reader: Reader, length: Long): Unit = {
    stmt.setClob(_position, reader, length: Long)
    _position += 1
  }

  def setBlob(inputStream: InputStream, length: Long): Unit = {
    stmt.setBlob(_position, inputStream, length: Long)
    _position += 1
  }

  def setNClob(reader: Reader, length: Long): Unit = {
    stmt.setNClob(_position, reader, length)
    _position += 1
  }

  def setSQLXML(xmlObject: SQLXML): Unit = {
    stmt.setSQLXML(_position, xmlObject)
    _position += 1
  }

  def setObject(x: Any, targetSqlType: Int, scaleOrLength: Int): Unit = {
    stmt.setObject(_position, x, targetSqlType, scaleOrLength)
    _position += 1
  }

  def setAsciiStream(x: InputStream, length: Long): Unit = {
    stmt.setAsciiStream(_position, x, length)
    _position += 1
  }

  def setBinaryStream(x: InputStream, length: Long): Unit = {
    stmt.setBinaryStream(_position, x, length)
    _position += 1
  }

  def setCharacterStream(reader: Reader, length: Long): Unit = {
    stmt.setCharacterStream(_position, reader, length)
    _position += 1
  }

  def setAsciiStream(x: InputStream): Unit = {
    stmt.setAsciiStream(_position, x)
    _position += 1
  }

  def setBinaryStream(x: InputStream): Unit = {
    stmt.setBinaryStream(_position, x)
    _position += 1
  }

  def setCharacterStream(reader: Reader): Unit = {
    stmt.setCharacterStream(_position, reader)
    _position += 1
  }

  def setNCharacterStream(value: Reader): Unit = {
    stmt.setNCharacterStream(_position, value)
    _position += 1
  }

  def setClob(reader: Reader): Unit = {
    stmt.setClob(_position, reader)
    _position += 1
  }

  def setBlob(inputStream: InputStream): Unit = {
    stmt.setBlob(_position, inputStream)
    _position += 1
  }

  def setNClob(reader: Reader): Unit = {
    stmt.setNClob(_position, reader)
    _position += 1
  }

  def executeQuery(sql: String): ResultSet =
    stmt.executeQuery(sql)

  def executeUpdate(sql: String): Int =
    stmt.executeUpdate(sql)

  def close(): Unit =
    stmt.close()

  def getMaxFieldSize: Int =
    stmt.getMaxFieldSize

  def setMaxFieldSize(max: Int): Unit =
    stmt.setMaxFieldSize(max)

  def getMaxRows: Int =
    stmt.getMaxRows

  def setMaxRows(max: Int): Unit =
    stmt.setMaxRows(max)

  def setEscapeProcessing(enable: Boolean): Unit =
    stmt.setEscapeProcessing(enable)

  def getQueryTimeout: Int =
    stmt.getQueryTimeout

  def setQueryTimeout(seconds: Int): Unit =
    stmt.setQueryTimeout(seconds)

  def cancel(): Unit =
    stmt.cancel()

  def getWarnings: SQLWarning =
    stmt.getWarnings

  def clearWarnings(): Unit =
    stmt.clearWarnings()

  def setCursorName(name: String): Unit =
    stmt.setCursorName(name)

  def execute(sql: String): Boolean =
    stmt.execute(sql)

  def getResultSet: ResultSet =
    stmt.getResultSet

  def getUpdateCount: Int =
    stmt.getUpdateCount

  def getMoreResults: Boolean =
    stmt.getMoreResults

  def setFetchDirection(direction: Int): Unit =
    stmt.setFetchDirection(direction)

  def getFetchDirection: Int =
    stmt.getFetchDirection

  def setFetchSize(rows: Int): Unit =
    stmt.setFetchSize(rows)

  def getFetchSize: Int =
    stmt.getFetchSize

  def getResultSetConcurrency: Int =
    stmt.getResultSetConcurrency

  def getResultSetType: Int =
    stmt.getResultSetType

  def addBatch(sql: String): Unit =
    stmt.addBatch(sql)

  def clearBatch(): Unit =
    stmt.clearBatch()

  def executeBatch(): scala.Array[Int] =
    stmt.executeBatch()

  def getConnection: Connection =
    stmt.getConnection

  def getMoreResults(current: Int): Boolean =
    stmt.getMoreResults(current: Int)

  def getGeneratedKeys: ResultSet =
    stmt.getGeneratedKeys

  def executeUpdate(sql: String, autoGeneratedKeys: Int): Int =
    stmt.executeUpdate(sql, autoGeneratedKeys)

  def executeUpdate(sql: String, columnIndexes: scala.Array[Int]): Int =
    stmt.executeUpdate(sql, columnIndexes)

  def executeUpdate(sql: String, columnNames: scala.Array[String]): Int =
    stmt.executeUpdate(sql, columnNames)

  def execute(sql: String, autoGeneratedKeys: Int): Boolean =
    stmt.execute(sql, autoGeneratedKeys)

  def execute(sql: String, columnIndexes: scala.Array[Int]): Boolean =
    stmt.execute(sql, columnIndexes)

  def execute(sql: String, columnNames: scala.Array[String]): Boolean =
    stmt.execute(sql, columnNames)

  def getResultSetHoldability: Int =
    stmt.getResultSetHoldability

  def isClosed: Boolean =
    stmt.isClosed

  def setPoolable(poolable: Boolean): Unit =
    stmt.setPoolable(poolable: Boolean)

  def isPoolable: Boolean =
    stmt.isPoolable

  def closeOnCompletion(): Unit =
    stmt.closeOnCompletion()

  def isCloseOnCompletion: Boolean =
    stmt.isCloseOnCompletion

  def unwrap[T](iface: Class[T]): T =
    stmt.unwrap(iface)

  def isWrapperFor(iface: Class[_]): Boolean =
    stmt.isWrapperFor(iface)

}
