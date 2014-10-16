package com.example.dao

import scala.slick.driver.PostgresDriver.simple._
import com.example.model.User

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class Users(tag: Tag) extends Table[User](tag, "users") {
  def id: Column[Long] = column[Long]("id", O.PrimaryKey)
  def token: Column[String] = column[String]("token")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, token) <> (User.tupled, User.unapply)
}

