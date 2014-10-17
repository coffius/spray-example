package com.example

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import com.example.controllers.{LinkRestController, UserRestController}


class Api extends HttpService with Actor{
  val routes = {
    new UserRestController().route ~
    new LinkRestController().route
  }
  def actorRefFactory = context
  def receive = runRoute(routes)
}