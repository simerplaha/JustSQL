# JustSQL

Just write SQL as `String` and parse results in types.

A thin facade over `java.sql` types that adds type-safety to query results & parameters.

Easy interop with existing ORMs. Interop for Slick and HikariCP is provided.

## Why another SQL library/facade?

- ORMs and custom string interpolation solutions are nice, but most are incomplete and restrictive, specially
  when writing complex SQL queries.
- Debugging performance issues becomes a challenge when generated queries by ORMs are not well optimised.
- Many ORMs do not have any support for `EXPLAIN ANALYZE` statements. Monitoring `index` performance becomes difficult.
- IDEs have better support for plain SQL queries and default Scala `s` string interpolation VS custom `sql`
  string interpolation.

Performance critical applications that want to write unrestricted SQL with type-safety added to query
results & parameters would find JustSQL much easy to work with.

# Sponsors

Thank you [JetBrains](https://www.jetbrains.com/?from=SwayDB)
& [JProfiler](https://www.ej-technologies.com/products/jprofiler/overview.html)
for full-featured open-source licences to their awesome development tools!

<table>
  <tr>
    <td><a href="https://www.jetbrains.com/?from=SwayDB" target="_blank"><img src="/docs/jetbrains_logo.png" alt="Jetbrains support"/></a></td>
    <td><a href="https://www.ej-technologies.com/products/jprofiler/overview.html" target="_blank"><img src="/docs/jprofiler_logo.png" alt="JProfiler support"/></a></td>
    <td><a href="https://github.com/sponsors/simerplaha" target="_blank">[Become a sponsor]</a></td>
  </tr>
</table>

# Setup

```scala
libraryDependencies ++= Seq(
  "com.github.simerplaha" %% "justsql" % "0.1.0",
  //Optional: For Slick interop
  "com.github.simerplaha" %% "justsql-hikari" % "0.1.0",
  //Optional: For hikariCP interop
  "com.github.simerplaha" %% "justsql-slick" % "0.1.0"
)
```

# Create JustSQL

The code snippets below can be found in [Example.scala](/JustSQL/src/test/scala/example/Example.scala).

I'm using Postgres and default `JavaSQLConnector` here, but you should a high-performance
JDBC connection pool library. See [Slick Interop](#slick-interop) or [HikariCO Interop](#hikaricp-interop).

```scala
import justsql._ //single import

implicit val db = JustSQL(datasource = JavaSQLConnector()) //create database instance
```

# update()

Queries that mutate like `CREATE, INSERT OR UPDATE` queries are executed via `update()` function.

Let's create our example `USERS` table

```scala
//create table
val create: Try[Int] = "CREATE TABLE USERS (id INT, name VARCHAR)".update()
//insert rows
val insert: Try[Int] = "INSERT INTO USERS (id, name) VALUES (1, 'Harry'), (2, 'Ayman')".update()
```

# Query parameters

SQL parameters are set with the suffix `?`.

```scala
//Or insert using parameters
val insertParametric: Try[Int] =
  Sql {
    implicit params =>
      s"""
         |INSERT INTO USERS (id, name)
         |     VALUES (${1.?}, ${"Harry".?}),
         |            (${2.?}, ${"Ayman".?})
         |""".stripMargin
  }.update()
```

# select()

First, we need to create a `case class` that represents a table row
which in our case is a `User`

```scala
//case class that represents a table row
case class User(id: Int, name: String)
//Build a row parser for User
implicit val userParser = RowParser(User.tupled)
```

Read all `User`s

```scala
val users: Try[Array[User]] = "SELECT * FROM USERS".select[User]()
```

# selectOne()

Expects `1` row else returns `None`

```scala
val count: Try[Option[Int]] = "SELECT count(*) FROM USERS".selectOne[Int]()
```

# Transactionally

Being plain SQL, transactions are written using the usual `BEGIN;` and `COMMIT;` statements.

```scala
val transaction: Try[Int] =
  """
    |BEGIN;
    |
    |CREATE TABLE USERS (id INT, name VARCHAR);
    |INSERT INTO USERS (id, name) VALUES (1, 'Harry'), (2, 'Ayman');
    |
    |COMMIT;
    |"""
    .stripMargin
    .update()
    .recoverWith {
      _ =>
        //rollback in-case of an error
        "ROLLBACK".update()
    }
```

# Custom `ParamSetter`

TODO

```scala
case class MyColumn(int: Int)

val paramSetter =
  new ParamSetter[MyColumn] {
    override def paramCount(): Int = 1
    override def apply(statement: PreparedStatement, paramIndex: Int, myColumn: MyColumn): Unit =
      statement.setInt(paramIndex, myColumn.int)
  }
```

# Custom `RowParser` and `ColParser`

TODO

A SQL table is just a bunch of a rows and columns. So we have a `RowParser` and a `ColParser`.

A `RowParser` is just a collection of one or many `ColParser(s)`.

```scala
//custom column
case class MyColumn(int: Int)

//custom column parser
val colParser: ColParser[MyColumn] =
  (resultSet: ResultSet, index: Int) =>
    MyColumn(resultSet.getInt(1))
```

# Slick interop

Make sure the dependency `justsql-slick` is in your build.

```scala
//Your Slick DB config 
val dbConfig: DatabaseConfig[JdbcProfile] = ???
//Just pass it onto JustSQL
implicit val justSQL = JustSQL(SlickSQLConnector(dbConfig))
```

# HikariCP interop

Make sure the dependency `justsql-hikari` is in your build.

```scala
//Pass HikariSQLConnector to JustSQL
implicit val db = JustSQL(HikariSQLConnector())
```

# Unsafe

Unsafe APIs give direct access to low level `java.sql.ResultSet` type.

## unsafeSelect()

```scala
//read the names of all Users
val names: Try[Array[String]] = "SELECT * FROM USERS".unsafeSelect(_.getString("name"))
```

## unsafeSelectOne()

```scala
val unsafeCount: Try[Int] = "SELECT count(*) as count FROM USERS".unsafeSelectOne(_.getInt("count"))
```

