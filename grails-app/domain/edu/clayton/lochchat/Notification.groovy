package edu.clayton.lochchat

class Notification {

  User user
  String message
  String url
  Boolean isViewed = false
  Boolean isDismissed = false

  Date dateCreated

  static constraints = {
    url nullable: true
  }

  static mapping = {
    message type: 'text'
  }

  String getFormattedDateCreated() {
    dateCreated.format("MMM. dd, yyyy 'at' h:mma")
  }
}
