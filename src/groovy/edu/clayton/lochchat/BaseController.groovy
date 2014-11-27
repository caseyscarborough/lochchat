package edu.clayton.lochchat

import grails.converters.JSON
import org.springframework.http.HttpStatus

abstract class BaseController {

  def renderResult(Map result) {
    if (result.status instanceof HttpStatus) {
      response.status = result.status.value
    }
    render result as JSON
  }
}
