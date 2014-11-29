package edu.clayton.lochchat

import grails.converters.JSON
import grails.util.Environment
import groovy.util.logging.Log4j
import org.apache.commons.lang.StringEscapeUtils
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes as GA
import org.springframework.context.ApplicationContext

import javax.servlet.ServletContext
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener
import javax.websocket.*
import javax.websocket.server.PathParam
import javax.websocket.server.ServerContainer
import javax.websocket.server.ServerEndpoint
import java.nio.ByteBuffer

@Log4j
@WebListener
@ServerEndpoint("/chatEndpoint/{chatId}")
public class ChatEndpoint implements ServletContextListener {

  static final Map<String, Set<Session>> chatroomUsers = ([:] as HashMap).asSynchronized()
  static GrailsApplication grailsApplication
  static ConfigObject config

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    ServletContext servletContext = sce.servletContext
    ServerContainer serverContainer = (ServerContainer) servletContext.getAttribute("javax.websocket.server.ServerContainer")
    try {
      if (Environment.current == Environment.DEVELOPMENT) {
        serverContainer.addEndpoint(ChatEndpoint)
      }
      ApplicationContext ctx = (ApplicationContext) servletContext.getAttribute(GA.APPLICATION_CONTEXT)
      grailsApplication = ctx.grailsApplication
      config = grailsApplication.config
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
  public void onOpen(Session userSession, @PathParam("chatId") String chatId) {
    def chatroom = chatroomUsers.get(chatId)
    if (!chatroom) {
      chatroomUsers.put(chatId, [] as Set)
      chatroom = chatroomUsers.get(chatId)
    }

    if (userSession.properties.httpSessionId) {
      def count = 0
      def iterator = chatroom.iterator()
      while(iterator.hasNext()) {
        def session = iterator.next()
        if (session.properties.httpSessionId) {
          count++
        }
      }

      if (count >= config.lochchat.maxParticipants) {
        def javascriptCallback = """
          swal({
          title:'This chatroom is full',
          text:'Chatrooms are limited to a total of ${config.lochchat.maxParticipants} participants. Try again later, or create your own.',
          type:'error',
          showCancelButton:false
          }, function(){ window.location.href='/lochchat/'; });
        """;
        sendMessage([callback: javascriptCallback], userSession);
        return
      }
    }

    userSession.userProperties.put("chatId", chatId)
    chatroom.add(userSession)
  }

  @OnMessage
  public void uploadFile(ByteBuffer data, boolean last, Session userSession) {
    String filename = (String) userSession.userProperties.get("filename")
    String username = (String) userSession.userProperties.get("username")
    String chatId = (String) userSession.userProperties.get("chatId")
    log.debug("$username uploading binary data for $filename...")

    FileOutputStream outputStream = (FileOutputStream) userSession.userProperties.get("outputStream")
    while (data.hasRemaining()) {
      try {
        outputStream.write(data.get())
      } catch (IOException e) {
        log.error("An error occurred trying to write data to file.", e)
        def javascriptCallback = "swal('A fatal error occurred', 'Your session will now be reset.', 'error'); _resetUploadButton();"
        sendMessage([message: '', callback: javascriptCallback], userSession)
        userSession.close()
      }
    }

    if (last) {
      log.debug("Closing file...")
      try {
        outputStream.close()
        outputStream.flush()
        userSession.userProperties.remove("outputStream")
        userSession.userProperties.remove("filename")
        FileUpload file = null
        def downloadUrl = ""
        FileUpload.withTransaction {
          def chat = Chat.findByUniqueId(chatId)
          file = FileUpload.findAllByOriginalFilenameAndChat(filename, chat).sort { it.dateCreated }.reverse().first()
          downloadUrl = file.downloadUrl
        }
        sendAndPersistMessage("$username uploaded <a href='$downloadUrl' target='_blank'>$filename</a>.", chatId)
        def javascriptCallback = "swal('Success!', 'Your file was successfully uploaded.', 'success'); _resetUploadButton();"
        sendMessage([message: '', callback: javascriptCallback], userSession)
      } catch (IOException e) {
        log.error("An error occurred closing the file.", e)
      }
    }
  }

  @OnMessage
  public String onMessage(String message, Session userSession) throws IOException {
    message = StringEscapeUtils.escapeHtml(message)
    log.debug("Received message from user: $message")

    String username = userSession.userProperties.get("username")
    String chatId = userSession.userProperties.get("chatId")

    if (message.startsWith("_serverMessage:")) {
      def serverMessage = new StringBuilder()
      message.split(":")?.eachWithIndex { string, index ->
        if (index != 0) {
          serverMessage.append(string)
        }
      }
      sendAndPersistMessage(serverMessage.toString(), chatId)
    } else if (!username) {
      log.debug("User has connected to the chatroom.")
      userSession.userProperties.put("username", message)
      sendAndPersistMessage(message + " has joined the chatroom.", chatId)
    } else if (message.startsWith("_file:")) {
      def filename = message.split(":")[1]
      log.debug("Beginning file upload for file: $filename")
      try {
        def uniqueId = "${filename}-${new Date().toTimestamp().time}".encodeAsMD5()
        def newFilename = filename.substring(0, filename.lastIndexOf('.')) + "-$uniqueId" + filename.substring(filename.lastIndexOf('.'))
        FileOutputStream outputStream = new FileOutputStream("${config.lochchat.uploadDir}/$newFilename")
        userSession.userProperties.put("outputStream", outputStream)
        userSession.userProperties.put("filename", filename)

        FileUpload.withTransaction {
          def chat = Chat.findByUniqueId(userSession.userProperties.get("chatId"))
          new FileUpload(filename: newFilename, location: config.lochchat.uploadDir, chat: chat, uniqueId: uniqueId, originalFilename: filename).save(flush: true)
        }
      } catch (FileNotFoundException e) {
        log.error("An error occurred creating the file: $filename", e)
      }
    } else {
      sendAndPersistMessage(message, chatId, username)
    }
  }

  @OnClose
  public void onClose(Session userSession, CloseReason reason) {
    String chatId = userSession.userProperties.get("chatId")
    String username = userSession.userProperties.get("username")
    chatroomUsers.get(chatId)?.remove(userSession)
    log.info("User left chatroom for the following reason: ${CloseReason.CloseCodes.getCloseCode(reason.closeCode.code)}")
    if (chatId && username) {
      sendAndPersistMessage("${username} has left the chatroom.", chatId)
    }
  }

  @OnError
  public void onError(Throwable t) {
    log.error("An error occurred.", t)
  }

  private void sendAndPersistMessage(String message, String chatId, String username=null) {
    def output = [:]
    log.debug("Sending message to $chatId...")
    Message.withTransaction {
      def chat = Chat.findByUniqueId(chatId)
      new Message(user: username, contents: message, log: chat?.log).save(flush: true)
    }
    output.put("message", username ? "${username}: ${message}" : message)
    sendMessage(output, chatId)
  }

  private void sendMessage(Map output, String chatId) {
    Iterator<Session> iterator = chatroomUsers.get(chatId).iterator()

    while (iterator.hasNext()) {
      sendMessage(output, iterator.next())
    }
  }

  private void sendMessage(Map output, Session userSession) {
    try {
      userSession.basicRemote.sendText((output as JSON).toString())
    } catch (Exception e) {
      // log.error("An error occurred, but was caught.", e)
    }
  }
}