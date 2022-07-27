package example

import justsql._

import scala.util.Try

object Example extends App {

  implicit val db = JustSQL(datasource = HikariDS())

  "DROP TABLE USERS".update() //create table

  /** WRITING */
  val create: Try[Int] = "CREATE TABLE USERS (id INT, name VARCHAR)".update() //create table
  val insert: Try[Int] = "INSERT INTO USERS (id, name) VALUES (1, 'Tony'), (2, 'Howard')".update() //insert rows

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

  //Run Select query
  val users: Try[Array[User]] = "SELECT * FROM USERS".select[User]()
  //Select first row
  val head: Try[Int] = "SELECT count(*) FROM USERS".selectHead[Int]()
  //Select map
  val userNames: Try[Array[String]] = "SELECT * FROM USERS".selectMap[User, String](_.name)
  //Manual map
  val userNamesMap: Try[Array[String]] = "SELECT * FROM USERS".select[User]().map(_.map(_.name))
  //Unsafe select
  val unsafeNames: Try[Array[String]] = "SELECT * FROM USERS".unsafeSelect(_.getString("name"))
  //Unsafe select head
  val unsafeCount: Try[Int] = "SELECT count(*) as count FROM USERS".unsafeSelectHead(_.getInt("count"))

}
