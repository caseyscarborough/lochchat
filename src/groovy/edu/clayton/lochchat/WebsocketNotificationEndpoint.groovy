package edu.clayton.lochchat

import grails.util.Environment
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
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
@ServerEndpoint("/notificationEndpoint/{username}")
class WebsocketNotificationEndpoint implements ServletContextListener {

  private final Logger log = LoggerFactory.getLogger(getClass().name)
  static final Set<Session> subscribers = ([] as Set).asSynchronized()

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ServletContext servletContext = sce.servletContext
    ServerContainer serverContainer = servletContext.getAttribute("javax.websocket.server.ServerContainer")
    try {
      if (Environment.current == Environment.DEVELOPMENT) {
        serverContainer.addEndpoint(WebsocketNotificationEndpoint)
      }
      ApplicationContext ctx = (ApplicationContext) servletContext.getAttribute(GrailsApplicationAttributes.APPLICATION_CONTEXT)
      GrailsApplication grailsApplication = ctx.grailsApplication
      Integer defaultMaxSessionIdleTimeout =  grailsApplication.config.myservlet.timeout ?: 0
      serverContainer.defaultMaxSessionIdleTimeout = defaultMaxSessionIdleTimeout
    } catch (IOException e) {
      log.error(e.message, e)
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent servletContextEvent) {
  }

  @OnOpen
  public void onOpen(Session userSession, @PathParam("username") String username) {
    log.debug("User $username connected to notification endpoint")
    userSession.userProperties.put("username", username)
    subscribers.add(userSession)
  }

  @OnMessage
  public void onMessage(String message, Session userSession, @PathParam("username") String username) throws IOException {
    def iterator = subscribers.iterator()
    while (iterator.hasNext()) {
      def user = iterator.next()
      log.debug("Sending message to $username")
      if (user.userProperties.get("username") == username) {
        try {
          user.basicRemote.sendText(message)
        } catch (IOException e) {
          // log.error(e)
        }
      }
    }
  }

  @OnClose
  public void onClose(Session userSession, CloseReason reason) {
    subscribers.remove(userSession)
  }

  @OnError
  public void onError(Throwable t) {
    log.error("An error occurred.", t)
  }
}
