/**
 * Created by Serozh on 2/8/16.
 */
$(document).ready(function () {
    alert("credit card test enabled")
//    create_credit_card();
//    alert('222222222222')
  //  edit_credit_card();
    enable_disable_delete_default();

})


function create_credit_card(){
    alert('11111111111111111111111')
    var holderName = 'holderName ';
    var number = 'number';
    var expiryDate = '01-03-2016';/*dd-mm-yyyy*/
    var cvv = 'cvv';
    var transactionType = '6';

    var country = 'country';
    var zip = 'zip';
    var state = 'state';
    var city = 'city';

    $( "#create_credit_card" ).remove();
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
}

function edit_credit_card(){
    var holderName = 'aaaaa ';
    var number = 'aaaaa';
    var expiryDate = '11-05-2016';/*dd-mm-yyyy*/
    var cvv = 'aaa';
    var transactionType = '7';
    var creditCardId = '3';

    var country = 'country';
    var zip = 'zip';
    var state = 'state';
    var city = 'city';

    $( "#edit_credit_card" ).remove();
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

function enable_disable_delete_default(){
    var creditCardId = '3';
//    var action = 'credit-card-enable.htm';
//    var action = 'credit-card-disable.htm';
//    var action = 'credit-card-delete.htm';
//    var action = 'credit-card-make-default.htm';
    var action = 'credit-card-reorder-priorities.htm';

    $.ajax({
        url: action,
        type: "post",
        dataType: "json",
        async: false,
        data: {
            //creditCardId : creditCardId
            creditCardIdes : [1,4,2,3]
        },
        success: function (data) {
            if (data != null && data.responseDto.status == "SUCCESS") {
                alert("ok")
            } else {
                var handle = null;
                if(data != null && data.responseDto != null){
                    handle = { status : data.responseDto.status, responseText : data.walletResponseDto.messages} ;
                } else {
                    handle = {status : 0, responseText : 'empty data'};
                }
                console.log('handle',handle);
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            var handle = {status:xhr.status, responseText : xhr.responseText} ;
            console.log('handle',handle);
        }
    });
}