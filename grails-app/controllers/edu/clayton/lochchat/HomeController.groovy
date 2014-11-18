package edu.clayton.lochchat

class HomeController {

  def index() {
    def chatroomUrl = new Chat().url
    [chatroomUrl: chatroomUrl]
  }
}
