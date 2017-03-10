<%@ page import="com.connectto.general.model.Partition" %>
<%@ page import="com.connectto.general.model.lcp.PartitionLCP" %>
<%@ page import="com.connectto.general.util.ConstantGeneral" %>

<%--
  Created by IntelliJ IDEA.
  User: htdev001
  Date: 6/5/14
  Time: 11:17 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<!DOCTYPE html>
<html>
<head>

    <title><s:text name="wallet.general.title">Wallet</s:text></title>
    <%
        Partition partition = (Partition) session.getAttribute(ConstantGeneral.SESSION_URL_PARTITION);
        String partitionDns = PartitionLCP.getDNS(partition.getId());
        String host = PartitionLCP.valueOf(partition.getId()).getHost();
    %>
    <link rel="icon" type="image/png" href="<%=request.getContextPath()%>/img/general/<%=partitionDns%>/favicon.png">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
    <link href="<%=request.getContextPath()%>/css/general/bootstrap/css/bootstrap.css" rel="stylesheet">


    <%--<link href="http://cdnjs.cloudflare.com/ajax/libs/bootstrap-datepicker/1.3.0/css/datepicker.css" rel="stylesheet" type="text/css" />--%>
    <style type="text/css">
        @font-face {
            font-family: 'GothaProBol.otf';
            src: url(/css/general/fonts/GothaProBol.otf);
        }

        @font-face {
            font-family: 'GothaProMed.otf';
            src: url(/css/general/fonts/GothaProMed.otf);
        }

        .fix_bottom {
            position: absolute;
            bottom: 0;
        }

        .blur {
            -webkit-filter: blur(5px);
            -moz-filter: blur(5px);
            -o-filter: blur(5px);
            -ms-filter: blur(5px);
            filter: blur(5px);
        }

        .loader {
            position: absolute;
            width: 100%;
            top: 0px;
            left: 0px;
            left: 0px;
            z-index: 10;
            display: none;

        }

        .loader_img_div {
            position: absolute;
            height: 50px;
            width: 50px;
            top: 50%;
            z-index: 3;
            left: 50%;

        }

        .loader img {
            width: 100%;
            height: auto;
        }

        .error_div_parent {
            width: 50%;
            min-width: 300px;
            height: 200px;
            position: absolute;
            top: 0;
            bottom: 0;
            left: 0;
            right: 0;
            margin: auto;
            background-color: #dee5eb;
            border-radius: 6px;
            overflow: auto;
            display: none;
            z-index: 5;
        }

        .error_header, .success_header {
            /*width: 100%;*/
            height: auto;
            background-color: #d5dde2;
            padding: 10px 0 10px 20px;
            color: #e03d4f;
            font-family: 'GothaProBol.otf';
            font-size: 110%;
            position: sticky;
            top: 0;

        }

        .close_error, .close_success {
            background-image: url('./img/wallet/icon/error_close.png');
            background-position: center;
            background-repeat: no-repeat;
            background-size: contain;
            width: 20px;
            height: 20px;
            float: right;
            margin-right: 10px;
            cursor: pointer;
        }

        .error_text, .success_text {
            /*width: 100%;*/
            height: auto;
            font-family: 'GothaProMed.otf';
            font-size: 100%;
            padding: 10px;
            color: #2b2f3e;
        }

        .success_div_parent {
            width: 30%;
            min-width: 300px;
            height: 200px;
            position: absolute;
            top: 0;
            bottom: 0;
            left: 0;
            right: 0;
            margin: auto;
            background-color: #dee5eb;
            border-radius: 6px;
            overflow: auto;
            display: none;
            z-index: 5;
        }

        .ok_button {
            text-align: center;
            font-family: 'GothaProMed.otf';
            font-size: 110%;
        }

        .ok_button button {
            min-width: 60px;
            min-height: 30px;
            padding: 5px 10px;
            border: none;
            box-shadow: 0 0 5px black;
            background-color: #10c76b;
            cursor: pointer;
            color: white;
        }

        .ok_button button:hover {
            background-color: #10c469;
        }

    </style>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/general/jquery/jquery.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/general/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/general/jquery/jquery-ui.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/general/general_api.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/general/log.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/wallet/freeze_charge_api.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/wallet/freeze_charge_api_test.js"></script>

    <script type="text/javascript"
            src="<%=request.getContextPath()%>/sounds/wallet/soundmanager2API/script/soundmanager2.js"></script>

    <script type="text/javascript">

        var IMAGE_BASE_URL = '<%=host%>/view_user_picture?userId=';
        var IMAGE_CASH = 2;
        var LOGGER = new GENERAL_API.ACTION.LOGS;
        var DATA_NOT_FOUND_MSG = '<s:text name="wallet.data.not.found">The data not found</s:text>';

        function block_user_call(id, ns, src, elem) {
            var cancel = "<s:text name='wallet.general.button.Cancel'>Submit</s:text>";
            var submit = "<s:text name='wallet.general.button.Submit'>Submit</s:text>";
            var title = "<s:text name='wallet.settings.blocked.users.popup.title'>Submit</s:text>";
            var reason = "<s:text name='wallet.settings.blocked.users.popup.reason'>Reason</s:text>";
            var img_src = "<%=request.getContextPath()%>/img/wallet/icon/cancel_disput_popup.png";
            blok_user_popup(submit, title, cancel, reason, id, ns, src, img_src, elem);
        }


        function write_error_msg(id, after) {
            $("#" + id).remove();
            var msg_content = '<div id="error_msg">' + DATA_NOT_FOUND_MSG + '</div>';
            $("#" + after).after(msg_content);
            loader_hide()
        }


        /*test for window close*/

        function closecallback() {
            var allowclose = false;

            /* todo   ajax, the url is example*/
            function closeinit() {
                var d = {
                    blockedId: 225,
                    title: 'title ',
                    content: ' content '
                };
                $.ajax({
                    url: 'block-user.htm',
                    type: "post",
                    dataType: "json",
                    async: false,
                    data: d,
                    success: function (data) {
                        localStorage.setItem("hhhhhhhhhhhhhh", "tytfytfry 6  ");
                    }
                });
            };

            $(document).bind('keydown', function (e) {
                if (e.keyCode == 116) {
                    allowclose = true;
                }
            });

            $("a").bind("click", function () {
                allowclose = true;
            });
            $("div").bind("click", function () {
                allowclose = true;
            });
            $("span").bind("click", function () {
                allowclose = true;
            });

            $("p").bind("click", function () {
                allowclose = true;
            });
            $("form").bind("submit", function () {
                allowclose = true;
            });

            $("input[type=submit]").bind("click", function () {
                allowclose = true;
            });
            window.onbeforeunload = function () {
                if (!allowclose) {
                    closeinit();
                }
            };

        }
        /*test for window close end */


        function goToAction(href) {
            //href +='?sc=' +$(document).width;
            window.location = href;
        }

        function sonds(url) {
            var music_url = url ? url : "";
            var sondmeneger = soundManager.setup({
                url: '<%=request.getContextPath()%>/sounds/wallet/soundmanager2API/swf',
                flashVersion: 9, // optional: shiny features (default = 8)
                // optional: ignore Flash where possible, use 100% HTML5 mode
                // preferFlash: false,
                onready: function () {
                    var mySound = soundManager.createSound({
                        id: 'aSound',
                        url: music_url
                    });
                    mySound.play();

                    // Ready to use; soundManager.createSound() etc. can now be called.
                }

            });

        }
        function get_url() {
            var pathname = window.location.pathname;
            return pathname
        }

        function body_background() {
            if (get_url() == "/home_wallet.htm" || get_url() == "/wallet/home_wallet.htm") {
                $("body").css("background-size", "cover")
            }
            else {
                $("body").css("background-size", "inherit")
            }
        }

        function local_storage_mute() {
            var mute = '<s:text name="walet.settingd.sound.mute">Click to mute all sounds</s:text>';
            var unmute = '<s:text name="walet.settingd.sound.unmute">Click to unmute all sounds</s:text>';
            if (typeof(Storage) !== "undefined") {
                if (localStorage.mute) {
                    if (localStorage.mute == "true") {
                        soundManager.mute();
                        $(".mute").removeClass("mute").addClass("un_mute").html(unmute)
                    }
                    else {
                        soundManager.unmute();
                        $(".un_mute").addClass("mute").removeClass("un_mute").html(mute)
                    }
                }
            }
            mute_sounds(mute, unmute);
        }
        function mute_sounds(mute, unmute) {
            $("body").delegate(".mute", "click", function () {
                soundManager.mute();
                $(".mute").removeClass("mute").addClass("un_mute").html(unmute);
                if (typeof(Storage) !== "undefined") {
                    localStorage.mute = true;
                }
            });
            $("body").delegate(".un_mute", "click", function () {
                soundManager.unmute();
                $(".un_mute").addClass("mute").removeClass("un_mute").html(mute);
                if (typeof(Storage) !== "undefined") {
                    localStorage.mute = false;
                }
            });

        }
        var wallet_error_handler = function (handle) {
            var sound_url = '<%=request.getContextPath()%>/sounds/wallet/musics/error.wav';
            var handler_status = handle.status ? handle.status : "";
            var handler_text = handle.responseText ? handle.responseText : "";
            var message = handler_status + "<br/>" + handler_text;
            $(".error_text").html(message);
            $(".error_div_parent").show();
            $(".close_error").click(function () {
                $(".error_div_parent").hide();
            });

            loader_hide();
            sonds(sound_url)

        };

        var wallet_succses = function (handle) {
            var sound_url = '<%=request.getContextPath()%>/sounds/wallet/musics/success.wav';
            var handler_status = handle.status ? handle.status : "";
            var handler_text = handle.responseText ? handle.responseText : "";
            var message = handler_status + "<br/>" + handler_text;
            $(".success_text").html(message);
            $(".success_div_parent").show();
            $(".close_success").click(function () {
                $(".success_div_parent").hide();
            });
            loader_hide();
            sonds(sound_url)
        };

        function close_any_popup(selector) {
            $(document).click(function (e) {
                if ($(e.target).closest(selector).length != 0) return false;
                console.log("e.target", $(e.target));
                console.log("e.target lengt", $(e.target).closest(selector).length);
                $(selector).hide();
            });

        }

        function only_number(selector) {
            $(selector).keydown(function (e) {
                // Allow: backspace, delete, tab, escape, enter and .
                if ($.inArray(e.keyCode, [46, 8, 9, 27, 13, 110, 190]) !== -1 ||
                        // Allow: Ctrl+A
                        (e.keyCode == 65 && e.ctrlKey === true) ||
                        // Allow: Ctrl+C
                        (e.keyCode == 67 && e.ctrlKey === true) ||
                        // Allow: Ctrl+X
                        (e.keyCode == 88 && e.ctrlKey === true) ||
                        // Allow: home, end, left, right
                        (e.keyCode >= 35 && e.keyCode <= 39)) {
                    // let it happen, don't do anything
                    return;
                }
                // Ensure that it is a number and stop the keypress
                if ((e.shiftKey || (e.keyCode < 48 || e.keyCode > 57)) && (e.keyCode < 96 || e.keyCode > 105)) {
                    e.preventDefault();
                }
            });
        }

        function selected_nav(selector) {
            $(selector).append($("<div class='selected_nav'></div>"))
        }

        function loader_show() {
            var loader_height = $(document).height();
            $(".loader").css("height", loader_height + "px").show();
            $(".div_for_blur").addClass("blur");
        }

        function loader_hide() {
            $(".loader").hide();
            $(".div_for_blur").removeClass("blur");
        }

        function footerPlace() {
            var doc_height = Math.max(
                    document.body.scrollHeight, document.documentElement.scrollHeight,
                    document.body.offsetHeight, document.documentElement.offsetHeight,
                    document.body.clientHeight, document.documentElement.clientHeight
            );
            var win_height = $(window).height();
            console.log("doc_height", doc_height, "win_height", win_height);
            if (win_height < doc_height) {
                $(".footer").removeClass("fix_bottom")
            }
            else {
                $(".footer").addClass("fix_bottom")
            }
        }


        $(document).ready(function () {
            footerPlace();
            local_storage_mute();
            get_url();
            body_background();

            //todo for window close
//            closecallback();

//            soundManager.mute();

            $("#logout").click(function () {
                logout();
            });
            $("#wallet_close").click(function () {
                wallet_close();
            });


        });

        function dragable_divs(selector) {
            $(selector).draggable({});
        }


        function logout() {
            loader_show();
            var f = document.createElement("form");
            f.setAttribute('method', "post");
            f.setAttribute('action', "logout.htm");
            document.body.appendChild(f);
            f.submit();

        }
        function wallet_close() {
            loader_show();
            /*var f = document.createElement("form");
             f.setAttribute('method', "post");
             f.setAttribute('action', "wallet_exit.htm");
             document.body.appendChild(f);
             f.submit();*/
            alert(1)
            goToAction('wallet_exit.htm')
        }

    </script>

</head>
<body>


<div>
    <tiles:insertAttribute name="person_panel"/>
</div>

<div class="div_for_blur">
    <tiles:insertAttribute name="action_panel"/>
</div>

<div>
    <tiles:insertAttribute name="footer_panel"/>
</div>
<div class="loader">
    <div class="loader_img_div">
        <img class="center-block loading" src="<%=request.getContextPath()%>/img/general/login/loading.gif"
             alt="loading..."/>
    </div>
</div>

<div class="error_div_parent">
    <div class="error_header">
        <span><s:text name="message.error">Error message</s:text></span>

        <div class="close_error"></div>
    </div>
    <div class="error_text">

    </div>
</div>


<div class="success_div_parent">
    <div class="success_header">
        <span><s:text name="wallet.success.message">Success message</s:text></span>

        <div class="close_success"></div>
    </div>
    <div class="success_text">

    </div>
    <div class="ok_button">
        <button><s:text name="guide.label.OK">OK</s:text></button>
    </div>
</div>
</body>
</html>