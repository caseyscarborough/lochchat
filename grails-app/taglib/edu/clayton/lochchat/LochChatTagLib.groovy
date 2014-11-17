package edu.clayton.lochchat

class LochChatTagLib {
    static defaultEncodeAs = [taglib:'raw']
    static namespace = "lochchat"

    def logHtml = { attrs ->
        Log logInstance = attrs.logInstance
        logInstance.contents.split("\n").each { line ->
            out << '<div class="chat-text old">' + line + '</div>'
        }
    }
}
