package edu.clayton.lochchat.marshallers

import grails.converters.JSON
import groovy.util.logging.Log4j
import org.springframework.http.HttpStatus

@Log4j
class HttpStatusMarshaller implements ObjectMarshaller {

  void register() {
    log.info("Registering JSON marshaller for HttpStatus")
    JSON.registerObjectMarshaller(HttpStatus) { HttpStatus status ->
      return [phrase: status.reasonPhrase, code: status.value()]
    }
  }
}
