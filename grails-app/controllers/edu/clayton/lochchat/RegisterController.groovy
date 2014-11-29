package edu.clayton.lochchat

import grails.plugin.springsecurity.SpringSecurityUtils
import grails.plugin.springsecurity.annotation.Secured
import grails.plugin.springsecurity.authentication.dao.NullSaltSource
import grails.plugin.springsecurity.ui.RegistrationCode

@Secured(['permitAll'])
class RegisterController extends grails.plugin.springsecurity.ui.RegisterController {

  @Override
  def index() {
    def copy = [:] + (flash.chainedParams ?: [:])
    copy.remove 'controller'
    copy.remove 'action'
    [command: new RegisterCommand(copy)]
  }

  def register(RegisterCommand command) {
    if (command.hasErrors()) {
      render view: 'index', model: [command: command]
      return
    }

    String salt = saltSource instanceof NullSaltSource ? null : command.username
    def user = lookupUserClass().newInstance(firstName: command.firstName, lastName: command.lastName,
      email: command.email, username: command.username, accountLocked: true, enabled: true)

    RegistrationCode registrationCode = springSecurityUiService.register(user, command.password, salt)
    if (registrationCode == null || registrationCode.hasErrors()) {
      // null means problem creating the user
      flash.error = message(code: 'spring.security.ui.register.miscError')
      flash.chainedParams = params
      redirect action: 'index'
      return
    }

    String url = generateLink('verifyRegistration', [t: registrationCode.token])
    def conf = SpringSecurityUtils.securityConfig
    def body = conf.ui.register.emailBody
    if (body.contains('$')) {
      body = evaluate(body, [user: user, url: url])
    }
    mailService.sendMail {
      to command.email
      from conf.ui.register.emailFrom
      subject conf.ui.register.emailSubject
      html body.toString()
    }

    render view: 'index', model: [emailSent: true]
  }

  @Override
  def verifyRegistration() {
    def conf = SpringSecurityUtils.securityConfig
    String defaultTargetUrl = conf.successHandler.defaultTargetUrl

    String token = params.t

    def registrationCode = token ? RegistrationCode.findByToken(token) : null
    if (!registrationCode) {
      flash.error = message(code: 'spring.security.ui.register.badCode')
      redirect uri: defaultTargetUrl
      return
    }

    def user
    RegistrationCode.withTransaction { status ->
      String usernameFieldName = SpringSecurityUtils.securityConfig.userLookup.usernamePropertyName
      user = lookupUserClass().findWhere((usernameFieldName): registrationCode.username)
      if (!user) {
        return
      }
      user.accountLocked = false
      user.save(flush: true)
      def UserRole = lookupUserRoleClass()
      def Role = lookupRoleClass()
      for (roleName in conf.ui.register.defaultRoleNames) {
        UserRole.create user, Role.findByAuthority(roleName)
      }
      registrationCode.delete()
    }

    if (!user) {
      flash.error = message(code: 'spring.security.ui.register.badCode')
      redirect uri: defaultTargetUrl
      return
    }

    // Add previous chat to user if they were not logged in during chat
    if (session.chatId) {
      def chat = Chat.findByUniqueId(session.chatId)
      user.chats.add(chat)
      user.save(flush: true)
    }

    springSecurityService.reauthenticate user.username

    flash.message = message(code: 'spring.security.ui.register.complete')
    redirect uri: conf.ui.register.postRegisterUrl ?: defaultTargetUrl
  }
}

class RegisterCommand extends grails.plugin.springsecurity.ui.RegisterCommand {
  String firstName
  String lastName

  static constraints = {
    firstName blank: false
    lastName blank: false
    username blank: false, validator: { value, command ->
      if (value) {
        def User = command.grailsApplication.getDomainClass(SpringSecurityUtils.securityConfig.userLookup.userDomainClassName).clazz
        if (User.findByUsername(value)) {
          return 'registerCommand.username.unique'
        }
      }
    }
    email blank: false, email: true, validator: { value, command ->
      if (value) {
        def User = command.grailsApplication.getDomainClass(SpringSecurityUtils.securityConfig.userLookup.userDomainClassName).clazz
        if (User.findByEmail(value)) {
          return 'registerCommand.email.unique'
        }
      }
    }
    password blank: false, validator: RegisterController.passwordValidator
    password2 validator: RegisterController.password2Validator
  }
}