package com.example

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._
import com.example.controllers.UserRestController

class UserRestControllerSpec extends Specification with Specs2RouteTest with Api{
  def actorRefFactory = system
  val route = new UserRestController().route

  "UserRestController" should {

    "return a greeting for GET requests to the root path" in {
      Get() ~> route ~> check {
        responseAs[String] must contain("Say hello")
      }
    }

    "leave GET requests to other paths unhandled" in {
      Get("/kermit") ~> route ~> check {
        handled must beFalse
      }
    }

    "return a MethodNotAllowed error for PUT requests to the root path" in {
      Put() ~> sealRoute(route) ~> check {
        status === MethodNotAllowed
        responseAs[String] === "HTTP method not allowed, supported methods: GET"
      }
    }
  }
}
