package edu.clayton.lochchat

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.springframework.http.HttpStatus

@Secured(["IS_AUTHENTICATED_REMEMBERED"])
class NotificationController {

  def springSecurityService

  static allowedMethods = [view: 'PUT']

  def view() {
    def user = springSecurityService.currentUser
    def notifications = request.JSON.notifications.split(",")
    notifications.each { String id ->
      def n = Notification.get(id)
      if (n && n.user == user) {
        n.isViewed = true
        n.isDismissed = true
        n.save(flush: true)
      }
    }
    render ([status: HttpStatus.OK] as JSON)
  }
}
