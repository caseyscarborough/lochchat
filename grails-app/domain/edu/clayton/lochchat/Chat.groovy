package edu.clayton.lochchat

class Chat {

  String url
  Date startTime
  Date endTime
  String[] anonymousUsers

  static constraints = {
    endTime nullable: true
    anonymousUsers nullable: true
    url unique: true
  }
}
