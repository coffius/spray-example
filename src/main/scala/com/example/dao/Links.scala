package com.example.dao

import scala.slick.driver.PostgresDriver.simple._
import com.example.model.Link

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class Links(tag: Tag) extends Table[Link](tag, "links") with EntityDao{
  def id: Column[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def ownerId: Column[Long] = column[Long]("owner_id")
  def folderId: Column[Option[Long]] = column[Option[Long]]("folder_id")
  def url: Column[String] = column[String]("url")
  def code: Column[String] = column[String]("code")

  // Every table needs a * projection with the same type as the table's type parameter
  def * = (id.?, ownerId, folderId, url, code) <> (Link.tupled, Link.unapply)
}