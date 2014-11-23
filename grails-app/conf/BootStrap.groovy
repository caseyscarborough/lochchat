import edu.clayton.lochchat.Role

class BootStrap {

  def grailsApplication

  def init = { servletContext ->

    def uploadDir = new File(grailsApplication.config.lochchat.uploadDir)
    if (!uploadDir.exists()) {
      log.info("Creating upload directory at $uploadDir")
      uploadDir.mkdirs()
    }

    if (Role.count == 0) {
      new Role(authority: "ROLE_USER").save(flush: true)
      new Role(authority: "ROLE_ADMIN").save(flush: true)
    }
  }
  def destroy = {
  }
}
