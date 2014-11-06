package edu.clayton.lochchat

class Chat {

  String url
  Date startTime
  Date endTime
  String[] anonymousUsers

  static constraints = {
    url nullable: false
  }
}
