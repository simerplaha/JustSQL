package justsql

import java.io.Closeable
import java.sql.ResultSet
import javax.sql.DataSource
import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try, Using}

object JustSQL {

  @inline def apply[D <: DataSource with AutoCloseable](ds: D) =
    new JustSQL(ds)

  @inline private def assertHasOneRow[T](rows: Array[T]): Try[T] =
    if (rows.length == 1)
      Success(rows(0))
    else
      Failure(new Exception(s"Invalid row count. Expected 1. Actual ${rows.length}"))

}

class JustSQL(db: DataSource with AutoCloseable) extends Closeable {

  def select[ROW: ClassTag](sql: String)(implicit rowParser: RowParser[ROW]): Try[Array[ROW]] =
    selectParse(sql)(rowParser)

  def selectHead[ROW: ClassTag](sql: String)(implicit rowParser: RowParser[ROW]): Try[ROW] =
    select(sql) flatMap JustSQL.assertHasOneRow

  def selectHeadParse[ROW: ClassTag](sql: String)(parser: ResultSet => ROW): Try[ROW] =
    selectParse[ROW](sql)(parser) flatMap JustSQL.assertHasOneRow

  def selectMap[ROW, B: ClassTag](sql: String)(f: ROW => B)(implicit rowParser: RowParser[ROW]): Try[Array[B]] =
    selectParse(sql)(resultSet => f(rowParser(resultSet)))

  def selectParse[ROW: ClassTag](sql: String)(rowParser: ResultSet => ROW): Try[Array[ROW]] =
    Using.Manager {
      manager =>
        val connection = manager(db.getConnection())
        val statement = manager(connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY))
        val resultSet = manager(statement.executeQuery())

        if (resultSet.last()) {
          val array = new Array[ROW](resultSet.getRow)

          resultSet.beforeFirst()

          var i = 0
          while (resultSet.next()) {
            array(i) = rowParser(resultSet)
            i += 1
          }
          array
        } else {
          Array.empty[ROW]
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
