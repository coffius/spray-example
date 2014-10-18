package com.example.repo

import scala.slick.lifted.{TableQuery, AbstractTable}
import scala.slick.driver.PostgresDriver.simple._
import scala.slick.jdbc.JdbcBackend
import com.example.dao.EntityDao
import com.example.model.Entity

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
trait EntityRepo[U <: E#TableElementType with Entity, E <: AbstractTable[U] with EntityDao] {
  type Session = JdbcBackend#Session

  def table: TableQuery[E]

  def save(entity: U)(implicit session: Session): Unit = table += entity

  def findById(id: Long)(implicit session: Session): Option[U]= {
    val findQuery = for{
      v <- table if v.id === id
    } yield v
    findQuery.firstOption
  }
}
