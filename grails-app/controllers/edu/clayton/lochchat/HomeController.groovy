package edu.clayton.lochchat

class HomeController {

  def index() {
    def baseUrl = createLink(controller: "home", action: "index", absolute: true)
    def chatroomUrl = "${baseUrl}${UUID.randomUUID().encodeAsBase64().substring(0, 22)}"
    [chatroomUrl: chatroomUrl]
  }
}
