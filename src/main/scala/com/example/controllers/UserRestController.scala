package com.example.controllers

import spray.routing.Directives
import spray.http.MediaTypes._
import com.example.dto.{TokenResponse, TokenRequest}
import spray.httpx.SprayJsonSupport._
import spray.httpx.marshalling._
import com.example.dto.JsonProtocol._

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class UserRestController extends Directives{
  val route =
    path("token") {
      get {
        parameters('user_id.as[Int], 'secret.as[String]).as(TokenRequest){ tokenRequest: TokenRequest =>
          respondWithMediaType(`application/json`) {
            complete {
              TokenResponse("sample_token")
            }
          }
        }
      }
    }
}
