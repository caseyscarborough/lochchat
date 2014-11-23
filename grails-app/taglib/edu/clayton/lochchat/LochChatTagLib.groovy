package edu.clayton.lochchat

class LochChatTagLib {
  static defaultEncodeAs = [taglib: 'raw']
  static namespace = "lochchat"

  def logHtml = { attrs ->
    Log logInstance = attrs.logInstance
    logInstance?.messages?.sort { it.dateCreated }?.each { message ->
      out << '<div class="chat-text old">' + message + '</div>'
    }
  }

  def gravatarFor = { attrs ->
    User user = attrs.user
    if (!attrs.width) {
      attrs.width = 400
    }
    out << "<img src='https://gravatar.com/avatar/${user.email.encodeAsMD5()}?s=800' width='${attrs.width}' class='${attrs.class}'>"
  }

}
