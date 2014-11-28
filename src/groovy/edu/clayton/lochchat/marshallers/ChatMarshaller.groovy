package edu.clayton.lochchat.marshallers

import edu.clayton.lochchat.Chat
import grails.converters.JSON
import groovy.util.logging.Log4j

@Log4j
class ChatMarshaller implements ObjectMarshaller {

  void register() {
    log.info("Registering JSON marshaller for Chat")
    JSON.registerObjectMarshaller(Chat) { Chat chat ->
      return [
        id       : chat.id,
        uniqueId : chat.uniqueId,
        startTime: chat.startTime,
        endTime  : chat.endTime,
        log      : log
      ]
    }
  }
}
