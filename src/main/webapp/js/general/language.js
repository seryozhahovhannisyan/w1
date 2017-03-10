/*
 *  Language API
 */

var LANGUAGESETINGS = LANGUAGESETINGS || {};

LANGUAGESETINGS.language = function (key, name, icon) {
    //Private variables
    var language = {
        name: name == null ? '' : name,
        lang: key == null ? '' : key,
        icon: icon == null ? '' : icon
    };
    //Private methods
    var init = function () {
        setLanguageIconParams();
        setEventListeners();
    };
    var setLanguageIconParams = function () {
        $('#languageIcon #lang-title').attr('lang', language.lang).text(language.name);
        $('#languageIcon #lang-icon').css('background-image','url(' + language.icon + ')');
    };
    var setLangParams = function (l) {
        language.name = l.name;
        language.lang = l.lang;
        language.icon = l.icon;
        setLanguageIconParams();
    };
    var getLangParams = function (o) {
        return {
            icon: o.children('img').attr('src'),
            lang: o.children('span').attr('lang'),
            name: o.children('span').text()
        };
    };
    var getLanguage = function () {
        return language.name;
    };
    var getLanguageLang = function () {
        return language.lang;
    };
    var getLanguageIcon = function () {
        return language.icon;
    };
    var confirmSelection = function (item) {
        var l = getLangParams(item);
        setLangParams(l);
        $("#language").remove();
        //console.info("l.lang ="+l.lang); todo itarate  the form field
        $('<form>', {
            "id": "language",
            "html": '<input type="hidden"  name="lang" value="' + language.lang + '" />',
            "action": "locale.htm"
        }).appendTo(document.body).submit();


    };
    var isPortMenuListClosed = function () {
        return $("#language-change-board").hasClass('d-n');
    };
    var closePortMenuList = function () {
        $("#language-change-board").addClass('d-n');
    };
    var openPortMenuList = function () {
        $("#language-change-board").removeClass('d-n');
    };
    var getSelectedItem = function () {
        var list = $('.port-menu-list li');
        var item = list.first();
        $.each(list, function () {
            if ($(this).children('span').attr('lang') === language.lang) {
                item = $(this);
                return;
            }
        });
        return item;
    };
    var getFocusedItem = function () {
        var list = $('.port-menu-list li');
        var item = list.first();
        $.each(list, function () {
            if ($(this).is(':focus')) {
                item = $(this);
                return;
            }
        });
        return item;
    };
    var setFocusedSelectedItem = function () {
        var g = getSelectedItem();
        if (g.length !== 0) {
            g.focus();
        }
    };
    var setEventListeners = function () {
        $(document).mouseup(function (e) {
            var container = $(".port-menu-list");
            var trigger = $("#languageIcon");
            if (!container.is(e.target) && !trigger.is(e.target) &&
                container.has(e.target).length === 0 && trigger.has(e.target).length === 0) {
                closePortMenuList();
            }

        });
        $("#languageIcon").click(function () {
            if (isPortMenuListClosed()) {
                openPortMenuList();
                setFocusedSelectedItem();
            } else {
                closePortMenuList();
            }
        });
        $(".port-menu-list li").click(function () {
            confirmSelection($(this));
            closePortMenuList();
        });
        $(document).keyup(function (e) {
            if (e.keyCode === 13) {
                if ($('#languageIcon').is(':focus')) {
                    openPortMenuList();
                    setFocusedSelectedItem();
                } else if ($('.port-menu-list li').is(':focus')) {
                    confirmSelection($('.languageList:focus'));
                    closePortMenuList();
                }
            } else if (e.keyCode === 40) {
                var focused = $('.port-menu-list li:focus');
                var next = focused.next();
                if (next.length !== 0) {
                    next.focus();
                }
            } else if (e.keyCode === 38) {
                var focused = getFocusedItem();
                var prev = focused.prev();
                if (prev.length !== 0) {
                    prev.focus();
                }
            }
        });
    };
    //Public API
    return {
        init: init,
        getLanguage: getLanguage,
        getLanguageLang: getLanguageLang,
        getLanguageIcon: getLanguageIcon
    };
};