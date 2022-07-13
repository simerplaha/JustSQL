package justsql

import com.zaxxer.hikari.HikariDataSource

final case class HikariDS(host: String,
                          port: Int,
                          dbName: String) extends HikariDataSource {

  setJdbcUrl(s"jdbc:postgresql://$host:$port/$dbName")

}
