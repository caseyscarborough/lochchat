package edu.clayton.lochchat

class Chat {

  String uniqueId
  Date startTime
  Date endTime
  String[] anonymousUsers
  Log log

  def grailsLinkGenerator
  static transients = ['grailsLinkGenerator']

  static hasMany = [users: User]
  static belongsTo = [User]

  static constraints = {
    log nullable: true
    endTime nullable: true
    anonymousUsers nullable: true
    uniqueId unique: true
  }

  def getFormattedStartTime() {
    startTime.format("MMM. dd, yyyy 'at' h:mma")
  }

  def getUrl() {
    if (!uniqueId) {
      uniqueId = UUID.randomUUID().encodeAsSHA1().substring(0, 16)
    }

    def baseUrl = grailsLinkGenerator.link(controller: "chat", action: "room", absolute: true).replaceAll(/http:\/\/(.*):443\//, /https:\/\/$1\//)
    "$baseUrl/$uniqueId"
  }
}
