package com.connectto.wallet.web.action.wallet.transaction.purchase;

import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.exception.InvalidParameterException;
import com.connectto.general.exception.PermissionDeniedException;
import com.connectto.general.model.WalletSetup;
import com.connectto.general.model.lcp.PartitionLCP;
import com.connectto.general.model.lcp.ResponseStatus;
import com.connectto.general.model.lcp.UserProfile;
import com.connectto.general.util.Utils;
import com.connectto.wallet.creditCard.exception.CreditCardException;
import com.connectto.wallet.dataaccess.service.transaction.purchase.ITransactionPurchaseManager;
import com.connectto.wallet.dataaccess.service.wallet.IWalletManager;
import com.connectto.wallet.dataaccess.service.wallet.IWalletSetupManager;
import com.connectto.wallet.encryption.EncryptException;
import com.connectto.wallet.model.general.lcp.LogAction;
import com.connectto.wallet.model.general.lcp.LogLevel;
import com.connectto.wallet.model.transaction.purchase.*;
import com.connectto.wallet.model.wallet.ExchangeRate;
import com.connectto.wallet.model.wallet.Wallet;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionPurchaseType;
import com.connectto.wallet.model.wallet.lcp.TransactionState;
import com.connectto.wallet.model.wallet.lcp.TransactionTaxType;
import com.connectto.wallet.web.action.wallet.transaction.util.TransactionBaseAction;
import com.connectto.wallet.web.dto.DataConverter;
import com.connectto.wallet.web.dto.WalletDto;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.Map;

/**
 * Created by Serozh on 11/17/2016.
 */
public class TransactionPurchaseAction extends TransactionBaseAction {

    private static final Logger logger = Logger.getLogger(TransactionPurchaseAction.class.getSimpleName());

    private ITransactionPurchaseManager transactionPurchaseManager;
    private IWalletManager walletManager;
    private IWalletSetupManager walletSetupManager;

    private String userId;
    private String partitionId;
    private String orderCode;
    //PurchaseTicket
    private String itemId;
    private String purchaseType;
    private String name;
    private String description;
    //
    private WalletDto walletDto;

    @Deprecated
    public String freezeDriverBalanceForFuturePayment() {

        responseDto.cleanMessages();
        if (Utils.isEmpty(sessionId)) {
            String msg = getText("wallet.back.end.message.empty.sessionId");
            responseDto.addMessage(msg);
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        try {

            Integer pId = Utils.safeStringToInt(partitionId);

            if(!PartitionLCP.isVshoo(pId)){
                String msg = getText("wallet.back.end.message.unsupported.partition");
                responseDto.addMessage(msg);
                responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
                return SUCCESS;
            }

            Long uId = Utils.safeStringToLong(userId);

            WalletSetup toWalletSetup = walletSetupManager.getByPartitionId(pId);
            Wallet myWallet = walletManager.getByUserId(uId,sessionId);

            if(myWallet.getUser().getTsmProfile().getKey() != UserProfile.DRIVER.getKey()){
                String msg = getText("wallet.back.end.message.unsupported.driver");
                responseDto.addMessage(msg);
                responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
                return SUCCESS;
            }

            if(!PartitionLCP.isVshoo(myWallet.getUser().getPartitionId())){
                String msg = getText("wallet.back.end.message.unsupported.user");
                responseDto.addMessage(msg);
                responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
                return SUCCESS;
            }

            if(!convertAmountAndCurrency(true)){
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            PurchaseTicket ticket = initPurchase();
            TransactionPurchase transactionPurchase = createTransaction(productAmount, productCurrencyType, myWallet, toWalletSetup, TransactionState.FUTURE_PAYMENT);
            transactionPurchase.setSessionId(sessionId);
            transactionPurchase.addTicket(ticket);

            Double totalPrice = transactionPurchase.getWalletTotalPrice();
            Double availableAmount = myWallet.getMoney() - myWallet.getFrozenAmount();
            if (availableAmount < totalPrice) {
                String msg = getText("wallet.back.end.message.less.money");
                responseDto.addMessage(msg);
                responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
            }

            transactionPurchaseManager.freezeDriverBalance(transactionPurchase);
            responseDto.setStatus(ResponseStatus.SUCCESS);

        } catch (InternalErrorException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        } catch (PermissionDeniedException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
        } catch (InvalidParameterException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
        }

        return SUCCESS;
    }

    public String directCharge() {

        responseDto.cleanMessages();
        if (Utils.isEmpty(sessionId)) {
            String msg = getText("wallet.back.end.message.empty.sessionId");
            responseDto.addMessage(msg);
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        try {

            Integer pId = Utils.safeStringToInt(partitionId);

            Long uId = Utils.safeStringToLong(userId);

            WalletSetup toWalletSetup = walletSetupManager.getByPartitionId(pId);
            Wallet myWallet = walletManager.getByUserId(uId,sessionId);

            if(!convertAmountAndCurrency(true)){
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            PurchaseTicket ticket = initPurchase();
            TransactionPurchase transactionPurchase = createTransaction(productAmount, productCurrencyType, myWallet, toWalletSetup, TransactionState.FUTURE_PAYMENT);
            transactionPurchase.setSessionId(sessionId);
            transactionPurchase.addTicket(ticket);

            Double totalPrice = transactionPurchase.getWalletTotalPrice();
            Double availableAmount = myWallet.getMoney() - myWallet.getFrozenAmount();
            if (availableAmount < totalPrice) {
                String msg = getText("wallet.back.end.message.less.money");
                responseDto.addMessage(msg);
                responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
            }

            transactionPurchaseManager.directChargeFromWallet(transactionPurchase);
            responseDto.setStatus(ResponseStatus.SUCCESS);

        } catch (InternalErrorException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        } catch (PermissionDeniedException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
        } catch (InvalidParameterException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
        } catch (CreditCardException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }

    public String freezeWallet() {

        responseDto.cleanMessages();
        if (Utils.isEmpty(sessionId)) {
            String msg = getText("wallet.back.end.message.empty.sessionId");
            responseDto.addMessage(msg);
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        try {

            Integer pId = Utils.safeStringToInt(partitionId);
            if(!PartitionLCP.isVshoo(pId)){
                String msg = getText("wallet.back.end.message.unsupported.partition");
                responseDto.addMessage(msg);
                responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
                return SUCCESS;
            }
            Long uId = Utils.safeStringToLong(userId);

            WalletSetup toWalletSetup = walletSetupManager.getByPartitionId(pId);
            Wallet myWallet = walletManager.getByUserId(uId,sessionId);

            if(!PartitionLCP.isVshoo(myWallet.getUser().getPartitionId())){
                String msg = getText("wallet.back.end.message.unsupported.user");
                responseDto.addMessage(msg);
                responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
                return SUCCESS;
            }

            if(!convertAmountAndCurrency(true)){
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            PurchaseTicket ticket = initPurchase();
            TransactionPurchase transactionPurchase = createTransaction(productAmount, productCurrencyType, myWallet, toWalletSetup, TransactionState.PURCHASE_FREEZE);
            transactionPurchase.setSessionId(sessionId);
            transactionPurchase.addTicket(ticket);

            Double totalPrice = transactionPurchase.getWalletTotalPrice();
            Double availableAmount = myWallet.getMoney() - myWallet.getFrozenAmount();
            if (availableAmount < totalPrice) {
                String msg = getText("wallet.back.end.message.less.money");
                responseDto.addMessage(msg);
                responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
            }

            transactionPurchaseManager.freeze(transactionPurchase);
            responseDto.setStatus(ResponseStatus.SUCCESS);

        } catch (InternalErrorException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        } catch (PermissionDeniedException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
        } catch (InvalidParameterException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
        }

        return SUCCESS;
    }

    public String freezeWalletCancel() {

        responseDto.cleanMessages();
        if (Utils.isEmpty(sessionId) || Utils.isEmpty(orderCode)  ) {
            String msg = getText("wallet.back.end.message.empty.sessionId");
            responseDto.addMessage(msg);
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        try {

            Integer pId = Utils.safeStringToInt(partitionId);
            if(!PartitionLCP.isVshoo(pId)){
                String msg = getText("wallet.back.end.message.unsupported.partition");
                responseDto.addMessage(msg);
                responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
                return SUCCESS;
            }
            Long uId = Utils.safeStringToLong(userId);

            WalletSetup toWalletSetup = walletSetupManager.getByPartitionId(pId);
            Wallet myWallet = walletManager.getByUserId(uId,sessionId);

            if(!PartitionLCP.isVshoo(myWallet.getUser().getPartitionId())){
                String msg = getText("wallet.back.end.message.unsupported.user");
                responseDto.addMessage(msg);
                responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
                return SUCCESS;
            }

            if(!convertAmountAndCurrency(true)){
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            TransactionPurchase transactionPurchase = createTransaction(productAmount, productCurrencyType, myWallet, toWalletSetup, TransactionState.PURCHASE_CANCEL);
            transactionPurchase.setSessionId(sessionId);
            transactionPurchase.setOrderCode(orderCode);
            TransactionPurchaseProcess process = transactionPurchase.getProcessStart();
            transactionPurchase.setProcessEnd(process);
            transactionPurchase.setProcessStart(null);
            transactionPurchase.setClosedAt(transactionPurchase.getOpenedAt());

            transactionPurchaseManager.cancelFreeze(transactionPurchase);
            responseDto.setStatus(ResponseStatus.SUCCESS);

        } catch (InternalErrorException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        } catch (PermissionDeniedException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
        } catch (InvalidParameterException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
        }

        return SUCCESS;
    }

    public String freezeWalletCharge() {

        responseDto.cleanMessages();
        if (Utils.isEmpty(sessionId) || Utils.isEmpty(orderCode)  ) {
            String msg = getText("wallet.back.end.message.empty.sessionId");
            responseDto.addMessage(msg);
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        try {

            Integer pId = Utils.safeStringToInt(partitionId);
            if(!PartitionLCP.isVshoo(pId)){
                String msg = getText("wallet.back.end.message.unsupported.partition");
                responseDto.addMessage(msg);
                responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
                return SUCCESS;
            }
            Long uId = Utils.safeStringToLong(userId);

            WalletSetup toWalletSetup = walletSetupManager.getByPartitionId(pId);
            Wallet myWallet = walletManager.getByUserId(uId,sessionId);

            if(!PartitionLCP.isVshoo(myWallet.getUser().getPartitionId())){
                String msg = getText("wallet.back.end.message.unsupported.user");
                responseDto.addMessage(msg);
                responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
                return SUCCESS;
            }

            if(!convertAmountAndCurrency(true)){
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            TransactionPurchase transactionPurchase = createTransaction(productAmount, productCurrencyType, myWallet, toWalletSetup, TransactionState.PURCHASE_CHARGE);
            transactionPurchase.setSessionId(sessionId);
            transactionPurchase.setOrderCode(orderCode);
            TransactionPurchaseProcess process = transactionPurchase.getProcessStart();
            transactionPurchase.setProcessEnd(process);
            transactionPurchase.setProcessStart(null);
            transactionPurchase.setClosedAt(transactionPurchase.getOpenedAt());

            transactionPurchaseManager.approveFreeze(transactionPurchase);
            responseDto.setStatus(ResponseStatus.SUCCESS);

        } catch (InternalErrorException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        } catch (PermissionDeniedException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
        } catch (InvalidParameterException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
        } catch (CreditCardException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }

    public String checkWallet() {

        responseDto.cleanMessages();
        if (Utils.isEmpty(sessionId)   ) {
            String msg = getText("wallet.back.end.message.empty.sessionId");
            responseDto.addMessage(msg);
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
            return SUCCESS;
        }

        try {

            Long uId = Utils.safeStringToLong(userId);
            Wallet myWallet = walletManager.getByUserId(uId,sessionId);

            if(!PartitionLCP.isVshoo(myWallet.getUser().getPartitionId())){
                String msg = getText("wallet.back.end.message.unsupported.user");
                responseDto.addMessage(msg);
                responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
                return SUCCESS;
            }

             walletDto = DataConverter.convertWallet(myWallet,true);

        } catch (InternalErrorException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.RESOURCE_NOT_FOUND);
        }  catch (InvalidParameterException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
        } catch (EncryptException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.READ, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }



    private synchronized TransactionPurchase createTransaction(Double purchaseAmount, CurrencyType purchaseCurrencyType, Wallet wallet, WalletSetup walletSetup, TransactionState transactionState) throws PermissionDeniedException, InvalidParameterException, InternalErrorException {

        String msgUnsupported = getText("wallet.back.end.message.unsupported.currency") + "\t";

        //<editor-fold desc="initBlock">
        Date currentDate = new Date(System.currentTimeMillis());

        boolean isPurchaseCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(purchaseCurrencyType.getId());
        if (!isPurchaseCurrencyTypeSupported) {
            throw new PermissionDeniedException(msgUnsupported + purchaseCurrencyType.getName());
        }

        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();
        CurrencyType walletCurrencyType = wallet.getCurrencyType();

        int setupCurrencyTypeId = setupCurrencyType.getId();
        int walletCurrencyTypeId = walletCurrencyType.getId();
        int purchaseCurrencyTypeId = purchaseCurrencyType.getId();

        TransactionPurchase transactionPurchase = new TransactionPurchase();
        transactionPurchase.setPurchaseAmount(purchaseAmount);
        transactionPurchase.setPurchaseCurrencyType(purchaseCurrencyType);
        transactionPurchase.setSetupTotalAmountCurrencyType(setupCurrencyType);
        transactionPurchase.setOpenedAt(currentDate);
        transactionPurchase.setSessionId(sessionId);
        transactionPurchase.setSetupId(walletSetup.getId());
        transactionPurchase.setPartitionId(walletSetup.getPartitionId());
        transactionPurchase.setWalletId(wallet.getId());
        transactionPurchase.setFinalState(transactionState);

        boolean isWalletCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(walletCurrencyTypeId);
        if (!isWalletCurrencyTypeSupported) {
            throw new PermissionDeniedException(msgUnsupported + walletCurrencyType.getName());
        }

        //</editor-fold>

        if (purchaseCurrencyTypeId == setupCurrencyTypeId) {
            if (purchaseCurrencyTypeId == walletCurrencyTypeId) {
                simpleTransactionPurchaseWithSameCurrencies(transactionPurchase, wallet, walletSetup, purchaseAmount, currentDate, transactionState);
            } else {
                otherWalletCurrency(transactionPurchase, wallet, walletSetup, purchaseAmount, currentDate, transactionState);
            }
        } else {
            //<editor-fold desc="elseBlock">

            if (setupCurrencyTypeId == walletCurrencyTypeId) {
                otherPurchaseCurrency(transactionPurchase, wallet, walletSetup, purchaseAmount, purchaseCurrencyType, currentDate, transactionState);
            } else if (purchaseCurrencyTypeId == walletCurrencyTypeId) {
                otherSetupCurrency(transactionPurchase, wallet, walletSetup, purchaseAmount, currentDate, transactionState);
            } else {
                throw new PermissionDeniedException(msgUnsupported + purchaseCurrencyType);
            }
            //</editor-fold>
        }
        return transactionPurchase;
    }


    private synchronized void simpleTransactionPurchaseWithSameCurrencies(TransactionPurchase transactionPurchase,
                                                                          Wallet wallet,
                                                                          WalletSetup walletSetup,
                                                                          Double purchaseAmount,
                                                                          Date currentDate,
                                                                          TransactionState transactionState) throws InternalErrorException {

        Long walletId = wallet.getId();
        Long setupId = walletSetup.getId();
        CurrencyType currencyType = walletSetup.getCurrencyType();

        Map<String, Object> purchaseTaxTypeMap = calculateTransferTax(walletSetup, purchaseAmount);
        TransactionTaxType taxType = (TransactionTaxType) purchaseTaxTypeMap.get(TAX_TYPE_KEY);
        Double taxAmount = (Double) purchaseTaxTypeMap.get(TAX_KEY);

        Double totalAmount = purchaseAmount + taxAmount;

        TransactionPurchaseProcessTax processTax = new TransactionPurchaseProcessTax(currentDate, walletId, setupId, taxAmount, currencyType, taxType);
        TransactionPurchaseTax purchaseTax = new TransactionPurchaseTax(currentDate, walletId, setupId, processTax);
        TransactionPurchaseProcess purchaseProcess = new TransactionPurchaseProcess(transactionState, currentDate, walletId, setupId, purchaseAmount, currencyType, processTax);

        transactionPurchase.setProcessStart(purchaseProcess);
        transactionPurchase.setWalletTotalPrice(totalAmount);
        transactionPurchase.setWalletTotalPriceCurrencyType(currencyType);

        transactionPurchase.setSetupTotalAmount(totalAmount);
        transactionPurchase.setSetupTotalAmountCurrencyType(currencyType);
        transactionPurchase.setTax(purchaseTax);
    }

    private synchronized void otherWalletCurrency(TransactionPurchase transactionPurchase,
                                                  Wallet wallet,
                                                  WalletSetup walletSetup,
                                                  Double purchaseAmount,
                                                  Date currentDate,
                                                  TransactionState transactionState) throws InternalErrorException {

        ExchangeRate selectedExchangeRate = getDefaultExchangeRate(wallet.getCurrencyType(), walletSetup.getCurrencyType());
        Double rateAmount = selectedExchangeRate.getBuy();

        Long walletId = wallet.getId();
        Long setupId = walletSetup.getId();
        Long rateId = selectedExchangeRate.getId();

        CurrencyType walletCurrencyType = wallet.getCurrencyType();
        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();

        Double purchasePrice = purchaseAmount * rateAmount;//480.000AMD
        Map<String, Object> purchaseProcessTaxMap = calculateTransferTax(walletSetup, purchaseAmount);//1000 USD
        TransactionTaxType purchaseProcessTaxType = (TransactionTaxType) purchaseProcessTaxMap.get(TAX_TYPE_KEY);
        Double purchaseProcessTax = (Double) purchaseProcessTaxMap.get(TAX_KEY);//100 USD
        Double purchaseProcessTaxPrice = purchaseProcessTax * rateAmount;
        TransactionPurchaseExchangeTax purchaseProcessExchangeTax = new TransactionPurchaseExchangeTax(currentDate, walletId, setupId, purchaseProcessTax, setupCurrencyType, purchaseProcessTaxPrice, walletCurrencyType, purchaseProcessTaxType);
        TransactionPurchaseExchange purchaseProcessExchange = new TransactionPurchaseExchange(walletId, setupId, rateId, currentDate, purchaseProcessTax, setupCurrencyType, rateAmount, walletCurrencyType, purchaseProcessTaxPrice, walletCurrencyType, purchaseProcessExchangeTax);

        Map<String, Object> processTaxExchangeTaxMap = calculateTransferExchangeTax(walletSetup, purchaseProcessTax);//100USD
        TransactionTaxType processTaxExchangeTaxType = (TransactionTaxType) processTaxExchangeTaxMap.get(TAX_TYPE_KEY);
        Double processTaxExchangeTaxTax = (Double) processTaxExchangeTaxMap.get(TAX_KEY);//10 USD
        Double processTaxExchangeTaxPrice = processTaxExchangeTaxTax * rateAmount;//4800AMD
        TransactionPurchaseExchangeTax processTaxExchangeTax = new TransactionPurchaseExchangeTax(currentDate, walletId, setupId, processTaxExchangeTaxTax, setupCurrencyType, processTaxExchangeTaxPrice, walletCurrencyType, processTaxExchangeTaxType);
        TransactionPurchaseExchange processTaxExchangeTaxExchange = new TransactionPurchaseExchange(walletId, setupId, rateId, currentDate, processTaxExchangeTaxTax, setupCurrencyType, rateAmount, walletCurrencyType, processTaxExchangeTaxPrice, walletCurrencyType, processTaxExchangeTax);

        Map<String, Object> exchangePurchaseMap = calculateTransferExchangeTax(walletSetup, purchaseAmount);//1000 USD
        TransactionTaxType exchangePurchaseType = (TransactionTaxType) exchangePurchaseMap.get(TAX_TYPE_KEY);
        Double exchangePurchase = (Double) exchangePurchaseMap.get(TAX_KEY);//100 USD
        Double exchangePurchasePrice = exchangePurchase * rateAmount;//48.000 AMD
        TransactionPurchaseExchangeTax purchaseExchangeTax = new TransactionPurchaseExchangeTax(currentDate, walletId, setupId, exchangePurchase, setupCurrencyType, exchangePurchasePrice, walletCurrencyType, exchangePurchaseType);

        Double totalTaxAmount = purchaseProcessTax + exchangePurchase + processTaxExchangeTaxTax;
        Double totalTaxPrice = purchaseProcessTaxPrice + exchangePurchasePrice + processTaxExchangeTaxPrice;

        Double purchaseTotalAmount = purchaseAmount + totalTaxAmount;
        Double purchaseTotalPrice = purchasePrice + totalTaxPrice;

        TransactionPurchaseProcessTax processTax = new TransactionPurchaseProcessTax(currentDate, walletId, setupId, purchaseProcessTax, setupCurrencyType, purchaseProcessTaxPrice, walletCurrencyType, purchaseProcessTaxType, processTaxExchangeTaxExchange);
        TransactionPurchaseTax purchaseTax = new TransactionPurchaseTax(currentDate, walletId, setupId, processTax, processTaxExchangeTax, purchaseExchangeTax);

        TransactionPurchaseProcess purchaseProcess = new TransactionPurchaseProcess(transactionState, currentDate, walletId, setupId,
                purchaseAmount, setupCurrencyType,
                purchasePrice, purchaseTotalPrice, walletCurrencyType,
                purchaseAmount, purchaseTotalAmount, setupCurrencyType,
                processTax,
                purchaseProcessExchange);

        transactionPurchase.setProcessStart(purchaseProcess);
        transactionPurchase.setWalletTotalPrice(purchaseTotalPrice);
        transactionPurchase.setWalletTotalPriceCurrencyType(walletCurrencyType);

        transactionPurchase.setSetupTotalAmount(purchaseTotalAmount);
        transactionPurchase.setSetupTotalAmountCurrencyType(setupCurrencyType);
        transactionPurchase.setTax(purchaseTax);
    }

    private synchronized void otherPurchaseCurrency(TransactionPurchase transactionPurchase,
                                                    Wallet wallet,//USD
                                                    WalletSetup walletSetup,//USD
                                                    Double purchaseAmount, CurrencyType purchaseCurrencyType,//480.000 AMD
                                                    Date currentDate,
                                                    TransactionState transactionState) throws InternalErrorException {

        ExchangeRate selectedExchangeRate = getDefaultExchangeRate(purchaseCurrencyType, wallet.getCurrencyType());
        Double rateAmount = selectedExchangeRate.getBuy();//480

        Long walletId = wallet.getId();
        Long setupId = walletSetup.getId();
        Long rateId = selectedExchangeRate.getId();

        CurrencyType walletCurrencyType = wallet.getCurrencyType();
        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();

        Double purchasePrice = purchaseAmount / rateAmount;//1.000 USD
        Map<String, Object> purchaseProcessTaxMap = calculateTransferTax(walletSetup, purchasePrice);//1000 USD
        TransactionTaxType purchaseProcessTaxType = (TransactionTaxType) purchaseProcessTaxMap.get(TAX_TYPE_KEY);
        Double purchaseProcessTax = (Double) purchaseProcessTaxMap.get(TAX_KEY);//100 USD
        TransactionPurchaseExchangeTax purchaseProcessExchangeTax = new TransactionPurchaseExchangeTax(currentDate, walletId, setupId, purchaseProcessTax, setupCurrencyType, purchaseProcessTax, walletCurrencyType, purchaseProcessTaxType);
        TransactionPurchaseExchange purchaseProcessExchange = new TransactionPurchaseExchange(walletId, setupId, rateId, currentDate, purchasePrice, setupCurrencyType, rateAmount, walletCurrencyType, purchaseAmount, purchaseCurrencyType, purchasePrice, walletCurrencyType, purchaseProcessExchangeTax);

        Map<String, Object> exchangePurchaseMap = calculateTransferExchangeTax(walletSetup, purchasePrice);//1000 USD
        TransactionTaxType exchangePurchaseType = (TransactionTaxType) exchangePurchaseMap.get(TAX_TYPE_KEY);
        Double exchangePurchase = (Double) exchangePurchaseMap.get(TAX_KEY);//100 USD
        TransactionPurchaseExchangeTax purchaseExchangeTax = new TransactionPurchaseExchangeTax(currentDate, walletId, setupId, exchangePurchase, setupCurrencyType, exchangePurchase, walletCurrencyType, exchangePurchaseType);

        Double totalTaxAmount = purchaseProcessTax + exchangePurchase;
        Double purchaseTotalAmount = purchasePrice + totalTaxAmount;

        TransactionPurchaseProcessTax processTax = new TransactionPurchaseProcessTax(currentDate, walletId, setupId, purchaseProcessTax, setupCurrencyType, purchaseProcessTax, walletCurrencyType, purchaseProcessTaxType, null);
        TransactionPurchaseTax purchaseTax = new TransactionPurchaseTax(currentDate, walletId, setupId, processTax, purchaseExchangeTax);
        TransactionPurchaseProcess purchaseProcess = new TransactionPurchaseProcess(transactionState, currentDate, walletId, setupId,
                purchaseAmount, purchaseCurrencyType,
                purchasePrice, purchaseTotalAmount, walletCurrencyType,
                purchasePrice, purchaseTotalAmount, setupCurrencyType,
                processTax,
                purchaseProcessExchange);

        transactionPurchase.setProcessStart(purchaseProcess);
        transactionPurchase.setWalletTotalPrice(purchaseTotalAmount);
        transactionPurchase.setWalletTotalPriceCurrencyType(walletCurrencyType);

        transactionPurchase.setSetupTotalAmount(purchaseTotalAmount);
        transactionPurchase.setSetupTotalAmountCurrencyType(setupCurrencyType);
        transactionPurchase.setTax(purchaseTax);
    }

    private synchronized void otherSetupCurrency(TransactionPurchase transactionPurchase,
                                                 Wallet wallet,//AMD
                                                 WalletSetup walletSetup,//USD
                                                 Double purchaseAmount,//AMD
                                                 Date currentDate,
                                                 TransactionState transactionState) throws InternalErrorException {

        ExchangeRate selectedExchangeRate = getDefaultExchangeRate(wallet.getCurrencyType(), walletSetup.getCurrencyType());
        Double rateAmount = selectedExchangeRate.getBuy();

        Long walletId = wallet.getId();
        Long setupId = walletSetup.getId();
        Long rateId = selectedExchangeRate.getId();

        CurrencyType walletCurrencyType = wallet.getCurrencyType();
        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();

        Double setupAmount = purchaseAmount / rateAmount;//1000 USD
        Map<String, Object> purchaseProcessTaxMap = calculateTransferTax(walletSetup, setupAmount);//1000 USD
        TransactionTaxType purchaseProcessTaxType = (TransactionTaxType) purchaseProcessTaxMap.get(TAX_TYPE_KEY);
        Double purchaseProcessTax = (Double) purchaseProcessTaxMap.get(TAX_KEY);//100 USD
        Double purchaseProcessTaxPrice = purchaseProcessTax * rateAmount;
        TransactionPurchaseExchangeTax purchaseProcessExchangeTax = new TransactionPurchaseExchangeTax(currentDate, walletId, setupId, purchaseProcessTax, setupCurrencyType, purchaseProcessTaxPrice, walletCurrencyType, purchaseProcessTaxType);
        TransactionPurchaseExchange purchaseProcessExchange = new TransactionPurchaseExchange(walletId, setupId, rateId, currentDate, setupAmount, setupCurrencyType, rateAmount, walletCurrencyType, purchaseAmount, walletCurrencyType, purchaseProcessExchangeTax);

        Map<String, Object> processTaxExchangeTaxMap = calculateTransferExchangeTax(walletSetup, purchaseProcessTax);//100USD
        TransactionTaxType processTaxExchangeTaxType = (TransactionTaxType) processTaxExchangeTaxMap.get(TAX_TYPE_KEY);
        Double processTaxExchangeTaxTax = (Double) processTaxExchangeTaxMap.get(TAX_KEY);//10 USD
        Double processTaxExchangeTaxPrice = processTaxExchangeTaxTax * rateAmount;//4800AMD
        TransactionPurchaseExchangeTax processTaxExchangeTax = new TransactionPurchaseExchangeTax(currentDate, walletId, setupId, processTaxExchangeTaxTax, setupCurrencyType, processTaxExchangeTaxPrice, walletCurrencyType, processTaxExchangeTaxType);
        TransactionPurchaseExchange processTaxExchangeTaxExchange = new TransactionPurchaseExchange(walletId, setupId, rateId, currentDate, processTaxExchangeTaxTax, setupCurrencyType, rateAmount, walletCurrencyType, processTaxExchangeTaxPrice, walletCurrencyType, processTaxExchangeTax);

        Map<String, Object> exchangePurchaseMap = calculateTransferExchangeTax(walletSetup, setupAmount);//1000 USD
        TransactionTaxType exchangePurchaseType = (TransactionTaxType) exchangePurchaseMap.get(TAX_TYPE_KEY);
        Double exchangePurchase = (Double) exchangePurchaseMap.get(TAX_KEY);//100 USD
        Double exchangePurchasePrice = exchangePurchase * rateAmount;//48.000 AMD
        TransactionPurchaseExchangeTax purchaseExchangeTax = new TransactionPurchaseExchangeTax(currentDate, walletId, setupId, exchangePurchase, setupCurrencyType, exchangePurchasePrice, walletCurrencyType, exchangePurchaseType);

        Double totalTaxAmount = purchaseProcessTax + exchangePurchase + processTaxExchangeTaxTax;
        Double totalTaxPrice = purchaseProcessTaxPrice + exchangePurchasePrice + processTaxExchangeTaxPrice;

        Double purchaseTotalAmount = setupAmount + totalTaxAmount;
        Double purchaseTotalPrice = purchaseAmount + totalTaxPrice;

        TransactionPurchaseProcessTax processTax = new TransactionPurchaseProcessTax(currentDate, walletId, setupId, purchaseProcessTax, setupCurrencyType, purchaseProcessTaxPrice, walletCurrencyType, purchaseProcessTaxType, processTaxExchangeTaxExchange);
        TransactionPurchaseTax purchaseTax = new TransactionPurchaseTax(currentDate, walletId, setupId, processTax, processTaxExchangeTax, purchaseExchangeTax);

        TransactionPurchaseProcess purchaseProcess = new TransactionPurchaseProcess(transactionState, currentDate, walletId, setupId,
                purchaseAmount, walletCurrencyType,
                purchaseAmount, purchaseTotalPrice, walletCurrencyType,
                setupAmount, purchaseTotalAmount, setupCurrencyType,
                processTax,
                purchaseProcessExchange);

        transactionPurchase.setProcessStart(purchaseProcess);
        transactionPurchase.setWalletTotalPrice(purchaseTotalPrice);
        transactionPurchase.setWalletTotalPriceCurrencyType(walletCurrencyType);

        transactionPurchase.setSetupTotalAmount(purchaseTotalAmount);
        transactionPurchase.setSetupTotalAmountCurrencyType(setupCurrencyType);
        transactionPurchase.setTax(purchaseTax);
    }

    private synchronized PurchaseTicket initPurchase() throws InvalidParameterException {

        if (Utils.isEmpty(itemId)) {
            throw new InvalidParameterException("Purchase itemId is empty");
        }
        if (Utils.isEmpty(purchaseType)) {
            throw new InvalidParameterException("Purchase purchaseType is empty");
        }
        if (Utils.isEmpty(name)) {
            throw new InvalidParameterException("Purchase name is empty");
        }
        if (Utils.isEmpty(description)) {
            throw new InvalidParameterException("Purchase description is empty");
        }


        PurchaseTicket purchaseTicket = new PurchaseTicket();
        purchaseTicket.setItemId(Long.parseLong(itemId));
        purchaseTicket.setPurchaseType(TransactionPurchaseType.typeOf(purchaseType));
        purchaseTicket.setName(name);
        purchaseTicket.setDescription(description);

        return purchaseTicket;
    }

    /*
     * #################################################################################################################
     * ########################################        GETTER & SETTER       ###########################################
     * #################################################################################################################
     */

    public WalletDto getWalletDto() {
        return walletDto;
    }

    public void setTransactionPurchaseManager(ITransactionPurchaseManager transactionPurchaseManager) {
        this.transactionPurchaseManager = transactionPurchaseManager;
    }

    public void setWalletManager(IWalletManager walletManager) {
        this.walletManager = walletManager;
    }

    public void setWalletSetupManager(IWalletSetupManager walletSetupManager) {
        this.walletSetupManager = walletSetupManager;
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
}
