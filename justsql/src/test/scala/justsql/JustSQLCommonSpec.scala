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

import scala.util.Success

/**
 * Common test-cases for any [[SQLConnector]].
 *
 * Currently used for testing Slick, HikariCP & [[JavaSQLConnector]]
 * */
trait JustSQLCommonSpec extends AnyWordSpec {

  def connector(): SQLConnector

  "update" should {
    "create and insert" when {
      "not transactional" in {
        withDB(connector()) { implicit db =>
          "CREATE TABLE TEST_TABLE (value varchar)".update() shouldBe Success(0)
          "INSERT INTO TEST_TABLE values ('value1')".update() shouldBe Success(1)

          Sql {
            implicit param =>
              s"INSERT INTO TEST_TABLE values (${"value2".?})"
          }.update() shouldBe Success(1)
        }
      }

      "transactional" when {
        "parametrised" in {
          withDB(connector()) { implicit db =>
            Sql {
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
            }.update() shouldBe Success(0)

            "SELECT * from TEST_TABLE".select[Int]().success.value shouldBe (1 to 4)
          }
        }

        "not parametrised" in {
          withDB(connector()) { implicit db =>
            """
              |BEGIN;
              |
              |CREATE TABLE TEST_TABLE (value Int);
              |INSERT INTO TEST_TABLE values (1), (2), (3);
              |
              |COMMIT;
              |"""
              .stripMargin
              .update() shouldBe Success(0)

            "SELECT * from TEST_TABLE".select[Int]().success.value shouldBe (1 to 3)
          }
        }
      }

      "insert tuple" in {
        withDB(connector()) { implicit db =>
          "CREATE TABLE TEST_TABLE (value varchar, int INT)".update() shouldBe Success(0)

          Sql {
            implicit param =>
              s"INSERT INTO TEST_TABLE values (${"String" ?}, ${1 ?})"
          }.update() shouldBe Success(1)

          "SELECT * from TEST_TABLE".select[(String, Int)]().success.value should contain only (("String", 1))
        }
      }
    }
  }


  "return empty select on empty table" in {
    withDB(connector()) { implicit db =>
      "CREATE TABLE TEST_TABLE(key varchar)".update() shouldBe Success(0)

      /** SELECT */
      //Select using typed API
      "SELECT * FROM TEST_TABLE".select[String]().success.value shouldBe Array.empty[String]
      //Select using Java ResultSet
      "SELECT * FROM TEST_TABLE".unsafeSelect[String](_.getString("key")).success.value shouldBe Array.empty[String]

      /** COUNT */
      //Count using typed API
      "SELECT count(*) FROM TEST_TABLE".selectOne[Int]() shouldBe Success(Some(0))
      //Count using ResultSet
      "SELECT count(*) as c FROM TEST_TABLE".unsafeSelectOne(_.getInt("c")) shouldBe Success(Some(0))
    }
  }

  "return non-empty select on non-empty table" in {
    withDB(connector()) { implicit db =>
      "CREATE TABLE TEST_TABLE(key varchar)".update() shouldBe Success(0)
      "INSERT INTO TEST_TABLE values ('1'), ('2'), ('3')".update() shouldBe Success(3)

      /** SELECT */
      //Select using typed API
      "SELECT * FROM TEST_TABLE".select[String]().success.value shouldBe Array("1", "2", "3")
      //Select using Java ResultSet
      "SELECT * FROM TEST_TABLE".unsafeSelect[String](_.getString("key")).success.value shouldBe Array("1", "2", "3")

      /** COUNT */
      //Count using typed API
      "SELECT count(*) FROM TEST_TABLE".selectOne[Int]() shouldBe Success(Some(3))
      //Count using ResultSet
      "SELECT count(*) as c FROM TEST_TABLE".unsafeSelectOne(_.getInt("c")) shouldBe Success(Some(3))
    }
  }

  "return zero for count query when table shouldBe empty" in {
    withDB(connector()) { implicit db =>
      "CREATE TABLE TEST_TABLE(key varchar)".update() shouldBe Success(0)

      /** COUNT */
      //Count using typed API
      "SELECT count(*) FROM TEST_TABLE".selectOne[Int]() shouldBe Success(Some(0))
      //Count using typed API with naming column
      "SELECT count(*) as count FROM TEST_TABLE".selectOne[Int]() shouldBe Success(Some(0))
      //Count using ResultSet
      "SELECT count(*) as count FROM TEST_TABLE".unsafeSelectOne[Int](_.getInt("count")) shouldBe Success(Some(0))
    }
  }

  "return row count when table shouldBe non-empty" in {
    withDB(connector()) { implicit db =>
      "CREATE TABLE TEST_TABLE(key varchar)".update() shouldBe Success(0)
      "INSERT INTO TEST_TABLE values ('one'), ('two'), ('three')".update() shouldBe Success(3)

      /** COUNT */
      //Count using typed API
      "SELECT count(*) FROM TEST_TABLE".selectOne[Int]() shouldBe Success(Some(3))
      //Count using typed API with naming column
      "SELECT count(*) as count FROM TEST_TABLE".selectOne[Int]() shouldBe Success(Some(3))
      //Count using ResultSet
      "SELECT count(*) as count FROM TEST_TABLE".unsafeSelectOne(_.getInt("count")) shouldBe Success(Some(3))
    }
  }

  "select from table with multiple rows created using transactions" in {
    withDB(connector()) { implicit db =>

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
        |""".stripMargin.update() shouldBe Success(0)

      case class Row(int: Int, string: String, bool: Boolean)

      implicit val rowReader: RowReader[Row] = RowReader(Row.tupled)

      /** SELECT USING A CASE CLASS */
      "SELECT * FROM TEST_TABLE".select[Row]().success.value shouldBe
        Array(
          Row(int = 0, string = "string1", bool = true),
          Row(int = 1, string = "string2", bool = false),
          Row(int = 2, string = "string3", bool = true)
        )

      /** SELECT USING A TUPLE */
      "SELECT * FROM TEST_TABLE".select[(Int, String, Boolean)]().success.value shouldBe
        Array(
          (0, "string1", true),
          (1, "string2", false),
          (2, "string3", true)
        )

      /** COUNT */
      //Count using typed API
      "SELECT count(*) FROM TEST_TABLE".selectOne[Int]() shouldBe Success(Some(3))
      //Count using typed API with naming column
      "SELECT count(*) as count FROM TEST_TABLE".selectOne[Int]() shouldBe Success(Some(3))
      //Count using ResultSet
      "SELECT count(*) as count FROM TEST_TABLE".unsafeSelectOne[Int](_.getInt("count")) shouldBe Success(Some(3))
    }
  }
}
