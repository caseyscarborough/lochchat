package edu.clayton.lochchat

class Log {

  String chatLog
  Date lastUpdated

  static constraints = {
    chatLog nullable: true, blank: true
  }

  static mapping = {
    chatLog type: 'text'
  }
}
