package com.example.repo

import com.example.model.Click
import com.example.dao.Clicks
import scala.slick.lifted.TableQuery

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class FolderRepo extends EntityRepo[Click, Clicks]{
  override def table: TableQuery[Clicks] = TableQuery[Clicks]

}
