var Room = (function($) {
    'use strict';

    var self = {},
        _uniqueId = null,
        _chatVideo = null,
        _chatWorkspace = null,
        _chatLog = null,
        _chatRoom = null,
        _chatText = null,
        _username = null;

    // WebSocket connection for chatroom
    var _socket = null;

    var _connectVideoAndAudio = function() {
        var connection = new RTCMultiConnection();

        connection.session = {
            audio: true,
            video: true
        };

        connection.onstream = function(e) {
            _chatVideo.show();
            _chatVideo.append(e.mediaElement);
        };

        connection.connect();

        document.querySelector('#enable-video').onclick = function() {
            connection.open();
        };
    };

    var _wrapMessage = function(message, clazz) {
        return "<div class='chat-text " + clazz + "'>" + message + '</div>';
    };

    var _setupIncomingChats = function() {
        _socket = new WebSocket("ws://localhost:8080/lochchat/chatMessage");

        _socket.onopen = function(message) {
            _chatLog.append(_wrapMessage("Connected to server..."));
        };

        _socket.onmessage = function(message) {
            var new_chatLog = $(_wrapMessage(JSON.parse(message.data).message));
            new_chatLog.linkify();
            _chatLog.append(new_chatLog);
            _chatLog.animate({ scrollTop: _chatLog.prop("scrollHeight") - _chatLog.height() }, 200);
        };

        _socket.onclose = function(message) {
            processClose(message);
            _socket.send("Client disconnected......\n");
            _chatLog.append(_wrapMessage("Server Disconnected..."));
        };

        _socket.onerror = function(message) {
            _chatLog.append(_wrapMessage("An error occurred."));
        };

        _chatText.keypress(function(event) {
            if (event.keyCode == 13) {
                event.preventDefault();
                if ($.trim(_chatText.val()) !== "") {
                    _socket.send(_chatText.val() + "|" + _uniqueId);
                    _chatText.val("");
                }
            }
        });
    };

    var _setupCopyUrl = function() {
        var copyButton = $("#chat-copy-url");
        var zc = new ZeroClipboard(copyButton);

        zc.on("ready", function() {
            zc.on("aftercopy", function() { alert("URL copied to clipboard!"); });
        });
    };

    var _setupUserInvitations = function() {
        var emails = $("#chatroom-emails");
        $("#invite-users").click(function() {
            $("#inviteUsersModal").modal();
        });

        $("#invite-users-button").click(function() {
            $(this).button('loading');
            $.ajax({
                type: "POST",
                data: {
                    uniqueId: _uniqueId,
                    emails: emails.val()
                },
                url: "/" + config.application.name + "/chat/invite",
                success: function() {
                    _socket.send(_username + " invited the following users to the chatroom: " + emails.val() + "|" + _uniqueId);
                    emails.val("");
                    $("#inviteUsersModal").modal('hide');
                    $(this).button('reset');
                },
                error: function(data) {
                    alert(data.responseJSON.message);
                    $(this).button('reset');
                }
            });
        });
    };

    var _setupToggleChat = function() {
        $("#toggle-chat").click(function() {
            if (_chatLog.css("right") === "-300px") {
                _chatLog.animate({ right: 0 }, 200);
                _chatText.animate({ right: 0 }, 200);
                _chatWorkspace.animate({ width: _chatWorkspace.width() - 252}, 200);
                return;
            }
            _chatLog.animate({ right: -300 }, 200);
            _chatText.animate({ right: -300 }, 200);
            _chatWorkspace.animate({ width: _chatWorkspace.width() + 300}, 200);
        });
    };

    var _setupExitChatroom = function() {
        $("#exit-chatroom").click(function() {
            if (confirm("Are you sure you'd like to exit the chatroom?")) {
                window.location.href = "/" + config.application.name;
            }
        });
    };

    self.init = function(uniqueId) {
        _uniqueId = uniqueId;
        _chatLog = $("#chat-log");
        _chatText = $("#chat-text");
        _chatRoom = $("#chatroom");
        _chatWorkspace = $("#chat-workspace-" + _uniqueId);
        _chatVideo = $("#chat-video");

        _setupIncomingChats();
        _setupCopyUrl();
        _setupUserInvitations();
        _setupToggleChat();
        _setupExitChatroom();

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
            _username = username.val();
            _socket.send(_username + "|" + _uniqueId );
            _connectVideoAndAudio();
        });

        _chatLog.height(_chatRoom.height() - 70);
        _chatWorkspace.css({ height: _chatRoom.height() - 110, width: _chatRoom.width() - 300 });

        $(window).resize(function() {
            _chatLog.height(_chatRoom.height() - 70);
            if (_chatVideo.is(":visible")) {
                _chatWorkspace.css({ height: _chatRoom.height() - 110, width: _chatRoom.width() - 500, "margin-left": 200 });
            } else {
                _chatWorkspace.css({ height: _chatRoom.height() - 110, width: _chatRoom.width() - 300 });
            }
        });

        if (_chatWorkspace.is(":visible")) {
            _chatVideo.css({ position: "fixed", left: 0, width: 200 });
        }

        $(window).on('load', function() { _chatLog.linkify(); });
        $("#export-workspace").click(function() { $("#workspace-form").submit(); });
    };

    return self;

}(jQuery));