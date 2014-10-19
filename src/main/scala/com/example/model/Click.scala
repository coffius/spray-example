package com.example.model

import org.joda.time.DateTime

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
case class Click(id: Option[Long] = None,
                 linkId: Long,
                 date: DateTime,
                 referer: String,
                 remoteIp: String) extends Entity
