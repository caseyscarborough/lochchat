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