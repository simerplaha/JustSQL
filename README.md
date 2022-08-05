# JustSQL

Just write SQL as `String` and parse results into types.

# Setup

I'm using here HikariCP & Postgres here, but you use any `DataSource` of your choice.

```scala
import justsql._ //single import

implicit val db = JustSQL(datasource = HikariDS()) //create database instance
```

The code snippets below can be found in [Example.scala](/JustSQL/src/test/scala/example/Example.scala).

# update()

Queries that mutate like `CREATE, INSERT OR UPDATE` queries are executed via `update()` function.

Let's create our example `USERS` table

```scala
//create table
val create: Try[Int] = "CREATE TABLE USERS (id INT, name VARCHAR)".update()
//insert rows
val insert: Try[Int] = "INSERT INTO USERS (id, name) VALUES (1, 'Harry'), (2, 'Ayman')".update()
```

## Use query parameters

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

## Transactionally

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
        //if there was an error rollback
        "ROLLBACK".update()
    }
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

Expects `1` row else returns `Failure`

```scala
val count: Try[Int] = "SELECT count(*) FROM USERS".selectOne[Int]()
```

# Parser

There are two types for parsing a row

- `RowParser` - Parser for a table row. It's a combination of one or many `ColParser(s)`.
- `ColParser` - Parser for a table column.

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
