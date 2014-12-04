package edu.clayton.lochchat

import grails.converters.JSON
import grails.transaction.Transactional
import grails.util.Environment
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.http.HttpStatus
import org.springframework.mail.MailSendException

import javax.websocket.Session

@Transactional
class ChatService {

  def grailsLinkGenerator
  def mailService
  def messageService
  def springSecurityService

  String getNotificationEndpointUrl() {
    getNotificationEndpointFor(((User) springSecurityService.currentUser)?.username)
  }

  String getNotificationEndpointForUser(User user) {
    getNotificationEndpointFor(user.username)
  }

  private String getNotificationEndpointFor(String username) {
    grailsLinkGenerator
      .link(uri: "/notificationEndpoint/$username", absolute: true)
      .replaceFirst(/http:\/\/(.*):443\//, /wss:\/\/$1\//)
      .replaceFirst(/http/, /ws/)
  }

  Map createChat(GrailsParameterMap params) {
    Log logInstance = new Log(messages: [])
    logInstance.save(flush: true)

    Chat chat = new Chat(uniqueId: params.url?.split("/")?.last(), startTime: new Date(), log: logInstance, users: [])
    if (!chat.save(flush: true)) {
      logInstance.delete(flush: true)
      return [status: HttpStatus.BAD_REQUEST, message: messageService.getErrorMessage(chat)]
    }

    handleInvitees(params.invitees, chat)
    return [status: HttpStatus.OK, data: [chat: chat, url: chat.url]]
  }

  Map invite(GrailsParameterMap params) {
    Chat chat = Chat.findByUniqueId(params.uniqueId)
    handleInvitees(params.invitees, chat, params.username)
    return [status: HttpStatus.OK]
  }

  Map deleteChat(String uniqueId) {
    Chat chat = Chat.findByUniqueId(uniqueId)
    User user = (User) springSecurityService.currentUser

    if (user.chats.contains(chat) && (chat.users - user).size() == 0) {
      user.chats.remove(chat)
      chat.delete(flush: true)
      return [status: HttpStatus.OK, data: null]
    }
    return [status: HttpStatus.NOT_FOUND]
  }

  Map enterRoom(String uniqueId) {
    def chatroom = Chat.findByUniqueId(uniqueId)
    if (!chatroom) {
      return [status: HttpStatus.NOT_FOUND]
    }

    if (springSecurityService.isLoggedIn()) {
      User user = (User) springSecurityService.currentUser
      if (!chatroom.users.contains(user)) {
        user.chats.add(chatroom)
        user.save(flush: true)
      }
    } else {
      def session = WebUtils.retrieveGrailsWebRequest().session
      session.chatId = chatroom.uniqueId
    }
    return [status: HttpStatus.OK, chatroom: chatroom]
  }

  protected void handleInvitees(String invitees, Chat chat, String username=null) {
    log.debug("Connecting to chatroom endpoint ${chat.websocketUrl}")
    Session chatSession = ClientHelper.getConnectedSession(chat.websocketUrl)

    if (invitees) {
      if (username) {
        ClientHelper.sendMessage(chatSession, "_serverMessage:$username is inviting users to to the chatroom...")
      }

      invitees.trim()?.split(",")?.each { String invitee ->
        def user = User.findByUsername(invitee.toLowerCase()) ?: User.findByEmail(invitee.toLowerCase())
        try {
          if (user) {
            Session notificationSession = ClientHelper.getConnectedSession(getNotificationEndpointForUser(user))
            emailUser(user.email, chat, chatSession)

            def message = "Someone has invited you to join a chatroom. Click here to join it."
            def notification = new Notification(user: user, message: message, url: chat.url)
            notification.save(flush: true)
            ClientHelper.sendMessage(notificationSession, (([notification: notification]) as JSON).toString())
            notificationSession.close()
            return
          }
          emailUser(invitee, chat)
        } catch(MailSendException e) {
          log.error("Could not deliver email to recipient $invitee")

          def message = "Could not deliver email to $invitee. Are you sure this user or email address exists?"
          new Message(contents: message, log: chat.log).save(flush: true)
          ClientHelper.sendMessage(chatSession, "_serverMessage:$message")
          chat.log.save(flush: true)
          chat.save(flush: true)
        }
      }
    }
    chatSession.close()
  }

  protected void emailUser(String email, Chat chat, Session session=null) throws MailSendException {
    log.info("Emailing $email...")
    mailService.sendMail {
      to email
      from 'LochChat'
      subject "LochChat Invite"
      body(view: "/email/invite", model: [url: chat.url])
    }
    if (session) {
      ClientHelper.sendMessage(session, "_serverMessage:Invited $email to the chatroom.")
    }
  }
}
