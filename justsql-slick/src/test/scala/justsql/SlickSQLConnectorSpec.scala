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

import com.typesafe.config.ConfigFactory
import slick.basic.DatabaseConfig
import slick.jdbc.JdbcProfile

class SlickSQLConnectorSpec extends JustSQLCommonSpec {

  override def connector(): SQLConnector = {
    new org.postgresql.Driver()
    SlickSQLConnector(config())
  }

  def config(): DatabaseConfig[JdbcProfile] =
    DatabaseConfig.forConfig[JdbcProfile](
      path = "db",
      config =
        ConfigFactory.parseString(
          s"""db = {
             |  profile = "slick.jdbc.PostgresProfile$$"
             |  db {
             |    connectionPool = disabled
             |    url            = "jdbc:postgresql://localhost:5432/postgres"
             |    username       = "postgres"
             |    password       = "password"
             |  }
             |}
             |""".stripMargin
        )
    )
}