package com.connectto.wallet.web.action.wallet.transaction.merchant;

import com.connectto.general.exception.*;
import com.connectto.general.model.Partition;
import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.WalletSetup;
import com.connectto.general.model.lcp.ResponseStatus;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.Utils;
import com.connectto.wallet.dataaccess.service.transaction.merchant.ITransactionDepositManager;
import com.connectto.wallet.dataaccess.service.wallet.IWalletManager;
import com.connectto.wallet.dataaccess.service.wallet.IWalletSetupManager;
import com.connectto.wallet.encryption.EncryptException;
import com.connectto.wallet.model.general.lcp.LogAction;
import com.connectto.wallet.model.general.lcp.LogLevel;
import com.connectto.wallet.model.transaction.merchant.MerchantTransactionReviewDto;
import com.connectto.wallet.model.transaction.merchant.deposit.*;
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
public class TransactionMerchantDepositAction extends TransactionMerchantAction {

    private static final Logger logger = Logger.getLogger(TransactionMerchantDepositAction.class.getSimpleName());
    private boolean isEncoded = true;

    private IWalletManager walletManager;
    private IWalletSetupManager walletSetupManager;
    private ITransactionDepositManager transactionDepositManager;

    private String depositId;

    private String userId;
    private String orderCode;
    //DepositTicket
    private String itemId;
    private String name;
    private String description;

    private MerchantTransactionReviewDto transactionReviewDto;

    /**
     * Calls from Merchant Application
     *
     * @return JSON
     */
    public String previewDeposit() {

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

            TransactionDeposit transactionDeposit = createTransaction(productAmount, productCurrencyType, myWallet, toWalletSetup, TransactionState.PENDING);
            transactionReviewDto = DataConverter.convertToMerchantTransactionReviewDto(transactionDeposit, true);

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
    public String startDeposit() {

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

            TransactionDeposit transactionDeposit = createTransaction(productAmount, productCurrencyType, myWallet, toWalletSetup, TransactionState.PENDING);
            transactionDepositManager.start(transactionDeposit);
            transactionReviewDto = DataConverter.convertToMerchantTransactionReviewDto(transactionDeposit, isEncoded);

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
    public String merchantDepositTimeOut() {

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

            TransactionDeposit transactionDeposit = createTransaction(productAmount, productCurrencyType, myWallet, toWalletSetup, TransactionState.PENDING);
            transactionDepositManager.start(transactionDeposit);
            transactionReviewDto = DataConverter.convertToMerchantTransactionReviewDto(transactionDeposit, true);

            Double totalPrice = transactionDeposit.getWalletTotalPrice();
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
    public String cancelDeposit() {

        responseDto.cleanMessages();

        try {

            Wallet myWallet = (Wallet)session.get(ConstantGeneral.SESSION_WALLET);
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("finalState", TransactionState.PENDING.getId());
            params.put("walletId", myWallet.getId());
            params.put("myWallet",myWallet);
            params.put("orderCode",orderCode);

            TransactionDeposit transactionDeposit = transactionDepositManager.cancel(params);
            transactionReviewDto = DataConverter.convertToMerchantTransactionReviewDto(transactionDeposit, true);

            Double totalPrice = transactionDeposit.getWalletTotalPrice();
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
    public String approveDeposit() {

        responseDto.cleanMessages();

        try {

            Wallet myWallet = (Wallet)session.get(ConstantGeneral.SESSION_WALLET);
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("finalState", TransactionState.PENDING.getId());
            params.put("walletId", myWallet.getId());
            params.put("myWallet",myWallet);
            params.put("orderCode",orderCode);

            TransactionDeposit transactionDeposit = transactionDepositManager.approve(params);
            transactionReviewDto = DataConverter.convertToMerchantTransactionReviewDto(transactionDeposit, true);

            Double totalPrice = transactionDeposit.getWalletTotalPrice();
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


    private synchronized TransactionDeposit createTransaction(Double depositAmount, CurrencyType depositAmountCurrencyType,
                                                               Wallet wallet, WalletSetup walletSetup, TransactionState transactionState) throws PermissionDeniedException, InvalidParameterException, InternalErrorException {

        String msgUnsupported = getText("wallet.back.end.message.unsupported.currency") + "\t";

        //<editor-fold desc="initBlock">
        Date currentDate = new Date(System.currentTimeMillis());


        boolean isDepositCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(depositAmountCurrencyType.getId());
        if (!isDepositCurrencyTypeSupported) {
            throw new PermissionDeniedException(msgUnsupported + depositAmountCurrencyType.getName());
        }

        Long walletId = wallet.getId();
        Long walletSetupId = walletSetup.getId();

        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();
        CurrencyType walletCurrencyType = wallet.getCurrencyType();

        int setupCurrencyTypeId = setupCurrencyType.getId();
        int walletCurrencyTypeId = walletCurrencyType.getId();
        int depositCurrencyTypeId = depositAmountCurrencyType.getId();

        MerchantDeposit merchantDeposit = initMerchantDeposit(currentDate, walletId, walletSetupId);

        TransactionDeposit transactionDeposit = new TransactionDeposit();
        transactionDeposit.setDepositAmount(depositAmount);
        transactionDeposit.setDepositAmountCurrencyType(depositAmountCurrencyType);
        transactionDeposit.setDepositMerchantTotalTax(merchantDeposit.getMerchantDepositTax().getDepositTaxTotal());
        transactionDeposit.setDepositMerchantTotalTaxCurrencyType(depositAmountCurrencyType);
        transactionDeposit.setSetupTotalAmountCurrencyType(setupCurrencyType);
        transactionDeposit.setOpenedAt(currentDate);
        transactionDeposit.setSetupId(walletSetupId);
        transactionDeposit.setWalletId(walletId);
        transactionDeposit.setFinalState(transactionState);
        transactionDeposit.setMerchantDeposit(merchantDeposit);
        transactionDeposit.setEncoded(isEncoded);

        boolean isWalletCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(walletCurrencyTypeId);
        if (!isWalletCurrencyTypeSupported) {
            throw new PermissionDeniedException(msgUnsupported + walletCurrencyType.getName());
        }

        //</editor-fold>

        if (depositCurrencyTypeId == setupCurrencyTypeId) {
            if (depositCurrencyTypeId == walletCurrencyTypeId) {
                simpleTransactionDepositWithSameCurrencies(transactionDeposit, wallet, walletSetup, depositAmount, currentDate, transactionState);
            } else {
                otherWalletCurrency(transactionDeposit, wallet, walletSetup, depositAmount, currentDate, transactionState);
            }
        } else {
            throw new PermissionDeniedException(msgUnsupported + depositAmountCurrencyType);
            //<editor-fold desc="elseBlock">


            //</editor-fold>
        }
        return transactionDeposit;
    }


    private synchronized void simpleTransactionDepositWithSameCurrencies(TransactionDeposit transactionDeposit,
                                                                          Wallet wallet,
                                                                          WalletSetup walletSetup,
                                                                          Double depositAmount,
                                                                          Date currentDate,
                                                                          TransactionState transactionState) throws InternalErrorException {

        MerchantDeposit merchantDeposit = transactionDeposit.getMerchantDeposit();
        MerchantDepositTax merchantDepositTax = merchantDeposit.getMerchantDepositTax();

        Long walletId = wallet.getId();
        Long setupId = walletSetup.getId();
        CurrencyType currencyType = walletSetup.getCurrencyType();

        Map<String, Object> receiverTaxTypeMap = calculateReceiverTax(walletSetup, depositAmount);
        TransactionTaxType receiverTaxType = (TransactionTaxType) receiverTaxTypeMap.get(TAX_TYPE_KEY);
        Double receiverTaxAmount = (Double) receiverTaxTypeMap.get(TAX_KEY);

        Map<String, Object> depositTaxTypeMap = calculateDepositTax(walletSetup, depositAmount);
        TransactionTaxType depositTaxType = (TransactionTaxType) depositTaxTypeMap.get(TAX_TYPE_KEY);
        Double depositTaxAmount = (Double) depositTaxTypeMap.get(TAX_KEY);

        Double walletTotalAmount = depositAmount + depositTaxAmount + receiverTaxAmount;
        Double setupTotalAmount = depositTaxAmount + receiverTaxAmount;

        TransactionDepositProcessTax processTax = new TransactionDepositProcessTax(currentDate, walletId, setupId, receiverTaxAmount, currencyType, receiverTaxType);
        WalletSetupDepositTax setupDepositTax = new WalletSetupDepositTax(currentDate, walletId, setupId, depositTaxAmount, currencyType, depositTaxType);

        TransactionDepositTax depositTax = new TransactionDepositTax(currentDate, walletId, setupId, processTax, setupDepositTax, merchantDepositTax);
        TransactionDepositProcess depositProcess = new TransactionDepositProcess(transactionState, currentDate, walletId, setupId, depositAmount, currencyType, processTax, setupDepositTax);

        transactionDeposit.setProcessStart(depositProcess);
        transactionDeposit.setWalletTotalPrice(walletTotalAmount);
        transactionDeposit.setWalletTotalPriceCurrencyType(currencyType);

        transactionDeposit.setSetupTotalAmount(setupTotalAmount);
        transactionDeposit.setSetupTotalAmountCurrencyType(currencyType);
        transactionDeposit.setTax(depositTax);
    }

    private synchronized void otherWalletCurrency(TransactionDeposit transactionDeposit,
                                                  Wallet wallet,
                                                  WalletSetup walletSetup,
                                                  Double depositAmount,
                                                  Date currentDate,
                                                  TransactionState transactionState) throws InternalErrorException {

        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();
        CurrencyType walletCurrencyType = wallet.getCurrencyType();
        ExchangeRate selectedExchangeRate = getDefaultExchangeRate(walletCurrencyType, setupCurrencyType);
        Double rateAmount = selectedExchangeRate.getBuy();

        Long walletId = wallet.getId();
        Long setupId = walletSetup.getId();
        Long rateId = selectedExchangeRate.getId();

        MerchantDeposit merchantDeposit = transactionDeposit.getMerchantDeposit();
        MerchantDepositTax merchantDepositTax = merchantDeposit.getMerchantDepositTax();
        merchantDepositTax = calculateMerchantDepositTaxPrice(walletId, setupId, rateId, currentDate, merchantDepositTax, selectedExchangeRate);//1 USD = 480 AMD

        Double depositPrice = depositAmount * rateAmount;//48000AMD
        TransactionDepositExchange depositExchange = new TransactionDepositExchange(walletId, setupId, rateId, currentDate, depositAmount, setupCurrencyType, rateAmount, walletCurrencyType, depositPrice, walletCurrencyType);

        Map<String, Object> receiverProcessTaxMap = calculateReceiverTax(walletSetup, depositAmount);//100 USD
        TransactionTaxType receiverProcessTaxType = (TransactionTaxType) receiverProcessTaxMap.get(TAX_TYPE_KEY);
        Double receiverProcessTaxAmount = (Double) receiverProcessTaxMap.get(TAX_KEY);//2 USD
        Double receiverProcessTaxPrice = receiverProcessTaxAmount * rateAmount;//960
        TransactionDepositExchange receiverProcessExchange = new TransactionDepositExchange(walletId, setupId, rateId, currentDate, receiverProcessTaxAmount, setupCurrencyType, rateAmount, walletCurrencyType, receiverProcessTaxPrice, walletCurrencyType);
        TransactionDepositProcessTax receiverProcessTax = new TransactionDepositProcessTax(currentDate, walletId, setupId, receiverProcessTaxAmount, setupCurrencyType, receiverProcessTaxPrice, walletCurrencyType, receiverProcessTaxType, receiverProcessExchange);


        Map<String, Object> depositTaxTypeMap = calculateDepositTax(walletSetup, depositAmount);//100
        TransactionTaxType depositTaxType = (TransactionTaxType) depositTaxTypeMap.get(TAX_TYPE_KEY);
        Double depositTaxAmount = (Double) depositTaxTypeMap.get(TAX_KEY);
        Double depositTaxPrice = depositTaxAmount * rateAmount;//3 USD
        TransactionDepositExchange depositTaxExchange = new TransactionDepositExchange(walletId, setupId, rateId, currentDate, depositTaxAmount, setupCurrencyType, rateAmount, walletCurrencyType, depositTaxPrice, walletCurrencyType);
        WalletSetupDepositTax setupDepositTax = new WalletSetupDepositTax(currentDate, walletId, setupId, depositTaxAmount, setupCurrencyType, depositTaxPrice, walletCurrencyType, depositTaxType, depositTaxExchange);

        Map<String, Object> exchangeDepositTaxMap = calculateReceiverExchangeTax(walletSetup, depositAmount);//1000 USD
        TransactionTaxType exchangeDepositTaxType = (TransactionTaxType) exchangeDepositTaxMap.get(TAX_TYPE_KEY);
        Double exchangeDepositTaxAmount = (Double) exchangeDepositTaxMap.get(TAX_KEY);//4 USD
        Double exchangeDepositTaxPrice = exchangeDepositTaxAmount * rateAmount;// AMD
        TransactionDepositExchangeTax exchangeDepositTax = new TransactionDepositExchangeTax(currentDate, walletId, setupId, exchangeDepositTaxAmount, setupCurrencyType, exchangeDepositTaxPrice, walletCurrencyType, exchangeDepositTaxType);
        TransactionDepositExchange exchangeDeposit = new TransactionDepositExchange(walletId, setupId, rateId, currentDate, exchangeDepositTaxAmount, setupCurrencyType, rateAmount, walletCurrencyType, exchangeDepositTaxPrice, walletCurrencyType, exchangeDepositTax);

        Double setupTotalProcess = receiverProcessTaxAmount + depositTaxAmount;//2+3
        Double setupTotalAmount = setupTotalProcess + exchangeDepositTaxAmount;//5+4
        Double totalTaxAmount = merchantDepositTax.getDepositTaxTotal() + setupTotalAmount;
        Double totalTaxPrice = merchantDepositTax.getDepositTaxPrice() + receiverProcessTaxPrice + depositTaxPrice + exchangeDepositTaxPrice;

        Double walletTotalAmount = depositAmount + totalTaxAmount;
        Double walletTotalPrice = depositPrice + totalTaxPrice;

        TransactionDepositTax depositTax = new TransactionDepositTax(currentDate, walletId, setupId, receiverProcessTax, setupDepositTax, merchantDepositTax, exchangeDepositTax);
        TransactionDepositProcess depositProcess =
                new TransactionDepositProcess(transactionState, currentDate, walletId, setupId,
                        depositAmount, setupCurrencyType,
                        depositPrice, walletTotalPrice, walletCurrencyType,
                        depositAmount, setupTotalProcess, setupCurrencyType,
                        receiverProcessTax, setupDepositTax, exchangeDeposit);

        transactionDeposit.setProcessStart(depositProcess);
        transactionDeposit.setWalletTotalPrice(walletTotalPrice);
        transactionDeposit.setWalletTotalPriceCurrencyType(walletCurrencyType);

        transactionDeposit.setSetupTotalAmount(setupTotalAmount);
        transactionDeposit.setSetupTotalAmountCurrencyType(setupCurrencyType);
        transactionDeposit.setTax(depositTax);
    }

    private synchronized MerchantDeposit initMerchantDeposit(Date actionDate, Long walletId, Long setupId) throws InvalidParameterException, NumberFormatException {

        if (Utils.isEmpty(itemId)) {
            throw new InvalidParameterException("Deposit itemId is empty");
        }
        if (Utils.isEmpty(name)) {
            throw new InvalidParameterException("Deposit name is empty");
        }
        if (Utils.isEmpty(description)) {
            throw new InvalidParameterException("Deposit description is empty");
        }


        MerchantDeposit merchantDeposit = new MerchantDeposit();
        merchantDeposit.setItemId(Long.parseLong(itemId));
        merchantDeposit.setName(name);
        merchantDeposit.setStartAt(actionDate);
        merchantDeposit.setRationalStopAt(Utils.getAfterSecunds(actionDate, rationalSecondsDuration));

        merchantDeposit.setDescription(description);
        merchantDeposit.setMerchantDepositTax(initMerchantDepositTax(actionDate, walletId, setupId));

        return merchantDeposit;
    }


    private synchronized MerchantDepositTax initMerchantDepositTax(Date actionDate, Long walletId, Long setupId) throws InvalidParameterException {
        MerchantDepositTax depositTax = new MerchantDepositTax(actionDate, walletId, setupId, paidTaxToMerchant, paidTaxCurrencyType, paidTaxType);
        return depositTax;
    }

    private synchronized MerchantDepositTax calculateMerchantDepositTaxPrice(Long walletId, Long setupId, Long rateId, Date exchangeDate, MerchantDepositTax merchantDepositTax, ExchangeRate selectedExchangeRate) {
        Double setupAmount = merchantDepositTax.getDepositTax();
        CurrencyType setupCurrencyType = merchantDepositTax.getDepositTaxCurrencyType();
        Double rate = selectedExchangeRate.getBuy();
        CurrencyType rateCurrencyType = selectedExchangeRate.getToCurrency();
        Double walletPaidAmount = setupAmount * rate;

        TransactionDepositExchange exchange = new TransactionDepositExchange(walletId, setupId, rateId, exchangeDate, setupAmount, setupCurrencyType, rate, rateCurrencyType, walletPaidAmount, rateCurrencyType);

        return new MerchantDepositTax(exchangeDate, walletId, setupId, setupAmount, setupCurrencyType, walletPaidAmount, rateCurrencyType, merchantDepositTax.getDepositTaxType(), exchange);
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
    public void setDepositId(String depositId) {
        this.depositId = depositId;
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

    public void setTransactionDepositManager(ITransactionDepositManager transactionDepositManager) {
        this.transactionDepositManager = transactionDepositManager;
    }
}
