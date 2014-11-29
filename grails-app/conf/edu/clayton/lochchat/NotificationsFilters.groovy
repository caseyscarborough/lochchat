package edu.clayton.lochchat

class NotificationsFilters {

    def grailsLinkGenerator
    def springSecurityService

    def filters = {
        all(controller:'*', action:'*') {
            after = { Map model ->
                if (springSecurityService.loggedIn) {
                    User user = (User) springSecurityService.currentUser
                    def notifications = Notification.findAllByUserAndIsDismissed(user, false)?.sort { it.dateCreated }
                    model?.notifications = notifications
                    model?.notificationEndpoint = grailsLinkGenerator.link(uri: "/notificationEndpoint/${user.username}", absolute: true)
                }
            }
        }
    }
}
