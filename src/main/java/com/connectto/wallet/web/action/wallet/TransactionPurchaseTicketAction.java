package com.connectto.wallet.web.action.wallet;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.User;
import com.connectto.general.model.WalletSetup;
import com.connectto.general.model.lcp.ResponseStatus;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.ShellAction;
import com.connectto.general.util.Utils;
import com.connectto.wallet.dataaccess.service.ITransactionManager;
import com.connectto.wallet.dataaccess.service.wallet.IWalletManager;
import com.connectto.wallet.dataaccess.service.wallet.IWalletSetupManager;
import com.connectto.wallet.model.general.lcp.LogAction;
import com.connectto.wallet.model.general.lcp.LogLevel;
import com.connectto.wallet.model.wallet.Wallet;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import org.apache.log4j.Logger;

/**
 * Created by Serozh on 2/15/16.
 */
@Deprecated
public class TransactionPurchaseTicketAction  extends ShellAction {


    private static final Logger logger = Logger.getLogger(TransactionPurchaseTicketAction.class.getSimpleName());
    private IWalletManager walletManager;
    private IWalletSetupManager walletSetupManager;
    private ITransactionManager transactionManager;

    private ResponseDto responseDto;
    //new
    private String itemId;
    private String purchaseType;
    private String name;
    private String description;

    private String userId;
    private String amount;
    private String currencyType;
    private String partitionId;
    protected String sessionId;
    //old    //

    private String orderCode;

    @Deprecated
    public String freezeWallet() {

        responseDto.cleanMessages();
        if (Utils.isEmpty(sessionId)) {
            String msg = getText("wallet.back.end.message.empty.sessionId");
            responseDto.addMessage(msg);
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        boolean valid = validatePartitionId();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        Integer pId = Integer.parseInt(partitionId);
        valid = validateParams();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        try {

            WalletSetup toWalletSetup = walletSetupManager.getByPartitionId(pId);
            Wallet myWallet = walletManager.getByUserId(Long.parseLong(userId));

            Double productAmount = Double.parseDouble(amount);
            int selectedCurrencyTypeId = Integer.parseInt(currencyType);
            CurrencyType productCurrencyType = CurrencyType.valueOf(selectedCurrencyTypeId);

            User sesUser = null;
            if (Utils.isEmpty(sessionId)) {
                sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
                if (sesUser != null) {
                    sessionId = sesUser.getCurrentAccount().getSessionId();
                }
            }

           /* TransactionPurchaseTicket purchaseTicket = initPurchase();
            Transaction transaction = TransactionUtil.createTransaction(productAmount, productCurrencyType, myWallet, null, toWalletSetup, TransactionState.SEND_MONEY, sessionId);
            transaction.addPurchaseTicket(purchaseTicket);
            transaction.setSessionId(sessionId);*/
            //transactionManager.add(transaction);
            responseDto.setStatus(ResponseStatus.SUCCESS);

        } catch (InternalErrorException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        } /*catch (PermissionDeniedException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (InvalidParameterException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
        }*/

        return SUCCESS;
    }

    @Deprecated
    public String freezeWalletCharge() {

        /*if (Utils.isEmpty(orderCode)) {
            String msg = getText("wallet.back.end.message.empty.orderCode");
            responseDto.addMessage(msg);
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        if (Utils.isEmpty(sessionId)) {
            String msg = getText("wallet.back.end.message.empty.sessionId");
            responseDto.addMessage(msg);
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        boolean valid = validateUserId();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        valid = validatePartitionId();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }
        Integer pId = Integer.parseInt(partitionId);
        valid = validateParams();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        try {

            Wallet userWallet = walletManager.getByUserId(Long.parseLong(userId));
            Transaction frozenTransaction = transactionManager.getByOrderCode(orderCode);
            if (TransactionState.PENDING.getId() != frozenTransaction.getState().getId()) {
                writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, "The approve action not allowed for current transaction transaction orderCode = " + frozenTransaction.getOrderCode());
                responseDto.addMessage(getText("errors.internal.server"));
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            if (TransactionState.PENDING.getId() == frozenTransaction.getActionState().getId() && !userWallet.getId().equals(frozenTransaction.getFromWalletId())) {
                writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, "The approve action not allowed for current transaction transaction id = " + frozenTransaction.getId());
                responseDto.addMessage(getText("errors.internal.server"));
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            WalletSetup toWalletSetup = walletSetupManager.getByPartitionId(pId);
            Wallet myWallet = walletManager.getByUserId(Long.parseLong(userId));

            Double productAmount = Double.parseDouble(amount);
            int selectedCurrencyTypeId = Integer.parseInt(currencyType);
            CurrencyType productCurrencyType = CurrencyType.valueOf(selectedCurrencyTypeId);

            Transaction chargeTransaction = prepareTransactionForCharge(productAmount, productCurrencyType, myWallet, null, toWalletSetup, TransactionState.SEND_MONEY);
            //todo

            String sessionIdFrozen = frozenTransaction.getSessionId();
            String sessionIdCharge = chargeTransaction.getSessionId();

            CurrencyType currencyTypeProductFrozen = frozenTransaction.getProductCurrencyType();
            CurrencyType currencyTypeProductCharge = chargeTransaction.getProductCurrencyType();

            CurrencyType currencyTypeSetupFrozen = frozenTransaction.getSetupAmountCurrencyType();
            CurrencyType currencyTypeSetupCharge = chargeTransaction.getSetupAmountCurrencyType();

            //todo check transaction's relations
            chargeTransaction.setFrozenTransactionId(frozenTransaction.getId());
            chargeTransaction.setFrozenTransaction(frozenTransaction);


            transactionManager.chargeFrozenTransaction(chargeTransaction);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.UPDATE, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.UPDATE, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        } catch (PermissionDeniedException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }*/

        return SUCCESS;
    }

    @Deprecated
    public String freezeWalletCancel() {

        /*if (Utils.isEmpty(orderCode)) {
            String msg = getText("wallet.back.end.message.empty.orderCode");
            responseDto.addMessage(msg);
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        boolean valid = validateUserId();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        try {

            User user = null;//todo userManager.getById(Long.parseLong(userId));
            Transaction transaction = transactionManager.getByOrderCode(orderCode);
            if (TransactionState.PENDING.getId() != transaction.getState().getId()) {
                writeLog(user, null, null, LogLevel.ERROR, LogAction.READ, "The reject action not allowed for current transaction transaction orderCode = " + transaction.getOrderCode());
                responseDto.addMessage(getText("errors.internal.server"));
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            TransactionState state = TransactionState.CANCEL;
            transactionManager.rejectOrCancel(transaction, state);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.UPDATE, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.UPDATE, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        }*/

        return SUCCESS;
    }

    /*freeze Wallet From Guide*/

    public String chargeWalletAmountFromGuide() {

        /*User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_URL_PARTITION);
        Wallet fromWallet = (Wallet) session.get(ConstantGeneral.SESSION_WALLET);
        responseDto.cleanMessages();

        if (fromWallet == null) {
            writeLog(null, null, null, LogLevel.ERROR, LogAction.READ, getText("errors.internal.server.timeout"));
            logger.error(getText("errors.internal.server.timeout"));
            responseDto.addMessage(getText("errors.internal.server.timeout"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
            return SUCCESS;
        }
        boolean valid = validateAmountAndCurrency();
        if (!valid) {
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        Double productAmount = Double.parseDouble(amount);
        int selectedCurrencyTypeId = Integer.parseInt(currencyType);
        CurrencyType productCurrencyType = CurrencyType.valueOf(selectedCurrencyTypeId);
        WalletSetup walletSetup = partition.getWalletSetup();

        try {

            Transaction transaction = createTransaction(productAmount, productCurrencyType, fromWallet, null, walletSetup, TransactionState.SEND_MONEY);
            transactionManager.chargeWalletImmediately(transaction);
            responseDto.setStatus(ResponseStatus.SUCCESS);

        } catch (InternalErrorException e) {
            writeLog(sesUser, fromWallet, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (PermissionDeniedException e) {
            writeLog(sesUser, fromWallet, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }*/

        return SUCCESS;
    }

    private synchronized boolean validateParams() {

        boolean valid = true;

        if (Utils.isEmpty(amount)) {
            valid = false;
            String msg = getText("wallet.back.end.message.empty.amount");
            responseDto.addMessage(msg);
        }

        if (Utils.isEmpty(userId)) {
            valid = false;
            String msg = getText("wallet.back.end.message.empty.userId");
            responseDto.addMessage(msg);
        }

        if (Utils.isEmpty(currencyType)) {
            valid = false;
            String msg = getText("wallet.back.end.message.empty.currencyType");
            responseDto.addMessage(msg);
        }

        if (!valid) {
            return valid;
        }

        try {
            Double.parseDouble(amount);
        } catch (Exception e) {
            String msg = getText("wallet.back.end.message.empty.currencyType")+" ,"+getText("wallet.payment.label.Amount") +"="+ amount;
            amount = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        }

        try {
            Long.parseLong(userId);
        } catch (Exception e) {
            String msg = getText("wallet.back.end.message.empty.incorrect.userId")+" ,"+getText("wallet.back.end.message.userId") +"="+ userId;
            userId = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        }

        try {
            int type = Integer.parseInt(currencyType);
            CurrencyType.valueOf(type);
        } catch (Exception e) {
            String msg = getText("wallet.back.end.message.empty.incorrect.currencyType")+" ,"+getText("wallet.back.end.message.currencyType") +"="+ currencyType;
            currencyType = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);

        }
        return valid;
    }

    private synchronized boolean validateAmountAndCurrency() {

        boolean valid = true;

        if (Utils.isEmpty(amount)) {
            valid = false;
            String msg = getText("wallet.back.end.message.empty.amount");
            responseDto.addMessage(msg);
        }

        if (Utils.isEmpty(currencyType)) {
            valid = false;
            String msg = getText("wallet.back.end.message.empty.currencyType");
            responseDto.addMessage(msg);
        }

        if (!valid) {
            return valid;
        }

        try {
            Double.parseDouble(amount);
        } catch (Exception e) {
            String msg = getText("wallet.back.end.message.empty.currencyType")+" ,"+getText("wallet.payment.label.Amount") +"="+ amount;
            amount = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        }
        try {
            int type = Integer.parseInt(currencyType);
            CurrencyType.valueOf(type);
        } catch (Exception e) {
            String msg = getText("wallet.back.end.message.empty.incorrect.currencyType")+" ,"+getText("wallet.back.end.message.currencyType") +"="+ currencyType;
            currencyType = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);

        }
        return valid;
    }

    private synchronized boolean validateUserId() {

        boolean valid = true;

        if (Utils.isEmpty(userId)) {
            valid = false;
            String msg = getText("wallet.back.end.message.empty.userId");
            responseDto.addMessage(msg);
        }

        try {
            Long.parseLong(userId);
        } catch (Exception e) {
            String msg = getText("wallet.back.end.message.empty.incorrect.transactionId")+" ,"+getText("wallet.back.end.message.userId") +"="+ userId;
            userId = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        }
        return valid;
    }

    private synchronized boolean validatePartitionId() {

        boolean valid = true;

        if (Utils.isEmpty(partitionId)) {
            valid = false;
            String msg = getText("wallet.back.end.message.empty.partitionId");
            responseDto.addMessage(msg);
        }

        try {
            Integer.parseInt(partitionId);
        } catch (Exception e) {
            String msg = getText("wallet.back.end.message.empty.incorrect.partitionId")+" ,"+getText("wallet.back.end.message.userId") +"="+ partitionId;
            partitionId = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        }
        return valid;
    }

    /*private synchronized TransactionPurchaseTicket initPurchase() throws InvalidParameterException{

        if(Utils.isEmpty(itemId)){
            throw new InvalidParameterException("Purchase itemId is empty");
        }
        if(Utils.isEmpty(purchaseType)){
            throw new InvalidParameterException("Purchase purchaseType is empty");
        }
        if(Utils.isEmpty(name)){
            throw new InvalidParameterException("Purchase name is empty");
        }
        if(Utils.isEmpty(description)){
            throw new InvalidParameterException("Purchase description is empty");
        }


        TransactionPurchaseTicket purchaseTicket = new TransactionPurchaseTicket();
        purchaseTicket.setItemId(Long.parseLong(itemId));
        purchaseTicket.setPurchaseType(TransactionPurchaseType.typeOf(purchaseType));
        purchaseTicket.setName(name);
        purchaseTicket.setDescription(description);

        return purchaseTicket;
    }*/

    public ResponseDto getResponseDto() {
        return responseDto;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPartitionId(String partitionId) {
        this.partitionId = partitionId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setPurchaseType(String purchaseType) {
        this.purchaseType = purchaseType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setResponseDto(ResponseDto responseDto) {
        this.responseDto = responseDto;
    }

    public void setWalletSetupManager(IWalletSetupManager walletSetupManager) {
        this.walletSetupManager = walletSetupManager;
    }

    public void setWalletManager(IWalletManager walletManager) {
        this.walletManager = walletManager;
    }

    public void setTransactionManager(ITransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }
}
