package com.connectto.wallet.web.action.general.interceptor;

import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.resourse.BeanProvider;
import com.connectto.wallet.dataaccess.service.wallet.IWalletManager;
import com.connectto.wallet.model.wallet.Wallet;
import com.connectto.wallet.web.action.wallet.dto.TransactionReviewDto;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import org.apache.log4j.Logger;

import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by htdev001 on 4/1/14.
 */
public class MerchantInterceptor implements Interceptor {

    private static final Logger logger = Logger.getLogger(MerchantInterceptor.class.getSimpleName());

    @Override
    public void destroy() {
    }

    @Override
    public void init() {
    }

    @Override
    public String intercept(ActionInvocation actionInvocation) {
        Map<String, Object> session = actionInvocation.getInvocationContext().getSession();

        try {

            Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
            if (wallet.getIsLocked()) {
                return "transactionMerchantShow";
            }


        } catch (Exception e) {
            logger.error(e);
            session.put(ConstantGeneral.ERR_CODE, 500);
            return ConstantGeneral.PAGE_RETURN_ERROR;
        }

        try {
            return actionInvocation.invoke();
        } catch (Exception e) {
            return "home_wallet";
        }
    }
}