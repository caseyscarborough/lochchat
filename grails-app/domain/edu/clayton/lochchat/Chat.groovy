package edu.clayton.lochchat

import grails.util.Environment

class Chat {

  String uniqueId
  Date startTime
  Date endTime
  String[] anonymousUsers
  Log log

  def grailsLinkGenerator
  static transients = ['grailsLinkGenerator']

  static hasMany = [users: User, files: FileUpload]
  static belongsTo = [User]

  static constraints = {
    log nullable: true
    endTime nullable: true
    anonymousUsers nullable: true
    uniqueId unique: true
  }

  static mapping = {
    files cascade: 'all-delete-orphan'
  }

  def getFormattedStartTime() {
    startTime.format("MMM. dd, yyyy 'at' h:mma")
  }

  def getUrl() {
    if (!uniqueId) {
      uniqueId = UUID.randomUUID().encodeAsSHA1().substring(0, 10)
    }

    def baseUrl = grailsLinkGenerator.link(uri: "/", absolute: true).replaceAll(/http:\/\/(.*):443\//, /https:\/\/$1\//)
    baseUrl + uniqueId
  }

  def getWebsocketUrl() {
    grailsLinkGenerator
      .link(uri: "/chatEndpoint/${uniqueId}", absolute: true)
      .replaceFirst(/http:\/\/(.*):443\//, /wss:\/\/$1\//)
      .replaceFirst(/http/, /ws/)
  }
}
