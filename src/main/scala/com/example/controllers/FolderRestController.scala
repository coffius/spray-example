package com.example.controllers

import com.example.auth.BearerAuthenticator
import spray.routing.Directives
import com.example.repo.{LinkRepo, FolderRepo, UserRepo}
import spray.http.{StatusCodes, StatusCode}
import com.example.dto.LinkData
import com.example.dto.FolderData
import com.example.dto.PageRequest
import spray.httpx.SprayJsonSupport._
import com.example.dto.JsonProtocol._

import scala.concurrent.ExecutionContext

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class FolderRestController(val userRepo: UserRepo = new UserRepo,
                           private val folderRepo: FolderRepo = new FolderRepo,
                           private val linkRepo: LinkRepo = new LinkRepo)
  extends Directives
  with WithDb
  with WithAuth
{
  private implicit val execContext = ExecutionContext.global

  val route =
    pathPrefix("folder" / LongNumber){ folderId =>
      get{
        authenticate(BearerAuthenticator(auth)){ user =>
          parameters(
            'offset.as[Option[Int]],
            'limit.as[Option[Int]]
          ).as(PageRequest){ request: PageRequest =>
            val result = db.withSession{ implicit session =>
              for{
                folder <- folderRepo.findById(folderId)
                links = linkRepo.findByOwnerAndFolder(user.id.get, folder.id.get, request.offset, request.offset)
              } yield {
                links.map(link => LinkData(link.url, link.code))
              }
            }

            result.fold{
              respondWithStatus(StatusCode.int2StatusCode(404)){ complete{ "" } }
            }{ response =>
              complete(response)
            }
          }
        }
      }
    } ~
  path("folder"){
    get{
      authenticate(BearerAuthenticator(auth)){ user =>
        val result = db.withSession{ implicit session =>
          folderRepo
            .findByOwner(user.id.get)
            .map(folder => FolderData(folder.id.get, folder.title))
        }

        complete(result)
      }
    }
  }
}
