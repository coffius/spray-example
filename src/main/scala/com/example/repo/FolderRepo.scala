package com.example.repo

import com.example.model.Folder
import com.example.dao.Folders
import scala.slick.lifted.TableQuery
import scala.slick.driver.PostgresDriver.simple._

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class FolderRepo extends EntityRepo[Folder, Folders]{
  override def table: TableQuery[Folders] = TableQuery[Folders]

  def findByOwner(ownerId: Long,
                  offset: Option[Int] = None,
                  limit: Option[Int] = None)(implicit session: Session): Seq[Folder] = {
    val query = for{
      f <- table if f.ownerId === ownerId
    } yield f

    val withOffset = offset.fold(query)(query.drop)
    val withLimit = limit.fold(withOffset)(withOffset.take)
    withLimit.run
  }
}
