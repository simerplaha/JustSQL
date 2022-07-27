package justsql

import java.io.Closeable
import java.sql.ResultSet
import javax.sql.DataSource
import scala.reflect.ClassTag
import scala.util.{Failure, Success, Try, Using}

object JustSQL {

  @inline def apply[D <: DataSource with AutoCloseable](datasource: D) =
    new JustSQL(datasource)

  @inline def assertHasOneRow[T](rows: Array[T]): Try[T] =
    if (rows.length == 1)
      Success(rows(0))
    else
      Failure(new Exception(s"Invalid row count. Expected 1. Actual ${rows.length}"))

}

class JustSQL(db: DataSource with AutoCloseable) extends Closeable {

  def select[ROW: ClassTag](sql: String)(implicit rowParser: RowParser[ROW]): Try[Array[ROW]] =
    unsafeSelect(sql)(rowParser)

  def selectHead[ROW: ClassTag](sql: String)(implicit rowParser: RowParser[ROW]): Try[ROW] =
    select(sql) flatMap JustSQL.assertHasOneRow

  def selectMap[ROW, B: ClassTag](sql: String)(f: ROW => B)(implicit rowParser: RowParser[ROW]): Try[Array[B]] =
    unsafeSelect(sql)(resultSet => f(rowParser(resultSet)))

  def update(sql: String): Try[Int] =
    Using.Manager {
      manager =>
        val connection = manager(db.getConnection())
        val statement = manager(connection.prepareStatement(sql))
        statement.executeUpdate()
    }

  def unsafeSelectHead[ROW: ClassTag](sql: String)(parser: ResultSet => ROW): Try[ROW] =
    unsafeSelect[ROW](sql)(parser) flatMap JustSQL.assertHasOneRow

  def unsafeSelect[ROW: ClassTag](sql: String)(rowParser: ResultSet => ROW): Try[Array[ROW]] =
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

  override def close(): Unit =
    db.close()
}
