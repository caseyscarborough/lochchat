package edu.clayton.lochchat

import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class HomeController {

  def index() {
    def chatroomUrl = new Chat().url
    [chatroomUrl: chatroomUrl]
  }
}
