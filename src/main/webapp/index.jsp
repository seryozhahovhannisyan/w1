<!DOCTYPE html>
<%@ page import="com.connectto.general.model.Partition" %>
<%@ page import="com.connectto.general.model.lcp.Language" %>
<%@ page import="com.connectto.general.model.lcp.PartitionLCP" %>
<%@ page import="com.connectto.general.util.ConstantGeneral" %>


<%-- Created by IntelliJ IDEA. --%>
<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%
    Partition partition = (Partition)session.getAttribute(ConstantGeneral.SESSION_URL_PARTITION);
    String partitionDns = PartitionLCP.getDNS(partition.getId());
%>
<head>
    <title><%=partition.getName()%>
    </title>
    <link rel="icon"
          type="image/png"
          href="/img/general/<%=partitionDns%>/favicon.png">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.4.0/css/font-awesome.min.css">
    <style type="text/css">
        html {
            width: 100%;
            padding: 0;
            margin: 0;
            background-color: rgba(230, 230, 230, 1);
        }

        body {
            width: 100%;
            padding: 0;
            margin: 0;
        }

        .logo {
            width: 60%;
            height: 100%;
            margin: 0 auto;
            background-repeat: no-repeat;
            background-size: contain;
            background-position: 50% 50%;
            background-image : url('/img/general/<%=partitionDns%>/start.png');
            position: absolute;
            margin-left: 20%;
        }
    </style>

    <%--<script type="text/javascript" src="<%=request.getContextPath()%>/js/general/jquery/jquery.js"></script>--%>
    <%--<script type="text/javascript" src="<%=request.getContextPath()%>/js/general/general_api.js"></script>--%>
    <%--<script type="text/javascript" src="<%=request.getContextPath()%>/js/wallet/freeze_charge_api.js"></script>--%>
    <%--<script type="text/javascript" src="<%=request.getContextPath()%>/js/wallet/freeze_charge_api_test.js"></script>--%>

    <%--<script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-1.12.4.min.js"></script>--%>

    <script type="text/javascript">
        window.onload = function () {
            var test = false;
            var params = "?";
            try {
                var userLang = navigator.language || navigator.userLanguage;
                var lang = userLang.split('-')[0];
                <%
                    if(PartitionLCP.isArmenian(partition.getId())){
                %>
                lang = '<%=Language.ARMENIAN.getKey()%>';
                <%
                }%>
                params += 'lang=' + lang;
            } catch (e) {
            }
            if(!test){
//                window.location = "start-action.htm";// + params;
            } else {
//                merchant_start_transaction_deposit();
//                merchant_start_transaction_withdraw();
//                direct_charge_from_wallet_test();
            }
        }


    </script>
</head>
<body style="background-color: red">
<div id="logo-hld" class="StartFilter-hld">
    <div class="logo"></div>
</div>

</body>
</html>