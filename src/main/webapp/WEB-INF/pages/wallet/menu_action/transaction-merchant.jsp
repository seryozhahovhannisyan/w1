<%@ page import="com.connectto.general.model.Partition" %>
<%@ page import="com.connectto.general.model.lcp.PartitionLCP" %>
<%@ page import="com.connectto.general.util.ConstantGeneral" %>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="show_pending_transaction.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/wallet/wallet_pending_transaction.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/wallet/block_user.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/wallet/transaction_info.css" type="text/css">
<link href="<%=request.getContextPath()%>/css/general/datepicker_bootstrap.css" rel="stylesheet">
<%--<script type="text/javascript" src="<%=request.getContextPath()%>/js/wallet/wallet_block_user.js"></script>--%>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/general/bootstrap-datepicker.js"></script>
<%--<script type="text/javascript" src="<%=request.getContextPath()%>/js/wallet/wallet_transaction_info.js"></script>--%>
<%
    Partition partition = (Partition) session.getAttribute(ConstantGeneral.SESSION_URL_PARTITION);
    String partitionDns = PartitionLCP.getDNS(partition.getId());
%>
<script type="text/javascript">
    var Logger = new GENERAL_API.ACTION.LOGS;

    var src_completed_icon = "<%=request.getContextPath()%>/img/wallet/icon/pending_transaction.png";
    var src_completed_minus = "<%=request.getContextPath()%>/img/wallet/icon/wallet_minus_icon.png";
    var msg = '<s:text name="wallet.data.not.found">The data not found</s:text>';
    var title_mek = "<s:text name='wallet.general.button.Submit'>Submit</s:text>";

    var transaction = {
        money : 150,
        cT :152
    }

    $(document).ready(function () {
        var orderCode = $('#orderCode').val();
        console.log("orderCode orderCode",orderCode);

        $.ajax({
            url: 'view-locked-transaction.htm',
            type: "post",
            dataType: "json",
            data: {
                orderCode: "9lcbhb"

            },
            success: function (data) {
                if (data.responseDto != null && data.responseDto.status == "SUCCESS") {
                    var witdraw = data.responseDto.response.data;
                    console.log('witdraw ++++++++++++++++++++++++++++', witdraw);
                    var show = [
                        witdraw.closedAt,
                        witdraw.encoded,
                        witdraw.finalState,
                        witdraw.id,
                        witdraw.merchantWithdraw,
                        witdraw.merchantWithdrawId,
                        witdraw.openedAt,
                        witdraw.orderCode,
                        witdraw.setupId,
                        witdraw.setupTotalAmount,
                        witdraw.setupTotalAmountCurrencyType,
                        witdraw.walletTotalPrice,
                        witdraw.walletTotalPriceCurrencyType,
                        witdraw.withdrawAmount,
                        witdraw.withdrawAmountCurrencyType,
                        witdraw.withdrawMerchantTotalTax,
                        witdraw.withdrawMerchantTotalTaxCurrencyType];
                }
                else {


                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                console.log('xhr', xhr)
                console.log('ajaxOptions', ajaxOptions)
                console.log('thrownError', thrownError)
            }
        });

    });



    function approve() {

        var orderCode = $('#orderCode').val();
        $.ajax({
            url: 'approve-withdraw-wallet.htm',
            type: "post",
            dataType: "json",
            async: true,
            data: {
                orderCode: orderCode

            },
            success: function (data) {

                if (data != null && data.responseDto.status == "SUCCESS") {
                    alert("SUCCESS")
                } else {
                    alert("ERROR")
                }

            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status: xhr.status, responseText: xhr.responseText};
                console.log('handle', handle);
                alert("ERROR 2")
            }
        });
    }

    function cancel() {

        var orderCode = $('#orderCode').val();
        $.ajax({
            url: 'cancel-withdraw-wallet.htm',
            type: "post",
            dataType: "json",
            async: true,
            data: {
                orderCode: orderCode

            },
            success: function (data) {

                if (data != null && data.responseDto.status == "SUCCESS") {
                    alert("SUCCESS")
                } else {
                    alert("ERROR" + data.responseDto.messages)
                }

            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status: xhr.status, responseText: xhr.responseText};
                console.log('handle', handle);
                alert("ERROR 2")
            }
        });
    }


</script>


<div class="complited_transaction container-fluid">
    <div class="row main">
        <div id="action_panel1" class="action_panel">

            <%--<div class="row" style="border-bottom: 1px solid #555965">--%>
                <%--<div id="search_user" class="user_search_div div_margin_height1">--%>
                    <%--&lt;%&ndash;<div class="input_before_text"><s:text&ndash;%&gt;--%>
                    <%--&lt;%&ndash;name="wallet.pages.make.search.before.text">Please choose precipitant</s:text></div>&ndash;%&gt;--%>
                    <%--<div class="search_parent_div">--%>
                        <%--<div class="search"></div>--%>
                        <%--<input id="search_user_text" type="search" class="search_user_input" name="searchLike"--%>
                               <%--placeholder="<s:text name="wallet.pages.make.label.search_by">Search Friend or Company</s:text>"--%>
                        <%-->--%>
                    <%--</div>--%>
                    <%--<div class="arrow_div">--%>
                        <%--<span style="display: none">desc</span>--%>

                        <%--<div class="up"></div>--%>
                        <%--<div class="down"></div>--%>
                        <%--<img src="<%=request.getContextPath()%>/img/wallet/icon/arrow_up_icon.png" alt="arrow icon"/>--%>
                    <%--</div>--%>
                    <%--<div class="sort_div">--%>
                        <%--<s:text name="wallet.action.search.result.sort">Sort</s:text>--%>
                    <%--</div>--%>
                <%--</div>--%>
                <%--<div class="date_parent_div">--%>
                    <%--<div class="  datepicker_one  date">--%>
                        <%--<div class="date_text   add-on">--%>
                            <%--<s:text name="wallet.transaction.date">Date</s:text>--%>
                        <%--</div>--%>
                        <%--<input class="multi_input" type="hidden"/>--%>

                        <%--<div class="multi_month">--%>
                            <%--<div class="start_date_parent">--%>
                                <%--<div class="start_date_date  add-on"></div>--%>
                                <%--<div class="start_mouth"></div>--%>
                                <%--<div class="start_year"></div>--%>
                            <%--</div>--%>
                            <%--<div style="clear: both"></div>--%>
                            <%--<div class="end_date_parent">--%>
                                <%--<div class="end_date_date"></div>--%>
                                <%--<div class="end_mouth"></div>--%>
                                <%--<div class="end_year"></div>--%>
                            <%--</div>--%>
                        <%--</div>--%>

                        <%--<div class="one_month">--%>
                            <%--<div class="one_start_date_date  "></div>--%>
                            <%--<div class="one_start_date_line">-</div>--%>
                            <%--<div class="one_end_date_date"></div>--%>
                            <%--<div class="one_end_month_parent">--%>
                                <%--<div class="one_end_mouth"></div>--%>
                                <%--<div class="one_end_year"></div>--%>
                            <%--</div>--%>
                        <%--</div>--%>

                    <%--</div>--%>
                <%--</div>--%>
                <%--<div class="amount_parent_div">--%>
                    <%--<div class="div_margin_height2">--%>
                        <%--<div class="amount_text">--%>
                            <%--<s:text name="wallet.payment.label.Amount">Amount</s:text>--%>
                        <%--</div>--%>
                        <%--<div class="amount_charge">--%>
                            <%--<span>1</span>--%>
                            <%--<span>-</span>--%>
                            <%--<span>10000</span>--%>
                            <%--<span>USD</span>--%>
                        <%--</div>--%>
                    <%--</div>--%>


                    <%--<div class="range_parent_div">--%>
                        <%--<div class="range_popup_parent">--%>
                            <%--<div class="cancel_submit_div">--%>
                                <%--<div class="cancel_">--%>
                                     <%--<span>--%>
                                <%--<img src="<%=request.getContextPath()%>/img/wallet/icon/cancel_disput_popup.png"--%>
                                     <%--alt="arrow icon"/>--%>
                                    <%--</span>--%>
                                    <%--<s:text name="wallet.transaction.button.Cancel">Cancel</s:text>--%>
                                <%--</div>--%>
                                <%--<div class="submit_">--%>

                                    <%--<s:text name="wallet.general.button.Submit">Submit</s:text>--%>
                                    <%--<span onclick="show_send_money_popup_transfer()">--%>
                                <%--<img src="<%=request.getContextPath()%>/img/wallet/icon/submit_disput_popup.png"--%>
                                     <%--alt="envelop icon"/>--%>
                                  <%--</span>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="select_div">--%>
                                <%--<select name="select" class="popup_select_tr">--%>
                                    <%--<option value="0"><s:text name="pages.group.label.all">All</s:text></option>--%>
                                    <%--<s:iterator var="cType"--%>
                                                <%--value="#session.session_url_partition.walletSetup.availableRates">--%>
                                        <%--<option value='<s:property value="#cType.id" />'>--%>
                                            <%--(<s:property value="#cType.name"/>)--%>
                                        <%--</option>--%>
                                    <%--</s:iterator>--%>
                                <%--</select>--%>
                            <%--</div>--%>
                            <%--<div style="clear: both;height: 10px"></div>--%>
                            <%--<div class="choose_amount_div">--%>
                                <%--<s:text name="wallet.completed.transaction.choose.amount">Choose an Amount Range</s:text>--%>
                            <%--</div>--%>
                            <%--<div class="amount_great">--%>
                                <%--<div class="text-right col-lg-4 col-md-4 col-sm-4 col-xs-4">--%>
                                    <%--<s:text name="wallet.completed.transaction.choose.amount.from">From</s:text>--%>
                                <%--</div>--%>
                                <%--<div class=" text-left col-lg-6 col-md-6 col-sm-6 col-xs-6">--%>
                                    <%--<input type="text"/>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="amount_less">--%>
                                <%--<div class="text-right col-lg-4 col-md-4 col-sm-4 col-xs-4">--%>
                                    <%--<s:text name="wallet.completed.transaction.choose.amount.to">To</s:text>--%>
                                <%--</div>--%>
                                <%--<div class="text-left col-lg-6 col-md-6 col-sm-6 col-xs-6">--%>
                                    <%--<input type="text"/>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                        <%--</div>--%>

                    <%--</div>--%>
                <%--</div>--%>

                <%--<div class="search_checkbox">--%>
                    <%--<div class="div_margin_height3">--%>
                        <%--<div class="search_type_click_div">--%>
                            <%--<s:text name="wallet.transaction.type">Transaction</s:text>--%>
                        <%--</div>--%>
                        <%--<div class="choose_type"><s:text name="wallet.transaction.show.both">Show All</s:text></div>--%>
                    <%--</div>--%>
                    <%--<div class="choose_transaction_type_parent">--%>

                    <%--</div>--%>
                <%--</div>--%>

            <%--</div>--%>

            <div id="action_queue">
                <%--<a href="pending_transaction.htm"><s:text name="wallet.general.menu.PendingTransactions"/></a>--%>
            </div>


            <%--<div>--%>
            <%--<ul id='pending_ul' class='search_result  port-menu-list'>--%>

            <%--</ul>--%>
            <%--</div>--%>

            <div id="pending_transaction_title">

                <div class="search_parent_div">
                    <input id="orderCode" type="text" class="search_user_input" name="orderCode"
                           placeholder="secure key"
                    >
                </div>
                <a onclick="cancel()"> cancel</a>
                <a onclick="approve()"> approve</a>
            </div>


        </div>
    </div>
</div>





