<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Home</title>
  <meta name="layout" content="main">
</head>

<body>
<div class="container">
  <div class="row">
    <div class="col-md-6 col-md-offset-3 new-chat">
      <h3>Welcome to LochChat!</h3>
      <p>Use the form below to enter a new chatroom:</p>
      <form role="form">
        <div class="form-group">
          <label for="chatroom-url">Chatroom URL</label>
          <input id="chatroom-url" class="form-control" placeholder="Chatroom URL" value="${chatroomUrl}">
        </div>
        <div class="form-group">
          <label for="chatroom-emails" >Enter the email addresses to invite: </label>
          <input id="chatroom-emails" class="form-control tagsinput" data-role="tagsinput" placeholder="Email Addresses">
        </div>
        <button class="btn btn-primary">Enter Chatroom</button>
      </form>
    </div>
  </div>
</div>
</body>
</html>