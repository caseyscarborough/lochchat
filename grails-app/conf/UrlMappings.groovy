class UrlMappings {

  static mappings = {
    "/$controller/$action?/$id?(.$format)?" {
      constraints {
        // apply constraints here
      }
    }

    "/f/$chatId/$fileId"(controller: "fileUpload", action: "download")
    "/r/$uniqueId"(controller: "chat", action: "room")
    "/"(controller: "home", action: "index")
    "500"(view: '/error')
  }
}
