<html>
<head>
  <meta name="layout" content="main">
  <title>Sign Up</title>
</head>

<body>
<div class="container">
  <div class="row">
    <div class="col-md-8 col-md-offset-2">
      <h3>Sign Up</h3>
      <p>Use the following form to sign up.</p>

      <g:if test='${emailSent}'>
        <div class='alert alert-info'><small><g:message code='spring.security.ui.register.sent'/></small></div>
      </g:if>
      <g:else>
        <g:form action='register' name='registerForm'>
          <div class="row">
            <div class="col-md-6">
              <ss2ui:fieldRow type="text" name='firstName' labelCode='user.firstName.label' bean="${command}"
                                 size='40' labelCodeDefault='First Name' value="${command.firstName}"/>
            </div>
            <div class="col-md-6">
              <ss2ui:fieldRow type="text" name='lastName' labelCode='user.lastName.label' bean="${command}"
                                 size='40' labelCodeDefault='Last Name' value="${command.lastName}"/>
            </div>
          </div>
          <ss2ui:fieldRow type="email" name='email' bean="${command}" value="${command.email}" labelCode='user.email.label' labelCodeDefault='Email Address'/>
          <ss2ui:fieldRow type="text" name='username' labelCode='user.username.label' bean="${command}" labelCodeDefault='Username' value="${command.username}"/>
          <ss2ui:fieldRow type="password" name='password' labelCode='user.password.label' bean="${command}" labelCodeDefault='Password' value="${command.password}"/>
          <ss2ui:fieldRow type="password" name='password2' labelCode='user.password2.label' bean="${command}" labelCodeDefault='Password Confirmation' value="${command.password2}"/>

          <input type="submit" class="btn btn-primary" value="Create your account">
        </g:form>
      </g:else>
    </div>
  </div>
</div>
<script type='text/javascript'>
  $(function() {
    $('#firstName').focus();
  });
</script>
</body>
</html>