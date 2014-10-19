package com.example.repo

import com.example.dao.Links
import com.example.model.Link
import scala.slick.lifted.TableQuery
import scala.slick.driver.PostgresDriver.simple._

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class LinkRepo extends EntityRepo[Link, Links]{
  override def table: TableQuery[Links] = TableQuery[Links]

  def findLinkByCode(code: String)(implicit session: Session): Option[Link] = {
    val query = for{
      l <- table if l.code === code
    } yield l

    query.firstOption
  }

  def findByOwnerAndFolder(ownerId: Long,
                           folderId: Long,
                           offset: Option[Int] = None,
                           limit: Option[Int] = None)(implicit session: Session): Seq[Link] = {
    val query = for{
      link <- table if link.ownerId === ownerId
    } yield link

    val withOffset = offset.fold(query)(query.drop)
    val withLimit = limit.fold(withOffset)(withOffset.take)
    withLimit.run
  }
}
