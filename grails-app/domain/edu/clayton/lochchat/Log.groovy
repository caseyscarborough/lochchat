package edu.clayton.lochchat

class Log {

  Date dateCreated
  Date lastUpdated

  static hasMany = [messages: Message]

  static constraints = {
  }

  static mapping = {
  }
}
