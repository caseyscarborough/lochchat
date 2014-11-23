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
              <label for="firstName"">First Name</label>
              <input type='text' class='form-control' name='firstName' id='firstName' placeholder="First Name" value="${user?.firstName}" required>
            </div>
          </div>
          <div class="col-md-6">
            <div class="form-group">
              <label for="lastName"">Last Name</label>
              <input type='text' class='form-control' name='lastName' id='lastName' placeholder="Last Name" value="${user?.lastName}" required>
            </div>
          </div>
        </div>
        <div class="form-group">
          <label for="email">Email Address</label>
          <input type="email" class="form-control" name="email" id="email" placeholder="Email Address" value="${user?.email}" required>
        </div>
        <div class="form-group">
          <label for="username">Username</label>
          <input type='text' class='form-control' name='username' id='username' placeholder="Username" value="${user?.username}" required>
        </div>

        <div class="form-group">
          <label for='password'>Password</label>
          <input type='password' class='form-control' name='password' id='password' placeholder="Password" required>
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