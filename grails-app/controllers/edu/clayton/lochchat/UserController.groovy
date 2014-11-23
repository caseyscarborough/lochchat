package edu.clayton.lochchat

import grails.plugin.springsecurity.annotation.Secured

class UserController {

  def messageService
  def springSecurityService
  static allowedMethods = [save: 'POST']

  @Secured(['permitAll'])
  def create() {
    [user: new User()]
  }

  @Secured(['permitAll'])
  def save() {
    def user = new User(firstName: params.firstName, lastName: params.lastName, email: params.email, username: params.username, password: params.password, chats: [])

    if (!user.save(flush: true)) {
      def error = messageService.getErrorMessage(user)
      flash.message = error
      render view: 'create', model: [user: user]
      return
    }

    def role = Role.findByAuthority("ROLE_USER")
    UserRole.create(user, role, true)
    springSecurityService.reauthenticate(params.username, params.password)

    // Add previous chat to user if they were not logged in during chat
    if (session.chatId) {
      def chat = Chat.findByUniqueId(session.chatId)
      user.chats.add(chat)
      user.save(flush: true)
    }

    flash.message = "You have successfully logged in."
    redirect(controller: 'home', action: 'index')
  }

  @Secured(['IS_AUTHENTICATED_FULLY'])
  def profile() {
    [user: (User)springSecurityService.currentUser, uniqueUrl: new Chat().url]
  }
}
