var Home = (function($) {
    'use strict';

    var self = {};

    var _createChatroom = function() {
        var url = $("#chatroom-url").val();
        var emails = $("#chatroom-emails").val();
        var data = { url: url, emails: emails };

        $.ajax({
            type: "POST",
            data: data,
            url: "/" + config.application.name + "/chat/create",
            success: function(response) {
                window.location.href = response.data.url;
            },
            error: function(data) {
                var error = $("#error-message");
                error.html('<small>' + data.responseJSON.message + '</small>');
                error.removeClass("hidden");
                $("#create-chatroom-button").button('reset');
            }
        });
    };

    self.init = function() {
        $("#create-chatroom-button").on('click', function() {
            $(this).button('loading');
            _createChatroom();
        });

        var copyButton = $("#copy-button");
        var client = new ZeroClipboard(copyButton);
        var timer;

        client.on("ready", function(readyEvent) {
            client.on("aftercopy", function(event) {
                clearTimeout(timer);
                copyButton.html('Copy&nbsp;&nbsp;<i class="fa fa-cog fa-spin"></i>');
                timer = setTimeout(function() { copyButton.html('Copy&nbsp;&nbsp;<i class="fa fa-check"></i>'); }, 500);
            });
        });
    };

    return self;

}(jQuery));