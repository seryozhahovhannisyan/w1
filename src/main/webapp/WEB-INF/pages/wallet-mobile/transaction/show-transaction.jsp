<%--
  Created by IntelliJ IDEA.
  User: htdev001
  Date: 11/18/14
  Time: 4:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css">
    .debited {
        background-color: #ff9b0e;
    }

    .credited {
        background-color: #00db00;
    }
</style>
<script type="text/javascript">
    $(document).ready(function () {
        var action_type = $("#action_type").val();
        $("#action_panel").addClass(action_type);
    });
</script>
<div id="action_panel" class="action_panel">

    <div>
        <span>
        <s:property value="pendingTransaction.debitedUser.name"/> <s:property
                value="pendingTransaction.debitedUser.surname"/>
        <a href='show_user_action.htm?selectedUserId=<s:property value="pendingTransaction.debitedUser.id"/>'><s:text
                name="wallet.transaction.button.Profile"/></a>
        </span>
        <span>
            <s:if test="%{pendingTransaction.debitedUser.defaultPhotoId == 0}">
                <img width="150px" height="150px"
                     src='<%=request.getContextPath()%>/<s:property value="%{getUserMainImg(pendingTransaction.debitedUser.id, pendingTransaction.debitedUser.photoData.fileName)}"/>'>
            </s:if>
            <s:else>
                <img width="150px" height="150px"
                     src="<%=request.getContextPath()%>/img/general/user_profile_default/default_<s:property value="pendingTransaction.debitedUser.defaultPhotoId"/>.jpg">
            </s:else>
        </span>
    </div>
    <div>
        <span>
            <s:property value="pendingTransaction.creditedUser.name"/> <s:property
                value="pendingTransaction.creditedUser.surname"/>
            <a href='show_user_action.htm?selectedUserId=<s:property value="pendingTransaction.creditedUser.id"/>'><s:text
                    name="wallet.transaction.button.Profile"/></a>
        </span>
        <span>
            <s:if test="%{pendingTransaction.creditedUser.defaultPhotoId == 0}">
                <img width="150px" height="150px"
                     src='<%=request.getContextPath()%>/<s:property value="%{getUserMainImg(pendingTransaction.creditedUser.id, pendingTransaction.creditedUser.photoData.fileName)}"/>'>
            </s:if>
            <s:else>
                <img width="150px" height="150px"
                     src="<%=request.getContextPath()%>/img/general/user_profile_default/default_<s:property value="pendingTransaction.creditedUser.defaultPhotoId"/>.jpg">
            </s:else>
        </span>
    </div>

    <div>
        <span><s:text name="wallet.payment.label.Amount"/></span>
        <span><s:property value="pendingTransaction.amount"/></span>
    </div>

    <div>
        <span><s:text name="wallet.payment.label.Currency"/></span>
        <span><s:property value="pendingTransaction.currencyType"/></span>
    </div>

    <div>
        <span><s:text name="wallet.payment.label.TransactionType"/></span>
        <span><s:property value="pendingTransaction.transactionType"/></span>
    </div>

    <div>
        <span><s:text name="wallet.payment.label.OrderCode"/></span>
        <span><s:property value="pendingTransaction.orderCode"/></span>
    </div>

    <div style="height: 250px;overflow: auto">
        <span><s:text name="wallet.payment.label.Message"/></span>
        <span><s:property value="pendingTransaction.message"/></span>
    </div>

    <div>
        <span><s:text name="wallet.payment.label.Attach"/></span>
        <s:iterator value="pendingTransaction.transactionDatas" var="data">
            <div>
                <a href='transaction_data_download.htm?transactionId=<s:property value="#data.transactionId"/>&dataFileName=<s:property value="#data.fileName"/>'><s:property
                        value="#data.fileName"/></a>
            </div>
        </s:iterator>
    </div>

    <div>
        <s:iterator value="pendingTransaction.transactionActions" var="action">
            <div>
                <span>
                  transactionState  <s:property value="#action.transactionState"/>
                </span>

                <span>
                   transfer Fee <s:property value="#action.transactionFee.transferFee"/>
                </span>

                <span>
                   exchange Fee <s:property value="#action.transactionFee.exchangeFee"/>
                </span>

                <span>
                   actionDate <s:property value="#action.actionDate"/>
                </span>
            </div>
        </s:iterator>
    </div>

    <div style="float: right;margin-right: 100px">
        <s:if test="%{pendingTransaction.transactionActions.size() == 1 &&  pendingTransaction.transactionActions[0].transactionState.id == 1}">

            <%--
                (a.wallet_id = #walletId# and a.wallet_id = t.debited_wallet_id ) or
                (a.wallet_id = #walletId# and a.wallet_id = t.credited_wallet_id ),1,0
            --%>
            <s:if test="%{#session.session_wallet.id == pendingTransaction.creditedWalletId}">
                <input type="hidden" value="credited" id="action_type"/>
                <s:if test="%{#session.session_wallet.id == pendingTransaction.transactionActions[0].walletId }">
                    <form action="delay_transaction.htm" method="post">
                        <input type="hidden" name="pendingTransactionId"
                               value='<s:property value="pendingTransaction.id"/>'>
                        <input type="submit" value='<s:text name="wallet.transaction.button.Delay"/>'>
                    </form>
                </s:if>
                <s:else>
                    <form action="cancel_transaction.htm" method="post">
                        <input type="hidden" name="pendingTransactionId"
                               value='<s:property value="pendingTransaction.id"/>'>
                        <input type="submit" value='<s:text name="wallet.transaction.button.Cancel"/>'>
                    </form>
                    <form action="allow_transaction.htm.htm" method="post">
                        <input type="hidden" name="pendingTransactionId"
                               value='<s:property value="pendingTransaction.id"/>'>
                        <input type="submit" value='<s:text name="wallet.transaction.button.Allow"/>'>
                    </form>
                </s:else>
            </s:if>
            <s:if test="%{#session.session_wallet.id == pendingTransaction.debitedWalletId}">
                <input type="hidden" value="debited" id="action_type"/>
                <s:if test="%{#session.session_wallet.id == pendingTransaction.transactionActions[0].walletId }">
                    <form action="delay_transaction.htm" method="post">
                        <input type="hidden" name="pendingTransactionId"
                               value='<s:property value="pendingTransaction.id"/>'>
                        <input type="submit" value='<s:text name="wallet.transaction.button.Delay"/>'>
                    </form>
                </s:if>
                <s:else>
                    <form action="cancel_transaction.htm" method="post">
                        <input type="hidden" name="pendingTransactionId"
                               value='<s:property value="pendingTransaction.id"/>'>
                        <input type="submit" value='<s:text name="wallet.transaction.button.Cancel"/>'>
                    </form>
                    <form action="transfer_transaction.htm" method="post">
                        <input type="hidden" name="pendingTransactionId"
                               value='<s:property value="pendingTransaction.id"/>'>
                        <input type="submit" value='<s:text name="wallet.transaction.button.Transfer"/>'>
                    </form>
                </s:else>
            </s:if>
        </s:if>
        <s:else>
            <s:if test="%{#session.session_wallet.id == pendingTransaction.creditedWalletId}">
                <input type="hidden" value="credited" id="action_type"/>
            </s:if>
            <s:if test="%{#session.session_wallet.id == pendingTransaction.debitedWalletId}">
                <input type="hidden" value="debited" id="action_type"/>
            </s:if>
            <form action="view-transaction.htm" method="post">
                <input type="hidden" name="transactionId" value='<s:property value="pendingTransaction.id"/>'>
                <input type="submit" value='<s:text name="wallet.transaction.button.Dispute"/>'>
            </form>
        </s:else>
    </div>
</div>
