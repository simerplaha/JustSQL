package justsql

import org.scalatest.matchers.should.Matchers._
import org.scalatest.wordspec.AnyWordSpec
import TestUtil._
import org.scalatest.TryValues._
import scala.util.Success

class JustSQLSpec extends AnyWordSpec {

  "run update" in {
    withDB { db =>
      db.update("CREATE TABLE TEST_TABLE (value varchar)") shouldBe Success(0)
      db.update("INSERT INTO TEST_TABLE values ('value')") shouldBe Success(1)
    }
  }

  "return empty select on empty table" in {
    withDB { executor =>
      executor.update("CREATE TABLE TEST_TABLE(key varchar)") shouldBe Success(0)

      /** SELECT */
      //Select using typed API
      executor.select[String]("SELECT * FROM TEST_TABLE").success.value shouldBe Array.empty[String]
      //Select and then map
      executor.selectMap[String, Int]("SELECT * FROM TEST_TABLE")(_.toInt).success.value shouldBe Array.empty[Int]
      //Select using Java ResultSet
      executor.selectMapRS[String]("SELECT * FROM TEST_TABLE")(_.getString("key")).success.value shouldBe Array.empty[String]

      /** COUNT */
      //Count using typed API
      executor.selectHead[Int]("SELECT count(*) FROM TEST_TABLE") shouldBe Success(0)
      //Count using ResultSet
      executor.selectHeadRS("SELECT count(*) as c FROM TEST_TABLE")(_.getInt("c")) shouldBe Success(0)
    }
  }

  "return non-empty select on non-empty table" in {
    withDB { executor =>
      executor.update("CREATE TABLE TEST_TABLE(key varchar)") shouldBe Success(0)
      executor.update("INSERT INTO TEST_TABLE values ('1'), ('2'), ('3')") shouldBe Success(3)

      /** SELECT */
      //Select using typed API
      executor.select[String]("SELECT * FROM TEST_TABLE").success.value shouldBe Array("1", "2", "3")
      //Select and then map
      executor.selectMap[String, Int]("SELECT * FROM TEST_TABLE")(_.toInt).success.value shouldBe Array(1, 2, 3)
      //Select using Java ResultSet
      executor.selectMapRS[String]("SELECT * FROM TEST_TABLE")(_.getString("key")).success.value shouldBe Array("1", "2", "3")

      /** COUNT */
      //Count using typed API
      executor.selectHead[Int]("SELECT count(*) FROM TEST_TABLE") shouldBe Success(3)
      //Count using ResultSet
      executor.selectHeadRS("SELECT count(*) as c FROM TEST_TABLE")(_.getInt("c")) shouldBe Success(3)
    }
  }

  "return zero for count query when table shouldBe empty" in {
    withDB { executor =>
      executor.update("CREATE TABLE TEST_TABLE(key varchar)") shouldBe Success(0)

      /** COUNT */
      //Count using typed API
      executor.selectHead[Int]("SELECT count(*) FROM TEST_TABLE") shouldBe Success(0)
      //Count using typed API with naming column
      executor.selectHead[Int]("SELECT count(*) as count FROM TEST_TABLE") shouldBe Success(0)
      //Count using ResultSet
      executor.selectHeadRS[Int]("SELECT count(*) as count FROM TEST_TABLE")(_.getInt("count")) shouldBe Success(0)
    }
  }

  "return row count when table shouldBe non-empty" in {
    withDB { executor =>
      executor.update("CREATE TABLE TEST_TABLE(key varchar)") shouldBe Success(0)
      executor.update("INSERT INTO TEST_TABLE values ('one'), ('two'), ('three')") shouldBe Success(3)

      /** COUNT */
      //Count using typed API
      executor.selectHead[Int]("SELECT count(*) FROM TEST_TABLE") shouldBe Success(3)
      //Count using typed API with naming column
      executor.selectHead[Int]("SELECT count(*) as count FROM TEST_TABLE") shouldBe Success(3)
      //Count using ResultSet
      executor.selectHeadRS("SELECT count(*) as count FROM TEST_TABLE")(_.getInt("count")) shouldBe Success(3)
    }
  }

  "select from table with multiple rows created using transactions" in {
    withDB { executor =>

      executor.update(
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
          |""".stripMargin) shouldBe Success(0)

      case class Row(int: Int, string: String, bool: Boolean)

      implicit val rowParser: RowParser[Row] = RowParser(Row.tupled)

      /** SELECT USING A CASE CLASS */
      executor.select[Row]("SELECT * FROM TEST_TABLE").success.value shouldBe
        Array(
          Row(int = 0, string = "string1", bool = true),
          Row(int = 1, string = "string2", bool = false),
          Row(int = 2, string = "string3", bool = true)
        )

      /** SELECT USING A TUPLE */
      executor.select[(Int, String, Boolean)]("SELECT * FROM TEST_TABLE").success.value shouldBe
        Array(
          (0, "string1", true),
          (1, "string2", false),
          (2, "string3", true)
        )

      /** SELECT USING A CASE CLASS & MAP TO ANOTHER VALUE (SELECT A VALUE OF COLUMN) */
      executor.selectMap[Row, String]("SELECT * FROM TEST_TABLE")(_.string + "_UPDATED").success.value shouldBe
        Array(
          "string1_UPDATED",
          "string2_UPDATED",
          "string3_UPDATED"
        )

      /** COUNT */
      //Count using typed API
      executor.selectHead[Int]("SELECT count(*) FROM TEST_TABLE") shouldBe Success(3)
      //Count using typed API with naming column
      executor.selectHead[Int]("SELECT count(*) as count FROM TEST_TABLE") shouldBe Success(3)
      //Count using ResultSet
      executor.selectHeadRS[Int]("SELECT count(*) as count FROM TEST_TABLE")(_.getInt("count")) shouldBe Success(3)
    }
  }
}
