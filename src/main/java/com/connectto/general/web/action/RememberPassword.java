package com.connectto.general.web.action;

import com.connectto.general.dataaccess.service.IPartitionSetupManager;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.Partition;
import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.User;
import com.connectto.general.model.lcp.PartitionLCP;
import com.connectto.general.model.lcp.ResponseStatus;
import com.connectto.general.util.*;
import com.connectto.general.util.encryption.EncryptException;
import com.connectto.general.util.encryption.SHAHashEnrypt;
import com.connectto.notification.MailContent;
import com.connectto.notification.MailException;
import com.connectto.notification.MailSender;
import com.connectto.wallet.dataaccess.service.wallet.IWalletManager;
import com.connectto.wallet.model.wallet.Wallet;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.File;
import java.io.StringWriter;


/**
 * Created with IntelliJ IDEA.
 * User: Serozh
 * Date: 11.11.13
 * Time: 0:51
 * To change this template use File | Settings | File Templates.
 */
public class RememberPassword extends ShellAction {

    private static final Logger logger = Logger.getLogger(RememberPassword.class.getSimpleName());

    private IWalletManager walletManager;
    private IPartitionSetupManager partitionSetupManager;

    private String user;
    private String token;
    private String password;

    private ResponseDto responseDto;

    @SkipValidation
    public String redirectResetPassword() {
        logger.info("redirectResetPassword user " + user + " token " + token);
        session.put("u", user);
        session.put("t", token);
        return SUCCESS;
    }

    @SkipValidation
    public String resetPassword() {

        setIsMobile();

        String u = (String)session.get("u");
        String t = (String)session.get("t");
        session.remove("u");
        session.remove("t");
        logger.info("header user " + u + " token " + t);

        if (Utils.isEmpty(u) || Utils.isEmpty(t)) {
            addActionError(getText("page.reset.expired.url"));
        }

        try {
            Wallet wallet =  walletManager.getWalletByUserAndResetPasswordToken(u, t);
            session.put(ConstantGeneral.RESET_SESSION_WALLET, wallet);
        } catch (InternalErrorException e1) {
            logger.error(e1);

            addActionError(getText("page.reset.expired.url"));
        } catch (EntityNotFoundException e1) {
            logger.error(e1);

            addActionError(getText("page.reset.expired.url"));
        }

        /*StringBuilder result = new StringBuilder(mobilPrefix);
        result.append(PartitionLCP.getDNS(partitionId));*/

        return  getIsMobile() ? "m-success" : SUCCESS;
    }

    public String execute() {

        responseDto.cleanMessages();

        Wallet wallet = (Wallet)session.get(ConstantGeneral.SESSION_WALLET);
        User user = (User)session.get(ConstantGeneral.SESSION_USER);
        int partitionId = user.getPartitionId();

        String partitionHost= PartitionLCP.getHost(partitionId);
        String partitionDns= PartitionLCP.getDNS(partitionId);

        try {

            String token = Generator.generateAlphaNumeric(ConstantGeneral.TOKEN_LENGTH);
            Wallet updateWallet= new Wallet();
            updateWallet.setUserId(wallet.getUserId());
            updateWallet.setResetPasswordToken(token);
            walletManager.resetPassword(updateWallet);

            StringBuilder gotoBuilder = new StringBuilder(partitionHost);
            gotoBuilder.append("/wallet");
            gotoBuilder.append("/redirect-wallet-reset-password").append(".htm?").
                    append("user=").append(user.getId()).
                    append("&token=").append(token);

            Partition partition = partitionSetupManager.getPartitionById(partitionId);

            String fromEmail = partition.getPartitionEmail();
            String fromEmailPassword = partition.getPartitionEmailPassword();
            if (Utils.isEmpty(fromEmail) || Utils.isEmpty(fromEmailPassword)) {
                responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
                return SUCCESS;
            }

            String partitionName = partition.getName();

            String subject = getText("mail.subject.reset.password", new String[]{partitionName});

            StringWriter writer = new StringWriter();
            Template template = Velocity.getTemplate("mail" + partitionDns + ".vm","UTF-8");
            VelocityContext context = new VelocityContext();

            context.put("Greeting", getText("mail.greeting"));
            context.put("Name", String.format("%s %s", user.getName(), user.getSurname()));
            context.put("title", getText("mail.title.reset.password"));
            context.put("ignore", getText("mail.ignore"));
            context.put("Action", getText("mail.action.reset.password"));
            context.put("goto", gotoBuilder.toString());
            template.merge(context, writer);

            String contentPath = Initializer.context.getRealPath("");

            StringBuilder logo = new StringBuilder(contentPath);
            logo.append(File.separator).append("img").
                    append(File.separator).append("general").
                    append(File.separator).append(partitionDns).
                    append(File.separator).append("logo.png");

            MailContent mailContent = new MailContent();
            mailContent.setEmailsTo(new String[]{"seryozha.hovhannisyan@gmail.com", user.getEmail(), fromEmail});
            mailContent.setSubject(subject);
            mailContent.setContent(writer.toString());
            mailContent.setRecipientTypeTo();
            mailContent.addDataSource("logo", logo.toString());

            MailSender mailNotification = new MailSender();
            try {
                mailNotification.sendEmailFromConnectTo(mailContent, fromEmail, fromEmailPassword);
            } catch (MailException e) {
                e.printStackTrace();
            }

            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            logger.error(e);
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            logger.error(e);
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        }
        return SUCCESS;
    }

    public String reset() {

        setIsMobile();

        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_PARTITION);

        Wallet sesWallet = (Wallet) session.remove(ConstantGeneral.RESET_SESSION_WALLET);
        if (sesWallet == null) {
            logger.error("Incorrect incoming data, incoming browser user was null");
            String message = getText("page.reset.expired.url");
            addActionError(message);
            return  getIsMobile() ? "m-error" : ERROR;
        }

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
            return  getIsMobile() ? "m-error" : ERROR;
        }

        if (Utils.isEmpty(password)) {
            logger.info("Validation not passed empty password");
            addFieldError("password", getText("errors.required", new String[]{getText("pages.registration.password")}));
            addActionError(getText("errors.required", new String[]{getText("pages.registration.password")}));
            return  getIsMobile() ? "m-error" : ERROR;
        } else if (!Utils.isValidPassword(password)) {
            logger.info("Validation not passed empty password");
            addFieldError("password", getText("errors.required", new String[]{getText("pages.registration.password.lengt.6")}));
            addActionError(getText("errors.required", new String[]{getText("pages.registration.password.lengt.6")}));
            return  getIsMobile() ? "m-error" : ERROR;
        }

        try {
            password = SHAHashEnrypt.get_MD5_SecurePassword(password);
        } catch (EncryptException e) {
            logger.warn(e);
        }

        Wallet wallet = new Wallet();
        wallet.setUserId(sesWallet.getUserId());
        wallet.setPassword(password);
        wallet.setResetPasswordToken("");
        try {
            walletManager.resetPassword(wallet);
            addActionMessage(getText("pages.reset.success"));
            return SUCCESS;
        } catch (InternalErrorException e) {
            logger.error(e);
            addActionError(getText("errors.internal.server"));
            return  getIsMobile() ? "m-error" : ERROR;
        } catch (EntityNotFoundException e) {
            logger.error(e);
            addActionError(getText("errors.internal.server"));
            return  getIsMobile() ? "m-error" : ERROR;
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ResponseDto getResponseDto() {
        return responseDto;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setWalletManager(IWalletManager walletManager) {
        this.walletManager = walletManager;
    }

    public void setResponseDto(ResponseDto responseDto) {
        this.responseDto = responseDto;
    }

    public void setPartitionSetupManager(IPartitionSetupManager partitionSetupManager) {
        this.partitionSetupManager = partitionSetupManager;
    }
}
