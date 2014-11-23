var Profile = (function($) {

    var self = {};

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
    };

    return self;

}(jQuery));