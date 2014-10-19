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
import com.example.repo.{ClickRepo, LinkRepo, UserRepo}

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class LinkRestController(private val userRepo: UserRepo = new UserRepo(),
                         private val linkRepo: LinkRepo = new LinkRepo(),
                         private val clickRepo: ClickRepo = new ClickRepo())
  extends Directives
  with WithDb
{

  val route =
    path("link") {
      post {
        entity(as[CreateLinkRequest]){ request =>
          require(!request.url.trim.isEmpty, "url can`t be empty empty")

          db.withSession{ implicit session =>
            val userOpt = userRepo.findByToken(request.token)
            userOpt.fold{
              respondWithStatus(StatusCode.int2StatusCode(404)){ complete{ "" } }
            }{ owner =>
              val newLink = Link(
                ownerId = owner.id.get,
                folderId = request.folderId,
                url = request.url,
                code = request.code.getOrElse(generateCode)
              )

              linkRepo.save(newLink)

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

        val foundLink = db.withSession{ implicit session =>
          val linkOpt = linkRepo.findLinkByCode(code)
          clickRepo.save(
            Click(
              date = DateTime.now(),
              referer = request.referer,
              remoteIp = request.remoteIp
            )
          )
          linkOpt
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
