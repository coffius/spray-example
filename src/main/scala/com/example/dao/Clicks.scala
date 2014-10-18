package com.example.dao

import scala.slick.driver.PostgresDriver.simple._
import com.github.tototoshi.slick.PostgresJodaSupport._
import com.example.model.{Click, User}
import org.joda.time.DateTime

/**
* @author coffius@gmail.com (Aleksei Shamenev)
*/
class Clicks (tag: Tag) extends Table[Click](tag, "clicks") {
  def id: Column[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def date: Column[DateTime] = column[DateTime]("date")
  def referer: Column[String] = column[String]("referer")
  def remoteIp: Column[String] = column[String]("remote_ip")

  def * = (id.?, date, referer, remoteIp) <> (Click.tupled, Click.unapply)
}
