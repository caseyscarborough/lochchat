package edu.clayton.lochchat.marshallers

import edu.clayton.lochchat.Log
import grails.converters.JSON
import groovy.util.logging.Log4j

@Log4j
class LogMarshaller implements ObjectMarshaller {

  void register() {
    log.info("Registering JSON marshaller for Log")
    JSON.registerObjectMarshaller(Log) { Log logInstance ->
      return [
        id: logInstance.id,
        dateCreated: logInstance.dateCreated,
        messages: logInstance.messages
      ]
    }
  }
}
