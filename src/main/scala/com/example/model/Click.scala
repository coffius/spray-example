package com.example.model

import org.joda.time.DateTime

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
case class Click(date: DateTime, referer: String, remoteIp: String)
