var Room = (function($) {
    'use strict';

    var self = {},
        _uniqueId = null,
        _chatVideo = null,
        _chatWorkspace = null,
        _chatLog = null,
        _chatRoom = null,
        _chatText = null,
        _username = null,
        _modal = null,
        _connectionRetries = 0;

    var debugMode = false;

    // WebSocket connection for chatroom
    var _socket = null;

    var _resetUploadButton = function() {
        var btn = $("#upload-file");
        btn.html('Upload File');
        btn.removeAttr('disabled');
        $('#file').val('');
    };

    var _scrollChatLog = function() {
        _chatLog.animate({ scrollTop: _chatLog.prop("scrollHeight") - _chatLog.height() + 20 }, 200);
    };

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

    var _setupIncomingChats = function(websocketUrl, username) {
        websocketUrl = websocketUrl.replace(/http:\/\/(.*):443/, "wss://$1");
        websocketUrl = websocketUrl.replace(/http/, "ws");
        _socket = new WebSocket(websocketUrl);

        _socket.onopen = function(message) {
            _chatLog.append(_wrapMessage("Connected to server..."));
            _connectionRetries = 0;
            _scrollChatLog();
        };

        _socket.onmessage = function(message) {
            try {
                var data = JSON.parse(message.data);
                if (data.message) {
                    var new_chatLog = $(_wrapMessage(data.message));
                    new_chatLog.linkify();
                    _chatLog.append(new_chatLog);
                    _scrollChatLog();
                }

                if (data.callback) {
                    eval(data.callback);
                }
            } catch (SyntaxError) {}
        };

        _socket.onclose = function(message) {
            _chatLog.append(_wrapMessage("Lost connection. Reconnecting..."));
            _scrollChatLog();
            _resetUploadButton();
            if (_connectionRetries < 5) {
                _connectionRetries++;
                setTimeout(function() { _setupIncomingChats(websocketUrl, _username); }, 2000);
            } else {
                _chatLog.append(_wrapMessage("There is a problem connecting to the server. Please try again later."));
            }
        };

        _socket.onerror = function(message) {
            // Not necessary, handled in socket.onclose
        };

        _chatText.keypress(function(event) {
            if (event.keyCode == 13) {
                event.preventDefault();
                if ($.trim(_chatText.val()) !== "") {
                    _socket.send(_chatText.val());
                    _chatText.val("");
                }
            }
        });

        if (username) {
            setTimeout(function() { _socket.send(username); }, 500);
        }
    };

    var _setupCopyUrl = function() {
        var copyButton = $("#chat-copy-url");
        var zc = new ZeroClipboard(copyButton);

        zc.on("ready", function() {
            zc.on("aftercopy", function() {
                swal("URL Copied!", "The chatroom URL has been copied to the clipboard.", "success");
            });
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
                    _socket.send(_username + " invited the following users to the chatroom: " + emails.val());
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
            swal({
                title: "Are you sure?",
                text: "Are you sure you'd like to exit the chatroom?",
                type: "error",
                showCancelButton: true,
                confirmButtonClass: "btn-danger",
                confirmButtonText: "Yes, get me out of here!",
                closeOnConfirm: false
            },
            function(){
                window.location.href = "/" + config.application.name;
            });
        });
    };

    var _setupFileUpload = function() {
        $("#upload-file").click(function() {
            var file = document.getElementById('file').files[0];

            if (typeof file == 'undefined') {
                swal('Please choose a file', 'You must choose a file to upload.', 'error');
                return;
            }

            _socket.send("file:" + file.name);

            var reader = new FileReader();
            var data = new ArrayBuffer();

            reader.loadend = function() {
            };

            reader.onload = function(e) {
                data = e.target.result;
                $("#upload-file").html('<i class="fa fa-refresh fa-spin"></i>').attr('disabled', 'disabled');
                _socket.send(data);
                _socket.send("endFile:" + file.name);
            };

            reader.readAsArrayBuffer(file);
        });
    };

    var _enterRoom = function(username) {
        if (_modal.hasClass('in')) {
            _modal.modal('hide');
        }
        _username = username;
        _socket.send(_username);
        _connectVideoAndAudio();
    };

    self.init = function(uniqueId, websocketUrl, loggedInUsername) {
        _uniqueId = uniqueId;
        _chatLog = $("#chat-log");
        _chatText = $("#chat-text");
        _chatRoom = $("#chatroom");
        _chatWorkspace = $("#chat-workspace-" + _uniqueId);
        _chatVideo = $("#chat-video");

        _setupIncomingChats(websocketUrl);
        _setupCopyUrl();
        _setupUserInvitations();
        _setupToggleChat();
        _setupExitChatroom();
        _setupFileUpload();

        var username = $("#username");
        var enterRoom = $("#enter-room-button");

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

        _modal = $("#usernameModal");

        if (debugMode) {
            setTimeout(function() { _enterRoom("Debug User"); }, 500);
        }

        if (loggedInUsername !== "") {
            setTimeout(function() { _enterRoom(loggedInUsername); }, 500);
        } else {
            _modal.modal();

            if ($.trim(username.val()) !== "") {
                enterRoom.removeAttr("disabled");
            }

            username.keyup(function(event) {
                if ($.trim(username.val()) !== "") {
                    enterRoom.removeAttr("disabled");
                } else {
                    enterRoom.attr("disabled", "disabled");
                }

                if (event.keyCode == 13) {
                    enterRoom.trigger('click');
                }
            });

            enterRoom.click(function() {
                if ($.trim(username.val()) === "") {
                    username.val("");
                    username.focus();
                    return false;
                }

                _enterRoom(username.val());
            });
        }
    };

    return self;

}(jQuery));