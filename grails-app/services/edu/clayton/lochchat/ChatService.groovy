package edu.clayton.lochchat

import grails.transaction.Transactional
import org.codehaus.groovy.grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.http.HttpStatus
import org.springframework.mail.MailSendException

@Transactional
class ChatService {

  def mailService
  def messageService

  Map createChat(GrailsParameterMap params) {
    def logInstance = new Log(messages: [])
    logInstance.save(flush: true)

    def chat = new Chat(uniqueId: params.url?.split("/")?.last(), startTime: new Date(), log: logInstance, users: [])
    if (!chat.save(flush: true)) {
      logInstance.delete(flush: true)
      return [status: HttpStatus.BAD_REQUEST.reasonPhrase, message: messageService.getErrorMessage(chat)]
    }

    if (params.emails) {
      params.emails?.trim()?.split(",")?.each { email ->
        log.info("Emailing $email...")
        emailUser(email, chat)
      }
    }

    return [status: HttpStatus.OK, data: [chat: chat, url: chat.url]]
  }


  protected void emailUser(String email, Chat chat) {
    log.info("Emailing $email...")
    try {
      mailService.sendMail {
        to email
        from 'LochChat'
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
