/**
 * Created by Serozh on 2/15/16.
 */
$(document).ready(function () {
    change_tabs(".terms_conditions", ".parent_div_terms_conditions");
    change_tabs(".default_currency", ".parent_div_default_currency");
    change_tabs(".change_password", ".parent_div_password");
    change_tabs(".blocked_users", ".blocked_user_parent");
    change_tabs(".sounds", ".mute_unmute_parent");
    selected_option();
    show_range_div();
    call_change_password();

    //unblock_user();
});

function change_tabs(sel1, sel2) {
    var div_arr = [".terms_conditions", ".change_password", ".default_currency", ".blocked_users",".sounds"];
    $(sel1).click(function () {
        var length = $(".left_part div").length;
        for (var i = 0; i < length; i++) {
            $(div_arr[i]).css("background-color", "transparent");
        }
        $(this).css("background-color", "#2f3747");
        $(".parent_div_default_currency").hide();
        $(".parent_div_password").hide();
        $(".blocked_user_parent").hide();
        $(".parent_div_terms_conditions").hide();
        $(".mute_unmute_parent").hide();
        $(sel2).show();

        if(sel1 == ".change_password"){
            $("#password").html("");
            $("#newPassword").html("");
        }
        else if(sel1 == ".default_currency"){

            $(".message_div").html("");
        }
        else if(sel1 == ".blocked_users"){
            call_load_blocked_users();
            call_load_users();

        }
    })


};

function selected_option(){
    var val = $(".select_currency select option:selected").val();

    $(".select_currency select").change(function(){
        var this_ = $(this);
        change_currency_type_check_tax(this_, val);
    })
}

function show_range_div(){
    var k = 0;
    $(".range_fee").click(function () {
        switch (k){
            case 0:
                $(".ranges").show(500);
                k=1;
                break;
            case 1:
                $(".ranges").hide(500);
                k=0;
                break;
        }

    })
}

function call_change_password(){
    $(".submit_button").click(function () {
        change_password();
    })
}
function change_password (){
    var password = $("#password").val();
    var newPassword = $("#newPassword").val();
var data = {
    password : password,
    newPassword : newPassword
}
    loader_show();
    $.ajax({
        url : 'change-password.htm' ,
        type: "post",
        dataType: "json",
        data :data,
        success: function (data) {
            if (data.responseDto != null && data.responseDto.status == "SUCCESS") {
                var handle = {
                    status: data.responseDto.status,
                    responseText: "dzer gaxtnabar@ hajoxutyamb poxvel e"
                };
                wallet_succses(handle);



            }
            else {
                var handle = null;
                if (data != null ) {
                    handle = {
                        status: data.responseDto.status,
                        responseText: "texi e unecel sxal"
                    };
                    wallet_error_handler(handle);

                    loader_hide();
                } else {
                    handle = {status: 0, responseText: 'empty data'};
                    wallet_error_handler(handle);
                    loader_hide();
                }


            }
        },
        error: function (xhr) {
            var handle = {status: xhr.status, responseText: xhr.responseText};
            wallet_error_handler(handle);
            loader_hide();
        }
    });

}
function ul_rezult_height(selector){
    var doc_height = Math.max(
        document.body.scrollHeight, document.documentElement.scrollHeight,
        document.body.offsetHeight, document.documentElement.offsetHeight,
        document.body.clientHeight, document.documentElement.clientHeight
    );
    var win_height = $(window).outerHeight();
    var person_panel_height = $(".person_parent").outerHeight(true);
    var footter_height = $(".footer").outerHeight(true);
    var user_search_height = $("#search_user").outerHeight(true);
    var content = doc_height - person_panel_height - footter_height - user_search_height;
    $(selector).css("height",content+"px");
    console.log("doc_height_send", doc_height, "win_height_send", win_height,"person_panel_height",person_panel_height,"footter_height",footter_height,"content",content,"user_search_height",user_search_height);
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
                    search_user(data_current_page, data_search_like, data_order_type);
                }
            }
            $(this).off("scroll")
        }
    });
}
function search_user(current_page, search_like, order_type,block) {
    loader_show();
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
                console.log("sendmioneyic",data);
                var cont;
                var cur_page = data.currentPage;
                var is_last = data.isLast;
                if (current_page == null || current_page <= 2) {
                    //clean_profile();
                    cont = $("<ul   class='port-menu-list logged_accountList search_result' data-order_type='" + order_type + "' data-action='search_user' data-search_like='" + search_like + "' data-current_page='" + cur_page + "' />");
                    $(".users_list").html(cont);


                } else {
                    $(".search_result").attr('data-current_page', cur_page).attr('data-is_last', is_last);
                    cont = $(".search_result");
                }
                ul_rezult_height(".search_result");

                var search_users = data.searchUsers;
                if (search_users != null) {
                    $.each(search_users, function (i, item) {
                        var search_user = item;
                        if (search_user != null) {
                            var id = search_user.id;

                            var name_surname = search_user.name + ' ' + search_user.surname
                            var li = $('<li class="searched_item"></li>').appendTo(cont);
                            var div = $('<div class="info_parent_div"></div>').appendTo(li);
                            var user_photo_path = IMAGE_BASE_URL + id;
                            $(div).append("<div class='user_div  div" + id + "'><img  class='thumb'/></div>");
                            $(".thumb").attr("src", user_photo_path);
                            var innerSpan = $('<div class="detales"/>').appendTo(div);
                            $('.div' + id + ' img').click(function () {
                                show_send_money_popup(id, name_surname, user_photo_path);
                            });
                            $("<a class='name name_div" + id + "'></a> ").text(search_user.name + ' ' + search_user.surname).appendTo(innerSpan);
                            $(div).append('<div class="removed_img" onclick="block_user_call(' + id + ',\'' + name_surname + '\',\'' + user_photo_path + '\',this)"><i class="fa fa-lock" aria-hidden="true"></i> <p class="remove_text">' + block + '</p></div><div style="clear: both"></div>')



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

function load_blocked_users(current_page, search_like, order_type, unblock) {
    var search_like_val = $("#search_user_text").val();
    if (search_like != null && search_like.length != 0) {
        search_like_val = search_like;
    }
    if (order_type == null) {
        order_type = 'asc';
    }
   var data = {
       searchLike: search_like_val,
       currentPage: current_page,
       orderType: order_type
   };
    $.ajax({
        url : 'load-blocked-users.htm' ,
        type: "post",
        dataType: "json",
        data : data,
        success: function (data) {
            if (data.responseDto != null && data.responseDto.status == "SUCCESS") {
                console.log("blocked useric",data);
                var cont;
                var cur_page = data.currentPage;
                var is_last = data.isLast;
                if (cur_page == null || cur_page <= 2) {
                    //clean_profile();
                    cont = $("<ul   class='port-menu-list logged_accountList blocked_search_result' data-order_type='" + order_type + "' data-action='search_user' data-search_like='" + search_like + "' data-current_page='" + cur_page + "' />");
                    $(".blocked_users_list").html(cont);


                } else {
                    $(".blocked_search_result").attr('data-current_page', cur_page).attr('data-is_last', is_last);
                    cont = $(".blocked_search_result");
                }
                ul_rezult_height(".blocked_search_result");

                var search_users = data.searchUsers;
                if (search_users != null) {

                    $.each(search_users, function (i, item) {
                        var search_user = item;
                        var id = search_user.id;
                        var name_surname = search_user.name + ' ' + search_user.surname;
                        var li = $('<li class="searched_item"></li>').appendTo(cont);
                        var div = $('<div class="info_parent_div"></div>').appendTo(li);

                        var user_photo_path = IMAGE_BASE_URL + id;
                        $(div).append("<div class='user_div  div" + id + "'><img  class='thumb'/></div>");
                        $(".thumb").attr("src", user_photo_path);
                        var innerSpan = $('<div class="detales"/>').appendTo(div);
                        $("<a class='name name_div" + id + "'></a> ").text(search_user.name + ' ' + search_user.surname).appendTo(innerSpan);
                        $(div).append('<div class="removed_img" onclick="unblock_user(' + id + ',this)"><i class="fa fa-unlock-alt" aria-hidden="true"></i><p class="remove_text">' + unblock + '</p></div><div style="clear: both"></div>');

                    });
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

        }
    });
}
function change_currency_type_check_tax(this_, val) {

    var currencyType = $(this_).val();
    var  data = {
        exchangeCurrencyType : currencyType
    }
    loader_show();
    $.ajax({
        url : 'change-currency-type-check-tax.htm' ,
        type: "post",
        dataType: "json",
        data : data,
        success: function (data_) {
            if (data_ != null && data_.responseDto.status == "SUCCESS") {
                var walletExchange = data_.walletExchange;
                $('.success_text').empty();
                var msg = '<div class="success_text">'+
                    '<span>New money : </span>'+
                    '<span>' + walletExchange.newMoneyPaidTax + ' ' + walletExchange.newCurrencyType + '</span> </br>' +
                    '<span>old money : </span>' +
                    '<span>' + walletExchange.money + ' ' + walletExchange.oldCurrencyType + '</span> </br>' +
                    '<span>Rate : </span>' +
                    '<span> 1' + walletExchange.newCurrencyType + ' = ' + walletExchange.newMoneyPaidTaxExchange.rateAmount + walletExchange.oldCurrencyType + '</span>' +
                    '</div>';
                $('.success_text').append(msg);
                dragable_divs(".success_div_parent");

                $(".success_div_parent").show();
                $(".close_success").click(function (){
                    $(".success_div_parent").hide();
                    $(this_).val(val)

                });

                $(document).keydown(function (e) {
                    if (e.which == 27) {
                        $(".success_div_parent").hide();
                        $(this_).val(val)
                    }
                });
                loader_hide();
                $(".ok_button button").click(function (){
                    loader_show();
                    $(".success_div_parent").hide();
                    $.ajax({
                        url: 'change-currency-type.htm',
                        type: "post",
                        dataType: "json",
                        data: data,
                        success: function (data__) {
                            console.log("sdsdsd", data__);
                            if (data_ != null && data_.responseDto.status == "SUCCESS") {
                                var text = $(".select_currency select option:selected").text();
                                $(".default_currency_div span:nth-child(2)").html(text);
                                $(".balance span:nth-child(3)").html(text);
                                $(".message_div").html("duq poxeciq dzer curency type");
                                loader_hide();
                            }
                        },
                        error: function (xhr, ajaxOptions, thrownError) {
                            var handle = {status: xhr.status, responseText: xhr.responseText};
                            console.log(handle);
                            $(this_).val(val);
                            loader_hide();
                        }
                    });
                });
            }
            else {
                var handle = null;
                if (data != null ) {
                    handle = {status: data.status, responseText: data.messages};
                    $(this_).val(val);
                    loader_hide();
                } else {
                    handle = {status: 0, responseText: 'empty data'};
                    $(this_).val(val);
                    loader_hide();
                }
                console.log(handle);
            }
        },
        error: function (xhr, ajaxOptions, thrownError) {
            var handle = {status: xhr.status, responseText: xhr.responseText};
            console.log(handle);
            $(this_).val(val);
            loader_hide();
        }
    });

}


function unblock_user(id, elem) {
    loader_show();

    var li = $(elem).closest("li");

    var data = {
        blockedId:id
    };
    $.ajax({
        url : 'unblock-user.htm' ,
        type: "post",
        dataType: "json",
        data : data,
        success: function (data) {
            if (data != null && data.responseDto.status == "SUCCESS") {
                li.hide(1000, function () {
                    li.remove();
                });

                console.log("exav");
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