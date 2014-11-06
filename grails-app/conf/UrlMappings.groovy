class UrlMappings {

  static mappings = {
    "/$controller/$action?/$id?(.$format)?" {
      constraints {
        // apply constraints here
      }
    }

    "/chatroom/$uniqueId"(controller: "chat", action: "room")
    "/"(controller: "home", action: "index")
    "500"(view: '/error')
  }
}
