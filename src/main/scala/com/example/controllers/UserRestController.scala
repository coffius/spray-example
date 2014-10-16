package com.example.controllers

import spray.routing.Directives
import spray.http.MediaTypes._
import com.example.dto.{TokenResponse, TokenRequest}
import spray.httpx.SprayJsonSupport._
import com.example.dto.JsonProtocol._
import spray.http.StatusCode

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class UserRestController extends Directives{
  private val secretCode = "qwer"
  val route =
    path("token") {
      get {
        parameters('user_id.as[Int], 'secret.as[String]).as(TokenRequest){ tokenRequest: TokenRequest =>
          if(tokenRequest.secret != secretCode){
            respondWithStatus(StatusCode.int2StatusCode(404)){
              complete{
                ""
              }
            }
          } else {
            respondWithMediaType(`application/json`) {
              complete {
                TokenResponse("sample_token")
              }
            }
          }
        }
      }
    }
}
