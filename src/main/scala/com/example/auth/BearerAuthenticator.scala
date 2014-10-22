package com.example.auth

import com.example.model.User
import spray.http._
import spray.routing.RequestContext
import spray.routing.authentication.HttpAuthenticator
import HttpHeaders._

import scala.concurrent.{Future, ExecutionContext}

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class BearerAuthenticator(val tokenAuthenticator: UserTokenAuthenticator[User])
                            (implicit val executionContext: ExecutionContext) extends HttpAuthenticator[User]{
  override def authenticate(credentials: Option[HttpCredentials], ctx: RequestContext): Future[Option[User]] = {
    val tokenOpt = credentials.flatMap{
      case OAuth2BearerToken(token) => Some(token)
      case _ => None
    }
    tokenAuthenticator(tokenOpt)
  }

  override def getChallengeHeaders(httpRequest: HttpRequest): List[HttpHeader] = {
    Authorization(OAuth2BearerToken(token = "")) :: Nil
  }
}

object BearerAuthenticator {
  def apply(tokenAuthenticator: UserTokenAuthenticator[User])
           (implicit executionContext: ExecutionContext) = new BearerAuthenticator(tokenAuthenticator)
}
