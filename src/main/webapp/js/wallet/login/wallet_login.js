/* 
 *  Wallet login page front-end functionality: (Uses Jquery librery).
 */
$(document).ready(function () {
    (function () {
        var mainContainer = $('body'),
            focusableElementsCount = $('[data-index]').length;
        init();

        function setThem(them) {
            $('html').addClass(them);
        }

        function setFocusableItem() {
            $('[data-index="1"]').focus();
        }

        function setHeight() {
            var w = window.innerHeight;
            mainContainer.css('height', w);
        }

        function prevField() {
            var i = getFocusedItemIndex();
            // First index in circle is 1, 
            // last is equel to focusable elements count (focusableElementsCount).
            // If i = 0, then the focus is out of order.
            if (i === 0 || i === 1) {
                // Do nothing.
                ;
            } else if (i > 1 && i <= focusableElementsCount) {
                $('[data-index="' + (i - 1) + '"]').focus();
            }
        }

        function nextField() {
            var i = getFocusedItemIndex();
            // First index in circle is 1, 
            // last is equel to focusable elements count (focusableElementsCount).
            // If i = 0, then the focus is out of order.
            if (i === 0 || i === focusableElementsCount) {
                // Do nothing.
                ;
            } else if (i > 0 && i < focusableElementsCount) {
                $('[data-index="' + (i + 1) + '"]').focus();
            }
        }

        function getFocusedItemIndex() {
            var i = $(':focus').attr('data-index');
            if (i !== undefined) {
                return parseInt(i);
            } else {
                return 0;
            }
        }

        function init() {
            setHeight();
            setThem('cm-them01');
            setEventListener();
            setFocusableItem();
        }

        function do_action() {
            var item = $(':focus');
            if (item.attr('id') === 'wallet_login_submit') {
                var form = document.getElementById('wallet_login');
                form.submit();
            }
        }

        function setEventListener() {
            $(window).resize(function () {
                setHeight();
            });
            $(document).keydown(function (e) {
                switch (e.keyCode) {
                    case 13:
                        // confirm
                        do_action();
                        break;
                    case 37:
                        prevField();
                        break;
                    case 38:
                        prevField();
                        break;
                    case 39:
                        nextField();
                        break;
                    case 40:
                        nextField();
                        break;
                }
            });
        }
    }());
});