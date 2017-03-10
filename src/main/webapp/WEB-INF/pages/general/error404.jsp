<%@ page import="com.connectto.general.util.ConstantGeneral" %>
<%--
  Created by IntelliJ IDEA.
  User: htdev001
  Date: 4/1/14
  Time: 10:45 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>--%>
<%--<fmt:setLocale scope="session" value="${sessionScope.WW_TRANS_I18N_LOCALE}" />--%>
<%--<fmt:setBundle basename="com.connectto.general.config.web.resource.message"  />--%>


<html>
<head>
    <link href="<%=request.getContextPath()%>/css/general/bootstrap/css/bootstrap.css" rel="stylesheet">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/general/jquery/jquery.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/general/bootstrap/js/bootstrap.js"></script>

    <title>ERROR</title>
    <style>


        @font-face {
            font-family: 'GothaProBol.otf';
            src: url(./css/general/fonts/GothaProBol.otf);
        }

        @font-face {
            font-family: 'GothaProReg.otf';
            src: url(./css/general/fonts/GothaProReg.otf);
        }

        .error_div, .number_div {
            text-align: center;
            padding: 5px;
            font-size: 500%;
            font-family: 'GothaProBol.otf';
            text-transform: uppercase;
            color: #e15f5f;
        }

        .main_cont {
            height: auto;
        }

        .centered {
            margin: 0 auto;
            float: none;
        }

        .info_div {
            font-size: 180%;
            font-family: 'GothaProReg.otf';
            color: #555555;
            text-align: left;
        }

        .button_div button {
            width: 100%;
            border-radius: 6px;
            border: none;
            text-transform: uppercase;
            background-color: #27ccc0;
            cursor: pointer;
            padding: 5px 10px 5px 10px;
            height: 55px;
            color: #fafafa;
            font-size: 200%;
            font-family: 'GothaProBol.otf';
            margin: 15px 0 15px 0;
        }

        .footer_div {
            width: 100%;
            height: 80px;
            background-color: #d7d7d7;

        }

    </style>
    <script type="text/javascript">
        function goToAction(href) {
            window.location = href;
        }

        function footerPlace() {
            var footer_height = $(".footer_div").outerHeight();
            console.log("footer_height", footer_height);
            var window_height = window.innerHeight;
            console.log("window_height",window_height);
            var document_height = Math.max(
                    document.body.scrollHeight, document.documentElement.scrollHeight,
                    document.body.offsetHeight, document.documentElement.offsetHeight,
                    document.body.clientHeight, document.documentElement.clientHeight

            );
            console.log(document.body.scrollHeight,document.body.offsetHeight,document.body.clientHeight,document.documentElement.scrollHeight,document.documentElement.offsetHeight, document.documentElement.clientHeight)
            console.log("document_height",document_height);
            var content = document_height - footer_height;
            console.log("content",content);
            if (window_height < document_height) {
                if ($(".footer_div")) {
                    $(".main_cont").css("height", "auto");

                }
            }
            else {
                $(".main_cont").css("min-height", content + "px");

            }
        }
        $(document).ready(function () {
            footerPlace();

            $(window).resize(function () {
                footerPlace();
            })
        })

    </script>
</head>
<body>

<div class="container-fluid main_cont">
    <div class="container  ">
        <div class="row">
            <div class="row">
                <div class="error_div centered col-lg-4 col-md-6 col-sm-5 col-xs-8">error</div>
            </div>
            <div class="row">
                <div class="number_div  centered col-lg-4 col-md-6 col-sm-5 col-xs-8">404</div>
            </div>
            <div class="row">
                <div class="info_div  centered col-lg-4 col-md-6 col-sm-5 col-xs-8">
                    <%--<fmt:message key="error.404" />--%>
                </div>
            </div>
            <div class="row">
                <div class="button_div  centered col-lg-4 col-md-6 col-sm-5 col-xs-8">
                    <button onclick="goToAction('general.htm')"> go back
                    </button>
                </div>
            </div>

        </div>
    </div>
</div>
<div class="footer_div"></div>


</body>
</html>

