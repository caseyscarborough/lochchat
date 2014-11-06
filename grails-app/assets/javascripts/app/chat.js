var Chat = (function($) {

    var self = {};

    var _createChatroom = function(errorCallback) {
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
                alert(data.responseJSON.message);
                errorCallback();
            }
        });
    };

    self.init = function() {
        $("#create-chatroom-button").on('click', function() {
            $(this).button('loading');

            _createChatroom(function() {
                $(this).button('reset');
            });
        });
    };

    return self;

}(jQuery));