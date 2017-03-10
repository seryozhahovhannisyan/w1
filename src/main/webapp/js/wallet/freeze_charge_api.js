/**
 * Created by htdev01 on 12/9/15.
 */
GENERAL_API.createNameSpace("GENERAL_API.ACTION");
GENERAL_API.ACTION.WALLET = function (params) {

    /*send money actions*/
    var send_money_check_tax = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        loader_show();
        $.ajax({
            url: 'send-money-check-tax.htm',
            type: "post",
            dataType: "json",
            async: true,
            data: params.data,
            success: function (data) {
                if (data != null && data.responseDto.status == "SUCCESS") {
                    callback(data);
                loader_hide();
                } else {
                    var handle = null;
                    if(data != null && data.responseDto != null){
                        handle = { status : data.responseDto.status, responseText : data.responseDto.messages} ;
                        loader_hide();
                    } else {
                        handle = {status : 0, responseText : 'empty data'};
                        loader_hide();
                    }
                    error_handler(handle);
                    loader_hide();
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);
                loader_hide();
            }
        });
    };

    var send_money_preview = function (params, callback, error_handler){
        loader_show();
        var aSync =  params.async == null ? false : params.async;
        $.ajax({
            url: 'send-money-preview.htm',
            type: "post",
            dataType: "json",
            async: aSync,
            data: params.data,
            success: function (data) {
                if (data != null && data.responseDto.status == "SUCCESS") {
                    callback(data);
                } else {
                    var handle = null;
                    if(data != null && data.responseDto != null){
                        handle = { status : data.responseDto.status, responseText : data.responseDto.messages} ;
                        loader_hide();
                    } else {
                        handle = {status : 0, responseText : 'empty data'};
                        loader_hide();
                    }
                    error_handler(handle);
                    loader_hide();
                }
                loader_hide();
            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);
                loader_hide();
            }
        });
    };

    var send_money_make_payment = function (params, callback, error_handler){
        loader_show();
        var aSync =  params.async == null ? false : params.async;
        $.ajax({
            url: 'send-money-make-payment.htm',
            type: "post",
            dataType: "json",
            async: aSync,
            data: params.data,
            success: function (data) {
                if (data != null && data.responseDto.status == "SUCCESS") {
                    callback(data);
                } else {
                    var handle = null;
                    if(data != null && data.responseDto != null){
                        handle = { status : data.responseDto.status, responseText : data.responseDto.messages} ;

                    } else {
                        handle = {status : 0, responseText : 'empty data'};

                    }
                    error_handler(handle);

                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);

            }
        });
    };

    var send_money_approve = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        loader_show();
        $.ajax({
            url: 'send-money-approve.htm',
            type: "post",
            dataType: "json",
            async: aSync,
            data: params.data,
            success: function (data) {
                if (data != null && data.responseDto.status == "SUCCESS") {
                    callback(data,params)
                } else {
                    var handle = null;
                    if(data != null && data.responseDto != null){
                        handle = { status : data.responseDto.status, responseText : data.responseDto.messages} ;
                    } else {
                        handle = {status : 0, responseText : 'empty data'};
                    }
                    error_handler(handle);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);
            }
        });
    };

    var send_money_reject = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        loader_show();
        $.ajax({
            url: 'send-money-reject.htm',
            type: "post",
            dataType: "json",
            async: aSync,
            data: params.data,
            success: function (data) {
                if (data != null && data.responseDto.status == "SUCCESS") {
                    callback(data, params)
                } else {
                    var handle = null;
                    if(data != null && data.responseDto != null){
                        handle = { status : data.responseDto.status, responseText : data.responseDto.messages} ;
                    } else {
                        handle = {status : 0, responseText : 'empty data'};
                    }
                    error_handler(handle);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);
            }
        });
    };

    /*request transaction actions*/

    var request_transaction_check_tax = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        loader_show();
        $.ajax({
            url: 'request-transaction-check-tax.htm',
            type: "post",
            dataType: "json",
            async: aSync,
            data: params.data,
            success: function (data) {
                console.log("data check taxic",data);
                if (data != null && data.responseDto.status == "SUCCESS") {
                    callback(data);
                } else {
                    var handle = null;
                    if(data != null && data.responseDto != null){
                        handle = { status : data.responseDto.status, responseText : data.responseDto.messages} ;
                    } else {
                        handle = {status : 0, responseText : 'empty data'};
                    }
                    error_handler(handle);
                }
                loader_hide();
            },
            error: function (xhr, ajaxOptions, thrownError) {
                console.log("xhr",xhr,"ajaxOptions 555",ajaxOptions,"thrownError 666",thrownError)
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);
            }
        });
    };

    var request_transaction_preview = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        loader_show();
        $.ajax({
            url: 'request-transaction-preview.htm',
            type: "post",
            dataType: "json",
            async: aSync,
            data: params.data,
            success: function (data) {
                if (data != null && data.responseDto.status == "SUCCESS") {
                    callback(data)
                } else {
                    var handle = null;
                    if(data != null && data.responseDto != null){
                        handle = { status : data.responseDto.status, responseText : data.responseDto.messages} ;
                    } else {
                        handle = {status : 0, responseText : 'empty data'};
                    }
                    error_handler(handle);
                }
                loader_hide();
            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);
            }
        });
    };

    var request_transaction_make_payment = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        loader_show();
        $.ajax({
            url: 'request-transaction-make-payment.htm',
            type: "post",
            dataType: "json",
            async: aSync,
            data: params.data,
            success: function (data) {
                if (data != null && data.responseDto.status == "SUCCESS") {
                    callback(data)
                    loader_hide();
                } else {
                    var handle = null;
                    if(data != null && data.responseDto != null){
                        handle = { status : data.responseDto.status, responseText : data.responseDto.messages} ;
                    } else {
                        handle = {status : 0, responseText : 'empty data'};
                    }
                    error_handler(handle);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);
            }
        });
    };

    var request_transaction_approve = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        loader_show();
        $.ajax({
            url: 'request-transaction-approve.htm',
            type: "post",
            dataType: "json",
            async: aSync,
            data: params.data,
            success: function (data) {
                if (data != null && data.responseDto.status == "SUCCESS") {
                    callback(data,params)
                } else {
                    var handle = null;
                    if(data != null && data.responseDto != null){
                        handle = { status : data.responseDto.status, responseText : data.responseDto.messages} ;
                    } else {
                        handle = {status : 0, responseText : 'empty data'};
                    }
                    error_handler(handle);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);
            }
        });
    };

    var request_transaction_reject = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        loader_show();
        $.ajax({
            url: 'request-transaction-reject.htm',
            type: "post",
            dataType: "json",
            async: aSync,
            data: params.data,
            success: function (data) {
                if (data != null && data.responseDto.status == "SUCCESS") {
                    callback(data,params)
                } else {
                    var handle = null;
                    if(data != null && data.responseDto != null){
                        handle = { status : data.responseDto.status, responseText : data.responseDto.messages} ;
                    } else {
                        handle = {status : 0, responseText : 'empty data'};
                    }
                    error_handler(handle);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);
            }
        });
    };

    /*transactions*/

    var load_available_currencies = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        loader_show();
        $.ajax({
            url: 'load-available-currencies.htm',
            type: "post",
            dataType: "json",
            async: aSync,
            data: params.data,

            success: function (data) {
                if (data != null && data.responseDto.status == "SUCCESS") {
                    callback(data);


                } else {
                    var handle = null;
                    if(data != null && data.responseDto != null){
                        handle = { status : data.responseDto.status, responseText : data.responseDto.messages} ;
                        loader_hide();
                    } else {
                        handle = {status : 0, responseText : 'empty data'};

                    }
                    error_handler(handle);

                }
                loader_hide();
            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);

            }
        });
    };

    var load_transaction_notifier = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        var walletId = params.walletId == null ? 0 : params.walletId;
        var host = params.host == null ? '' : '' + params.host;
        if(walletId > 0){
            function get_notification() {

                var url = host + '/service/notifier/load-notifier/' + walletId;
                console.log('start get_notification ',url);
                console.log('start host ',host);
                $.ajax({
                    url:  url,
                    type: "post",
                    dataType: "json",
                    async: aSync,
                    success: function (data) {
                        if (data != null && data.status == "SUCCESS") {
                            callback(data, params)
                        }
                        else {
                            var handle = null;
                            if (data != null ) {
                                handle = {status: data.status, responseText: data.messages};
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
            setTimeout(get_notification,100);
            setInterval(get_notification,20000);
        }
    };


    var load_pending_transactions = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        var current_page = params.data.currentPage ? params.data.currentPage :  "0";
        loader_show();
        $.ajax({
            url: 'load-pending-transactions.htm',
            type: "post",
            dataType: "json",
            async: aSync,
            data: params.data,
            success: function (data) {
                console.log("pendingi statusner@", data.responseDto.status)
                if (data != null && data.responseDto.status == "SUCCESS") {
                    callback(data,current_page);
                    console.log("data pendingic",data)
                }
                else if(data != null && data.responseDto.status == "DATA_NOT_FOUND"){
                    loader_hide();
                    $(".search_result").empty();
                    write_error_msg('search_result', 'action_queue');
                    // LOGGER.save(Logger.LOG_LEVEL.ERROR, 'search_user', 0, "searched search_like_val =[" + search_like_val + "],responseText=[incorrect incoming data]");
                }
                else {
                    var handle = null;
                    if(data != null && data.responseDto != null){
                        handle = { status : data.responseDto.status, responseText : data.responseDto.messages} ;
                        loader_hide();
                    } else {

                        handle = {status : 0, responseText : 'empty data'};
                        loader_hide();
                    }
                    error_handler(handle);
                    loader_hide();
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);
                loader_hide();
            }
        });
    };

    var load_completed_transactions = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        var current_page = params.data.currentPage ? params.data.currentPage :  "0";
        $.ajax({
           url: 'load-completed-transactions.htm',
           type: "post",
           dataType: "json",
           async: aSync,
           data: params.data,
           success: function (data) {
                if (data != null && data.responseDto.status == "SUCCESS") {
                    callback(data,current_page)
                }
                    else if(data != null && data.responseDto.status == "DATA_NOT_FOUND"){
                    loader_hide();
                    $(".search_result").empty();
                    write_error_msg('search_result', 'action_queue');
                    // LOGGER.save(Logger.LOG_LEVEL.ERROR, 'search_user', 0, "searched search_like_val =[" + search_like_val + "],responseText=[incorrect incoming data]");
                }
                else {
                    var handle = null;
                    if(data != null && data.responseDto != null){
                        handle = { status : data.responseDto.status, responseText : data.responseDto.messages} ;
                    } else {
                        handle = {status : 0, responseText : 'empty data'};
                    }
                    error_handler(handle);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {

                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);
            }
        });
    };

    var load_exchanged_transaction = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        var current_page = params.data.currentPage ? params.data.currentPage :  "0";
        $.ajax({
           url: 'load-exchanged-transactions.htm',
           type: "post",
           dataType: "json",
           async: aSync,
           data: params.data,
           success: function (data) {
                if (data != null && data.responseDto.status == "SUCCESS") {
                    callback(data,current_page)
                } else {
                    var handle = null;
                    if(data != null && data.responseDto != null){
                        handle = { status : data.responseDto.status, responseText : data.responseDto.messages} ;
                    } else {
                        handle = {status : 0, responseText : 'empty data'};
                    }
                    error_handler(handle);
                }
            },
           error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);
            }
        });
    };




    /*Wallet setup services*/
    var freeze = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        $.ajax({
            url: 'freeze-wallet.htm',
            type: "post",
            dataType: "json",
            async: aSync,
            data: params.data,
            success: function (data) {
                if (data != null && data.walletResponseDto.status == "SUCCESS") {
                    callback(data)
                } else {
                    var handle = null;
                    if(data != null && data.walletResponseDto != null){
                        handle = { status : data.walletResponseDto.status, responseText : data.walletResponseDto.messages} ;
                    } else {
                        handle = {status : 0, responseText : 'empty data'};
                    }
                    error_handler(handle);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);
            }
        });
    };

    var direct_charge_from_wallet = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        $.ajax({
            url: 'direct-charge-from-wallet.htm',
            type: "post",
            dataType: "json",
            async: aSync,
            data: params.data,
            success: function (data) {
                if (data != null && data.walletResponseDto.status == "SUCCESS") {
                    callback(data)
                } else {
                    var handle = null;
                    if(data != null && data.walletResponseDto != null){
                        handle = { status : data.walletResponseDto.status, responseText : data.walletResponseDto.messages} ;
                    } else {
                        handle = {status : 0, responseText : 'empty data'};
                    }
                    error_handler(handle);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);
            }
        });
    };

    var cancel_freeze = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        $.ajax({
            url: 'cancel-freeze-wallet.htm',
            type: "post",
            dataType: "json",
            async: aSync,
            data: params.data,
            success: function (data) {
                if (data != null && data.walletResponseDto.status == "SUCCESS") {
                    callback(data)
                } else {
                    var handle = null;
                    if(data != null && data.walletResponseDto != null){
                        handle = { status : data.walletResponseDto.status, responseText : data.walletResponseDto.messages} ;
                    } else {
                        handle = {status : 0, responseText : 'empty data'};
                    }
                    error_handler(handle);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);
            }
        });
    };

    var charge_from_wallet = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        $.ajax({
            url: 'charge-from-wallet.htm',
            type: "post",
            dataType: "json",
            async: aSync,
            data: params.data,
            success: function (data) {
                if (data != null && data.walletResponseDto.status == "SUCCESS") {
                    callback(data)
                } else {
                    var handle = null;
                    if(data != null && data.walletResponseDto != null){
                        handle = { status : data.walletResponseDto.status, responseText : data.walletResponseDto.messages} ;
                    } else {
                        handle = {status : 0, responseText : 'empty data'};
                    }
                    error_handler(handle);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);
            }
        });
    };

    var charge_amount_from_wallet = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        $.ajax({
            url: 'charge-amount-from-wallet.htm',
            type: "post",
            dataType: "json",
            async: aSync,
            data: params.data,
            success: function (data) {
                if (data != null && data.walletResponseDto.status == "SUCCESS") {
                    callback(data)
                } else {
                    var handle = null;
                    if(data != null && data.walletResponseDto != null){
                        handle = { status : data.walletResponseDto.status, responseText : data.walletResponseDto.messages} ;
                    } else {
                        handle = {status : 0, responseText : 'empty data'};
                    }
                    error_handler(handle);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);
            }
        });
    };

    var browser_notification = function (params, callback, error_handler){


        if (!Notification) {
            error_handler();
            return;
        }

        if (Notification.permission !== "granted")
            Notification.requestPermission();
        else {

            var host = params.host == null ? '' : '' + params.host;
            var title = params.title == null ? 'You have new transaction' : '' + params.title;

            var notification = new Notification(title, {
                icon: host +'/img/wallet/logos/connecttotv_wallet.png',
                body: params.text
            });

            sonds(params.url);
            notification.onclick = function () {
                callback()
            };
        }
    };

    /*merchant demo*/

    var merchant_start_transaction_deposit = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        $.ajax({
            url: 'start-deposit.htm',
            type: "post",
            dataType: "json",
            async: aSync,
            data: params.data,
            success: function (data) {
                if (data != null && data.walletResponseDto.status == "SUCCESS") {
                    callback(data)
                } else {
                    var handle = null;
                    if(data != null && data.walletResponseDto != null){
                        handle = { status : data.walletResponseDto.status, responseText : data.walletResponseDto.messages} ;
                    } else {
                        handle = {status : 0, responseText : 'empty data'};
                    }
                    error_handler(handle);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);
            }
        });
    };

    var merchant_start_transaction_withdraw = function (params, callback, error_handler){
        var aSync =  params.async == null ? false : params.async;
        $.ajax({
            url: 'start-withdraw.htm',
            type: "post",
            dataType: "json",
            async: aSync,
            data: params.data,
            success: function (data) {
                if (data != null && data.walletResponseDto.status == "SUCCESS") {
                    callback(data)
                } else {
                    var handle = null;
                    if(data != null && data.walletResponseDto != null){
                        handle = { status : data.walletResponseDto.status, responseText : data.walletResponseDto.messages} ;
                    } else {
                        handle = {status : 0, responseText : 'empty data'};
                    }
                    error_handler(handle);
                }
            },
            error: function (xhr, ajaxOptions, thrownError) {
                var handle = {status:xhr.status, responseText : xhr.responseText} ;
                error_handler(handle);
            }
        });
    };



    GENERAL_API.ACTION.WALLET = function () {
        return publicApi;
    };

    var publicApi = {
        send_money_check_tax : send_money_check_tax,
        send_money_preview : send_money_preview,
        send_money_make_payment : send_money_make_payment,
        send_money_approve : send_money_approve,
        send_money_reject : send_money_reject,
        request_transaction_check_tax : request_transaction_check_tax,
        request_transaction_preview : request_transaction_preview,
        request_transaction_make_payment : request_transaction_make_payment,
        request_transaction_approve : request_transaction_approve,
        request_transaction_reject : request_transaction_reject,
        load_available_currencies : load_available_currencies,
        load_transaction_notifier : load_transaction_notifier,
        load_pending_transactions : load_pending_transactions,
        load_completed_transactions : load_completed_transactions,
        load_exchanged_transaction : load_exchanged_transaction,
        browser_notification : browser_notification,
        freeze : freeze,
        direct_charge_from_wallet : direct_charge_from_wallet,
        cancel_freeze : cancel_freeze,
        charge_from_wallet : charge_from_wallet,
        charge_amount_from_wallet : charge_amount_from_wallet,
        merchant_start_transaction_deposit : merchant_start_transaction_deposit,
        merchant_start_transaction_withdraw : merchant_start_transaction_withdraw
    };

    return publicApi;
};