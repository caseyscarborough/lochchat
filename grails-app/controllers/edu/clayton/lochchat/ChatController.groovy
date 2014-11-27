package edu.clayton.lochchat
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.support.encoding.CodecLookup
import org.springframework.http.HttpStatus

@Secured(['permitAll'])
class ChatController extends BaseController {

  CodecLookup codecLookup

  static allowedMethods = [create: 'POST', invite: 'POST', delete: 'DELETE']

  def chatService
  def mailService
  def springSecurityService

  def create() {
    def result = chatService.createChat(params)
    renderResult(result)
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

  @Secured(['IS_AUTHENTICATED_REMEMBERED'])
  def delete(String uniqueId) {
    def chat = Chat.findByUniqueId(uniqueId)
    User user = springSecurityService.currentUser

    if (user.chats.contains(chat) && (chat.users - user).size() == 0) {
      user.chats.remove(chat)
      chat.delete(flush: true)
      response.status = 200
      render ""
      return
    }
    response.status = 404
  }

}
