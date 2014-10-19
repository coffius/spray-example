package com.example.controllers

import spray.routing.Directives
import com.example.dto.{LinkData, FolderLinkDataResponse, FolderLinkDataRequest}
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
      parameters(
        'token,
        'offset.as[Option[Int]],
        'limit.as[Option[Int]]
      ).as(FolderLinkDataRequest){ request: FolderLinkDataRequest =>
        val result = db.withSession{ implicit session =>
          for{
            user <- userRepo.findByToken(request.token)
            folder <- folderRepo.findById(folderId)
            links = linkRepo.findByOwnerAndFolder(user.id.get, folder.id.get, request.offset, request.offset)
          } yield {
            val linkDataSeq = links.map(link => LinkData(link.url, link.code))
            FolderLinkDataResponse(linkDataSeq)
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
