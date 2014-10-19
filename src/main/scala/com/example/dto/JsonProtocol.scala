package com.example.dto

import spray.json.DefaultJsonProtocol

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
object JsonProtocol extends DefaultJsonProtocol{
  implicit val TokenRequestFormat = jsonFormat2(TokenRequest)
  implicit val TokenResponseFormat = jsonFormat1(TokenResponse)
  implicit val CreateLinkRequestFormat = jsonFormat4(CreateLinkRequest)
  implicit val CreateLinkResponseFormat = jsonFormat2(CreateLinkResponse)
  implicit val PostLinkDataRequestFormat = jsonFormat2(PostLinkDataRequest)
  implicit val LinkDataFormat = jsonFormat2(LinkData)
  implicit val GetLinkDateResponseFormat = jsonFormat3(GetLinkDataResponse)
  implicit val FolderLinkDataResponseFormat = jsonFormat1(FolderLinkDataResponse)
}
