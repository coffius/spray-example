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
}
