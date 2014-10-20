package com.example

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import com.example.controllers.{FolderRestController, LinkRestController, UserRestController}

class ApiActor extends Actor with Api{
  def actorRefFactory = context
  def receive = runRoute(routes)
}

trait Api extends HttpService{
  val routes = {
    new UserRestController().route ~
    new LinkRestController().route ~
    new FolderRestController().route
  }
}