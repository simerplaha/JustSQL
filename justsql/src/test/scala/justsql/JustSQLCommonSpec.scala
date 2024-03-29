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

import justsql.TestUtil._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.TryValues._

import scala.collection.immutable.ArraySeq

/**
 * Common test-cases for any [[SQLConnector]].
 *
 * Currently used for testing Slick, HikariCP & [[JavaSQLConnector]]
 * */
trait JustSQLCommonSpec extends AnyWordSpec {

  def connector(): SQLConnector

  "update" should {
    "create a table and insert" when {
      "not transactional" in {
        withDB(connector()) {
          implicit db =>
            //create table
            "CREATE TABLE TEST_TABLE (value varchar)".update().runSync().success.value shouldBe 0
            //insert data to table
            "INSERT INTO TEST_TABLE values ('value1')".update().runSync().success.value shouldBe 1

            //another insert with parameters
            UpdateSQL {
              implicit param =>
                s"INSERT INTO TEST_TABLE values (${"value2".?})"
            }.runSync().success.value shouldBe 1

            //check data exists
            "SELECT * FROM TEST_TABLE".select[String]().runSync().success.value shouldBe ArraySeq("value1", "value2")
        }
      }

      "transactional" when {
        "parametrised" in {
          withDB(connector()) {
            implicit db =>
              UpdateSQL {
                implicit param =>
                  s"""
                     |BEGIN;
                     |
                     |CREATE TABLE TEST_TABLE (value INT);
                     |INSERT INTO TEST_TABLE values (${1.?}), (${2.?});
                     |INSERT INTO TEST_TABLE values (${3.?}), (${4.?});
                     |
                     |COMMIT;
                     |""".stripMargin
              }.runSync().success.value shouldBe 0

              "SELECT * from TEST_TABLE".select[Int]().runSync().success.value shouldBe (1 to 4)
          }
        }

        "not parametrised" in {
          withDB(connector()) {
            implicit db =>
              """
                |BEGIN;
                |
                |CREATE TABLE TEST_TABLE (value Int);
                |INSERT INTO TEST_TABLE values (1), (2), (3);
                |
                |COMMIT;
                |"""
                .stripMargin
                .update().runSync().success.value shouldBe 0

              "SELECT * from TEST_TABLE".select[Int]().runSync().success.value shouldBe (1 to 3)
          }
        }
      }

      "insert tuple" in {
        withDB(connector()) {
          implicit db =>
            "CREATE TABLE TEST_TABLE (value varchar, int INT)".update().runSync().success.value shouldBe 0

            UpdateSQL {
              implicit param =>
                s"INSERT INTO TEST_TABLE values (${"String" ?}, ${1 ?})"
            }.runSync().success.value shouldBe 1

            "SELECT * from TEST_TABLE".select[(String, Int)]().runSync().success.value should contain only (("String", 1))
        }
      }
    }
  }

  "return empty select on empty table" in {
    withDB(connector()) {
      implicit db =>
        "CREATE TABLE TEST_TABLE(key varchar)".update().runSync().success.value shouldBe 0
        "SELECT * FROM TEST_TABLE".select[String]().headOption().runSync().success.value shouldBe empty

        /** SELECT */
        //Select using typed API
        "SELECT * FROM TEST_TABLE".select[String]().runSync().success.value shouldBe empty
        //Select using Java ResultSet
        "SELECT * FROM TEST_TABLE".unsafeSelect[String](_.getString("key")).runSync().success.value shouldBe empty

        /** COUNT */
        //Count using typed API
        "SELECT count(*) FROM TEST_TABLE".select[Int]().headOption().runSync().success.value should contain(0)
    }
  }

  "return non-empty select on non-empty table" in {
    withDB(connector()) {
      implicit db =>
        "CREATE TABLE TEST_TABLE(key varchar)".update().runSync().success.value shouldBe 0
        "INSERT INTO TEST_TABLE values ('1'), ('2'), ('3')".update().runSync().success.value shouldBe 3

        /** SELECT */
        //Select using typed API
        "SELECT * FROM TEST_TABLE".select[String]().runSync().success.value shouldBe ArraySeq("1", "2", "3")
        //Select using Java ResultSet
        "SELECT * FROM TEST_TABLE".unsafeSelect[String](_.getString("key")).runSync().success.value shouldBe ArraySeq("1", "2", "3")

        /** COUNT */
        //Count using typed API
        "SELECT count(*) FROM TEST_TABLE".select[Int]().headOption().runSync().success.value should contain(3)
    }
  }

  "return zero for count query when table shouldBe empty" in {
    withDB(connector()) {
      implicit db =>
        "CREATE TABLE TEST_TABLE(key varchar)".update().runSync().success.value shouldBe 0

        /** COUNT */
        //Count using typed API
        "SELECT count(*) FROM TEST_TABLE".select[Int]().headOption().runSync().success.value should contain(0)
        //Count using typed API with naming column
        "SELECT count(*) as count FROM TEST_TABLE".select[Int]().headOption().runSync().success.value should contain(0)
        //Count using ResultSet
        "SELECT count(*) as count FROM TEST_TABLE".unsafeSelect[Int](_.getInt("count")).headOption().runSync().success.value should contain(0)
    }
  }

  "return row count when table shouldBe non-empty" in {
    withDB(connector()) {
      implicit db =>
        "CREATE TABLE TEST_TABLE(key varchar)".update().runSync().success.value shouldBe 0
        "INSERT INTO TEST_TABLE values ('one'), ('two'), ('three')".update().runSync().success.value shouldBe 3

        /** COUNT */
        //Count using typed API
        "SELECT count(*) FROM TEST_TABLE".select[Int]().headOption().runSync().success.value should contain(3)
        //Count using typed API with naming column
        "SELECT count(*) as count FROM TEST_TABLE".select[Int]().headOption().runSync().success.value should contain(3)
        //Count using ResultSet
        "SELECT count(*) as count FROM TEST_TABLE".unsafeSelect(_.getInt("count")).headOption().runSync().success.value should contain(3)
    }
  }

  "select from table with multiple rows created using transactions" in {
    withDB(connector()) {
      implicit db =>

        """
          |BEGIN;
          |
          |CREATE TABLE TEST_TABLE(
          |   int SERIAL PRIMARY KEY,
          |   string VARCHAR NOT NULL,
          |   bool BOOLEAN NOT NUll
          |);
          |
          |INSERT INTO TEST_TABLE
          |values (0, 'string1', 'true'),
          |       (1, 'string2', 'false'),
          |       (2, 'string3', 'true');
          |
          |COMMIT;
          |""".stripMargin.update().runSync().success.value shouldBe 0

        case class Row(int: Int, string: String, bool: Boolean)

        implicit val rowReader: RowReader[Row] = RowReader(Row.tupled)

        /** SELECT USING A CASE CLASS */
        "SELECT * FROM TEST_TABLE".select[Row]().runSync().success.value shouldBe
          ArraySeq(
            Row(int = 0, string = "string1", bool = true),
            Row(int = 1, string = "string2", bool = false),
            Row(int = 2, string = "string3", bool = true)
          )

        /** SELECT USING A TUPLE */
        "SELECT * FROM TEST_TABLE".select[(Int, String, Boolean)]().runSync().success.value shouldBe
          ArraySeq(
            (0, "string1", true),
            (1, "string2", false),
            (2, "string3", true)
          )

        /** COUNT */
        //Count using typed API
        "SELECT count(*) FROM TEST_TABLE".select[Int]().headOption().runSync().success.value should contain(3)
        //Count using typed API with naming column
        "SELECT count(*) as count FROM TEST_TABLE".select[Int]().headOption().runSync().success.value should contain(3)
        //Count using ResultSet
        "SELECT count(*) as count FROM TEST_TABLE".unsafeSelect[Int](_.getInt("count")).headOption().runSync().success.value should contain(3)
    }
  }

  "embed queries" in {
    withDB(connector()) {
      implicit db =>
        "CREATE TABLE TEST_TABLE(int int, bool boolean, string varchar)".update().runSync().success.value shouldBe 0

        UpdateSQL {
          implicit params =>
            s"""
               |INSERT INTO TEST_TABLE values (${1.?}, ${false.?}, ${"one".?}),
               |                              (${2.?}, ${true.?},  ${"two".?}),
               |                              (${3.?}, ${false.?}, ${"three".?})
               |
               |""".stripMargin
        }.runSync().success.value shouldBe 3

        val maxIntQuery =
          SelectSQL[Int] {
            implicit params: Params =>
              s"""
                 |SELECT max(int) from TEST_TABLE where bool = ${true.?}
                 |""".stripMargin
          }.exactlyOne()

        val finalQuery =
          SelectSQL[Int] {
            implicit params: Params =>
              s"""
                 |SELECT int from TEST_TABLE
                 | WHERE int = (${maxIntQuery.embed})
                 |""".stripMargin
          }.headOption()

        finalQuery.runSync().success.value should contain(2)
    }
  }

  "map" should {
    "transform query result" when {
      "query succeeds" in {
        withDB(connector()) {
          implicit db =>
            val created =
              "CREATE TABLE TEST_TABLE(int int)".update() map {
                result =>
                  result == 0
              }

            created.runSync().success.value shouldBe true

            val content =
              "SELECT * FROM TEST_TABLE".stripMargin.select[Boolean]()

            content.runSync().success.value shouldBe empty
        }
      }

      "query fails but has recovery" in {
        withDB(connector()) {
          implicit db =>
            val sql =
              "CREATE TABLE TEST_TABLE(int invalid_type)" //original query
                .update()
                .map {
                  _ =>
                    fail("Should not have touched this code") // this code should not be called because of error in query
                }
                .recoverWith {
                  exception =>
                    //exception contains invalid field
                    exception.getMessage.contains("""ERROR: type "invalid_type" does not exist""") shouldBe true

                    //this time also insert some data
                    """
                      |BEGIN;
                      |CREATE TABLE TEST_TABLE(int int);
                      |INSERT INTO TEST_TABLE VALUES (1), (2);
                      |COMMIT;
                      |""".stripMargin.update()
                }

            sql.runSync().success.value shouldBe 0

            //assert data exists.
            "SELECT * FROM TEST_TABLE".stripMargin.select[Int]().runSync().success.value shouldBe ArraySeq(1, 2)
        }
      }
    }

    "not transform query result" when {
      "query fails" in {
        withDB(connector()) {
          implicit db =>
            val sql =
              "CREATE TABLE TEST_TABLE(int invalid_type)".update() map {
                _ =>
                  fail("Should not have touched this code")
              }

            //TODO: Might not work for different database, different SQL databases might have different error response message.
            sql
              .runSync()
              .failure
              .exception
              .getMessage.contains("""ERROR: type "invalid_type" does not exist""") shouldBe true
        }
      }

      "query succeeds but map fails" in {
        withDB(connector()) {
          implicit db =>
            val sql =
              "CREATE TABLE TEST_TABLE(int int)".update() map {
                _ =>
                  throw new Exception("Kaboom!")
              }

            sql.runSync().failure.exception should have message "Kaboom!"
        }
      }
    }
  }

  "flatMap" should {
    "execute queries in sequence" when {
      "create and then insert" in {
        withDB(connector()) {
          implicit db =>
            val sql =
              "CREATE TABLE TEST_TABLE (value varchar)".update() flatMap {
                result =>
                  result shouldBe 0
                  "INSERT INTO TEST_TABLE values ('value1')".update()
              }

            sql.runSync().success.value shouldBe 1
        }
      }

      "insert and then create" in {
        withDB(connector()) {
          implicit db =>
            val sql =
              "INSERT INTO TEST_TABLE values ('value1')".update() flatMap {
                _ =>
                  "CREATE TABLE TEST_TABLE (value varchar)".update()
              }

            //expect failure
            sql
              .runSync()
              .failure
              .exception
              .getMessage.contains("""ERROR: relation "test_table" does not exist""") shouldBe true
        }
      }
    }

    "fail execution" when {
      "flatMap to query fails" in {
        withDB(connector()) {
          implicit db =>
            val sql =
              "CREATE TABLE TEST_TABLE (value int)".update() flatMap {
                result =>
                  result shouldBe 0
                  "INSERT INTO TEST_TABLE values ('not an int')".update()
              }

            sql
              .runSync()
              .failure
              .exception
              .getMessage.contains("""ERROR: invalid input syntax for type integer: "not an int"""") shouldBe true
        }
      }

      "flatMap fails" in {
        withDB(connector()) {
          implicit db =>
            val sql =
              "CREATE TABLE TEST_TABLE (value int)".update() flatMap {
                _ =>
                  throw new Exception("Kaboom!")
              }

            //expect failure
            sql
              .runSync()
              .failure
              .exception should have message "Kaboom!"
        }
      }
    }
  }
}
