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
        <li<g:if test="${params.controller == 'home'}"> class="active"</g:if>><g:link controller="home" action="index">Home</g:link></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
      <sec:ifNotLoggedIn>
        <li<g:if test="${params.controller == 'user'}"> class="active"</g:if>><g:link controller="user" action="create">Sign Up</g:link></li>
        <li<g:if test="${params.controller == 'login'}"> class="active"</g:if>><g:link controller="login" action="auth">Login</g:link></li>
      </sec:ifNotLoggedIn>
      <sec:ifLoggedIn>
        <li<g:if test="${params.controller == 'user'}"> class="active"</g:if>><g:link controller="user" action="profile">Profile</g:link></li>
        <li><g:link controller="logout" action="index">Logout</g:link></li>
      </sec:ifLoggedIn>
      </ul>
    </div>
  </div>
</nav>