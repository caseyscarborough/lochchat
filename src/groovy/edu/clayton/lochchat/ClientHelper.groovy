package edu.clayton.lochchat

import groovy.util.logging.Log4j

import javax.websocket.ContainerProvider
import javax.websocket.DeploymentException
import javax.websocket.Session
import javax.websocket.WebSocketContainer

@Log4j
class ClientHelper {

  public static Session getConnectedSession(String url) {
    WebSocketContainer container = ContainerProvider.webSocketContainer
    Session session = null
    try {
      session = container.connectToServer(WebsocketClient.class, URI.create(url))
      return session
    } catch (DeploymentException e) {
      log.error("An error occurred: ${e.message}")
    } catch (IOException e) {
      log.error("An error occurred: ${e.message}")
    }
    return session
  }

  public static void sendMessage(Session session, String message) {
    try {
      session.basicRemote.sendText(message)
    } catch (IOException e) {
      log.error("An error occurred sending the message ${e.message}")
    }
  }
}
