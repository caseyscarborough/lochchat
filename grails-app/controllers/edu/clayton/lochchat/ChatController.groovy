package edu.clayton.lochchat

import grails.converters.JSON
import org.springframework.http.HttpStatus

import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.SendTo

class ChatController {

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

    // TODO: Implement email
    params.emails?.split(",")?.each { email -> log.info("Emailing $email...") }

    result = [status: HttpStatus.OK, data: [chat: chat, url: createLink(controller: 'chat', action: 'room', params: [uniqueId: chat.uniqueId])]]
    render result as JSON
  }

  def room(String uniqueId) {
    def chatroom = Chat.findByUniqueId(uniqueId)
    [chatroom: chatroom]
  }

  @MessageMapping("/hello")
  @SendTo("/topic/hello")
  protected String hello(String text) {
    return text
  }
}
