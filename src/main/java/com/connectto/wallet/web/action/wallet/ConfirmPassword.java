package com.connectto.wallet.web.action.wallet;

import com.connectto.general.model.Partition;
import com.connectto.general.model.User;
import com.connectto.general.util.*;
import com.connectto.general.util.encryption.SHAHashEnrypt;
import com.connectto.wallet.dataaccess.service.wallet.IWalletManager;
import com.connectto.wallet.model.general.lcp.LogAction;
import com.connectto.wallet.model.general.lcp.LogLevel;
import com.connectto.wallet.model.wallet.Wallet;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by htdev001 on 9/2/14.
 */
public class ConfirmPassword extends ShellAction {

    private static final Logger logger = Logger.getLogger(ConfirmPassword.class.getSimpleName());

    private IWalletManager walletManager;

    public void setWalletManager(IWalletManager walletManager) {
        this.walletManager = walletManager;
    }

    private String password;
    private String walletNewPassword;

    private Long disputeId = 0L;
    private Long transactionId = 0L;
    private String redirectUrl;

    public String execute() {

        Partition partition = (Partition)session.get(ConstantGeneral.SESSION_PARTITION);
        String recaptchaSecretKey = partition.getRecaptchaSecretKey();
        String recap = getHttpServletRequest().getParameter("g-recaptcha-response");
        String remoteAddress = getHttpServletRequest().getRemoteAddr();

        CaptchaResponse capRes = HttpURLBaseConnection.googleReCaptchaSiteVerify(recaptchaSecretKey,recap,remoteAddress);
        if(capRes.isSuccess()) {
            // Input by Human
            getHttpServletRequest().setAttribute("verified", "true");
        } else {
            // Input by Robot
            getHttpServletRequest().setAttribute("verified", "false");
            addFieldError("password", getText("errors.invalid.captcha.or.psw"));
            return  getIsMobile() ? "m-input" : INPUT;
        }

        User user = (User) session.get(ConstantGeneral.SESSION_USER);
        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);

        if (user == null ) {
            logger.error("user does not exists");
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server.timeout"));
            getHttpServletRequest().setAttribute("verified", "false");
            session.put(ConstantGeneral.ERR_MESSAGE,"user is null");
            return  getIsMobile() ? "m-input" : INPUT;
        }

        if ( wallet == null) {
            logger.error("wallet does not exists");
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server.timeout"));
            getHttpServletRequest().setAttribute("verified", "false");
            session.put(ConstantGeneral.ERR_MESSAGE,"wallet is null");
            return  getIsMobile() ? "m-input" : INPUT;
        }

        String walletPassword = wallet.getPassword();

        try {

            if(!Utils.isEmpty(walletPassword)){
                password = SHAHashEnrypt.get_MD5_SecurePassword(password);
                if (!password.equals(wallet.getPassword())) {
                    addFieldError("password", getText("errors.invalid.captcha.or.psw"));
                    getHttpServletRequest().setAttribute("verified", "false");
                    return  getIsMobile() ? "m-input" : INPUT;
                }
            } else {
                password = SHAHashEnrypt.get_MD5_SecurePassword(password);
                walletNewPassword = SHAHashEnrypt.get_MD5_SecurePassword(walletNewPassword);
                if (!password.equals(user.getPassword())) {
                    addFieldError("password", getText("errors.invalid.captcha.or.psw"));
                    getHttpServletRequest().setAttribute("verified", "false");
                    return  getIsMobile() ? "m-input" : INPUT;
                }
                wallet.setPassword(walletNewPassword);
                walletManager.resetPassword(wallet);
            }

        }  catch (Exception e) {
            e.printStackTrace();
            writeLog(EntryAction.class.getName(), user, wallet, e, LogLevel.ERROR, LogAction.UPDATE, "");

            logger.error(e);
            session.put(ConstantGeneral.ERR_MESSAGE, getText("errors.internal.server"));
            getHttpServletRequest().setAttribute("verified", "false");
            return  getIsMobile() ? "m-input" : INPUT;
        }

        session.put(ConstantGeneral.SESSION_CONFIRMED, true);
        session.put(ConstantGeneral.INFO, getText("info.welcome.wallet"));

        //String actionUrl = (String) session.remove(ConstantGeneral.SESSION_REDIRECT_ACTION_URL);

        if (!Utils.isEmpty(redirectUrl)) {
            HttpServletResponse resp = getHttpServletResponse();
            StringBuilder ru =  new StringBuilder();
            if(getIsMobile()){
                ru.append("m-");
            }
            ru.append(redirectUrl);
            if(disputeId != null && disputeId > 0){
                ru.append("?disputeId=").append(disputeId);
            } else if(transactionId != null && transactionId > 0){
                ru.append("?transactionId=").append(transactionId);
            }


            try {
                logger.info(ru);
                resp.sendRedirect(ru.toString());
                logger.info("redirected=" + ru);
                return super.execute();
            } catch (IOException e) {
                logger.error(e);
            } catch (Exception e) {
                logger.error(e);
            }
        }

        return ConstantGeneral.RETURN_CONTINUE;
    }

    @Override
    public void validate() {
        if (Utils.isEmpty(password)) {
            addFieldError("password", getText("errors.required", new String[]{getText("pages.registration.password")}));
        }
    }

    public void setWalletNewPassword(String walletNewPassword) {
        this.walletNewPassword = walletNewPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getDisputeId() {
        return disputeId;
    }

    public void setDisputeId(String disputeId) {
        try {
            this.disputeId = Long.parseLong(disputeId);
        } catch (Exception e) {
        }
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        try {
            this.transactionId = Long.parseLong(transactionId);
        } catch (Exception e) {
        }
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }
}
