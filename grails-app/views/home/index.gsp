<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Home</title>
  <meta name="layout" content="main">
  <script>
    $(document).ready(function() {
      Home.init();
      $('.popover-link').popover({ trigger: 'hover' });
    })
  </script>
</head>

<body>
<div class="container">
  <div class="row">
    <div id="jumbotron" class="jumbotron">
      <div id="jumbotron-background-image"></div>
      <div class="new-chat">
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
  </div>
  <div class="row">
    <div class="col-md-3 homepage-info">
      <span class="homepage-icon">
        <asset:image src="flat-ui/dist/img/icons/svg/mail.svg" />
      </span>
      <span class="homepage-description">
        <h3>Sharing is Easy</h3>
        Easily invite others to your chatroom before or during your meeting via an intuitive URL copy option, or by entering your teammates' email addresses.
      </span>
    </div>
    <div class="col-md-3 homepage-info">
      <span class="homepage-icon">
        <asset:image src="flat-ui/dist/img/icons/svg/pencils.svg" />
      </span>
      <span class="homepage-description">
        <h3>Simple Collaboration</h3>
        A real-time shared workspace allows you and your teammates to collaborate simultaneously on a project, without having to meet up.
      </span>
    </div>
    <div class="col-md-3 homepage-info">
      <span class="homepage-icon">
        <asset:image src="flat-ui/dist/img/icons/svg/clipboard.svg" />
      </span>
      <span class="homepage-description">
        <h3>Save Your Work</h3>
        Export options for your chat log and your workspace give you a hard record of your meeting.
      </span>
    </div>
  </div>
</div>
<footer>
  <div class="container">
    <div class="row">
      <div class="col-md-5 col-md-offset-1">
        <h3>Credits</h3>
        <p>Created with the help of Casey Scarborough, Eli Fernandes, and Todd Birchfield for our Human Computer Interface class at <a target="_blank" href="http://clayton.edu">Clayton State University</a>.</p>
      </div>
      <div class="col-md-5 col-md-offset-1">
        <h3>Contribute</h3>
        <span class="github-image"><h2><i class="fa fa-github"></i></h2></span>
        <p><br><a target="_blank" href="https://github.com/caseyscarborough/lochchat">Contribute to this project at GitHub.</a></p>
      </div>
    </div>
  </div>
</footer>
</body>
</html>