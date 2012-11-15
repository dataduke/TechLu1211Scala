package models

import anorm._
import anorm.SqlParser._
import java.sql.Connection

case class Person(id: Long, givenname: String, surname: String, age: Int)

object Person {

  val parser = get[Long]("id") ~
    get[String]("givenname") ~
    get[String]("surname") ~
    get[Int]("age") map {
    case i ~ g ~ s ~ a => Person(i,g,s,a)
  }

  def insert(p: Person)(implicit conn: Connection): Long = {
    SQL("insert into person (givenname, surname, age) values ({g},{s},{a})")
      .on('g -> p.givenname, 's -> p.surname, 'a -> p.age).executeInsert(scalar[Long].single)
  }

  def findAll()(implicit conn: Connection): List[Person] = {
    SQL("select id, givenname, surname, age from person order by surname")
      .as(parser *)
  }

}