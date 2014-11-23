<%@ page contentType="text/html;charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/html">
<head>
  <title>Chatroom</title>
  <meta name="layout" content="main">
  <script src="//cdn.webrtc-experiment.com/RTCMultiConnection.js"></script>
  <script src="//mobwrite3.appspot.com/static/compressed_form.js"></script>
  <script>
    $(function(){
      Room.init("${chatroom.uniqueId}", "${createLink(uri: "/chatEndpoint/${chatroom.uniqueId}", absolute: true)}", "<sec:username/>");
      mobwrite.syncGateway = 'https://mobwrite3.appspot.com/scripts/q.py';
      mobwrite.share('chat-workspace-${chatroom.uniqueId}');
    });
  </script>
</head>

<body>
<div id="chatroom">
  <div>
  </div>
  <div id="chat-video"></div>
  <form id="workspace-form" action="${createLink(controller: 'chat', action: 'exportWorkspace')}" method="POST" target="_blank">
    <input type="hidden" name="chatroom" value="${chatroom.uniqueId}">
    <textarea class="chat-workspace" name="workspace" id="chat-workspace-${chatroom.uniqueId}" placeholder="Collaborate here..."></textarea>
  </form>
  <div id="chat-log"><lochchat:logHtml logInstance="${chatroom.log}" /></div>
  <textarea id="chat-text" placeholder="Type to chat..."></textarea>
  <div id="chat-options">
    <div class="chat-option">
      <asset:image id="chat-copy-url" data-clipboard-text="${chatroom.url}" src="flat-icons/Icons/set-2/PNG/4.png" />
      <label>Copy URL</label>
    </div>
    <div class="chat-option">
      <asset:image id="toggle-chat" src="flat-icons/Icons/set-2/PNG/11.png" />
      <label>Toggle Chat</label>
    </div>
    <div class="chat-option">
      <asset:image id="enable-video" src="flat-icons/Icons/set-2/PNG/10.png" />
      <label>Enable Video</label>
    </div>
    <div class="chat-option">
      <asset:image id="invite-users" src="flat-icons/Icons/set-2/PNG/2.png" />
      <label>Invite Users</label>
    </div>
    <div class="chat-option">
      <g:link target="_blank" controller="chat" action="exportLog" params="[uniqueId: chatroom.uniqueId]"><asset:image id="export-log" src="flat-icons/Icons/set-3/PNG/3.png" /></g:link>
      <label>Export Chat Log</label>
    </div>
    <div class="chat-option">
      <asset:image id="export-workspace" src="flat-ui/dist/img/icons/svg/pencils.svg" />
      <label>Export Workspace</label>
    </div>
    <div class="chat-option">
      <asset:image id="exit-chatroom" src="flat-icons/Icons/set-2/PNG/3.png" />
      <label>Exit Chatroom</label>
    </div>
    <div class="chat-option">
      <input type="file" id="file">
      <label><button id="upload-file" class="btn btn-primary">Upload File</button></label>
    </div>
  </div>
</div>
<g:render template="usernameModal" />
<g:render template="inviteUsersModal" />
</body>
</html>