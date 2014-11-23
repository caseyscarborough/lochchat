<html>
<head>
  <meta name="layout" content="main">
  <title>Forgot Password</title>
</head>

<body>
<div class="container">
  <div class="row">
    <div class="col-md-8 col-md-offset-2">
      <h3>Forgot Password</h3>

      <g:if test='${emailSent}'>
        <div class='alert alert-info'><small><g:message code='spring.security.ui.register.sent'/></small></div>
      </g:if>
      <g:else>
        <g:form action='forgotPassword' name="forgotPasswordForm" autocomplete='off'>
          <p><g:message code='spring.security.ui.forgotPassword.description'/></p>

          <div class="form-group">
            <label for="username">Username</label>&nbsp;&nbsp;<g:if test="${flash.error}"><span class="s2ui_error">${flash.error}</span></g:if>
            <input type="text" id="username" class="form-control" name="username" placeholder="Username" required>
          </div>

          <input type="submit" class="btn btn-primary" content="Reset my password">
        </g:form>
      </g:else>
    </div>
  </div>
</div>
<script>
$(document).ready(function() {
	$('#username').focus();
});
</script>

</body>
</html>
