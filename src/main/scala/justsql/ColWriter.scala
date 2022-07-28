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

  /** Return the index of the next position */
  override def apply(statement: PreparedStatement, index: Int, param: P): Int

}

object ColWriter {

  implicit case object IntColWriter extends ColWriter[Int] {
    override def apply(statement: PreparedStatement, index: Int, param: Int): Int = {
      statement.setInt(index, param)
      index + 1
    }
  }

  implicit case object StringColWriter extends ColWriter[String] {
    override def apply(statement: PreparedStatement, index: Int, param: String): Int = {
      statement.setString(index, param)
      index + 1
    }
  }

  implicit case object BooleanColWriter extends ColWriter[Boolean] {
    override def apply(statement: PreparedStatement, index: Int, param: Boolean): Int = {
      statement.setBoolean(index, param)
      index + 1
    }
  }

  implicit case object LongColWriter extends ColWriter[Long] {
    override def apply(statement: PreparedStatement, index: Int, param: Long): Int = {
      statement.setLong(index, param)
      index + 1
    }
  }

  implicit case object ByteColWriter extends ColWriter[Byte] {
    override def apply(statement: PreparedStatement, index: Int, param: Byte): Int = {
      statement.setByte(index, param)
      index + 1
    }
  }

  implicit case object DoubleColWriter extends ColWriter[Double] {
    override def apply(statement: PreparedStatement, index: Int, param: Double): Int = {
      statement.setDouble(index, param)
      index + 1
    }
  }

  implicit case object FloatColWriter extends ColWriter[Float] {
    override def apply(statement: PreparedStatement, index: Int, param: Float): Int = {
      statement.setFloat(index, param)
      index + 1
    }
  }

  implicit case object ShortColWriter extends ColWriter[Short] {
    override def apply(statement: PreparedStatement, index: Int, param: Short): Int = {
      statement.setShort(index, param)
      index + 1
    }
  }

  implicit case object BigDecimalColWriter extends ColWriter[java.math.BigDecimal] {
    override def apply(statement: PreparedStatement, index: Int, param: java.math.BigDecimal): Int = {
      statement.setBigDecimal(index, param)
      index + 1
    }
  }

  implicit case object ByteArrayColWriter extends ColWriter[Array[Byte]] {
    override def apply(statement: PreparedStatement, index: Int, param: Array[Byte]): Int = {
      statement.setBytes(index, param)
      index + 1
    }
  }


  //java.sql types
  implicit case object TimestampColWriter extends ColWriter[java.sql.Timestamp] {
    override def apply(statement: PreparedStatement, index: Int, param: java.sql.Timestamp): Int = {
      statement.setTimestamp(index, param)
      index + 1
    }
  }

  implicit case object BlobColWriter extends ColWriter[java.sql.Blob] {
    override def apply(statement: PreparedStatement, index: Int, param: java.sql.Blob): Int = {
      statement.setBlob(index, param)
      index + 1
    }
  }

  implicit case object ClobColWriter extends ColWriter[java.sql.Clob] {
    override def apply(statement: PreparedStatement, index: Int, param: java.sql.Clob): Int = {
      statement.setClob(index, param)
      index + 1
    }
  }

  implicit case object DateColWriter extends ColWriter[java.sql.Date] {
    override def apply(statement: PreparedStatement, index: Int, param: java.sql.Date): Int = {
      statement.setDate(index, param)
      index + 1
    }
  }

  implicit case object TimeColWriter extends ColWriter[java.sql.Time] {
    override def apply(statement: PreparedStatement, index: Int, param: java.sql.Time): Int = {
      statement.setTime(index, param)
      index + 1
    }
  }

  implicit case object RefColWriter extends ColWriter[java.sql.Ref] {
    override def apply(statement: PreparedStatement, index: Int, param: java.sql.Ref): Int = {
      statement.setRef(index, param)
      index + 1
    }
  }

  implicit case object NClobColWriter extends ColWriter[java.sql.NClob] {
    override def apply(statement: PreparedStatement, index: Int, param: java.sql.NClob): Int = {
      statement.setNClob(index, param)
      index + 1
    }
  }

  implicit case object RowIdColWriter extends ColWriter[java.sql.RowId] {
    override def apply(statement: PreparedStatement, index: Int, param: java.sql.RowId): Int = {
      statement.setRowId(index, param)
      index + 1
    }
  }

  implicit case object SqlXMLColWriter extends ColWriter[java.sql.SQLXML] {
    override def apply(statement: PreparedStatement, index: Int, param: java.sql.SQLXML): Int = {
      statement.setSQLXML(index, param)
      index + 1
    }
  }


  //other types
  implicit case object CharacterStreamColWriter extends ColWriter[java.io.Reader] {
    override def apply(statement: PreparedStatement, index: Int, param: java.io.Reader): Int = {
      statement.setCharacterStream(index, param)
      index + 1
    }
  }

  implicit case object BinaryStreamColWriter extends ColWriter[java.io.InputStream] {
    override def apply(statement: PreparedStatement, index: Int, param: java.io.InputStream): Int = {
      statement.setBinaryStream(index, param)
      index + 1
    }
  }

  implicit case object UrlColWriter extends ColWriter[java.net.URL] {
    override def apply(statement: PreparedStatement, index: Int, param: java.net.URL): Int = {
      statement.setURL(index, param)
      index + 1
    }
  }

}
