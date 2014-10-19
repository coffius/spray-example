package com.example.dto

import spray.http.DateTime

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
case class ClickData(date: Long, referer: String, remoteIp: String)
