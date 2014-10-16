package com.example.dto

import spray.json.DefaultJsonProtocol

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
object JsonProtocol extends DefaultJsonProtocol{
  implicit val TokenRequestFormat = jsonFormat2(TokenRequest)
  implicit val TokenResponseFormat = jsonFormat1(TokenResponse)
}
