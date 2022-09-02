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
import scala.reflect.ClassTag
import scala.util.{Try, Using}

object JustSQL {

  @inline def apply[D <: SQLConnector](datasource: D): JustSQL =
    new JustSQL(datasource)

  @inline def setParams(params: Params, statement: PreparedStatement): Unit = {
    val positionedStatements = PositionedPreparedStatement(statement)
    params foreach (_.set(positionedStatements))
  }
}

class JustSQL(connector: SQLConnector) extends Closeable {

  def run[ROW](f: (Connection, Using.Manager) => ROW): Try[ROW] =
    Using.Manager {
      manager =>
        val connection = manager(connector.getConnection())
        f(connection, manager)
    }

  def update(sql: RawSQL)(context: SqlContext): Int = {
    import context._
    val statement = manager(connection.prepareStatement(sql.sql))
    setParams(sql.params, statement)
    statement.executeUpdate()
  }

  def select[ROW: ClassTag](sql: RawSQL)(context: SqlContext)(implicit rowReader: RowReader[ROW]): Array[ROW] = {
    import context._
    val statement = manager(connection.prepareStatement(sql.sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY))
    setParams(sql.params, statement)

    val resultSet = manager(statement.executeQuery())

    if (resultSet.last()) {
      val array = new Array[ROW](resultSet.getRow)

      resultSet.beforeFirst()

      var i = 0
      while (resultSet.next()) {
        array(i) = rowReader(resultSet)
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
