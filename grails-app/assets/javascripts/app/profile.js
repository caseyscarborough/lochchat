var Profile = (function($) {

    var self = {};

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
    };

    return self;

}(jQuery));