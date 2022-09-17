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

import java.sql.Connection
import scala.util.Using

sealed trait SQLConnectionManager {
  def connection(): Connection
  def manager(): Using.Manager
}

object LazySQLConnectionManager {
  @inline def apply(manager: Using.Manager,
                    connector: SQLConnector): LazySQLConnectionManager =
    new LazySQLConnectionManager(
      manager = manager,
      connector = connector
    )
}

/**
 * A lazy connection manager. Connection is initialised on when [[connection]] is invoked.
 *
 * @note Connection initialisation is NOT thread-safe because
 *       as defined by [[java.sql.Connection]] we cannot concurrently
 *       run multiple queries in the same connection.
 */
class LazySQLConnectionManager private(val manager: Using.Manager,
                                       connector: SQLConnector,
                                       private var lazyConnection: Connection = null) extends SQLConnectionManager {

  override def connection(): Connection =
    if (lazyConnection == null) {
      lazyConnection = manager(connector.getConnection())
      lazyConnection
    } else {
      lazyConnection
    }
}
