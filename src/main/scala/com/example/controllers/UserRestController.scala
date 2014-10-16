package com.example.controllers

import akka.actor.{ActorRefFactory, Actor}
import spray.routing.{Directives, HttpService}
import spray.http.MediaTypes._

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class UserRestController extends Directives{
  val route =
    path("register") {
      get {
        respondWithMediaType(`text/html`) {
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
