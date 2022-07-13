package justsql

import com.zaxxer.hikari.HikariDataSource

final case class HikariDS(host: String = "localhost",
                          port: Int = 5432,
                          dbName: String = "postgres") extends HikariDataSource {

  setJdbcUrl(s"jdbc:postgresql://$host:$port/$dbName")

}
