package com.connectto.general.web.action;

import com.connectto.general.dataaccess.service.IUserManager;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.HttpConnectionDeniedException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.Partition;
import com.connectto.general.model.User;
import com.connectto.general.model.lcp.PartitionLCP;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.ShellAction;
import com.connectto.general.util.Utils;
import com.connectto.general.util.encryption.EncryptException;
import com.connectto.general.util.encryption.SHAHashEnrypt;
import com.connectto.general.util.resourse.CookieUtil;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: Seryozha Hovhannisyan
 * Date: 6/6/13
 * Time: 3:16 PM
 */
public class Logout extends ShellAction {

    private static final Logger logger = Logger.getLogger(Logout.class.getSimpleName());

    private IUserManager userManager;

    @SkipValidation
    public String pre_execute() {
        return SUCCESS;
    }

    public String logout() {

        System.out.println("Logout");
        HttpServletResponse resp = getHttpServletResponse();
        HttpServletRequest req = getHttpServletRequest();
        User user = (User) session.get(ConstantGeneral.SESSION_USER);

        if (user != null) {
            try {
                String result = userManager.logout(user);
                if (Utils.isEmpty(result)) {
                    String errorMsg = "Transportation Logout returns null invalid response";
                    logger.error(errorMsg);
                }
                CookieUtil.removeCookie(resp, String.format("%s_%s", PartitionLCP.getDNS(user.getPartitionId()), ConstantGeneral.COOKIE_SAVED_ACCOUNT_LOGIN_KEY));

                try {
                    String encodeEmail = SHAHashEnrypt.get_MD5_SecurePassword(user.getEmail());
                    String lKey = user.getCurrentAccount() == null ? "" : user.getCurrentAccount().getLoginKey();
                    CookieUtil.removeCookie(req, resp, encodeEmail, lKey);
                } catch (EncryptException e) {
                    logger.warn(e);
                }

                logger.info("User id=[" + user.getId() + "] with current account id=[" + user.getId() + "] is successfully log outed");
            } catch (InternalErrorException e) {
                logger.error(e);
            } catch (EntityNotFoundException e) {
                logger.error(e);
            } catch (HttpConnectionDeniedException e) {
                logger.error(e);
            }

        } else {
            logger.error("The current session have not any user");
        }
        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_URL_PARTITION);
        getHHttpSession().invalidate();
        //session.clear();

        String host = PartitionLCP.valueOf(partition.getId()).getHost();
        try {
            resp.sendRedirect(host);
            return super.execute();
        } catch (IOException e) {
            logger.error(e);
        } catch (Exception e) {
            logger.error(e);
        }

        return SUCCESS;
    }

    public String closeBrowser() {
        HttpServletRequest servletRequest = getHttpServletRequest();
        //getHHttpSession().invalidate();
        //session.clear();

        return SUCCESS;
    }



    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }
}
