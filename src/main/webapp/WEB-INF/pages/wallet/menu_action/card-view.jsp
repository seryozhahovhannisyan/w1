
<%@ page import="com.connectto.general.util.ConstantGeneral" %>
<%@ page import="com.connectto.wallet.model.wallet.lcp.TransactionType" %>
<%@ page import="java.util.List" %>
<%@ page import="com.connectto.general.model.User" %>
<%@ page import="com.connectto.general.model.WalletSetup" %>
<%--
  Created by IntelliJ IDEA.
  User: htdev001
  Date: 9/2/14
  Time: 10:18 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    User user = (User) session.getAttribute(ConstantGeneral.SESSION_USER);
    WalletSetup walletSetup = user.getPartition().getWalletSetup();
    List<TransactionType>  availableCards = walletSetup.parseAvailableCards();
%>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/wallet/wallet_mobile/credit_card_mobile.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/wallet/detect-card.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/general/jquery/jquery_maskedinput.js"></script>
<script type="text/javascript">

    $(document).ready(function () {

        var types = [
            {
                id: 0,
                name: 'all',
                class_: 'show_both',
                value: '<s:text name="wallet.transaction.credit.card.show.all">Show All</s:text>'
            },
            {
                id: 1,
                name: 'all',
                class_: 'show_only_received',
                value: '<s:text name="wallet.transaction.credit.card.show.disabled">Show only disabled</s:text>'

            },
            {
                id: 2,
                name: 'all',
                class_: 'show_only_sended',
                value: '<s:text name="wallet.transaction.credit.card.show.enabled">Show only enabled</s:text>'
            }
        ];

        $('.choose_transaction_type_parent').html('');
        $.each(types, function (i, item) {
            var div = '<div class="' + item.class_ + '">' +
                    '<div data-id="' + item.id + '">' + item.value + '</div>' +
                    '</div>';
            $('.choose_transaction_type_parent').append(div);
        });
        show_dropdown_type_search();
        search_by_type();
        search_row_design();
        add_new_credit_card();
        fill_card_type();
        card_number_limit();
        tranfer_from_card();
        edit_card_details();
        drag_div();
        credid_card_load();
        fill_new_credit_card();
        disable_enable_card();
        delete_card();



    });

    function create_credit_card(nume_surname, card_number, expiry_date, card_cvv, trns_type, user_country, card_zip, user_state, user_city) {
        loader_show();
        var holderName = nume_surname;
        var number = card_number;
        var expiryDate = expiry_date;
        /*dd-mm-yyyy*/
        var cvv = card_cvv;
        var transactionType = trns_type;

        var country = user_country;
        var zip = card_zip;
        var state = user_state;
        var city = user_city;

        $("#create_credit_card").remove();
        var create_credit_card = $('<form>', {
            'action': 'credit-card-create.htm',
            'method': 'post',
            'id': 'create_credit_card'
        })
                .append($('<input>', {
                    'name': 'holderName',
                    'value': holderName,
                    'type': 'hidden'
                }))
                .append($('<input>', {
                    'name': 'number',
                    'value': number,
                    'type': 'hidden'

                }))
                .append($('<input>', {
                    'name': 'expiryDate',
                    'value': expiryDate,
                    'type': 'hidden'
                }))
                .append($('<input>', {
                    'name': 'cvv',
                    'value': cvv,
                    'type': 'hidden'
                }))
                .append($('<input>', {
                    'name': 'type',
                    'value': transactionType,
                    'type': 'hidden'
                }))
                .append($('<input>', {
                    'name': 'country',
                    'value': country,
                    'type': 'hidden'
                }))
                .append($('<input>', {
                    'name': 'zip',
                    'value': zip,
                    'type': 'hidden'
                }))
                .append($('<input>', {
                    'name': 'state',
                    'value': state,
                    'type': 'hidden'
                }))
                .append($('<input>', {
                    'name': 'city',
                    'value': city,
                    'type': 'hidden'
                }));

        $('body').append(create_credit_card);

        create_credit_card.submit();
        loader_hide();

    }
    function edit_credit_card(nume_surname, card_number, expiry_date, card_cvv, trns_type, credit_card_id, user_country, card_zip, user_state, user_city) {
        var holderName = nume_surname;
        var number = card_number;
        var expiryDate = expiry_date;
        /*dd-mm-yyyy*/
        var cvv = card_cvv;
        var transactionType = trns_type;
        var creditCardId = credit_card_id;
        var country = user_country;
        var zip = card_zip;
        var state = user_state;
        var city = user_city;

        $("#edit_credit_card").remove();
        var create_credit_card = $('<form>', {
            'action': 'credit-card-edit.htm',
            'method': 'post',
            'id': 'edit_credit_card'
        })
                .append($('<input>', {
                    'name': 'creditCardId',
                    'value': creditCardId,
                    'type': 'hidden'
                }))
                .append($('<input>', {
                    'name': 'holderName',
                    'value': holderName,
                    'type': 'hidden'
                }))
                .append($('<input>', {
                    'name': 'number',
                    'value': number,
                    'type': 'hidden'
                }))
                .append($('<input>', {
                    'name': 'expiryDate',
                    'value': expiryDate,
                    'type': 'hidden'
                }))
                .append($('<input>', {
                    'name': 'cvv',
                    'value': cvv,
                    'type': 'hidden'
                }))
                .append($('<input>', {
                    'name': 'type',
                    'value': transactionType,
                    'type': 'hidden'
                }))
                .append($('<input>', {
                    'name': 'country',
                    'value': country,
                    'type': 'hidden'
                }))
                .append($('<input>', {
                    'name': 'zip',
                    'value': zip,
                    'type': 'hidden'
                }))
                .append($('<input>', {
                    'name': 'state',
                    'value': state,
                    'type': 'hidden'
                }))
                .append($('<input>', {
                    'name': 'city',
                    'value': city,
                    'type': 'hidden'
                }));

        $('body').append(create_credit_card);

        create_credit_card.submit();
    }

    function show_dropdown_type_search() {
        $(".search_type_click_div").click(function () {
            $(".choose_transaction_type_parent").toggle();
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
                $(".choose_type").html(type_text).css("color", type_color);
                $(".choose_type").attr('data-id', id);
                $(".choose_transaction_type_parent").hide();
            }
        });
    }

    function search_row_design() {
        var user_search_div_height = $(".user_search_div").outerHeight();
        var margin_center_height1 = (user_search_div_height - $(".div_margin_height1").outerHeight()) / 2;
        var margin_center_height2 = (user_search_div_height - $(".div_margin_height2").outerHeight()) / 2;

        console.log("user_search_div_height", user_search_div_height, "margin_center_height1", margin_center_height1, "margin_center_height2", margin_center_height2);
        $(".choose_transaction_type_parent").css("top", user_search_div_height + "px");
        $(".search_checkbox").css("height", user_search_div_height);
        $(".div_margin_height1").css("margin-top", margin_center_height1 + "px");
        $(".div_margin_height2").css("margin-top", margin_center_height2 + "px");

    }
    function fill_new_credit_card() {
        $(".cards_whith_plus").click(function () {
            create_editable_card();
            fill_mont_year_select();
            card_type_detect();





        })
    }
    function fill_card_type() {


        var cards_type = '';
        <%
            for(TransactionType card : availableCards){
            if(card.getIsCreditCard()){
            String cardLogo = "/img/wallet/cardsLogo/"+card.getLogo();
        %>
        cards_type += '<div class="card_type_img_visa ux_card" ><img data-type="<%=card.getId()%>" src="<%=request.getContextPath()%>' + '<%=cardLogo%>' + '"></div>' ;
        <%}}%>

        $('body').delegate('.card_type_edit', 'click', function () {
            var top = $(this).offset().top;
            var left = $(this).offset().left;
            $(".card_type_img").css({'top': top, 'left': left}).html(cards_type).show()
        });

        $('body').delegate('.ux_card', 'click', function (e) {
            if (!e) e = window.event;
            if ($(e.target).closest(".card_type_img")) {
                var src = $(e.target).attr("src");
                var trans_type = $(e.target).data("type");
                $(".card_type_edit").css({
                    "background-image": "url(" + src + ")",
                    "border": "none"
                }).data("image_src", src).data("trans_type", trans_type);
                $(".card_type_img").hide();
                $(".card_awesmoe_font").hide();
            }
        })
    }
    function card_number_limit() {
        $('body').delegate(".card_number", 'focus', function () {
            $(this).mask("9999-9999-9999-9999");
        });

        $('body').delegate(".card_number", 'keyup', function () {
            var val = $(this).val();
            val = val.replace(/[-,_]/g, "");
            $(".card_number_hidden").val(val);
        });

    }
    function fill_mont_year_select() {
        var month_arr = ["<s:text name='calendar.months.january'>january</s:text>", "<s:text name='calendar.months.february'>february</s:text>", "<s:text name='calendar.months.march'>march</s:text>", "<s:text name='calendar.months.april'>april</s:text>", "<s:text name='calendar.months.may'>may</s:text>", "<s:text name='calendar.months.june'>june</s:text>", "<s:text name='calendar.months.july'></s:text>", "<s:text name='calendar.months.august'>august</s:text>", "<s:text name='calendar.months.september'>september</s:text>", "<s:text name='calendar.months.october'>october</s:text>", "<s:text name='calendar.months.november'>november</s:text>", "<s:text name='calendar.months.december'>december</s:text>"];
        var curent_year = new Date().getFullYear();
        for (var i = 0; i < month_arr.length; i++) {

            $(".sel_month").append("<option data-month=" + i + " >" + month_arr[i] + "</option>");
            $(".sel_year").append("<option>" + curent_year + "</option>");
            curent_year++;
        }
    }
    function create_editable_card() {
        var fill_card = '<div class="credit_card_edit">' +
                '<div class="card_header_edit">' +
                '<div class="close_card_edit">' +
                '<s:text name="wallet.close.wallet.X">X close</s:text>' +
                '</div>' +
                '<div class="add_new_credit_card_parent">' +
                '<div class="add_new_credit_card">' +
                '<s:text name="wallet.credit.add.new.credit.card">Add new Credit Card</s:text>' +
                '</div>' +
                '<div class="add_new_credit_next">' +
                '<s:text name="wallet.credit.add.new.credit.card.next">Next</s:text>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div class="card_content">' +
                '<div class="section_1">' +
                '<div style="clear: both; height: 35px"></div>' +
                '<div class="card_name_left_edit col-lg-6 col-md-6 col-sm-6 col-xs-6">' +
                '<div>' +
                '<s:text name="wallet.credit.card.cardholders.name">cardholders name</s:text>' +
                '</div>' +
                '<div class="card_name_left_edit_input ">' +
                '<input id="nume_surname" class="nume_surname" type="text" placeholder="<s:text name="wallet.credit.card.nume.surname">NAME SURNAME</s:text>"/>' +
                '</div>' +
                '</div>' +
                '<div class="card_name_right_edit col-lg-6 col-md-6 col-sm-6 col-xs-6">' +
                '<div>' +
                '<s:text name="wallet.credit.card.card.number">card number</s:text>' +
                '</div>' +
                '<div class="card_name_right_edit_input  ">' +
                '<input type="hidden" class="card_number_hidden"  >' +
                '<input id="card_number" class="card_number" type="text" minlength="19" maxlength="19" placeholder="<s:text name="wallet.credit.card.card.number.placeholder">****-****-****-1111</s:text>"/>' +
                '</div>' +
                '</div>' +
                '</div>' +

                '<div class="section_2">' +
                '<div class="expiry_date_edit col-lg-6 col-md-6 col-sm-6 col-xs-6">' +
                '<div>' +
                '<s:text name="wallet.credit.card.expiry.date">expiry date</s:text>' +
                '</div>' +
                '<div class="expiry_date_input_edit">' +
                '<select class="sel_month select_design">' +

                '</select>' +
                '</div>' +
                '<div class="expiry_date_input_edit">' +
                '<select class="sel_year select_design"> </select>' +
                '</div>' +
                '</div>' +
                '<div class="card_cvv_edit col-lg-6 col-md-6 col-sm-6 col-xs-6">' +
                '<div class="cvv_part_edit col-lg-4 col-md-4 col-sm-4 col-xs-4">' +
                '<div class="cvv_text_edit">' +
                '<s:text name="wallet.credit.card.cvv">cvv</s:text>' +
                '</div>' +
                '<div class="card_cvv_input_edit">' +
                '<input id="card_cvv_edit_input" class="card_cvv_edit_input" type="text"/>' +
                '</div>' +
                '</div>' +
                '<div class="card_type_parent col-lg-8 col-md-8 col-sm-8 col-xs-8">' +
                '<div class="card_type_text">' +
                '<s:text name="wallet.credit.select.card.typeadd">select card  </s:text>' +
                '</div>' +
                '<div   class="card_type_edit">' +
                '<i class="fa fa-credit-card card_awesmoe_font"></i>' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div style="clear: both"></div>' +
                '</div>' +
                '</div>' +
                '</div>';


        var fill_card_part_2 = '<div class="credit_card_edit_part_2">' +
                '<div class="card_header_edit">' +
                '<div class="add_new_credit_card_back">' +
                '<s:text name="wallet.general.Back">Back</s:text>' +
                '</div>' +

                '<div class="add_new_credit_card_optional">' +
                '<s:text name="wallet.credit.Credit.Card.Billing.Address">Credit Card Billing Address(optional)</s:text>' +
                '</div>' +
                '<div class="add_new_credit_finish">' +
                '<s:text name="wallet.credit.add.new.credit.card.finish">Finish</s:text>' +
                '</div>' +
                '</div>' +
                '<div class="card_content">' +
                '<div class="section_1">' +
                '<div style="clear: both; height: 40px"></div>' +
                '<div class="card_name_left_edit col-lg-6 col-md-6 col-sm-6 col-xs-6">' +
                '<div>' +
                '<s:text name="pages.user.settings.general.fields.country">country</s:text>' +
                '</div>' +
                '<div class="card_name_left_edit_input">' +
                '<input id="country" type="text">' +
                '</div>' +
                '</div>' +
                '<div class="card_name_right_edit col-lg-6 col-md-6 col-sm-6 col-xs-6">' +
                '<div>' +
                '<s:text name="wallet.credit.Credit.Card.state">State</s:text>' +
                '</div>' +
                '<div class="card_name_right_edit_input">' +
                '<input id="state" type="text">' +
                '</div>' +
                '</div>' +
                '</div>' +
                '<div class="section_2">' +
                '<div class="expiry_date_edit col-lg-6 col-md-6 col-sm-6 col-xs-6">' +
                '<div>' +
                '<s:text name="wallet.credit.Credit.Card.city">City</s:text>' +
                '</div>' +
                '<div class="expiry_date_input_edit_part2">' +
                '<input id="city" type="text">' +
                '</div>' +
                '</div>' +
                '<div class="card_cvv_edit col-lg-6 col-md-6 col-sm-6 col-xs-6">' +
                '<div class="zip_text_edit">' +
                '<s:text name="wallet.credit.Credit.Card.zip.code">Zip code</s:text>' +
                '</div>' +
                '<div class="card_zip_code">' +
                '<input id="card_zip_code" type="text">' +
                '</div>' +
                '</div>' +
                '<div style="clear: both"></div>' +
                '</div>' +
                '</div>' +
                '</div>';

        $(".card_blur_div").remove();
        $("body").append($("<div class='card_blur_div'><div class='card_type_img'></div><div class='div_for_card'></div></div>"));
        $(".div_for_card").append(fill_card).append(fill_card_part_2);
        $(".credit_card_edit_part_2").hide();
        dragable_divs(".credit_card_edit");
        dragable_divs(".credit_card_edit_part_2");
//        fill_new_credit_card()
//        $("body").delegate(".close_card_edit", 'click',function () {
//            $(".card_blur_div").remove();
//
//        });
        $(document).keydown(function (e) {
            if (e.which == 27) {
                $(".card_blur_div").remove();
            }
        });
        only_number(".card_cvv_input_edit input");
        only_number(".card_number");
        only_number(".card_zip_code input");
        $(".div_for_card input").focus(function () {
            $(this).css("border", "1px solid #018ffd")
        });
        $(".nume_surname").blur(function () {
            $(this).css("border", "1px solid #9b9b9b")
        });

        $(".card_number").blur(function () {

            var val_length = $(".card_number").val().length;
            if (val_length < 19) {
                $(".card_number").css("border", "1px solid red")
            }
            else {
                $(this).css("border", "1px solid #9b9b9b")
            }
        });
        $(".card_cvv").blur(function () {
            $(this).css("border", "1px solid #9b9b9b")
        });
        $("body").delegate(".close_card_edit", 'click', function () {
            $(".card_blur_div").remove();

        });
        $("body").delegate(".add_new_credit_next", 'click', function () {
            $(".credit_card_edit").hide();
            $(".credit_card_edit_part_2").show();

        });
        $("body").delegate(".add_new_credit_card_back", 'click', function () {
            $(".credit_card_edit_part_2").hide();
            $(".credit_card_edit").show();
        });
        $(".card_blur_div").show();
        fill_mont_year_select();

    }
    function credid_card_load() {
        loader_show();
        $.ajax({
            url: "credit-cards-load.htm",
            type: "post",
            dataType: "json",
            async: true,
            cache: true,
            success: function (data) {
                if (data != null && data.responseDto.status == "SUCCESS") {

                    var credit_cards = data.creditCards;
                    var month_arr = ["<s:text name='calendar.months.january'>january</s:text>", "<s:text name='calendar.months.february'>february</s:text>", "<s:text name='calendar.months.march'>march</s:text>", "<s:text name='calendar.months.april'>april</s:text>", "<s:text name='calendar.months.may'>may</s:text>", "<s:text name='calendar.months.june'>june</s:text>", "<s:text name='calendar.months.july'></s:text>", "<s:text name='calendar.months.august'>august</s:text>", "<s:text name='calendar.months.september'>september</s:text>", "<s:text name='calendar.months.october'>october</s:text>", "<s:text name='calendar.months.november'>november</s:text>", "<s:text name='calendar.months.december'>december</s:text>"];

                    var priority = 0;

                    $.each(credit_cards, function (i, item) {

                        if(item.isDeleted){
                            return;
                        }

                        priority++;

                        var holderName = item.holderName;
                        var card_number = item.number;
                        var month = month_arr[new Date(item.expiryDate).getMonth()];
                        var year = new Date(item.expiryDate).getFullYear();
                        var cvv = item.cvv;
                        //var priority = item.priority;
                        var country = item.country;
                        var state = item.state;
                        var city = item.city;
                        var zip = item.zip;
                        var id = item.id;
                        var trans_type = item.transactionType;
                        console.log(trans_type);

                        var enable_disable = item.isEnabled
                                ? '<div data-type="0" data-id="' + id + '" class="cart_disabel"><s:text name="wallet.credit.card.disable">Disable</s:text></div>'
                                : '<div data-type="1" data-id="' + id + '" class="cart_disabel" style="background-image: url(<%=request.getContextPath()%>/img/wallet/icon/wallet_credit_enable.png); color: rgb(0, 200, 101);"><s:text name="wallet.credit.card.enable">Enable</s:text></div>';

                        var new_card =
                                '<div id="item_' + id + ' " class="credit_card credit_card_design">' +
                                '<div class="card_header">' +
                                '<div class="card_pencil card_pencil_div" data-cardid=' + id + '>' +
                                '<s:text name="wallet.credit.card.edit">Edit</s:text>' +
                                '</div>' +
                                enable_disable +
                                '<div data-id="' + id + '" class="card_delete">' +
                                '<s:text name="wallet.credit.card.delete">Delete</s:text>' +
                                '</div>' +
                                '<div class="card_transfer trans' + id + '" data-id="' + id + '">' +
                                '<div class="card_prioirty prop' + id + '"> <span class="prioriti_number">' + priority + '</span></div>' +
                                '<span title="<s:text name='wallet.credit.card.Transfer.long'>Transfer to Connect To Wallet</s:text>"><s:text name="wallet.credit.card.Transfer">Transfer </s:text></span>' +
                                '</div>' +
                                '</div>' +
                                '<div class="card_content">' +
                                '<div>' +
                                '<div class="card_name_left   col-lg-6 col-md-6 col-sm-6 col-xs-6">' +
                                '<div>' +
                                '<s:text name="wallet.credit.card.cardholders.name">cardholders name</s:text>' +
                                '</div>' +
                                '<div class="card_name_left_input hh' + priority + '  ">' +
                                '' + holderName + '' +
                                '</div>' +
                                '</div>' +
                                '<div class="card_name_right col-lg-6 col-md-6 col-sm-6 col-xs-6">' +
                                '<div>' +
                                '<s:text name="wallet.credit.card.card.number">card number</s:text>' +
                                '</div>' +
                                '<div class="card_name_right_input  ">' +
                                '' + card_number + '' +
                                '</div>' +
                                '</div>' +
                                '<div style="clear: both"></div>' +
                                '</div>' +
                                '<div class="expiry_date_parent">' +
                                '<div class="expiry_date col-lg-6 col-md-6 col-sm-6 col-xs-6">' +
                                '<div>' +
                                '<s:text name="wallet.credit.card.expiry.date">expiry date</s:text>' +
                                '</div>' +
                                '<div class="expiry_date_input month">' +
                                '' + month + '' +
                                '</div>' +
                                '<div class="expiry_date_input year">' +
                                '' + year + '' +
                                '</div>' +
                                '</div>' +
                                '<div class="card_cvv col-lg-6 col-md-6 col-sm-6 col-xs-6">' +
                                '<div>' +
                                '<s:text name="wallet.credit.card.cvv">cvv</s:text>' +
                                '</div>' +
                                '<div class="card_cvv_input card_cvv">' +
                                '' + cvv + '' +
                                '</div>' +
                                '<div class="card_type type_cards' + id + '">' +
                                '</div>' +
                                '</div>' +
                                '<div style="clear: both"></div>' +
                                '</div>' +
                                '</div>' +
                                '<input class="country_hidden" type="hidden" value=' + country + '>' +
                                '<input class="state_hidden" type="hidden" value=' + state + '>' +
                                '<input class="city_hidden" type="hidden" value=' + city + '>' +
                                '<input class="zip_hidden" type="hidden" value=' + zip + '>' +
                                '</div>';


                        if (holderName != "" && card_number != "" && month != "" && year != "") {
                            $('.cards_whith_plus').before(new_card);
                            switch (trans_type){
                                case "DISCOVER":
                                    $('.type_cards' + id + '').css("background-image", "url('<%=request.getContextPath()%>/img/wallet/cardsLogo/discover.png')");
                                    break;
                                case "MASTER_CARD":
                                    $('.type_cards' + id + '').css("background-image", "url('<%=request.getContextPath()%>/img/wallet/cardsLogo/mastercard.png')");
                                    break;
                                case "VISA_CARD":
                                    $('.type_cards' + id + '').css("background-image", "url('<%=request.getContextPath()%>/img/wallet/cardsLogo/vsisa.png')");
                                    break;
                                case "AMERICAN_EXPRESS":
                                    $('.type_cards' + id + '').css("background-image", "url('<%=request.getContextPath()%>/img/wallet/cardsLogo/american_express.png')");
                                    break;
                                case "DINERS_CLUB":
                                    $('.type_cards' + id + '').css("background-image", "url('<%=request.getContextPath()%>/img/wallet/cardsLogo/diners_club.png')");
                                    break;
                                case "JCB":
                                    $('.type_cards' + id + '').css("background-image", "url('<%=request.getContextPath()%>/img/wallet/cardsLogo/jcb.png')");
                                    break;
                            }
                        }
                    });
                    footerPlace();
                    loader_hide();
                }
                else {
                    alert("error 1");
                    write_error_msg(msg);
                    Logger.save(Logger.LOG_LEVEL.ERROR, 'credit-card-load', 0, "incorrect response:");
                    loader_hide()
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {

                Logger.save(Logger.LOG_LEVEL.ERROR, 'credit-card-load', xhr.status, xhr.responseText);
                loader_hide()
            }
        })
    }
    function add_new_credit_card() {
        $('body').delegate('.add_new_credit_finish', 'click', function () {

            var namesurname_val = $(".nume_surname").val().trim();
            var cardnumber_val = $(".card_number").val().trim();
            var mont_val = $('.sel_month option:selected').text().trim();
            var mont_option_val = $('.sel_month option:selected').data("month");
            var year_val = $('.sel_year option:selected').text().trim();
            var cvv_val = $(".card_cvv_input_edit input").val().trim();
            var tans_type = $(".card_type_edit").data("trans_type");
            var card_type = $(".card_type_edit").data("image_src");
            var country_val = $('#country').val().trim();///$(".card_name_left_edit_input input").val().trim();
            var state_val = $('#state').val().trim();//$(".card_name_right_edit_input input").val().trim();
            var city_val = $(".expiry_date_input_edit_part2 input").val().trim();
            var zipcode_val = $(".card_zip_code input").val().trim();
            var background_img = $(".card_type_edit").css("background-image");
            if (mont_option_val > 10) {
                mont_option_val = mont_option_val
            }
            else {
                mont_option_val = "0" + mont_option_val
            }
            var date = "01-" + mont_option_val + "-" + year_val + "";

            if (namesurname_val != "" && cardnumber_val != "" && mont_val != "" && year_val != "") {
                var val_length = $(".card_number").val().length;
                if (val_length < 19) {
                    $(".credit_card_edit_part_2").hide();
                    $(".credit_card_edit").show();
                    $(".card_number").css("border", "1px solid red");
                }
                else if (background_img == "none") {
                    $(".credit_card_edit_part_2").hide();
                    $(".credit_card_edit").show();
                    $(".card_type_edit").css("border", "1px solid red")
                }
                else if (cvv_val == 0) {
                    $(".credit_card_edit_part_2").hide();
                    $(".credit_card_edit").show();
                    $(".card_cvv_edit_input").css("border", "1px solid red")
                }
                else {
                    var card_id = $(this).data("card_id");
                    if (card_id) {
                        alert('namesurname_val')
                        edit_credit_card(namesurname_val, cardnumber_val, date, cvv_val, tans_type, card_id, country_val, zipcode_val, state_val, city_val);
                    }
                    else {
                        create_credit_card(namesurname_val, cardnumber_val, date, cvv_val, tans_type, country_val, zipcode_val, state_val, city_val);
                    }
                    $(".card_blur_div").remove();
                }
            }
            else {
                $(".credit_card_edit_part_2").hide();
                $(".credit_card_edit").show();
                if (namesurname_val == "") {
                    $(".nume_surname").css("border", "1px solid red")
                }
                else {
                    $(".nume_surname").css("border", "1px solid #9b9b9b")
                }
                if (cardnumber_val == "") {
                    $(".card_number").css("border", "1px solid red")
                }
                else {
                    $(".card_number").css("border", "1px solid #9b9b9b")
                }
                if (cvv_val == "") {
                    $(".card_cvv_edit_input").css("border", "1px solid red")
                }
                else {
                    $(".card_cvv_edit_input").css("border", "1px solid #9b9b9b")
                }
            }


        })
    }
    function transfer_from_card(){

        $(".credit_send_money").click(function () {
            var card_id = $(this).data("card_id");
            var amount = $(".untitiled").val().trim();
            var test = {cardId: card_id, amount: amount};

            $.ajax({
                url: "/credit-card-transfer",
                type: "post",
                dataType: "json",
                data: {test:JSON.stringify(test)},
                success: function (data) {
                    console.log("nor data", data)
                },

                error: function (xhr, ajaxOptions, thrownError) {
                    alert("error 2");
//                    write_error_msg(msg);
//                    Logger.save(Logger.LOG_LEVEL.ERROR, 'credit-card-load', xhr.status, xhr.responseText);
                    loader_hide();
                }
            })
        })
    }
    function tranfer_from_card() {
        $("body").delegate('.card_transfer', 'click', function () {
            var card_id = $(this).data("id");
            var card_numebr = $(this).closest(".card_transfer ").find(".card_prioirty span").text();
            var money_transfer_card = '<div class="money_transfer_div_popup">' +
                    '<div   class="money_transfer_div">' +
                    '<div class="close_transfer">' +
                    '<s:text name="wallet.general.button.Cancel">Cancel</s:text>' +
                    '</div>' +
                    '<div class="credit_send_money" data-card_id = "'+card_id+'">' +
                    '<s:text name="wallet.login.send.money.transfer">Transfer</s:text>' +
                    '</div >' +
                    '<div style="clear: both;height:70px"></div>' +
                    '<div class="credit_card_number_info">' +
                    '<s:text name="wallet.credit.card.transfer.from.card">Transfer from card </s:text>'+
                    '  '+card_numebr+''+
                    '</div>' +
                    '<div class="balance_text">' +
                    '<s:text name="wallet.login.your.current.balance.is">Your Current Balance is</s:text>' +
                    '<span><input class="untitiled" type="text"></span>' +
                    '<span>USD</span>' +
                    '</div>' +
                    '</div>' +
                    '</div>';

            if($(".money_transfer_div_popup")){
                $(".money_transfer_div_popup").remove();
            }
            $("body").append(money_transfer_card);
            transfer_from_card();
            dragable_divs(".money_transfer_div_popup");

            $('body').delegate(".close_transfer", 'click', function () {
                $('.money_transfer_div_popup').remove()
            })
        })

    }
    function edit_card_details() {

        console.log("minchev click  iii");

        $("body").delegate('.card_pencil_div', 'click', function () {

            loader_show()
            var card_id = $(this).data("cardid");
            var month_arr = ["<s:text name='calendar.months.january'>january</s:text>", "<s:text name='calendar.months.february'>february</s:text>", "<s:text name='calendar.months.march'>march</s:text>", "<s:text name='calendar.months.april'>april</s:text>", "<s:text name='calendar.months.may'>may</s:text>", "<s:text name='calendar.months.june'>june</s:text>", "<s:text name='calendar.months.july'></s:text>", "<s:text name='calendar.months.august'>august</s:text>", "<s:text name='calendar.months.september'>september</s:text>", "<s:text name='calendar.months.october'>october</s:text>", "<s:text name='calendar.months.november'>november</s:text>", "<s:text name='calendar.months.december'>december</s:text>"];
            $.ajax({
                url: "credit-card-view.htm",
                type: "post",
                dataType: "json",
                async: true,
                cache: true,
                data: {
                    creditCardId: card_id
                },
                success: function (data) {
                    if (data != null && data.responseDto.status == "SUCCESS") {

                        var card = data.creditCard
                        var namesurname_val = card.holderName;
                        var cardnumber_val = card.number;
                        var month = month_arr[new Date(card.expiryDate).getMonth()];
                        var year = new Date(card.expiryDate).getFullYear();
                        var cvv_val = card.cvv;
                        var country_val = card.country;
                        var state_val = card.state;
                        var city_val = card.city;
                        var zipcode_val = card.zip;
                        create_editable_card();
                        fill_new_credit_card();
                        $(".nume_surname").val(namesurname_val);
                        $(".card_number").val(cardnumber_val);
                        $('.sel_month').val(month);
                        $('.sel_year').val(year);
                        $(".card_cvv_edit_input").val(cvv_val);
//            $(".card_type_edit").css("background-image","url('"+card_type+"')").data("image_src",card_type);
                        $(".card_awesmoe_font").hide();
                        $('#country').val(country_val);
                        $('#state').val(state_val);
                        //$(".card_name_left_edit_input input").val(country_val);
                        //$(".card_name_right_edit_input input").val(state_val);
                        $(".expiry_date_input_edit_part2 input").val(city_val);
                        $(".card_zip_code input").val(zipcode_val);
                        $(".add_new_credit_finish").data("card_id", card_id);
                        loader_hide();
                    }
                    else {
                        alert("error 1");
                        write_error_msg(msg);
                        Logger.save(Logger.LOG_LEVEL.ERROR, 'credit-card-load', 0, "incorrect response:");
                        loader_hide()
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    write_error_msg(msg);
                    Logger.save(Logger.LOG_LEVEL.ERROR, 'credit-card-load', xhr.status, xhr.responseText);
                    loader_hide()
                }

            })
        });


    }
    function disable_enable_card() {
        $("body").delegate(".cart_disabel", "click", function () {
            var action, color, img, this_, text, type_;
            this_ = $(this);
            //var card_id = $(".card_pencil_div").data("cardid");
            var card_id = $(this).data("id");
            if ($(this).data("type") == 1) {
                action = 'credit-card-enable.htm';
                type_ = 0;
                color = '#d9d9d9';
                img = "/img/wallet/icon/wallet_credit_disable.png";
                text = "<s:text name="wallet.credit.card.disable">Disable</s:text>";
            }
            else if ($(this).data("type") == 0) {
                action = 'credit-card-disable.htm';
                type_ = 1;
                color = '#00c865';
                img = "<%=request.getContextPath()%>/img/wallet/icon/wallet_credit_enable.png";
                text = "<s:text name="wallet.credit.card.enable">Enable</s:text>";
            }
            loader_show();
            $.ajax({
                url: action, //'credit-card-enable.htm',
                type: "post",
                dataType: "json",
                async: true,
                data: {
                    creditCardId: card_id

//                    creditCardIdes : [6,5]
                },
                success: function (data) {
                    if (data != null && data.responseDto.status == "SUCCESS") {
                        this_.text(text).css("background-image", "url(" + img + ")");
                        this_.css("color", '' + color + '');
                        this_.data("type", type_);
                    } else {
                        var handle = null;
                        if (data != null && data.responseDto != null) {
                            handle = {status: data.responseDto.status, responseText: data.walletResponseDto.messages};
                        } else {
                            handle = {status: 0, responseText: 'empty data'};
                        }
                        console.log('handle', handle);
                    }
                    loader_hide();
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    var handle = {status: xhr.status, responseText: xhr.responseText};
                    console.log('handle', handle);
                    loader_hide();
                }
            });
        })
    }
    function delete_card() {
        $("body").delegate(".card_delete", "click", function () {
            loader_show();
            //var card_id = $(".card_pencil_div").data("cardid");
            var card_id = $(this).data("id");
            $.ajax({
                url: 'credit-card-delete.htm',
                type: "post",
                dataType: "json",
                async: true,
                data: {
                    creditCardId: card_id
                },
                success: function (data) {


                    if (data != null && data.responseDto.status == "SUCCESS") {
                        goToAction('credit-cards-view.htm');
                    } else {
                        var handle = null;
                        if (data != null && data.responseDto != null) {
                            handle = {status: data.responseDto.status, responseText: data.walletResponseDto.messages};
                        } else {
                            handle = {status: 0, responseText: 'empty data'};
                        }
                        console.log('handle', handle);
                    }

                },
                error: function (xhr, ajaxOptions, thrownError) {
                    var handle = {status: xhr.status, responseText: xhr.responseText};
                    console.log('handle', handle);
                    loader_hide();
                }
            });
        })
    }
    function drag_div() {

        $(".cards_place_div").sortable({
            containment: "parent",
            tolerance: "pointer",
            cursor: "pointer",
            revert: true,
            update: function (event, ui) {
                var string = "";
                var sorted = $(this).sortable("serialize", {key: " "});
                sorted = sorted.replace(/\=/g, '').trim().split('&');
                for (var i = 0; i < sorted.length; i++) {
                    string = string + sorted[i].trim() + ",";
                }
                loader_show();
                $.ajax({
                    url: 'credit-card-reorder-priorities.htm',
                    type: "post",
                    dataType: "json",
                    async: true,
                    data: {
                        creditCardIdes: string

                    },
                    success: function (data) {


                        if (data != null && data.responseDto.status == "SUCCESS") {
                            goToAction('credit-cards-view.htm');
                        } else {
                            var handle = null;
                            if (data != null && data.responseDto != null) {
                                handle = {
                                    status: data.responseDto.status,
                                    responseText: data.walletResponseDto.messages
                                };
                            } else {
                                handle = {status: 0, responseText: 'empty data'};
                            }
                            console.log('handle', handle);
                        }

                    },
                    error: function (xhr, ajaxOptions, thrownError) {
                        var handle = {status: xhr.status, responseText: xhr.responseText};
                        console.log('handle', handle);
                        loader_hide();
                    }
                });

            }
        });
    }
    function card_type_detect() {
        var src,trans_type ;
        $(".card_number").detectCard();
        $("body").delegate('.card_number','cardChange', function(e, card){
            switch (card.type)
            {
                case "mastercard":
                    src = '/img/wallet/cardsLogo/mastercard.png';
                    trans_type = 5;
                    break;
                case "visa":
                    src = '/img/wallet/cardsLogo/vsisa.png';
                    trans_type = 6;
                    break;
                case "american-express":
                    src = '/img/wallet/cardsLogo/american_express.png';
                    trans_type = 7;
                    break;
                case "discover":
                    src = '/img/wallet/cardsLogo/discover.png';
                    trans_type = 8;
                    break;
                case "jcb":
                    src = '/img/wallet/cardsLogo/jcb.png';
                    trans_type = 10;
                    break;
                case "diners-club":
                    src = '/img/wallet/cardsLogo/diners_club.png';
                    trans_type = 9;
                    break;
                case "maestro":
                    src = '/img/wallet/cardsLogo/diners_club.png';
                    break;
            }

            $(".card_type_edit").css({
                "background-image": "url(" + src + ")",
                "border": "none"
            }).data("image_src", src).data("trans_type", trans_type);
            $(".card_awesmoe_font").hide();
        })

    }
</script>

<div class="container-fluid">
    <div class="row main">
        <div id="action_panel" class="action_panel">

                <div id="search_user" class="user_search_div">
                    <div class="card_sort_div div_margin_height1 ">
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
                    <div class="search_checkbox  ">
                        <div class="div_margin_height2">
                            <div class="search_type_click_div">
                                <s:text name="wallet.transaction.type">Transaction</s:text>
                            </div>
                            <div class="choose_type"><s:text name="wallet.transaction.show.both">Show All</s:text></div>
                        </div>
                        <div class="choose_transaction_type_parent">
                        </div>
                    </div>
                </div>

        </div>
    </div>
</div>

<div class="cards_place_div">
    <%--add card div--%>
    <div class="cards_whith_plus"></div>




    <%--<div class="cont_drag">--%>
    <%--<div class="number1"></div>--%>
    <%--<div class="number2"></div>--%>
    <%--<div class="number3"></div>--%>
    <%--<div class="number4"></div>--%>
    <%--</div>--%>
    <%--<div class="credit_card1">--%>
    <%--<div class="card_header">--%>
    <%--<div class="card_pencil">--%>
    <%--<s:text name="wallet.credit.card.edit">Edit</s:text>--%>
    <%--</div>--%>
    <%--<div class="cart_disabel">--%>
    <%--<s:text name="wallet.credit.card.disable">Disable</s:text>--%>
    <%--</div>--%>
    <%--<div class="card_delete">--%>
    <%--<s:text name="wallet.credit.card.delete">Delete</s:text>--%>
    <%--</div>--%>
    <%--<div class="card_prioirty">--%>
    <%--<span><s:text name="wallet.credit.card.prioirty">Prioirty:</s:text></span>--%>
    <%--<span class="prioriti_number">1</span>--%>
    <%--</div>--%>
    <%--</div>--%>

    <%--<div class="card_content">--%>

    <%--<div>--%>
    <%--<div class="card_name_left col-lg-6 col-md-6 col-sm-6 col-xs-6">--%>
    <%--<div>--%>
    <%--<s:text name="wallet.credit.card.cardholders.name">cardholders name</s:text>--%>
    <%--</div>--%>
    <%--<div class="card_name_left_input">--%>
    <%--NUME SURNUME--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--<div class="card_name_right col-lg-6 col-md-6 col-sm-6 col-xs-6">--%>
    <%--<div>--%>
    <%--<s:text name="wallet.credit.card.card.number">card number</s:text>--%>
    <%--</div>--%>
    <%--<div class="card_name_right_input">--%>
    <%--****-****-****-1111--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--<div style="clear: both"></div>--%>
    <%--</div>--%>

    <%--<div class="expiry_date_parent">--%>
    <%--<div class="expiry_date col-lg-6 col-md-6 col-sm-6 col-xs-6">--%>
    <%--<div>--%>
    <%--<s:text name="wallet.credit.card.expiry.date">expiry date</s:text>--%>
    <%--</div>--%>
    <%--<div class="expiry_date_input">--%>
    <%--MONT--%>
    <%--</div>--%>
    <%--<div class="expiry_date_input">--%>
    <%--YEAR--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--<div class="card_cvv col-lg-6 col-md-6 col-sm-6 col-xs-6">--%>
    <%--<div>--%>
    <%--<s:text name="wallet.credit.card.cvv">cvv</s:text>--%>
    <%--</div>--%>
    <%--<div class="card_cvv_input">--%>
    <%--ffg--%>
    <%--</div>--%>
    <%--<div class="card_type">--%>

    <%--</div>--%>
    <%--</div>--%>
    <%--<div style="clear: both"></div>--%>
    <%--</div>--%>
    <%--</div>--%>
    <%--</div>--%>


    <%--<div class="credit_card1">
        <div class="card_header"></div>
        <div class="card_content"></div>
    </div>
    <div class="credit_card1">
        <div class="card_header"></div>
        <div class="card_content"></div>
    </div>--%>
</div>







