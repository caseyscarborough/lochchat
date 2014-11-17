package edu.clayton.lochchat

import grails.converters.JSON
import org.codehaus.groovy.grails.support.encoding.CodecLookup
import org.springframework.http.HttpStatus
import org.springframework.mail.MailSendException
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo

class ChatController {

  CodecLookup codecLookup

  static allowedMethods = [create: 'POST', invite: 'POST']

  def mailService
  def messageService

  def create() {
    def logInstance = new Log(chatLog: "")
    log.info(logInstance.save(flush: true))
    def chat = new Chat(uniqueId: params.url?.split("/")?.last(), startTime: new Date(), log: logInstance)
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
    [chatroom: chatroom]
  }

  def export() {
    def chat = Chat.findByUniqueId(params.uniqueId)
    response.contentType = 'application/octet-stream'
    response.setHeader('Content-disposition', "attachment; filename=${params.uniqueId}-${new Date().toTimestamp()}.txt")
    response.outputStream << chat.log.contents.getBytes()
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

  @MessageMapping("/chatMessage")
  @SendTo("/topic/chatMessage")
  protected String message(String text) {
    def array = text?.split(/\|/) as List
    Chat.withTransaction {
      def chat = Chat.findByUniqueId(array.pop())
      text = codecLookup.lookupEncoder('HTML').encode(array.join(""))

      if (!chat.log.contents) {
        chat.log.contents = "$text\r\n"
      } else {
        chat.log.contents += "$text\r\n"
      }
      chat.log.save(flush: true)
    }
    text
  }

  protected void emailUser(String email, Chat chat) {
    log.info("Emailing $email...")
    try {
      mailService.sendMail {
        async true
        to email
        subject "LochChat Invite"
        body "You've been invited to join a chat at the following url: ${createLink(controller: "chat", action: "room", absolute: true)}/${chat.uniqueId}"
      }
    } catch (MailSendException e) {
      log.error("Could not deliver email to recipient.", e)
    }
  }
}
