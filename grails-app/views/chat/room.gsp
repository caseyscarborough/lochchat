<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
  <title>Home</title>
  <meta name="layout" content="main">
  <asset:javascript src="spring-websocket" />
  <script>
    $(function() {
      var socket = new SockJS("${createLink(uri: '/stomp')}");
      var client = Stomp.over(socket);

      client.connect({}, function() {
        client.subscribe("/topic/hello", function(message) {
          $("#chat-log-text").append(JSON.parse(message.body) + '<br>');
        });
      });

      $("#chat-text").keyup(function(event) {
        if (event.keyCode == 13) {
          var text = $("#chat-text");
          client.send("/app/hello", {}, JSON.stringify($("#name").val() + ": " + text.val()));
          text.val("");
        }
      });

    });
  </script>
</head>

<body>
<div id="chatroom">
  <input id="name" placeholder="Enter your name in this box">
  <div id="chat-log">
    <div id="chat-log-text"></div>
  </div>
  <textarea id="chat-text" placeholder="Type to chat..."></textarea>
</div>
</body>
</html>