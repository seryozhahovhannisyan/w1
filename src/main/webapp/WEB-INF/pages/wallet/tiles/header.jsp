<%--
  Created by IntelliJ IDEA.
  User: htdev001
  Date: 9/2/14
  Time: 10:14 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<header class="">
    <div id="gen-head" data-focus-section="1" class="cm-ns-bar ">
        <div class="cm-aligner01">

            <a class="cm-item-text-icon" data-index="4">
                <span class=""><s:property value='#session.session_user.partition.name'/></span>
                <img width="30px" class="cm-item-img"
                     src='<s:property value="#session.session_user.partition.partitionServerUrl"/><s:property value="#session.session_user.partition.partitionLogoDirectory"/><s:property value="#session.session_user.partition.logoPath"/>'
                     alt='<s:property value='#session.session_user.partition.name'/>'/>
            </a>

            <!-- Text element with link -->
            <div class="cm-item-text">
                <a data-index="5" class=" home" id="main"
                   href='<s:property value="#session.session_wallet.currentLocation.previousUrl"/>'><s:text
                        name="wallet.general.Back"/>

            </div>
            <!-- Text element with span -->
            <a class="cm-item-text-icon" href="pre_menu.htm" id="main_home" data-index="4">
                <span class=""> <s:text name="menu.field.Menu">Menu</s:text></span>
                <span class="cm-item-icon-inner location"></span>
            </a>

            <!-- Icon element: g01 - online, g02 - offline, ...-->
            <div id="online-status" class=" cm-item-icon">
                <span class="icon-area g01"></span>
            </div>
        </div>
    </div>
</header>

