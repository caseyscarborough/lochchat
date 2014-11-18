<%@ page contentType="text/html;charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/html">
<head>
  <title>Chatroom</title>
  <meta name="layout" content="main">
  <asset:javascript src="spring-websocket" />
  <script src="//cdn.webrtc-experiment.com/RTCMultiConnection.js"></script>
  <script src="//mobwrite3.appspot.com/static/compressed_form.js"></script>
  <script>
    mobwrite.syncGateway = 'https://mobwrite3.appspot.com/scripts/q.py';
  </script>
  <script>
    var _connectVideoAndAudio = function() {
      var connection = new RTCMultiConnection();

      connection.session = {
        audio: true,
        video: true
      };

      connection.onstream = function(e) {
        $("#chat-video").show();
        $("#chat-video").append(e.mediaElement);
      };

      connection.connect();

      document.querySelector('#enable-video').onclick = function() {
        connection.open();
      };
    };

    $(function() {
      var socket = new SockJS("${createLink(uri: '/stomp')}");
      var client = Stomp.over(socket);

      var chatLog = $("#chat-log");
      var chatText = $("#chat-text");
      var chatRoom = $("#chatroom");
      var chatWorkspace = $("#chat-workspace-${chatroom.uniqueId}");
      var chatVideo = $("#chat-video");

      client.connect({}, function() {
        client.subscribe("/topic/chatMessage", function(message) {
          var newChatLog = $("<div class='chat-text'>" + JSON.parse(message.body) + '</div>');
          newChatLog.linkify();
          chatLog.append(newChatLog);
          chatLog.animate({ scrollTop: chatLog.prop("scrollHeight") - chatLog.height() }, 200);
        });
      });

      var username = $("#username");
      var modal = $("#usernameModal");
      var enterRoom = $("#enter-room-button");
      modal.modal();

      if ($.trim(username.val()) !== "") {
        enterRoom.removeAttr("disabled");
      }

      username.keyup(function() {
        if ($.trim(username.val()) !== "") {
          enterRoom.removeAttr("disabled");
        } else {
          enterRoom.attr("disabled", "disabled");
        }
      });

      enterRoom.click(function() {
        if ($.trim(username.val()) === "") {
          username.val("");
          username.focus();
          return false;
        }

        modal.modal('hide');
        client.send("/app/chatMessage", {}, JSON.stringify(username.val() + " has entered the chatroom.|${chatroom.uniqueId}" ));
        _connectVideoAndAudio();
      });

      chatText.keypress(function(event) {
        if (event.keyCode == 13) {
          event.preventDefault();
          if ($.trim(chatText.val()) !== "") {
            client.send("/app/chatMessage", {}, JSON.stringify(username.val() + ": " + chatText.val() + "|${chatroom.uniqueId}"));
            chatText.val("");
          }
        }
      });

      $("#exit-chatroom").click(function() {
        if (confirm("Are you sure you'd like to exit the chatroom?")) {
          client.send("/app/chatMessage", {}, JSON.stringify(username.val() + " has left the chatroom.|${chatroom.uniqueId}"));
          client.disconnect();
          window.location.href = "/" + config.application.name;
        }
      });

      chatLog.height(chatRoom.height() - 70);
      chatWorkspace.css({ height: chatRoom.height() - 110, width: chatRoom.width() - 300 });

      $(window).resize(function() {
        chatLog.height(chatRoom.height() - 70);
        if (chatVideo.is(":visible")) {
          chatWorkspace.css({ height: chatRoom.height() - 110, width: chatRoom.width() - 500, "margin-left": 200 });
        } else {
          chatWorkspace.css({ height: chatRoom.height() - 110, width: chatRoom.width() - 300 });
        }
      });

      if (chatWorkspace.is(":visible")) {
        chatVideo.css({ position: "fixed", left: 0, width: 200 });
      }

      chatLog.html(chatLog.html());

      $(window).on('load', function() {
        chatLog.linkify();
      });

      var copyButton = $("#chat-copy-url");
      var zc = new ZeroClipboard(copyButton);

      zc.on("ready", function(readyEvent) {
        zc.on("aftercopy", function(event) {
          alert("URL copied to clipboard!");
        });
      });

      $("#toggle-chat").click(function() {
        if (chatLog.css("right") === "-300px") {
          chatLog.animate({ right: 0 }, 200);
          chatText.animate({ right: 0 }, 200);
          return;
        }
        chatLog.animate({ right: -300 }, 200);
        chatText.animate({ right: -300 }, 200);
      });

      $("#invite-users").click(function() {
        $("#inviteUsersModal").modal();
      });

      $("#invite-users-button").click(function() {
        $(this).button('loading');
        $.ajax({
          type: "POST",
          data: {
            uniqueId: "${chatroom.uniqueId}",
            emails: $("#chatroom-emails").val()
          },
          url: "/" + config.application.name + "/chat/invite",
          success: function() {
            client.send("/app/chatMessage", {}, JSON.stringify(username.val() + " invited the following users to the chatroom: " + $("#chatroom-emails").val() + "|${chatroom.uniqueId}"));
            $("#chatroom-emails").val("");
            $("#inviteUsersModal").modal('hide');
            $(this).button('reset');
          },
          error: function(data) {
            alert(data.responseJSON.message);
            $(this).button('reset');
          }
        });
      });

      mobwrite.share('chat-workspace-${chatroom.uniqueId}');
    });
  </script>
</head>

<body>
<div id="chatroom">
  <div>
  </div>
  <div id="chat-video"></div>
  <textarea class="chat-workspace" id="chat-workspace-${chatroom.uniqueId}" placeholder="Collaborate here..."></textarea>
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
      <asset:image id="exit-chatroom" src="flat-icons/Icons/set-2/PNG/3.png" />
      <label>Exit Chatroom</label>
    </div>
  </div>
</div>
<g:render template="usernameModal" />
<g:render template="inviteUsersModal" />
</body>
</html>