class UrlMappings {

  static mappings = {
    "/$controller/$action?/$id?(.$format)?" {
      constraints {
        // apply constraints here
      }
    }

    "/admin"(controller: "admin", action: "index")
    "/login"(controller: "login", action: "auth")
    "/logout"(controller: "logout", action: "index")
    "/signup"(controller: "register", action: "index")
    "/signup/pending"(controller: "register", action: "register")
    "/profile"(controller: "user", action: "profile")
    "/chat/log/$uniqueId"(controller: "chat", action: "exportLog")
    "/chat/workspace/$uniqueId"(controller: "chat", action: "exportWorkspace")
    "/chat/delete/$uniqueId"(controller: "chat", action: "delete")
    "/chatEndpoint"(uri: "/chatEndpoint")
    "/f/$chatId/$fileId"(controller: "fileUpload", action: "download")
    "/$uniqueId"(controller: "chat", action: "room")
    "/"(controller: "home", action: "index")
    "404"(view: '/notFound')
    "500"(view: '/error')
  }
}
