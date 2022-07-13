package justsql

import scala.util.{Failure, Success, Try}

object TestUtil {

  def withDB[O](code: JustSQL => O): O =
    using(JustSQL(HikariDS()))(code)

  def using[O](db: JustSQL)(code: JustSQL => O): O =
    dropTables()(db) match {
      case Success(_) =>
        try
          code(db)
        finally
          db.close()

      case Failure(exception) =>
        throw exception
    }


  def dropTables()(implicit db: JustSQL): Try[Int] =
    getAllTableNames() flatMap (dropTables(_))

  def getAllTableNames()(implicit db: JustSQL): Try[Array[String]] =
    db.select[String] {
      """
        |select table_name
        |from information_schema.tables
        |where table_schema = 'public';
        |""".stripMargin
    }

  def dropTables(tableNames: Array[String])(implicit db: JustSQL): Try[Int] = {
    val drops =
      tableNames
        .map(name => s"drop table $name;")
        .mkString("\n")

    db.update(
      s"""
         |BEGIN;
         |$drops
         |COMMIT;
         |""".stripMargin
    )
  }
}
