package com.example.dto

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
case class LinkListDataRequest(token: String, offset: Option[Int], limit: Option[Int])
