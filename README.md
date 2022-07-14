# JustSQL

Just write SQL as `String` and parse results into types.

# Setup

I'm using here HikariCP & Postgres here, but you use any `DataSource`.

```scala
import justsql._ //single import

implicit val db = JustSQL(new HikariDataSource()) //create JustSQL instance

val users: Try[Array[User]] = "SELECT * FROM USERS".select[User] //run query
```

# update()

A query that mutates the database like `CREATE, INSERT OR UPDATE` queries are executed via `update()` function.

For example:

```scala
//Create and insert to the table transactionally
val result: Try[Int] =
  """
    |BEGIN;
    |
    |CREATE TABLE TEST_TABLE(
    |   int SERIAL PRIMARY KEY,
    |   string VARCHAR NOT NULL,
    |   bool BOOLEAN NOT NUll
    |);
    |
    |INSERT INTO TEST_TABLE
    |values (0, 'string1', 'true'),
    |       (1, 'string2', 'false'),
    |       (2, 'string3', 'true');
    |
    |COMMIT;
    |""".stripMargin.update()
```

# select()

# selectMap()

# selectHead()

# unsafeMap()

# unsafeHead()
