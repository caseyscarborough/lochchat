<html>
<head>
  <meta name="layout" content="main">
  <title>Profile</title>
  <script>$(function() { Profile.init(); });</script>
</head>

<body>
<div class="container profile">
  <div class="row">
    <div class="col-md-2">
      <a href="http://en.gravatar.com/" target="_blank" class="popover-link" data-title="Where does this come from?"
         data-content="Your avatar comes from <a href='http://en.gravatar.com/'>Gravatar</a>, a service that gives
         you a globally recognized avatar. Click your photo to change your avatar at <a href='http://en.gravatar.com/'>
         Gravatar</a>'s website." data-container="body" data-html="true" data-placement="right">
        <lochchat:gravatarFor user="${user}" width="100%" class="" />
      </a>
      <h4>${user}</h4>
      <p class="info"><a href="mailto:${user.email}">${user.email}</a></p>
      <p class="info">Joined on ${user.dateCreated.format("MMM. dd, yyyy")}</p>
    </div>
    <div class="col-md-10">
      <g:if test='${flash.message}'>
        <div class="alert alert-info alert-dismissible" role="alert">
          <button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
          <small>${flash.message}</small>
        </div>
      </g:if>
      <div class="profile-section">
        <h4>Chatrooms</h4>
        <g:if test="${user.chats.size() > 0}">
          <form name="hidden">
            <input type="hidden" id="new-url" value="${uniqueUrl}">
            <input type="hidden" id="fresh" name="fresh" value="0">
          </form>
          <p class="info"><a id="enter-new-chatroom" href="#">Enter new chatroom now</a></p>

          <p class="info">The following is a list of chatrooms that you have attended:</p>
          <table class="table table-condensed table-hover">
            <thead>
            <tr>
              <th>Chatroom ID</th>
              <th>Start Time</th>
              <th>URL</th>
              <th>Other Users</th>
              <th>Uploaded Files</th>
              <th>Options</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${user.chats.sort { it.startTime }}" var="chatroom">
              <tr id="chatroom-${chatroom.uniqueId}">
                <td class="monospace">${chatroom.uniqueId}</td>
                <td>${chatroom.formattedStartTime}</td>
                <td><g:link controller="chat" action="room" params="[uniqueId: chatroom.uniqueId]" target="_blank">${chatroom.url}</g:link></td>
                <td>
                  <g:if test="${(chatroom.users - user).size() > 0}">
                    <g:each in="${chatroom.users - user}" var="chatroomUser" status="i">
                      <g:if test="${i != 0}">, </g:if>${chatroomUser}
                    </g:each>
                  </g:if>
                  <g:else>
                    Just you
                  </g:else>
                </td>
                <td>
                  <g:if test="${chatroom.files.size() > 0}">
                    <g:each in="${chatroom.files.sort { it.filename }}" var="file" status="i">
                      <g:if test="${i != 0}">, </g:if><a href="${file.downloadUrl}">${file.originalFilename}</a>
                    </g:each>
                  </g:if>
                  <g:else>
                    None
                  </g:else>
                </td>
                <td class="options">
                  <a id="copy-url-${chatroom.uniqueId}" class="copy-url tooltip-link" data-clipboard-text="${chatroom.url}" title="Copy URL" data-container="body"><i class="fa fa-clipboard"></i></a>&nbsp;
                  <script>
                    var client_${chatroom.uniqueId} = new ZeroClipboard(document.getElementById("copy-url-${chatroom.uniqueId}"));
                    var timer_${chatroom.uniqueId};
                    client_${chatroom.uniqueId}.on('ready', function(readyEvent) {
                      client_${chatroom.uniqueId}.on('aftercopy', function(event) {
                        clearTimeout(timer_${chatroom.uniqueId});
                        $("#copy-url-${chatroom.uniqueId}").html('<i class="fa fa-check"></i>');
                        timer_${chatroom.uniqueId} = setTimeout(function() { $("#copy-url-${chatroom.uniqueId}").html('<i class="fa fa-clipboard"></i>'); }, 1000);
                      });
                    });
                  </script>
                  <g:link controller="chat" action="exportLog" params="[uniqueId: chatroom.uniqueId]" title="Export chat log" class="tooltip-link" data-container="body"><i class="fa fa-download"></i></g:link>&nbsp;
                  <g:if test="${(chatroom.users - user).size() == 0}">
                    <a class="delete-chat tooltip-link" data-id="${chatroom.uniqueId}" title="Delete chatroom" data-container="body"><i class="fa fa-trash-o"></i></a>
                  </g:if>
                  <g:else>
                    <a class="disabled tooltip-link" title="Cannot delete this chatroom" data-container="body"><i class="fa fa-trash-o"></i></a>
                  </g:else>
                </td>
              </tr>
            </g:each>
            </tbody>
          </table>
        </g:if>
        <g:else>
          <p class="info">You have not yet attended any chatrooms. <g:link controller="home" action="index">Get started now</g:link>.</p>
        </g:else>
      </div>
      <div class="profile-section">
        <h4>Notifications</h4>
        <g:if test="${user.notifications?.size() > 0}">
          <table class="table table-hover table-condensed">
            <thead>
              <tr>
                <th>Message</th>
                <th>URL</th>
                <th>Received At</th>
              </tr>
            </thead>
            <tbody>
              <g:each in="${user.notifications?.sort { it.dateCreated }}" var="notification">
                <tr>
                  <td>${notification.message}</td>
                  <td>
                    <g:if test="${notification.url}">
                      <a href="${notification.url}">${notification.url}</a>
                    </g:if>
                    <g:else>
                      None
                    </g:else>
                  </td>
                  <td>${notification.formattedDateCreated}</td>
                </tr>
              </g:each>
            </tbody>
          </table>
        </g:if>
        <g:else>
          <p class="info">You have not yet received any notifications.</p>
        </g:else>
      </div>
      <div class="profile-section">
        <h4>User Information</h4>
        <div class="row option">
          <div class="col-md-3">
            <h5 class="pull-right">Update Password</h5>
            <p class="info pull-right" style="text-align: right">Use this section to change your password.</p>
            <p class="info pull-right" style="text-align: right">
              <a id="show-passwords" data-status="hidden">Show Passwords</a>
            </p>
          </div>
          <div class="col-md-9">
            <div class="option-section">
              <form role="form">
                <div class="form-group">
                  <label for="password">Current Password</label>
                  <input type="password" id="password" class="form-control" placeholder="Current Password" required>
                </div>
                <div class="row">
                  <div class="col-md-6">
                    <div class="form-group">
                      <label for="new-password">New Password</label>
                      <input type="password" id="new-password" class="form-control" placeholder="New Password" required>
                    </div>
                  </div>
                  <div class="col-md-6">
                    <div class="form-group">
                      <label for="password-confirmation">Confirm Password</label>
                      <input type="password" id="password-confirmation" class="form-control" placeholder="Current Password" required>
                    </div>
                  </div>
                </div>
                <div class="form-group">
                  <button type="button" id="change-password" class="btn btn-primary" data-loading-text="Updating Password...">Update Password</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
</body>
</html>