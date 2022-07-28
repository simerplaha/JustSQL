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

package example

import justsql._

import scala.util.Try

object Example extends App {

  implicit val db = JustSQL(datasource = HikariDS())

  /** WRITING */
  val create: Try[Int] = "CREATE TABLE USERS (id INT, name VARCHAR)".update() //create table
  val insert: Try[Int] = "INSERT INTO USERS (id, name) VALUES (1, 'Tony'), (2, 'Howard')".update() //insert rows

  val insertParametric: Try[Int] =
    Sql {
      implicit params =>
        s"""
           |INSERT INTO USERS (id, name)
           |     VALUES ${(1, "Harry") ??},
           |            ${(2, "Ryan") ??}
           |""".stripMargin
    }.update()

  //  Or Transactionally
  val transaction: Try[Int] =
    """
      |BEGIN;
      |
      |CREATE TABLE USERS (id INT, name VARCHAR);
      |INSERT INTO USERS (id, name) VALUES (1, 'Tony'), (2, 'Howard');
      |
      |COMMIT;
      |"""
      .stripMargin
      .update()
      .recoverWith {
        _ =>
          "ROLLBACK".update() //if there was an error rollback
      }

  /** READING */
  //  case class that represents a table row
  case class User(id: Int, name: String)
  //Build a row parser for User
  implicit val userParser = RowParser(User.tupled)

  //Select all users
  val users: Try[Array[User]] = "SELECT * FROM USERS".select[User]()
  //Select first row
  val head: Try[Int] = "SELECT count(*) FROM USERS".selectOne[Int]()
  //Select and map to names
  val userNames: Try[Array[String]] = "SELECT * FROM USERS".selectMap[User, String](_.name)
  //Select all and then map to names
  val userNamesMap: Try[Array[String]] = "SELECT * FROM USERS".select[User]().map(_.map(_.name))
  //Unsafe select
  val unsafeNames: Try[Array[String]] = "SELECT * FROM USERS".unsafeSelect(_.getString("name"))
  //Unsafe select head
  val unsafeCount: Try[Int] = "SELECT count(*) as count FROM USERS".unsafeSelectOne(_.getInt("count"))

}
