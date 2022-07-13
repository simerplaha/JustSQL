# JustSQL

Nothing fancy! Just write SQL as `String` and parse query results into types.

```scala
implicit val db = JustSQL(HikariDS())

"SELECT name FROM user".select[String]
```
