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
    def logInstance = new Log(messages: [])
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

  def exportLog() {
    def chat = Chat.findByUniqueId(params.uniqueId)
    def decoder = codecLookup.lookupDecoder('HTML')
    response.contentType = 'application/octet-stream'
    response.setHeader('Content-disposition', "attachment; filename=${params.uniqueId}-${new Date().toTimestamp()}.txt")
    response.outputStream << "Chat log for room $chat.uniqueId, starting on $chat.log.formattedDateCreated.\n\n"
    chat.log.messages.sort { it.dateCreated }. each { Message message ->
      response.outputStream << message.dateCreated.format("h:mma '-' ") + decoder.decode(message.contents) + "\n"
    }
    response.outputStream.flush()
  }

  def exportWorkspace() {
    response.contentType = 'application/octet-stream'
    response.setHeader('Content-disposition', "attachment; filename=${params.chatroom}-workspace-${new Date().toTimestamp()}.txt")
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
      new Message(contents: "Cound not deliver email to recipient: $email. Are you sure this email address exists?", log: chat.log).save(flush: true)
      chat.log.save(flush: true)
      chat.save(flush: true)
    }
  }
}
