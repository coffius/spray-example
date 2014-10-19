package com.example.dto

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
case class PageRequest(token: String, offset: Option[Int], limit: Option[Int])
