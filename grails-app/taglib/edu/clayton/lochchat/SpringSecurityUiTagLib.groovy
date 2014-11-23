package edu.clayton.lochchat

class SpringSecurityUiTagLib extends grails.plugin.springsecurity.ui.SecurityUiTagLib {

  static namespace = 'ss2ui'

  Closure fieldErrors = { attrs ->
    def bean = attrs.remove('bean')
    if (bean) {
      out << eachError(attrs, {
        out << "<span class='s2ui_error'>${message(error:it)}</span>"
      })
    }
  }

  Closure fieldRow = { attrs ->
    String labelCodeDefault = attrs.remove('labelCodeDefault')
    String name = getRequiredAttribute(attrs, 'name', 'textFieldRow')
    String labelCode = getRequiredAttribute(attrs, 'labelCode', 'textFieldRow')
    def value = getRequiredAttribute(attrs, 'value', 'textFieldRow')
    def bean = getRequiredAttribute(attrs, 'bean', 'textFieldRow')
    def type = attrs.type

    out << """
		<div class='form-group'>
			<label for="${name}">${message(code: labelCode, default: labelCodeDefault)}</label>&nbsp;&nbsp;${fieldErrors(bean: bean, field: name)}
			<input type="${type}" id="${name}" class="form-control" name="${name}" placeholder="${message(code: labelCode, default: labelCodeDefault)}" value="${value ?: ''}" required>
    </div>
		"""
  }
}
