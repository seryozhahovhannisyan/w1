<%@ page import="com.connectto.general.model.Partition" %>
<%@ page import="com.connectto.general.model.lcp.PartitionLCP" %>
<%@ page import="com.connectto.general.util.ConstantGeneral" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="show_pending_transaction.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/wallet/wallet_pending_transaction.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/wallet/block_user.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/wallet/transaction_info.css"  type="text/css">
<link href="<%=request.getContextPath()%>/css/general/datepicker_bootstrap.css" rel="stylesheet">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/wallet/wallet_block_user.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/general/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/wallet/wallet_transaction_info.js"></script>
<%
    Partition partition = (Partition) session.getAttribute(ConstantGeneral.SESSION_URL_PARTITION);
    String partitionDns = PartitionLCP.getDNS(partition.getId());
    Long transactionId = (Long)request.getAttribute("transactionId");
%>
<script type="text/javascript">
var Logger = new GENERAL_API.ACTION.LOGS;

var src_completed_icon = "<%=request.getContextPath()%>/img/wallet/icon/pending_transaction.png";
var src_completed_minus = "<%=request.getContextPath()%>/img/wallet/icon/wallet_minus_icon.png";
var msg = '<s:text name="wallet.data.not.found">The data not found</s:text>';
var title_mek = "<s:text name='wallet.general.button.Submit'>Submit</s:text>";

$(document).ready(function () {
    var types = [
        {id: 0, name: 'all', class_: 'show_both', value: '<s:text name="wallet.transaction.show.both">Show All</s:text>'},
        {id: 1, name: 'all', class_: 'show_only_received', value: '<s:text name="wallet.transaction.show.only.received">Show only Received</s:text>'},
        {id: 2, name: 'all', class_: 'show_only_sended', value: '<s:text name="wallet.transaction.show.only.sent">Show only Sent</s:text>'}
    ];
    $('.choose_transaction_type_parent').html('');
    $.each(types, function (i, item) {
        var div = '<div class="' + item.class_ + '">' +
                '<div data-id="' + item.id + '">' + item.value + '</div>' +
                '</div>';
        $('.choose_transaction_type_parent').append(div);
    });
    search_user();
    selected_nav('.pending_envelop_parent_li');
    call_pending_transaction();
    date_picker();
    search_row_design();
    search_by_amount();
    search_by_type();
    show_dropdown_type_search();
    sort_arrow_img();
    show_block_user();
    dragable_divs(".popup_cont");
    dragable_divs(".range_parent_div");

//    block_user_call();

    <%
           if(transactionId != null && transactionId > 0 ){
       %>
            show_pending_transaction(<%=transactionId%>);
    <%
        }
    %>
});
function datepickerCancelSubmit() {
    var con = '<div class="datepicker_header">' +
            '<div class="dp_cancel_div"><s:text name="wallet.transaction.button.Cancel">Cancel</s:text></div>' +
            '<div class="dp_submit_div"><s:text name="wallet.general.button.Submit">Submit</s:text></div>' +
            '</div>';
    $(".datepicker-days").before(con);
}

var array_ids = [];
var array_name_surnames = [];
var array_user_photo_paths = [];
var hide_next_prev_buttons = '';
var hide_prev_button       = '';
var hide_next_button       = '';
var params_next_prev = {
    array_ids : undefined,
    transaction_id        : undefined,
    array_name_surnames : undefined,
    array_user_photo_paths : undefined


};
function update_params(array_ids, transaction_id, array_user_photo_paths, array_name_surnames) {
    params_next_prev.array_ids = array_ids;
    params_next_prev.transaction_id = transaction_id;
    params_next_prev.array_name_surnames = array_name_surnames;
    params_next_prev.array_user_photo_paths = array_user_photo_paths;

}

function logic_show_pending_transaction_popup_previous( ){
    hide_next_button = false;
    for (var i = 0; i < params_next_prev.array_ids.length; i++) {
        if (params_next_prev.array_ids[i] == params_next_prev.transaction_id && params_next_prev.array_ids.length >= 2) {

            params_next_prev.transaction_id = params_next_prev.array_ids[i-1] ;
            var previews_id = params_next_prev.array_ids[i-1] ;
            var previews_name_surname = params_next_prev.array_name_surnames[i-1];
            var previews_user_photo_path = params_next_prev.array_user_photo_paths[i-1];
            if (params_next_prev.array_ids[i-2] == undefined && previews_id != undefined) {
                hide_prev_button = true;
                hide_next_prev_buttons = false;
                show_pending_transaction_popup_previous(previews_id, previews_user_photo_path, previews_name_surname);
               // $('.arrow-left-payment-popup').hide();
            } else if (previews_id == undefined) {
                hide_prev_button = true;

            } else {
                hide_prev_button = false;
                show_pending_transaction_popup_previous(previews_id, previews_user_photo_path, previews_name_surname);

            }
        }
    }
    return params_next_prev.transaction_id ;
}

function logic_show_pending_transaction_popup_next(){
    hide_prev_button = false;
    for (var i = params_next_prev.array_ids.length; i >= 0; i--) {
        if (params_next_prev.array_ids[i] == params_next_prev.transaction_id && params_next_prev.array_ids.length >= 2) {
            params_next_prev.transaction_id = params_next_prev.array_ids[i+1] ;
            var next_id = params_next_prev.array_ids[i+1] ;
            var next_name_surname = params_next_prev.array_name_surnames[i+1];
            var next_user_photo_path = params_next_prev.array_user_photo_paths[i+1];
            if (params_next_prev.array_ids[i+2] == undefined && next_id != undefined) {
                hide_next_button = true;
                hide_next_prev_buttons = false;
                show_pending_transaction_popup_next(next_id, next_user_photo_path, next_name_surname);

            } else if (next_id == undefined) {
                hide_next_button = true;

            } else {
                hide_next_button = false;
                show_pending_transaction_popup_next(next_id, next_user_photo_path, next_name_surname);
            }
        }
    }
    return params_next_prev.transaction_id ;
}

function show_pending_transaction_popup_previous(id, user_photo_path, name_surname) {
    clsoe_info_popup();
    show_pending_transaction(id, user_photo_path, name_surname);
}
function show_pending_transaction_popup_next(id, user_photo_path, name_surname) {
    clsoe_info_popup();
    show_pending_transaction(id, user_photo_path, name_surname);
}

function search_user(){
    $(".search").click(function () {
        call_pending_transaction();
    });
    $(".search_user_input").keydown(function (e) {
        if (e.which == 13) {
            call_pending_transaction();
        }
    });
}
function unite_criteria() {

    var params = {};
        if ($(".multi_month").css("display") == "none") {
            params.date_less = $(".one_end_date_date").data("search_date_less");
            params.date_great = $(".one_start_date_date").data("search_date_great");
        }
        else {
            params.date_less = $(".end_date_date").data("search_date_less");
            params.date_great = $(".start_date_date").data("search_date_great");
        }
        params.searchLike = $(".search_user_input").val();
        params.current_page = $(".search_result").attr("data-current_page");

        params.order_type = $('.arrow_div span:nth-child(1)').html();
        params.byCurrency = $('.popup_select_tr option:selected').val();
        params.rangeAmountGreat = $(".amount_charge span:nth-child(1)").text();
        params.rangeAmountLess = $(".amount_charge span:nth-child(3)").text();
        params.transactionType = $(".choose_type").attr('data-id');
    return params;
}
function call_pending_transaction() {
    var params = unite_criteria();
    params.current_page = 0;
    load_pending_transaction(params);
}
function load_pending_transaction(params, clear_array_for_popup) {
    loader_show();
    $('#error_msg').remove();

    if (clear_array_for_popup !== false) {
        array_ids = [];
        array_name_surnames = [];
        array_user_photo_paths = [];
    }
    var current_page = params.current_page != undefined ? params.current_page : '-1';
    var order_type =   params.order_type != undefined ? params.order_type : 'asc';
    var date_less = params.date_less != undefined ? params.date_less : "";
    var date_great = params.date_great != undefined ? params.date_great : "";
    var byCurrency = params.byCurrency != undefined ? params.byCurrency : "";
    var rangeAmountGreat = params.rangeAmountGreat != undefined ? params.rangeAmountGreat : "";
    var rangeAmountLess = params.rangeAmountLess != undefined ? params.rangeAmountLess : "";
    var searchLike =  params.searchLike != undefined ? params.searchLike : "";
    var transactionType = params.transactionType != undefined ? params.transactionType : "0";
    var data = {
        currentPage: current_page,
        orderType: order_type,
        rangeDateLess: date_less,
        rangeDateGreat: date_great,
        byCurrency: byCurrency,
        rangeAmountGreat: rangeAmountGreat,
        rangeAmountLess: rangeAmountLess,
        searchLike: searchLike,
        transactionType: transactionType
    };
    var params_ = {};
    params_.async = true;
    params_.data = data;
    WALLET.load_pending_transactions(params_, pending_load_available_currencies_callback, wallet_error_handler);
}
function reject_send(id,tabindex) {
    for (var i = 0 ; i <= array_ids.length; i ++) {
        if (array_ids[i] == id) {
            array_ids.splice(i, 1);
            array_name_surnames.splice(i, 1);
            array_user_photo_paths.splice(i, 1);
        }
    }
    var params = {};
    var data = {};
    params.async =  true;
    data.transactionId = id;
    data.tabindex = tabindex;
    params.data = data;
    WALLET.send_money_reject(params, pending_reject_send_money_callback, wallet_error_handler);
}
function reject_requset(id,tabindex) {
    var params = {};
    var data = {};
    params.async =  true;
    data.transactionId = id;
    data.tabindex = tabindex;
    params.data = data;
    WALLET.request_transaction_reject(params, pending_reject_request_money_callback, wallet_error_handler);
}
function  allow_request(id,tabindex) {
    var params = {};
    var data = {};
    params.async =  true;
    data.transactionId = id;
    data.tabindex = tabindex;
    params.data = data;
    WALLET.send_money_approve(params, pending_allow_send_money_callback, wallet_error_handler);
}
function allow_send(id,tabindex) {
    var params = {};
    var data = {};
    params.async =  true;
    data.transactionId = id;
    data.tabindex = tabindex;
    params.data = data;
    WALLET.request_transaction_approve(params, pending_allow_request_money_callback, wallet_error_handler);
}
function close_confirm_panel() {
    $("#confirm_panel").remove();
}
function date_picker() {

    $("body").delegate(".dp_cancel_div", "click", function () {
        $(".datepicker").hide();
        $('.datepicker_one').datepicker('update', "");
        $(".one_start_date_line").hide();
        $(".one_end_date_date").hide();
        $(".multi_month ").hide();
        $(".one_month").show();
        $(".one_start_date_date ").show().html(new Date().getDate());
        $(".one_end_mouth ").show().html(datepicker_obj.months[new Date().getMonth()]);
        $(".one_end_year ").show().html(new Date().getFullYear());
        search_row_design();

    });
    $("body").delegate(".dp_submit_div", "click", function () {
        call_pending_transaction()
        $('.datepicker_one').datepicker('update', "");
        $('.datepicker_one').datepicker('hide');
        arr = [];
        arr_month = [];
        arr_month_int = [];
        arr_year = [];
        arr_input_value = [];

    });
    var language_key = "<s:property value="getSessionLanguage().getKey()"/>";


    $.fn.datepicker.dates[language_key] = {
        days: ["<s:text name='calendar.weeks.Sunday'>Sunday</s:text>", "<s:text name='calendar.weeks.Monday'>Monday</s:text>", "<s:text name='calendar.weeks.Tuesday'>Tuesday</s:text>", "<s:text name='calendar.weeks.Wednesday'>Wednesday</s:text>", "<s:text name='calendar.weeks.Thursday'>Thursday</s:text>", "<s:text name='calendar.weeks.Friday'>Friday</s:text>", "<s:text name='calendar.weeks.Saturday'>Saturday</s:text>"],
        daysShort: ["<s:text name='calendar.weeks.Sun'>Sun</s:text>", "<s:text name='calendar.weeks.Mon'>Mon</s:text>", "<s:text name='calendar.weeks.Tue'>Tue</s:text>", "<s:text name='calendar.weeks.Wed'>Wed</s:text>", "<s:text name='calendar.weeks.Thu'>Thu</s:text>", "<s:text name='calendar.weeks.Fri'>Fri</s:text>", "<s:text name='calendar.weeks.Sat'>Sat</s:text>"],
        daysMin: ["Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"],
        months: ["<s:text name='calendar.months.january'>January</s:text>", "<s:text name='calendar.months.february'>February</s:text>", "<s:text name='calendar.months.march'>March</s:text>", "<s:text name='calendar.months.april'>April</s:text>", "<s:text name='calendar.months.may'>May</s:text>", "<s:text name='calendar.months.june'>June</s:text>", "<s:text name='calendar.months.july'>July</s:text>", "<s:text name='calendar.months.august'>August</s:text>", "<s:text name='calendar.months.september'>September</s:text>", "<s:text name='calendar.months.october'>October</s:text>", "<s:text name='calendar.months.november'>November</s:text>", "<s:text name='calendar.months.december'>December</s:text>"],
        monthsShort: ["<s:text name='calendar.months.Jan'>Jan</s:text>", "<s:text name='calendar.months.Feb'>Feb</s:text>", "<s:text name='calendar.months.Mar'>Mar</s:text>", "<s:text name='calendar.months.Apr'>Apr</s:text>", "<s:text name='calendar.months.May'>May</s:text>", "<s:text name='calendar.months.Jun'>Jun</s:text>", "<s:text name='calendar.months.Jul'>Jul</s:text>", "<s:text name='calendar.months.Aug'>Aug</s:text>", "<s:text name='calendar.months.Sep'>Sep</s:text>", "<s:text name='calendar.months.Oct'>Oct</s:text>", "<s:text name='calendar.months.Nov'>Nov</s:text>", "<s:text name='calendar.months.Dec'>Dec</s:text>"],
        today: "Today",
        clear: "Clear",
        format: "dd-mm-yyyy",
        titleFormat: "MM yyyy", /* Leverages same syntax as 'format' */
        weekStart: 0
    };
    var datepicker_obj = $.fn.datepicker.dates[language_key];

    $('.datepicker_one').datepicker({
        multidate: 2,
        todayHighlight: true,
        multidateSeparator: ",   ",
        format: "dd-mm-yyyy",
        endDate: "0d",
        language: language_key

    });
    $('.datepicker_one').datepicker().on("show", function (e) {
        dragable_divs(".datepicker");
        datepickerCancelSubmit();
    });
    var arr = [];
    var arr_month = [];
    var arr_month_int = [];
    var arr_year = [];
    var real_month = datepicker_obj.months[new Date().getMonth()];
    var real_day = new Date().getDate();
    var counter;
    var arr_input_value;
    $('.datepicker_one').on("changeDate", function (e) {

        var currmonth = datepicker_obj.months[$('.datepicker_one').datepicker('getDate').getMonth()];
        var currmonthint = $('.datepicker_one').datepicker('getDate').getMonth();
        var curryear = $('.datepicker_one').datepicker('getDate').getFullYear();
        var currday = $('.datepicker_one').datepicker('getDate').getDate();
        var currinputval = $('.multi_input').val();


        console.log("curday creat", currday);
        console.log("multi_input", $('.multi_input').val());
        console.log("counter", counter);
        console.log("e", e);


        /*get input value*/
        arr_input_value = $('.multi_input').val().split(",");
        console.log("arr_input_value", arr_input_value);


        /*get yaer*/
        if (arr_year.length > 1) {
            arr_year.shift()
        }
        arr_year.push(curryear);
        console.log("arr_year", arr_year);

        /*get month*/
        if (arr_month.length > 1) {
            arr_month.shift()
        }
        arr_month.push(currmonth);
        console.log("arr_month", arr_month);

        /*get month int*/
        if (arr_month_int.length > 1) {
            arr_month_int.shift()
        }
        arr_month_int.push(currmonthint);
        console.log("arr_month_int", arr_month_int);


        /*get day*/
        if (arr.length > 1) {
            arr.shift()
        }
        arr.push(currday);
        console.log("arr_day", arr);

        if (arr_year[0] == undefined || arr_month[0] == undefined || arr_year[1] == undefined || arr_month[1] == undefined) {
            $(".one_month").show();
            $(".multi_month").hide();
            $(".one_start_date_line").hide();
            $(".one_end_date_date").hide();
            if (isNaN(currday)) {
                $(".one_start_date_date").html(arr[0]);
            }
            else {
                $(".one_start_date_date").html(currday);
            }
            $(".one_end_mouth").html(currmonth);
            $(".one_end_year").html(curryear);
            $(".one_end_date_date").data("search_date_less", arr_input_value[0]);
        }
        else {
            if (arr_year[0] != arr_year[1] || arr_month[0] != arr_month[1]) {
                $(".one_month").hide();
                $(".multi_month").show();
                if (arr_year[0] > arr_year[1]) {
                    $(".start_year").html(arr_year[1]);
                    $(".start_mouth").html(arr_month[1]);
                    $(".start_date_date").html(arr[1]);
                    $(".end_year").html(arr_year[0]);
                    $(".end_mouth").html(arr_month[0]);
                    $(".end_date_date").html(arr[0]);

                    $(".start_date_date").data("search_date_great", arr_input_value[1]);
                    $(".end_date_date").data("search_date_less", arr_input_value[0]);
                }
                else if (arr_month_int[0] > arr_month_int[1] && arr_year[0] == arr_year[1]) {
                    $(".start_year").html(arr_year[1]);
                    $(".start_mouth").html(arr_month[1]);
                    $(".start_date_date").html(arr[1]);
                    $(".end_year").html(arr_year[0]);
                    $(".end_mouth").html(arr_month[0]);
                    $(".end_date_date").html(arr[0]);

                    $(".start_date_date").data("search_date_great", arr_input_value[1]);
                    $(".end_date_date").data("search_date_less", arr_input_value[0]);

                }
                else {
                    $(".start_year").html(arr_year[0]);
                    $(".start_mouth").html(arr_month[0]);
                    $(".start_date_date").html(arr[0]);
                    $(".end_year").html(arr_year[1]);
                    $(".end_mouth").html(arr_month[1]);
                    $(".end_date_date").html(arr[1]);

                    $(".start_date_date").data("search_date_great", arr_input_value[0]);
                    $(".end_date_date").data("search_date_less", arr_input_value[1]);

                }
            }
            if (arr_year[0] == arr_year[1] && arr_month[0] == arr_month[1]) {
                if (currday > arr[0]) {
                    $(".one_start_date_line").show();
                    $(".one_end_date_date").show().html(currday);
                    $(".one_start_date_date").html(arr[0]);

                    $(".one_start_date_date").data("search_date_great", arr_input_value[0]);
                    $(".one_end_date_date").data("search_date_less", arr_input_value[1]);
                }
                else if (currday == arr[0]) {
                    $(".one_start_date_line").hide();
                    $(".one_end_date_date").hide();
                    $(".one_start_date_date").html(currday);

//                        $(".one_start_date_date").data("search_date_great",arr_input_value[0]);
                    $(".one_end_date_date").data("search_date_less", arr_input_value[0]);
                }
                else {
                    $(".one_start_date_line").show();
                    $(".one_end_date_date").show().html(arr[0]);
                    $(".one_start_date_date").html(currday);

                    $(".one_start_date_date").data("search_date_great", arr_input_value[1]);
                    $(".one_end_date_date").data("search_date_less", arr_input_value[0]);
                }
                $(".one_end_mouth").html(currmonth);
                $(".one_end_year").html(curryear);
            }

        }

        if (e.dates.length > 1) {
            arr = [];
            arr_month = [];
            arr_year = [];
            console.log("arr", arr, "arr_month", arr_month, "arr_years", arr_year)
        }
        search_row_design();
    });

    $(".one_start_date_date ").html(new Date().getDate());
    $(".one_end_mouth ").html(datepicker_obj.months[new Date().getMonth()]);
    $(".one_end_year ").html(new Date().getFullYear());
    $(".one_start_date_line").hide();
    $(".one_end_date_date").hide();

}
function search_by_amount() {
    $(".amount_text").click(function () {
        $(".range_parent_div").show();
        $(".choose_transaction_type_parent").hide();
        setTimeout(function () {
            close_any_popup(".range_parent_div")

        }, 100);
        $(document).off("click");


    });
    $(".cancel_").click(function () {
        $(".range_parent_div").hide()
    });
    var currency, first_val, second_val;
    $(".amount_charge span:last-child").html("USD");
    $('.popup_select_tr').on('change', function () {
        var currency = $(".popup_select_tr option:selected").text();
        $(".amount_charge span:last-child").html(currency);
    });
    $(".submit_").click(function () {
        first_val = Number($(".amount_great input").val());
        second_val = Number($(".amount_less input").val());
              if (first_val > second_val) {
            $(".amount_charge span:first-child").html(second_val);
            $(".amount_charge span:nth-child(3)").html(first_val);
            $(".range_parent_div").hide();
        }
        else {
            $(".amount_charge span:first-child").html(first_val);
            $(".amount_charge span:nth-child(3)").html(second_val);
            $(".range_parent_div").hide();
        }
        $(".amount_charge span:last-child").html(currency);
//            $(".amount_great input").val("");
//            $(".amount_less input").val("");
        call_pending_transaction();
    })
}
function search_row_design() {
    var date_search_height = $(".date_parent_div").outerHeight();
    var margin_center_height1 = ($(".date_parent_div").height() - $(".div_margin_height1").outerHeight()) / 2;
    var margin_center_height2 = ($(".date_parent_div").height() - $(".div_margin_height2").outerHeight()) / 2;
    var margin_center_height3 = ($(".date_parent_div").height() - $(".div_margin_height3").outerHeight()) / 2;
    $(".choose_transaction_type_parent").css("top", date_search_height + "px");
    $(".choose_disputation_type_parent").css("top", date_search_height + "px");
    $(".search_checkbox").css("height", date_search_height);
    $(".div_margin_height1").css("margin-top", margin_center_height1 + "px");
    $(".div_margin_height2").css("margin-top", margin_center_height2 + "px");
    $(".div_margin_height3").css("margin-top", margin_center_height3 + "px");


}
function show_dropdown_type_search() {
    $(".search_type_click_div").click(function () {
        $(".choose_transaction_type_parent").toggle();
        $(".range_parent_div").hide();
        $(".choose_disputation_type_parent").hide();
        setTimeout(function () {
            close_any_popup(".choose_transaction_type_parent")
        }, 100);
        $(document).off("click")
    })
}
function search_by_type() {
    $(".choose_transaction_type_parent").click(function (e) {
        if (!e) e = window.event;
        if ($(e.target).closest(".choose_transaction_type_parent")) {
            var type_text = $(e.target).text();
            var id = $(e.target).attr('data-id');
            var type_color = $(e.target).css("color");
            $(".choose_type").html(type_text).css("color", type_color).attr('data-id', id);
            $(".choose_transaction_type_parent").hide();
        }
    });
}
function scroll_paging() {
    $('.search_result').scroll(function () {
        if ($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {
            var data_action = $(this).data("action");
            var data_current_page = $(this).data("current_page");
            var data_is_last = $(this).data("is_last");
            var data_order_type = $(this).attr("data-order_type");
            console.log("data_action",data_action,"data_current_page",data_current_page,"data_is_last",data_is_last,"data_order_type",data_order_type);
            if (data_is_last == 0 || data_is_last == null) {
                if (data_action == "load_pending_transaction") {
                    var params = unite_criteria();
                    params.current_page = data_current_page;
                    params.order_type = data_order_type;
                    var clear_array_for_popup = false;
                    load_pending_transaction(params, clear_array_for_popup);
                }
            }
            $(this).off("scroll")
        }
    });
}

function show_block_user() {
    $("body").delegate(".name_surname_a","mouseover", function (){
        $(this).closest(".name_surname_a_div").find(".block_img_div").show(500);
    }).delegate(".name_surname_a_div","mouseleave", function(){
        $(this).closest(".name_surname_a_div").find(".block_img_div").hide(500)
    })

}

function sort_arrow_img() {
    $(".up").click(function () {
        $('.arrow_div img').attr('src', '<%=request.getContextPath()%>/img/wallet/icon/arrow_up_icon.png');
        $('.arrow_div span:nth-child(1)').html("desc");
        call_pending_transaction()
    });
    $(".down").click(function () {
        $('.arrow_div img').attr('src', '<%=request.getContextPath()%>/img/wallet/icon/arrow_down_icon.png');
        $('.arrow_div span:nth-child(1)').html("asc");
        call_pending_transaction()
    })
}

function ul_rezult_height(){
    var doc_height = Math.max(
            document.body.scrollHeight, document.documentElement.scrollHeight,
            document.body.offsetHeight, document.documentElement.offsetHeight,
            document.body.clientHeight, document.documentElement.clientHeight
    );
    var win_height = $(window).outerHeight();
    var person_panel_height = $(".person_parent").outerHeight(true);
    var footter_height = $(".footer").outerHeight(true);
    var user_search_height = $(".date_parent_div").outerHeight(true);
    var content = doc_height - person_panel_height - footter_height - user_search_height;
    $(".search_result").css("height",content+"px");
    console.log("doc_height_send", doc_height, "win_height_send", win_height,"person_panel_height",person_panel_height,"footter_height",footter_height,"content",content,"user_search_height",user_search_height);
}

    //CALLBACK
var pending_load_available_currencies_callback = function (data,current_page){
    var cont;
    var current_page_new = data.currentPage;
    var is_last = data.isLast;
    var order_type = data.orderType;
    console.log("is ast", is_last, "cyr page", current_page);
    if (current_page == null || current_page <  2) {
        $(".search_result").remove();
        cont = $("<ul id='pending_ul' class='port-menu-list pending_transaction search_result' data-action='load_pending_transaction' data-current_page='" + current_page_new + "' data-is_last='" + is_last + "' data-order_type='" + order_type + "' />");
        $("#pending_transaction_title").after(cont);
    } else {

        $(".search_result").data('current_page', current_page_new).data('is_last', is_last).data("action",'load_pending_transaction');
        cont = $(".search_result");
    }
    ul_rezult_height();
    var pendingTransactions = data.pendingTransactions;
    if (pendingTransactions != null && pendingTransactions.length > 0) {
        var partition_dns = "<%=partitionDns%>";
        var src_envelop;
        $.each(pendingTransactions, function (i, item) {
            var pending = item;
            if (pending != null) {
                var debited = pending.debited ? 'debited' : ' ';
                var credited = pending.credited ? 'credited' : ' ';
                var allow_delay = pending.allowDelay;
                var li = $('<li class="searched_item ' + debited + credited+' li' + ( i + 1 ) + '" tabindex="' + ( i + 1 ) + '" />').appendTo(cont);
                var tabindex = $(li).attr("tabindex");
                var user;
                var walletSetupId;
                if (pending.debited) {
                    user = pending.userDto;
                    walletSetupId = pending.walletSetupId;
                    src_envelop = "/img/wallet/icon/send_money.png";
                    src_completed_minus = "<%=request.getContextPath()%>/img/wallet/icon/wallet_minus_icon.png";
                } else if (pending.credited) {
                    user = pending.userDto;
                    walletSetupId = pending.walletSetupId;
                    src_envelop = "img/wallet/icon/requst_transaction.png";
                    src_completed_minus = "<%=request.getContextPath()%>/img/wallet/icon/wallet_plus_icon.png";
                } else {
                    JSLoggerAction(LOG_LEVEL.ERROR, JS.AJAX, "load_pending_transaction", "responseText=[incorrect incoming data]");
                    alert(1)
                }
                var div = $("<div class='parent_div'/>").appendTo(li);
                var src = '<s:property value="#session.session_user.partition.partitionServerUrl"/><s:property value="#session.session_user.partition.partitionLogoDirectory"/><s:property value="#session.session_user.partition.logoPath"/>';
                if (user != null) {
                    src = IMAGE_BASE_URL + user.id;
                    array_user_photo_paths.push(src);
                }
                var img_icon = $('<div class="icon_div"> <img src="' + src_completed_icon + '"/></div> ');//.appendTo(div);
                var img_minus_icon = $('<div class="icon_mius_div"> <img src="' + src_completed_minus + '"/></div> ');//.appendTo(div);
                img_icon.appendTo(div);
                img_minus_icon.appendTo(div);
                var transaction_id = pending.transactionId;

                array_ids.push(transaction_id);

                var ns = user != null ? user.name + " " + user.surname : partition_dns;

                array_name_surnames.push(ns);

                var show_pending_transaction ="show_pending_transaction('"+transaction_id+"', '"+src+"', '"+ns+"')"  ;
                var img = $('<div class="img_div"><img class="for_next_previews_buttons_img' + transaction_id + '" onclick="'+show_pending_transaction+'" src="' + src + '"/></div> ').appendTo(div);
                var id = user != null ? user.id : walletSetupId;
                var partitionId = user != null ? user.partitionId : 0;
                var ns = user != null ? user.name + " " + user.surname : partition_dns;
                if (!allow_delay) {
                    $('<button class="action_button  delay_button_pending"/>').text('<s:text name="wallet.transaction.button.Delay"/>')
                            .attr('onclick', 'reject_send(' + pending.transactionId+','+tabindex+')').appendTo(div);
                } else {
                    $('<button class="action_button  cancel_button_pending"/>').text('<s:text name="wallet.transaction.button.Cancel"/>')
                            .attr('onclick', 'reject_requset(' + pending.transactionId+','+tabindex+ ')').appendTo(div);
                    if (pending.credited) {
                        var func_allow = "allow_transaction.htm?pendingTransactionId=" + pending.transactionId;
                        /*todo*/
                        $('<button class="action_button  allow_button_pending"/>').text('<s:text name="wallet.transaction.button.Allow"/>')
                                .attr('onclick', 'allow_request(' + pending.transactionId+','+tabindex+')').appendTo(div);
                    }
                    if (pending.debited) {
                        var func_transfer = "transfer_transaction.htm?pendingTransactionId=" + pending.transactionId;
                        /*todo*/
                        $('<button class="action_button  transfer_button_pending"/>').text('<s:text name="wallet.transaction.button.Transfer"/>')
                                .attr('onclick', 'allow_send(' + pending.transactionId+','+tabindex+')').appendTo(div);
                    }
                }



                var action_date = pending.openedAt;
                var arr_date_time = action_date.split("T");
                $('<div class="name_surname_a_div"><div class="block_img_div" onclick="block_user_call(' + user.id +',\'' + ns + '\',\'' + src + '\')"><i class="fa fa-lock" aria-hidden="true"></i> <s:text name="wallet.general.wallet.block.user">Block user</s:text></div><a href="#" onclick="' + show_pending_transaction + '" class="name_surname_a for_next_previews_buttons'+transaction_id+ ' ">' + ns + '</a></div>').appendTo(div);
                $('<div class="envelop_parent_div">' +
                        '<div class="envelop_div">' +
                        '<img src="' + src_envelop + '" alt=""/>' +
                        '</div>' +
                        '<div class="date_div">' +
                        '<div class="text-center">' + arr_date_time[0] + '</div>' +
                        '<div class="text-center">' + arr_date_time[1] + '</div>' +
                        '</div>' +
                        '</div>').appendTo(div);
                $('<div class="amount_div">'+pending.price + " " + pending.priceCurrency+'</div>').appendTo(div);


                $('.for_next_previews_buttons' + transaction_id).click(function () {
                    if (array_ids[0] == transaction_id) {
                        hide_prev_button = true;
                    }
                    else {
                        hide_prev_button = false;
                    }
                    if (array_ids[array_ids.length - 1] == transaction_id) {
                        hide_next_button = true;
                    }
                    else {
                        hide_next_button = false;
                    }
                    update_params(array_ids, transaction_id, array_user_photo_paths, array_name_surnames );



                });
                $('.for_next_previews_buttons_img' + transaction_id).click(function () {
                    if (array_ids[0] == transaction_id) {
                        hide_prev_button = true;
                    }
                    else {
                        hide_prev_button = false;
                    }
                    if (array_ids[array_ids.length - 1] == transaction_id) {
                        hide_next_button = true;
                    }
                    else {
                        hide_next_button = false;
                    }
                    update_params(array_ids, transaction_id, array_user_photo_paths, array_name_surnames );



                });

            }

        });

        scroll_paging();
        footerPlace();
        loader_hide();


    }

    loader_hide();
}

function show_pending_transaction(transaction_id,src,ns){
    console.log("src",src);
    var close = "<s:text name="button.close">close</s:text>";
    var img = '<img alt="avatar" src="'+src+'"/>';
    var transfer_fee = '<s:text name="wallet.login.send.money.transfer.fee">Transfer Fee</s:text>';
    var total_debit = '<s:text name="wallet.exchange.label.Transfer.Total.debit">Total debit (subtracted from your Wallet):</s:text>';
    var transfer = '<s:text name="wallet.exchange.label.Transfer">Transfer:</s:text>';
    var transfer_cost = '<s:text name="wallet.exchange.label.Transfer.cost">Transfer cost:</s:text>';
    var trans_exch_fee = '<s:text name="wallet.exchange.label.Transfer.exchange.fee">Transfer exchange fee:</s:text>';
    var exchange_fee = '<s:text name="wallet.exchange.label.Exchange.fee">Exchange fee:</s:text>';
    var trans_fee_det = '<s:text name="wallet.exchange.label.Transfer.fee.details">Transfer fee details:</s:text>';
    var attach_file = '<s:text name="wallet.payment.label.Attach">Attach file</s:text>';
    var print = '<s:text name="wallet.exchange.button.Print">Print</s:text>';
    var download = '<s:text name="wallet.exchange.button.Download">Download</s:text>';
    var email = '<s:text name="label.user.email">email</s:text>';
    var send = '<s:text name="tsm.become.company.request.send">send</s:text>';
    var success_message = '<s:text name="wallet.transaction.info.email.success">Email successfully sent</s:text>';
    var error_message = '<s:text name="errors.internal.server">Please, try again. An error occurred on the server</s:text>';
    var url_after_close ="pending-transaction.htm";
    loader_show();
    $.ajax({
        url: 'show-pending-transaction.htm',
        type: "post",
        dataType: "json",
        data : {
            transactionId : transaction_id
        },
        success: function (data) {
            if (data.responseDto != null && data.responseDto.status == "SUCCESS") {
                console.log('data', data.preview);
                var fromTotalPrice = data.preview.fromTotalPrice != null ? data.preview.fromTotalPrice : "-";
                var fromTotalPriceCurrencyType = data.preview.fromTotalPriceCurrencyType != null ? data.preview.fromTotalPriceCurrencyType : "-";
                var productAmount = data.preview.productAmount != null ? data.preview.productAmount : "-";
                var productCurrencyType = data.preview.productCurrencyType != null ? data.preview.productCurrencyType : "-";
                var totalAmountTaxPrice = data.preview.fromTransactionProcess.tax.totalAmountTaxPrice != null ? data.preview.fromTransactionProcess.tax.totalAmountTaxPrice : "-";
                var transferTaxPrice = data.preview.fromTransactionProcess.tax.transferTaxPrice != null ? data.preview.fromTransactionProcess.tax.transferTaxPrice : "-";
                var transferExchangeTaxPrice = data.preview.fromTransactionProcess.tax.transferExchangeTaxPrice != null ? data.preview.fromTransactionProcess.tax.transferExchangeTaxPrice : "-";
                var attached_file_arr = data.preview.transactionDatas != null ? data.preview.transactionDatas : "-";


                var params = {
                    close: close,
                    src: src,
                    ns: ns,
                    message: data.preview.message,
                    transfer_fee: transfer_fee,
                    total_debit: total_debit,
                    transfer: transfer,
                    transfer_cost: transfer_cost,
                    trans_exch_fee: trans_exch_fee,
                    exchange_fee: exchange_fee,
                    trans_fee_det: trans_fee_det,
                    fromTotalPrice: fromTotalPrice,
                    fromTotalPriceCurrencyType: fromTotalPriceCurrencyType,
                    productAmount: productAmount,
                    productCurrencyType: productCurrencyType,
                    totalAmountTaxPrice: totalAmountTaxPrice,
                    transferTaxPrice: transferTaxPrice,
                    transferExchangeTaxPrice: transferExchangeTaxPrice,
                    img: img,
                    attach_file: attach_file,
                    print: print,
                    download: download,
                    email: email,
                    transaction_id: transaction_id,
                    send: send,
                    success_message:success_message,
                    error_message:error_message,
                    url_after_close:url_after_close

                };
                transaction_info(params);
                console.log("attached_file_arr", attached_file_arr);
                if (attached_file_arr.length > 0) {
                    $(".attached_files_par_div").show();
                    $.each(attached_file_arr, function (key, value) {
                        $(".attached_files").append("<div><a  a href ='/transaction_data_download.htm?dataFileName="+value.fileName+"&transactionId=" + transaction_id+"'  style='display: block;cursor: pointer;'><span>" + value.fileName + "</span><span style='margin: 0 10px;'>" + value.size + "kb</span><span>" + value.transactionId + "</span></a></div>")
                        console.log(value.contentType);
                    });
                    loader_hide();
                }
                else {
                    $(".attached_files_par_div").hide();
                    loader_hide();
                }
            }
            else {

                var handle = null;
                if (data != null ) {
                    handle = {status: data.status, responseText: data.messages};
                    loader_hide();
                } else {
                    handle = {status: 0, responseText: 'empty data'};
                    loader_hide();
                }
                console.log(handle);
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {

            var handle = {status: xhr.status, responseText: xhr.responseText};
            console.log(handle);
            loader_hide();
        }
    });
}

var pending_reject_send_money_callback = function (data, params) {
    var class_name =".li"+params.data.tabindex+""
    loader_hide();
    $(class_name).animate({
        "margin-left": "3000px"
    }, 1500,function (){
        $(this).remove();
    });

}
var pending_reject_request_money_callback = function (data, params) {
    var class_name =".li"+params.data.tabindex+""
    loader_hide();
    $(class_name).animate({
        "margin-left": "3000px"
    }, 1500,function (){
        $(this).remove();
    });

}
var pending_allow_send_money_callback = function (data, params) {
    var class_name =".li"+params.data.tabindex+""
    loader_hide();
    $(class_name).animate({
        "margin-left": "3000px"
    }, 1500,function (){
        $(this).remove();
    });

}
var pending_allow_request_money_callback = function (data, params) {
    var class_name =".li"+params.data.tabindex+""
    loader_hide();
    $(class_name).animate({
        "margin-left": "3000px"
    }, 1500,function (){
        $(this).remove();
    });

}

</script>


<div class="complited_transaction container-fluid">
<div class="row main">
<div id="action_panel1" class="action_panel">

<div class="row" style="border-bottom: 1px solid #555965">
    <div id="search_user" class="user_search_div div_margin_height1">
        <%--<div class="input_before_text"><s:text--%>
                <%--name="wallet.pages.make.search.before.text">Please choose precipitant</s:text></div>--%>
        <div class="search_parent_div">
            <div class="search"></div>
            <input id="search_user_text" type="search" class="search_user_input" name="searchLike"
                   placeholder="<s:text name="wallet.pages.make.label.search_by">Search Friend or Company</s:text>"
                    >
        </div>
        <div class="arrow_div">
            <span style="display: none">desc</span>

            <div class="up"></div>
            <div class="down"></div>
            <img src="<%=request.getContextPath()%>/img/wallet/icon/arrow_up_icon.png" alt="arrow icon"/>
        </div>
        <div class="sort_div">
            <s:text name="wallet.action.search.result.sort">Sort</s:text>
        </div>
    </div>
    <div class="date_parent_div">
        <div class="  datepicker_one  date">
            <div class="date_text   add-on">
                <s:text name="wallet.transaction.date">Date</s:text>
            </div>
            <input class="multi_input" type="hidden"/>

            <div class="multi_month">
                <div class="start_date_parent">
                    <div class="start_date_date  add-on"></div>
                    <div class="start_mouth"></div>
                    <div class="start_year"></div>
                </div>
                <div style="clear: both"></div>
                <div class="end_date_parent">
                    <div class="end_date_date"></div>
                    <div class="end_mouth"></div>
                    <div class="end_year"></div>
                </div>
            </div>

            <div class="one_month">
                <div class="one_start_date_date  "></div>
                <div class="one_start_date_line">-</div>
                <div class="one_end_date_date"></div>
                <div class="one_end_month_parent">
                    <div class="one_end_mouth"></div>
                    <div class="one_end_year"></div>
                </div>
            </div>

        </div>
    </div>
    <div class="amount_parent_div">
        <div class="div_margin_height2">
            <div class="amount_text">
                <s:text name="wallet.payment.label.Amount">Amount</s:text>
            </div>
            <div class="amount_charge">
                <span>1</span>
                <span>-</span>
                <span>10000</span>
                <span>USD</span>
            </div>
        </div>


        <div class="range_parent_div">
            <div class="range_popup_parent">
                <div class="cancel_submit_div">
                    <div class="cancel_">
                                     <span>
                                <img src="<%=request.getContextPath()%>/img/wallet/icon/cancel_disput_popup.png"
                                     alt="arrow icon"/>
                                    </span>
                        <s:text name="wallet.transaction.button.Cancel">Cancel</s:text>
                    </div>
                    <div class="submit_">

                        <s:text name="wallet.general.button.Submit">Submit</s:text>
                                     <span onclick="show_send_money_popup_transfer()">
                                <img src="<%=request.getContextPath()%>/img/wallet/icon/submit_disput_popup.png"
                                     alt="envelop icon"/>
                                  </span>
                    </div>
                </div>
                <div class="select_div">
                    <select name="select" class="popup_select_tr">
                        <option value="0"><s:text name="pages.group.label.all">All</s:text></option>
                        <s:iterator var="cType" value="#session.session_url_partition.walletSetup.availableRates" >
                            <option value='<s:property value="#cType.id" />'>
                                 (<s:property value="#cType.name"/>)
                            </option>
                        </s:iterator>
                    </select>
                </div>
                <div style="clear: both;height: 10px"></div>
                <div class="choose_amount_div">
                    <s:text name="wallet.completed.transaction.choose.amount">Choose an Amount Range</s:text>
                </div>
                <div class="amount_great">
                    <div class="text-right col-lg-4 col-md-4 col-sm-4 col-xs-4">
                        <s:text name="wallet.completed.transaction.choose.amount.from">From</s:text>
                    </div>
                    <div class=" text-left col-lg-6 col-md-6 col-sm-6 col-xs-6">
                        <input type="text"/>
                    </div>
                </div>
                <div class="amount_less">
                    <div class="text-right col-lg-4 col-md-4 col-sm-4 col-xs-4">
                        <s:text name="wallet.completed.transaction.choose.amount.to">To</s:text>
                    </div>
                    <div class="text-left col-lg-6 col-md-6 col-sm-6 col-xs-6">
                        <input type="text"/>
                    </div>
                </div>
            </div>

        </div>
    </div>

    <div class="search_checkbox">
        <div class="div_margin_height3">
            <div class="search_type_click_div">
                <s:text name="wallet.transaction.type">Transaction</s:text>
            </div>
            <div class="choose_type"><s:text name="wallet.transaction.show.both">Show All</s:text></div>
        </div>
        <div class="choose_transaction_type_parent">

        </div>
    </div>

</div>

<div id="action_queue">
    <%--<a href="pending_transaction.htm"><s:text name="wallet.general.menu.PendingTransactions"/></a>--%>
</div>




<%--<div>--%>
    <%--<ul id='pending_ul' class='search_result  port-menu-list'>--%>

    <%--</ul>--%>
<%--</div>--%>

<div id="pending_transaction_title">

</div>


</div>
</div>
</div>





