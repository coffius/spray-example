package com.example.model

/**
 * Папка с ссылками
 */
case class Folder(id: Option[Long] = None, ownerId: Long, title: String) extends Entity
