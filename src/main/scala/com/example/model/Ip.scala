package com.example.model

/**
 * @author coffius@gmail.com (Aleksei Shamenev)
 */
case class Ip(bytes: Array[Byte]){
  override def toString: String = s"${bytes(0)}.${bytes(1)}.${bytes(2)}.${bytes(3)}"
}

object Ip{
  def apply(ip:String) = {
    throw new UnsupportedOperationException
  }
}
