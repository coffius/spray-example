package com.example.dto

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
case class GetLinkDataResponse(link: LinkData, folderId: Option[Long], clickCount: Long)
