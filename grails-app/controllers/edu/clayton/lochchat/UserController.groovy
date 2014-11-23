package edu.clayton.lochchat

import grails.plugin.springsecurity.annotation.Secured

class UserController {

  @Secured(['permitAll'])
  def create() {
    [user: new User()]
  }
}
