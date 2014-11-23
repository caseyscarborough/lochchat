var Profile = (function($) {
    'use strict';

    var self = {};

    var _showHidePasswords = function(type) {
        $("#password").attr("type", type);
        $("#password-confirmation").attr("type", type);
        $("#new-password").attr("type", type);
    };

    var _updatePassword = function() {
        var password = $("#password");
        var passwordConf = $("#password-confirmation");
        var newPassword = $("#new-password");

        if ($.trim(password.val()) !== '' && ($.trim(newPassword.val()) == $.trim(passwordConf.val()))) {
            var data = { currentPassword: password.val(), newPassword: newPassword.val() };
            var btn = $("#update-password");
            btn.button('loading');
            $.ajax({
                type: "put",
                contentType: "application/json",
                dataType: "json",
                url: "/" + config.application.name + "/user/updatePassword",
                data: JSON.stringify(data),
                success: function() {
                    btn.button('reset');
                    swal("Success!", "Successfully updated password.", "success");
                    password.val("");
                    passwordConf.val("");
                    newPassword.val("");
                },
                error: function(response) {
                    swal({
                        title: "An error occurred",
                        text: response.responseJSON.message,
                        type: "error"
                    },
                    function() {
                        setTimeout(function() { password.focus(); }, 200);
                    });
                }
            });
        } else {
            swal({
                title: "Passwords do not match",
                text: "The passwords that you entered do not match.",
                type: "error"
            },
            function() {
                setTimeout(function() { newPassword.focus(); }, 200);
            });
        }
    };

    var _deleteChatroom = function(uniqueId) {
        $.ajax({
            type: "delete",
            url: "/" + config.application.name + "/chat/delete/" + uniqueId,
            success: function() {
                $("#chatroom-" + uniqueId).fadeOut(400);
            }
        });
    };

    var _enterNewChatroom = function() {
        var data = { url: $("#new-url").val() };
        $.ajax({
            type: "POST",
            data: data,
            url: "/" + config.application.name + "/chat/create",
            success: function(response) {
                window.location.href = response.data.url;
            },
            error: function() {
                // If user came from back button, chat url will already be taken, this will grab a new url.
                $.get("/" + config.application.name + "/chat/generateChatroomUrl", function(data) {
                    $("#new-url").val(data);
                    _enterNewChatroom();
                });
            }
        });
    };

    self.init = function() {
        $("#enter-new-chatroom").click(function() {
            _enterNewChatroom();
        });

        $(".delete-chat").click(function() {
            var uniqueId = $(this).attr("data-id");
            swal({
                title: "Are you sure?",
                text: "You are able to delete this chatroom because you are the only person that joined it. This action is irreversible.",
                type: "warning",
                showCancelButton: true,
                confirmButtonClass: "btn-danger",
                confirmButtonText: "Yes, delete it!"
            },
            function() {
                _deleteChatroom(uniqueId);
            });
        });

        $("#change-password").click(function() {
            _updatePassword();
        });

        $("#show-passwords").click(function() {
            var status = $(this).attr("data-status");
            if (status == "hidden") {
                _showHidePasswords("text");
                $(this).attr("data-status", "shown");
                $(this).html("Hide Passwords");
            } else {
                _showHidePasswords("password");
                $(this).attr("data-status", "hidden");
                $(this).html("Show Passwords");
            }
        });
    };

    return self;

}(jQuery));