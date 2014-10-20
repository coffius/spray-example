package com.example.controllers

import scala.slick.driver.PostgresDriver.simple._
/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
trait WithDb {
  def db = WithDb.db
}

object WithDb{
  private val host = System.getProperty("db.host", "localhost")
  private val port = System.getProperty("db.port", "5432")
  private val name = System.getProperty("db.name", "spray-example")
  private val url: String = s"jdbc:postgresql://$host:$port/$name"

  val db = {

    Database.forURL(
      url = url,
      driver = "org.postgresql.Driver",
      user = "spray"
    )
  }
}
