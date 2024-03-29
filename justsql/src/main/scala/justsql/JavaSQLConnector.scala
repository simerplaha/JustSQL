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

import java.sql.{Connection, DriverManager}

/** Default [[SQLConnector]] */
case class JavaSQLConnector(url: String = "jdbc:postgresql://localhost:5432/postgres",
                            username: String = "postgres",
                            password: String = "password") extends SQLConnector {

  override def getConnection(): Connection =
    DriverManager.getConnection(url, username, password)

  override def close(): Unit =
    ()

}
