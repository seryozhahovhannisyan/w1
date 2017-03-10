<%--
  Created by IntelliJ IDEA.
  User: htdev01
  Date: 9/25/2015
  Time: 12:48 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css">
  /*body { font-family:Arial, Helvetica, Sans-Serif; font-size:0.75em; color:#000;}*/
  .desc {
    color: #6b6b6b;
  }

  .desc a {
    color: #0092dd;
  }

   .dropdown dd, .dropdown dt, .dropdown ul {
    margin: 0px;
    padding: 0px;
  }

  .dropdown dt {
    display: inline-block;
    border: none;
  }

  .dropdown dd {
    position: relative;
  }

  .dropdown a, .dropdown a:visited {
    color: #816c5b;
    text-decoration: none;
    outline: none;
  }

  .dropdown a:hover {
    color: #5d4617;
  }

  .dropdown dt a:hover {
    color: #5d4617;
    border: 1px solid #d0c9af;
    box-shadow: 10px 10px 5px #888888;
  }

  .dropdown dt a {
    display: block;
    padding-right: 20px;
    width: 150px;
  }

  .dropdown dt a span {
    cursor: pointer;
    display: block;
    padding: 5px;
  }

  /*.dropdown dd ul {*/
    /*background: rgb(234, 240, 241);*/
    /*border: 1px solid #d4ca9a;*/
    /*color: #C5C0B0;*/
    /*display: none;*/
    /*left: 0px;*/
    /*padding: 5px 0px;*/
    /*position: absolute;*/
    /*top: -260px;*/
    /*width: auto;*/
    /*min-width: 170px;*/
    /*list-style: none;*/
    /*box-shadow: 10px 10px 5px #888888;*/
  /*}*/

  .dropdown span.value {
    display: none;
  }

  .dropdown dd ul li a {
    padding: 5px;
    display: block;
  }

  .dropdown dd ul li a:hover {
    background-color: #d0c9af;
  }

  .dropdown img.flag {
    border: none;
    vertical-align: middle;
    margin-left: 20px;
      width: 30px;
  }




</style>
<script type="text/javascript">
  $(document).ready(function () {
    $(document).on("touchstart",function(e){
      var $clicked = $(e.target);
      if (! $clicked.parents().hasClass("dropdown"))
        $(".dropdown dd ul").hide();
    });
    var userLang = navigator.language || navigator.userLanguage;
    var l = userLang.split('-')[0];


    $(".dropdown dt a").click(function () {
      $(".dropdown dd ul").toggle();
    });

  });

  $(document).bind('click', function (e) {
    var $clicked = $(e.target);
    if (!$clicked.parents().hasClass("dropdown"))
      $(".dropdown dd ul").hide();
  });


  function select_lang(lang) {
    window.location = "locale.htm?lang=" + lang;
  }
  function hide_lang() {
    $(".dropdown dd ul").hide();
  }



</script>

<dl id="sample" class="dropdown ">
  <dt>
    <a href="javascript:void(0)">
      <s:set var="lang" value="%{getSessionLanguage()}"/>
      <img class="flag"
           src='<%=request.getContextPath()%>/img/general/icon/language/<s:property value="#lang.key"/>.png'
           alt="<s:property value="#lang.title"/>"/>
      <s:property value="#lang.title"/>

      <span class="value"></span>
    </a>
  </dt>
  <dd>
    <ul>
      <s:iterator value="%{getLanguages()}" var="language">

        <li>
          <a href="#"
                  <s:if test="%{#lang.value == #language.value}"> class="current_lang" onclick="hide_lang();return false;"
                    style=" background-color: #d0c9af; "
                  </s:if>
                  <s:else> onclick="select_lang('<s:property
                          value="#language.key"/>');return false;"</s:else>>
            <img class="flag"
                 src='<%=request.getContextPath()%>/img/general/icon/language/<s:property value="#language.key"/>.png'
                 alt="<s:property value="#language.title"/>"/>
            <s:property value="#language.title"/>

              <%--<span class="value"><s:property value="#lang.key"/></span>--%>
          </a>
        </li>
      </s:iterator>
    </ul>
  </dd>
</dl>

