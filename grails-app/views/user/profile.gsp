<html>
<head>
  <meta name="layout" content="main">
  <title>Profile</title>
</head>

<body>
<div class="container">
  <div class="row">
    <div class="col-md-12 profile">
      <g:if test='${flash.message}'>
        <div class='alert alert-info'><small>${flash.message}</small></div>
      </g:if>

      <div class="row">
        <div class="col-md-2">
          <lochchat:gravatarFor user="${user}" width="100%" class="" />
          <h4>${user}</h4>
          <p class="info"><a href="mailto:${user.email}">${user.email}</a></p>
          <p class="info">Joined on ${user.dateCreated.format("MMM. dd, yyyy")}</p>
        </div>
        <div class="col-md-10">
          <div class="profile-section">
            <h4>Chatrooms</h4>
            <g:if test="${user.chats.size() > 0}">
              <p class="info">The following is a list of chatrooms that you have attended:</p>
              <table class="table table-condensed table-hover">
                <thead>
                <tr>
                  <th>Chatroom ID</th>
                  <th>Start Time</th>
                  <th>Log</th>
                  <th>URL</th>
                  <th>Other Users</th>
                </tr>
                </thead>
                <tbody>
                <g:each in="${user.chats.sort { it.startTime }}" var="chatroom">
                  <tr>
                    <td>${chatroom.uniqueId}</td>
                    <td>${chatroom.formattedStartTime}</td>
                    <td><g:link controller="chat" action="exportLog" params="[uniqueId: chatroom.uniqueId]" target="_blank">Export Chatlog</g:link></td>
                    <td><a href="${chatroom.url}" target="_blank">${chatroom.url}</a></td>
                    <td><g:each in="${chatroom.users - user}" var="chatroomUser" status="i">
                      <g:if test="${i != 0}">, </g:if>${chatroomUser}
                    </g:each></td>
                  </tr>
                </g:each>
                </tbody>
              </table>
            </g:if>
            <g:else>
              <p class="info">You have not yet attended any chatrooms. <g:link controller="home" action="index">Get started now</g:link>.</p>
            </g:else>
          </div>
        </div>
      </div>

    </div>
  </div>
</div>
</body>
</html>