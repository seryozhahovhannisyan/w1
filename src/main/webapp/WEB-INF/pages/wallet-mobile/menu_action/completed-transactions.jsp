<%@ page import="com.connectto.general.util.ConstantGeneral" %>
<%@ page import="com.connectto.wallet.model.wallet.lcp.DisputeState" %>
<%@ page import="com.connectto.general.model.Partition" %>
<%@ page import="com.connectto.general.model.lcp.PartitionLCP" %>
<%@ page import="com.connectto.general.model.User" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@include file="show_pending_transaction.jsp" %>
<%
    Partition partition = (Partition) session.getAttribute(ConstantGeneral.SESSION_URL_PARTITION);
    String partitionDns = PartitionLCP.getDNS(partition.getId());
    Long disputeId = (Long)request.getAttribute("disputeId");
    Long transactionId = (Long)request.getAttribute("transactionId");
    User user = (User) session.getAttribute(ConstantGeneral.SESSION_USER);
%>
<meta http-equiv="Content-type" content="text/html; charset=utf-8" />

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/wallet/wallet_mobile/completed_transaction_mobile.css">
<link href="<%=request.getContextPath()%>/css/general/datepicker_bootstrap.css" rel="stylesheet">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/wallet/wallet_mobile/mobile_transaction_info.css"  type="text/css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/general/bootstrap-datepicker_mobile.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/wallet/wallet_transaction_info_mobile.js"></script>

<script type="text/javascript">

    var Logger = new GENERAL_API.ACTION.LOGS;
    var src_completed_icon = "<%=request.getContextPath()%>/img/wallet/icon/complited_transaction.png";
    var src_completed_minus = "<%=request.getContextPath()%>/img/wallet/icon/wallet_plus_icon.png";
    var src_dispute_start = "<%=request.getContextPath()%>/img/wallet/icon/wallet-disput_icon.png";
    var src_dispute_pending = "<%=request.getContextPath()%>/img/wallet/icon/wallet-disput_icon_pending.png";
    var src_dispute_approved = "<%=request.getContextPath()%>/img/wallet/icon/wallet-disput_icon_approved.png";
    var src_dispute_rejected = "<%=request.getContextPath()%>/img/wallet/icon/wallet-disput_icon_rejected.png";
    var src_dispute_closed = "<%=request.getContextPath()%>/img/wallet/icon/wallet-disput_icon_closed.png";
    var txt_dispute_start = "<s:text name="wallet.login.completed.transaction.dispute">Dispute</s:text>";
    var txt_dispute_pending = "<s:text name="wallet.login.completed.transaction.disputing">Disputing</s:text>";
    var txt_dispute_approved = "<s:text name="wallet.login.completed.transaction.dispute.approved">Approved</s:text>";
    var txt_dispute_rejected = "<s:text name="wallet.login.completed.transaction.dispute.rejected">Rejected</s:text>";
    var txt_dispute_closed = "<s:text name="wallet.login.completed.transaction.dispute.closed">Closed</s:text>";
    var msg = '<s:text name="wallet.data.not.found">The data not found</s:text>';

    $(document).ready(function () {


        $(".search").click(function () {
            call_load_completed_transaction();
        });
        $(".search_user_input").keydown(function (e) {
            if (e.which == 13) {
                call_load_completed_transaction();
            }
        });
        call_load_completed_transaction();
        selected_nav('.completed_envelop_parent_li');
        date_picker();
        sort_arrow_img();
        search_by_amount();
        only_number(".amount_great input");
        only_number(".amount_less input");
        show_hide_disput_popup();
        hide_write_message();
        show_dropdown_type_search();
        search_by_type();
        show_dispute_dropdown_type_search();
        search_dispute_by_type();
        show_criteria_menu();
        dragable_divs(".range_parent_div");
        dragable_divs(".popup_cont");


        dispute_scrolling_popup();

    });

    <%
            if(disputeId != null && disputeId > 0 ){
        %>
    view_dispute(<%=disputeId%>);
    <%
        }

//        if(transactionId != null && transactionId > 0 ){
    %>
    //stor to local db and compare with current tId
    <%--if (typeof(Storage) !== "undefined") {--%>
        <%--if(localStorage.user_tr_id != <%=transactionId%>){--%>
            <%--show_completed_transaction(<%=transactionId%>);--%>
            <%--localStorage.user_tr_id = <%=transactionId%>;--%>
        <%--}--%>
    <%--}--%>


    <%
//        }
    %>

    var disable_previews_scroll = undefined;
    var disable_next_scroll = undefined;
    var var_default_position_scrolling_popup_completed_transaction = undefined;
    var array_ids = [];
    var array_name_surnames = [];
    var array_user_photo_paths = [];
    var params_next_prev = {
        array_ids             : undefined,
        transaction_id        : undefined,
        array_name_surnames   : undefined,
        array_user_photo_paths: undefined

    };


    var dispute_disable_previews_scroll = undefined;
    var dispute_disable_next_scroll = undefined;

    var array_name_dispute = [];
    var array_id_dispute = [];
    var array_partitionId_dispute = [];
    var array_img_src_dispute = [];
    var array_transaction_id_dispute = [];
    var array_wallet_exchange_id_dispute = [];
    var array_dipute_text_dispute = [];
    var array_disputeId_dispute = [];
    var params_next_prev_dispute = {
        array_name_dispute              : undefined,
        array_id_dispute                : undefined,
        array_partitionId_dispute       : undefined,
        array_img_src_dispute           : undefined,
        array_transaction_id_dispute    : undefined,
        array_wallet_exchange_id_dispute: undefined,
        array_dipute_text_dispute       : undefined,
        array_disputeId_dispute         : undefined,
        transaction_id                  : undefined
    };

    function update_dispute_params(transaction_id, array_name_dispute, array_id_dispute, array_partitionId_dispute, array_img_src_dispute, array_transaction_id_dispute, array_wallet_exchange_id_dispute, array_dipute_text_dispute, array_disputeId_dispute) {
        params_next_prev_dispute.array_name_dispute = array_name_dispute;
        params_next_prev_dispute.array_id_dispute = array_id_dispute;
        params_next_prev_dispute.array_partitionId_dispute = array_partitionId_dispute;
        params_next_prev_dispute.array_img_src_dispute = array_img_src_dispute;
        params_next_prev_dispute.array_transaction_id_dispute = array_transaction_id_dispute;
        params_next_prev_dispute.array_wallet_exchange_id_dispute = array_wallet_exchange_id_dispute;
        params_next_prev_dispute.array_dipute_text_dispute = array_dipute_text_dispute;
        params_next_prev_dispute.array_disputeId_dispute = array_disputeId_dispute;
        params_next_prev_dispute.disputeId = array_disputeId_dispute;
        params_next_prev_dispute.transaction_id = transaction_id;

    }



    function dispute_logic_show_completed_transaction_popup_previous( ){
        dispute_disable_next_scroll = false;
        for (var i = 0; i < params_next_prev_dispute.array_transaction_id_dispute.length; i++) {
            if (params_next_prev_dispute.array_transaction_id_dispute[i] == params_next_prev_dispute.transaction_id && params_next_prev_dispute.array_transaction_id_dispute.length >= 2) {
                params_next_prev_dispute.transaction_id = params_next_prev_dispute.array_transaction_id_dispute[i-1] ;

                if (params_next_prev_dispute.transaction_id === undefined) {
                    params_next_prev_dispute.transaction_id =  params_next_prev_dispute.array_transaction_id_dispute[i];
                }
                var previews_name = params_next_prev_dispute.array_name_dispute[i-1] ;
                var previews_id = params_next_prev_dispute.array_id_dispute[i-1] ;
                var previews_partitionId = params_next_prev_dispute.array_partitionId_dispute[i-1] ;
                var previews_img_src = params_next_prev_dispute.array_img_src_dispute[i-1] ;
                var previews_transaction_id = params_next_prev_dispute.array_transaction_id_dispute[i-1] ;
                var previews_wallet_exchange_id = params_next_prev_dispute.array_wallet_exchange_id_dispute[i-1] ;
                var previews_dispute_text = params_next_prev_dispute.array_dipute_text_dispute[i-1] ;
                var previews_disputeId = params_next_prev_dispute.array_disputeId_dispute[i-1] ;

                if (params_next_prev_dispute.array_transaction_id_dispute[i-2] == undefined && previews_transaction_id != undefined) {
                    dispute_show_pending_transaction_popup(previews_name, previews_id, previews_partitionId, previews_img_src, previews_transaction_id, previews_wallet_exchange_id, previews_dispute_text, previews_disputeId);
                    dispute_disable_previews_scroll = true;
                    dispute_disabble_previews_scrolling();
                } else if (previews_transaction_id == undefined) {
                    dispute_disable_previews_scroll = true;
                    dispute_disabble_previews_scrolling();
                } else {
                    dispute_disable_previews_scroll = false;
                    dispute_show_pending_transaction_popup(previews_name, previews_id, previews_partitionId, previews_img_src, previews_transaction_id, previews_wallet_exchange_id, previews_dispute_text, previews_disputeId);

                }
            }
        }
        return params_next_prev_dispute.transaction_id ;
    }


    function dispute_logic_show_completed_transaction_popup_next( ){
        dispute_disable_previews_scroll = false;
        for (var i = params_next_prev_dispute.array_transaction_id_dispute.length; i >= 0; i--) {
            if (params_next_prev_dispute.array_transaction_id_dispute[i] == params_next_prev_dispute.transaction_id && params_next_prev_dispute.array_transaction_id_dispute.length >= 2) {
                params_next_prev_dispute.transaction_id = params_next_prev_dispute.array_transaction_id_dispute[i+1] ;

                if (params_next_prev_dispute.transaction_id === undefined) {
                    params_next_prev_dispute.transaction_id =  params_next_prev_dispute.array_transaction_id_dispute[i];
                }

                var previews_name = params_next_prev_dispute.array_name_dispute[i+1] ;
                var previews_id = params_next_prev_dispute.array_id_dispute[i+1] ;
                var previews_partitionId = params_next_prev_dispute.array_partitionId_dispute[i+1] ;
                var previews_img_src = params_next_prev_dispute.array_img_src_dispute[i+1] ;
                var previews_transaction_id = params_next_prev_dispute.array_transaction_id_dispute[i+1] ;
                var previews_wallet_exchange_id = params_next_prev_dispute.array_wallet_exchange_id_dispute[i+1] ;
                var previews_dispute_text = params_next_prev_dispute.array_dipute_text_dispute[i+1] ;
                var previews_disputeId = params_next_prev_dispute.array_disputeId_dispute[i+1] ;

                if (params_next_prev_dispute.array_transaction_id_dispute[i+2] == undefined && previews_transaction_id != undefined) {
                    dispute_show_pending_transaction_popup(previews_name, previews_id, previews_partitionId, previews_img_src, previews_transaction_id, previews_wallet_exchange_id, previews_dispute_text, previews_disputeId);
                    dispute_disable_next_scroll = true;
                    dispute_disabble_next_scrolling();
                } else if (previews_transaction_id == undefined) {
                    dispute_disable_next_scroll = true;
                    dispute_disabble_next_scrolling();
                } else {
                    dispute_disable_next_scroll = false;
                    dispute_show_pending_transaction_popup(previews_name, previews_id, previews_partitionId, previews_img_src, previews_transaction_id, previews_wallet_exchange_id, previews_dispute_text, previews_disputeId);

                }
            }
        }
        return params_next_prev_dispute.transaction_id ;
    }



    function update_params(array_ids, transaction_id, array_user_photo_paths, array_name_surnames) {
        params_next_prev.array_ids = array_ids;
        params_next_prev.transaction_id = transaction_id;
        params_next_prev.array_name_surnames = array_name_surnames;
        params_next_prev.array_user_photo_paths = array_user_photo_paths;

    }

    function logic_show_completed_transaction_popup_previous( ){
        disable_next_scroll = false;
        for (var i = 0; i < params_next_prev.array_ids.length; i++) {
            if (params_next_prev.array_ids[i] == params_next_prev.transaction_id && params_next_prev.array_ids.length >= 2) {
                params_next_prev.transaction_id = params_next_prev.array_ids[i-1] ;
                if (params_next_prev.transaction_id === undefined) {
                    params_next_prev.transaction_id = params_next_prev.array_ids[i] ;
                }
                var previews_id = params_next_prev.array_ids[i-1] ;
                var previews_name_surname = params_next_prev.array_name_surnames[i-1];
                var previews_user_photo_path = params_next_prev.array_user_photo_paths[i-1];
                if (params_next_prev.array_ids[i-2] == undefined && previews_id != undefined) {
                    show_pending_transaction_popup_previous(previews_id, previews_user_photo_path, previews_name_surname);
                    disable_previews_scroll = true;
                    disabble_previews_scrolling();
                } else if (previews_id == undefined) {
                    disable_previews_scroll = true;
                    disabble_previews_scrolling();
                } else {
                    disable_previews_scroll =  false;
                    show_pending_transaction_popup_previous(previews_id, previews_user_photo_path, previews_name_surname);
                }
            }
        }
        return params_next_prev.transaction_id ;
    }

    function logic_show_completed_transaction_popup_next(){
        disable_previews_scroll = false;
        for (var i = params_next_prev.array_ids.length; i >= 0; i--) {
            if (params_next_prev.array_ids[i] == params_next_prev.transaction_id && params_next_prev.array_ids.length >= 2) {
                params_next_prev.transaction_id = params_next_prev.array_ids[i+1] ;
                if (params_next_prev.transaction_id === undefined) {
                    params_next_prev.transaction_id = params_next_prev.array_ids[i] ;
                }
                var next_id = params_next_prev.array_ids[i+1] ;
                var next_name_surname = params_next_prev.array_name_surnames[i+1];
                var next_user_photo_path = params_next_prev.array_user_photo_paths[i+1];
                if (params_next_prev.array_ids[i+2] == undefined && next_id != undefined) {
                    show_pending_transaction_popup_next(next_id, next_user_photo_path, next_name_surname);
                    disable_next_scroll = true;
                    disabble_next_scrolling();
                } else if (next_id == undefined) {
                    disable_next_scroll = true;
                    disabble_next_scrolling();
                } else {
                    disable_next_scroll = false;
                    show_pending_transaction_popup_next(next_id, next_user_photo_path, next_name_surname);

                }
            }
        }
        return params_next_prev.transaction_id ;
    }

    function default_position_scrolling_popup_completed_transaction() {
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
        }, 20);


    }

    function dispute_default_position_scrolling_popup_completed_transaction() {
        var $container = $(".container-for-scroll-sliding_dispute");
        var $content = $(".row-for-scroll-sliding_dispute");
        containerWidth = $container.width();
        contentWidth = $content.outerWidth();
        contentLeft = 0;
        scrollLeft = 0;

        var window_width = window.innerWidth;
        var need_margin_left =  window_width * 25/100;
        setInterval(function() {
            $container.scrollLeft(scrollLeft + need_margin_left);
        }, 20);


    }

    function dispute_scrolling_popup() {
        var $container = $(".container-for-scroll-sliding_dispute");
        var $content = $(".row-for-scroll-sliding_dispute");
        containerWidth = $container.width();
        contentWidth = $content.outerWidth();
        contentLeft = 0;
        scrollLeft = 0;
        $(".container-for-scroll-sliding_dispute").scroll(function(event) {

            var scrollleft = $(".container-for-scroll-sliding_dispute").scrollLeft();
            var window_width = window.innerWidth;
            var need_margin_left = window_width * 25/100;
            if ((scrollleft > need_margin_left + 40) && dispute_disable_previews_scroll !== true){
                params_next_prev_dispute.transaction_id = dispute_logic_show_completed_transaction_popup_previous();
                setTimeout(function() {
                    $container.scrollLeft(scrollLeft + need_margin_left);
                }, 20);
            }
            if ((scrollleft < need_margin_left - 40) && dispute_disable_next_scroll !== true){
                params_next_prev_dispute.transaction_id = dispute_logic_show_completed_transaction_popup_next();
                setTimeout(function() {
                    $container.scrollLeft(scrollLeft + need_margin_left);
                }, 20);
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
                }, 20);

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
                }, 20);
            }
        });
    }
    function dispute_disabble_previews_scrolling() {
        $(".container-for-scroll-sliding_dispute").scroll(function(event) {
            var scrollleft = $(".container-for-scroll-sliding_dispute").scrollLeft();
            var window_width = window.innerWidth;
            var need_margin_left = window_width * 25 / 100;
            if (scrollleft > need_margin_left) {
                setTimeout(function() {
                    $(".container-for-scroll-sliding_dispute").scrollLeft(scrollLeft + need_margin_left);
                }, 20);

            }
        });
    }

    function dispute_disabble_next_scrolling() {
        $(".container-for-scroll-sliding_dispute").scroll(function(event) {
            var scrollright = $(".container-for-scroll-sliding_dispute").scrollLeft();
            var window_width = window.innerWidth;
            var need_margin_left = window_width * 25 / 100;
            if (scrollright < need_margin_left) {
                setTimeout(function() {
                    $(".container-for-scroll-sliding_dispute").scrollLeft(scrollLeft + need_margin_left);
                }, 20);
            }
        });
    }

    function dispute_show_pending_transaction_popup(previews_name, previews_id, previews_partitionId, previews_img_src, previews_transaction_id, previews_wallet_exchange_id, previews_dispute_text, previews_disputeId)
    {
        $(".popup_cont").hide();
        $(".dispute_title input").val("");
        $(".write_message").html("");
        show_hide_disput_popup_by_next_prev(previews_name, previews_id, previews_partitionId, previews_img_src, previews_transaction_id, previews_wallet_exchange_id, previews_dispute_text, previews_disputeId);
    }
    function show_pending_transaction_popup_previous(id, user_photo_path, name_surname) {
        show_completed_transaction(id, user_photo_path, name_surname);
    }
    function show_pending_transaction_popup_next(id, user_photo_path, name_surname) {
        show_completed_transaction(id, user_photo_path, name_surname);
    }



    function call_load_completed_transaction() {
        var params = unite_criteria();
        params.current_page = 0;
        load_completed_transaction(params);
    }
    function hide_write_message() {

        var place = '<span class="message_placeholder"><s:text name="wallet.login.write.message">Write message</s:text></span>';

        $("body").delegate('.write_message','click', function () {
            $(this).focus();
            $(".write_message").focus(function () {
                if ($(".message_placeholder")) {
                    $(".message_placeholder").remove();
                    $(this).html("")
                }
            });
        });
        $(".popup_cont").on('click',function (e) {
            if (!$(e.target).hasClass("write_message")){
                $(".write_message").blur();
                $(".write_message").blur(function () {
                    if ($(this).text() === "") {
                        $(this).html(place);

                    }

                });
            }
        });
    }


    function datepickerCancelSubmit() {
        var con = '<div class="datepicker_header">' +
                '<div class="dp_cancel_div"><s:text name="wallet.transaction.button.Cancel">Cancel</s:text></div>' +
                '<div class="dp_submit_div"><s:text name="wallet.general.button.Submit">Submit</s:text></div>' +
                '</div>';
        $(".datepicker-days").before(con);
    }

    function show_hide_disput_popup_by_next_prev(previews_name, previews_id, previews_partitionId, previews_img_src, previews_transaction_id, previews_wallet_exchange_id, previews_dispute_text, previews_disputeId) {

        var name = previews_name;
        var id = previews_id;
        var partitionId = previews_partitionId;
        var img_src = previews_img_src;
        var transaction_id = previews_transaction_id;
        var wallet_exchange_id = previews_wallet_exchange_id;
        var dipute_text = previews_dispute_text;
        var disputeId = previews_disputeId;
        if (disputeId != null && disputeId > 0) {

            view_dispute(disputeId, name, dipute_text);

        } else {
            $('#d_transaction_id').val(transaction_id);
            $('#d_wallet_exchange_id').val(wallet_exchange_id);
            $(".a_href_name a").html(name).attr('href', 'show_user_action.htm?selectedUserId=' + id + '&patitionId=' + partitionId);
            $('#profile_img').attr('src', img_src);
            $(".write_message").html('<span class="message_placeholder">Write message</span>');
            $(".dispute_title ").show();
            $(".popup_textarea  ").show();
            $(".popup_sm_header div:nth-child(3)  ").show();
            $(".disput_content  ").hide();
            $(".disput_resaon  ").hide();
            $(".disput_stat_text").text(dipute_text);
            $(".popup_cont").show();
        }
        $(document).keydown(function (e) {
            if (e.which == 27) {
                $(".popup_cont").hide();
                $(".dispute_title input").val("");
                $(".write_message").html("");
            }
        });

        $(".popup_sm_header div:nth-child(2) span").click(function () {
            $(".popup_cont").hide();
            $(".dispute_title input").val("");
            $(".write_message").html("");
        });
        $(".popup_sm_header div:nth-child(3) span").click(function () {
            $(".popup_cont").hide();
            $(".dispute_title input").val("");
            $(".write_message").html("");
        })
    }

    function show_hide_disput_popup() {

        $("body").delegate(".disput_div", "click", function () {
            var name = $(this).data("name");
            var id = $(this).data("id");
            var partitionId = $(this).data("partition_id");
            var img_src = $(this).data("img_src");
            var transaction_id = $(this).data("transaction_id");
            var wallet_exchange_id = $(this).data("wallet_exchange_id");
            var dipute_text = $(this).data("dispute_txt");
            var disputeId = $(this).data("dispute_id");
            if (disputeId != null && disputeId > 0) {
                view_dispute(disputeId, name, dipute_text);
            } else {
                $('#d_transaction_id').val(transaction_id);
                $('#d_wallet_exchange_id').val(wallet_exchange_id);
                $(".a_href_name a").html(name).attr('href', 'show_user_action.htm?selectedUserId=' + id + '&patitionId=' + partitionId);
                $('#profile_img').attr('src', img_src);
                $(".write_message").html('<span class="message_placeholder">Write message</span>');
                $(".dispute_title ").show();
                $(".popup_textarea  ").show();
                $(".popup_sm_header div:nth-child(3)  ").show();
                $(".disput_content  ").hide();
                $(".disput_resaon  ").hide();
                $(".disput_stat_text").text(dipute_text);
                $(".popup_cont").show();
            }

        });

        $("body").delegate(".disput_text_div", "click", function () {
            var name = $(this).data("name");
            var id = $(this).data("id");
            var partitionId = $(this).data("partition_id");
            var img_src = $(this).data("img_src");
            var wallet_exchange_id = $(this).data("wallet_exchange_id");
            var transaction_id = $(this).data("transaction_id");
            var dipute_text = $(this).data("dispute_txt");

            var disputeId = $(this).data("dispute_id");
            if (disputeId != null && disputeId > 0) {
                view_dispute(disputeId, name, dipute_text);
            } else {
                $('#d_transaction_id').val(transaction_id);
                $('#d_wallet_exchange_id').val(wallet_exchange_id);
                $(".a_href_name a").html(name).attr('href', 'show_user_action.htm?selectedUserId=' + id + '&patitionId=' + partitionId);
                $('#profile_img').attr('src', img_src);
                $(".write_message").html('<span class="message_placeholder">Write message</span>');
                $(".dispute_title ").show();
                $(".popup_textarea  ").show();
                $(".popup_sm_header div:nth-child(3)  ").show();
                $(".disput_content  ").hide();
                $(".disput_resaon  ").hide();
                $(".disput_stat_text").text(dipute_text);
                $(".popup_cont").show();
            }

        });
        $(document).keydown(function (e) {
            if (e.which == 27) {
                $(".popup_cont").hide();
                $(".dispute_title input").val("");
                $(".write_message").html("");
            }
        });

        $(".popup_sm_header div:nth-child(2) span").click(function () {
            $(".popup_cont").hide();
            $(".dispute_title input").val("");
            $(".write_message").html("");
        });
        $(".popup_sm_header div:nth-child(3) span").click(function () {
            $(".popup_cont").hide();
            $(".dispute_title input").val("");
            $(".write_message").html("");
        })

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


        });
        $("body").delegate(".dp_submit_div", "click", function () {
            call_load_completed_transaction();
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

        });

        $(".one_start_date_date ").html(new Date().getDate());
        $(".one_end_mouth ").html(datepicker_obj.months[new Date().getMonth()]);
        $(".one_end_year ").html(new Date().getFullYear());
        $(".one_start_date_line").hide();
        $(".one_end_date_date").hide();

    }
    function sort_arrow_img() {
        $(".up").click(function () {
            $('.arrow_div img').attr('src', '<%=request.getContextPath()%>/img/wallet/icon/arrow_up_icon.png');
            $('.arrow_div span:nth-child(1)').html("desc");
            call_load_completed_transaction()
        });
        $(".down").click(function () {
            $('.arrow_div img').attr('src', '<%=request.getContextPath()%>/img/wallet/icon/arrow_down_icon.png');
            $('.arrow_div span:nth-child(1)').html("asc");
            call_load_completed_transaction()
        })
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
    function search_by_amount() {
        console.log($('.range_parent_div').css("display"));

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

            call_load_completed_transaction()
        })
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

    function load_completed_transaction(params, clear_array_for_popup) {
        loader_show();
        $('#error_msg').remove();

        if (clear_array_for_popup !== false) {
            clear_all_arrays_for_next_prev_popup();
        }

        var current_page = params.current_page != undefined ? params.current_page : '0';
        var order_type = params.order_type != undefined ? params.order_type : 'desc';
        var date_less = params.date_less != undefined ? params.date_less : "";
        var date_great = params.date_great != undefined ? params.date_great : "";
        var byCurrency = params.byCurrency != undefined ? params.byCurrency : "";
        var rangeAmountGreat = params.rangeAmountGreat != undefined ? params.rangeAmountGreat : "";
        var rangeAmountLess = params.rangeAmountLess != undefined ? params.rangeAmountLess : "";
        var searchLike = params.searchLike != undefined ? params.searchLike : "";
        var transactionType = params.transactionType != undefined ? params.transactionType : "0";
        var disputeState = params.disputeState != undefined ? params.disputeState : "0";
        var data = {
            currentPage: current_page,
            orderType: order_type,
            rangeDateLess: date_less,
            rangeDateGreat: date_great,
            byCurrency: byCurrency,
            rangeAmountGreat: rangeAmountGreat,
            rangeAmountLess: rangeAmountLess,
            searchLike: searchLike,
            transactionType: transactionType,
            disputeState: disputeState
        };
        console.log("uxarkac data", data)
        var params_ = {};
        params_.async = true;
        params_.data = data;
        WALLET.load_completed_transactions(params_, completed_load_available_currencies_callback, wallet_error_handler);
    }

    function load_exchanged_transaction(params, clear_array_for_popup) {

        loader_show();
        $('#error_msg').remove();

        if (clear_array_for_popup !== false) {
            clear_all_arrays_for_next_prev_popup();
        }

        var current_page = params.current_page_exchange != undefined ? params.current_page_exchange : '0';
        var order_type = params.order_type != undefined ? params.order_type : 'desc';
        var date_less = params.date_less != undefined ? params.date_less : "";
        var date_great = params.date_great != undefined ? params.date_great : "";
        var byCurrency = params.byCurrency != undefined ? params.byCurrency : "";
        var rangeAmountGreat = params.rangeAmountGreat != undefined ? params.rangeAmountGreat : "";
        var rangeAmountLess = params.rangeAmountLess != undefined ? params.rangeAmountLess : "";
        var searchLike = params.searchLike != undefined ? params.searchLike : "";
        var transactionType = params.transactionType != undefined ? params.transactionType : "0";
        var disputeState = params.disputeState != undefined ? params.disputeState : "0";
        var data = {
            currentPage: current_page,
            orderType: order_type,
            rangeDateLess: date_less,
            rangeDateGreat: date_great,
            byCurrency: byCurrency,
            rangeAmountGreat: rangeAmountGreat,
            rangeAmountLess: rangeAmountLess,
            searchLike: searchLike,
            transactionType: transactionType,
            disputeState: disputeState
        };
        var params_ = {};
        params_.async = true;
        params_.data = data;
        WALLET.load_exchanged_transaction(params_, exchanged_load_available_currencies_callback, wallet_error_handler);
    }

    function scroll_paging() {
        $('.search_result').scroll(function () {
            if ($(this).scrollTop() + $(this).innerHeight() >= $(this)[0].scrollHeight) {
                var data_action = $(this).data("action");
                var data_current_page = $(this).data("current_page");
                var data_is_last = $(this).data("is_last");
                var data_order_type = $(this).data("order_type");
                console.log("data_action",data_action,"data_current_page",data_current_page,"data_is_last",data_is_last,"data_order_type",data_order_type);
                if (data_is_last == 0 || data_is_last == null) {
                    if (data_action == "load_completed_transaction") {
                        var params = unite_criteria();
                        params.current_page = data_current_page;
                        params.order_type = data_order_type;
                        var clear_array_for_popup = false;
                        load_completed_transaction(params, clear_array_for_popup);
                    } else if (data_action == "load_exchanged_transaction") {
                        var params = unite_criteria();
                        params.current_page = data_current_page;
                        params.order_type = data_order_type;
                        var clear_array_for_popup = false;
                        load_exchanged_transaction(params, clear_array_for_popup);
                    }
                }
                $(this).off("scroll")
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
        params.current_page = '0';
        params.order_type = $('.arrow_div span:nth-child(1)').html();
        params.byCurrency = $('.popup_select_tr').val();
        params.rangeAmountGreat = $(".amount_charge span:nth-child(1)").text();
        params.rangeAmountLess = $(".amount_charge span:nth-child(3)").text();
        params.transactionType = $(".choose_type").attr('data-id');
        params.disputeState = $(".choose_dispute_type").attr('data-id');
        return params;
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
        var types = [
            {
                id: 0,
                name: 'all',
                class_: 'show_both',
                value: '<s:text name="wallet.transaction.show.both">Show All</s:text>'
            },
            {
                id: 1,
                name: 'all',
                class_: 'show_only_received',
                value: '<s:text name="wallet.transaction.show.only.received">Show only Received</s:text>'
            },
            {
                id: 2,
                name: 'all',
                class_: 'show_only_sended',
                value: '<s:text name="wallet.transaction.show.only.sent">Show only Sent</s:text>'
            }
        ];
        $('.choose_transaction_type_parent').html('');
        $.each(types, function (i, item) {
            var div = '<div class="' + item.class_ + '">' +
                    '<div data-id="' + item.id + '">' + item.value + '</div>' +
                    '</div>';
            $('.choose_transaction_type_parent').append(div);
        });
        $(".choose_transaction_type_parent").click(function (e) {
            if (!e) e = window.event;
            if ($(e.target).closest(".choose_transaction_type_parent")) {
                var type_text = $(e.target).text();
                var id = $(e.target).attr('data-id');
                var type_color = $(e.target).css("color");
                $(".choose_type").html(type_text).css("color", type_color);
                $(".choose_type").attr('data-id', id);
                $(".choose_transaction_type_parent").hide();
            }
        });
    }
    /*disputation*/
    function show_dispute_dropdown_type_search() {
        $(".search_dispute_type_click_div").click(function () {
            $(".choose_disputation_type_parent").toggle();
            $(".range_parent_div").hide();
            $(".choose_transaction_type_parent").hide();
            setTimeout(function () {
                close_any_popup(".choose_disputation_type_parent");
            }, 100);
            $(document).off("click")
        })
    }
    function search_dispute_by_type() {
        $(".choose_disputation_type_parent").click(function (e) {
            if (!e) e = window.event;
            if ($(e.target).closest(".choose_disputation_type_parent")) {
                var type_text = $(e.target).html();
                var id = $(e.target).attr('data-id');
                var type_color = $(e.target).css("color");
                $(".choose_dispute_type").html(type_text).css("color", type_color);
                $(".choose_dispute_type").attr('data-id', id);
                $(".choose_disputation_type_parent").hide();
            }
        });
    }

    function do_dispute() {

        var transactionId = $('#d_transaction_id').val();
        var walletExchangeId = $('#d_wallet_exchange_id').val();
        var reason = $('#d_reason').val();
        var content = $('#d_content').text();
        var form = document.createElement("form");
        var e_transaction = document.createElement("input");
        var e_reason = document.createElement("input");
        var e_content = document.createElement("input");
        loader_show();
        $.ajax({
            url:  'dispute.htm' ,
            type: "post",
            dataType: "json",
            data : {
                transactionId : transactionId,
                walletExchangeId : walletExchangeId,
                reason : reason,
                content : content
            },
            success: function (data) {
                console.log("disput data", data);
                if (data != null && data.responseDto.status == "SUCCESS") {
                    var selector = '.tr_id' + transactionId;
                    var selector2 = '.tr_tx_id' + transactionId;
                    $(selector).attr("src", src_dispute_pending);
                    $(selector2).html(txt_dispute_pending);

                    loader_hide();
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
    function ul_rezult_height() {
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
        $(".search_result").css("height", content + "px");
        console.log("doc_height_send", doc_height, "win_height_send", win_height, "person_panel_height", person_panel_height, "footter_height", footter_height, "content", content), "user_search_height", user_search_height;
    }
    function show_criteria_menu() {
        $(".criteria_menu").click(function () {
            $(".criterias_div").toggle(500);
        })
    }

    function clear_all_arrays_for_next_prev_popup(){
        array_ids = [];
        array_name_surnames = [];
        array_user_photo_paths = [];

        array_name_dispute = [];
        array_id_dispute = [];
        array_partitionId_dispute = [];
        array_img_src_dispute = [];
        array_transaction_id_dispute = [];
        array_wallet_exchange_id_dispute = [];
        array_dipute_text_dispute = [];
        array_disputeId_dispute = [];
    }

    function exchange_history(){
        $(".exchange_text").click(function () {
            var params = unite_criteria();
            params.current_page = 0;
            load_exchanged_transaction(params);
        })
    }

    //CALBACK
    var completed_load_available_currencies_callback = function (data,current_page) {
        console.log("completed daata", data);

        var cont;
        var current_page_new = data.currentPage;
        var is_last = data.isLast;
        var order_type = data.orderType;
        if (current_page == null || current_page < 2) {
            $("#search_result").remove();
            cont = $("<ul id='completed_ul' class='port-menu-list completed_transaction search_result' data-action='load_completed_transaction' data-current_page='"
                    + current_page_new + "' data-is_last='" + is_last + "' data-order_type='" + order_type + "' />");
            $("#completed_transaction_title").after(cont);
        }
        else {
            $(".search_result").data('current_page', current_page_new).data('is_last', is_last).data("action", "load_completed_transaction");
            cont = $(".search_result");
        }
        ul_rezult_height();
        var completedTransactions = data.completedTransactions;
        if (completedTransactions != null && completedTransactions.length > 0) {
            var partition_dns = "<%=partitionDns%>";
            var src_envelop;
            $.each(completedTransactions, function (i, item) {
                var completed = item;
                if (completed == null) {
                    Logger.save(Logger.LOG_LEVEL.ERROR, 'load_completed_transaction', 0, "responseText=[incorrect incoming data]");
                }
                else {
                    var debited = completed.isDebited ? 'debited' : ' ';
                    var credited = completed.isCredited ? 'credited' : ' ';

                    var li = $('<li class="searched_item ' + debited + credited + '" tabindex="' + ( i + 1 ) + '"/>').appendTo(cont);

                    var user;
                    var walletSetupId;
                    if (completed.debited == true) {
                        user = completed.userDto;
                        walletSetupId = completed.walletSetupId;
                        src_envelop = "<%=request.getContextPath()%>/img/wallet/icon/send_money.png";
                        src_completed_minus = "<%=request.getContextPath()%>img/wallet/icon/wallet_minus_icon.png";
                    }
                    else if (completed.credited == true) {
                        user = completed.userDto;
                        walletSetupId = completed.walletSetupId;
                        src_envelop = "<%=request.getContextPath()%>/img/wallet/icon/requst_transaction.png";
                        src_completed_minus = "<%=request.getContextPath()%>/img/wallet/icon/wallet_plus_icon.png";
                    }
                    else {
                        Logger.save(Logger.LOG_LEVEL.ERROR, 'load_completed_transaction', 0, "responseText=[incorrect incoming data, isDebited = false; isCredited= false]");
                    }

                    console.log('user', user);
                    var div = $("<div class='parent_div'/>").appendTo(li);

                    var src = '<s:property value="#session.session_user.partition.partitionServerUrl"/><s:property value="#session.session_user.partition.partitionLogoDirectory"/><s:property value="#session.session_user.partition.logoPath"/>';
                    if (user != null) {
                        src = IMAGE_BASE_URL + user.id;
                    }
//                    var img_icon = $('<div class="icon_div"> <img src="' + src_completed_icon + '"/></div> ');//.appendTo(div);
//                    var img_minus_icon = $('<div class="icon_mius_div"> <img src="' + src_completed_minus + '"/></div> ');//.appendTo(div);
//                    img_icon.appendTo(div);
//                    img_minus_icon.appendTo(div);
                    var transaction_id = completed.transactionId;
                    var disputeState = completed.disputeState;
                    var disputeId = completed.disputeId;
                    var src_dispute = '';
                    var txt_dispute = '';
                    var ns = user != null ? user.name + " " + user.surname : partition_dns;
                    var show_completed_transaction ="show_completed_transaction('"+transaction_id+"', '"+src+"', '"+ns+"')"  ;
                    var img = $('<div class="img_div"><img class="for_next_previews_buttons_img' + transaction_id + '" onclick="' + show_completed_transaction+ '" src="' + src + '"/></div> ').appendTo(div);
                    var id = user != null ? user.id : walletSetupId;
                    var partitionId = user != null ? user.partitionId : 0;


                    array_user_photo_paths.push(src);
                    array_ids.push(transaction_id);
                    array_name_surnames.push(ns);
                    if (disputeState < 1) {
                        src_dispute = src_dispute_start;
                        txt_dispute = txt_dispute_start;
                    } else {
                        if (disputeState == <%=DisputeState.PENDING.getId()%>) {
                            src_dispute = src_dispute_pending;
                            txt_dispute = txt_dispute_pending;
                        } else if (disputeState == <%=DisputeState.APPROVE.getId()%>) {
                            src_dispute = src_dispute_approved;
                            txt_dispute = txt_dispute_approved;
                        } else if (disputeState == <%=DisputeState.REJECT.getId()%>) {
                            src_dispute = src_dispute_rejected;
                            txt_dispute = txt_dispute_rejected;
                        } else if (disputeState == <%=DisputeState.ClOSE.getId()%>) {
                            src_dispute = src_dispute_closed;
                            txt_dispute = txt_dispute_closed;
                        } else {
                            Logger.save(Logger.LOG_LEVEL.ERROR, 'load_completed_transaction', 0, "incorrect response disputeState:" + disputeState);
                        }
                    }

                    //var func_dispute = "pre_dispute.htm?transactionId=" + completed.id;
                    var action_date = item.openedAt;
                    console.log("completedTransactions", item, "completedTransactions.openedAt", item.openedAt)
                    var arr_date_time = action_date.split("T");
                    $('<div class="name_surname_a_div"><a href="#" onclick="' + show_completed_transaction + '" class="name_surname_a for_next_previews_buttons'+transaction_id+ '">' + ns + '</a></div>').appendTo(div);
                    $('<div class="envelop_parent_div">' +
                            '<div class="envelop_div">' +
                            '<img src="' + src_envelop + '" alt=""/>' +
                            '</div>' +
                            '<div class="date_div">' +
                            '<div class="text-center">' + arr_date_time[0] + '</div>' +
                            '<div class="text-center">' + arr_date_time[1] + '</div>' +
                            '</div>' +
                            '</div>').appendTo(div);

                    array_name_dispute.push(ns);
                    array_id_dispute.push(id);
                    array_partitionId_dispute.push(partitionId);
                    array_img_src_dispute.push(src);
                    array_transaction_id_dispute.push(transaction_id);
                    array_wallet_exchange_id_dispute.push(null);
                    array_dipute_text_dispute.push(txt_dispute);
                    array_disputeId_dispute.push(disputeId);

                    $('<div class="disput_parent_div">' +
//                                          '<div class="disput_div" onclick=goToAction("' + func_dispute + '")>' +
                            '<div class="disput_div" data-dispute_id = "' + disputeId + '" data-transaction_id="' + transaction_id + '" data-id="' + id + '" data-partition_id="' + partitionId + '" data-img_src="' + src + '" data-name="' + ns + '">' +
                            '<img src="' + src_dispute + '" alt=""/>' +
                            '</div>' +
                            '<div class="disput_text_div text-center" data-dispute_id = "' + disputeId + '" data-transaction_id="' + transaction_id + '" data-id="' + id + '" data-partition_id="' + partitionId + '" data-img_src="' + src + '" data-name="' + ns + '">' +
//                                          '<div class="disput_text_div text-center" onclick=goToAction("' + func_dispute + '")>' +
//                                          '<div class="disput_text_div text-center" data-name="'+ns+'">' +
                            txt_dispute +
                            '</div>' +
                            '</div>').appendTo(div);

                    $('<div class="amount_div"/></div>').text(completed.price + " " + completed.priceCurrency).appendTo(div);
                    $('<div style="clear: both"/>').appendTo(div);

                    $('.for_next_previews_buttons' + transaction_id).click(function () {

                        var_default_position_scrolling_popup_completed_transaction = true;

                        update_params(array_ids, transaction_id, array_user_photo_paths, array_name_surnames);

                    });

                    $('.for_next_previews_buttons_img' + transaction_id).click(function () {
                        var_default_position_scrolling_popup_completed_transaction = true;
                        update_params(array_ids, transaction_id, array_user_photo_paths, array_name_surnames );

                    });

                    $(".disput_div[data-transaction_id=" + transaction_id +  "]").click(function () {
                        dispute_default_position_scrolling_popup_completed_transaction();

                        update_dispute_params(transaction_id, array_name_dispute, array_id_dispute, array_partitionId_dispute, array_img_src_dispute, array_transaction_id_dispute, array_wallet_exchange_id_dispute, array_dipute_text_dispute, array_disputeId_dispute);


                    });

                }
            });
        }
        scroll_paging();
        footerPlace();
        loader_hide();
    };
    var exchanged_load_available_currencies_callback = function (data,current_page){
        console.log("exchange datas", data, "current page", current_page);
        var cont;
        var current_page_new = data.currentPage;
        var is_last = data.isLast;
        var order_type = data.orderType;
        if (current_page == null || current_page < 2) {
            $("#search_result").remove();
            $(".search_result").remove();
            cont = $("<ul id='completed_ul' class='port-menu-list completed_transaction search_result' data-action='load_exchanged_transaction' data-current_page_exchange='"
                    + current_page_new + "' data-is_last='" + is_last + "' data-order_type='" + order_type + "' />");
            $("#completed_transaction_title").after(cont);

        }
        else {
            $(".search_result").data('current_page_exchange',current_page_new).data('is_last', is_last).data("action","load_exchanged_transaction");
            cont = $(".search_result");

        }
        ul_rezult_height();
        var walletExchanges = data.walletExchanges;
        src_completed_minus = "<%=request.getContextPath()%>/img/wallet/icon/wallet_minus_icon.png";
        if (walletExchanges != null && walletExchanges.length > 0) {
            var src_envelop = "<%=request.getContextPath()%>/img/wallet/icon/exchange.png";

            var src = $('.avatar_div img').attr('src');
            var ns = '<%=user.getName()%> ' + ' ' + '<%=user.getSurname()%>';
            var id = <%=user.getId()%>;

            $.each(walletExchanges, function (i, item) {
                var walletExchange = item;
                if (walletExchange == null) {
                    Logger.save(Logger.LOG_LEVEL.ERROR, 'load_exchanged_transaction', 0, "responseText=[incorrect incoming data]");
                }
                else {

                    var li = $('<li class="searched_item credited" tabindex="' + ( i + 1 ) + '"/>').appendTo(cont);

                    var div = $("<div class='parent_div'/>").appendTo(li);

                    var img_icon = $('<div class="icon_div"> <img src="' + src_completed_icon + '"/></div> ');//.appendTo(div);
                    var img_minus_icon = $('<div class="icon_mius_div"> <img src="' + src_completed_minus + '"/></div> ');//.appendTo(div);
                    img_icon.appendTo(div);
                    img_minus_icon.appendTo(div);
                    var walletExchangeId = walletExchange.id;
                    var disputeState = walletExchange.disputeState;
                    var disputeId = walletExchange.disputeId;
                    var src_dispute = '';
                    var txt_dispute = '';
                    var show_exchanged_transaction ="show_exchanged_transaction('" + walletExchangeId + "', '" + src + "', '"+ns+"')"  ;
                    var img = $('<div class="img_div"><img onclick="' + show_exchanged_transaction + '" src="' + src + '"/></div>').appendTo(div);
                    console.log('disputeState '+ disputeState);
                    console.log('disputeId '+ disputeId);
                    if (disputeState < 1) {
                        src_dispute = src_dispute_start;
                        txt_dispute = txt_dispute_start;
                    } else {
                        if (disputeState == <%=DisputeState.PENDING.getId()%>) {
                            src_dispute = src_dispute_pending;
                            txt_dispute = txt_dispute_pending;
                        } else if (disputeState == <%=DisputeState.APPROVE.getId()%>) {
                            src_dispute = src_dispute_approved;
                            txt_dispute = txt_dispute_approved;
                        } else if (disputeState == <%=DisputeState.REJECT.getId()%>) {
                            src_dispute = src_dispute_rejected;
                            txt_dispute = txt_dispute_rejected;
                        } else if (disputeState == <%=DisputeState.ClOSE.getId()%>) {
                            src_dispute = src_dispute_closed;
                            txt_dispute = txt_dispute_closed;
                        } else {
                            Logger.save(Logger.LOG_LEVEL.ERROR, 'load_completed_transaction', 0, "incorrect response disputeState:" + disputeState);
                        }
                    }

                    var action_date = item.exchangedAt;
                    var arr_date_time = action_date.split("T");
                    $('<div class="name_surname_a_div"><a href="#" onclick="' + show_exchanged_transaction + '" class="name_surname_a">' + ns + '</a></div>').appendTo(div);
                    $('<div class="envelop_parent_div">' +
                            '<div class="envelop_div">' +
                            '<img src="' + src_envelop + '" alt=""/>' +
                            '</div>' +
                            '<div class="date_div">' +
                            '<div class="text-center">' + arr_date_time[0] + '</div>' +
                            '<div class="text-center">' + arr_date_time[1] + '</div>' +
                            '</div>' +
                            '</div>').appendTo(div);

                    $('<div class="disput_parent_div">' +
                            '<div class="disput_div" data-dispute_id = "' + disputeId + '" data-wallet_exchange_id="' + walletExchangeId + '" data-id="' + id +  '" data-img_src="' + src + '" data-name="' + ns + '">' +
                            '<img src="' + src_dispute + '" alt=""/>' +
                            '</div>' +
                            '<div class="disput_text_div text-center" data-dispute_id = "' + disputeId + '" data-wallet_exchange_id="' + walletExchangeId + '" data-id="' + id + '" data-img_src="' + src + '" data-name="' + ns + '">' +
                            txt_dispute +
                            '</div>' +
                            '</div>').appendTo(div);

                    $('<div class="amount_div"/></div>').text(walletExchange.newMoneyPaidTaxPrice + " " + walletExchange.newCurrencyType).appendTo(div);
                    $('<div style="clear: both"/>').appendTo(div);

                }
            });
        }
        scroll_paging();
        footerPlace();
        loader_hide();
    };

    function show_completed_transaction(transaction_id,src,ns){
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

        loader_show();
        <%--window.localStorage.setItem('current_logged_user_'+<%=user.getId()%> +'_tr', '<%=transactionId%>');--%>
        $.ajax({
            url: 'show-completed-transaction.htm',
            type: "post",
            dataType: "json",
            data: {
                transactionId: transaction_id
            },
            success: function (data) {
                if (data.responseDto != null && data.responseDto.status == "SUCCESS") {
                    console.log('data', data.preview);
                    var message = data.preview.message != null ? data.preview.message : "-";
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
                        message: message,
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
                        success_message: success_message,
                        error_message: error_message,
                        transaction_completed: true


                    };
                    transaction_info(params);
                    console.log("attached_file_arr", attached_file_arr);
                    if (attached_file_arr.length > 0) {
                        $(".attached_files_par_div").show();
                        $.each(attached_file_arr, function (key, value) {
                            $(".attached_files").append("<div><a   href ='/transaction_data_download.htm?dataFileName=" + value.fileName + "&transactionId=" + transaction_id + "'  style='display: block;cursor: pointer;'><span>" + value.fileName + "</span><span style='margin: 0 10px;'>" + value.size + "kb</span><span>" + value.transactionId + "</span></a></div>")
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
                    if (data != null) {
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
    function show_exchanged_transaction(walletExchange_id,src,ns){
        var close = "<s:text name="button.close">close</s:text>";

        var img = '<img alt="avatar" src="'+src+'"/>';
        var exchange_history = '<s:text name="wallet.exchange.history">Exchange history</s:text>';
        var Your_money_before  = '<s:text name="wallet.settings.exchange.old.money">Your money before exchange</s:text>';
        var Your_money_after = '<s:text name="wallet.settings.exchange.new.money">Your money after exchange</s:text>';
        var transfer_cost = '<s:text name="wallet.settings.exchange.date">Exchange Date</s:text>';
        var trans_exch_fee = '<s:text name="wallet.exchange.label.Transfer.exchange.fee">Transfer exchange fee:</s:text>';
        var exchange_fee = '<s:text name="wallet.exchange.label.Exchange.fee">Exchange fee:</s:text>';
        var trans_fee_det = '<s:text name="wallet.exchange.label.Transfer.fee.details">Transfer fee details:</s:text>';
        var pending_trans = '<s:text name="wallet.login.pending.transaction">Pending transaction</s:text>';
        var print = '<s:text name="wallet.exchange.button.Print">Print</s:text>';
        var download = '<s:text name="wallet.exchange.button.Download">Download</s:text>';
        var email = '<s:text name="label.user.email">email</s:text>';
        var send = '<s:text name="tsm.become.company.request.send">send</s:text>';
        var success_message = '<s:text name="wallet.transaction.info.email.success">Email successfully sent</s:text>';
        var error_message = '<s:text name="errors.internal.server">Please, try again. An error occurred on the server</s:text>';

        loader_show();
        $.ajax({
            url: 'show-exchanged-transaction.htm',
            type: "post",
            dataType: "json",
            data: {
                walletExchangeId: walletExchange_id
            },
            success: function (data) {
                if (data.responseDto != null && data.responseDto.status == "SUCCESS") {
                    console.log(' data.previewExchange', data.previewExchange);
                    var money = data.previewExchange.money != null ? data.previewExchange.money : " ";
                    var oldCurrencyType = data.previewExchange.oldCurrencyType != null ? data.previewExchange.oldCurrencyType : " ";
                    var newMoneyPaidTaxPrice = data.previewExchange.newMoneyPaidTaxPrice != null ? data.previewExchange.newMoneyPaidTaxPrice : " ";
                    var newCurrencyType = data.previewExchange.newCurrencyType != null ? data.previewExchange.newCurrencyType : " ";
                    var exchangedAt = data.previewExchange.exchangedAt != null ? data.previewExchange.exchangedAt.split("T")[0] + " " + data.previewExchange.exchangedAt.split("T")[1] : " ";
                    var pending_transaction_arr = data.previewExchange.walletExchangePendingIdes != null ? data.previewExchange.walletExchangePendingIdes : " ";


                    var params = {
                        close: close,
                        src: src,
                        ns: ns,
                        exchange_history: exchange_history,
                        Your_money_before: Your_money_before,
                        Your_money_after: Your_money_after,
                        transfer_cost: transfer_cost,
                        trans_exch_fee: trans_exch_fee,
                        exchange_fee: exchange_fee,
                        trans_fee_det: trans_fee_det,
                        money: money,
                        oldCurrencyType: oldCurrencyType,
                        newMoneyPaidTaxPrice: newMoneyPaidTaxPrice,
                        newCurrencyType: newCurrencyType,
                        exchangedAt: exchangedAt,
                        img: img,
                        pending_trans: pending_trans,
                        print: print,

                        download: download,
                        email: email,
                        walletExchange_id: walletExchange_id,
                        send: send,
                        success_message: success_message,
                        error_message: error_message
//
                    };
                    walletExchange(params);


                    loader_hide();
                    if (pending_transaction_arr.length > 0) {
                        $(".attached_files_par_div").show();
                        var view_transactin = '<s:text name="wallet.exchange.history.view.transaction">View pending transaction information</s:text>';
                        $.each(pending_transaction_arr, function (key, value) {
                            var show_pending_transaction = "show_pending_transaction('" + value + "', '" + src + "', '" + ns + "')";
                            $(".attached_files").append('<div class="current_pending_transaction" onclick="' + show_pending_transaction + '">' + view_transactin + '</div>');

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
                    if (data != null) {
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
    function view_dispute(disputeId, name, dipute_text) {
        loader_show();
        $.ajax({
            url:  'view-dispute.htm' ,
            type: "post",
            dataType: "json",
            data : {
                disputeId : disputeId
            },
            success: function (data) {
                if (data != null && data.responseDto.status == "SUCCESS") {
                    console.log('data view dispute', data);
                    var reason = data.currentDispute.reason;
                    var content = data.currentDispute.content;
                    $(".disput_resaon").remove();
                    $(".disput_content").remove();
                    $(".a_href_name a").html(name)
                    $(".a_href_name").after("<div class='disput_resaon'>" + reason + "</div><div class='disput_content'>" + content + "</div>");
                    $(".dispute_title ").hide();
                    $(".popup_textarea ").hide();
                    $(".popup_sm_header div:nth-child(3)  ").hide();
                    $(".disput_stat_text").text(dipute_text);
                    $(".popup_cont").show();
                    loader_hide();
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

</script>

<div class="complited_transaction container-fluid">
    <div class="row main">
        <div id="action_panel" class="action_panel">

            <div class="row" style="border-bottom: 1px solid #555965">
                <div id="search_user" class="user_search_div div_margin_height1">
                    <div class="input_before_text">
                        <span>
                        <s:text name="wallet.pages.make.search.before.text">Please choose precipitant</s:text>
                        </span>
                    </div>

                        <div class="search_parent_div">
                            <div class="search"></div>
                            <input id="search_user_text" type="search" class="search_user_input" name="searchLike"
                                   placeholder="<s:text name="wallet.pages.make.label.search_by">Search Friend or Company</s:text>">
                        </div>
                    <div class="arrow_sort_divs">
                        <div class="arrow_div">
                            <div class="up"></div>
                            <div class="down"></div>
                            <img src="<%=request.getContextPath()%>/img/wallet/icon/arrow_up_icon.png"
                                 alt="arrow icon"/>
                        </div>
                        <div class="sort_div">
                            <s:text name="wallet.action.search.result.sort">Sort</s:text>
                        </div>


                        <div class="criteria_menu">
                            <p><s:text name="wallet.payment.label.More">More</s:text></p>

                            <div class="criteria_menu_img_div">
                                <img src="<%=request.getContextPath()%>/img/wallet/icon/mobile_menu_icon.png"
                                     alt="menu">
                            </div>
                        </div>


                        <div class="criterias_div">
                            <%--search by date--%>
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
                            <%--search by date end--%>

                            <%--search by amount--%>

                            <div class="amount_parent_div">
                                <div class="div_margin_height2">
                                    <div class="amount_text">
                                        <s:text name="wallet.payment.label.Amount">Amount</s:text>
                                    </div>
                                    <div class="amount_charge">
                                        <span>100</span>
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
                                                <option value="0"><s:text
                                                        name="pages.group.label.all">All</s:text></option>
                                                <s:iterator var="cType"
                                                            value="#session.session_url_partition.walletSetup.availableRates">
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

                            <%--search by amount end--%>


                            <%--search by disputattion type--%>

                            <div class="search_dispute_checkbox">
                                <div class="div_margin_height3">
                                    <div class="search_dispute_type_click_div">
                                        <s:text name="wallet.label.disputation.type">Disputation</s:text>
                                    </div>
                                    <div class="choose_dispute_type"><s:text name="wallet.transaction.credit.card.show.all">Show All</s:text></div>
                                </div>

                                <div class="choose_disputation_type_parent">
                                    <div class="show_only_start">
                                        <div data-id="-1">
                                            <img src="<%=request.getContextPath()%>/img/wallet/icon/wallet-disput_icon.png"
                                                 alt="disput logo">
                                            <s:text name="wallet.login.completed.transaction.dispute.not.yet">Not disposed yet</s:text>
                                        </div>
                                    </div>
                                    <div class="show_only_pending">
                                        <div data-id="<%=DisputeState.PENDING.getId()%>">
                                            <img src="<%=request.getContextPath()%>/img/wallet/icon/wallet-disput_icon_pending.png"
                                                 alt="disput logo">
                                            <s:text name="wallet.login.completed.transaction.disputing">Disputing</s:text>
                                        </div>
                                    </div>
                                    <div class="show_only_approved">
                                        <div data-id="<%=DisputeState.APPROVE.getId()%>">
                                            <img src="<%=request.getContextPath()%>/img/wallet/icon/wallet-disput_icon_approved.png"
                                                 alt="disput logo">
                                            <s:text name="wallet.login.completed.transaction.dispute.approved">Approved</s:text>
                                        </div>
                                    </div>
                                    <div class="show_only_rejected">
                                        <div data-id="<%=DisputeState.REJECT.getId()%>">
                                            <img src="<%=request.getContextPath()%>/img/wallet/icon/wallet-disput_icon_rejected.png"
                                                 alt="disput logo">
                                            <s:text name="wallet.login.completed.transaction.dispute.rejected">Rejected</s:text>
                                        </div>
                                    </div>
                                    <div class="show_only_closed">
                                        <div data-id="<%=DisputeState.ClOSE.getId()%>">
                                            <img src="<%=request.getContextPath()%>/img/wallet/icon/wallet-disput_icon_closed.png"
                                                 alt="disput logo">
                                            <s:text name="wallet.login.completed.transaction.dispute.closed">Closed</s:text>
                                        </div>
                                    </div>
                                    <div class="show_both" align="center">
                                        <div data-id="0"><s:text name="wallet.transaction.credit.card.show.all">Show All</s:text></div>
                                    </div>
                                </div>
                            </div>

                            <%--search by disputattion type end--%>


                            <%--search by transaction type--%>
                            <div class="search_checkbox">
                                <div class="div_margin_height3">
                                    <div class="search_type_click_div">
                                        <s:text name="wallet.transaction.type">Transaction</s:text>
                                    </div>
                                    <div class="choose_type"><s:text
                                            name="wallet.transaction.show.both">Show both</s:text></div>
                                </div>
                                <div class="choose_transaction_type_parent">

                                </div>
                            </div>
                            <%--search by transaction type end--%>
                        </div>
                    </div>
                </div>

            </div>

            <div id="action_queue">
                <%--<a href="pre_make_payment.htm"><s:text name="wallet.action.make.payment"/></a>--%>
            </div>

            <div id="search_result" class="search_result port-menu-list">

            </div>

            <div id="selected_user" class="selected_user">
            </div>


        </div>
    </div>
</div>

<div id="completed_transaction_title">

</div>

<div class="container-fliuid popup_cont">
    <div class="container  container-for-scroll-sliding_dispute">
        <div class="row">
            <div class="row-for-scroll-sliding_dispute">.</div>
            <div class="popup_main">
                  <div class="popup_sm_header_parent">
                    <div class="popup_sm_header">
                        <div class="disput_stat_text">
                            <s:text name="wallet.dispute.label.Dispute">Dispute</s:text>
                        </div>
                        <div>
                            <span>
                                <img src="<%=request.getContextPath()%>/img/wallet/icon/cancel_disput_popup.png"
                                     alt="arrow icon"/>
                            </span>
                              <span>
                                <s:text name="wallet.general.button.Cancel">Cancel</s:text>
                            </span>
                        </div>
                        <div>
                            <span onclick="do_dispute()">
                                <s:text name="wallet.dispute.send">Send</s:text>
                            </span>
                            <span>
                                <img src="<%=request.getContextPath()%>/img/wallet/icon/submit_disput_popup.png"
                                     alt="envelop icon"/>
                            </span>
                        </div>
                        <div style="clear: both"></div>
                    </div>
                </div>
                <div class="col-lg-6 col-md-6 col-sm-6 col-xs-12">
                    <div class="popup_left">
                        <div class="left_first">
                            <img id="profile_img" alt="profile img"/>
                        </div>
                        <div class="a_href_name">
                            <a href="#"> </a>

                        </div>

                        <form action="transaction_data_upload.htm" id="file_upload_form" method="post"
                              enctype="multipart/form-data">
                            <input id="d_transaction_id" type="hidden"/>

                            <div class="dispute_title col-lg-12 col-md-12 col-sm-12 col-xs-12">
                                <input id="d_reason" type="text" placeholder="Title">
                            </div>

                            <div class="popup_textarea col-lg-12 col-md-12 col-sm-12 col-xs-12">
                                <div class="file_upload">
                                    <input type="file" class="file_upload_input" name="data" id="upload_file" multiple
                                           onchange="upload_photo()">
                                </div>
                                <div id="d_content" class="write_message" contenteditable="true">

                                <span class="message_placeholder">
                                     <s:text name="wallet.login.write.message">Write message</s:text>
                                </span>

                                </div>
                            </div>
                        </form>
                    </div>
                </div>
                <div class="col-lg-6 col-md-6 col-sm-6  col-xs-12">
                    <form id="make_payment" action="make_payment.htm" method="post">
                        <div>
                            <div id="data_div" style="overflow: auto; height: 190px">

                            </div>
                        </div>
                    </form>

                </div>
                <div class="arrow-right-payment-popup-dispute"><img src="<%=request.getContextPath()%>/img/wallet/icon/arrow-right-payment-popup.png" alt="arrow icon"></div>
            </div>
        </div>
    </div>
</div>
