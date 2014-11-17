package edu.clayton.lochchat

class Chat {

  String uniqueId
  Date startTime
  Date endTime
  String[] anonymousUsers
  Log log

  def grailsLinkGenerator
  static transients = ['grailsLinkGenerator']

  static constraints = {
    log nullable: true
    endTime nullable: true
    anonymousUsers nullable: true
    uniqueId unique: true
  }

  def getUrl() {
    def baseUrl = grailsLinkGenerator.link(controller: "chat", action: "room", absolute: true).replaceAll(/http:\/\/(.*):443\//, /https:\/\/$1\//)
    "$baseUrl/$uniqueId"
  }
}
