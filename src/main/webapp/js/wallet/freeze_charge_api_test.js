/**
 * Created by htdev01 on 12/9/15.
 */

var WALLET = new GENERAL_API.ACTION.WALLET();

// is completed
var send_money_check_tax_test = function () {
    var params = {};
    var data = {};
    params.async = false;
    data.userId = '115';
    data.amount = '10000';
    data.currencyType = '152';
    params.data = data;
    WALLET.send_money_check_tax(params, payment_callback, error_handler);
};

//is completed
//var send_money_preview_test = function (){
//    var params = {};
//    var data = {};
//    params.async =  false;
//    data.userId = '372';
//    data.amount = '10000';
//    data.currencyType = '152';
//    params.data = data;
//    WALLET.send_money_preview(params, payment_callback, error_handler);
//};
// is completed
//var send_money_make_payment_test = function (){
//    var params = {};
//    var data = {};
//    params.async =  false;
//    data.userId = '372';
//    data.amount = '10000';
//    data.currencyType = '152';
//    params.data = data;
//    WALLET.send_money_make_payment(params, payment_callback, error_handler);
//};

/*pending */
var send_money_approve_test = function () {
    var params = {};
    var data = {};
    params.async = false;
    data.transactionId = 5;
    params.data = data;
    WALLET.send_money_approve(params, payment_callback, error_handler);
};
var send_money_reject_test = function () {
    var params = {};
    var data = {};
    params.async = false;
    data.transactionId = 1;
    params.data = data;
    WALLET.send_money_reject(params, payment_callback, error_handler);
};

var request_transaction_check_tax_test = function () {
    var params = {};
    var data = {};
    params.async = false;
    data.userId = '511';
    data.amount = '10000';
    data.currencyType = '152';
    params.data = data;
    WALLET.request_transaction_check_tax(params, payment_callback, error_handler);
};
var request_transaction_preview_test = function () {
    var params = {};
    var data = {};
    params.async = false;
    data.userId = '389';
    data.amount = '10000';
    data.currencyType = '152';
    params.data = data;
    WALLET.request_transaction_preview(params, payment_callback, error_handler);
};
var request_transaction_make_payment_test = function () {
    var params = {};
    var data = {};
    params.async = false;
    data.userId = '389';
    data.amount = '10000';
    data.currencyType = '152';
    params.data = data;
    WALLET.request_transaction_make_payment(params, payment_callback, error_handler);
};
/*pending */
var request_transaction_approve_test = function () {
    var params = {};
    var data = {};
    params.async = false;
    data.transactionId = 5;
    params.data = data;
    WALLET.request_transaction_approve(params, payment_callback, error_handler);
};
var request_transaction_reject_test = function () {
    var params = {};
    var data = {};
    params.async = false;
    data.transactionId = 4;
    params.data = data;
    WALLET.request_transaction_reject(params, payment_callback, error_handler);
};


// is completed
//var load_available_currencies_test = function (){
//    var params = {};
//    var data = {};
//    params.async =  false;
//    data.userId = '372';
//    params.data = data;
//    WALLET.load_available_currencies(params, payment_callback, error_handler);
//};
var load_transaction_notifier_test = function () {
    var params = {};
    params.async = true;
    WALLET.load_transaction_notifier(params, payment_callback, error_handler);
};
var load_notifier_test = function () {
    var params = {};
    params.async = true;
    params.walletId = 25;
    WALLET.load_notifier(params, payment_callback, error_handler);
};

var load_pending_transactions_test = function () {
    var params = {};
    var data = criteria_builder(null);
    params.async = false;
    params.data = data;
    WALLET.load_pending_transactions(params, payment_callback, error_handler);
};
var load_completed_transactions_test = function () {
    var params = {};
    var data = criteria_builder(null);
    params.async = false;
    params.data = data;

    WALLET.load_completed_transactions(params, payment_callback, error_handler);
};

var criteria_builder = function (params) {
    var current_page = (params != null && params.current_page != undefined ) ? params.current_page : '0';
    var order_type = (params != null && params.order_type != undefined ) ? params.order_type : 'desc';
    var date_less = (params != null && params.date_less != undefined) ? params.date_less : "";
    var date_great = (params != null && params.date_great != undefined) ? params.date_great : "";
    var byCurrency = (params != null && params.byCurrency != undefined) ? params.byCurrency : "";
    var rangeAmountGreat = (params != null && params.rangeAmountGreat != undefined) ? params.rangeAmountGreat : "";
    var rangeAmountLess = (params != null && params.rangeAmountLess != undefined) ? params.rangeAmountLess : "";
    var searchLike = (params != null && params.searchLike != undefined) ? params.searchLike : "";
    var transactionType = (params != null && params.transactionType != undefined) ? params.transactionType : "0";
    var disputeState = (params != null && params.disputeState != undefined) ? params.disputeState : "0";
    var data = {};
    data.currentPage = current_page;
    data.orderType = order_type;
    data.rangeDateLess = date_less;
    data.rangeDateGreat = date_great;
    data.byCurrency = byCurrency;
    data.rangeAmountGreat = rangeAmountGreat;
    data.rangeAmountLess = rangeAmountLess;
    data.searchLike = searchLike;
    data.transactionType = transactionType;
    data.disputeState = disputeState;
    return data;
}


/*Wallet Setup services*/
var freeze_test = function () {

    //data -init
    var data = {};
    data.userId = 436;
    data.partitionId = 14;
    data.itemId = 111;
    data.purchaseType = "service";
    data.name = "ride";
    data.description = "ride";
    data.sessionId = 'TU03W9QGWYO0NN11L5RO28DMCHS73QP0';

    data.amount = '1666666666060656BDDDDDDDDDFDFDFD9555555555F5F5550666666666060616B888888888F8F8E8F444444444F4F4D4266666666606069637777777770707D710000000000000401999999999090969EDDDDDDDDDFDFDBD2777777777070797B888888888F8F8E829999999990909A93FFFFFFFFF0F0FFFB333333333F3F3C3';
    data.currencyType = '1444444444040454D444444444F4F454AAAAAAAAAAFAFAAA2DDDDDDDDD0D0DBDCFFFFFFFFFFFFF3F0DDDDDDDDD0D0D3DBFFFFFFFFFFFFFFF6EEEEEEEEE0E0EBE29999999990909A9499999999909092939999999990909E99444444444F4F454266666666606069633333333330303C3B999999999F9F9E90CCCCCCCCC0C0C3C';

    //data -init
    var params = {};
    params.async = false;
    params.data = data;
    WALLET.freeze(params, callback, error_handler);
};

var direct_charge_from_wallet_test = function () {

    //data -init
    var data = {};
    data.userId = 436;
    data.partitionId = 14;
    data.itemId = 111;
    data.purchaseType = "service";
    data.name = "ride";
    data.description = "ride";
    data.sessionId = 'TU03W9QGWYO0NN11L5RO28DMCHS73QP0';

    data.amount = '9DDDDDDDDDFDFD7D5CCCCCCCCC0C0C7CF777777777F7F7D750000000000000409333333333F3F34340000000000000008EEEEEEEEEFEFE3E1222222222020242FCCCCCCCCCFCFCFCF555555555F5F5D53DDDDDDDDD0D0DFDC222222222F2F2024FFFFFFFFF0F0F3F39999999990909E9C888888888F8F8282CCCCCCCCC0C0CBC';
    data.currencyType = '1444444444040454D444444444F4F454AAAAAAAAAAFAFAAA2DDDDDDDDD0D0DBDCFFFFFFFFFFFFF3F0DDDDDDDDD0D0D3DBFFFFFFFFFFFFFFF6EEEEEEEEE0E0EBE29999999990909A9499999999909092939999999990909E99444444444F4F454266666666606069633333333330303C3B999999999F9F9E90CCCCCCCCC0C0C3C';

    //data -init
    var params = {};
    params.async = false;
    params.data = data;
    WALLET.direct_charge_from_wallet(params, callback, error_handler);
};

var cancel_freeze_test = function () {
    var params = {};
    var data = {};
    data.orderCode = 334770895075437;
    params.async = false;
    data.userId = 436;
    data.partitionId = 14;

    data.amount = '1666666666060656BDDDDDDDDDFDFDFD9555555555F5F5550666666666060616B888888888F8F8E8F444444444F4F4D4266666666606069637777777770707D710000000000000401999999999090969EDDDDDDDDDFDFDBD2777777777070797B888888888F8F8E829999999990909A93FFFFFFFFF0F0FFFB333333333F3F3C3';
    data.currencyType = '1444444444040454D444444444F4F454AAAAAAAAAAFAFAAA2DDDDDDDDD0D0DBDCFFFFFFFFFFFFF3F0DDDDDDDDD0D0D3DBFFFFFFFFFFFFFFF6EEEEEEEEE0E0EBE29999999990909A9499999999909092939999999990909E99444444444F4F454266666666606069633333333330303C3B999999999F9F9E90CCCCCCCCC0C0C3C';


    data.sessionId = 'TU03W9QGWYO0NN11L5RO28DMCHS73QP0';
    params.data = data;

    WALLET.cancel_freeze(params, callback, error_handler);
};

var charge_from_wallet_test = function () {
    var params = {};
    var data = {};
    data.orderCode = 334770895075437;
    params.async = false;
    data.userId = 436;
    data.partitionId = 14;

    data.amount = '1666666666060656BDDDDDDDDDFDFDFD9555555555F5F5550666666666060616B888888888F8F8E8F444444444F4F4D4266666666606069637777777770707D710000000000000401999999999090969EDDDDDDDDDFDFDBD2777777777070797B888888888F8F8E829999999990909A93FFFFFFFFF0F0FFFB333333333F3F3C3';
    data.currencyType = '1444444444040454D444444444F4F454AAAAAAAAAAFAFAAA2DDDDDDDDD0D0DBDCFFFFFFFFFFFFF3F0DDDDDDDDD0D0D3DBFFFFFFFFFFFFFFF6EEEEEEEEE0E0EBE29999999990909A9499999999909092939999999990909E99444444444F4F454266666666606069633333333330303C3B999999999F9F9E90CCCCCCCCC0C0C3C';


    data.sessionId = 'TU03W9QGWYO0NN11L5RO28DMCHS73QP0';
    params.data = data;

    WALLET.charge_from_wallet(params, callback, error_handler);
};

var charge_amount_from_wallet_test = function () {
    /*async: false
     data: Object{
     amount: "11"
     currencyType: 152
     sessionId: "25TQS8T08Z264O0Y41GL3ZWVO4N7SVD5"
     userId: 389
     }*/

    var params = {};
    var data = {};
    params.async = false;
    /*data.userId=4;
     data.amount=100;
     data.currencyType=152;
     data.sessionId='7ADRNM9HB3ATIRWDD8Q9C86662Q26BYI';*/

    data.userId = 389;
    data.amount = 11;
    data.currencyType = 152;
    data.sessionId = '25TQS8T08Z264O0Y41GL3ZWVO4N7SVD5';
    params.data = data;
    alert("asasdad")
    WALLET.charge_amount_from_wallet(params, callback, error_handler);
};

var browser_notification_test = function () {
    WALLET.browser_notification(null, null, null);
};

var callback = function (data) {

    var cashAmount = data.walletResponseDto.cashAmount;
    var messages = data.walletResponseDto.messages;
    var status = data.walletResponseDto.status;
    var transactionDto_amount = data.walletResponseDto.transactionDto.amount;
    var transactionDto_currencyType = data.walletResponseDto.transactionDto.currencyType;
    var transactionDto_orderCode = data.walletResponseDto.transactionDto.orderCode;

    var msg = '';
    msg += 'cashAmount=[' + cashAmount + '];';
    msg += 'messages=[' + messages + '];';
    msg += 'status=[' + status + '];';
    msg += 'transactionDto_amount=[' + transactionDto_amount + '];';
    msg += 'transactionDto_currencyType=[' + transactionDto_currencyType + '];';
    msg += 'transactionDto_orderCode=[' + transactionDto_orderCode + '];';
    print_msg(msg, data);
};
var payment_callback = function (data) {

    var data = data;

    var msg = 'responseDto';
    print_msg(msg, data);
};
var error_handler = function (data) {
    var msg = 'status=[' + data.status + ']; responseText =[' + data.responseText + ']';
    print_msg(msg, data);
};


var print_msg = function (msg, data) {
    console.log(msg, data);
};

var merchant_start_transaction_deposit = function () {

    //data -init
    var data = {};
    data.userId = 35;
    // data.userId = 4;
    data.itemId = 111;
    data.name = "merchant deposit";
    data.description = "description";
    data.rationalDuration = 60*5;
    // data.sessionId = '08NP3120F6V5GWRG2T1OQMBUM4BT8EBQ';

    data.amount = 100;
    data.currencyType = 152;

    data.tax = 1;
    data.taxCurrencyType = 152;
    data.taxType = 3;


    //data -init
    var params = {};
    params.async = false;
    params.data = data;
    WALLET.merchant_start_transaction_deposit(params, callback, error_handler);
};
var merchant_start_transaction_withdraw = function () {

    //data -init
    var data = {};
    data.userId = 35;
    // data.userId = 4;
    data.itemId = 111;
    data.name = "merchant deposit";
    data.description = "description";
    data.rationalDuration = 60*5;
    // data.sessionId = '08NP3120F6V5GWRG2T1OQMBUM4BT8EBQ';

    data.amount = 100;
    data.currencyType = 152;

    data.tax = 1;
    data.taxCurrencyType = 152;
    data.taxType = 3;


    //data -init
    var params = {};
    params.async = false;
    params.data = data;
    WALLET.merchant_start_transaction_withdraw(params, callback, error_handler);
};
