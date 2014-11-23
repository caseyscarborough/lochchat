package edu.clayton.lochchat

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.support.encoding.CodecLookup
import org.springframework.http.HttpStatus
import org.springframework.mail.MailSendException

@Secured(['permitAll'])
class ChatController {

  CodecLookup codecLookup

  static allowedMethods = [create: 'POST', invite: 'POST']

  def mailService
  def messageService
  def springSecurityService

  def create() {
    def logInstance = new Log(messages: [])
    log.info(logInstance.save(flush: true))

    def chat = new Chat(uniqueId: params.url?.split("/")?.last(), startTime: new Date(), log: logInstance, users: [])
    def result
    if (!chat.save(flush: true)) {
      logInstance.delete(flush: true)
      result = [status: HttpStatus.BAD_REQUEST.reasonPhrase, message: messageService.getErrorMessage(chat)]
      response.status = 500
      render result as JSON
      return
    }

    log.info("Params: $params")

    if (params.emails) {
      log.info("Email addresses were provided.")

      params.emails?.trim()?.split(",")?.each { email ->
        log.info("Emailing $email...")
        emailUser(email, chat)
      }
    }

    result = [status: HttpStatus.OK, data: [chat: chat, url: createLink(controller: 'chat', action: 'room', params: [uniqueId: chat.uniqueId])]]
    render result as JSON
  }

  def room(String uniqueId) {
    def chatroom = Chat.findByUniqueId(uniqueId)

    if (!chatroom) {
      redirect(controller: "home", action: "index")
      return
    }

    if (springSecurityService.isLoggedIn()) {
      User user = springSecurityService.currentUser

      if (!chatroom.users.contains(user)) {
        user.chats.add(chatroom)
        user.save(flush: true)
      }
    } else {
      session.chatId = chatroom.uniqueId
    }

    [chatroom: chatroom]
  }

  def generateChatroomUrl() {
    def url = new Chat().url
    render url
  }

  def exportLog() {
    def chat = Chat.findByUniqueId(params.uniqueId)
    def decoder = codecLookup.lookupDecoder('HTML')
    response.contentType = 'application/octet-stream'
    response.setHeader('Content-disposition', "attachment; filename=${params.uniqueId}-${new Date().toTimestamp().time}.html")
    response.outputStream << "Chat log for room $chat.uniqueId, starting on $chat.log.formattedDateCreated.<br><br>"
    chat.log.messages.sort { it.dateCreated }. each { Message message ->
      def output = message.dateCreated.format("h:mma '-' ")
      if (message.user) {
        output += message?.user + ": "
      }
      response.outputStream << output + decoder.decode(message.contents) + "<br>"
    }
    response.outputStream.flush()
  }

  def exportWorkspace() {
    response.contentType = 'application/octet-stream'
    response.setHeader('Content-disposition', "attachment; filename=${params.chatroom}-workspace-${new Date().toTimestamp().time}.txt")
    response.outputStream << params.workspace
    response.outputStream.flush()
  }

  def invite() {
    def chat = Chat.findByUniqueId(params.uniqueId)
    if (params.emails) {
      params.emails.split(",").each { String email ->
        emailUser(email, chat)
      }
    }
    def result = [status: HttpStatus.OK]
    render result as JSON
  }

  protected void emailUser(String email, Chat chat) {
    log.info("Emailing $email...")
    try {
      mailService.sendMail {
        to email
        subject "LochChat Invite"
        body "You've been invited to join a chat at the following url: ${chat.url}"
      }
    } catch (MailSendException e) {
      log.error("Could not deliver email to recipient.", e)
      new Message(contents: "Could not deliver email to recipient: $email. Are you sure this email address exists?", log: chat.log).save(flush: true)
      chat.log.save(flush: true)
      chat.save(flush: true)
    }
  }
}
