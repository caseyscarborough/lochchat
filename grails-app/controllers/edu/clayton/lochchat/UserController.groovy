package edu.clayton.lochchat

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException

class UserController {

  def authenticationManager
  def messageService
  def springSecurityService

  static allowedMethods = [save: 'POST', updatePassword: 'PUT']

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

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def profile() {
    [user: (User)springSecurityService.currentUser, uniqueUrl: new Chat().url]
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def updatePassword() {
    User user = (User) springSecurityService.currentUser
    log.debug(request.JSON)
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.username, request.JSON.currentPassword))
    } catch (AuthenticationException e) {
      // User provided incorrect password.
      response.status = 400
      render ([status: HttpStatus.BAD_REQUEST, message: "The current password you entered was incorrect. Please try again."] as JSON)
      return
    }

    if (request.JSON.newPassword.isEmpty()) {
      response.status = 400
      render ([status: HttpStatus.BAD_REQUEST, message: "Your new password cannot be blank."] as JSON)
      return
    }

    user.password = request.JSON.newPassword
    user.save(flush: true)
    render ([status: HttpStatus.OK] as JSON)
  }
}
