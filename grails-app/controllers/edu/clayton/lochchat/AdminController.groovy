package edu.clayton.lochchat

import grails.plugin.springsecurity.annotation.Secured

@Secured(['ROLE_ADMIN'])
class AdminController {

  def index() {
    redirect(controller: 'user', action: 'create')
  }
}