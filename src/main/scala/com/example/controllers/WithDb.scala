package com.example.controllers

import scala.slick.driver.PostgresDriver.simple._
/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
trait WithDb {
  def db = WithDb.db
}

object WithDb{
  val db = Database.forURL(
    url = "jdbc:postgresql://192.168.86.5:5432/spray-example",
    driver = "org.postgresql.Driver",
    user = "spray"
  )
}
