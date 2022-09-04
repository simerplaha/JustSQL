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
          "CREATE TABLE TEST_TABLE (value varchar)".update().run().success.value.value shouldBe 0
          "INSERT INTO TEST_TABLE values ('value1')".update().run().success.value.value shouldBe 1

          UpdateSQL {
            implicit param =>
              s"INSERT INTO TEST_TABLE values (${"value2".?})"
          }.run().success.value.value shouldBe 1
        }
      }

      "transactional" when {
        "parametrised" in {
          withDB(connector()) { implicit db =>
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
            }.run().success.value.value shouldBe 0

            "SELECT * from TEST_TABLE".select[Int]().run().success.value shouldBe (1 to 4)
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
              .update().run().success.value.value shouldBe 0

            "SELECT * from TEST_TABLE".select[Int]().run().success.value shouldBe (1 to 3)
          }
        }
      }

      "insert tuple" in {
        withDB(connector()) { implicit db =>
          "CREATE TABLE TEST_TABLE (value varchar, int INT)".update().run().success.value.value shouldBe 0

          UpdateSQL {
            implicit param =>
              s"INSERT INTO TEST_TABLE values (${"String" ?}, ${1 ?})"
          }.run().success.value.value shouldBe 1

          "SELECT * from TEST_TABLE".select[(String, Int)]().run().success.value should contain only (("String", 1))
        }
      }
    }
  }


  "return empty select on empty table" in {
    withDB(connector()) { implicit db =>
      "CREATE TABLE TEST_TABLE(key varchar)".update()

      "CREATE TABLE TEST_TABLE(key varchar)".update().run().success.value.value shouldBe 0

      "SELECT * FROM TEST_TABLE".select[String]().headOption().run().success.value shouldBe empty

      /** SELECT */
      //Select using typed API
      "SELECT * FROM TEST_TABLE".select[String]().run().success.value shouldBe empty
      //Select using Java ResultSet
      "SELECT * FROM TEST_TABLE".unsafeSelect[String](_.getString("key")).run().success.value shouldBe empty

      /** COUNT */
      //Count using typed API
      "SELECT count(*) FROM TEST_TABLE".select[Int]().headOption().run().success.value should contain(0)
    }
  }

  "return non-empty select on non-empty table" in {
    withDB(connector()) { implicit db =>
      "CREATE TABLE TEST_TABLE(key varchar)".update().run().success.value.value shouldBe 0
      "INSERT INTO TEST_TABLE values ('1'), ('2'), ('3')".update().run().success.value.value shouldBe 3

      /** SELECT */
      //Select using typed API
      "SELECT * FROM TEST_TABLE".select[String]().run().success.value shouldBe Array("1", "2", "3")
      //Select using Java ResultSet
      "SELECT * FROM TEST_TABLE".unsafeSelect[String](_.getString("key")).run().success.value shouldBe Array("1", "2", "3")

      /** COUNT */
      //Count using typed API
      "SELECT count(*) FROM TEST_TABLE".select[Int]().headOption().run().success.value should contain(3)
    }
  }

  "return zero for count query when table shouldBe empty" in {
    withDB(connector()) { implicit db =>
      "CREATE TABLE TEST_TABLE(key varchar)".update().run().success.value.value shouldBe 0

      /** COUNT */
      //Count using typed API
      "SELECT count(*) FROM TEST_TABLE".select[Int]().headOption().run().success.value should contain(0)
      //Count using typed API with naming column
      "SELECT count(*) as count FROM TEST_TABLE".select[Int]().headOption().run().success.value should contain(0)
      //Count using ResultSet
      "SELECT count(*) as count FROM TEST_TABLE".unsafeSelect[Int](_.getInt("count")).headOption().run().success.value should contain(0)
    }
  }

  "return row count when table shouldBe non-empty" in {
    withDB(connector()) { implicit db =>
      "CREATE TABLE TEST_TABLE(key varchar)".update().run().success.value.value shouldBe 0
      "INSERT INTO TEST_TABLE values ('one'), ('two'), ('three')".update().run().success.value.value shouldBe 3

      /** COUNT */
      //Count using typed API
      "SELECT count(*) FROM TEST_TABLE".select[Int]().headOption().run().success.value should contain(3)
      //Count using typed API with naming column
      "SELECT count(*) as count FROM TEST_TABLE".select[Int]().headOption().run().success.value should contain(3)
      //Count using ResultSet
      "SELECT count(*) as count FROM TEST_TABLE".unsafeSelect(_.getInt("count")).headOption().run().success.value should contain(3)
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
        |""".stripMargin.update().run().success.value.value shouldBe 0

      case class Row(int: Int, string: String, bool: Boolean)

      implicit val rowReader: RowReader[Row] = RowReader(Row.tupled)

      /** SELECT USING A CASE CLASS */
      "SELECT * FROM TEST_TABLE".select[Row]().run().success.value shouldBe
        Array(
          Row(int = 0, string = "string1", bool = true),
          Row(int = 1, string = "string2", bool = false),
          Row(int = 2, string = "string3", bool = true)
        )

      /** SELECT USING A TUPLE */
      "SELECT * FROM TEST_TABLE".select[(Int, String, Boolean)]().run().success.value shouldBe
        Array(
          (0, "string1", true),
          (1, "string2", false),
          (2, "string3", true)
        )

      /** COUNT */
      //Count using typed API
      "SELECT count(*) FROM TEST_TABLE".select[Int]().headOption().run().success.value should contain(3)
      //Count using typed API with naming column
      "SELECT count(*) as count FROM TEST_TABLE".select[Int]().headOption().run().success.value should contain(3)
      //Count using ResultSet
      "SELECT count(*) as count FROM TEST_TABLE".unsafeSelect[Int](_.getInt("count")).headOption().run().success.value should contain(3)
    }
  }

  "embed queries" in {
    withDB(connector()) {
      implicit db =>
        "CREATE TABLE TEST_TABLE(int int, bool boolean, string varchar)".update().run().success.value.value shouldBe 0

        UpdateSQL {
          implicit params =>
            s"""
               |INSERT INTO TEST_TABLE values (${1.?}, ${false.?}, ${"one".?}),
               |                              (${2.?}, ${true.?},  ${"two".?}),
               |                              (${3.?}, ${false.?}, ${"three".?})
               |
               |""".stripMargin
        }.run().success.value.value shouldBe 3

        val maxIntQuery: SelectSQL[Int] =
          SelectSQL[Int] {
            implicit params: Params =>
              s"""
                 |SELECT max(int) from TEST_TABLE where bool = ${true.?}
                 |""".stripMargin
          }

        val finalQuery: TrackedSQL[Int, Option] =
          SelectSQL[Int] {
            implicit params: Params =>
              s"""
                 |SELECT int from TEST_TABLE
                 | WHERE int = (${maxIntQuery.embed})
                 |""".stripMargin
          }.headOption()

        finalQuery.run().success.value should contain(2)
    }

  }
}
