package edu.clayton.lochchat

class LochChatTagLib {
    static defaultEncodeAs = [taglib:'raw']
    static namespace = "lochchat"

    def logHtml = { attrs ->
        Log logInstance = attrs.logInstance
        logInstance?.messages?.sort { it.dateCreated }?.each { message ->
            out << '<div class="chat-text old">' + message + '</div>'
        }
    }
}
