package edu.clayton.lochchat

class Notification {

  User user
  String message
  Boolean isViewed = false
  Boolean isDismissed = false

  static constraints = {
  }

  static mapping = {
    message type: 'text'
  }
}
