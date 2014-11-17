package edu.clayton.lochchat

class Chat {

  String uniqueId
  Date startTime
  Date endTime
  String[] anonymousUsers
  Log log

  static constraints = {
    log nullable: true
    endTime nullable: true
    anonymousUsers nullable: true
    uniqueId unique: true
  }
}
