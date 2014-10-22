package com.example.dto

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
case class CreateLinkRequest(url: String,
                             code: Option[String],
                             folderId:Option[Long])
