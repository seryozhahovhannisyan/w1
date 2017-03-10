<%--
  Created by IntelliJ IDEA.
  User: htdev001
  Date: 9/2/14
  Time: 10:18 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType = "text/html;charset=UTF-8" language = "java" %>
<%@ taglib prefix = "s" uri = "/struts-tags" %>
<s:if test = "#session.confirmed != null && #session.confirmed">
    <script type = "text/javascript">
        close_any_popup('.curent_bal_div');
        $("body").delegate(".close_bal", 'click', function () {
            $(".curent_bal_div").hide();
        });
        $(document).keydown(function (e) {
            if (e.which == 27) {
                $(".curent_bal_div").hide();
            }
        });
        function current_balance() {
            $.ajax({
                url: "current-balance-view.htm",
                type: "post",
                dataType: "json",
                async: true,
                success: function (data) {

                    if (data != null && data.responseDto.status == "SUCCESS") {
                        var wallet = data.userWallet;
                        var current_balanc = $('<div class="curent_bal_div"><div class="close_bal"></div><div style="clear: both"></div><div class="balance_text"><p><s:text name="wallet.login.your.current.balance.is">Your Current Balance is</s:text></p>' +
                                '<div>' + wallet.money + '</div></div><div class="balance_text"><p><s:text name="wallet.login.your.frozen.amount.is">Amount in hold in your account</s:text></p><div>' + wallet.frozenAmount + '</div>' +
                                '</div><div class="balance_text"><p><s:text name="wallet.login.your.receiving.amount.is">Expected incoming transactions</s:text></p><div>' + wallet.receivingAmount + '</div></div>' +
                                '<div class="balance_text last_div"><p><s:text name="wallet.login.your.currency.type.is">Your Currency Type is</s:text></p><div>' + wallet.currencyType + '</div></div></div>');
                        $("body").append(current_balanc)

                    } else {
                        console.log(data.responseDto.status + " " + data.responseDto.messages);
                    }
                },
                error: function (e) {
                    console.log(e);
                }
            })
        }
    </script>
    <link rel = "stylesheet" href = "<%=request.getContextPath()%>/css/wallet/wallet_mobile/wallet_login_mobile.css"
          media = "screen">
    <div class = "current_button_div">
        <button class = "btn" onclick = "current_balance()">
            <img src = "<%=request.getContextPath()%>/img/wallet/icon/balance_button_icon.png"
                 alt = "balance icon"/>
            <s:text name = "wallet.login.current.balance"> Current Balance</s:text>
        </button>
    </div>


    <div class = "hello_div_parent">
        <s:text name = "info.welcome.wallet.hello">Hello</s:text>
        <div class = "name_div">
            <s:property value = "#session.session_user.name"/> <s:property value = "#session.session_user.surname"/>
        </div>
    </div>

    <div class = "user_text_div col-lg-8 col-md-8 col-sm-8 col-xs-10">
        <s:text name = "wallet.login.hello.text">Welcome to your ConnectToWallet. With the help of this page you can send money,
            also ask for it and receive it by selecting any member of ConnectToWallet. During these transactions a certain percent
            of the money you deal with will be charged from you(you may get familiar with the max and min percents of transactions
            by looking through the section Terms and Conditions). If someone annoys you, you may add him/her to your Blocked Users' list.
            If you are dealing mainly with one currency you had better set it as your current currency in the section Current Currency, as
            in this case you may save your money. You also have the opportunity to look through your pending transactions, as well as the transactions
            you have already completed, making your search easier and more comfortable with the help of various filters. You may have
            transactions using your credit cards too by selecting section Credit Card Transaction and following the given instructions.
            ConnectToWallet supports four types of credit cards: MasterCard, Visa, American Express, Discover. You may also set your priority
            over your credit cards by sorting them.
        </s:text>
    </div>


</s:if>

