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

import java.io.Closeable
import java.sql.{Connection, PreparedStatement, ResultSet}
import scala.collection.Factory
import scala.util.{Try, Using}

object JustSQL {

  @inline def apply[D <: SQLConnector](datasource: D): JustSQL =
    new JustSQL(datasource)

  @inline def setParams(params: Params,
                        statement: PreparedStatement): Unit = {
    val positionedStatements = PositionedPreparedStatement(statement)
    params foreach (_.set(positionedStatements))
  }

  @inline def update(sql: String, params: Params)(connection: Connection,
                                                  manager: Using.Manager): Int = {
    val statement = manager(connection.prepareStatement(sql))
    setParams(params, statement)
    statement.executeUpdate()
  }

  def select[ROW, C](sql: String, params: Params)(connection: Connection,
                                                  manager: Using.Manager)(implicit rowReader: RowReader[ROW],
                                                                          factory: Factory[ROW, C]): C = {
    val statement = manager(connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY))
    setParams(params, statement)

    val resultSet = manager(statement.executeQuery())

    if (resultSet.last()) {
      val builder = factory.newBuilder
      builder.sizeHint(resultSet.getRow)

      resultSet.beforeFirst()

      while (resultSet.next())
        builder addOne rowReader(resultSet)

      builder.result()
    } else {
      factory.newBuilder.result()
    }
  }
}

class JustSQL(connector: SQLConnector) extends Closeable {

  def connectAndRun[RESULT](f: (Connection, Using.Manager) => RESULT): Try[RESULT] =
    Using.Manager {
      manager =>
        val connection = manager(connector.getConnection())
        f(connection, manager)
    }

  override def close(): Unit =
    connector.close()
}
