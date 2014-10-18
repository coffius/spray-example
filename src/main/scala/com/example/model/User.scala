package com.example.model

/**
 * Пользователь
 */
case class User(id: Option[Long] = None, token: String) extends Entity
