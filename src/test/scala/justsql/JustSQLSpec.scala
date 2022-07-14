package justsql

import justsql.TestUtil._
import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.TryValues._

import scala.util.Success

class JustSQLSpec extends AnyWordSpec {

  "run update" in {
    withDB { implicit db =>
      "CREATE TABLE TEST_TABLE (value varchar)".update() shouldBe Success(0)
      "INSERT INTO TEST_TABLE values ('value')".update() shouldBe Success(1)
    }
  }

  "return empty select on empty table" in {
    withDB { implicit db =>
      "CREATE TABLE TEST_TABLE(key varchar)".update() shouldBe Success(0)

      /** SELECT */
      //Select using typed API
      "SELECT * FROM TEST_TABLE".select[String]().success.value shouldBe Array.empty[String]
      //Select and then map
      "SELECT * FROM TEST_TABLE".selectMap[String, Int](_.toInt).success.value shouldBe Array.empty[Int]
      //Select using Java ResultSet
      "SELECT * FROM TEST_TABLE".selectMapRS[String](_.getString("key")).success.value shouldBe Array.empty[String]

      /** COUNT */
      //Count using typed API
      "SELECT count(*) FROM TEST_TABLE".selectHead[Int]() shouldBe Success(0)
      //Count using ResultSet
      "SELECT count(*) as c FROM TEST_TABLE".selectHeadRS(_.getInt("c")) shouldBe Success(0)
    }
  }

  "return non-empty select on non-empty table" in {
    withDB { implicit db =>
      "CREATE TABLE TEST_TABLE(key varchar)".update() shouldBe Success(0)
      "INSERT INTO TEST_TABLE values ('1'), ('2'), ('3')".update() shouldBe Success(3)

      /** SELECT */
      //Select using typed API
      "SELECT * FROM TEST_TABLE".select[String]().success.value shouldBe Array("1", "2", "3")
      //Select and then map
      "SELECT * FROM TEST_TABLE".selectMap[String, Int](_.toInt).success.value shouldBe Array(1, 2, 3)
      //Select using Java ResultSet
      "SELECT * FROM TEST_TABLE".selectMapRS[String](_.getString("key")).success.value shouldBe Array("1", "2", "3")

      /** COUNT */
      //Count using typed API
      "SELECT count(*) FROM TEST_TABLE".selectHead[Int]() shouldBe Success(3)
      //Count using ResultSet
      "SELECT count(*) as c FROM TEST_TABLE".selectHeadRS(_.getInt("c")) shouldBe Success(3)
    }
  }

  "return zero for count query when table shouldBe empty" in {
    withDB { implicit db =>
      "CREATE TABLE TEST_TABLE(key varchar)".update() shouldBe Success(0)

      /** COUNT */
      //Count using typed API
      "SELECT count(*) FROM TEST_TABLE".selectHead[Int]() shouldBe Success(0)
      //Count using typed API with naming column
      "SELECT count(*) as count FROM TEST_TABLE".selectHead[Int]() shouldBe Success(0)
      //Count using ResultSet
      "SELECT count(*) as count FROM TEST_TABLE".selectHeadRS[Int](_.getInt("count")) shouldBe Success(0)
    }
  }

  "return row count when table shouldBe non-empty" in {
    withDB { implicit db =>
      "CREATE TABLE TEST_TABLE(key varchar)".update() shouldBe Success(0)
      "INSERT INTO TEST_TABLE values ('one'), ('two'), ('three')".update() shouldBe Success(3)

      /** COUNT */
      //Count using typed API
      "SELECT count(*) FROM TEST_TABLE".selectHead[Int]() shouldBe Success(3)
      //Count using typed API with naming column
      "SELECT count(*) as count FROM TEST_TABLE".selectHead[Int]() shouldBe Success(3)
      //Count using ResultSet
      "SELECT count(*) as count FROM TEST_TABLE".selectHeadRS(_.getInt("count")) shouldBe Success(3)
    }
  }

  "select from table with multiple rows created using transactions" in {
    withDB { implicit db =>

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

      implicit val rowParser: RowParser[Row] = RowParser(Row.tupled)

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

      /** SELECT USING A CASE CLASS & MAP TO ANOTHER VALUE (SELECT A VALUE OF COLUMN) */
      "SELECT * FROM TEST_TABLE".selectMap[Row, String](_.string + "_UPDATED").success.value shouldBe
        Array(
          "string1_UPDATED",
          "string2_UPDATED",
          "string3_UPDATED"
        )

      /** COUNT */
      //Count using typed API
      "SELECT count(*) FROM TEST_TABLE".selectHead[Int]() shouldBe Success(3)
      //Count using typed API with naming column
      "SELECT count(*) as count FROM TEST_TABLE".selectHead[Int]() shouldBe Success(3)
      //Count using ResultSet
      "SELECT count(*) as count FROM TEST_TABLE".selectHeadRS[Int](_.getInt("count")) shouldBe Success(3)
    }
  }
}
