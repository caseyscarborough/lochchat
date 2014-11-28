package edu.clayton.lochchat.marshallers

import edu.clayton.lochchat.Message
import grails.converters.JSON
import groovy.util.logging.Log4j

@Log4j
class MessageMarshaller implements ObjectMarshaller {

  void register() {
    log.info("Registering JSON marshaller for Message")
    JSON.registerObjectMarshaller(Message) { Message message ->
      return [
        user       : message.user,
        contents   : message.contents,
        dateCreated: message.dateCreated
      ]
    }
  }
}
