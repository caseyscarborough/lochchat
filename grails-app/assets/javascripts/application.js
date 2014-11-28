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
    });
}
