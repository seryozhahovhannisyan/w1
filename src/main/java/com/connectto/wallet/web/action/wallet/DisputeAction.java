package com.connectto.wallet.web.action.wallet;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.Partition;
import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.User;
import com.connectto.general.model.lcp.PartitionLCP;
import com.connectto.general.model.lcp.ResponseStatus;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.ShellAction;
import com.connectto.general.util.Utils;
import com.connectto.notification.MailContent;
import com.connectto.notification.MailException;
import com.connectto.notification.MailSender;
import com.connectto.wallet.dataaccess.service.ITransactionDisputeManager;
import com.connectto.wallet.model.general.lcp.LogAction;
import com.connectto.wallet.model.general.lcp.LogLevel;
import com.connectto.wallet.model.wallet.Transaction;
import com.connectto.wallet.model.wallet.TransactionData;
import com.connectto.wallet.model.wallet.TransactionDispute;
import com.connectto.wallet.model.wallet.Wallet;
import com.connectto.wallet.model.wallet.lcp.DisputeState;
import com.opensymphony.xwork2.Preparable;
import org.apache.log4j.Logger;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Date;
import java.util.List;

/**
 * Created by htdev001 on 9/2/14.
 */
public class DisputeAction extends ShellAction implements Preparable{

    private static final Logger logger = Logger.getLogger(DisputeAction.class.getSimpleName());
    private ResponseDto responseDto;


    public ResponseDto getResponseDto() {
        return responseDto;
    }

    public void setResponseDto(ResponseDto responseDto) {
        this.responseDto = responseDto;
    }

    private ITransactionDisputeManager disputeManager;
    //private ITransactionManager transactionManager;
    //Getters
    private TransactionDispute currentDispute;
    private Transaction transaction;
    //Setter
    private Long transactionId;
    private Long walletExchangeId;
    private String reason;
    private String content;

    private Long disputeId;

    public String viewDispute() {

        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        User user = (User) session.get(ConstantGeneral.SESSION_USER);
        if (wallet == null || user == null) {
            writeLog(DisputeAction.class.getName(), null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        if (disputeId == null || disputeId < 1) {
            writeLog(DisputeAction.class.getName(),user, wallet, null, LogLevel.ERROR, LogAction.READ, "disputeId is empty");
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        try {
            currentDispute = disputeManager.getById(disputeId);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(DisputeAction.class.getName(),user, wallet, e, LogLevel.ERROR, LogAction.READ, "");
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        } catch (EntityNotFoundException e) {
            writeLog(DisputeAction.class.getName(),user, wallet, e, LogLevel.ERROR, LogAction.READ, "");
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }
        return SUCCESS;
    }

    public String dispute() {

        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        User user = (User) session.get(ConstantGeneral.SESSION_USER);

        if(wallet == null || user == null ){
            writeLog(DisputeAction.class.getName(), null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        if (!isValidId()) {
            writeLog(DisputeAction.class.getName(),user, wallet, null, LogLevel.ERROR, LogAction.INSERT, "transactionId or walletExchangeId is empty");
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        if (Utils.isEmpty(reason)) {
            writeLog(DisputeAction.class.getName(),user, wallet, null, LogLevel.ERROR, LogAction.INSERT, "reason is empty");
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        if (Utils.isEmpty(content)) {
            writeLog(DisputeAction.class.getName(),user, wallet, null, LogLevel.ERROR, LogAction.INSERT, "content is empty");
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        Long disputedBy = user.getId();
        Date currentDate = new Date(System.currentTimeMillis());
        List<TransactionData> transactionDatas = (List<TransactionData>) session.remove(ConstantGeneral.WALLET_TRANSACTION_FILE_DATAS);

        TransactionDispute transactionDispute = new TransactionDispute();

        transactionDispute.setDisputedAt(currentDate);
        transactionDispute.setReason(reason);
        transactionDispute.setContent(content);
        transactionDispute.setTransactionId(transactionId);
        transactionDispute.setWalletExchangeId(walletExchangeId);
        transactionDispute.setDisputedById(disputedBy);
        transactionDispute.setState(DisputeState.PENDING);
        transactionDispute.setTransactionDatas(transactionDatas);

        try {

            disputeManager.add(transactionDispute);
            String partitionDns = PartitionLCP.getDNS(user.getPartitionId());
            Partition partition = (Partition)session.get(ConstantGeneral.SESSION_PARTITION);
            String fromEmail = partition.getPartitionEmail();
            String fromEmailPassword = partition.getPartitionEmailPassword();
            String userData =  String.format("%s %s", user.getName(), user.getSurname());

            StringWriter writer = new StringWriter();
            Template template = Velocity.getTemplate("mail" + partitionDns + ".vm","UTF-8");
            VelocityContext context = new VelocityContext();

            String href =  "https://core.hollor.com/wallet/view-dispute.htm?disputeId=" + transactionDispute.getId()+"&partitionId=" + partition.getId();
            String url = "<a href='"+href+"'>"+href+"</a>";

            context.put("Greeting", "Dear");
            context.put("Name", "Administration");
            context.put("title", "Dear Administration!! The User  "+userData+" disputed transaction");
            context.put("ignore", "if you didn't sign up with  "+ partition.getName()+ " please ignore this message ");
            context.put("message", "Please answer it with this url: " + href );
            context.put("goto", url);
            context.put("Action", "Visit");
            template.merge(context, writer);

            MailContent mailContent = new MailContent();
            mailContent.setEmailsTo(new String[]{fromEmail});
            mailContent.setEmailsCC(new String[]{"seryozha.hovhannisyan@gmail.com"});
            mailContent.setSubject("New Dispute::");
            mailContent.setContent(writer.toString());
            mailContent.setRecipientTypeTo();

            MailSender mailNotification = new MailSender();
            mailNotification.sendEmailFromConnectTo(mailContent, fromEmail, fromEmailPassword);

            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(DisputeAction.class.getName(),user, wallet, e, LogLevel.ERROR, LogAction.INSERT, "");
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        } catch (MailException e) {
            writeLog(DisputeAction.class.getName(),user, wallet, e, LogLevel.ERROR, LogAction.INSERT, "");
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        return SUCCESS;
    }

    @Deprecated
    public String closeDispute() {
        Wallet wallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        User user = (User) session.get(ConstantGeneral.SESSION_USER);
        if (wallet == null || user == null  ) {
            writeLog(DisputeAction.class.getName(), null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        if (disputeId == null || disputeId < 1) {
            writeLog(DisputeAction.class.getName(),user, wallet, null, LogLevel.ERROR, LogAction.INSERT, "disputeId is empty");
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }

        try {
            disputeManager.closeDispute(disputeId);
        } catch (InternalErrorException e) {
            writeLog(DisputeAction.class.getName(),user, wallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        } catch (EntityNotFoundException e) {
            writeLog(DisputeAction.class.getName(),user, wallet, e, LogLevel.ERROR, LogAction.UPDATE, "");
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }
        return SUCCESS;
    }

    private synchronized boolean isValidId(){
        boolean isValid = false;
        if(transactionId != null && transactionId > 0){
            isValid = true;
        } else if(walletExchangeId != null && walletExchangeId > 0){
            isValid = true;
        }
        return isValid;
    }

    /*
     *##################################################################################################################
     *Getters
     *##################################################################################################################
     */

    public Transaction getTransaction() {
        return transaction;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public TransactionDispute getCurrentDispute() {
        return currentDispute;
    }

    public Long getDisputeId() {
        return disputeId;
    }

    /*
     *##################################################################################################################
     *Setters
     *##################################################################################################################
     */

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTransactionId(String transactionId) {
        try {
            this.transactionId = Long.parseLong(transactionId);
        } catch (Exception e) {
            this.transactionId = null;
            logger.warn("Incorrect transactionId ,  transactionId=" + transactionId);
        }
    }

    public Long getWalletExchangeId() {
        return walletExchangeId;
    }

    public void setWalletExchangeId(String walletExchangeId) {
        try {
            this.walletExchangeId = Long.parseLong(walletExchangeId);
        } catch (Exception e) {
            this.walletExchangeId = null;
            logger.warn("Incorrect walletExchangeId ,  walletExchangeId=" + walletExchangeId);
        }
    }

    public void setDisputeId(String disputeId) {
        try {
            this.disputeId = Long.parseLong(disputeId);
        } catch (Exception e) {
            this.disputeId = null;
            logger.error("Incorrect disputeId ,  disputeId=" + disputeId);
        }
    }


    public void setDisputeManager(ITransactionDisputeManager disputeManager) {
        this.disputeManager = disputeManager;
    }

//    public void setTransactionManager(ITransactionManager transactionManager) {
//        this.transactionManager = transactionManager;
//    }

    @Override
    public void prepare() throws Exception {
        setIsMobile();
    }
}
