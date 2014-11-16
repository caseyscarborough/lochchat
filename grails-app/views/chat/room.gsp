<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Home</title>
  <meta name="layout" content="main">
  <asset:javascript src="spring-websocket" />
  <script>
    $(function() {
      var username = $("#username");
      var modal = $("#usernameModal");
      var enterRoom = $("#enter-room-button");
      modal.modal();

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
      });


      var socket = new SockJS("${createLink(uri: '/stomp')}");
      var client = Stomp.over(socket);

      var chatLog = $("#chat-log");
      var chatText = $("#chat-text");
      var chatRoom = $("#chatroom");

      client.connect({}, function() {
        client.subscribe("/topic/chatMessage", function(message) {
          chatLog.append("<div class='chat-text'>" + JSON.parse(message.body) + '</div>');
        });
      });

      chatText.keypress(function(event) {
        if (event.keyCode == 13) {
          event.preventDefault();
          if ($.trim(chatText.val()) !== "") {
            client.send("/app/chatMessage", {}, JSON.stringify(username.val() + ": " + chatText.val()));
            chatText.val("");
          }
        }
      });

      chatLog.height(chatRoom.height() - 90);
      $(window).resize(function() {
        chatLog.height(chatRoom.height() - 90);
      });

    });
  </script>
</head>

<body>
<div id="chatroom">
  <div id="chat-log">
    <div id="chat-log-text"></div>
  </div>
  <textarea id="chat-text" placeholder="Type to chat..."></textarea>
</div>
<g:render template="usernameModal" />
</body>
</html>