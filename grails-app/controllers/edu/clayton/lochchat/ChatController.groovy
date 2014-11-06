package edu.clayton.lochchat

import grails.converters.JSON
import org.springframework.http.HttpStatus

class ChatController {

  static allowedMethods = [create: 'POST']

  def create() {
    def chat = new Chat(uniqueId: params.url?.split("/")?.last(), startTime: new Date())
    def result
    if (!chat.save(flush: true)) {
      result = [status: HttpStatus.BAD_REQUEST, message: "The chat could not be created."]
      render result as JSON
      return
    }

    // TODO: Implement email
    params.emails?.split(",")?.each { email ->
      log.info("Emailing $email...")
    }

    result = [status: HttpStatus.OK, data: [chat: chat]]
    render result as JSON
  }

  def room(String uniqueId) {
    def chatroom = Chat.findByUniqueId(uniqueId)
    render "You are in chatroom number ${chatroom.uniqueId}"
  }
}
