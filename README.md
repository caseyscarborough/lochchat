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

Copy the sample DataSource and Email configuration files somewhere on your machine, and update them with your configuration. You will need to update the location that you copy them to [`grails-app/conf/Config.groovy`](https://github.com/caseyscarborough/lochchat/blob/master/grails-app/conf/Config.groovy#L15).

```bash
cp grails-app/conf/DataSource.example.groovy /etc/grails/config/lochchat/DataSource.groovy
cp grails-app/conf/EmailConfig.example.groovy /etc/grails/config/lochchat/EmailConfig.groovy
```

Ensure that you've created the following local MySQL instance: `lochchat_dev`, run the database migrations, and start the application:

```bash
grails dbm-update
grails run-app
```

## Deployment

Deployment is currently tested and working in Tomcat 8. Setting the default protocol to `org.apache.coyote.http11.Http11NioProtocol` in your `$CATALINA_HOME/conf/server.xml` will improve the websocket connections:

```xml
<Connector port="8080"
           protocol="org.apache.coyote.http11.Http11NioProtocol"
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