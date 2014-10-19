package com.example.dao

import scala.slick.driver.PostgresDriver.simple._
import com.example.model.Folder

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class Folders(tag: Tag) extends Table[Folder](tag, "folders") with EntityDao{
  def id: Column[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def ownerId: Column[Long] = column[Long]("owner_id")
  def title: Column[String] = column[String]("title")

  def * = (id.?, ownerId, title) <> (Folder.tupled, Folder.unapply)
}
