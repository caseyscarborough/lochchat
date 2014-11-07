<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Home</title>
  <meta name="layout" content="main">
  <script>
    $(document).ready(function() {
      Chat.init();
      $('.popover-link').popover({})
    })
  </script>
</head>

<body>
<div class="container">
  <div class="row">
    <div class="col-md-8 col-md-offset-2 new-chat">
      <h3>Welcome to LochChat!</h3>
      <p>Use the form below to enter a new chatroom:</p>
      <div id="error-message" class="alert alert-danger hidden"></div>
      <div class="form-group">
        <label for="chatroom-url">Chatroom URL</label>
        <input id="chatroom-url" class="form-control" placeholder="Chatroom URL" value="${chatroomUrl}" disabled>
      </div>
      <div class="form-group">
        <label for="chatroom-emails" >
          Enter the email addresses to invite:
          <a href="#" class="popover-link" data-toggle="popover" title="Emails to Invite" data-content="Use the following text box to enter the email addresses of people that you'd like to invite to this chatroom. Press the Enter key between each email.">What's this?</a>
        </label>

        <input id="chatroom-emails" class="form-control tagsinput" data-role="tagsinput" placeholder="Email Addresses">
      </div>
      <button type="button" class="btn btn-primary" id="create-chatroom-button" data-loading-text="Creating Chatroom...">Create Chatroom</button>
    </div>
  </div>
</div>
</body>
</html>