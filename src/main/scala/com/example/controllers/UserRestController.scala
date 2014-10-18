package com.example.controllers

import spray.routing.Directives
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport._
import com.example.dto.JsonProtocol._
import com.example.dto.{TokenResponse, TokenRequest}
import spray.http.StatusCode
import com.example.model.User
import java.util.UUID
import com.example.repo.UserRepo
import com.example.service.GeneratorService

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class UserRestController(private val userRepo: UserRepo = new UserRepo(),
                         private val generatorService: GeneratorService = new GeneratorService())
  extends Directives
  with WithDb
{
  private val secretCode = "qwer"

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
              val userOpt = userRepo.findById(tokenRequest.userId)
              val userToken = userOpt.fold{
                val newToken = generatorService.generateUserToken
                userRepo.save(User(id = Some(tokenRequest.userId), token = newToken))
                newToken
              }{
                _.token
              }

              respondWithMediaType(`application/json`) {
                complete(TokenResponse(userToken))
              }
            }
          }
        }
      }
    }
}
