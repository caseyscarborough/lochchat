<nav class="navbar navbar-default navbar-inverse" role="navigation">
  <div class="container-fluid">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navigation-bar">
        <span class="sr-only">Toggle navigation</span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="${createLink(controller: 'home', action: 'index')}"><asset:image src="loch.png" class="logo" /> LochChat</a>
    </div>
    <div class="collapse navbar-collapse" id="navigation-bar">
      <ul class="nav navbar-nav">
        <li<g:if test="${params.controller == 'home'}"> class="active"</g:if>><g:link controller="home" action="index"><i class="fa fa-home"></i>&nbsp;&nbsp;Home</g:link></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
      <sec:ifNotLoggedIn>
        <li<g:if test="${params.controller == 'user'}"> class="active"</g:if>><g:link controller="register" action="index"><i class="fa fa-user"></i>&nbsp;&nbsp;Sign Up</g:link></li>
        <li<g:if test="${params.controller == 'login'}"> class="active"</g:if>><g:link controller="login" action="auth"><i class="fa fa-sign-in"></i>&nbsp;&nbsp;Login</g:link></li>
      </sec:ifNotLoggedIn>
      <sec:ifLoggedIn>
        <sec:ifAllGranted roles="ROLE_ADMIN">
          <li><g:link controller="admin"><i class="fa fa-dashboard"></i>&nbsp;&nbsp;Admin</g:link></li>
        </sec:ifAllGranted>
        <li class="dropdown">
          <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false">
            <i class="fa fa-exclamation-circle"></i>&nbsp;&nbsp;Notifications <span class="navbar-unread"<g:if test="${notifications?.size() == 0}"> style="display:none"</g:if>></span>
          </a>
          <ul class="dropdown-menu" role="menu" id="notifications-dropdown">
            <div class="notification" id="no-notifications"<g:if test="${notifications?.size() > 0}"> style="display:none"</g:if>>You currently have no notifications.</div>
            <g:each in="${notifications}" var="notification">
              <div class="notification" data-id="${notification.id}"<g:if test="${notification.url}"> data-url="${notification.url}"</g:if>>${notification.message}</div>
            </g:each>
          </ul>
        </li>
        <li<g:if test="${params.controller == 'user'}"> class="active"</g:if>><g:link controller="user" action="profile"><i class="fa fa-user"></i>&nbsp;&nbsp;Profile</g:link></li>
        <li><g:link controller="logout" action="index"><i class="fa fa-sign-out"></i>&nbsp;&nbsp;Logout</g:link></li>
      </sec:ifLoggedIn>
      </ul>
    </div>
  </div>
</nav>