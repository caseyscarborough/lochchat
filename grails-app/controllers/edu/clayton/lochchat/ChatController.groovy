package edu.clayton.lochchat

import grails.converters.JSON
import org.codehaus.groovy.grails.plugins.codecs.HTMLCodec
import org.codehaus.groovy.grails.plugins.codecs.HTMLCodecFactory
import org.codehaus.groovy.grails.support.encoding.CodecLookup
import org.springframework.http.HttpStatus

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo

class ChatController {

  CodecLookup codecLookup

  static allowedMethods = [create: 'POST']

  def messageService

  def create() {
    def chat = new Chat(uniqueId: params.url?.split("/")?.last(), startTime: new Date())
    def result
    if (!chat.save(flush: true)) {
      result = [status: HttpStatus.BAD_REQUEST.reasonPhrase, message: messageService.getErrorMessage(chat)]
      response.status = 500
      render result as JSON
      return
    }

    // TODO: Validate email
    params.emails?.split(",")?.each { email ->
      log.info("Emailing $email...")
      sendMail {
        async true
        to email
        subject "LochChat Invite"
        body "You've been invited to join a chat at the following url: ${createLink(controller: "chat", action: "room", absolute: true)}/${chat.uniqueId}"
      }
    }

    result = [status: HttpStatus.OK, data: [chat: chat, url: createLink(controller: 'chat', action: 'room', params: [uniqueId: chat.uniqueId])]]
    render result as JSON
  }

  def room(String uniqueId) {
    def chatroom = Chat.findByUniqueId(uniqueId)
    [chatroom: chatroom]
  }

  @MessageMapping("/chatMessage")
  @SendTo("/topic/chatMessage")
  protected String message(String text) {
    return codecLookup.lookupEncoder('HTML').encode(text)
  }
}
