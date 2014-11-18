var Room = (function($) {

    var self = {};

    _uniqueId = null;

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

    self.init = function(uniqueId) {
        _uniqueId = uniqueId;

        var socket = new SockJS("/" + config.application.name + "/stomp");
        var client = Stomp.over(socket);

        var chatLog = $("#chat-log");
        var chatText = $("#chat-text");
        var chatRoom = $("#chatroom");
        var chatWorkspace = $("#chat-workspace-" + _uniqueId);
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
            client.send("/app/chatMessage", {}, JSON.stringify(username.val() + " has entered the chatroom.|" + _uniqueId ));
            _connectVideoAndAudio();
        });

        chatText.keypress(function(event) {
            if (event.keyCode == 13) {
                event.preventDefault();
                if ($.trim(chatText.val()) !== "") {
                    client.send("/app/chatMessage", {}, JSON.stringify(username.val() + ": " + chatText.val() + "|" + _uniqueId));
                    chatText.val("");
                }
            }
        });

        $("#exit-chatroom").click(function() {
            if (confirm("Are you sure you'd like to exit the chatroom?")) {
                client.send("/app/chatMessage", {}, JSON.stringify(username.val() + " has left the chatroom.|" + _uniqueId));
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
                chatWorkspace.animate({ width: chatWorkspace.width() - 252}, 200);
                return;
            }
            chatLog.animate({ right: -300 }, 200);
            chatText.animate({ right: -300 }, 200);
            chatWorkspace.animate({ width: chatWorkspace.width() + 300}, 200);
        });

        $("#invite-users").click(function() {
            $("#inviteUsersModal").modal();
        });

        $("#invite-users-button").click(function() {
            $(this).button('loading');
            $.ajax({
                type: "POST",
                data: {
                    uniqueId: _uniqueId,
                    emails: $("#chatroom-emails").val()
                },
                url: "/" + config.application.name + "/chat/invite",
                success: function() {
                    client.send("/app/chatMessage", {}, JSON.stringify(username.val() + " invited the following users to the chatroom: " + $("#chatroom-emails").val() + "|" + _uniqueId));
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

        $("#export-workspace").click(function() {
            $("#workspace-form").submit();
        });

        mobwrite.share('chat-workspace-' + _uniqueId);
    };

    return self;

}(jQuery));