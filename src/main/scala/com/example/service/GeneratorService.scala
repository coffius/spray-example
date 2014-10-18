package com.example.service

import java.util.UUID

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
class GeneratorService {
  def generateUserToken: String = UUID.randomUUID().toString
}
