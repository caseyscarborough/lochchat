var Home = (function ($) {
    'use strict';

    var self = {};

    var _createChatroom = function () {
        var url = $("#chatroom-url").val(),
            invitees = $("#chatroom-invitees").val(),
            data = {url: url, invitees: invitees};

        $.ajax({
            type: "POST",
            data: data,
            url: "/" + config.application.name + "/chat/create",
            success: function (response) {
                window.location.href = response.data.url;
            },
            error: function (data) {
                var error = $("#error-message");
                error.html('<small>' + data.responseJSON.message + '</small>');
                error.removeClass("hidden");
                $("#create-chatroom-button").button('reset');
            }
        });
    };

    self.init = function () {
        $("#create-chatroom-button").on('click', function () {
            $(this).button('loading');
            _createChatroom();
        });

        var copyButton = $("#copy-button"),
            client = new ZeroClipboard(copyButton),
            timer;

        client.on("ready", function () {
            client.on("aftercopy", function () {
                clearTimeout(timer);
                copyButton.html('Copy&nbsp;&nbsp;<i class="fa fa-cog fa-spin"></i>');
                timer = setTimeout(function () {
                    copyButton.html('Copy&nbsp;&nbsp;<i class="fa fa-check"></i>');
                }, 500);
            });
        });
    };

    return self;

}(jQuery));