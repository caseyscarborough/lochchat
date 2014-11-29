package edu.clayton.lochchat

import groovy.util.logging.Log4j

import javax.websocket.ClientEndpointConfig
import javax.websocket.Endpoint
import javax.websocket.EndpointConfig
import javax.websocket.MessageHandler
import javax.websocket.Session

import org.glassfish.tyrus.client.ClientManager

@Log4j
public class NotificationClient {

  private Session session

  def connect(String url) {
    try {
      final ClientEndpointConfig configuration = ClientEndpointConfig.Builder.create().build()
      ClientManager client = ClientManager.createClient()
      client.connectToServer(new Endpoint() {
        @Override
        public void onOpen(Session session,EndpointConfig config) {
          NotificationClient.this.session = session
          NotificationClient.this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
              log.debug("Received message: " + message)
            }
          })
        }
      }, configuration,new URI(url))

    } catch (Exception e) {
      e.printStackTrace()
    }
  }

  def sendMessage(String message) throws IOException, InterruptedException {
    session.getBasicRemote().sendText(message)
  }

  def disconnect() {
    session.close()
  }
}