package com.example.repo

import com.example.model.Click
import com.example.dao.{Links, Clicks}
import scala.slick.lifted.TableQuery
import scala.slick.driver.PostgresDriver.simple._

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class ClickRepo extends EntityRepo[Click, Clicks]{
  override def table: TableQuery[Clicks] = TableQuery[Clicks]
  private def links = TableQuery[Links]

  def countByLink(linkId: Long)(implicit session: Session): Int = {
    val result = for{
      c <- table if c.linkId === linkId
    } yield c
    result.length.run
  }

  def findAllByOwnerAndCode(ownerId: Long,
                            linkCode: String,
                            offset: Option[Int] = None,
                            limit: Option[Int] = None)(implicit session: Session): Seq[Click] = {
    val query = for {
      (click, link) <- table leftJoin links on(_.linkId === _.id) if link.code === linkCode
    } yield click

    val drop = offset.getOrElse(0)
    val take = limit.getOrElse(10)
    query.drop(drop).take(take).run
  }
}
