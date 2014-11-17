package edu.clayton.lochchat

class HomeController {

  def index() {
    def baseUrl = createLink(controller: "chat", action: "room", absolute: true).replaceAll(/http:\/\/(.*):443\//, /https:\/\/$1\//)
    def chatroomUrl = "${baseUrl}/${UUID.randomUUID().encodeAsBase64().substring(20)}"
    [chatroomUrl: chatroomUrl]
  }
}
