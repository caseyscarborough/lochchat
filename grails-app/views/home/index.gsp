<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Home</title>
  <meta name="layout" content="main">
  <script>
    $(document).ready(function() {
      Chat.init();
      $('.popover-link').popover({ trigger: 'hover' });
      $('.bootstrap-tagsinput input').focus(function() {
        $(this).parent().css("border-color", "#e67e22");
      }).blur(function() {
        $(this).parent().css("border-color", "#ebedef");
      });
    })
  </script>
</head>

<body>
<div id="jumbotron" class="jumbotron">
  <div id="jumbotron-background-image"></div>
  <div class="col-md-8 col-md-offset-2 new-chat">
    <h3><asset:image src="loch.png" class="logo" /> Welcome to LochChat!</h3>
    <p><strong>LochChat is a group discussion and collaboration tool targeted at college students, which allows multi-user chatting along with concurrent editing of a central workspace.</strong></p>
    <p><strong>Use the form below to enter a new chatroom:</strong></p>
    <div id="error-message" class="alert alert-danger hidden"></div>
    <div class="form-group">
      <label for="chatroom-url" class="fui-chat">
        Chatroom URL
      </label>
      <div class="input-group">
        <input id="chatroom-url" class="form-control" placeholder="Chatroom URL" value="${chatroomUrl}" disabled>
        <span id="copy-button" data-clipboard-text="${chatroomUrl}" title="Copy URL" class="btn-primary input-group-addon">Copy&nbsp;&nbsp;<i class="fa fa-copy"></i></span>
      </div>
    </div>
    <div class="form-group">
      <label for="chatroom-emails" class="fui-mail">
        Enter the email addresses to invite:
        <a href="#" class="popover-link" data-toggle="popover" title="Emails to Invite" data-content="Use the following text box to enter the email addresses of people that you'd like to invite to this chatroom. Press the Enter key between each email.">What's this?</a>
      </label>

      <input id="chatroom-emails" class="form-control tagsinput" data-role="tagsinput" placeholder="Email Addresses">
    </div>
    <button type="button" class="btn btn-primary btn-lg" id="create-chatroom-button" data-loading-text="Creating Chatroom...">Create Chatroom</button>
  </div>
</div>
</body>
</html>