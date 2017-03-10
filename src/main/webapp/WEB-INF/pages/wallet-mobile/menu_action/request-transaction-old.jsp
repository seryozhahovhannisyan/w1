
<%@ page import="com.connectto.general.util.ConstantGeneral" %>
<%@ page import="com.connectto.general.model.Partition" %>
<%@ page import="com.connectto.general.model.lcp.PartitionLCP" %>
<%--
  Created by IntelliJ IDEA.
  User: htdev001
  Date: 9/2/14
  Time: 10:18 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/wallet/wallet.css" media="screen">
<%
    Partition partition = (Partition)session.getAttribute(ConstantGeneral.SESSION_URL_PARTITION);
    String partitionDns = PartitionLCP.getDNS(partition.getId());
%>
<script type="text/javascript">

    $(document).ready(function () {
        selected_nav('.request_envelop_parent_li');
        <s:if test="searchLike != null " >
        console.info('<s:property value="searchLike"/>');
        search_user(0, '<s:property value="searchLike"/>');
        </s:if>


        search_click();
    })

    function search_click() {
        $(".search").click(function () {
            search_user(-1, '', 'asc')
        })
    }

    function search_user(current_page, search_like,order_type) {
        loader_show();
        var search_like_val = $("#search_user_text").val();

        if (search_like != null && search_like.length != 0) {
            search_like_val = search_like;
        }
        if (order_type == null) {
            order_type = 'asc';
        }
        $.ajax({
            url: "search_user.htm",
            type: "post",
            dataType: "json",
            async: true,
            data: {
                searchLike: search_like_val,
                currentPage: current_page
            },
            success: function (data) {
                if (data != null && data.responseDto.status == "SUCCESS") {
                    //lastScrollTop = 0 ;
                    var cont;
                    var cur_page = data.currentPage;
                    var is_last = data.isLast;
                    if (current_page == null || current_page < 2) {
                        clean_profile();
                        cont = $("<ul   class='port-menu-list logged_accountList search_result'  data-order_type='" + order_type + "' data-action='search_user' data-search_like='" + search_like + "' data-current_page='" + cur_page + "' />");
                        $("#action_queue").after(cont);
                        $("#action_queue").empty();

                        <%--var title_request_payment = '<s:text name="wallet.action.request.payment"/>';--%>
                        <%--$("<a href='pre_request_payment.htm'>" + title_request_payment + "</a>").appendTo("#action_queue");--%>
                        <%--var func_search_user = "search_user(0,'" + search_like_val + "')";--%>

                        <%--var title_search_result = '<s:text name="wallet.action.search.result"/>';--%>
                        <%--$("<a href='#'>" + title_search_result + "</a>").attr('onclick', func_search_user).appendTo("#action_queue");--%>

                    } else {
                        $(".search_result").attr('data-current_page', cur_page).attr('data-is_last', is_last);
                        cont = $(".search_result");
                    }

                    var search_users = data.searchUsers;
                    if (search_users != null) {
                        var partition_dns = "<%=partitionDns%>";

                        $.each(search_users, function (i, item) {
                            var src = '';
                            var search_user = item;

                            if (search_user == null) {
                                JSLoggerAction(LOG_LEVEL.ERROR, JS.AJAX, "search_user", "status=[search user is null];searched data is=[" + search_like_val + "],responseText=[incorrect incoming data]");
                            } else {
                                var id = search_user.id;
                                var li = $('<li class="searched_item" tabindex="' + ( i + 1 ) + '"/>').appendTo(cont);
                                var div = $('<div class="info_parent_div"></div>').appendTo(li);
                                var fileName = search_user.photoData != null ? search_user.photoData.fileName : null;
                                if (fileName == null) {
                                    src = "<%=request.getContextPath()%>/img/general/avatar/" + partition_dns + "/1.png";
                                }
                                else {
                                    src = getUserPhotoPath("<%=request.getContextPath()%>", search_user.id, fileName, search_user.defaultPhotoId);
                                }


                                $img = $('<div class="user_div" ><img class="thumb" src="' + src + '"/></div> ').appendTo(div);
                                var innerDiv = $('<div class="detales"/>').appendTo(div);


                                var func_show_user_action = 'pre_request_payment_view.htm?userId=' + id + '&searchLike=' + search_like_val;
                                $('<a class="name"/>').text(search_user.name + ' ' + search_user.surname).attr('href', func_show_user_action).appendTo(innerDiv);

                                var envelop_div = $("<div class='envelop_div'></div>");
                                $(innerDiv).after(envelop_div);
                                $(envelop_div).html("<img src='<%=request.getContextPath()%>/img/wallet/icon/requst_transaction.png' alt='envelop icon'>");
                            }
                        });
                        scroll_paging();
                        footerPlace();
                        loader_hide()
                    }

                } else {
                    JSLoggerAction(LOG_LEVEL.ERROR, JS.AJAX_DTO, "search_user", "status=[" + data.responseDto.status + "]");
                    loader_hide();
                }

            },
            error: function (xhr, ajaxOptions, thrownError) {
                JSLoggerAction(LOG_LEVEL.ERROR, JS.AJAX, "search_user", "status=[" + xhr.status + "],responseText=[" + xhr.responseText + "]");
                loader_hide();
            }


        })
    }

    function scroll_paging() {
        $('.search_result').scroll(function () {
            if ($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {
                var data_action = $(this).attr("data-action");
                var data_id = $(this).attr("data-id");
                var data_current_page = $(this).attr("data-current_page");
                var data_is_last = $(this).attr("data-is_last");
                var data_search_like = $(this).attr("data-search_like");
                var data_order_type = $(this).attr("data-order_type");
                if (data_is_last == 0 || data_is_last == null) {
                    if (data_action == "search_user") {
                        search_user(data_current_page, data_search_like, data_order_type);
                    }
                }
                $(this).off( "scroll" )
            }
        });
    }

//    function scroll_paging() {
//
//        var lastOffset = $('.search_result').scrollTop();
//        var lastDate = new Date().getTime();
//        $('.search_result').scroll(function (e) {
//
//            var delayInMs = e.timeStamp - lastDate;
//            var offset = e.target.scrollTop - lastOffset;
//            var speedInpxPerMs = offset / delayInMs;
//
//            lastDate = e.timeStamp;
//            lastOffset = e.target.scrollTop;
//
//            //var documentScrollTop = $(this).scrollTop();
//            // var documentScrollLeft = $(this).scrollLeft();
//            var elem_height = $(".searched_item").height();
//            var start_load = elem_height * 2;
//
//            var this_innerHeight = $(this).innerHeight();
//            var this_scrollHeight = $(this)[0].scrollHeight;
//
//            var this_scrollTop = $(this).scrollTop();
//            var this_innerHeight = $(this).innerHeight();
//            var this_scrollHeight = $(this)[0].scrollHeight;
//
//            var startLoad = start_load + $(this).scrollTop() + $(this).innerHeight();
//            /*$(this)[0].scrollHeight +10 >  startLoad &&*/
//            if ((this_scrollTop + this_innerHeight) >= this_scrollHeight) {
//                if (this_scrollTop < lastScrollTop) {
//                    return;
//                    //  code
//                } else {
//                    var data_action = $(this).attr("data-action");
//                    /*var selected_user_id = $(this).attr("data-selected_user_id");*/
//                    var data_id = $(this).attr("data-id");
//                    var data_current_page = $(this).attr("data-current_page");
//                    var data_is_last = $(this).attr("data-is_last");
//                    var data_search_like = $(this).attr("data-search_like");
//
//
//                    console.info("data_action =" + data_action + ",data_id =" + data_id + ",data_current_page =" + data_current_page + ",data_is_last =" + data_is_last);
//
//                    if (data_is_last == 0 || data_is_last == null) {
//                        if (data_action == "search_user") {
//                            search_user(data_current_page, data_search_like);
//                        }
//                    }
//                    return;
//                    // upscroll code
//                }
//            }
//            lastScrollTop = this_scrollTop;
//        });
//    }

    function clean_profile() {
        $(".search_result").remove();
    }



    var index = 0;
    function getUserPhotoPath(contextPath, userId, photoDataName, defaultPhotoId) {
        var src = contextPath;

        if (photoDataName != null) {
            src += "view_thumb_picture?noCache=" + (++index) + "&userId=" + userId + "&name=" + photoDataName;
        } else if (defaultPhotoId != null && defaultPhotoId != 0) {
            src += "<%=request.getContextPath()%>/img/general/user_profile_default/default_" + defaultPhotoId + ".jpg";
        } else {
            src += "<%=request.getContextPath()%>/img/general/user_profile_default/default_0.jpg";
        }
        return src;
    }

</script>

<div style="clear: both; height: 1px"></div>

<div class="container-fluid">
    <div class="row main">
        <div id="action_panel" class="action_panel">
            <div class="row" style="border-bottom: 1px solid #555965">
                <div id="search_user" class="user_search_div">
                    <div class="input_before_text"><s:text
                            name="wallet.pages.make.search.before.text">Please choose precipitant</s:text></div>
                    <div class="search_parent_div">
                        <div class="search"></div>
                        <input id="search_user_text" type="search" class="search_user_input" name="searchLike"
                               placeholder="<s:text name="wallet.pages.make.label.search_by">Search Friend or Company</s:text>"
                                >
                    </div>
                    <div class="arrow_div">
                        <div class="up"></div>
                        <div class="down"></div>
                        <img src="<%=request.getContextPath()%>/img/wallet/icon/arrow_up_icon.png" alt="arrow icon"/>
                    </div>
                    <div class="sort_div">
                        <s:text name="wallet.action.search.result.sort">Sort</s:text>
                    </div>

                </div>
            </div>
            <div id="action_queue">

            </div>
            <div id="search_result" class="search_result">

            </div>

            <div id="selected_user" class="selected_user">
            </div>


        </div>
    </div>
</div>
