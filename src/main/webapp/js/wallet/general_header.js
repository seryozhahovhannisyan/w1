/* 
 * General header part common for all pages.
 */
$(document).ready(function () {
    (function () {
        var mainContainer = $('#gen-head');
        init();

        function setOnline() {
            $('#online-status .icon-area')
                .removeClass('g02').addClass('g01')
        }

        function setOffline() {
            $('#online-status .icon-area')
                .removeClass('g01').addClass('g02')
        }

        function init() {
            setEventListener();
        }

        function setEventListener() {
            $(document).keydown(function (e) {
                switch (e.keyCode) {
                    case 13:
                        // confirm
                        break;
                }
            });
        }
    }());
});

