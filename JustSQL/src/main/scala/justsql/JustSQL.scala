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

import justsql.JustSQL._

import java.io.Closeable
import java.sql.{Connection, PreparedStatement, ResultSet}
import javax.sql.DataSource
import scala.reflect.ClassTag
import scala.util.{Try, Using}

object JustSQL {

  @inline def apply(datasource: DataSource with AutoCloseable): JustSQL =
    new JustSQL(SQLConnector(datasource))

  @inline def apply[D <: SQLConnector](datasource: D) =
    new JustSQL(datasource)

  @inline def oneOrNone[T](rows: Array[T]): Option[T] =
    if (rows.length == 1)
      Some(rows(0))
    else
      None

  @inline def setParams(params: ParamBuilder, statement: PreparedStatement): Int =
    params.params.foldLeft(1) {
      case (index, writer) =>
        writer.set(statement = statement, paramIndex = index)
        index + writer.param.paramCount()
    }
}

class JustSQL(connector: SQLConnector) extends Closeable {

  def select[ROW: ClassTag](sql: Sql)(implicit rowParser: RowParser[ROW]): Try[Array[ROW]] =
    unsafeSelect(sql)(rowParser)

  def selectOne[ROW: ClassTag](sql: Sql)(implicit rowParser: RowParser[ROW]): Try[Option[ROW]] =
    select(sql) map JustSQL.oneOrNone

  def update(sql: Sql): Try[Int] =
    Using.Manager {
      manager =>
        val connection: Connection = manager(connector.getConnection())
        val statement = manager(connection.prepareStatement(sql.sql))
        setParams(sql.params, statement)
        statement.executeUpdate()
    }

  def unsafeSelectOne[ROW: ClassTag](sql: Sql)(parser: ResultSet => ROW): Try[Option[ROW]] =
    unsafeSelect[ROW](sql)(parser) map JustSQL.oneOrNone

  def unsafeSelect[ROW: ClassTag](sql: Sql)(rowParser: ResultSet => ROW): Try[Array[ROW]] =
    Using.Manager {
      manager =>
        val connection = manager(connector.getConnection())
        val statement = manager(connection.prepareStatement(sql.sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY))
        setParams(sql.params, statement)

        val resultSet = manager(statement.executeQuery())

        if (resultSet.last()) {
          val array = new Array[ROW](resultSet.getRow)

          resultSet.beforeFirst()

          var i = 0
          while (resultSet.next()) {
            array(i) = rowParser(resultSet)
            i += 1
          }
          array
        } else {
          Array.empty[ROW]
        }
    }

  override def close(): Unit =
    connector.close()
}
