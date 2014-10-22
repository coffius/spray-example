package com.example.controllers

import com.example.auth.BearerAuthenticator
import spray.routing.Directives
import scala.concurrent.{ExecutionContext, Future}
import scala.slick.driver.PostgresDriver.simple._
import spray.httpx.SprayJsonSupport._
import com.example.dto.JsonProtocol._
import com.example.dto._
import scala.slick.lifted.TableQuery
import com.example.dao.{Clicks, Links, Users}
import spray.http.StatusCode
import com.example.model.{User, Click, Link}
import java.util.UUID
import spray.http.MediaTypes._
import com.example.dto.CreateLinkRequest
import com.example.dto.CreateLinkResponse
import org.joda.time.DateTime
import com.example.repo.{ClickRepo, LinkRepo, UserRepo}

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class LinkRestController(val userRepo: UserRepo = new UserRepo(),
                         private val linkRepo: LinkRepo = new LinkRepo(),
                         private val clickRepo: ClickRepo = new ClickRepo())
  extends Directives
  with WithAuth
  with WithDb
{

  private implicit val execContext = ExecutionContext.global

  val route =
    path("link") {
      post {
        authenticate(BearerAuthenticator(auth)){ owner =>
          entity(as[CreateLinkRequest]){ request =>
            require(!request.url.trim.isEmpty, "url can`t be empty empty")

            db.withSession{ implicit session =>
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
          linkOpt.foreach{ link =>
            clickRepo.save(
              Click(
                linkId = link.id.get,
                date = DateTime.now(),
                referer = request.referer,
                remoteIp = request.remoteIp
              )
            )
          }
          linkOpt
        }



        foundLink.fold {
          respondWithStatus(StatusCode.int2StatusCode(404)){ complete{ "" } }
        }{ link =>
          complete { link.url }
        }
      }
    }
  } ~
  pathPrefix("link" / Segment){ code =>
    get {
      authenticate(BearerAuthenticator(auth)){ owner =>
        val result = db.withSession{ implicit session =>
          for{
            link <- linkRepo.findLinkByCode(code)
          } yield {
            if(owner.id.get != link.ownerId){
              None
            } else {
              val count = clickRepo.countByLink(link.id.get)
              Some(
                GetLinkDataResponse(
                  link = LinkData(
                    link.url,
                    link.code
                  ),
                  folderId = link.folderId,
                  clickCount = count
                )
              )
            }
          }
        }

        result.fold{
          respondWithStatus(StatusCode.int2StatusCode(404)){ complete{ "" } }
        }{ response =>
          complete(response)
        }
      }
    }
  } ~
  path("link"){
    get{
      authenticate(BearerAuthenticator(auth)){ user =>
        parameters(
          'offset.as[Option[Int]],
          'limit.as[Option[Int]]
        ).as(PageRequest){ request: PageRequest =>
          val result = db.withSession{ implicit session =>
              linkRepo
                .findAllByOwner(user.id.get, request.offset, request.limit)
                .map(link => LinkData(link.url, link.code))
          }

          complete(result)
        }
      }
    }
  } ~
  pathPrefix("link" / Segment){ code =>
    path("clicks"){
      authenticate(BearerAuthenticator(auth)){ user =>
        get{
          parameters(
            'offset.as[Option[Int]],
            'limit.as[Option[Int]]
          ).as(PageRequest) { request: PageRequest =>
            val result = db.withSession { implicit session =>
                clickRepo
                  .findAllByOwnerAndCode(user.id.get, code, request.offset, request.limit)
                  .map(click => ClickData(click.date.getMillis, click.referer, click.remoteIp))
            }

            complete(result)
          }
        }
      }
    }
  }

  private def generateCode = UUID.randomUUID().toString
}
