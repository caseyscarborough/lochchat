package edu.clayton.lochchat

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException

class UserController extends grails.plugin.springsecurity.ui.UserController {

  def authenticationManager
  def springSecurityService

  static allowedMethods = [save: 'POST', updatePassword: 'PUT']

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
