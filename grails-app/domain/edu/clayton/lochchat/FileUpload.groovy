package edu.clayton.lochchat

class FileUpload {

  String filename
  String location
  String originalFilename
  Chat chat
  String uniqueId

  Date dateCreated

  def grailsLinkGenerator
  static transients = ['grailsLinkGenerator']

  static constraints = {
  }

  def getPath() {
    "$location/$filename"
  }

  def getDownloadUrl() {
    grailsLinkGenerator.link(controller: 'fileUpload', action: 'download', params: [chatId: chat.uniqueId, fileId: uniqueId])
  }
}
