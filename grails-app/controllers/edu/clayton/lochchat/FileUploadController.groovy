package edu.clayton.lochchat

import grails.plugin.springsecurity.annotation.Secured

@Secured(['permitAll'])
class FileUploadController {

    def download(String chatId, String fileId) {
      def chat = Chat.findByUniqueId(chatId)
      def fileUpload = FileUpload.findByUniqueIdAndChat(fileId, chat)
      if (fileUpload) {
        def file = new File(fileUpload.path)
        if (file.exists()) {
          response.setContentType("application/octet-stream")
          response.setHeader("Content-disposition", "attachment;filename=${fileUpload.originalFilename}")
          response.outputStream << file.bytes
          response.outputStream.flush()
        }
        else response.sendError(404)
      }
    }
}
