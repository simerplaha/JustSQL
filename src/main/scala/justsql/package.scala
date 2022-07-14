import java.sql.ResultSet
import scala.reflect.ClassTag
import scala.util.Try

package object justsql {

  /** Query Implicits */
  implicit class SQLImplicits(val sql: String) extends AnyVal {
    def select[ROW: ClassTag]()(implicit db: JustSQL,
                                rowParser: RowParser[ROW]): Try[Array[ROW]] =
      db.select(sql)

    def selectHead[ROW: ClassTag]()(implicit db: JustSQL,
                                    rowParser: RowParser[ROW]): Try[ROW] =
      db.selectHead(sql)

    def selectMap[ROW, B: ClassTag](f: ROW => B)(implicit db: JustSQL,
                                                 rowParser: RowParser[ROW]): Try[Array[B]] =
      db.selectMap(sql)(f)

    def update()(implicit db: JustSQL): Try[Int] =
      db.update(sql)

    def unsafeSelect[ROW: ClassTag](rowParser: ResultSet => ROW)(implicit db: JustSQL): Try[Array[ROW]] =
      db.unsafeSelect(sql)(rowParser)

    def unsafeSelectHead[ROW: ClassTag](rowParser: ResultSet => ROW)(implicit db: JustSQL): Try[ROW] =
      db.unsafeSelectHead(sql)(rowParser)
  }

}
