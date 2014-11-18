package edu.clayton.lochchat

class Log {

  Date dateCreated
  Date lastUpdated

  static hasMany = [messages: Message]

  static constraints = {
  }

  static mapping = {
  }

  String getFormattedDateCreated() {
    dateCreated.format("MM/dd/yyyy 'at' h:mma")
  }
}
