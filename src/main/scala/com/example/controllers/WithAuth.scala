package com.example.controllers

import com.example.model.User
import com.example.repo.UserRepo

import scala.concurrent.Future

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
trait WithAuth { self: WithDb =>
  val userRepo: UserRepo

  def auth(tokenOpt: Option[String]): Future[Option[User]] = {
    Future.successful(tokenOpt.flatMap{
      db.withSession { implicit session =>
        userRepo.findByToken(_)(session)
      }
    })
  }
}
