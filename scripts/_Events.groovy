import groovy.xml.StreamingMarkupBuilder

eventWebXmlEnd = {String tmpfile ->

  def root = new XmlSlurper().parse(webXmlFile)

  root.appendNode {
    'listener' {
      'listener-class' (
        'edu.clayton.lochchat.LochChatServletChatListener'
      )
    }
  }
  webXmlFile.text = new StreamingMarkupBuilder().bind {
    mkp.declareNamespace(
      "": "http://java.sun.com/xml/ns/javaee")
    mkp.yield(root)
  }
}