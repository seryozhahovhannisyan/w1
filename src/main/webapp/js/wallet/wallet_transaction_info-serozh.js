$(document).ready(function () {

    });

function transaction_info(params) {
    var close = params.close ? params.close : "";
    var src = params.src ? params.src : "";
    var ns = params.ns ? params.ns : "";
    var message = params.message ? params.message : "";
    var transfer_fee = params.transfer_fee ? params.transfer_fee : "";
    var total_debit = params.total_debit ? params.total_debit : "";
    var transfer = params.transfer ? params.transfer : "";
    var transfer_cost = params.transfer_cost ? params.transfer_cost : "";
    var trans_exch_fee = params.trans_exch_fee ? params.trans_exch_fee : "";
    var exchange_fee = params.exchange_fee ? params.exchange_fee : "";
    var trans_fee_det = params.trans_fee_det ? params.trans_fee_det : "";
    var fromTotalPrice = params.fromTotalPrice ? params.fromTotalPrice : "-";
    var fromTotalPriceCurrencyType = params.fromTotalPriceCurrencyType ? params.fromTotalPriceCurrencyType : "-";
    var productAmount = params.productAmount ? params.productAmount : "-";
    var productCurrencyType = params.productCurrencyType ? params.productCurrencyType : "-";
    var totalAmountTaxPrice = params.totalAmountTaxPrice ? params.totalAmountTaxPrice : "-";
    var transferTaxPrice = params.transferTaxPrice ? params.transferTaxPrice : "-";
    var transferExchangeTaxPrice = params.transferExchangeTaxPrice ? params.transferExchangeTaxPrice : "-";
    var attach_file = params.attach_file ? params.attach_file : "-";
    var img = params.img ? params.img : "-";
    var print = params.print ? params.print : "-";
    var download = params.download ? params.download : "-";
    var email = params.email ? params.email : "-";
    var transaction_id = params.transaction_id ? params.transaction_id : "-";
    var send = params.send ? params.send : "-";
    var success_message = params.success_message ? params.success_message : "-";
    var error_message = params.error_message ? params.error_message : "-";
    var url_after_close = params.url_after_close ? params.url_after_close : "-";

    var cont = '<div  class="container-fliuid popup_cont_transfer" >' +
                    '<div class="container ">'+
                        '<div class="popup_main_transfer_bkg">'+
                            '<div id="DivIdToPrint" class="popup_main_transfer_ap" >'+
                                '<div class="popup_sm_header_parent_ap">'+
                                    '<div class="popup_sm_header_transfer">'+
                                        '<div class="transfer_fee">'+
                                            ''+transfer_fee+''+
                                        '</div>'+
                                        '<div>'+
                                            '<span class="clsoe_info_popup" >'+
                                                ''+close+''+
                                            '</span>'+
                                        '</div>'+
                                    '</div>' +
                                '</div>'+
                                '<div class="success_mesage">'+success_message+'</div>'+
                                '<div class="error_mesage">'+error_message+'</div>'+
                                '<div style="clear: both"></div>'+
                                '<div class="popup_transfer_info">'+
                                    '<div class="row">'+
                                    '<div class="div_img_transaction">' +
                                        ''+img+'' +
                                    '</div>' +
                                    '<div class="name_sur_trans_inf" >'+ns+'</div>'+
                                    '<div id="check_payment_fee_msg_box" class="popup_message_div " >' +
                                        ''+message+''+
                                    '</div>'+
                                    '<div class="cntr-res " >'+
                                        '<span class="ttl col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                            ''+total_debit+''+
                                        '</span>'+
                                        '<span class="ttr  tot-tr col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                            ''+fromTotalPrice+' '+fromTotalPriceCurrencyType+''+
                                        '</span>'+
                                    '</div>'+
                                    '<div style="clear: both"></div>'+
                                        '<div class="cntr-res" >'+
                                            '<span class="ttl col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                                ''+transfer+''+
                                            '</span>'+
                                            '<span class="ttr trans col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                                ''+productAmount+' '+productCurrencyType+''+
                                            '</span>'+
                                        '</div>'+
                                        '<div class="cntr-res" >'+
                                            '<span class="ttl col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                                ''+transfer_cost+''+
                                            '</span>'+
                                            '<span class="ttr  trans_cost   col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                                  ''+totalAmountTaxPrice+' <span class="display_exch">'+fromTotalPriceCurrencyType+'</span>'+
                                            '</span>'+

                                        '</div>'+
                                    '</div>'+

                                    '<div class="row  det-des" >'+
                                        '<div class="det-des-1 " >'+
                                            '<div class="det-tem " >'+
                                                '<span class="ttl col-lg-6 col-md-6 col-sm-6 col-xs-6"  >'+
                                                    '<span class="display_exch">'+transfer_fee+'</span> <span> </span>'+
                                                '</span>'+
                                                '<span class="ttr  trans_fee col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                                    ''+totalAmountTaxPrice+' '+fromTotalPriceCurrencyType+''+
                                                '</span>'+
                                            '</div>'+
                                            '<div class="det-tem " >'+
                                                '<span class="ttl col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                                    ''+trans_exch_fee+''+
                                                '</span>'+
                                                '<span class="ttr trans-exchange col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                                    ''+transferTaxPrice+' '+fromTotalPriceCurrencyType+''+
                                                '</span>'+
                                            '</div>'+
                                            '<div class="det-tem " >'+
                                                '<span class="ttl col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                                    ''+exchange_fee+''+
                                                '</span>'+
                                                '<span class="ttr exchange_fee col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                                    ''+transferExchangeTaxPrice+' '+fromTotalPriceCurrencyType+''+
                                                '</span>'+
                                            '</div>'+
                                            '<div style="clear: both"></div>' +
                                            '<div class="det-tem " >'+
                                                '<div class="attached_files_par_div" >' +
                                                    ''+attach_file+''+
                                                '</div>'+
                                                '<div class="attached_files">'+
                                                '</div>'+
                                            '</div>'+
                                        '</div>'+
                                    '</div>'+
                                    '<div class="print_down_email">'+
                                        '<span  class="email" >'+
                                            '<i class="fa fa-envelope-o" aria-hidden="true"></i>'+email+''+
                                        '</span>'+
                                        '<span  class="print" onclick="print_page()">'+
                                            '<i class="fa fa-print" aria-hidden="true"></i> '+print+''+
                                        '</span>'+
                                        '<span  onclick="save_as_pdf('+transaction_id+')" >'+
                                            '<i class="fa fa-download" aria-hidden="true"></i> '+download+''+
                                        '</span>' +
                                        '<div class="email_valid_message"></div>'+
                                    '</div>'+
                                '</div>'+
                            '</div>'+
                        '</div>'+
                    '</div>'+
                '</div>';



    $("body").append(cont);
    $(".popup_cont_transfer").show();
    $(".popup_main").draggable();
    clsoe_info_popup();
    transaction_email(send,transaction_id,email);
    clsoe_info_popup(url_after_close);

}

function walletExchange(params) {

    var close = params.close;
    var src = params.src;
    var ns = params.ns;
    var message = params.message;
    var transfer_fee = params.transfer_fee;
    var total_debit = params.total_debit;
    var transfer = params.transfer;
    var transfer_cost = params.transfer_cost;
    var trans_exch_fee = params.trans_exch_fee;
    var exchange_fee = params.exchange_fee;
    var trans_fee_det = params.trans_fee_det;
    var exchangedAt = params.exchangedAt;
    var newMoneyPaidTaxPrice = params.newMoneyPaidTaxPrice;
    var rateAmount = params.rateAmount;
    var newCurrencyType = params.newCurrencyType;
    var money = params.money;
    var oldCurrencyType = params.oldCurrencyType;
    var totalTax = params.totalTax;
    var img = params.img;
    var attach_file = params.attach_file;
    var print = params.print;
    var download = params.download;
    var email = params.email;
    var totalTaxType = params.totalTaxType;
    var walletExchangePendings = params.walletExchangePendings;
    var send = params.send;
    var walletExchangeId = params.walletExchangeId

    var cont = '<div  class="container-fliuid popup_cont_transfer" >' +
                    '<div class="container ">'+
                        '<div class="popup_main_transfer_bkg">'+
                            '<div id="DivIdToPrint" class="popup_main_transfer_ap" >'+
                                '<div class="popup_sm_header_parent_ap">'+
                                    '<div class="popup_sm_header_transfer">'+
                                        '<div class="transfer_fee">'+
                                            ''+transfer_fee+''+
                                        '</div>'+
                                        '<div>'+
                                            '<span class="clsoe_info_popup" >'+
                                                ''+close+''+
                                            '</span>'+
                                        '</div>'+
                                    '</div>' +
                                '</div>'+
                                '<div class="success_mesage">'+success_message+'</div>'+
                                '<div class="error_mesage">'+error_message+'</div>'+
                                '<div style="clear: both"></div>'+
                                '<div class="popup_transfer_info">'+
                                    '<div class="row">'+
                                    '<div class="div_img_transaction">' +
                                        ''+img+'' +
                                    '</div>' +
                                    '<div class="name_sur_trans_inf" >'+ns+'</div>'+
                                    '<div id="check_payment_fee_msg_box" class="popup_message_div " >' +
                                        ''+message+''+
                                    '</div>'+
                                    '<div class="cntr-res " >'+
                                        '<span class="ttl col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                            ''+total_debit+''+
                                        '</span>'+
                                        '<span class="ttr  tot-tr col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                            ''+fromTotalPrice+' '+fromTotalPriceCurrencyType+''+
                                        '</span>'+
                                    '</div>'+
                                    '<div style="clear: both"></div>'+
                                        '<div class="cntr-res" >'+
                                            '<span class="ttl col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                                ''+transfer+''+
                                            '</span>'+
                                            '<span class="ttr trans col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                                ''+productAmount+' '+productCurrencyType+''+
                                            '</span>'+
                                        '</div>'+
                                        '<div class="cntr-res" >'+
                                            '<span class="ttl col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                                ''+transfer_cost+''+
                                            '</span>'+
                                            '<span class="ttr  trans_cost col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                                  ''+totalAmountTaxPrice+' '+fromTotalPriceCurrencyType+''+
                                            '</span>'+
                                        '</div>'+
                                    '</div>'+

                                    '<div class="row  det-des" >'+
                                        '<div class="det-des-1 " >'+
                                            '<div class="det-tem " >'+
                                                '<span class="ttl col-lg-6 col-md-6 col-sm-6 col-xs-6"  >'+
                                                    ''+transfer_fee+''+
                                                '</span>'+
                                                '<span class="ttr  trans_fee col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                                    ''+totalAmountTaxPrice+' '+fromTotalPriceCurrencyType+''+
                                                '</span>'+
                                            '</div>'+
                                            '<div class="det-tem " >'+
                                                '<span class="ttl col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                                    ''+trans_exch_fee+''+
                                                '</span>'+
                                                '<span class="ttr trans-exchange col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                                    ''+transferTaxPrice+' '+fromTotalPriceCurrencyType+''+
                                                '</span>'+
                                            '</div>'+
                                            '<div class="det-tem " >'+
                                                '<span class="ttl col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                                    ''+exchange_fee+''+
                                                '</span>'+
                                                '<span class="ttr exchange_fee col-lg-6 col-md-6 col-sm-6 col-xs-6" >'+
                                                    ''+transferExchangeTaxPrice+' '+fromTotalPriceCurrencyType+''+
                                                '</span>'+
                                            '</div>'+
                                            '<div style="clear: both"></div>' +
                                            '<div class="det-tem " >'+
                                                '<div class="attached_files_par_div" >' +
                                                    ''+attach_file+''+
                                                '</div>'+
                                                '<div class="attached_files">'+
                                                '</div>'+
                                            '</div>'+
                                        '</div>'+
                                    '</div>'+
                                    '<div class="print_down_email">'+
                                        '<span  class="email" >'+
                                            '<i class="fa fa-envelope-o" aria-hidden="true"></i>'+email+''+
                                        '</span>'+
                                        '<span  class="print" onclick="print_page()">'+
                                            '<i class="fa fa-print" aria-hidden="true"></i> '+print+''+
                                        '</span>'+
                                        '<span  onclick="save_as_pdf('+transaction_id+')" >'+
                                            '<i class="fa fa-download" aria-hidden="true"></i> '+download+''+
                                        '</span>' +
                                        '<div class="email_valid_message"></div>'+
                                    '</div>'+
                                '</div>'+
                            '</div>'+
                        '</div>'+
                    '</div>'+
                '</div>';



    $("body").append(cont);
    $(".popup_cont_transfer").show();
    $(".popup_main").draggable();
    clsoe_info_popup();
    transaction_email(send,transaction_id,email);
    clsoe_info_popup(url_after_close);

}


function save_as_pdf(transaction_id){
    var my_form=document.createElement('FORM');
    $("#myForm").remove();
    my_form.id='myForm';
    my_form.name='myForm';
    my_form.method='POST';
    my_form.action='transaction-info-download.htm';

    var my_tb=document.createElement('INPUT');
    my_tb.type='hidden';
    my_tb.name='transactionId';
    my_tb.value=transaction_id;
    my_form.appendChild(my_tb);
    document.body.appendChild(my_form);
    my_form.submit();
}

function print_page() {
    var divToPrint = document.getElementById('DivIdToPrint');
    var newWin = window.open('','Print-Window');
    newWin.document.open();
    newWin.document.write('<html><link rel="stylesheet" href="/css/wallet/transaction_info.css"  ><body onload="window.print()">'+divToPrint.innerHTML+'</body></html>');
    newWin.document.close();
    setTimeout(function(){newWin.close();},200);
}

 function clsoe_info_popup(url_after_close){
     $("body").delegate(".clsoe_info_popup", "click", function () {
         $(".popup_cont_transfer").hide();
         goToAction(url_after_close)
     });
 }

function close_input(email) {
    $("body").delegate(".close_email_input", "click", function () {

        $(".input_div").fadeOut(500,function(){
            $(".send_email").html('<i class="fa fa-envelope-o" aria-hidden="true">').append(email).addClass("email").removeClass("send_email");
        });
    })
}

function isValidEmail(emailText) {
    var pattern = new RegExp(/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i);
    return pattern.test(emailText);
};

function transaction_email(send,transaction_id,email) {
    $("body").delegate(".email", "click", function () {
        $(".email").text(send).addClass("send_email").removeClass("email");
        $(".input_div").remove();
        $(".print_down_email").prepend("<div class='input_div'><div class='close_email_input'></div><input type='email' class='input_email'  required/></div>");

        $(".input_email").hide().fadeIn(500, function () {
            $(".close_email_input").css("background-image","url('/img/wallet/icon/send_money_upload_delete.png')");

        });

        $("body").delegate(".send_email", "click", function () {
            var email_val = $(".input_email").val();
            if(email_val!="") {
                if( !isValidEmail(email_val) ) {
                    $(".email_valid_message").html("your Email address   syntax is incorrect");
                    $(".input_email").css("border","1px solid red")
                }
                else {
                    $(".input_email").css("border", "1px solid #a0a0a0");
                    $(".email_valid_message").html("");
                    var email_val = $(".input_email").val();
                    var my_form = document.createElement('FORM');
                    $("#myForm").remove();
                    my_form.id = 'myForm';
                    my_form.name = 'myForm';
                    my_form.method = 'POST';
                    my_form.action = 'transaction-info-email.htm';
                    var my_tb = document.createElement('INPUT');
                    my_tb.type = 'hidden';
                    my_tb.name = 'transactionId';
                    my_tb.value = transaction_id;
                    my_form.appendChild(my_tb);
                    var my_emali = document.createElement('INPUT');
                    my_emali.type = 'hidden';
                    my_emali.name = 'email';
                    my_emali.value = email_val;
                    my_form.appendChild(my_emali);
                    document.body.appendChild(my_form);
                    my_form.submit();
                }
            }
            else {
                $(".input_email").css("border","1px solid red");
                $(".email_valid_message").html("you must fill the email fild");
            }
        });
    close_input(email)
    })
}

$(document).keydown(function (e) {
    if (e.which == 27) {
        $(".popup_cont_transfer").hide();
    }
});
