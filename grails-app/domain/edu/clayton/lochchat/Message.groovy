package edu.clayton.lochchat

class Message {

  Log log
  String user
  String contents
  Date dateCreated

  static constraints = {
    user nullable: true
  }

  static mapping = {
    contents type: 'text'
  }

  String toString() {
    if (user) {
      return "$user: $contents"
    }
    return contents
  }
}
