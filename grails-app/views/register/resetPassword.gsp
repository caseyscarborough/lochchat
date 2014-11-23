<html>
<head>
  <meta name="layout" content="main">
  <title>Forgot Password</title>
</head>

<body>
<div class="container">
  <div class="row">
    <div class="col-md-8 col-md-offset-2">
      <h3>Reset Password</h3>
      <g:form action='resetPassword' name='resetPasswordForm' autocomplete='off'>
        <g:hiddenField name='t' value='${token}'/>
        <div class="sign-in">
        <p><g:message code='spring.security.ui.resetPassword.description'/></p>

          <ss2ui:fieldRow type='password' name='password' labelCode='resetPasswordCommand.password.label' bean="${command}"
                                   labelCodeDefault='Password' value="${command?.password}"/>

          <ss2ui:fieldRow type='password' name='password2' labelCode='resetPasswordCommand.password2.label' bean="${command}"
                                   labelCodeDefault='Password Confirmation' value="${command?.password2}"/>

          <input type="submit" class="btn btn-primary" content="Update my password">
        </div>
      </g:form>
    </div>
  </div>
</div>
<script>
$(document).ready(function() {
	$('#password').focus();
});
</script>
</body>
</html>
