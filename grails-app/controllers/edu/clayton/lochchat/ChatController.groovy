package edu.clayton.lochchat

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.support.encoding.CodecLookup
import org.springframework.http.HttpStatus

@Secured(['permitAll'])
class ChatController {

  CodecLookup codecLookup

  static allowedMethods = [create: 'POST', invite: 'POST', delete: 'DELETE']

  def chatService
  def mailService
  def springSecurityService

  def create() {
    def result = chatService.createChat(params)
    response.status = result.status.value
    render result as JSON
  }

  def room(String uniqueId) {
    def result = chatService.enterRoom(uniqueId)
    if (result.status == HttpStatus.NOT_FOUND) {
      redirect(controller: 'home', action: 'index')
      return
    }
    result
  }

  def generateChatroomUrl() {
    render new Chat().url
  }

  def invite() {
    def result = chatService.invite(params)
    response.status = result.status.value
    render result as JSON
  }

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def delete(String uniqueId) {
    def result = chatService.deleteChat(uniqueId)
    response.status = result.status.value
    render result as JSON
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
}
