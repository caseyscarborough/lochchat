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
          $("#chat").append(JSON.parse(message.body) + '<br>');
        });
      });

      $("#submit").click(function() {
        var text = $("#text");
        client.send("/app/hello", {}, JSON.stringify($("#name").val() + ": " + text.val()));
        text.val("");
      });
    });
  </script>
</head>

<body>
<input id="name" placeholder="Enter your name in this box">
<div id="chat"></div>
<textarea id="text"></textarea>
<button id="submit">hello</button>
</body>
</html>