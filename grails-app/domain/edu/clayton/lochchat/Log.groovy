package edu.clayton.lochchat

class Log {

  String contents
  Date lastUpdated

  static constraints = {
    contents nullable: true, blank: true
  }

  static mapping = {
    contents type: 'text'
  }
}
