package com.example.controllers

import spray.routing.Directives
import com.example.repo.{LinkRepo, FolderRepo, UserRepo}
import spray.http.StatusCode
import com.example.dto.LinkData
import com.example.dto.FolderData
import com.example.dto.PageRequest
import spray.httpx.SprayJsonSupport._
import com.example.dto.JsonProtocol._

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class FolderRestController(private val userRepo: UserRepo = new UserRepo,
                           private val folderRepo: FolderRepo = new FolderRepo,
                           private val linkRepo: LinkRepo = new LinkRepo)
  extends Directives
  with WithDb
{
  val route =
    pathPrefix("folder" / LongNumber){ folderId =>
      get{
        parameters(
          'token,
          'offset.as[Option[Int]],
          'limit.as[Option[Int]]
        ).as(PageRequest){ request: PageRequest =>
          val result = db.withSession{ implicit session =>
            for{
              user <- userRepo.findByToken(request.token)
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
    } ~
  path("folder"){
    get{
      parameters('token){ token =>
        val result = db.withSession{ implicit session =>
          for{
            user <- userRepo.findByToken(token)
            folders = folderRepo.findByOwner(user.id.get)
          } yield {
            folders.map(folder => FolderData(folder.id.get, folder.title))
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
}
