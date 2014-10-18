package com.example

import org.specs2.mutable.Specification
import spray.testkit.Specs2RouteTest
import spray.http._
import StatusCodes._
import com.example.controllers.UserRestController
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.mockito.Matchers.{eq => mockEq}
import com.example.repo.UserRepo
import com.example.service.GeneratorService
import com.example.model.User
import com.example.dto.TokenResponse
import spray.httpx.SprayJsonSupport._
import com.example.dto.JsonProtocol._
import MediaTypes._

class UserRestControllerSpec extends Specification with Specs2RouteTest with Api{
  def actorRefFactory = system

  "UserRestController" should {

    "should return 404 if secret is invalid" in {
      val userRepo = mock(classOf[UserRepo])
      val generatorService = mock(classOf[GeneratorService])
      val route = new UserRestController(userRepo, generatorService).route

      Get("/token?user_id=1&secret=invalid") ~> route ~> check {
        response.status.intValue must beEqualTo(404)
      }
    }

    "return token of exist user if it was found" in {
      val userRepo = mock(classOf[UserRepo])
      val token: String = "exist_test_token"
      when(userRepo.findById(mockEq(1L))(any())).thenReturn(Some(User(id = Some(1), token = token)))
      val generatorService = mock(classOf[GeneratorService])
      val route = new UserRestController(userRepo, generatorService).route

      Get("/token?user_id=1&secret=qwer") ~> route ~> check {
        responseAs[TokenResponse].token must_== token
      }
    }

    "should save new user if it doesn`t exist yet" in {
      val newToken: String = "new_token"

      val userRepo = mock(classOf[UserRepo])
      when(userRepo.findById(mockEq(1L))(any())).thenReturn(None)

      val generatorService = mock(classOf[GeneratorService])
      doReturn(newToken).when(generatorService).generateUserToken

      val route = new UserRestController(userRepo, generatorService).route
      Get("/token?user_id=1&secret=qwer") ~> route ~> check {
        verify(userRepo).save(mockEq(User(Some(1L), newToken)))(any())
        responseAs[TokenResponse].token must_== newToken
      }
    }
  }
}
