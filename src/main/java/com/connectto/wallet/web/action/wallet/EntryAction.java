package com.connectto.wallet.web.action.wallet;

import com.connectto.general.dataaccess.service.IPartitionSetupManager;
import com.connectto.general.dataaccess.service.IUserManager;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.Account;
import com.connectto.general.model.Partition;
import com.connectto.general.model.User;
import com.connectto.general.model.WalletSetup;
import com.connectto.general.model.lcp.PartitionLCP;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.Generator;
import com.connectto.general.util.ShellAction;
import com.connectto.general.util.Utils;
import com.connectto.wallet.dataaccess.service.wallet.IWalletManager;
import com.connectto.wallet.model.general.lcp.LogAction;
import com.connectto.wallet.model.general.lcp.LogLevel;
import com.connectto.wallet.model.wallet.Wallet;
import com.connectto.wallet.model.wallet.WalletLocation;
import com.opensymphony.xwork2.ActionContext;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * User: Seryozha Hovhannisyan
 * Date: 6/6/13
 * Time: 3:16 PM
 */
public class EntryAction extends ShellAction {

    private static final Logger logger = Logger.getLogger(EntryAction.class.getSimpleName());
    private IUserManager userManager;
    private IWalletManager walletManager;
    private IPartitionSetupManager partitionSetupManager;

    private String latitude;
    private String longitude;

    private Long disputeId = 0L;
    private Long transactionId = 0L;
    private String redirectUrl;

    public String entry() {
        setIsMobile();//http://localhost:8787/redirect-action-completed-transaction.htm?disputeId=6&sessionId=CQ8ZCVW4IG0HIQ4HLN2TTGLMV0D8G114
        HttpServletRequest req = getHttpServletRequest();
        HttpServletResponse resp = getHttpServletResponse();
        Date currentDate = new Date(System.currentTimeMillis());

        String sessionId = findSessionId();
        Partition partition = (Partition)session.get(ConstantGeneral.SESSION_URL_PARTITION);
        User user = null;

        String serverName = new String(req.getServerName());
        if(serverName.contains("127.0.0.1") || serverName.contains("192.168.40.25") || serverName.contains("192.168.1.110") || serverName.contains("localhost") || serverName.contains("204.174.104.63")){
            logger.info("DEV MODE");
            serverName = "hollor.com";
        }

        PartitionLCP partitionLCP = PartitionLCP.findCorrespondingPartitionLCP(serverName);
        String partitionHost = partitionLCP.getHost();

        try {

            if(partition == null){

                partition = partitionSetupManager.getPartitionById(partitionLCP.getId());
                partition.setServerName(serverName);

                logger.info("ENTRY : partition"+partition);
                //image valid path
                String pUrl = partition.getPartitionServerUrl();
                if(pUrl.contains("https:")) {
                    String dns = pUrl.replace("https:", "");
                    int indexOfPort = dns.lastIndexOf(":");
                    if (indexOfPort > 4) {
                        dns = dns.substring(0, indexOfPort);
                    }
                    partition.setPartitionServerUrl("https:"+ dns);
                } else if(pUrl.contains("http:")) {
                    String dns = pUrl.replace("http:", "");
                    int indexOfPort = dns.lastIndexOf(":");
                    if (indexOfPort > 4) {
                        dns = dns.substring(0, indexOfPort);
                    }
                    partition.setPartitionServerUrl("http:"+ dns);
                } else {
                    int indexOfPort = pUrl.lastIndexOf(":");
                    if (indexOfPort > 4) {
                        pUrl = pUrl.substring(0, indexOfPort);
                    }
                    partition.setPartitionServerUrl(pUrl);
                }
            }


            user = userManager.getUserByAccountSessionId(sessionId);
            session.put(ConstantGeneral.SESSION_USER, user);
            partition =  user.getPartition();
            WalletSetup walletSetup = partition.getWalletSetup();
            walletSetup.parseAvailableRates();
            session.put(ConstantGeneral.SESSION_URL_PARTITION, partition);
        } catch (InternalErrorException e) {
            writeLog(EntryAction.class.getName(), null, null, e, LogLevel.ERROR, LogAction.READ, "");
        } catch (EntityNotFoundException e) {
            writeLog(EntryAction.class.getName(), null, null, e, LogLevel.ERROR, LogAction.READ, "");
        }

        if(user == null && partitionHost != null){
            HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
            try {
                response.sendRedirect(partitionHost);
                return super.execute();
            } catch (IOException e) {
                logger.error(e);
                return  getIsMobile() ? "m-error" : ERROR;
            } catch (Exception e) {
                logger.error(e);
                return  getIsMobile() ? "m-error" : ERROR;
            }
        }

        Account currentAccount = user.getCurrentAccount();

        String userAgent = req.getHeader("User-Agent");

        Wallet wallet = new Wallet();
        wallet.setUserId(user.getId());
        wallet.setCurrentAccountLastUrl(partitionHost);
        wallet.setCurrentAccountId(currentAccount.getId());
        wallet.setLoggedAt(currentDate);

        String encryptKey = Generator.generateRandomString(ConstantGeneral.TOKEN_WALLET_LENGTH);

        WalletLocation walletLocation = new WalletLocation();

        walletLocation.setWalletId(wallet.getId());
        walletLocation.setEncryptKey(encryptKey);
        walletLocation.setPreviousUrl(partitionHost);
        walletLocation.setLoginDate(currentDate);
        walletLocation.setUserAgent(userAgent);
        walletLocation.setLatitude(latitude);
        walletLocation.setLongitude(longitude);

        wallet.setCurrentLocation(walletLocation);

        try {
            wallet = walletManager.entry(wallet);
            wallet.setUserId(user.getId());
            session.put(ConstantGeneral.SESSION_WALLET, wallet);
            Utils.setLanguage(session, user.getLanguage(), true, user.getPartitionId());
        } catch (Exception e) {
            logger.error(e);

            writeLog(EntryAction.class.getName(), user, null, e, LogLevel.ERROR, LogAction.READ, "");
            if(partitionHost != null){
                HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
                try {
                    response.sendRedirect(partitionHost);
                    return super.execute();
                } catch (IOException e1) {
                    logger.error(e1);
                } catch (Exception e1) {
                    logger.error(e1);
                }
            }

            return  getIsMobile() ? "m-error" : ERROR;
        }

        String uri = req.getRequestURI();
        if(uri.contains(ConstantGeneral.SESSION_REDIRECT_ACTION_LABEL)){
            redirectUrl = uri.replace(ConstantGeneral.SESSION_REDIRECT_ACTION_LABEL,"");
            Boolean confirmed = (Boolean)session.get(ConstantGeneral.SESSION_CONFIRMED);
            if(confirmed != null && confirmed){

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
        }

        return  getIsMobile() ? "m-success" : SUCCESS;
    }

    public String exit() {

        HttpServletResponse resp = getHttpServletResponse();
        HttpServletRequest req = getHttpServletRequest();
        User user = (User) session.get(ConstantGeneral.SESSION_USER);

        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_URL_PARTITION);
        getHHttpSession().invalidate();
        //session.clear();

        String host = PartitionLCP.valueOf(partition.getId()).getHost();
        String menu = String.format("%s/back-to-menu-%s.htm?sessionId=%s",host,PartitionLCP.getDNS(partition.getId()), user.getCurrentAccount().getSessionId());
        try {
            resp.sendRedirect(menu);
            return super.execute();
        } catch (IOException e) {
            logger.error(e);
        } catch (Exception e) {
            logger.error(e);
        }

        return SUCCESS;
    }



    private synchronized String findSessionId() {
        String sessionId = (String) session.remove("sessionId");
        logger.info("session sessionId " + sessionId);
        if (Utils.isEmpty(sessionId)) {
            sessionId = getHttpServletRequest().getHeader("sessionId");
            logger.info("Header sessionId " + sessionId);
        }  if (Utils.isEmpty(sessionId)) {
            sessionId = (String) getHttpServletRequest().getAttribute("sessionId");
            logger.info("Attribute sessionId " + sessionId);
        }  if (Utils.isEmpty(sessionId)) {
            sessionId = getHttpServletRequest().getParameter("sessionId");
            logger.info("Parameter sessionId " + sessionId);
        }

        session.put("sessionId", sessionId); 
        return sessionId;
    }

    private synchronized String findPartitionDns() {
        String partitionDns = getHttpServletRequest().getHeader("partitionDns");
        logger.info("Header partitionDns " + partitionDns);
        if (Utils.isEmpty(partitionDns)) {
            partitionDns = (String) getHttpServletRequest().getAttribute("partitionDns");
            logger.info("Attribute partitionDns " + partitionDns);
        }

        if (Utils.isEmpty(partitionDns)) {
            partitionDns = getHttpServletRequest().getParameter("partitionDns");
            logger.info("Parameter partitionDns " + partitionDns);
        }
        return partitionDns;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setUserManager(IUserManager userManager) {
        this.userManager = userManager;
    }

    public void setWalletManager(IWalletManager walletManager) {
        this.walletManager = walletManager;
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

    public String getRedirectUrl() {
        return redirectUrl;
    }
}
