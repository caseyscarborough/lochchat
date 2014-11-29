package edu.clayton.lochchat

import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.codehaus.groovy.grails.web.util.WebUtils
import org.springframework.http.HttpStatus
import org.springframework.mail.MailSendException

@Transactional
class ChatService {

  def mailService
  def messageService
  def springSecurityService

  ChatClient client = null

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

  protected void handleInvitees(String invitees, Chat chat, String username) {
    if (!client) {
      client = new ChatClient()
    }
    client.connect(chat.websocketUrl)

    if (invitees) {
      if (username) {
        client.sendMessage("_serverMessage:$username is inviting users to to the chatroom...")
      }

      invitees.trim()?.split(",")?.each { String invitee ->
        def user = User.findByUsername(invitee.toLowerCase()) ?: User.findByEmail(invitee.toLowerCase())
        try {
          if (user) {
            new Notification(user: user, message: "Someone has invited you to join a chatroom. Click here to join it.", url: chat.url).save(flush: true)
            emailUser(user.email, chat)
            return
          }
          emailUser(invitee, chat)
        } catch(MailSendException e) {
          log.error("Could not deliver email to recipient $invitee")

          def message = "Could not deliver email to $invitee. Are you sure this user or email address exists?"
          new Message(contents: message, log: chat.log).save(flush: true)
          client.sendMessage("_serverMessage:$message")
          chat.log.save(flush: true)
          chat.save(flush: true)
        }
      }
    }
  }

  protected void emailUser(String email, Chat chat) throws MailSendException {
    log.info("Emailing $email...")
    mailService.sendMail {
      to email
      from 'LochChat'
      subject "LochChat Invite"
      body "You've been invited to join a chat at the following url: ${chat.url}"
    }
    client.sendMessage("_serverMessage:Invited $email to the chatroom.")
  }
}
