package com.connectto.wallet.web.action.general.interceptor;

import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.resourse.BeanProvider;
import com.connectto.wallet.dataaccess.service.transaction.merchant.ITransactionDepositManager;
import com.connectto.wallet.dataaccess.service.transaction.merchant.ITransactionWithdrawManager;
import com.connectto.wallet.dataaccess.service.wallet.IWalletManager;
import com.connectto.wallet.model.transaction.merchant.withdraw.TransactionWithdraw;
import com.connectto.wallet.model.wallet.Wallet;
import com.connectto.wallet.model.wallet.lcp.TransactionState;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by htdev001 on 4/1/14.
 */
public class WalletAuthInterceptor implements Interceptor {

    private static final Logger logger = Logger.getLogger(WalletAuthInterceptor.class.getSimpleName());

    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    }

    @Override
    public String intercept(ActionInvocation actionInvocation) {

        Map<String, Object> session = actionInvocation.getInvocationContext().getSession();
        Locale locale = ActionContext.getContext().getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle(ConstantGeneral.LANGUAGE_BUNDLE_PATH, locale);
        String msg = bundle.getString("errors.internal.server.timeout");
        boolean isLockStatusChanged = false;

        try {

            Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);

            if (wallet == null) {
                logger.error("Wallet session expired, wallet is null");

                //HttpServletRequest request = ServletActionContext.getRequest();
                //String uri = request.getRequestURI();
                //String queryString = request.getQueryString();
                //if(uri.contains(ConstantGeneral.SESSION_REDIRECT_ACTION_LABEL)){
                //    uri = uri.replace(ConstantGeneral.SESSION_REDIRECT_ACTION_LABEL,"");
                //    session.put(ConstantGeneral.SESSION_REDIRECT_ACTION_URL, uri + "?" + queryString);
                //}

                session.put(ConstantGeneral.ERR_MESSAGE, msg);
                session.put(ConstantGeneral.ERR_CODE, 500);
                return ConstantGeneral.PAGE_RETURN_ERROR;
            }

            IWalletManager walletManager = BeanProvider.getWalletWalletManager();
            Wallet balance =  walletManager.getById(wallet.getId());
            boolean balanceIsLocked = balance.getIsLocked();
            if(wallet.getIsLocked() && !balanceIsLocked){
                isLockStatusChanged = true;
            }

            wallet.setMoney(balance.getMoney());
            wallet.setCurrencyType(balance.getCurrencyType());
            wallet.setIsLocked(balanceIsLocked);
            session.put(ConstantGeneral.SESSION_WALLET, wallet);

            if (isLockStatusChanged){
                return "home";
            }

            return actionInvocation.invoke();
        } catch (Exception e) {
            logger.error(e);
            session.put(ConstantGeneral.ERR_MESSAGE, msg);
            session.put(ConstantGeneral.ERR_CODE, 500);
            return ConstantGeneral.PAGE_RETURN_ERROR;
        }
    }
}