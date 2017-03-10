<%@ page import="com.connectto.general.model.User" %>
<%@ page import="com.connectto.general.model.lcp.UserProfile" %>
<%@ page import=" com.connectto.general.model.WalletSetup" %>
<%@ page import="com.connectto.general.util.ConstantGeneral" %>
<%@ page import="com.connectto.wallet.model.wallet.lcp.TransactionType" %>
<%@ page import="java.util.List" %>
<%@ page import="com.connectto.wallet.model.wallet.Wallet" %>
<%@ page import="com.connectto.general.util.Utils" %>
<%--
  Created by IntelliJ IDEA.
  User: htdev001
  Date: 9/2/14
  Time: 10:15 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%
    User user = (User) session.getAttribute(ConstantGeneral.SESSION_USER);
    WalletSetup walletSetup = user.getPartition().getWalletSetup();
    List<TransactionType> availableCards = walletSetup.parseAvailableCards();

    Long disputeId = (Long) request.getAttribute("disputeId");
    Long transactionId = (Long) request.getAttribute("transactionId");
    String redirectUrl = (String) request.getAttribute("redirectUrl");
%>
<s:if test="#session.confirmed != null && #session.confirmed">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/wallet/wallet_person_panel.css">

    <script type="text/javascript" src="<%=request.getContextPath()%>/js/general/general_api.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/wallet/freeze_charge_api.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/wallet/freeze_charge_api_test.js"></script>


    <script type="text/javascript">

        function run_test() {

            // request permission on page load
            document.addEventListener('DOMContentLoaded', function () {
                if (Notification.permission !== "granted")
                    Notification.requestPermission();
            });

//            browser_notification_test();

//            send_money_check_tax_test();
//            send_money_preview_test();
//            send_money_make_payment_test();//+
//            send_money_approve_test();//+
//            send_money_reject_test();
//
//            request_transaction_check_tax_test();
//            request_transaction_preview_test();
//            request_transaction_make_payment_test();//+
//            request_transaction_approve_test();
//            request_transaction_reject_test();//+
//
//            load_available_currencies_test();
//            load_pending_transactions_test();//+
//            load_completed_transactions_test(); //+

//            load_transaction_notifier_test();

//               charge_amount_from_wallet_test()

        }

        $(document).ready(function () {

//            run_test();
//            request_transaction_check_tax_test();

            close_menu();
            show_menu();
            log_out_wallet();
            load_transaction_notifier_init();

        })

        function load_transaction_notifier_init() {

            var params = {};
            params.async = true;
            params.tot_pending_arr = [];
            params.tot_completed_arr = [];
            params.host = '<%=request.getContextPath()%>';
            <%
                Wallet wallet = (Wallet) session.getAttribute(ConstantGeneral.SESSION_WALLET);
                if(wallet != null){
            %>
            params.walletId = '<%=wallet.getId()%>';
            <%
                }
            %>

            WALLET.load_transaction_notifier(params, notification_callback, notification_error_handler);
            <%--var sound_url = '<%=request.getContextPath()%>/sounds/wallet/musics/message.mp3';--%>
            <%--sonds(sound_url)--%>
        }

            /*todo data.data. test*/
        var notification_callback = function (data, params) {
            console.log("data notificationic ", data);
            var pending_total = data.response.data.pendingCountTotal;
            var pending_send = data.response.data.pendingCountSend;
            var pending_request = data.response.data.pendingCountRequestTransaction;
            var completed_total = data.response.data.completedCountTotal;
            var completed_send = data.response.data.completedCountSend;
            var completed_request = data.response.data.completedCountRequestTransaction;
            $(".all_pending").html(pending_total);
            $(".send_pending").html(pending_send);
            $(".request_pending").html(pending_request);
            $(".all_completed").html(completed_total);
            $(".send_completed").html(completed_send);
            $(".request_completed").html(completed_request);


            var error_function = function () {
                alert('Desktop notifications not available in your browser. Try Chromium.');
            };
            if (pending_total > params.tot_pending_arr[0]) {
                var params_ = {};
                var count = pending_total - params.tot_pending_arr[0];
                params_.text = "you have a " + count + " new pending transaction";
                params_.url = '<%=request.getContextPath()%>/sounds/wallet/musics/message.mp3';
                var calback_pending_notification = function () {
                    goToAction('pending-transaction.htm')
                };

                params_.host = '<%=request.getContextPath()%>';
                params_.title = '<s:text name="wallet.notification.title.pending">You have new pending transaction</s:text>';
                WALLET.browser_notification(params_, calback_pending_notification, error_function);
            }
            if (completed_total > params.tot_completed_arr[0]) {

                var params__ = {};
                var count_ = completed_total - params.tot_completed_arr[0];
                params__.text = "you have a " + count_ + " new completed transaction";
                var calback_completed_notification = function () {
                    goToAction('completed-transaction.htm')
                };
                params__.host = '<%=request.getContextPath()%>';
                params__.title = '<s:text name="wallet.notification.title.completed">You have new completed transaction</s:text>';
                WALLET.browser_notification(params__, calback_completed_notification, error_function);
            }
            params.tot_pending_arr.unshift(pending_total);
            params.tot_completed_arr.unshift(completed_total);


        };
        var notification_error_handler = function (handle) {

            var handler_status = handle.status ? handle.status : "";
            var handler_text = handle.responseText ? handle.responseText : "";
            var message = handler_status + "<br/>" + handler_text;
            console.log('handler_status ' + handler_status + ' handler_text ' + handler_text + ' message ');
        };
        function log_out_wallet() {
            $(".close_wallet").click(function () {
                goToAction('<s:property value="#session.session_wallet.currentLocation.previousUrl"/>')
            })
        }
        function close_menu() {
            $(".close_menu").click(function () {
                if ($(".aside").hasClass("expand")) {
                    $(".aside").removeClass("expand").css("display", "none")
                }
            })
        }
        function show_menu() {
            $(".configurations p").click(function () {
                $(".configurations_div").slideToggle(500)
            })
        }


    </script>


    <div class="person_panel container-fluid">
        <div class="row person_parent">
            <div class="header_logo">
                <div class="menu_person_panel">
                    <img src="<%=request.getContextPath()%>/img/wallet/icon/menu_icon.png" alt="menu"/>
                </div>
                <div class="header_logo_img col-lg-3 col-md-4 col-sm-5 col-xs-8">
                    <img src="<%=request.getContextPath()%>/img/wallet/logos/connecttotv_wallet.png" alt="logo"/>
                </div>
                <div class="configurations">
                    <p><s:text name="wallet.settings.Configuration">Configuration</s:text></p>
                    <div class="configurations_div">

                        <div class="name_surname_parent_div">
                            <div class="avatar_div" onclick="goToAction('home_wallet.htm')">
                                <img src="https://www.hollor.com/view_thumb_picture?userId=<%=user.getId()%>&name=<%=user.getPhoto()%>&noCash=0"/>
                            </div>
                            <div class="name_surname_div" onclick="goToAction('home_wallet.htm')">
                                <s:property value="#session.session_user.name"/> <s:property
                                    value="#session.session_user.surname"/>
                            </div>
                        </div>

                        <div class="log_out" id="logout">
                            <img src="<%=request.getContextPath()%>/img/wallet/icon/wallet_log_out.png"
                                 alt="log_out">
                            <s:text name="wallet.log.out">Log Out</s:text>
                        </div>

                        <%--<s:if test="%{#session.session_wallet.currentLocation.previousUrl != null}">--%>
                            <div class="close_wallet" id="wallet_close">
                                <img src="<%=request.getContextPath()%>/img/wallet/icon/wallet_close.png"
                                     alt="close_wallet">
                                <s:text name="wallet.close.wallet">Close Wallet</s:text>
                            </div>
                        <%--</s:if>--%>

                        <div class="settings" onclick="goToAction('settings.htm')">
                            <i class="fa fa-cog"></i>
                            <s:text name="button.backToSettings">Settings</s:text>
                        </div>
                        <div style="clear: both"></div>

                        <div style="display: none" class="settings"
                             onclick="goToAction('http://localhost:8787/wallet_entry_hollor.htm?tsmSessionId=<%=user.getCurrentAccount().getSessionId()%>')">
                            <i class="fa fa-cog"></i>
                            TestMenu
                        </div>

                    </div>
                </div>
                <div style="clear: both"></div>
            </div>


            <div class="row ">
                <div class="person_panel_parent">

                    <ul class="person_panel_ul">
                        <li class="col-lg-2 send_money_parent_li  ">
                            <div class="money_envelop">
                                <img onclick="goToAction('send-money.htm')"
                                     src="<%=request.getContextPath()%>/img/wallet/icon/send_money.png"
                                     alt="send money"/>
                            </div>
                            <span onclick="goToAction('send-money.htm')" class="send_money_div">

                                    <s:text name="wallet.login.send.money">Send money</s:text>


                            </span>
                                <%--<div class="selected_nav"></div>--%>
                        </li>
                        <li class="request_envelop_parent_li col-lg-2">
                            <div class="request_envelop">
                                <img onclick="goToAction('request-transaction.htm')"
                                     src="<%=request.getContextPath()%>/img/wallet/icon/requst_transaction.png"
                                     alt="request transaction"/>
                            </div>
                            <span onclick="goToAction('request-transaction.htm')" class="send_request_div">
                                    <s:text name="wallet.login.request.transaction">Request transaction</s:text>
                            </span>

                        </li>
                        <li class="pending_envelop_parent_li col-lg-2">
                            <div class="pending_envelop">
                                <img class="person_img"
                                     onclick="goToAction('pending-transaction.htm')"
                                     src="<%=request.getContextPath()%>/img/wallet/icon/pending_transaction.png"
                                     alt="pending transaction"/>
                            </div>
                            <span onclick="goToAction('pending-transaction.htm')" class="send_pending_div">
                                    <s:text name="wallet.login.pending.transaction">Pending transaction</s:text>
                            </span>
                            <span class="all_pending"></span>
                            <span>(
                                <span class="send_pending"></span>
                                <span>/</span>
                                <span class="request_pending"></span>
                            </span>)

                        </li>
                        <li class="completed_envelop_parent_li col-lg-2">
                            <div class="completed_envelop">

                                <img class="person_img"
                                     onclick="goToAction('completed-transaction.htm')"
                                     src="<%=request.getContextPath()%>/img/wallet/icon/complited_transaction.png"
                                     alt="pending transaction"/>
                            </div>
                            <span onclick="goToAction('completed-transaction.htm')" class="send_completed_div">

                                    <s:text name="wallet.login.complited.transaction">Completed transaction</s:text>
                            </span>
                            <span class="all_completed"></span>
                            <span>(
                                <span class="send_completed"></span>
                                <span>/</span>
                                <span class="request_completed"></span>
                            </span>)

                        </li>
                        <%if (availableCards != null && availableCards.size() > 0) { %>
                        <li class="completed_credit_card_parent_li col-lg-2">

                            <div class="credit_card_img">
                                <img onclick="goToAction('credit-cards-view.htm')"
                                     src="<%=request.getContextPath()%>/img/wallet/icon/credit_card_icon.png"
                                     alt="avatar"/>
                            </div>
                                      <span onclick="goToAction('credit-cards-view.htm')" class="credit_card_span">
                                              <s:text name="wallet.login.card.transaction">Credit Card Transaction</s:text>
                                      </span>

                        </li>
                        <%}%>
                        <li class="completed_merchant_wallet_parent_li col-lg-2">

                            <div class="merchant_wallet_img">
                                <img onclick="goToAction('transaction-merchant-show.htm')"
                                     src="<%=request.getContextPath()%>/img/wallet/logos/merchant_wallet_logo.png"
                                     alt="avatar"/>
                            </div>
                            <span onclick="goToAction('transaction-merchant-show.htm')" class="credit_card_span">
                                              <s:text name="wallet.login.card.transaction">Credit Card Transaction</s:text>
                                      </span>

                        </li>
                        <div style="clear: both"></div>
                    </ul>
                </div>
            </div>
        </div>


    </div>

</s:if>
<s:else>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/css/wallet/wallet_home.css"
          media="screen">

    <script src='https://www.google.com/recaptcha/api.js?hl=<s:property value="getSessionLanguage().getKey()"/>' async
            defer></script>
    <script>

        function footerPlace() {
            var window_width = $(window).width();
            var footer_height = $(".footer").outerHeight(true);

            var window_height = $(window).height();

            var document_height = $(document).height();

            var content = document_height - footer_height;

            var margin = (window_height - $(".content").height()) / 2;


            if (window_height < document_height) {
                $(".content").css("margin-top", "30px");
                $(".main").css("height", "auto");
            }
            else {
                $(".content").css("margin-top", margin + "px");
                $(".main").css("height", content);
            }
        }

        function re_enter_input() {


            $('.input_div input').focus(function () {
                $(this).css("border-color", "#27ccc0");
            });
            $('.input_div input').blur(function () {
                $(this).css("border-color", "#555965");
            });
        }

        $(document).ready(function () {
            footerPlace();
            re_enter_input();
//            password_fild_type(".re_enter_pass");
//            password_fild_type(".enter_wallet_pass");
            $(window).resize(function () {
                footerPlace()
            });
            show_title.title_();
            show_title_wallet.title_();
            show_pass.show_hide_password();
            show_wallet_pass.show_hide_password();
        });

        var show_password = function (selector1, selector2) {
            this.class_name = selector1;
            this.input_class = selector2;

            this.title_show = "show password";
            this.title_hide = "hide password";
            this.title_ = function () {
                if ($(this.class_name).length > 0) {
                    var a = $(this.class_name).css("background-image");
                    if (~a.indexOf("password_eye.png")) {
                        $(this.class_name).attr("title", this.title_show)
                    }
                    if (~a.indexOf("password_eye_closed.png")) {
                        $(this.class_name).attr("title", this.title_hide)
                    }
                }
            };

            this.show_hide_password = function () {
                var input_class = this.input_class;
                if ($(this.class_name).length > 0) {
                    $(this.class_name).click(function () {
                        var back_img_text = $(this).css("background-image");
                        if (~back_img_text.indexOf("password_eye.png")) {
                            $(this).css('background', " url(<%=request.getContextPath()%>/img/wallet/icon/password_eye_closed.png) no-repeat  center");
                            $(input_class).attr('type', 'password');
                        }
                        if (~back_img_text.indexOf("password_eye_closed.png")) {
                            $(this).css('background', " url(<%=request.getContextPath()%>/img/wallet/icon/password_eye.png) no-repeat  center");
                            $(input_class).attr('type', 'text');
                        }
                    });

                }
            }
        };

        var show_title = new show_password(".eye", ".re_enter_pass");
        var show_title_wallet = new show_password(".eye_wallet", ".enter_wallet_pass");
        var show_pass = new show_password(".eye", ".re_enter_pass");
        var show_wallet_pass = new show_password(".eye_wallet", ".enter_wallet_pass");

        function forgot_password() {
            $.ajax({
                url: 'remember-wallet-password.htm',
                type: "post",
                dataType: "json",
                async: true,
                success: function (data) {
                    if (data != null && data.responseDto.status == "SUCCESS") {
                        alert(data);
                        loader_hide();
                    } else {
                        var handle = null;
                        if (data != null && data.responseDto != null) {
                            handle = {status: data.responseDto.status, responseText: data.responseDto.messages};
                            loader_hide();
                        } else {
                            handle = {status: 0, responseText: 'empty data'};
                            loader_hide();
                        }
                        alert(handle);
                        loader_hide();
                    }
                },
                error: function (xhr, ajaxOptions, thrownError) {
                    var handle = {status: xhr.status, responseText: xhr.responseText};
                    alert(handle);
                    loader_hide();
                }
            });
        }

    </script>

    <div class="container-fluid">
        <div class="row">
            <div class=" row main">
                <div>--<s:actionerror/>--</div>
                <div class="log_out_div">
                    <div class="log_out" id="logout">
                        <img src="<%=request.getContextPath()%>/img/wallet/icon/wallet_log_out.png" alt="log_out">
                        <s:text name="wallet.log.out">Log Out</s:text>

                    </div>
                    <div class="close_wallet" id="wallet_close">
                        <img src="<%=request.getContextPath()%>/img/wallet/icon/wallet_close.png"
                             alt="close_wallet">
                        <s:text name="button.back">Back</s:text>
                    </div>
                    <div style="clear: both"></div>
                </div>

                <div class="row content">

                    <div class="  col-lg-8 col-md-7 col-sm-6 col-xs-12">
                        <div class="logo_div col-lg-8">
                            <img src="<%=request.getContextPath()%>/img/wallet/logos/connecttotv_wallet.png"
                                 alt="logo"/>
                        </div>
                        <div style="clear: both"></div>
                        <div class="login_type">
                            <div class="text_parent centered">
                                <div class="logo_img">
                                    <img data-index="1"
                                         src='<s:property value="#session.session_url_partition.partitionServerUrl"/><s:property value="#session.session_url_partition.partitionLogoDirectory"/><s:property value="#session.session_url_partition.logoPath"/>'
                                         alt='<s:property value='#session.session_url_partition.name'/>'/>
                                </div>
                                <div class="vshoo_type">
                                    <div><s:property value='#session.session_url_partition.name'/></div>
                                </div>
                                <div class="vshoo_type">

                                    <% Integer profileTsm = (Integer) session.getAttribute(ConstantGeneral.SESSION_URL_TSM_TYPE_KEY);%>
                                    <%if (profileTsm != null) { %>
                                    <% if (UserProfile.VSHOO_CUSTOM.getKey() == profileTsm) {%>
                                    <div><s:text name="label.vshoo.Customer">Customer</s:text></div>
                                    <%} else if (UserProfile.DRIVER.getKey() == profileTsm) {%>
                                    <div><s:text name="label.vshoo.Driver">Driver</s:text></div>
                                    <%} else if (UserProfile.COMPANY.getKey() == profileTsm) {%>
                                    <div><s:text name="label.vshoo.Company">Company administrator</s:text></div>
                                    <%} else if (UserProfile.LOCATION.getKey() == profileTsm) {%>
                                    <div><s:text
                                            name="label.vshoo.Company.Location">Company location administrator</s:text></div>
                                    <%} %>
                                    <%} %>
                                </div>
                                <div style="clear: both"></div>
                            </div>
                        </div>
                    </div>

                        <%--not loginet page--%>
                    <div class="right text-center   col-lg-4 col-md-5 col-sm-6 col-xs-12">
                        <div class="name_surname_parent_li">
                            <div class="avatar_div">
                                <img src="https://www.hollor.com/view_thumb_picture?userId=<%=user.getId()%>&name=<%=user.getPhoto()%>&noCash=0"/>
                            </div>
                            <div class="name_surname_div">
                                <s:property value="#session.session_user.name"/> <s:property
                                    value="#session.session_user.surname"/>
                            </div>
                        </div>

                        <%
                            Wallet walletReset = (Wallet) session.getAttribute(ConstantGeneral.RESET_SESSION_WALLET);
                        %>
                            <%--todo change for error message to walletReset != null--%>
                        <form id="wallet_login"
                                <%if (walletReset == null) {%>
                              action="confirm_password_wallet.htm"
                                <%} else {%>
                              action="do-wallet-reset-password.htm"
                                <%
                                    }
                                %>
                              method="post" autocomplete="new-password" >


                            <%
                                if (!Utils.isEmpty(redirectUrl) && disputeId != null && disputeId > 0) {
                            %>

                            <input type="hidden" name="redirectUrl" value="<%=redirectUrl%>"/>
                            <input type="hidden" name="disputeId" value="<%=disputeId%>"/>

                            <%
                            } else if (!Utils.isEmpty(redirectUrl) && transactionId != null && transactionId > 0) {
                            %>
                            <input type="hidden" name="redirectUrl" value="<%=redirectUrl%>"/>
                            <input type="hidden" name="transactionId" value="<%=transactionId%>"/>
                            <%
                                }
                            %>


                            <div class="input_div centered col-lg-10 col-md-10 col-sm-10 col-xs-10">

                                <div class="lock"></div>
                                <div class="eye"></div>
                                <input type="password" name="fake_password1"
                                       style="visibility: hidden; height: 1px; position: absolute">

                                <input class="re_enter_pass" type="password"
                                       name="password" data-index="1" autocomplete="new-password"
                                       placeholder="<s:text name="wallet.general.placeholder.text">Re-Enter Password</s:text>"
                                       required/>
                                <span class="forError"><s:fielderror name="password" fieldName="password"/></span>
                            </div>
                            <br/>
                            <%
                                Wallet wallet = (Wallet) session.getAttribute(ConstantGeneral.SESSION_WALLET);
                                if (Utils.isEmpty(wallet.getPassword())) {
                            %>
                            <div class="input_wallet_div centered col-lg-10 col-md-10 col-sm-10 col-xs-10">
                                <div class="lock"></div>
                                <div class="eye_wallet"></div>
                                <input type="password" name="fake_password2"
                                       style="visibility: hidden; height: 1px; position: absolute"/>
                                <input class="enter_wallet_pass" type="password" name="walletNewPassword" data-index="1"
                                       autocomplete="new-password"
                                       placeholder="<s:text name="wallet.general.wallet.password.placeholder.text">Re-enter wallet password</s:text>"
                                       required/>
                                <span class="forError"><s:fielderror name="password" fieldName="password"/></span>
                            </div>
                            <%
                                }
                            %>

                            <%if (walletReset == null) {%>
                            <div class="forget_pass" onclick="forgot_password()">
                                <s:text name="pages.registration.password.rem">Forgot Password?</s:text>
                            </div>
                            <%} %>


                            <div style="clear: both;height: 50px"></div>
                            <div class="centered chapta col-lg-10 col-md-10 col-sm-10 col-xs-10">
                                <div class="g-recaptcha" data-theme="dark"
                                     data-sitekey='<s:property value="#session.session_url_partition.recaptchaClientKey"/>'></div>

                            </div>
                            <div style="clear: both;height: 50px"></div>
                            <div class="centered col-lg-12 col-md-12 col-sm-12 col-xs-12">
                                <div class="centered button_div col-lg-10 col-md-10 col-sm-10 col-xs-10">
                                    <button class="btn login_button" id="wallet_login_submit" onclick="loader_show()"
                                            data-index="3">
                                        <%if (walletReset == null) {%>
                                        <s:text name="wallet.general.button.login">log in</s:text>
                                        <%} else {%>
                                        YO YO
                                        <%}%>
                                    </button>
                                </div>
                            </div>
                        </form>

                    </div>
                    <div style="clear:both; height: 30px"></div>
                </div>

            </div>
        </div>
    </div>

</s:else>