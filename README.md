# JustSQL

A thin wrapper for parsing SQL query results.

Just write SQL as `String`.

```scala
implicit val db = JustSQL(HikariDS())

"SELECT name FROM user".select[String]
```
