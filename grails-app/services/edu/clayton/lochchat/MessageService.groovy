package edu.clayton.lochchat

import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder

class MessageService {

  def messageSource

  /**
   * This method is used to retrieve validation errors for any domain instance.
   * This will retrieve the Grails default message, or it will retrieve an error
   * from the messages properties file, if it is created in the following format:
   *   className.fieldName.errorCode
   *   i.e.: message.text.typeMismatch
   *
   * Example:
   *
   *   instance = Instance.new(field: "field", ...)
   *
   *   if (!instance.save(flush: true) {
   *     def error = messageService.getErrorMessage(instance)
   *     return [ success: false, message: "An error occurred: ${error}" ]
   *   }
   *
   *   return [ success: true, message: "Successfully created instance." ]
   *
   * @param domainInstance - The domain instance, of any type
   * @return
   */
  String getErrorMessage(domainInstance) {
    def error = domainInstance.errors.fieldError
    def className = domainInstance.class.simpleName.toLowerCase()
    def value = ""
    try {
      value = domainInstance."get${error?.field?.capitalize()}"()
    } catch (MissingPropertyException e) { /* Ignore, this is fine */ }

    def message = null
    try {
      message = messageSource.getMessage("${className}.${error?.field}.${error?.code}", [value].toArray(), LocaleContextHolder.locale)
    } catch (NoSuchMessageException e) {
      message = "Rejected value '${error?.rejectedValue}' for ${className} ${error?.field}. Violates ${error?.code} constraint."
    }
    return message
  }

  /**
   * This will retrieve a message from the messages.properties file in a cleaner format.
   * Example:
   *   flash.message = messageService.getMessage('home.controller.flash.message', [ 'param1', 'param2' ])
   *
   * @param key String - the key of the messages in messages.properties
   * @param params List - the list of properties
   * @return String
   * @throws NoSuchMessageException
   */
  String getMessage(String key, List params) throws NoSuchMessageException {
    return messageSource.getMessage(key, params.toArray(), LocaleContextHolder.locale)
  }
}