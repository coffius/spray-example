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
import com.example.model.User
import java.util.UUID

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
          //проверяем секретный ключ
          if(tokenRequest.secret != secretCode){
            respondWithStatus(StatusCode.int2StatusCode(404)){
              complete{ "" }
            }
          } else {
            //если ключи совпадают - создаем новый или получаем существующий токен юзера
            db.withSession{ implicit session =>
              val userQuery = for{
                u <- users if u.id === tokenRequest.userId
              } yield u
              val userToken = userQuery.firstOption.fold{
                val newToken: String = UUID.randomUUID().toString
                users += User(id = Some(tokenRequest.userId), token = newToken)
                newToken
              }{
                _.token
              }

              respondWithMediaType(`application/json`) {
                complete {
                  TokenResponse(userToken)
                }
              }
            }
          }
        }
      }
    }
}
