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
import scala.util.{Try, Using}

case class JustSQL(private val connector: SQLConnector) extends Closeable {

  def connectAndRun[RESULT](f: SQLConnectionManager => RESULT): Try[RESULT] =
    Using.Manager {
      manager =>
        val connectionManager =
          LazySQLConnectionManager(
            manager = manager,
            connector = connector
          )

        f(connectionManager)
    }

  override def close(): Unit =
    connector.close()
}
