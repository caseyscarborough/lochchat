package edu.clayton.lochchat

import grails.converters.JSON
import grails.util.Environment
import org.apache.commons.lang.StringEscapeUtils
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes as GA
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationContext

import javax.servlet.ServletContext
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener
import javax.websocket.*
import javax.websocket.server.PathParam
import javax.websocket.server.ServerContainer
import javax.websocket.server.ServerEndpoint

@WebListener
@ServerEndpoint("/chatEndpoint/{chatId}")
public class LochChatAnnotation implements ServletContextListener {

  private final Logger log = LoggerFactory.getLogger(getClass().name)
  static final Set<Session> chatroomUsers = ([] as Set).asSynchronized()

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ServletContext servletContext = sce.servletContext
    ServerContainer serverContainer = (ServerContainer) servletContext.getAttribute("javax.websocket.server.ServerContainer")
    try {
      if (Environment.current == Environment.DEVELOPMENT) {
        serverContainer.addEndpoint(LochChatAnnotation)
      }
      ApplicationContext ctx = (ApplicationContext) servletContext.getAttribute(GA.APPLICATION_CONTEXT)
      GrailsApplication grailsApplication = ctx.grailsApplication
      ConfigObject config = grailsApplication.config
      Integer defaultMaxSessionIdleTimeout = config.myservlet.timeout ?: 0
      serverContainer.defaultMaxSessionIdleTimeout = defaultMaxSessionIdleTimeout
    } catch (IOException e) {
      log.error(e.message, e)
    } catch (NullPointerException e) {
      log.error(e.message, e)
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
  }

  @OnOpen
  public void handleOpen(Session userSession, @PathParam("chatId") String chatId) {
    chatroomUsers.add(userSession)
    userSession.userProperties.put("chatId", chatId)
  }

  @OnMessage
  public String handleMessage(String message, Session userSession)  throws IOException {
    def output = [:]
    message = StringEscapeUtils.escapeHtml(message)
    log.debug("Received message from user: $message")

    String username = userSession.userProperties.get("username")
    String chatId = userSession.userProperties.get("chatId")
    if (!username) {
      log.debug("User has connected to the chatroom.")

      userSession.userProperties.put("username", message)
      Message.withTransaction {
        def chat = Chat.findByUniqueId(chatId)
        new Message(contents: message + " has joined the chatroom.", log: chat?.log).save(flush: true)
        message += " has joined the chatroom."
      }

      output.put("message", "$message")
      sendMessage(output, chatId)
    } else {
      Message.withTransaction {
        def chat = Chat.findByUniqueId(chatId)
        new Message(user: username, contents: message, log: chat?.log).save(flush: true)
      }
      output.put("message", "${username}: ${message}")
      sendMessage(output, chatId)
    }
  }

  private void sendMessage(output, chatId) {
    Iterator<Session> iterator = chatroomUsers.iterator()

    while (iterator.hasNext()) {
      def user = iterator.next()
      try {
        log.info(user.userProperties.toMapString())
        if (user.userProperties.get("chatId") == chatId) {
          user.basicRemote.sendText((output as JSON).toString())
        }
      } catch (IllegalStateException e) {
        log.error("An error occurred, but was caught.", e)
      }
    }
  }

  @OnClose
  public void handleClose(Session userSession) {
    String chatId = userSession.userProperties.get("chatId")
    String username = userSession.userProperties.get("username")
    chatroomUsers.remove(userSession)

    if (chatId && username) {
      def message = "${username} has left the chatroom."
      Message.withTransaction {
        def chat = Chat.findByUniqueId(chatId)
        new Message(contents: message, log: chat?.log).save(flush: true)
      }
      def output = [message: message]
      Iterator<Session> iterator = chatroomUsers.iterator()

      while (iterator.hasNext()) {
        def user = iterator.next()
        log.info(user.userProperties.toMapString())
        if (user.userProperties.get("chatId") == chatId) {
          user.basicRemote.sendText((output as JSON).toString())
        }
      }
    }
  }

  @OnError
  public void handleError(Throwable t) {
    log.error("An error occurred.", t)
  }
}