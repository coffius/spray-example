package com.example.model

/**
 * Ссылка
 */
case class Link(id: Option[Long] = None,
                ownerId: Long,
                folderId: Option[Long],
                url: String,
                code: String)
