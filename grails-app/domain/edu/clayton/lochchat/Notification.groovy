package edu.clayton.lochchat

class Notification {

  User user
  String message
  String url
  Boolean isViewed = false
  Boolean isDismissed = false

  static constraints = {
    url nullable: true
  }

  static mapping = {
    message type: 'text'
  }
}
