package com.example.repo

import scala.slick.driver.PostgresDriver.simple._
import scala.slick.lifted.TableQuery
import com.example.dao.Users
import scala.slick.jdbc.JdbcBackend
import com.example.model.User

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class UserRepo extends EntityRepo[User, Users]{
  override def table: TableQuery[Users] = TableQuery[Users]
}
