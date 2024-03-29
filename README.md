# JustSQL

Just write SQL as `String` and parse results into types.

A lightweight SQL library that adds type-safety to query results & parameters,
allowing plain SQL queries.

Can be used in parallel with other libraries.
Interop for [Slick](#slick-interop) or [HikariCP](#hikaricp-interop) is provided.

Small: 407KB jar file. No external core dependency.

## Why another SQL library?

- ORMs, DSLs and custom string interpolation solutions are nice, but most are incomplete and restrictive, specially
  when writing complex SQL queries.
- Debugging performance issues in ORM generated queries, translating back and forth between SQL and ORM types is
  time-consuming.
- Many ORMs do not have any support for `EXPLAIN ANALYZE` statements.
- IDEs have much better support/plugins for executing and analysing plain SQL queries versus custom DSLs.

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

# _NOTE: The following documentation and release is WIP_

[//]: # (# Setup)

[//]: # ()

[//]: # (```scala)

[//]: # (libraryDependencies ++= Seq&#40;)

[//]: # (  "com.github.simerplaha" %% "justsql" % "0.1.0",)

[//]: # (  //Optional: For Slick interop)

[//]: # (  "com.github.simerplaha" %% "justsql-slick" % "0.1.0",)

[//]: # (  //Optional: For hikariCP interop)

[//]: # (  "com.github.simerplaha" %% "justsql-hikari" % "0.1.0")

[//]: # (&#41;)

[//]: # (```)

# Quick start

See quick-start [Example.scala](/justsql/src/test/scala/example/Example.scala).

# Create JustSQL

I'm using Postgres and the default `JavaSQLConnector` here, but you should use a high-performance
JDBC connection pool library. See interop for [Slick](#slick-interop) or [HikariCP](#hikaricp-interop).

A `JustSQL` instance is only required when executing a query i.e. when invoking `runSync()` or `runAsync()`.

Everywhere else queries are declarative, so you define your queries without executing them so they can be
[embedded](#embed-queries) and [composed](#compose-queries).

```scala
import justsql._ //single import

implicit val db = JustSQL(datasource = JavaSQLConnector()) //create database instance
```

# `update()`

Queries that mutate like `CREATE, INSERT OR UPDATE` queries are executed via `update()` function.

Let's create our example `USERS` table.

```scala
//create table
val create: Try[Int] = "CREATE TABLE USERS (id INT, name VARCHAR)".update().runSync()
//insert rows
val insert: Try[Int] =
  """
    |INSERT INTO USERS (id, name)
    |           VALUES (1, 'Harry'),
    |                  (2, 'Ayman')
    |""".stripMargin.update().runSync()
```

## for-comprehension

Or execute the above queries using `for`-comprehension

```scala
val createAndInsert: Sql[(Int, Int)] =
  for {
    create <- "CREATE TABLE USERS (id INT, name VARCHAR)".update()
    insert <- "INSERT INTO USERS (id, name) VALUES (1, 'Harry'), (2, 'Ayman')".update()
  } yield (create, insert)

val result: Try[(Int, Int)] = createAndInsert.runSync()
```

# Query parameters

SQL parameters are set with the suffix `?`.

The above `INSERT` query can be written with parameters as following

```scala
//Or insert using parameters
val insertParametric: Try[Int] =
  UpdateSQL {
    implicit params =>
      s"""
         |INSERT INTO USERS (id, name)
         |           VALUES (${1.?}, ${"Harry".?}),
         |                  (${2.?}, ${"Ayman".?})
         |""".stripMargin
  }.runSync()
```

# Transactionally

Being just SQL, transactions are written with the usual `BEGIN;` and `COMMIT;` statements.

```scala
val transaction: Try[Int] =
  UpdateSQL {
    implicit params =>
      s"""
         |BEGIN;
         |
         |CREATE TABLE USERS (id INT, name VARCHAR);
         |
         |INSERT INTO USERS (id, name)
         |           VALUES (${1.?}, ${"Harry".?}),
         |                  (${2.?}, ${"Ayman".?});
         |
         |COMMIT;
         |""".stripMargin
  }.recoverWith {
    _ =>
      "ROLLBACK".update() //if there was an error rollback
  }.runSync()
```

# `select()`

First, we need to create a `case class` that represents a table row, which in our case is a `User`

```scala
//case class that represents a table row
case class User(id: Int, name: String)
//Build a row reader for User
implicit val userReader = RowReader(User.tupled)
```

Read all `User`s

```scala
val users: Try[ArraySeq[User]] = "SELECT * FROM USERS".select[User]().runSync()
```

## Or if you want a `List`, provide it as a type argument

```scala
val usersCollected: Try[List[User]] = "SELECT * FROM USERS".select[User, List]().runSync()
```

## Or with Parameters

```scala
val usersParametric: SelectSQL[String, ArraySeq] =
  SelectSQL[String] {
    implicit params: Params =>
      s"""
         |SELECT name from USERS where id = ${1.?}
         |""".stripMargin
  }
```

# `head()`, `headOption()`, `exactlyOne()`

Returns `Some(first element)` from the query result or-else `None` if empty

```scala
val headOption: Try[Option[User]] = "SELECT * FROM USERS".select[User]().headOption().runSync()
```

First element from the query result

```scala
val head: Try[Int] = "SELECT max(id) FROM USERS".select[Int]().head().runSync()
```  

Expects always one row in the result. If there are more than one, returns a failure.

```scala
val exactlyOne: Try[Int] = "SELECT count(*) FROM USERS".select[Int]().exactlyOne().runSync()
```

# Embed queries - `embed`

Embed queries using `embed` function.

```scala
val query1: SelectSQL[Int, ArraySeq] =
  "SELECT max(id) from USERS".select[Int]()

//This query embeds query1 by calling `query1.embed`
val query2: Try[ArraySeq[String]] =
  SelectSQL[String] {
    implicit params: Params =>
      s"""
         |SELECT name from USERS
         | WHERE id = (${query1.embed})
         |""".stripMargin
  }.runSync()
```

# Compose queries

TODO

# Sequence

Run multiple queries in the same connection

```scala
val sequence: SQL[Seq[Int]] =
  SQL.sequence(
    "SELECT 1".select[Int](),
    "SELECT 2".select[Int](),
    "SELECT 3".select[Int]()
  ).map(_.flatten)
```

# Failed or Success `SQL[T]`

Create a successful SQL

```scala
val success: SQL[Int] =
  SQL.success[Int](123)
```

Create a failed SQL

```scala
val failed: SQL[Int] =
  SQL.failure[Int](new Exception("Something went wrong"))
```

# Custom `ParamWriter`

## `ParamWriter` - Data types with single or multiple JDBC parameters

TODO

## `OneParamWriter` - Data types with single JDBC parameter

```scala
//My custom data types
case class MyColumn(int: Int)

//Writer. See ParamWriter for more examples.
val paramWriter: OneParamWriter[MyColumn] =
  (statement: PositionedPreparedStatement, myColumn: MyColumn) =>
    statement setInt myColumn.int
```

# Custom `RowReader` and `ColReader`

A SQL table is just a bunch of a rows and columns right. So we have a `RowReader` and a `ColReader` to represent those.

## `RowReader`

A `RowReader` is just a collection of one or many `ColReader(s)`.

TODO

## `ColReader`

```scala
//custom column
case class MyColumn(int: Int)

//custom column reader
val colReader: ColReader[MyColumn] =
  (resultSet: ResultSet, index: Int) =>
    MyColumn(resultSet.getInt(1))
```

# Slick interop

Make sure the dependency [`justsql-slick`](#setup) is in your build.

This allows JustSQL to borrow connections created by Slick.

```scala
//Your Slick database-config 
val dbConfig: DatabaseConfig[JdbcProfile] = ???
//Just pass it onto JustSQL
implicit val justSQL = JustSQL(SlickSQLConnector(dbConfig))
```

# HikariCP interop

Make sure the dependency [`justsql-hikari`](#setup) is in your build.

```scala
//Pass HikariSQLConnector to JustSQL
implicit val justSQL = JustSQL(HikariSQLConnector())
```

# Unsafe

Unsafe APIs give direct access to low level `java.sql.ResultSet` type.

## unsafeSelect()

```scala
//read the names of all Users
val names: Try[Array[String]] = "SELECT * FROM USERS".unsafeSelect(_.getString("name")).runSync()
```

