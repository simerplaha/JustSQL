package justsql

import scala.util.{Failure, Success, Try}

object TestUtil {

  def withDB[O](f: JustSQL => O): O =
    using(JustSQL(HikariDS()))(f)

  def using[O](db: JustSQL)(f: JustSQL => O): O =
    dropTables()(db) match {
      case Success(_) =>
        try
          f(db)
        finally
          db.close()

      case Failure(exception) =>
        throw exception
    }

  def dropTables()(implicit db: JustSQL): Try[Int] =
    getAllTableNames() flatMap dropTables

  def getAllTableNames()(implicit db: JustSQL): Try[Array[String]] =
    """
      |select table_name
      |from information_schema.tables
      |where table_schema = 'public';
      |""".stripMargin.select()

  def dropTables(tableNames: Array[String])(implicit db: JustSQL): Try[Int] = {
    val drops =
      tableNames
        .map(name => s"drop table $name;")
        .mkString("\n")

    s"""
       |BEGIN;
       |$drops
       |COMMIT;
       |""".stripMargin.update()
  }
}
