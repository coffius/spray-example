package com.example.repo

import com.example.dao.Links
import com.example.model.Link
import scala.slick.lifted.TableQuery

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class LinkRepo extends EntityRepo[Link, Links]{
  override def table: TableQuery[Links] = TableQuery[Links]
}
