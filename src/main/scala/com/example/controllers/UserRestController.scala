package com.example.controllers

import spray.routing.Directives
import spray.http.MediaTypes._
import com.example.dto.{TokenResponse, TokenRequest}
import spray.httpx.SprayJsonSupport._
import com.example.dto.JsonProtocol._
import spray.http.StatusCode
import scala.slick.lifted.TableQuery
import com.example.dao.Users
import scala.slick.driver.PostgresDriver.simple._
/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class UserRestController extends Directives with WithDb{
  private val secretCode = "qwer"

  private val users = TableQuery[Users]

  val route =
    path("token") {
      get {
        parameters('user_id.as[Int], 'secret.as[String]).as(TokenRequest){ tokenRequest: TokenRequest =>
          if(tokenRequest.secret != secretCode){
            respondWithStatus(StatusCode.int2StatusCode(404)){
              complete{ "" }
            }
          } else {
            db.withSession{ implicit session =>
              val userQuery = for{
                u <- users if u.id === tokenRequest.userId
              } yield u
              userQuery.firstOption.fold{
                respondWithStatus(StatusCode.int2StatusCode(404)){ complete{ "" } }
              }{ user =>
                respondWithMediaType(`application/json`) {
                  complete {
                    TokenResponse(user.token)
                  }
                }
              }

            }
          }
        }
      }
    }
}
