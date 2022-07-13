package justsql

import java.io.Closeable
import java.sql.ResultSet
import javax.sql.DataSource
import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try, Using}

object JustSQL {

  @inline private def assertHasOneRow[T](rows: Array[T]): Try[T] =
    if (rows.length == 1)
      Success(rows(0))
    else
      Failure(new Exception(s"Invalid row count for count query. Expected 1. Actual ${rows.length}"))

}

final case class JustSQL(db: DataSource with AutoCloseable) extends Closeable {

  def select[T: ClassTag](sql: String)(implicit rowParser: RowParser[T]): Try[Array[T]] =
    selectMapRS(sql)(rowParser)

  def selectHead[T: ClassTag](sql: String)(implicit rowParser: RowParser[T]): Try[T] =
    select(sql) flatMap JustSQL.assertHasOneRow

  def selectHeadRS[T: ClassTag](sql: String)(parser: ResultSet => T): Try[T] =
    selectMapRS[T](sql)(parser) flatMap JustSQL.assertHasOneRow

  def selectMap[T, R: ClassTag](sql: String)(f: T => R)(implicit rowParser: RowParser[T]): Try[Array[R]] =
    selectMapRS(sql)(resultSet => f(rowParser(resultSet)))

  def selectMapRS[T: ClassTag](sql: String)(rowParser: ResultSet => T): Try[Array[T]] =
    Using.Manager {
      manager =>
        val connection = manager(db.getConnection())
        val statement = manager(connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY))
        val resultSet = manager(statement.executeQuery())

        if (resultSet.last()) {
          val array = new Array[T](resultSet.getRow)

          resultSet.beforeFirst()

          var i = 0
          while (resultSet.next()) {
            array(i) = rowParser(resultSet)
            i += 1
          }
          array
        } else {
          Array.empty[T]
        }
    }

  def update(sql: String): Try[Int] =
    Using.Manager {
      manager =>
        val session = manager(db.getConnection())
        val statement = manager(session.prepareStatement(sql))
        statement.executeUpdate()
    }

  override def close(): Unit =
    db.close()
}
