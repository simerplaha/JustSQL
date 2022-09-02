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

  implicit val db = JustSQL(datasource = JavaSQLConnector())


  /** WRITING */
  val create: Try[Int] = "CREATE TABLE USERS (id INT, name VARCHAR)".update().run() //create table
  val insert: Try[Int] = "INSERT INTO USERS (id, name) VALUES (1, 'Harry'), (2, 'Ayman')".update().run() //insert rows

  /** For-comprehension */
  val action: SqlAction[(Int, Int)] =
    for {
      create <- "CREATE TABLE USERS (id INT, name VARCHAR)".update()
      insert <- "INSERT INTO USERS (id, name) VALUES (1, 'Harry'), (2, 'Ayman')".update()
    } yield (create, insert)

  val result: Try[(Int, Int)] = action.run()

  val insertParametric: Try[Int] =
    Sql {
      implicit params =>
        s"""
           |INSERT INTO USERS (id, name)
           |     VALUES (${1.?}, ${"Harry".?}),
           |            (${2.?}, ${"Ayman".?})
           |""".stripMargin
    }.update().run()

  //  Or Transactionally
//  val transaction: Try[Int] =
//    Sql {
//      implicit params =>
//        s"""
//           |BEGIN;
//           |
//           |CREATE TABLE USERS (id INT, name VARCHAR);
//           |INSERT INTO USERS   (id, name)
//           |            VALUES  (${1.?}, ${"Harry".?}),
//           |                    (${2.?}, ${"Ayman".?});
//           |
//           |COMMIT;
//           |"""
//          .stripMargin
//    }.update()
//      .recoverWith {
//        _ =>
//          //TODO: Needs to occur in the same session
//          "ROLLBACK".update().run() //if there was an error rollback
//      }.run()

  /** READING */
  //  case class that represents a table row
  case class User(id: Int, name: String)
  //Build a row reader for User
  implicit val userReader = RowReader(User.tupled)

  //Select all users
  val users: Try[Array[User]] = "SELECT * FROM USERS".select[User]().run()
  //Select first row
//  val head: Try[Option[Int]] = "SELECT count(*) FROM USERS".selectOne[Int]().run()
//  //Select all and then map to names
//  val userNamesMap: Try[Array[String]] = "SELECT * FROM USERS".select[User]().run().map(_.map(_.name))
//  //Unsafe select
//  val unsafeNames: Try[Array[String]] = "SELECT * FROM USERS".unsafeSelect(_.getString("name")).run()
//  //Unsafe select head
//  val unsafeCount: Try[Option[Int]] = "SELECT count(*) as count FROM USERS".unsafeSelectOne(_.getInt("count")).run()

}
