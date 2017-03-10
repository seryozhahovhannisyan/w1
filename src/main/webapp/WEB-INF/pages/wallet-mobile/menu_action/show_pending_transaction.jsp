<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
    function show_pending_transaction(transaction_id, src, ns) {
        console.log("transaction_id", transaction_id);
        console.log("src", src);
        console.log("ns", ns);
        var close = "<s:text name="button.close">close</s:text>";

        var img = '<img alt="avatar" src="' + src + '"/>';
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
        $.ajax({
            url: 'show-pending-transaction.htm',
            type: "post",
            dataType: "json",
            data: {
                transactionId: transaction_id
            },
            success: function (data) {
                if (data.responseDto != null && data.responseDto.status == "SUCCESS") {
                    $(".popup_main_transfer_bkg_exchange").hide();
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
                        success_message: success_message,
                        error_message: error_message,


                    };
                    transaction_info(params);
                    console.log("attached_file_arr", attached_file_arr);
                    if (attached_file_arr.length > 0) {
                        $(".attached_files_par_div").show();
                        $.each(attached_file_arr, function (key, value) {
                            $(".attached_files").append("<div><a  a href ='/transaction_data_download.htm?dataFileName=" + value.fileName + "&transactionId=" + transaction_id + "'  style='display: block;cursor: pointer;'><span>" + value.fileName + "</span><span style='margin: 0 10px;'>" + value.size + "kb</span><span>" + value.transactionId + "</span></a></div>")
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
</script>