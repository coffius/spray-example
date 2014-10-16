package com.example.controllers

import akka.actor.Actor
import spray.routing.HttpService
import spray.http.MediaTypes._

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class UserRestControllerActor extends Actor with UserRestController{
  def actorRefFactory = context
  def receive = runRoute(myRoute)
}

trait UserRestController extends HttpService{
  val myRoute =
    path("") {
      get {
        respondWithMediaType(`text/html`) { // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            <html>
              <body>
                <h1>Say hello to <i>spray-routing</i> on <i>spray-can</i>!</h1>
              </body>
            </html>
          }
        }
      }
    }
}
