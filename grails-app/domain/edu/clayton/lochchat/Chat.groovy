package edu.clayton.lochchat

class Chat {

  String uniqueId
  Date startTime
  Date endTime
  String[] anonymousUsers

  static constraints = {
    endTime nullable: true
    anonymousUsers nullable: true
    uniqueId unique: true
  }
}
