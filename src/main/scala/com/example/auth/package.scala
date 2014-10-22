package com.example

import spray.routing.authentication.UserPass

import scala.concurrent.Future

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
package object auth {
  type UserTokenAuthenticator[T] = Option[String] ⇒ Future[Option[T]]
}
