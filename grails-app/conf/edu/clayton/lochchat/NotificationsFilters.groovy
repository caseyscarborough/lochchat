package edu.clayton.lochchat

class NotificationsFilters {

    def springSecurityService

    def filters = {
        all(controller:'*', action:'*') {
            after = { Map model ->
                if (springSecurityService.loggedIn) {
                    User user = (User) springSecurityService.currentUser
                    model?.notifications = Notification.findAllByUserAndIsDismissed(user, false)?.sort { it.dateCreated }
                }
            }
        }
    }
}
