package com.example.controllers

import spray.routing.Directives
import scala.slick.driver.PostgresDriver.simple._
import spray.httpx.SprayJsonSupport._
import com.example.dto.JsonProtocol._
import com.example.dto._
import scala.slick.lifted.TableQuery
import com.example.dao.{Clicks, Links, Users}
import spray.http.StatusCode
import com.example.model.{Click, Link}
import java.util.UUID
import spray.http.MediaTypes._
import com.example.dto.CreateLinkRequest
import com.example.dto.CreateLinkResponse
import org.joda.time.DateTime

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class LinkRestController extends Directives with WithDb{
  private val users = TableQuery[Users]
  private val links = TableQuery[Links]
  private val clicks = TableQuery[Clicks]

  val route =
    path("link") {
      post {
        entity(as[CreateLinkRequest]){ request =>
          require(!request.url.trim.isEmpty, "url can`t be empty empty")

          val findUserByTokenQuery = for{
            u <- users if u.token === request.token
          } yield u

          db.withSession{ implicit session =>
            findUserByTokenQuery.firstOption.fold{
              respondWithStatus(StatusCode.int2StatusCode(404)){ complete{ "" } }
            }{ owner =>
              val newLink = Link(
                ownerId = owner.id.get,
                folderId = request.folderId,
                url = request.url,
                code = request.code.getOrElse(generateCode)
              )

              links += newLink

              respondWithMediaType(`application/json`) {
                complete {
                  CreateLinkResponse(newLink.url, newLink.code)
                }
              }
            }
          }
        }
      }
    } ~
  pathPrefix("link" / Segment){ code =>
    post {
      entity(as[PostLinkDataRequest]){request =>
        val findLinkQuery = for{
          link <- links if link.code === code
        } yield link

        val foundLink = db.withSession{ implicit session =>
          clicks += Click(
            date = DateTime.now(),
            referer = request.referer,
            remoteIp = request.remoteIp
          )
          findLinkQuery.firstOption
        }



        foundLink.fold {
          respondWithStatus(StatusCode.int2StatusCode(404)){ complete{ "" } }
        }{ link =>
          complete { link.url }
        }
      }
    }
  }

  private def generateCode = UUID.randomUUID().toString
}
