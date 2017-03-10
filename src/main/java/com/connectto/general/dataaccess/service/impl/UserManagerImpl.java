package com.connectto.general.dataaccess.service.impl;

import com.connectto.general.dataaccess.dao.IUserDao;
import com.connectto.general.dataaccess.service.IUserManager;
import com.connectto.general.exception.DatabaseException;
import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.HttpConnectionDeniedException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.Account;
import com.connectto.general.model.Partition;
import com.connectto.general.model.User;
import com.connectto.general.model.WalletSetup;
import com.connectto.general.model.lcp.PartitionLCP;
import com.connectto.general.util.HttpURLBaseConnection;
import com.connectto.general.util.Initializer;
import com.connectto.general.util.Utils;
import com.connectto.notification.MailContent;
import com.connectto.notification.MailException;
import com.connectto.notification.MailSender;
import com.connectto.wallet.dataaccess.dao.wallet.IWalletSetupDao;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by htdev001 on 2/19/14.
 */
@Transactional(readOnly = true)
public class UserManagerImpl implements IUserManager {

    private static final Logger logger = Logger.getLogger(UserManagerImpl.class.getSimpleName());

    private IUserDao userDao;
    private IWalletSetupDao walletSetupDao;

    public void setUserDao(IUserDao userDao) {
        this.userDao = userDao;
    }

    public void setWalletSetupDao(IWalletSetupDao walletSetupDao) {
        this.walletSetupDao = walletSetupDao;
    }

    @Override
    public User getUserByAccountSessionId(String sessionId) throws InternalErrorException, EntityNotFoundException {

        try {
            User user = userDao.getUserByAccountSessionId(sessionId);
            Partition partition = user.getPartition();
            int partitionId = partition.getId();
            WalletSetup walletSetup = walletSetupDao.getByPartitionId(partitionId);
            partition.setWalletSetup(walletSetup);
            user.setPartition(partition);
            return user;
        } catch (DatabaseException e) {
            logger.error(e);
            throw new InternalErrorException(e);
        }
    }

    @Override
    public List<User> getByParams(Map<String, Object> params) throws InternalErrorException {
        try {
            return userDao.getByParams(params);
        } catch (DatabaseException e) {
            logger.error(e);
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void updateLanguage(User user) throws InternalErrorException, EntityNotFoundException {
        try {
            userDao.updateLanguage(user);
        } catch (DatabaseException e) {
            logger.error(e);
            throw new InternalErrorException(e);
        }
    }

    @Override
    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public String logout(User user) throws InternalErrorException, EntityNotFoundException, HttpConnectionDeniedException {

        Account account = user.getCurrentAccount();
        Partition partition = user.getPartition();

        if (account != null) {
            Date currentDate = new Date(System.currentTimeMillis());


            String tsmSessionId = account.getSessionId();
            String loginKey = account.getLoginKey();

            account.setSessionId("");
            account.setOldSessionId(tsmSessionId);

            account.setLoginKey("");
            account.setOldLoginKey(loginKey);

            account.setSavePassword(false);
            account.setLogoutDate(currentDate);

            try {

                userDao.logoutAccount(account);
                if (PartitionLCP.isVshoo(user.getPartitionId())) {

                    String testConnection = HttpURLBaseConnection.transportationJsonAction(Initializer.TRANSPORTATION_TEST_CONNECTION, null);
                    if (Utils.isEmpty(testConnection) && partition != null) {
                        String errorMsg = "Transportation Test Connection not exists";
                        alertNetworkAdmin(partition, user, Initializer.TRANSPORTATION_TEST_CONNECTION);
                        throw new HttpConnectionDeniedException(errorMsg);
                    }

                    JSONObject transportationRequest = new JSONObject();
                    transportationRequest.put("userId", user.getId());
                    transportationRequest.put("sessionId", tsmSessionId);


                    logger.info("transportationJsonAction Start transportationRequest=" + transportationRequest.toString());

                    return HttpURLBaseConnection.transportationJsonAction(Initializer.TRANSPORTATION_LOGOUT, transportationRequest);

                }

                return "OK";

            } catch (DatabaseException e) {
                throw new InternalErrorException(e);
            } catch (EntityNotFoundException e) {
                throw new EntityNotFoundException(e);
            }
        } else {
            throw new EntityNotFoundException("User have not current account user id=[" + user.getId() + "]");
        }

    }

    private void alertNetworkAdmin(Partition partition, User user, String url) {
        String fromEmail = partition.getPartitionEmail();
        String fromEmailPassword = partition.getPartitionEmailPassword();

        String subject = "Transportation connection refused";
        String partitionDns = PartitionLCP.getDNS(partition.getId());


        String userData = "";
        if (user != null) {
            userData = "The User " + String.format("%s %s", user.getName(), user.getSurname());
        }
        StringWriter writer = new StringWriter();
        Template template = Velocity.getTemplate("mail" + partitionDns + ".vm");
        VelocityContext context = new VelocityContext();

        context.put("Greeting", "Dear");
        context.put("Name", String.format("%s %s", user.getName(), user.getSurname()));
        context.put("title", "Dear Network Administration!! " + userData + "The url=" + url + " is not working");
        context.put("ignore", "if you didn't sign up with  " + partition.getName() + " please ignore this message ");
        context.put("message", "");
        context.put("Action", "Visit");
        template.merge(context, writer);

        MailContent mailContent = new MailContent();
        mailContent.setEmailsTo(new String[]{Initializer.NOC_ADMIN_EMAIL});
        mailContent.setEmailsCC(new String[]{fromEmail});
        mailContent.setSubject(subject);
        mailContent.setContent(writer.toString());
        mailContent.setRecipientTypeTo();

        MailSender mailNotification = new MailSender();
        try {
            mailNotification.sendEmailFromConnectTo(mailContent, fromEmail, fromEmailPassword);
        } catch (MailException e) {
            e.printStackTrace();
        }

    }

}
