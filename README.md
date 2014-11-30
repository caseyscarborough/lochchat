# LochChat

LochChat is a group discussion and collaboration tool targeted at college students, which allows multi-user chatting along with concurrent editing of a central workspace.

It is being developed as part of a senior project.

## Requirements

The following dependencies are required to run the application:

* JDK 7 or 8
* Grails 2.4.4 (not required if using Grails wrapper)
* MySQL

## Getting Started

Clone the repository by executing the following:

```bash
git clone git@github.com:caseyscarborough/lochchat.git
cd lochchat
```

Create an [`EmailConfig.groovy`](https://github.com/caseyscarborough/lochchat/blob/ce6c36dc22287d62a3c95168e39b750e84c959a3/grails-app/conf/EmailConfig.example.groovy), and update the path to it in the [`grails-app/conf/Config.groovy`](https://github.com/caseyscarborough/lochchat/blob/ce6c36dc22287d62a3c95168e39b750e84c959a3/grails-app/conf/Config.groovy#L17) file.

Ensure that you've created the following databases on your local MySQL instance: `lochchat_dev`, and run the database migrations:

```bash
grails dbm-update
```

You can then start the application up:

```bash
grails run-app
```

## Deployment

Deployment is currently tested and working in Tomcat 8. Setting the default protocol to `org.apache.coyote.http11.Http11Nio2Protocol` in your `$CATALINA_HOME/conf/server.xml` will improve the websocket connections:

```xml
<Connector port="8080"
           protocol="org.apache.coyote.http11.Http11Nio2Protocol"
           connectionTimeout="20000"
           redirectPort="8443" />
```

## Credits

The following open source libraries were used in the development of LochChat:

* [Grails Framework](http://grails.org)
* [Grails Spring Security Plugin](http://grails.org/plugin/spring-security-core)
* [jQuery](http://jquery.com/)
* [Bootstrap CSS Framework](http://getbootstrap.com/)
* [Flat UI Bootstrap Theme](http://designmodo.github.io/Flat-UI/)
* [FontAwesome](http://fontawesome.io/)
* [ZeroClipboard](http://zeroclipboard.org/)
* [Linkify jQuery plugin](http://soapbox.github.io/jQuery-linkify/)
* [RTCMultiConnection](http://www.rtcmulticonnection.org/)
* [Google Mobwrite](http://code.google.com/p/google-mobwrite/)
* [Bootstrap SweetAlert](http://lipis.github.io/bootstrap-sweetalert/)