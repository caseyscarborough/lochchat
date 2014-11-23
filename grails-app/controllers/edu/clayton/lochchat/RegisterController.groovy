package edu.clayton.lochchat

class RegisterController extends grails.plugin.springsecurity.ui.RegisterController {
}

class RegisterCommand extends grails.plugin.springsecurity.ui.RegisterCommand {
  String firstName
  String lastName
}