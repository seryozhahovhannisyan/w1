<%@ page import="com.connectto.general.util.ConstantGeneral" %>
<%@ page import="com.connectto.general.model.Partition" %>
<%@ page import="com.connectto.general.model.lcp.PartitionLCP" %>
<%@ page import="com.connectto.general.model.User" %>
<%@ page import="com.connectto.general.model.WalletSetup" %>
<%@ page import="java.util.List" %>
<%@ page import="com.connectto.wallet.model.wallet.lcp.TransactionType" %>
<%@ page import="com.connectto.wallet.model.wallet.lcp.CurrencyType" %>
<%@ page import="com.connectto.wallet.model.wallet.Wallet" %>
<%@ page import="com.connectto.general.util.Utils" %>
<%--
  Created by IntelliJ IDEA.
  User: htdev001
  Date: 9/2/14
  Time: 10:18 AM
  To change this template use File | Settings | File Templates.

--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--//todo backend switch cases--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
    User user = (User) session.getAttribute(ConstantGeneral.SESSION_USER);
    Wallet wallet = (Wallet) session.getAttribute(ConstantGeneral.SESSION_WALLET);
    WalletSetup walletSetup = user.getPartition().getWalletSetup();
    List<TransactionType> availableCards = walletSetup.parseAvailableCards();
    List<CurrencyType> currencyTypes = walletSetup.getAvailableRates();
%>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/wallet/wallet_mobile/wallet_mobile_settings.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/wallet/wallet_mobile/block_user_mobile.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/wallet/wallet_mobile_settings.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/wallet/wallet_block_user.js"></script>

<script type="text/javascript">
    $(document).ready(function () {
//        call_load_blocked_users();
//        call_load_users();
        serach_users();
    });

        function call_load_blocked_users(){
            var unblock_text = '<s:text name="pages.friend.tab.action.unblock">unblock</s:text>';
            load_blocked_users(-1, '', 'asc',unblock_text);
        }

  function call_load_users(){
            var block = '<s:text name="wallet.general.wallet.block.user">Block user</s:text>';
      search_user(-1, '', 'asc',block );
        }

function serach_users(){
    $(".blocked_search").click(function () {
        call_load_blocked_users();
    });
    $(".search").click(function () {
        call_load_users();
    })
}




</script>

<div class="container-fluid">
    <div class="left_part">
        <div class="terms_conditions">
            <s:text name="wallet.settings.terms.and.conditions">Terms and Conditions</s:text>
        </div>
        <div class="change_password">
            <s:text name="pages.parentalControl.changePassword">Change Password</s:text>
        </div>
        <div class="default_currency">
            <s:text name="wallet.settings.default.currency">Default Currency</s:text>
        </div>
        <div class="blocked_users">
            <s:text name="wallet.settings.blocked.users">Blocked Users</s:text>
        </div>
        <div class="sounds">
             <s:text name="walet.settingd.sound.sound">Sounds</s:text>
        </div>
    </div>
    <div class="right_part">


        <%--terms and conditions--%>
        <div class="parent_div_terms_conditions">
            <div class="available_currency">
                <span><s:text name="wallet.general.wallet.available.currency">Available currency:</s:text></span>
                <%
                    for(CurrencyType currencyType : currencyTypes){
                %>
                <span><%=currencyType.getName()%>  </span>
                <%}%>

            </div>
            <div class="available_credit_cards">
                <span><s:text
                        name="wallet.general.wallet.available.credit.cards">Available credit cards:</s:text></span>

                <%
                    for(TransactionType card : availableCards){
                        if(card.getIsCreditCard()){
                            String cardLogo = "/img/wallet/cardsLogo/"+card.getLogo();
                %>

                <span class="cards_img"><img
                        src="<%=request.getContextPath()%><%=cardLogo%>"
                        alt="<%=card.getValue()%>"></span>
                <%}}%>


            </div>
            <div class="fees_clases">
                <table class="fee_table">
                    <th class="table_th">
                        <s:text name="wallet.general.wallet.fees">Fees</s:text>
                    </th>
                    <th class="table_th">
                        <img src="<%=request.getContextPath()%>/img/wallet/icon/send_money.png" alt="send">
                        <p><s:text name="wallet.general.wallet.fees.sender">Sender</s:text></p>

                    </th>
                    <th class="table_th">
                        <img src="<%=request.getContextPath()%>/img/wallet/icon/requst_transaction.png" alt="send">
                        <p><s:text name="wallet.general.wallet.fees.receiver">Receiver</s:text></p>
                    </th>
                    <th class="range_fee">
                        <img src="<%=request.getContextPath()%>/img/wallet/icon/calculate_icon.png" alt="">
                        <div class="ranges">

                            <input type="range">

                        </div>
                    </th>
                    <tr>
                        <td><s:text name="wallet.general.wallet.fees.min.fee">Min fee</s:text></td>
                        <td><%=walletSetup.getTransferMinFee()%><%=walletSetup.getCurrencyType().getCode()%></td>
                        <td><%=walletSetup.getReceiverMinFee()%><%=walletSetup.getCurrencyType().getCode()%></td>
                    </tr>
                    <tr>
                        <td><s:text name="wallet.general.wallet.fees.max.fee">Max fee</s:text></td>
                        <td><%=walletSetup.getTransferMaxFee()%><%=walletSetup.getCurrencyType().getCode()%></td>
                        <td><%=walletSetup.getReceiverMaxFee()%><%=walletSetup.getCurrencyType().getCode()%></td>
                    </tr>
                    <tr>
                        <td><s:text name="wallet.setting.default.percent">Default percent</s:text></td>
                        <td><%=walletSetup.getTransferFeePercent()%>%</td>
                        <td><%=walletSetup.getReceiverFeePercent()%>%</td>
                    </tr>

                    <tr>
                        <td><s:text name="wallet.setting.Min.Exchange.fee">Min Exchange fee</s:text></td>
                        <td><%=walletSetup.getExchangeTransferMinFee()%><%=walletSetup.getCurrencyType().getCode()%></td>
                        <td><%=walletSetup.getExchangeReceiverMinFee()%><%=walletSetup.getCurrencyType().getCode()%></td>
                    </tr>
                    <tr>
                        <td><s:text name="wallet.setting.Max.Exchange.fee">Max Exchange fee</s:text></td>
                        <td><%=walletSetup.getExchangeTransferMaxFee()%><%=walletSetup.getCurrencyType().getCode()%></td>
                        <td><%=walletSetup.getExchangeReceiverMaxFee()%><%=walletSetup.getCurrencyType().getCode()%></td>
                    </tr>
                    <tr>
                        <td><s:text name="wallet.setting.Default.Exchange.percent">Default Exchange percent</s:text></td>
                        <td><%=walletSetup.getExchangeTransferFeePercent()%>%</td>
                        <td><%=walletSetup.getExchangeReceiverFeePercent()%>%</td>
                    </tr>

                </table>

            </div>
        </div>
        <%--end terms and conditions--%>


        <%--change password--%>
        <div class="parent_div_password">
            <form action="#">
                <div class="change_password_input_div">
                    <input class="change_password_input1" type="text" name="password" id="password"
                           placeholder="<s:text name="wallet.general.wallet.password.placeholder.enter.old.password">Enter old password</s:text>"
                            />
                </div>

                <div class="change_password_input_div">
                    <input class="change_password_input2" type="text" name="newPassword" id="newPassword"
                           placeholder="<s:text name="wallet.general.wallet.password.placeholder.enter.new.password">Enter new password</s:text>"
                            />
                </div>

                <div class="change_password_input_div">
                    <button type="button" class="submit_button">
                        <s:text name="wallet.general.button.Submit">Submit</s:text>
                    </button>
                </div>
            </form>
        </div>
        <%-- end change password--%>

        <%--default currency--%>
        <div class="parent_div_default_currency">
            <div class="default_currency_div">
                <span><s:text
                        name="wallet.general.wallet.your.default.currency.is">Your default currency is </s:text></span>
                <span><%=wallet.getCurrencyType().getCode()%></span>
            </div>
            <div class="balance">
                <span>
                    <s:text name="wallet.general.wallet.your.balance.is">Your Balance is</s:text>
                </span>
                <span><%=Utils.convertDoubleToViewString(wallet.getMoney())%></span>
                <span><%=wallet.getCurrencyType().getCode()%></span>
            </div>
            <div class="select_currency">
                <select>
                    <%
                        for(CurrencyType currencyType : currencyTypes){
                    %>
                    <span><%=currencyType.getName()%>  </span>
                    <option <%if(wallet.getCurrencyType().getId() == currencyType.getId()){%> selected="selected" <%}%>value="<%=currencyType.getId()%>"><%=currencyType.getCode()%></option>
                    <%}%>

                </select>
            </div>
            <div class="message_div">tghghgf</div>
        </div>
        <%--end default currency--%>

        <%--blocked user--%>


        <div class="blocked_user_parent">
            <div class="blocked_user_select">
                <div class="blocked_user_tab">
                    <i class="fa fa-lock" aria-hidden="true"></i>
                    <s:text name="wallet.general.wallet.blocked.user">Blocked Users</s:text>
                </div>
                <div class="all_user_tab">
                    <i class="fa fa-unlock-alt" aria-hidden="true"></i>
                    <s:text name="wallet.settings.all.users">All users</s:text>
                </div>
            </div>
            <div class="blocked_user_left_part">
                <div id="blocked_search_user" class="user_search_div">
                    <%--<div class="input_before_text"><s:text--%>
                    <%--name="wallet.pages.make.search.before.text">Please choose precipitant</s:text></div>--%>
                    <div class="search_parent_div">
                        <div class="blocked_search"></div>
                        <input id="blocked_search_user_text" type="search" class="search_user_input" name="searchLike"
                               placeholder="<s:text name="wallet.pages.make.label.search_by">Search Friend or Company</s:text>"
                                />
                    </div>
                    <div class="arrow_div">
                        <div class="up"></div>
                        <div class="down"></div>
                        <img src="<%=request.getContextPath()%>/img/wallet/icon/arrow_up_icon.png" alt="arrow icon"/>
                    </div>

                <div style="clear: both"></div>
                </div>
                <div class="blocked_users_list"></div>
            </div>

            <div class="blocked_user_right_part">
                <div id="search_user" class="user_search_div">
                    <%--<div class="input_before_text"><s:text--%>
                    <%--name="wallet.pages.make.search.before.text">Please choose precipitant</s:text></div>--%>
                    <div class="search_parent_div">
                        <div class="search"></div>
                        <input id="search_user_text" type="search" class="search_user_input" name="searchLike"
                               placeholder="<s:text name="wallet.pages.make.label.search_by">Search Friend or Company</s:text>"
                                />
                    </div>
                    <div class="arrow_div">
                        <div class="up"></div>
                        <div class="down"></div>
                        <img src="<%=request.getContextPath()%>/img/wallet/icon/arrow_up_icon.png" alt="arrow icon"/>
                    </div>

                    <div style="clear: both"></div>
                </div>
                <div class="users_list"></div>
            </div>

        </div>
        <%--end blocked user--%>

<%--mute unmute sounds--%>
        <div class="mute_unmute_parent">
            <div class="mute">
                <s:text name="walet.settingd.sound.mute">Mute</s:text>
            </div>
        </div>
            <%--end mute unmute sounds--%>
    </div>
</div>