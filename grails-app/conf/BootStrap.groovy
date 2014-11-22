class BootStrap {

  def grailsApplication

  def init = { servletContext ->

    def uploadDir = new File(grailsApplication.config.lochchat.uploadDir)
    if (!uploadDir.exists()) {
      log.info("Creating upload directory at $uploadDir")
      uploadDir.mkdirs()
    }
  }
  def destroy = {
  }
}
