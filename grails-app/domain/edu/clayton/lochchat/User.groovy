package edu.clayton.lochchat

class User {

  transient springSecurityService

  String firstName
  String lastName
  String email
  String username
  String password

  Date dateCreated
  Date lastUpdated

  boolean enabled = true
  boolean accountExpired
  boolean accountLocked
  boolean passwordExpired

  static transients = ['springSecurityService']
  static hasMany = [chats: Chat]

  static constraints = {
    username blank: false, unique: true
    password blank: false
    email blank: false, unique: true
    firstName blank: false
    lastName blank: false
  }

  static mapping = {
    password column: '`password`'
  }

  Set<Role> getAuthorities() {
    UserRole.findAllByUser(this).collect { it.role }
  }

  def beforeInsert() {
    encodePassword()
  }

  def beforeUpdate() {
    if (isDirty('password')) {
      encodePassword()
    }
  }

  def getName() {
    "$firstName $lastName"
  }

  String toString() {
    name
  }

  protected void encodePassword() {
    password = springSecurityService?.passwordEncoder ? springSecurityService.encodePassword(password) : password
  }
}
