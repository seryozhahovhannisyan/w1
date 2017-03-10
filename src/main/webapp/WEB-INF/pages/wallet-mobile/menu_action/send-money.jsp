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
<%--//todo backend switch cases--%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    Partition partition = (Partition) session.getAttribute(ConstantGeneral.SESSION_URL_PARTITION);
    String partitionDns = PartitionLCP.getDNS(partition.getId());
%>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/wallet/wallet_mobile/wallet_send_money_mobile.css">
<link rel="stylesheet"
      href="<%=request.getContextPath()%>/css/wallet/wallet_mobile/wallet_send_money_transfer_popup_mobile.css">

<script type="text/javascript">
    var Logger = new GENERAL_API.ACTION.LOGS;

    $(document).ready(function () {
        selected_nav('.send_money_parent_li');
        <s:if test="searchLike != null " >
        search_user(0, '<s:property value="searchLike"/>', "asc");
        </s:if>
        search_click();
        search_by_enter();
        sort_arrow_img();
        hide_write_message();
        chose_selected_option();
        show_details();
        close_by_esc();
        select_blur();
        transfer();
//        dragable_divs(".popup_cont");
//        dragable_divs(".popup_cont_transfer");

        scrolling_popup();


    });

    var disable_previews_scroll = undefined;
    var disable_next_scroll = undefined;
    var array_ids = [];
    var array_name_surnames = [];
    var array_user_photo_paths = [];
    var params = {
        array_ids : undefined,
        id        : undefined,
        array_name_surnames : undefined,
        array_user_photo_paths : undefined
    };

    function update_params(array_ids, id, array_name_surnames, array_user_photo_paths) {
            params.array_ids = array_ids;
            params.id = id;
            params.array_name_surnames = array_name_surnames;
            params.array_user_photo_paths = array_user_photo_paths;
    }

    function transfer() {
        $(".make_transfer").click(function () {
            var params = {};
            var data = {};
            params.async = true;
            data.userId = $(".popup_sm_header").data("user_id");
            data.amount = $(".popup_ammount_input").val();
            data.currencyType = $("#currencyType option:selected").val();
            data.message = $("#write_message").text();
            params.data = data;
            WALLET.send_money_make_payment(params, send_money_make_payment_callback, wallet_error_handler);
        });
    }

    function remove_uploaded_files(name, calback) {

        var dataFileName = name;
        console.log("sdasdasd", dataFileName);
        $.ajax({
            url: 'remove-uploaded.htm',
            type: "post",
            dataType: "json",
            async: true,
            data: {
                dataFileName: dataFileName
            },
            success: function (data) {
                if (data != null && data.responseDto.status == "SUCCESS") {
                    calback();
                } else {
                    var handle = null;
                    if (data != null && data.responseDto != null) {
                        handle = {status: data.responseDto.status, responseText: data.responseDto.messages};
                    } else {
                        handle = {status: 0, responseText: 'empty data'};
                    }
                    error_handler(handle);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status: xhr.status, responseText: xhr.responseText};
                error_handler(handle);
            }
        });
    }
    function upload_photo() {
        var form = document.getElementById('file_upload_form');
        var fileSelect = document.getElementById('upload_file');
        // Get the selected files from the input.
        var files = fileSelect.files;
        // Create a new FormData object.
        var formData = new FormData();

        // Loop through each of the selected files.
        for (var i = 0; i < files.length; i++) {
            var file = files[i];
            // Check the file type.
            /*if (!file.type.match('image.*')) {
             continue;
             }*/
            // Add the file to the request.
            formData.append('datas', file, file.name);

        }

        // Files formData.append(name, file, filename);
        // Blobs formData.append(name, blob, filename);
        // Strings formData.append(name, value);

        var xhr = new XMLHttpRequest();
        // Set up a handler for when the request finishes.
        xhr.onload = function () {
            if (xhr.status === 200) {
                // File(s) uploaded.
                //uploadButton.innerHTML = 'Upload';
            } else {
                error_handle("upload_photo ", "An error occurred! " + files[0].name);
            }
        };
        xhr.open('POST', 'transaction_data_upload.htm');
        xhr.onreadystatechange = function (response) {
            if (xhr.readyState == 4 && xhr.status == 200) {
                if (xhr.responseText == 'success') {
                    for (var i = 0; i < files.length; i++) {
                        var file_ = files[i];
                        $("#data_div").append('<div class="aplooaded_file" >' +
                                '<div class="file_upload_name"> ' + file_.name + '</div>' +
                                '<div class="file_upload_size"> ' + file_.size + 'kb' + '</div>' +
                                '<div class="close_x_img  close' + i + '"><img src="<%=request.getContextPath()%>/img/wallet/icon/send_money_upload_delete.png" alt="delete icon"/></div>' +
                                '</div>');
                        (function (i) {
                            $(".close" + i + "").click(function () {
                                var file_ = files[i];
                                var this_ = $(this);

                                function remove_file() {
                                    this_.closest(".aplooaded_file").remove()
                                }

                                remove_uploaded_files(file_.name, remove_file)
                            });
                        }(i));
                    }
                } else if (response == 'none') {
                    error_handle("upload_photo", "An error occurred! Empty upload " + files[0].name);
                } else {
                    error_handle("upload_photo", "An error occurred! Empty upload " + files[0].name);
                }
            }
        };

        xhr.send(formData);
    }
    function select_blur() {
        $(".show_send_money_trans").click(function () {
            $(".popup_ammount_input").css("border","none");
            if (($(".popup_ammount_input").val()).trim() != "") {
                var params = {};
                var data = {};
                params.async = true;
                data.userId = $(".popup_sm_header").data("user_id");
                data.amount = $(".popup_ammount_input").val();
                data.currencyType = $("#currencyType option:selected").val();
                params.data = data;
                WALLET.send_money_check_tax(params, send_money_check_tax_callback, wallet_error_handler);
            }
            else{
                $(".popup_ammount_input").css("border","1px solid #ff4154")
            }
        })
    }
    function close_by_esc() {
        $(document).keydown(function (e) {
            if (e.which == 27) {
                close_send_money_popup()
            }
        })

    }
    function show_details() {
        $(".details_button span").click(function () {
            $(".show_details").toggle("slow");
        })
    }
    function hide_write_message() {

        var place = '<span class="message_placeholder"><s:text name="wallet.login.write.message">Write message</s:text></span>';

        $("body").delegate('.write_message','click', function () {

            $(this).focus(function () {
                if ($(".message_placeholder")) {
                    $(".message_placeholder").remove();
                    $(this).html("")
                }
            });
            $(this).focus();
        });
        $(".popup_cont").on('click',function (e) {
            if (!$(e.target).hasClass("write_message")){
                     $(".write_message").blur(function () {
                    if ($(this).text() === "") {
                        $(this).html(place);

                    }

                });
                $(".write_message").blur();
            }
        });
    }
    function search_by_enter() {
        $(".search_user_input").keyup(function (event) {
            if (event.keyCode == 13) {
                search_user(-1, '', 'asc');
            }
        })
    }

    function show_send_money_popup(id, name_surname, user_photo_path) {
        pre_make_payment_view(id, name_surname, user_photo_path);

    }

    function logic_show_send_money_popup_previous( ){
        disable_next_scroll = false;
        for (var i = 0; i < params.array_ids.length; i++) {
            if (params.array_ids[i] == params.id && params.array_ids.length >= 2) {

                params.id = params.array_ids[i-1] ;
                if (params.id === undefined) {
                    params.id = params.array_ids[i] ;
                }
                var previews_id = params.array_ids[i-1] ;
                var previews_name_surname = params.array_name_surnames[i-1];
                var previews_user_photo_path = params.array_user_photo_paths[i-1];
                if (params.array_ids[i-2] == undefined && previews_id != undefined) {
                    show_send_money_popup_previous(previews_id, previews_name_surname, previews_user_photo_path);
                    disable_previews_scroll = true;
                    disabble_previews_scrolling();
                } else if (previews_id == undefined) {
                    disable_previews_scroll = true;
                    disabble_previews_scrolling();
                } else {
                    disable_previews_scroll =  false;
                    show_send_money_popup_previous(previews_id, previews_name_surname, previews_user_photo_path);
                }
            }

        }
        return params.id;
    }

    function logic_show_send_money_popup_next(){
        disable_previews_scroll = false;
        for (var i = params.array_ids.length; i >= 0; i--) {
            if (params.array_ids[i] == params.id && params.array_ids.length >= 2) {
                params.id = params.array_ids[i+1] ;
                if (params.id === undefined) {
                    params.id = params.array_ids[i] ;
                }
                var next_id = params.array_ids[i+1] ;
                var next_name_surname = params.array_name_surnames[i+1];
                var next_user_photo_path = params.array_user_photo_paths[i+1];
                if (params.array_ids[i+2] == undefined && next_id != undefined) {
                    show_send_money_popup_next(next_id, next_name_surname, next_user_photo_path);
                    disable_next_scroll = true;
                    disabble_next_scrolling();
                } else if (next_id == undefined) {
                    disable_next_scroll = true;
                    disabble_next_scrolling();
                } else {
                    disable_next_scroll = false;
                    show_send_money_popup_next(next_id, next_name_surname, next_user_photo_path);
                }
            }
        }
        return params.id ;
    }

    function show_send_money_popup_previous(id, name_surname, user_photo_path) {
        close_send_money_popup();
        pre_make_payment_view(id, name_surname, user_photo_path);
    }
    function show_send_money_popup_next(id, name_surname, user_photo_path) {
        close_send_money_popup();
        pre_make_payment_view(id, name_surname, user_photo_path);
    }

    function close_send_money_popup() {
        $(".popup_cont").hide();
        $(".a_href_name a").html("");
        $(".popup_select_sm").html("");
        $(".popup_info_div").html("");
        $(".popup_ammount_input").val("");

    }
    function close_send_money_transfer_popup() {
        $(".popup_cont_transfer").hide();
        $(".popup_info_div").html("");
        $(".popup_cont").show();
    }
    function close_send_money_transfer_popup_only() {
        $(".popup_cont_transfer").hide();
        $(".popup_cont").hide();
    }
    function search_click() {
        $(".search").click(function () {
            search_user(-1, '', 'asc')
        })
    }
    function sort_arrow_img() {

        $(".up").click(function () {
            var order_type = 'desc';
            $('.arrow_div img').attr('src', '<%=request.getContextPath()%>/img/wallet/icon/arrow_up_icon.png');
            search_user(0, '<s:property value="searchLike"/>', order_type);
        });

        $(".down").click(function () {
            var order_type = 'asc';
            $('.arrow_div img').attr('src', '<%=request.getContextPath()%>/img/wallet/icon/arrow_down_icon.png');
            search_user(0, '<s:property value="searchLike"/>', order_type);
        })

    }

    function default_position_scrolling_popup() {
        var $container = $(".container-for-scroll-sliding");
        var $content = $(".row-for-scroll-sliding");
        containerWidth = $container.width();
        contentWidth = $content.outerWidth();
        contentLeft = 0;
        scrollLeft = 0;

        var window_width = window.innerWidth;
        var need_margin_left =  window_width * 25/100;
        setTimeout(function() {
            $container.scrollLeft(scrollLeft + need_margin_left);
            console.log("default_position_scrolling_popup","need_margin_left",need_margin_left)
        }, 2000);
    }

    function scrolling_popup() {
        var $container = $(".container-for-scroll-sliding");
        var $content = $(".row-for-scroll-sliding");
        containerWidth = $container.width();
        contentWidth = $content.outerWidth();
        contentLeft = 0;
        scrollLeft = 0;
        $(".container-for-scroll-sliding").scroll(function(event) {
            var scrollleft = $(".container-for-scroll-sliding").scrollLeft();
            var window_width = window.innerWidth;
            var need_margin_left = window_width * 25/100;
            if ((scrollleft > need_margin_left + 40) && disable_previews_scroll !== true){
                params.id = logic_show_send_money_popup_previous();
                setTimeout(function() {
                    $container.scrollLeft(scrollLeft + need_margin_left);
                }, 2500);
            }
            if ((scrollleft < need_margin_left - 40) && disable_next_scroll !== true){
                params.id = logic_show_send_money_popup_next();
                setTimeout(function() {
                    $container.scrollLeft(scrollLeft + need_margin_left);
                }, 2500);
            }

       });
    }

    function disabble_previews_scrolling() {
        $(".container-for-scroll-sliding").scroll(function(event) {
            var scrollleft = $(".container-for-scroll-sliding").scrollLeft();

            var window_width = window.innerWidth;
            var need_margin_left = window_width * 25 / 100;
            if (scrollleft > need_margin_left) {
                setTimeout(function() {
                    $(".container-for-scroll-sliding").scrollLeft(scrollLeft + need_margin_left);
                }, 200);
            }
        });
    }

    function disabble_next_scrolling() {
        $(".container-for-scroll-sliding").scroll(function(event) {
            var scrollright = $(".container-for-scroll-sliding").scrollLeft();

            var window_width = window.innerWidth;
            var need_margin_left = window_width * 25 / 100;
            if (scrollright < need_margin_left) {
                setTimeout(function() {
                    $(".container-for-scroll-sliding").scrollLeft(scrollLeft + need_margin_left);
                }, 200);
            }
        });
    }

    function search_user(current_page, search_like, order_type, clear_array_for_popup) {
        loader_show();
        if (clear_array_for_popup !== false) {
            array_ids = [];
            array_name_surnames = [];
            array_user_photo_paths = [];
        }
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
                currentPage: current_page,
                orderType: order_type
            },
            success: function (data) {
                if (data != null && data.responseDto.status == "SUCCESS") {
                    //lastScrollTop = 0 ;
                    var cont;
                    var cur_page = data.currentPage;
                    var is_last = data.isLast;
                    if (current_page == null || current_page < 2) {
                        clean_profile();
                        cont = $("<ul   class='port-menu-list logged_accountList search_result' data-order_type='" + order_type + "' data-action='search_user' data-search_like='" + search_like + "' data-current_page='" + cur_page + "' />");
                        $("#action_queue").after(cont);
                        $("#action_queue").empty();

                    } else {
                        $(".search_result").attr('data-current_page', cur_page).attr('data-is_last', is_last);
                        cont = $(".search_result");
                    }
                    ul_rezult_height();

                    var search_users = data.searchUsers;
                    if (search_users != null) {

                        if (search_users.length == 1) {

                            disabble_previews_scrolling();
                            disabble_next_scrolling();
                            disable_previews_scroll = true;
                            disable_next_scroll = true;
                        } else {
                            disable_previews_scroll = false;
                            disable_next_scroll = false;
                        }

                        $.each(search_users, function (i, item) {
                            var search_user = item;
                            if (search_user != null) {
                                var id = search_user.id;
                                var name_surname = search_user.name + ' ' + search_user.surname
                                var li = $('<li class="searched_item"></li>').appendTo(cont);
                                var div = $('<div class="info_parent_div"></div>').appendTo(li);
                                var user_photo_path = IMAGE_BASE_URL + id;

                                array_ids.push(id);
                                array_name_surnames.push(name_surname);
                                array_user_photo_paths.push(user_photo_path);
                                $(div).append("<div class='user_div  div" + id + "'><img  class='thumb'/></div>");
                                $(".thumb").attr("src", user_photo_path);
                                var innerSpan = $('<div class="detales"/>').appendTo(div);
                                $('.div' + id + ' img').click(function () {

                                    show_send_money_popup(id, name_surname, user_photo_path);
                                    default_position_scrolling_popup();
                                    update_params(array_ids, id, array_name_surnames, array_user_photo_paths );
                                });
                                $("<a class='name name_div" + id + "'/>").text(search_user.name + ' ' + search_user.surname).appendTo(innerSpan);
                                $('.name_div' + id + '').click(function () {


                                    show_send_money_popup(id, name_surname, user_photo_path);

                                    default_position_scrolling_popup();
                                    update_params(array_ids, id, array_name_surnames, array_user_photo_paths );
                                });
                                var envelop_div = $("<div class='envelop_div'></div>");
                                $(innerSpan).after(envelop_div);
                                $(envelop_div).html("<img src='<%=request.getContextPath()%>/img/wallet/icon/send_money.png' alt='envelop icon'>");
                                /*todo footer*/

                            }
                        });
                        scroll_paging();
                        footerPlace();
                        loader_hide();


                    }
                    else {
                        loader_hide();
                        write_error_msg('search_result', 'action_queue');
                        LOGGER.save(Logger.LOG_LEVEL.ERROR, 'search_user', 0, "searched search_like_val =[" + search_like_val + "],responseText=[incorrect incoming data]");
                    }
                }
                else {
                    loader_hide();
                    write_error_msg('search_result', 'action_queue');
                    LOGGER.save(Logger.LOG_LEVEL.ERROR, 'search_user', 0, "searched search_like_val =[" + search_like_val + "],responseText=[incorrect incoming data]");
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                loader_hide();
                write_error_msg('search_result', 'action_queue');
                LOGGER.save(Logger.LOG_LEVEL.ERROR, 'search_user', xhr.status, xhr.responseText);
            }
        })
    }
    function pre_make_payment_view(id, name_surname, user_photo_path) {
        $(".a_href_name a").html(name_surname);
        $(".left_first img").attr("src", user_photo_path);
        $(".popup_sm_header").data("user_id", id);
        $(".popup_select_sm").val('');
        $(".popup_ammount_input").val('');
        $(".popup_info_div").html('');

        var params = {};
        var data = {};
        data.userId = id;
        params.data = data;
        params.async = true;
        WALLET.load_available_currencies(params, load_available_currencies_callback, wallet_error_handler);
    }

    function scroll_paging() {
        $('.search_result').scroll(function () {
            if ($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {
                var data_action = $(this).attr("data-action");
                var data_current_page = $(this).attr("data-current_page");
                var data_is_last = $(this).attr("data-is_last");
                var data_search_like = $(this).attr("data-search_like");
                var data_order_type = $(this).attr("data-order_type");
                if (data_is_last == 0 || data_is_last == null) {
                    if (data_action == "search_user") {
                        var clear_array_for_popup = false;
                        search_user(data_current_page, data_search_like, data_order_type, clear_array_for_popup);
                    }
                }
                $(this).off("scroll")
            }
        });
    }
    function clean_profile() {
        $(".search_result").remove();
    }
    function getInputValue() {
        return $("#search_user_text").val();
    }
    function chose_selected_option() {
        $('.popup_select_sm').on('change', function () {
            var currency = $(".popup_select_sm option:selected").text();
            var id = $(".popup_select_sm option:selected").val();
            $(".currency_type").html(currency).attr('data-id', id);
        });
    }
    function ul_rezult_height() {
        var doc_height = Math.max(
                document.body.scrollHeight, document.documentElement.scrollHeight,
                document.body.offsetHeight, document.documentElement.offsetHeight,
                document.body.clientHeight, document.documentElement.clientHeight
        );
        var win_height = $(window).outerHeight();
        var person_panel_height = $(".person_parent").outerHeight(true);
        var footter_height = $(".footer").outerHeight(true);
        var user_search_height = $(".user_search_div").outerHeight(true);
        var content = doc_height - person_panel_height - footter_height - user_search_height;
        $(".search_result").css("height", content + "px");
        console.log("doc_height_send", doc_height, "win_height_send", win_height, "person_panel_height", person_panel_height, "footter_height", footter_height, "content", content), "user_search_height", user_search_height;
    }


    //CALLBACKS
    var send_money_check_tax_callback = function (data) {
        loader_hide();
        send_money_preview_callback(data);
    };
    var send_money_preview_callback = function (data) {
        console.log("data send_money_preview",data)
        var fromTotalPrice = data.preview.fromTotalPrice != null ? data.preview.fromTotalPrice : "-";
        var fromTotalPriceCurrencyType = data.preview.fromTotalPriceCurrencyType != null ? data.preview.fromTotalPriceCurrencyType : "-";
        var productAmount = data.preview.productAmount != null ? data.preview.productAmount : "-";
        var productCurrencyType = data.preview.productCurrencyType != null ? data.preview.productCurrencyType : "-";
        var totalAmountTaxPrice = data.preview.fromTransactionProcess.tax.totalAmountTaxPrice != null ? data.preview.fromTransactionProcess.tax.totalAmountTaxPrice : "-";
        var transferTaxPrice = data.preview.fromTransactionProcess.tax.transferTaxPrice != null ? data.preview.fromTransactionProcess.tax.transferTaxPrice : "-";
        var transferExchangeTaxPrice = data.preview.fromTransactionProcess.tax.transferExchangeTaxPrice != null ? data.preview.fromTransactionProcess.tax.transferExchangeTaxPrice : "-";
        var rateAmount;
        if (data.preview.fromTransactionProcess.tax.transferTaxExchange === null) {
            rateAmount = "0";
            $(".view_rate").hide();
        }
        else {
            rateAmount = data.preview.fromTransactionProcess.tax.transferTaxExchange;
            $(".view_rate").show();
        }

        $(".tot-tr").html(fromTotalPrice + ' ' + fromTotalPriceCurrencyType);
        $(".trans").html(productAmount + ' ' + productCurrencyType);
        $(".trans_cost").html(totalAmountTaxPrice + ' ' + fromTotalPriceCurrencyType);
        $(".trans_fee").html(totalAmountTaxPrice + ' ' + fromTotalPriceCurrencyType);
        $(".trans-exchange").html(transferTaxPrice + ' ' + fromTotalPriceCurrencyType);
        $(".exchange_fee").html(transferExchangeTaxPrice + ' ' + fromTotalPriceCurrencyType);
        $(".tfd").html('1 x ' + fromTotalPriceCurrencyType + ' = ' + rateAmount + ' ' + productCurrencyType);

        $("#check_payment_fee_msg_box").html(data.paymentFeeMsg);
        $(".popup_cont_transfer").show();
        $(".popup_cont").hide();


    };
    var send_money_make_payment_callback = function (data) {
        loader_hide();
        $(".popup_cont_transfer").hide();
        $(".popup_cont").hide();
    };
    var load_available_currencies_callback = function (data) {
        var user = data.user;
        var defaultCurrencyType = data.defaultCurrencyType;
        var currencyTypes_name = data.currencyTypes;
        var selected = '';
        for (var i = 0; i < currencyTypes_name.length; i++) {
            selected = '';
            if (defaultCurrencyType.id == currencyTypes_name[i].id) {
                selected = " selected='selected' ";
                $(".currency_type").attr('data-id', defaultCurrencyType).html(currencyTypes_name[i].code);
            }
            $(".popup_select_sm").append("<option class='option" + i + "' value=" + currencyTypes_name[i].id + selected + ">" + currencyTypes_name[i].code + "</option>")
        }
        $(".popup_cont").show();
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
                    <div class="sort_div_parent">
                        <div class="arrow_div">
                            <div class="up"></div>
                            <div class="down"></div>
                            <img src="<%=request.getContextPath()%>/img/wallet/icon/arrow_up_icon.png"
                                 alt="arrow icon"/>
                        </div>
                        <div class="sort_div">
                            <s:text name="wallet.action.search.result.sort">Sort</s:text>
                        </div>
                    </div>

                </div>
            </div>

            <div id="action_queue">
                <%--<a href="pre_make_payment.htm"><s:text name="wallet.action.make.payment"/></a>--%>
            </div>


            <div id="search_result" class="search_result">

            </div>

            <div id="selected_user" class="selected_user">
            </div>


        </div>
    </div>
</div>

<div class="container-fliuid popup_cont">
    <div class="container container-for-scroll-sliding">
        <div class="row" >
            <div class="row-for-scroll-sliding">.</div>
            <div class="popup_main">
                <div class="popup_sm_header_parent">
                    <div class="popup_sm_header">
                        <div>
                            <span onclick="close_send_money_popup()">
                                <img src="<%=request.getContextPath()%>/img/wallet/icon/send_money_popup_arrow.png"
                                     alt="arrow icon"/>
                            </span>
                              <span onclick="close_send_money_popup()">
                                <s:text name="wallet.general.button.Cancel">Cancel</s:text>
                            </span>
                        </div>
                        <div>
                            <span class="show_send_money_trans" >
                                <s:text name="wallet.login.send.money">Send money</s:text>
                            </span>
                            <span class="show_send_money_trans" >
                                <img src="<%=request.getContextPath()%>/img/wallet/icon/send_money.png"
                                     alt="envelop icon"/>
                            </span>
                        </div>
                    </div>
                </div>
                <div class="profil_info">
                    <div class="popup_left">
                        <div class="name_img_surname">
                        <div class="left_first">
                            <img alt="profile img"/>
                        </div>
                        <div class="a_href_name">
                            <a href="#" id="user_id" data-id="0"> </a>
                        </div>
                        </div>

                        <div class="fill_amount_div_parent">

                            <form id="make_payment" action="make_payment.htm" method="post">
                                <input type="hidden" value='<s:text name="wallet.action.make.payment"/>' class="actionLabel"
                                       name="actionLabel"/>

                                <input type="hidden" class="currency_type_id" name="selectedCurrencyType"/>
                                <input type="hidden" name="userId" class="user_id_val"/>
                                <input type="hidden" name="toUserNameSurname" class="toUserNameSurname_val"/>
                                <%--<input type="hidden" name="amount" class="amount_val"/>--%>

                                <%--<input type="hidden" name="searchLike" value='<s:property value="searchLike"/>'>--%>

                                <%--<div id="check_payment_fee_msg_box" class="popup_info_div hidden-xs">--%>


                                <%--</div>--%>
                                <div class="popup_ammount ">
                                    <div>
                                        <input type="text" id="amount" class="popup_ammount_input" name="amount"
                                               placeholder="example 50"
                                                autocomplete="off"/>

                            <span class="currency_type">
                            USD
                        </span>
                                    </div>

                                </div>
                                <div class="text-center">


                                    <select id="currencyType" name="currencyType" class="popup_select_sm ">

                                    </select>
                                </div>
                                <div>
                                    <div id="data_div" style="overflow: auto; height: auto;max-height: 190px; text-align: center">

                                    </div>
                                </div>
                            </form>
                        </div>
                        <form action="transaction_data_upload.htm" id="file_upload_form" method="post"
                              enctype="multipart/form-data">

                            <div class="popup_textarea">
                                <div class="file_upload">
                                    <input type="file" class="file_upload_input" name="data" id="upload_file" multiple
                                           onchange="upload_photo()">
                                </div>
                                <div id="write_message" class="write_message" contenteditable="true">

                                <span class="message_placeholder">
                                     <s:text name="wallet.login.write.message">Write message</s:text>
                                </span>

                                </div>
                            </div>
                            <div style="clear: both"></div>
                        </form>
                    </div>
                </div>

            </div>
        </div>
    </div>
    <div style="clear: both"></div>
</div>

<div class="container-fliuid popup_cont_transfer">
    <div class="container ">

            <div class="popup_main_transfer">
                <div class="popup_sm_header_parent">
                    <div class="popup_sm_header_transfer">
                        <div class="transfer_fee">
                            <s:text name="wallet.login.send.money.transfer.fee">Transfer Fee</s:text>
                        </div>
                        <div>
                            <span class="transffer_back_img_div" onclick="close_send_money_transfer_popup()()">
                                <img src="<%=request.getContextPath()%>/img/wallet/icon/send_money_popup_arrow.png"
                                     alt="arrow icon"/>
                            </span>
                              <span  onclick="close_send_money_transfer_popup()">
                                 <s:text name="wallet.general.Back">Back</s:text>
                            </span>
                            <span onclick="close_send_money_transfer_popup_only()">
                                <s:text name="wallet.general.button.Cancel">Cancel</s:text>

                            </span>
                        </div>
                        <div>
                            <span class="make_transfer">
                                <s:text name="wallet.login.send.money.transfer">Transfer</s:text>
                            </span>
                            <span  class="transffer_next_img_div">
                                <img src="<%=request.getContextPath()%>/img/wallet/icon/arrow_icon2.png"
                                     alt="envelop icon"/>
                            </span>
                        </div>

                    </div>
                </div>

                <div class="popup_transfer_info">

                    <%--first row--%>
                    <div class="row">
                        <div class="act-exc">
                            <span class="act-res-1 ">
                                <%--1 AMD = 0.0020636USD--%>
                            </span>
                        </div>
                        <div class="cntr-res-n amount_to_tranfer_exchanged">
                        <span class="trt-1">
                            <%--6546 USD = 3172126.2 AMD--%>
                        </span>
                        </div>



                        <div class="act-exc">
                            <span class="act-res ">
                                <%--1 AMD = 0.0020636USD--%>
                            </span>
                        </div>
                        <div class="cntr-res-n amount_to_tranfer_exchanged">
                        <span class="trt">
                            <%--6546 USD = 3172126.2 AMD--%>
                        </span>
                        </div>
                        <div id="check_payment_fee_msg_box" class="popup_info_div"></div>
                        <div class="cntr-res ">
                            <span class="ttl col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                <s:text name="wallet.exchange.label.Transfer.Total.debit">Total debit(subtracted from your Wallet):</s:text>
                            </span>
                            <span class="ttr  tot-tr col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                <%--3256929.5 AMD--%>
                            </span>
                        </div>
                        <div style="clear: both"></div>
                        <div class="cntr-res">
                            <span class="ttl col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                <s:text name="wallet.exchange.label.Transfer">Transfer:</s:text>
                            </span>
                            <span class="ttr trans col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                <%--3172126.2 AMD--%>
                            </span>
                        </div>
                        <div class="cntr-res">
                            <span class="ttl col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                <s:text name="wallet.exchange.label.Transfer.cost">Transfer cost:</s:text>
                            </span>
                            <span class="ttr  trans_cost col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                <%--84803.25 AMD--%>
                            </span>
                        </div>
                    </div>


                    <div class="row  det-des">

                        <div class="det-des-1 ">
                            <div class="det-tem ">
                            <span class="ttl col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                 <s:text name="wallet.exchange.label.Transfer.fee">Transfer fee:</s:text>
                            </span>
                            <span class="ttr  trans_fee col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                <%--72688.5 AMD--%>
                            </span>
                            </div>
                            <div class="det-tem ">
                            <span class="ttl col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                 <s:text name="wallet.exchange.label.Transfer.exchange.fee">Transfer exchange fee:</s:text>
                            </span>
                            <span class="ttr trans-exchange col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                <%--0 AMD--%>
                            </span>
                            </div>
                            <div class="det-tem ">
                            <span class="ttl col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                 <s:text name="wallet.exchange.label.Exchange.fee">Exchange fee:</s:text>
                            </span>
                            <span class="ttr exchange_fee col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                <%--12114.75 AMD--%>
                            </span>
                            </div>
                            <div style="clear: both"></div>
                        </div>


                    </div>
                    <div class="row view_rate ">
                        <div class="show_details">
                            <div class="pdes">
                            <span class="deskcTitle ttl col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                <s:text name="wallet.exchange.label.Transfer.fee.details">Transfer fee details:</s:text>
                            </span>
                            <span class="deskc ttr tfd col-lg-6 col-md-6 col-sm-6 col-xs-12">
                                <%--150 USD / 0.0020636 AMD = 72688.5 AMD--%>
                            </span>
                            </div>

                        </div>

                        <div class="details_button">
                            <span>
                                <s:text name="wallet.send.money.view.rates">View rate</s:text>
                            </span>
                        </div>
                    </div>
                </div>
            </div>

    </div>
</div>
