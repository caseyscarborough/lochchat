class UrlMappings {

  static mappings = {
    "/$controller/$action?/$id?(.$format)?" {
      constraints {
        // apply constraints here
      }
    }

    "/login"(controller: "login", action: "auth")
    "/logout"(controller: "logout", action: "index")
    "/signup"(controller: "user", action: "create")
    "/profile"(controller: "user", action: "profile")
    "/chat/delete/$uniqueId"(controller: "chat", action: "delete")
    "/chatEndpoint"(uri: "/chatEndpoint")
    "/f/$chatId/$fileId"(controller: "fileUpload", action: "download")
    "/$uniqueId"(controller: "chat", action: "room")
    "/"(controller: "home", action: "index")
    "500"(view: '/error')
  }
}
