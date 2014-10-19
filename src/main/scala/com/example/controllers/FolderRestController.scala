package com.example.controllers

import spray.routing.Directives
import com.example.dto.{LinkData, LinkListDataResponse, LinkListDataRequest}
import com.example.repo.{LinkRepo, FolderRepo, UserRepo}
import spray.http.StatusCode
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
        ).as(LinkListDataRequest){ request: LinkListDataRequest =>
          val result = db.withSession{ implicit session =>
            for{
              user <- userRepo.findByToken(request.token)
              folder <- folderRepo.findById(folderId)
              links = linkRepo.findByOwnerAndFolder(user.id.get, folder.id.get, request.offset, request.offset)
            } yield {
              val linkDataSeq = links.map(link => LinkData(link.url, link.code))
              LinkListDataResponse(linkDataSeq)
            }
          }

          result.fold{
            respondWithStatus(StatusCode.int2StatusCode(404)){ complete{ "" } }
          }{ response =>
            complete(response)
          }

          complete("")
        }
      }
    }
}
