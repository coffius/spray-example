package com.example.dao

import scala.slick.lifted.Column

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
trait EntityDao {
  def id: Column[Long]
}
