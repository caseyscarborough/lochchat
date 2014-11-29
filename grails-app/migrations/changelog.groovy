databaseChangeLog = {

  include file: '201411111458-create-chat-class.groovy'

  include file: '201411171304-create-log-class.groovy'

  include file: '201411180901-create-message-class.groovy'

  include file: '201411211415-create-fileupload-class.groovy'

  include file: '201411211421-associate-chat-with-fileupload.groovy'

  include file: '201411211448-add-unique-id-to-fileupload.groovy'

  include file: '201411211724-add-original-filename-to-fileupload.groovy'

  include file: '201411211835-add-spring-security-classes.groovy'

  include file: '201411211919-add-name-and-email-to-user.groovy'

  include file: '201411211950-associate-user-and-chat.groovy'

  include file: '201411221105-add-dates-to-user.groovy'

  include file: '201411221725-add-spring-security-ui.groovy'

	include file: '201411291301-create-notification-class.groovy'

	include file: '201411291350-add-url-to-notification.groovy'
}
