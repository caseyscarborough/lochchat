<html>
<head>
  <meta name="layout" content="main">
  <title>Login</title>
</head>

<body>
<div class="container">
  <div class="row">
    <div class="col-md-8 col-md-offset-2">
      <h4>Use the following form to log in.</h4>

      <g:if test='${flash.message}'>
        <div class='alert alert-info'><small>${flash.message}</small></div>
      </g:if>

      <form action='${postUrl}' method='POST' id='loginForm' autocomplete='off'>
        <div class="form-group">
          <label for="username">Username</label>
          <input type='text' class='form-control' name='j_username' id='username' placeholder="Username">
        </div>

        <div class="form-group">
          <label for='password'>Password</label>
          <input type='password' class='form-control' name='j_password' id='password' placeholder="Password">
        </div>

        <div class="checkbox">
          <label for='remember_me'>
            <input type='checkbox' name='${rememberMeParameter}' id='remember_me' <g:if test='${hasCookie}'>checked='checked'</g:if>> Remember Me
          </label>
        </div>

        <input type='submit' class="btn btn-primary" id="submit" value='Login'><br><br>

        <p>
          <g:link controller='register' action='forgotPassword'><g:message code='spring.security.ui.login.forgotPassword'/></g:link><br>
          <s2ui:linkButton elementId='register' controller='register' messageCode='spring.security.ui.login.register'/>
        </p>
      </form>
    </div>
  </div>
</div>
<script type='text/javascript'>
  $(document).ready(function() {
    $('#username').focus();
  });

  <s2ui:initCheckboxes/>
</script>
</body>
</html>
