// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better 
// to create separate JavaScript files as needed.
//
//= require jquery
//= require bootstrap/dist/js/bootstrap
//= require flat-ui/dist/js/flat-ui
//= require zeroclipboard/dist/ZeroClipboard
//= require jQuery-linkify/dist/jquery.linkify.min
//= require bootstrap-sweetalert/lib/sweet-alert
//= require_tree ./app
//= require_self

function bindNotificationClick() {
    $('.notification').on('click', function () {
        var url = $(this).attr("data-url"),
            id = $(this).attr("data-id");
        $.ajax({
            type: "put",
            data: JSON.stringify({ id: id }),
            dataType: "json",
            contentType: "application/json",
            url: "/" + config.application.name + "/notification/dismiss",
            success: function() {
                var notificationCount = $("#notification-count");
                notificationCount.val(parseInt(notificationCount.val()) - 1);
                if (notificationCount.val() === "0") {
                    $("#no-notifications").show();
                }

                if (url) {
                    window.location.href = url;
                }
            }
        });
    });
}

function subscribeToNotificationEndpoint(websocketUrl) {
    var socket = new WebSocket(websocketUrl);

    socket.onopen = function () {
        console.log("Connected to notification endpoint successfully...");
    };

    socket.onmessage = function (message) {
        var data = JSON.parse(message.data);
        $("#no-notifications").hide();
        $(".navbar-unread").fadeIn(150).fadeOut(150).fadeIn(150).fadeOut(150).fadeIn(150);
        $("#notifications-dropdown").append(
            '<div class="notification" data-url="' + data.notification.url + '" data-id="' + data.notification.id + '" data-viewed="' + data.notification.isViewed + '">' +
            '<div class="notification-message">' + data.notification.message + '</div>' +
            '</div>'
        );
        bindNotificationClick();
    };

    socket.onclose = function () {
        setTimeout(function() { subscribeToNotificationEndpoint(websocketUrl) }, 4000);
    };

    socket.onerror = function () {
        // Not necessary, handled in socket.onclose
    };
}

if (typeof jQuery !== 'undefined') {
    (function($) {
        'use strict';
        $('#spinner').ajaxStart(function() {
            $(this).fadeIn();
        }).ajaxStop(function() {
            $(this).fadeOut();
        });
    })(jQuery);

    $(function() {
        'use strict';
        ZeroClipboard.config({ swfPath: "/" + config.application.name + "/assets/zeroclipboard/dist/ZeroClipboard.swf" });

        $('.bootstrap-tagsinput input').focus(function() {
            $(this).parent().css("border-color", "#e67e22");
        }).blur(function() {
            $(this).parent().css("border-color", "#ebedef");
        });

        $(".tooltip-link").tooltip();
        $('.popover-link').popover({ trigger: 'hover' });

        bindNotificationClick();

        $("#notifications-link").click(function () {
            var notifications = "";
            $(".navbar-unread").hide();
            $('.notification').each(function (index) {
                if (index !== 0 && $(this).attr("data-viewed") === "false") {
                    if (index !== 1) {
                        notifications += ",";
                    }
                    notifications += $(this).attr("data-id");
                    $(this).attr("data-viewed", "true");
                }
            });

            if ($.trim(notifications) !== "") {
                $.ajax({
                    type: "put",
                    data: JSON.stringify({ notifications: notifications }),
                    dataType: "json",
                    contentType: "application/json",
                    url: "/" + config.application.name + "/notification/view",
                    success: function () {
                        console.log("Marked notifications " + notifications + " as viewed");
                    },
                    error: function (response) {
                        console.error("An error occurred marking notifications as viewed: " + response.responseJSON.message);
                    }
                });
            }
        });
    });
}
