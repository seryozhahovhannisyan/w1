package com.connectto.general.util;

import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.Partition;
import com.connectto.general.model.User;
import com.connectto.general.model.lcp.Language;
import com.connectto.general.model.lcp.PartitionLCP;
import com.connectto.general.model.lcp.UserProfile;
import com.connectto.wallet.dataaccess.service.general.IWalletLoggerManager;
import com.connectto.wallet.model.general.WalletLogger;
import com.connectto.wallet.model.general.lcp.LogAction;
import com.connectto.wallet.model.general.lcp.LogLevel;
import com.connectto.wallet.model.wallet.Wallet;
import com.connectto.wallet.util.TransactionShellAction;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.CookiesAware;
import org.apache.struts2.interceptor.RequestAware;
import org.apache.struts2.interceptor.SessionAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Map;

/**
 * This class extends the ActionSupport and implements SessionAware and RequestAware
 * The main is to store and return data from session and request
 * <p/>
 * User: Seryozha Hovhannisyan
 * Date: 6/6/13
 * Time: 4:03 PM
 */
public class ShellAction extends ActionSupport implements SessionAware, RequestAware, CookiesAware {

    protected Logger tracker = Logger.getLogger("Tracker");

    private static Logger logger = Logger.getLogger(ShellAction.class);
    private IWalletLoggerManager walletLoggerManager;

    public void setWalletLoggerManager(IWalletLoggerManager walletLoggerManager) {
        this.walletLoggerManager = walletLoggerManager;
    }

    /*static {
        DATA_FILE_PATH = ServletActionContext.getServletContext().getRealPath(ConstantGeneral.MAIN_PATH);
    }*/

    //protected Map<String, Object> actionContext;
    protected Map<String, Object> session;
    protected Map<String, Object> request;
    protected Map<String, String> cookies;
    //protected String localeLang = ActionContext.getContext().getLocale().getLanguage().toString();
    protected ImageSizeLoader imageSizeLoader;

    protected synchronized void writeLog(String className , User sesUser, Wallet wallet, Exception ex, LogLevel logLevel, LogAction logAction, String msg) {
        Date currentDate = new Date(System.currentTimeMillis());
        WalletLogger walletLogger;
        Long userId = sesUser == null ? null : sesUser.getId();
        Long walletId = wallet == null ? null : wallet.getId();
        if (ex != null) {
            logger.error(ex);
            walletLogger = new WalletLogger(userId, walletId, logLevel, className, logAction, ex.getMessage(), currentDate);
        } else {
            logger.error(msg);
            walletLogger = new WalletLogger(userId, walletId, logLevel, className, logAction, msg, currentDate);
        }
        session.put(ConstantGeneral.ERR_MESSAGE, msg);
        try {
            walletLoggerManager.add(walletLogger);
        } catch (InternalErrorException e) {
            logger.error(e);
        }
    }

    protected synchronized void writeLog(User sesUser, Wallet wallet, Exception ex, LogLevel logLevel, LogAction logAction, String msg) {
        Date currentDate = new Date(System.currentTimeMillis());
        WalletLogger walletLogger;
        Long userId = sesUser == null ? null : sesUser.getId();
        Long walletId = wallet == null ? null : wallet.getId();
        if (ex != null) {
            logger.error(ex);
            walletLogger = new WalletLogger(userId, walletId, logLevel, TransactionShellAction.class.getName(), logAction, ex.getMessage(), currentDate);
        } else {
            logger.error(msg);
            walletLogger = new WalletLogger(userId, walletId, logLevel, TransactionShellAction.class.getName(), logAction, msg, currentDate);
        }
        session.put(ConstantGeneral.ERR_MESSAGE, msg);
        try {
            walletLoggerManager.add(walletLogger);
        } catch (InternalErrorException e) {
            logger.error(e);
        }
    }

    public String execute(){
        setIsMobile();
        return  getIsMobile() ? "m-success" : SUCCESS;
    }

    /*@Override
    public void setApplication(Map<String, Object> applicationStorage) {
        this.applicationStorage = applicationStorage;
    }*/

    @Override
    public void setRequest(Map<String, Object> request) {
        this.request = request;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }

    @Override
    public void setCookiesMap(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    //store block

    //getter block
    //   HttpServlet request, session
    protected HttpServletRequest getHttpServletRequest() {
        return ServletActionContext.getRequest();
    }

    protected HttpServletResponse getHttpServletResponse() {
        return ServletActionContext.getResponse();
    }

    protected HttpSession getHHttpSession() {
        return ServletActionContext.getRequest().getSession();
    }

    protected User getCurrentUser() {
        return (User) session.get(ConstantGeneral.SESSION_USER);
    }

    public String getAccountType() {
        //todo use this during back-path vshoo.com/action pre_main.htm
        User user = getCurrentUser();
        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_PARTITION);
        if (user == null || user.getTsmProfile() == null || user.getTsmProfile().getKey() == UserProfile.NONE.getKey()) {
            return PartitionLCP.getDNS(partition.getId());
        } else {
            return user.getTsmProfile().getProfile();
        }
    }

    public Language[] getLanguages() {
        return Language.values();
    }

    public Language getSessionLanguage() {
        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_PARTITION);
        if (partition == null) {
            return Language.getDefault();
        }

        String langKey = Generator.generateLanguageStoreKey(ConstantGeneral.LANGUAGE, partition.getId());
        Language lang = (Language) session.get(langKey);
//        if(lang == null){
//            lang = PartitionLCP.valueOf(partition.getId()).getLanguage();
//        }
        return lang;
    }

    public boolean isUnderConstruction() {
        return Initializer.isUnderConstruction();
    }

    public void setImageSizeLoader(ImageSizeLoader imageSizeLoader) {
        this.imageSizeLoader = imageSizeLoader;
    }

    protected void setIsMobile() {

        // http://www.hand-interactive.com/m/resources/detect-mobile-java.htm
        final String userAgent = getHttpServletRequest().getHeader("User-Agent");
        final String httpAccept = getHttpServletRequest().getHeader("Accept");

        final UAgentInfo detector = new UAgentInfo(userAgent, httpAccept);
//        session.put(ConstantGeneral.SESSION_MOBILE,detector.detectMobileQuick());/
        session.put(ConstantGeneral.SESSION_MOBILE,!detector.getIsPersonalComputer());
    }

    public boolean getIsMobile() {
        Boolean isSessionMobile = (Boolean) session.get(ConstantGeneral.SESSION_MOBILE);
        return (isSessionMobile != null && isSessionMobile);
    }

    protected String[] getBulkSMSSettings() {
        String[] settings = new String[2];
        settings[0] = Initializer.BULK_SMS_USERNAME;
        settings[1] = Initializer.BULK_SMS_PASSWORD;
        return settings;
    }

    @Override
    public String getText(String aTextName) {
        try{
            return Utils.isEmpty(super.getText(aTextName))? "AAAA" : super.getText(aTextName);
        } catch (NullPointerException e){
            return aTextName;
        }
    }
}

