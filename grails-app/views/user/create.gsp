<html>
<head>
  <meta name="layout" content="main">
  <title>Sign Up</title>
</head>

<body>
<div class="container">
  <div class="row">
    <div class="col-md-8 col-md-offset-2">
      <h4>Use the following form to sign up.</h4>

      <g:if test='${flash.message}'>
        <div class='alert alert-info'><small>${flash.message}</small></div>
      </g:if>

      <form action='${createLink(controller: 'user', action: 'save')}' method='POST' id='loginForm' autocomplete='off'>
        <div class="row">
          <div class="col-md-6">
            <div class="form-group">
              <label for="first_name"">First Name</label>
              <input type='text' class='form-control' name='first_name' id='first_name' placeholder="First Name" required>
            </div>
          </div>
          <div class="col-md-6">
            <div class="form-group">
              <label for="last_name"">Last Name</label>
              <input type='text' class='form-control' name='last_name' id='last_name' placeholder="Last Name" required>
            </div>
          </div>
        </div>
        <div class="form-group">
          <label for="email">Email Address</label>
          <input type="email" class="form-control" name="email" id="email" placeholder="Email Address" required>
        </div>
        <div class="form-group">
          <label for="username">Username</label>
          <input type='text' class='form-control' name='j_username' id='username' placeholder="Username" required>
        </div>

        <div class="form-group">
          <label for='password'>Password</label>
          <input type='password' class='form-control' name='j_password' id='password' placeholder="Password" required>
        </div>

        <div class="checkbox">
          <label for='remember_me'>
            <input type='checkbox' name='${rememberMeParameter}' id='remember_me' <g:if test='${hasCookie}'>checked='checked'</g:if>> Remember Me
          </label>
        </div>

        <input type='submit' class="btn btn-primary" id="submit" value='Login'>
      </form>
    </div>
  </div>
</div>
<div id='login'>

</div>
<g:render template="../shared/footer" />
<script type='text/javascript'>
  <!--
  (function() {
    document.forms['loginForm'].elements['j_username'].focus();
  })();
  // -->
</script>
</body>
</html>