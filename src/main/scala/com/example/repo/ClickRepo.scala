package com.example.repo

import com.example.model.Click
import com.example.dao.Clicks
import scala.slick.lifted.TableQuery
import scala.slick.driver.PostgresDriver.simple._

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class ClickRepo extends EntityRepo[Click, Clicks]{
  override def table: TableQuery[Clicks] = TableQuery[Clicks]

  def countByLink(linkId: Long)(implicit session: Session): Int = {
    val result = for{
      c <- table if c.linkId === linkId
    } yield c
    result.length.run
  }
}
