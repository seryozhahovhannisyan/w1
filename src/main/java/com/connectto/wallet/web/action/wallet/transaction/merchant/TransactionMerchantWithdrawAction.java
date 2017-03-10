package com.connectto.wallet.web.action.wallet.transaction.merchant;

import com.connectto.general.exception.*;
import com.connectto.general.model.Partition;
import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.WalletSetup;
import com.connectto.general.model.lcp.ResponseStatus;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.Utils;
import com.connectto.wallet.dataaccess.service.transaction.merchant.ITransactionWithdrawManager;
import com.connectto.wallet.dataaccess.service.wallet.IWalletManager;
import com.connectto.wallet.dataaccess.service.wallet.IWalletSetupManager;
import com.connectto.wallet.encryption.EncryptException;
import com.connectto.wallet.model.general.lcp.LogAction;
import com.connectto.wallet.model.general.lcp.LogLevel;
import com.connectto.wallet.model.transaction.merchant.MerchantTransactionReviewDto;
import com.connectto.wallet.model.transaction.merchant.withdraw.*;
import com.connectto.wallet.model.wallet.ExchangeRate;
import com.connectto.wallet.model.wallet.Wallet;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionState;
import com.connectto.wallet.model.wallet.lcp.TransactionTaxType;
import com.connectto.wallet.web.action.wallet.transaction.util.TransactionMerchantAction;
import com.connectto.wallet.web.dto.DataConverter;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Serozh on 11/17/2016.
 */
public class TransactionMerchantWithdrawAction extends TransactionMerchantAction {

    private static final Logger logger = Logger.getLogger(TransactionMerchantWithdrawAction.class.getSimpleName());
    private boolean isEncoded = true;

    private IWalletManager walletManager;
    private IWalletSetupManager walletSetupManager;
    private ITransactionWithdrawManager transactionWithdrawManager;

    private String withdrawId;

    private String userId;
    private String orderCode;
    //WithdrawTicket
    private String itemId;
    private String name;
    private String description;

    private MerchantTransactionReviewDto transactionReviewDto;

    /**
     * Calls from Merchant Application
     *
     * @return JSON
     */
    public String previewWithdraw() {

        responseDto.cleanMessages();

        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_PARTITION);

        try {

            Long uId = Utils.safeStringToLong(userId);

            WalletSetup toWalletSetup = walletSetupManager.getByPartitionId(partition.getId());
            Wallet myWallet = walletManager.getByUserId(uId);

            if (!convertAmountAndCurrency(true)) {
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            if (!convertMerchantTaxAndTaxCurrency(true)) {
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            TransactionWithdraw transactionWithdraw = createTransaction(productAmount, productCurrencyType, myWallet, toWalletSetup, TransactionState.PENDING);
            transactionReviewDto = DataConverter.convertToMerchantTransactionReviewDto(transactionWithdraw, true);

            Double totalPrice = transactionWithdraw.getWalletTotalPrice();
            Double availableAmount = myWallet.getMoney() - myWallet.getFrozenAmount();
            if (availableAmount < totalPrice) {
                String msg = getText("wallet.back.end.message.less.money");
                responseDto.addMessage(msg);
                responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
            }
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        } catch (PermissionDeniedException e) {
            e.printStackTrace();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (EncryptException e) {
            e.printStackTrace();
        }

        return SUCCESS;
    }

    /**
     * Calls from Merchant Application
     *
     * @return JSON
     */
    public String startWithdraw() {

        responseDto.cleanMessages();


        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_PARTITION);

        try {

            Long uId = Utils.safeStringToLong(userId);

            WalletSetup toWalletSetup = walletSetupManager.getByPartitionId(partition.getId());
            Wallet myWallet = walletManager.getByUserId(uId);

            if (!convertAmountAndCurrency(isEncoded)) {
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            if (!convertMerchantTaxAndTaxCurrency(isEncoded)) {
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            TransactionWithdraw transactionWithdraw = createTransaction(productAmount, productCurrencyType, myWallet, toWalletSetup, TransactionState.PENDING);
            transactionWithdrawManager.start(transactionWithdraw);
            transactionReviewDto = DataConverter.convertToMerchantTransactionReviewDto(transactionWithdraw, isEncoded);

            Double totalPrice = transactionWithdraw.getWalletTotalPrice();
            Double availableAmount = myWallet.getMoney() - myWallet.getFrozenAmount();
            if (availableAmount < totalPrice) {
                String msg = getText("wallet.back.end.message.less.money");
                responseDto.addMessage(msg);
                responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
            }
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        } catch (PermissionDeniedException e) {
            e.printStackTrace();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }catch (EncryptException e) {
            e.printStackTrace();
        }

        return SUCCESS;
    }

    /**
     * Calls from special pending transaction page
     *
     * @return Redirect home page open current balance popup
     */
    public String merchantWithdrawTimeOut() {

        responseDto.cleanMessages();

        Partition partition = (Partition) session.get(ConstantGeneral.SESSION_PARTITION);

        try {

            Long uId = Utils.safeStringToLong(userId);

            WalletSetup toWalletSetup = walletSetupManager.getByPartitionId(partition.getId());
            Wallet myWallet = walletManager.getByUserId(uId);

            if (!convertAmountAndCurrency(true)) {
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            if (!convertMerchantTaxAndTaxCurrency(true)) {
                responseDto.setStatus(ResponseStatus.INVALID_PARAMETER);
                return SUCCESS;
            }

            TransactionWithdraw transactionWithdraw = createTransaction(productAmount, productCurrencyType, myWallet, toWalletSetup, TransactionState.PENDING);
            transactionWithdrawManager.start(transactionWithdraw);
            transactionReviewDto = DataConverter.convertToMerchantTransactionReviewDto(transactionWithdraw, true);

            Double totalPrice = transactionWithdraw.getWalletTotalPrice();
            Double availableAmount = myWallet.getMoney() - myWallet.getFrozenAmount();
            if (availableAmount < totalPrice) {
                String msg = getText("wallet.back.end.message.less.money");
                responseDto.addMessage(msg);
                responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
            }
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            e.printStackTrace();
        } catch (PermissionDeniedException e) {
            e.printStackTrace();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }  catch (EncryptException e) {
            e.printStackTrace();
        }

        return SUCCESS;
    }

    /**
     * Calls from special pending transaction page
     *
     * @return Redirect home page open current balance popup
     */
    public String cancelWithdraw() {

        responseDto.cleanMessages();

        try {

            Wallet myWallet = (Wallet)session.get(ConstantGeneral.SESSION_WALLET);
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("finalState", TransactionState.PENDING.getId());
            params.put("walletId", myWallet.getId());
            params.put("myWallet",myWallet);
            params.put("orderCode",orderCode);

            TransactionWithdraw transactionWithdraw = transactionWithdrawManager.cancel(params);
            transactionReviewDto = DataConverter.convertToMerchantTransactionReviewDto(transactionWithdraw, true);

            Double totalPrice = transactionWithdraw.getWalletTotalPrice();
            Double availableAmount = myWallet.getMoney() - myWallet.getFrozenAmount();
            if (availableAmount < totalPrice) {
                String msg = getText("wallet.back.end.message.less.money");
                responseDto.addMessage(msg);
                responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
            }
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (PermissionDeniedException e) {
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EncryptException e) {
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (MerchantApiException e) {
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (HttpConnectionDeniedException e) {
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }


    /**
     * Calls from special pending transaction page
     *
     * @return Redirect home page open current balance popup
     */
    public String approveWithdraw() {

        responseDto.cleanMessages();

        try {

            Wallet myWallet = (Wallet)session.get(ConstantGeneral.SESSION_WALLET);
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("finalState", TransactionState.PENDING.getId());
            params.put("walletId", myWallet.getId());
            params.put("myWallet",myWallet);
            params.put("orderCode",orderCode);

            TransactionWithdraw transactionWithdraw = transactionWithdrawManager.approve(params);
            transactionReviewDto = DataConverter.convertToMerchantTransactionReviewDto(transactionWithdraw, true);

            Double totalPrice = transactionWithdraw.getWalletTotalPrice();
            Double availableAmount = myWallet.getMoney() - myWallet.getFrozenAmount();
            if (availableAmount < totalPrice) {
                String msg = getText("wallet.back.end.message.less.money");
                responseDto.addMessage(msg);
                responseDto.setStatus(ResponseStatus.PERMISSION_DENIED);
            }
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (InternalErrorException e) {
            writeLog(null, null, e, LogLevel.ERROR, LogAction.INSERT, "");
            logger.error(e);
            responseDto.addMessage(getText("errors.internal.server"));
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EntityNotFoundException e) {
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (PermissionDeniedException e) {
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (EncryptException e) {
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (MerchantApiException e) {
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        } catch (HttpConnectionDeniedException e) {
            responseDto.addMessage(e.getMessage());
            responseDto.setStatus(ResponseStatus.INTERNAL_ERROR);
        }

        return SUCCESS;
    }


    private synchronized TransactionWithdraw createTransaction(Double withdrawAmount, CurrencyType withdrawAmountCurrencyType,
                                                               Wallet wallet, WalletSetup walletSetup, TransactionState transactionState) throws PermissionDeniedException, InvalidParameterException, InternalErrorException {

        String msgUnsupported = getText("wallet.back.end.message.unsupported.currency") + "\t";

        //<editor-fold desc="initBlock">
        Date currentDate = new Date(System.currentTimeMillis());


        boolean isWithdrawCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(withdrawAmountCurrencyType.getId());
        if (!isWithdrawCurrencyTypeSupported) {
            throw new PermissionDeniedException(msgUnsupported + withdrawAmountCurrencyType.getName());
        }

        Long walletId = wallet.getId();
        Long walletSetupId = walletSetup.getId();

        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();
        CurrencyType walletCurrencyType = wallet.getCurrencyType();

        int setupCurrencyTypeId = setupCurrencyType.getId();
        int walletCurrencyTypeId = walletCurrencyType.getId();
        int withdrawCurrencyTypeId = withdrawAmountCurrencyType.getId();

        MerchantWithdraw merchantWithdraw = initMerchantWithdraw(currentDate, walletId, walletSetupId);

        TransactionWithdraw transactionWithdraw = new TransactionWithdraw();
        transactionWithdraw.setWithdrawAmount(withdrawAmount);
        transactionWithdraw.setWithdrawAmountCurrencyType(withdrawAmountCurrencyType);
        transactionWithdraw.setWithdrawMerchantTotalTax(merchantWithdraw.getMerchantWithdrawTax().getWithdrawTaxTotal());
        transactionWithdraw.setWithdrawMerchantTotalTaxCurrencyType(withdrawAmountCurrencyType);
        transactionWithdraw.setSetupTotalAmountCurrencyType(setupCurrencyType);
        transactionWithdraw.setOpenedAt(currentDate);
        transactionWithdraw.setSetupId(walletSetupId);
        transactionWithdraw.setWalletId(walletId);
        transactionWithdraw.setFinalState(transactionState);
        transactionWithdraw.setMerchantWithdraw(merchantWithdraw);
        transactionWithdraw.setEncoded(isEncoded);

        boolean isWalletCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(walletCurrencyTypeId);
        if (!isWalletCurrencyTypeSupported) {
            throw new PermissionDeniedException(msgUnsupported + walletCurrencyType.getName());
        }

        //</editor-fold>

        if (withdrawCurrencyTypeId == setupCurrencyTypeId) {
            if (withdrawCurrencyTypeId == walletCurrencyTypeId) {
                simpleTransactionWithdrawWithSameCurrencies(transactionWithdraw, wallet, walletSetup, withdrawAmount, currentDate, transactionState);
            } else {
                otherWalletCurrency(transactionWithdraw, wallet, walletSetup, withdrawAmount, currentDate, transactionState);
            }
        } else {
            throw new PermissionDeniedException(msgUnsupported + withdrawAmountCurrencyType);
            //<editor-fold desc="elseBlock">


            //</editor-fold>
        }
        return transactionWithdraw;
    }


    private synchronized void simpleTransactionWithdrawWithSameCurrencies(TransactionWithdraw transactionWithdraw,
                                                                          Wallet wallet,
                                                                          WalletSetup walletSetup,
                                                                          Double withdrawAmount,
                                                                          Date currentDate,
                                                                          TransactionState transactionState) throws InternalErrorException {

        MerchantWithdraw merchantWithdraw = transactionWithdraw.getMerchantWithdraw();
        MerchantWithdrawTax merchantWithdrawTax = merchantWithdraw.getMerchantWithdrawTax();

        Long walletId = wallet.getId();
        Long setupId = walletSetup.getId();
        CurrencyType currencyType = walletSetup.getCurrencyType();

        Map<String, Object> receiverTaxTypeMap = calculateReceiverTax(walletSetup, withdrawAmount);
        TransactionTaxType receiverTaxType = (TransactionTaxType) receiverTaxTypeMap.get(TAX_TYPE_KEY);
        Double receiverTaxAmount = (Double) receiverTaxTypeMap.get(TAX_KEY);

        Map<String, Object> withdrawTaxTypeMap = calculateWithdrawTax(walletSetup, withdrawAmount);
        TransactionTaxType withdrawTaxType = (TransactionTaxType) withdrawTaxTypeMap.get(TAX_TYPE_KEY);
        Double withdrawTaxAmount = (Double) withdrawTaxTypeMap.get(TAX_KEY);

        Double walletTotalAmount = withdrawAmount + withdrawTaxAmount + receiverTaxAmount;
        Double setupTotalAmount = withdrawTaxAmount + receiverTaxAmount;

        TransactionWithdrawProcessTax processTax = new TransactionWithdrawProcessTax(currentDate, walletId, setupId, receiverTaxAmount, currencyType, receiverTaxType);
        WalletSetupWithdrawTax setupWithdrawTax = new WalletSetupWithdrawTax(currentDate, walletId, setupId, withdrawTaxAmount, currencyType, withdrawTaxType);

        TransactionWithdrawTax withdrawTax = new TransactionWithdrawTax(currentDate, walletId, setupId, processTax, setupWithdrawTax, merchantWithdrawTax);
        TransactionWithdrawProcess withdrawProcess = new TransactionWithdrawProcess(transactionState, currentDate, walletId, setupId, withdrawAmount, currencyType, processTax, setupWithdrawTax);

        transactionWithdraw.setProcessStart(withdrawProcess);
        transactionWithdraw.setWalletTotalPrice(walletTotalAmount);
        transactionWithdraw.setWalletTotalPriceCurrencyType(currencyType);

        transactionWithdraw.setSetupTotalAmount(setupTotalAmount);
        transactionWithdraw.setSetupTotalAmountCurrencyType(currencyType);
        transactionWithdraw.setTax(withdrawTax);
    }

    private synchronized void otherWalletCurrency(TransactionWithdraw transactionWithdraw,
                                                  Wallet wallet,
                                                  WalletSetup walletSetup,
                                                  Double withdrawAmount,
                                                  Date currentDate,
                                                  TransactionState transactionState) throws InternalErrorException {

        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();
        CurrencyType walletCurrencyType = wallet.getCurrencyType();
        ExchangeRate selectedExchangeRate = getDefaultExchangeRate(walletCurrencyType, setupCurrencyType);
        Double rateAmount = selectedExchangeRate.getBuy();

        Long walletId = wallet.getId();
        Long setupId = walletSetup.getId();
        Long rateId = selectedExchangeRate.getId();

        MerchantWithdraw merchantWithdraw = transactionWithdraw.getMerchantWithdraw();
        MerchantWithdrawTax merchantWithdrawTax = merchantWithdraw.getMerchantWithdrawTax();
        merchantWithdrawTax = calculateMerchantWithdrawTaxPrice(walletId, setupId, rateId, currentDate, merchantWithdrawTax, selectedExchangeRate);//1 USD = 480 AMD

        Double withdrawPrice = withdrawAmount * rateAmount;//48000AMD
        TransactionWithdrawExchange withdrawExchange = new TransactionWithdrawExchange(walletId, setupId, rateId, currentDate, withdrawAmount, setupCurrencyType, rateAmount, walletCurrencyType, withdrawPrice, walletCurrencyType);

        Map<String, Object> receiverProcessTaxMap = calculateReceiverTax(walletSetup, withdrawAmount);//100 USD
        TransactionTaxType receiverProcessTaxType = (TransactionTaxType) receiverProcessTaxMap.get(TAX_TYPE_KEY);
        Double receiverProcessTaxAmount = (Double) receiverProcessTaxMap.get(TAX_KEY);//2 USD
        Double receiverProcessTaxPrice = receiverProcessTaxAmount * rateAmount;//960
        TransactionWithdrawExchange receiverProcessExchange = new TransactionWithdrawExchange(walletId, setupId, rateId, currentDate, receiverProcessTaxAmount, setupCurrencyType, rateAmount, walletCurrencyType, receiverProcessTaxPrice, walletCurrencyType);
        TransactionWithdrawProcessTax receiverProcessTax = new TransactionWithdrawProcessTax(currentDate, walletId, setupId, receiverProcessTaxAmount, setupCurrencyType, receiverProcessTaxPrice, walletCurrencyType, receiverProcessTaxType, receiverProcessExchange);


        Map<String, Object> withdrawTaxTypeMap = calculateWithdrawTax(walletSetup, withdrawAmount);//100
        TransactionTaxType withdrawTaxType = (TransactionTaxType) withdrawTaxTypeMap.get(TAX_TYPE_KEY);
        Double withdrawTaxAmount = (Double) withdrawTaxTypeMap.get(TAX_KEY);
        Double withdrawTaxPrice = withdrawTaxAmount * rateAmount;//3 USD
        TransactionWithdrawExchange withdrawTaxExchange = new TransactionWithdrawExchange(walletId, setupId, rateId, currentDate, withdrawTaxAmount, setupCurrencyType, rateAmount, walletCurrencyType, withdrawTaxPrice, walletCurrencyType);
        WalletSetupWithdrawTax setupWithdrawTax = new WalletSetupWithdrawTax(currentDate, walletId, setupId, withdrawTaxAmount, setupCurrencyType, withdrawTaxPrice, walletCurrencyType, withdrawTaxType, withdrawTaxExchange);

        Map<String, Object> exchangeWithdrawTaxMap = calculateReceiverExchangeTax(walletSetup, withdrawAmount);//1000 USD
        TransactionTaxType exchangeWithdrawTaxType = (TransactionTaxType) exchangeWithdrawTaxMap.get(TAX_TYPE_KEY);
        Double exchangeWithdrawTaxAmount = (Double) exchangeWithdrawTaxMap.get(TAX_KEY);//4 USD
        Double exchangeWithdrawTaxPrice = exchangeWithdrawTaxAmount * rateAmount;// AMD
        TransactionWithdrawExchangeTax exchangeWithdrawTax = new TransactionWithdrawExchangeTax(currentDate, walletId, setupId, exchangeWithdrawTaxAmount, setupCurrencyType, exchangeWithdrawTaxPrice, walletCurrencyType, exchangeWithdrawTaxType);
        TransactionWithdrawExchange exchangeWithdraw = new TransactionWithdrawExchange(walletId, setupId, rateId, currentDate, exchangeWithdrawTaxAmount, setupCurrencyType, rateAmount, walletCurrencyType, exchangeWithdrawTaxPrice, walletCurrencyType, exchangeWithdrawTax);

        Double setupTotalProcess = receiverProcessTaxAmount + withdrawTaxAmount;//2+3
        Double setupTotalAmount = setupTotalProcess + exchangeWithdrawTaxAmount;//5+4
        Double totalTaxAmount = merchantWithdrawTax.getWithdrawTaxTotal() + setupTotalAmount;
        Double totalTaxPrice = merchantWithdrawTax.getWithdrawTaxPrice() + receiverProcessTaxPrice + withdrawTaxPrice + exchangeWithdrawTaxPrice;

        Double walletTotalAmount = withdrawAmount + totalTaxAmount;
        Double walletTotalPrice = withdrawPrice + totalTaxPrice;

        TransactionWithdrawTax withdrawTax = new TransactionWithdrawTax(currentDate, walletId, setupId, receiverProcessTax, setupWithdrawTax, merchantWithdrawTax, exchangeWithdrawTax);
        TransactionWithdrawProcess withdrawProcess =
                new TransactionWithdrawProcess(transactionState, currentDate, walletId, setupId,
                        withdrawAmount, setupCurrencyType,
                        withdrawPrice, walletTotalPrice, walletCurrencyType,
                        withdrawAmount, setupTotalProcess, setupCurrencyType,
                        receiverProcessTax, setupWithdrawTax, exchangeWithdraw);

        transactionWithdraw.setProcessStart(withdrawProcess);
        transactionWithdraw.setWalletTotalPrice(walletTotalPrice);
        transactionWithdraw.setWalletTotalPriceCurrencyType(walletCurrencyType);

        transactionWithdraw.setSetupTotalAmount(setupTotalAmount);
        transactionWithdraw.setSetupTotalAmountCurrencyType(setupCurrencyType);
        transactionWithdraw.setTax(withdrawTax);
    }

    private synchronized MerchantWithdraw initMerchantWithdraw(Date actionDate, Long walletId, Long setupId) throws InvalidParameterException, NumberFormatException {

        if (Utils.isEmpty(itemId)) {
            throw new InvalidParameterException("Withdraw itemId is empty");
        }
        if (Utils.isEmpty(name)) {
            throw new InvalidParameterException("Withdraw name is empty");
        }
        if (Utils.isEmpty(description)) {
            throw new InvalidParameterException("Withdraw description is empty");
        }


        MerchantWithdraw merchantWithdraw = new MerchantWithdraw();
        merchantWithdraw.setItemId(Long.parseLong(itemId));
        merchantWithdraw.setName(name);
        merchantWithdraw.setStartAt(actionDate);
        merchantWithdraw.setRationalStopAt(Utils.getAfterSecunds(actionDate, rationalSecondsDuration));

        merchantWithdraw.setDescription(description);
        merchantWithdraw.setMerchantWithdrawTax(initMerchantWithdrawTax(actionDate, walletId, setupId));

        return merchantWithdraw;
    }


    private synchronized MerchantWithdrawTax initMerchantWithdrawTax(Date actionDate, Long walletId, Long setupId) throws InvalidParameterException {
        MerchantWithdrawTax withdrawTax = new MerchantWithdrawTax(actionDate, walletId, setupId, paidTaxToMerchant, paidTaxCurrencyType, paidTaxType);
        return withdrawTax;
    }

    private synchronized MerchantWithdrawTax calculateMerchantWithdrawTaxPrice(Long walletId, Long setupId, Long rateId, Date exchangeDate, MerchantWithdrawTax merchantWithdrawTax, ExchangeRate selectedExchangeRate) {
        Double setupAmount = merchantWithdrawTax.getWithdrawTax();
        CurrencyType setupCurrencyType = merchantWithdrawTax.getWithdrawTaxCurrencyType();
        Double rate = selectedExchangeRate.getBuy();
        CurrencyType rateCurrencyType = selectedExchangeRate.getToCurrency();
        Double walletPaidAmount = setupAmount * rate;

        TransactionWithdrawExchange exchange = new TransactionWithdrawExchange(walletId, setupId, rateId, exchangeDate, setupAmount, setupCurrencyType, rate, rateCurrencyType, walletPaidAmount, rateCurrencyType);

        return new MerchantWithdrawTax(exchangeDate, walletId, setupId, setupAmount, setupCurrencyType, walletPaidAmount, rateCurrencyType, merchantWithdrawTax.getWithdrawTaxType(), exchange);
    }


    /*
     * #################################################################################################################
     * ########################################        GETTER & SETTER       ###########################################
     * #################################################################################################################
     */

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    //############################################################################
    public void setWithdrawId(String withdrawId) {
        this.withdrawId = withdrawId;
    }

    public MerchantTransactionReviewDto getTransactionReviewDto() {
        return transactionReviewDto;
    }

    public ResponseDto getResponseDto() {
        return responseDto;
    }

    public void setWalletManager(IWalletManager walletManager) {
        this.walletManager = walletManager;
    }

    public void setWalletSetupManager(IWalletSetupManager walletSetupManager) {
        this.walletSetupManager = walletSetupManager;
    }

    public void setTransactionWithdrawManager(ITransactionWithdrawManager transactionWithdrawManager) {
        this.transactionWithdrawManager = transactionWithdrawManager;
    }
}
