var Chat = (function($) {

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
    };

    return self;

}(jQuery));