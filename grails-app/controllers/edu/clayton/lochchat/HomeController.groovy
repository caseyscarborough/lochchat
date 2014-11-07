package edu.clayton.lochchat

class HomeController {

  def index() {
    def baseUrl = createLink(controller: "chat", action: "room", absolute: true)
    def chatroomUrl = "${baseUrl}/${UUID.randomUUID().encodeAsBase64().substring(20)}"
    [chatroomUrl: chatroomUrl]
  }
}
