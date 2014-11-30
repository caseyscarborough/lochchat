package edu.clayton.lochchat

import groovy.util.logging.Log4j

import javax.websocket.ClientEndpoint
import javax.websocket.OnError
import javax.websocket.OnMessage
import javax.websocket.OnOpen
import javax.websocket.Session

@Log4j
@ClientEndpoint
public class WebsocketClient {

  @OnOpen
  public void onOpen(Session session) {
    log.debug("Connected to endpoint: " + session.basicRemote)
  }

  @OnMessage
  public void onMessage(String message) {
    log.debug("Received message... $message")
  }

  @OnError
  public void onError(Throwable t) {
    log.error("An error occurred: ${t.message}")
  }
}