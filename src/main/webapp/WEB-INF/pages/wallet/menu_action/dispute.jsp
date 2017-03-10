<%--
  Created by IntelliJ IDEA.
  User: htdev001
  Date: 9/2/14
  Time: 10:18 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<style type="text/css">
    .dispute {
        border: 2px solid red;
    }

    .requested_transaction {
        overflow: auto;
        background: #d3d3d7;
        border: solid green 2px;
        margin: 5px;
        width: 80%;
    }

</style>
<div id="action_panel" class="action_panel">

    <div id="action_queue">
        <a href='view-transaction.htm?transactionId=<s:property value="transactionId"/>'>
            <s:text name="wallet.transaction.button.Dispute"/>
        </a>
    </div>

    <s:if test="#currentDispute != null && #currentDispute.disputes != null">
        <s:iterator value="currentDispute.disputes" var="dispute">
            <div class="searched_item">
                <img src="/img/general/user_default/default_1.jpg"/>

                <div class="detales">
                    <s:property value="#dispute.answeredAt"/>
                    <a class="name" href='#?userId=<s:property value="#dispute.answeredBy.id"/>'><s:property
                            value="#dispute.answeredBy.name"/> <s:property value="#dispute.answeredBy.surname"/></a>
                </div>
                <div>
                    <s:property value="#dispute.answer"/>
                </div>
            </div>

        </s:iterator>
    </s:if>

    <div class="transactions">
        <form action="dispute.htm" method="post">
            <input type="hidden" name="transactionId" value='<s:property value="transactionId"/>'>

            <div>
                <span><s:text name="wallet.dispute.label.Reason"/></span>
                <span>
                    <input type="text" name="reason">
                </span>
            </div>
            <div>
                <span><s:text name="wallet.dispute.label.Content"/></span>
                <span>
                    <textarea rows="10" cols="45" name="content"></textarea>
                </span>
            </div>
            <div>
                <span><input type="submit" value='<s:text name="wallet.transaction.button.Dispute"/>'></span>
            </div>
        </form>
    </div>
</div>
