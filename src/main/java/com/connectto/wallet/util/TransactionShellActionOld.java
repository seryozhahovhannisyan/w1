package com.connectto.wallet.util;

import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.exception.PermissionDeniedException;
import com.connectto.general.model.User;
import com.connectto.general.model.WalletSetup;
import com.connectto.general.util.ConstantGeneral;
import com.connectto.general.util.ShellAction;
import com.connectto.general.util.Utils;
import com.connectto.wallet.dataaccess.service.wallet.IExchangeRateManager;
import com.connectto.wallet.dataaccess.service.general.IWalletLoggerManager;
import com.connectto.wallet.model.general.WalletLogger;
import com.connectto.wallet.model.general.lcp.LogAction;
import com.connectto.wallet.model.general.lcp.LogLevel;
import com.connectto.wallet.model.wallet.*;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionState;
import com.connectto.wallet.model.wallet.lcp.TransactionTaxType;
import com.connectto.wallet.model.wallet.lcp.TransactionType;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Serozh on 2/15/16.
 */
public class TransactionShellActionOld extends ShellAction {


    private static final Logger logger = Logger.getLogger(TransactionShellActionOld.class.getSimpleName());

    private static final String FEE_KEY = "fee";
    private static final String FEE_TYPE_KEY = "type";
    private static final String MESSAGE_UNKNOWN_WALLETS = "Please choose receiver wallet or sender ";
    private static final String MESSAGE_LESS_MONEY = "Less money ";
    private static final String MESSAGE_LESS_REQUEST = "Aveli ";
    private static final String MESSAGE_NOT_SUPPORTED_CURRENCY = "The currency not supported ";

    private IExchangeRateManager exchangeRateManager;
    private IWalletLoggerManager walletLoggerManager;
    //criteria
    private String searchLike;
    private String rangeDateGreat;
    private String rangeDateLess;
    private CurrencyType byCurrency;
    private Double rangeAmountGreat;
    private Double rangeAmountLess;
    private int transactionType;
    private int disputeState;
    //
    protected String transactionId;

    protected String sessionId;


    public void setExchangeRateManager(IExchangeRateManager exchangeRateManager) {
        this.exchangeRateManager = exchangeRateManager;
    }

    public void setWalletLoggerManager(IWalletLoggerManager walletLoggerManager) {
        this.walletLoggerManager = walletLoggerManager;
    }

    protected synchronized Transaction createTransaction(
            Double productAmount, CurrencyType productCurrencyType,
            Wallet fromWallet,
            Wallet toWallet,
            WalletSetup walletSetup,
            TransactionState transactionState
    ) throws InternalErrorException, PermissionDeniedException {

        Date currentDate = new Date(System.currentTimeMillis());

        if (Utils.isEmpty(sessionId)) {
            User sesUser = (User) session.get(ConstantGeneral.SESSION_USER);
            if (sesUser != null) {
                sessionId = sesUser.getCurrentAccount().getSessionId();
            }
        }

        if (fromWallet == null && toWallet == null) {
            throw new InternalErrorException(MESSAGE_UNKNOWN_WALLETS);
        }

        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();

        int setupCurrencyTypeId = setupCurrencyType.getId();
        int productCurrencyTypeId = productCurrencyType.getId();

        Transaction transaction = new Transaction();
        transaction.setProductAmount(productAmount);
        transaction.setProductCurrencyType(productCurrencyType);
        transaction.setSetupAmountCurrencyType(setupCurrencyType);
        transaction.setState(TransactionState.PENDING);
        transaction.setActionState(transactionState);
        transaction.setOpenedAt(currentDate);
        transaction.setSessionId(sessionId);

        if (fromWallet != null) {
            transaction.setFromWalletId(fromWallet.getId());
            transaction.setFromWallet(fromWallet);
        } else {
            transaction.setFromWalletSetupId(walletSetup.getId());
            transaction.setFromWalletSetup(walletSetup);
        }

        if (toWallet != null) {
            transaction.setToWalletId(toWallet.getId());
            transaction.setToWallet(toWallet);
        } else {
            transaction.setToWalletSetupId(walletSetup.getId());
            transaction.setToWalletSetup(walletSetup);
        }

        Double currentBalance = 0d;
        Double frozenAmount = 0d;

        if (fromWallet != null) {
            currentBalance = fromWallet.getMoney();
            frozenAmount = fromWallet.getFrozenAmount();
        } else {
            currentBalance = walletSetup.getBalance();
            frozenAmount = walletSetup.getFrozenAmount();
        }

        Double availableAmount = currentBalance - frozenAmount;
        if (availableAmount <= 0) {
            throw new InternalErrorException(MESSAGE_LESS_MONEY);
        }
        //<editor-fold desc="productCurrencyTypeId == setupCurrencyTypeId">
        if (productCurrencyTypeId == setupCurrencyTypeId) {

            if (fromWallet != null) {

                CurrencyType fromCurrencyType = fromWallet.getCurrencyType();
                int fromCurrencyTypeId = fromWallet.getCurrencyType().getId();
                Long fromWalletId = fromWallet.getId();

                //Double totalAmount = null;
                if (productCurrencyTypeId == fromCurrencyTypeId) {

                    Map<String, Object> transferFeeTypeMap = calculateTransferFee(walletSetup, productAmount);
                    TransactionTaxType taxType = (TransactionTaxType) transferFeeTypeMap.get(FEE_TYPE_KEY);
                    Double transferFee = (Double) transferFeeTypeMap.get(FEE_KEY);

                    Double totalAmount = productAmount + transferFee;
                    if (availableAmount < totalAmount) {
                        throw new InternalErrorException(MESSAGE_LESS_MONEY);
                    }
                    //Long walletId, Long walletSetupId, Date paymentDate,
                    // Double totalSetupTax, Double totalSetupTaxPrice,
                    // Double transferTax, Double transferTaxPrice, TransactionTaxType transferTaxType, Exchange transferTaxExchange,
                    // Double transferExchangeTax, Double transferExchangeTaxPrice, TransactionTaxType transferExchangeTaxType, Exchange transferExchangeTaxExchange,
                    // Double transferExchangeToSetupCurrencyTax, Double transferExchangeToSetupCurrencyTaxPrice, TransactionTaxType transferExchangeToSetupCurrencTaxType, Exchange transferExchangeToSetupCurrencyTaxExchange) {
                    TransactionTax transactionTax = new TransactionTax(fromWalletId, walletSetup.getId(), currentDate,
                            transferFee, transferFee,
                            transferFee, transferFee, taxType, null,
                            null, null, null, null,
                            null, null, null, null,
                            null, false);
                    //TransactionState transactionState, Date actionDate, Wallet wallet, WalletSetup walletSetup,
                    //Double frozenAmount, Double totalAmount, Double totalAmountPrice, Exchange totalAmountExchange,
                    //Double transferExchangeToSetupCurrency, Double transferExchangeToSetupCurrencyPrice, Exchange transferExchangeToSetupCurrencyExchange, Exchange transferExchangeToSetupCurrencyPriceExchange,
                    // TransactionTax tax) {
                    TransactionProcess transactionAction = new TransactionProcess(currentDate, fromWalletId, walletSetup.getId(),
                            productAmount, totalAmount, totalAmount, null, null, null,
                            transactionTax);

                    transaction.setFromTransactionProcess(transactionAction);
                    transaction.setFromTotalPrice(totalAmount);
                    transaction.setFromTotalPriceCurrencyType(fromWallet.getCurrencyType());
                    transaction.setSetupAmount(productAmount);
                } else {

                    boolean isFromCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(fromCurrencyTypeId);
                    if (!isFromCurrencyTypeSupported) {
                        throw new PermissionDeniedException(String.format("From user currency type %s not supported ", fromCurrencyType.getName()));
                    }

                    Map<String, Object> transferFeeMap = calculateTransferFee(walletSetup, productAmount);
                    TransactionTaxType transferFeeType = (TransactionTaxType) transferFeeMap.get(FEE_TYPE_KEY);
                    Double transferFee = (Double) transferFeeMap.get(FEE_KEY);

                    Double setupAmount = productAmount + transferFee;

                    Map<String, Object> exchangeTransferFeeMap = calculateExchangeTransferFee(walletSetup, setupAmount);
                    Double exchangeTransferFee = (Double) exchangeTransferFeeMap.get(FEE_KEY);
                    TransactionTaxType exchangeTransferFeeTaxType = (TransactionTaxType) exchangeTransferFeeMap.get(FEE_TYPE_KEY);

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("isDefault", 1);
                    params.put("toCurrency", fromCurrencyTypeId);
                    params.put("oneCurrency", setupCurrencyTypeId);
                    params.put("lastOne", 1);

                    List<ExchangeRate> exchangeRates = exchangeRateManager.getByParams(params);
                    if (Utils.isEmpty(exchangeRates)) {
                        String msg = "Could not found exchangeRates, with params, params = " + params;
                        throw new InternalErrorException(msg);
                    }

                    ExchangeRate selectedExchangeRate = exchangeRates.get(0);
                    Double rateAmount = selectedExchangeRate.getBuy();

                    setupAmount += exchangeTransferFee;

                    Double transferFeePrice = transferFee * rateAmount;
                    Double exchangeTransferFeePrice = exchangeTransferFee * rateAmount;
                    Double setupAmountPrice = setupAmount * rateAmount;

                    Double totalTax = transferFee + exchangeTransferFee;
                    Double totalTaxPrice = transferFeePrice + exchangeTransferFeePrice;

                    Double totalAmount = productAmount + totalTax;

                    if (availableAmount < totalTaxPrice) {
                        throw new InternalErrorException(MESSAGE_LESS_MONEY);
                    }

                    Exchange transferFeeExchange = new Exchange(transferFee, setupCurrencyType, rateAmount, setupCurrencyType, transferFeePrice, fromCurrencyType, currentDate, fromWalletId, selectedExchangeRate.getId());
                    Exchange exchangeFeeExchange = new Exchange(exchangeTransferFee, setupCurrencyType, rateAmount, setupCurrencyType, exchangeTransferFeePrice, fromCurrencyType, currentDate, fromWalletId, selectedExchangeRate.getId());
                    Exchange productExchange = new Exchange(setupAmount, setupCurrencyType, rateAmount, setupCurrencyType, setupAmountPrice, fromCurrencyType, currentDate, fromWalletId, selectedExchangeRate.getId());

                    //Long walletId, Long walletSetupId, Date paymentDate,
                    // Double totalSetupTax, Double totalSetupTaxPrice, Exchange totalSetupTaxExchange,
                    // Double transferTax, Double transferTaxPrice, TransactionTaxType transferTaxType, Exchange transferTaxExchange,
                    // Double transferExchangeTax, Double transferExchangeTaxPrice, TransactionTaxType transferExchangeTaxType, Exchange transferExchangeTaxExchange,
                    // Double transferExchangeToSetupCurrencyTax, Double transferExchangeToSetupCurrencyTaxPrice, TransactionTaxType transferExchangeToSetupCurrencTaxType, Exchange transferExchangeToSetupCurrencyTaxExchange) {
                    TransactionTax transactionTax = new TransactionTax(fromWalletId, walletSetup.getId(), currentDate,
                            totalTax, totalTaxPrice,
                            transferFee, transferFeePrice, transferFeeType, transferFeeExchange,
                            exchangeTransferFee, exchangeTransferFeePrice, exchangeTransferFeeTaxType, exchangeFeeExchange, null,
                            null, null, null, null, false);

                    //TransactionProcess(TransactionState transactionState, Date actionDate, Long walletId, Long walletSetupId, Double setupAmount, CurrencyType setupAmountCurrencyType, Exchange setupAmountExchange, Double priceAmount,  CurrencyType priceAmountCurrencyType, Exchange priceAmountExchange, TransactionTax tax, TransactionTax taxForExchange) {

                    //TransactionState transactionState, Date actionDate, Wallet wallet, WalletSetup walletSetup,
                    //Double frozenAmount, Double totalAmount, Double totalAmountPrice, Exchange totalAmountExchange,
                    //Double transferExchangeToSetupCurrency, Double transferExchangeToSetupCurrencyPrice, Exchange transferExchangeToSetupCurrencyExchange, Exchange transferExchangeToSetupCurrencyPriceExchange,
                    // TransactionTax tax) {
                    TransactionProcess transactionAction = new TransactionProcess(currentDate, fromWalletId, walletSetup.getId(),
                            totalAmount, totalAmount, setupAmountPrice, productExchange, null,null,
                            transactionTax);
                    transaction.setFromTransactionProcess(transactionAction);
                    transaction.setFromTotalPrice(setupAmountPrice);
                    transaction.setFromTotalPriceCurrencyType(fromWallet.getCurrencyType());
                    transaction.setSetupAmount(productAmount);
                }
            }

            if (toWallet != null) {
                CurrencyType toCurrencyType = toWallet.getCurrencyType();
                int toCurrencyTypeId = toWallet.getCurrencyType().getId();
                Long toWalletId = toWallet.getId();

                if (productCurrencyTypeId == toCurrencyTypeId) {
                    Map<String, Object> receiverFeeMap = calculateReceiverFee(walletSetup, productAmount);
                    Double receiverFee = (Double) receiverFeeMap.get(FEE_KEY);
                    TransactionTaxType receiverFeeType = (TransactionTaxType) receiverFeeMap.get(FEE_TYPE_KEY);

                    Double totalAmount = productAmount - receiverFee;
                    if (totalAmount <= 0) {
                        throw new InternalErrorException(MESSAGE_LESS_REQUEST);
                    }

                    TransactionTax transactionTax = new TransactionTax(toWalletId, walletSetup.getId(), currentDate,
                            receiverFee, receiverFee,
                            receiverFee, receiverFee, receiverFeeType, null,
                            null, null, null, null,
                            null, null, null, null,
                            null, false);
                    //TransactionState transactionState, Date actionDate, Wallet wallet, WalletSetup walletSetup,
                    //Double frozenAmount, Double totalAmount, Double totalAmountPrice, Exchange totalAmountExchange,
                    //Double transferExchangeToSetupCurrency, Double transferExchangeToSetupCurrencyPrice, Exchange transferExchangeToSetupCurrencyExchange, Exchange transferExchangeToSetupCurrencyPriceExchange,
                    // TransactionTax tax) {
                    TransactionProcess transactionAction = new TransactionProcess(currentDate, toWalletId, walletSetup.getId(),
                            productAmount, totalAmount, totalAmount, null,null,null,
                            transactionTax);

                    transaction.setToTransactionProcess(transactionAction);
                    transaction.setToTotalPrice(totalAmount);
                    transaction.setToTotalPriceCurrencyType(toWallet.getCurrencyType());
                } else {

                    boolean isToCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(toCurrencyTypeId);
                    if (!isToCurrencyTypeSupported) {
                        throw new PermissionDeniedException(String.format("To user currency type %s not supported ", toCurrencyType.getName()));
                    }
                    Map<String, Object> receiverFeeMap = calculateReceiverFee(walletSetup, productAmount);
                    Double receiverFee = (Double) receiverFeeMap.get(FEE_KEY);
                    TransactionTaxType receiverFeeType = (TransactionTaxType) receiverFeeMap.get(FEE_TYPE_KEY);

                    Double totalAmount = productAmount - receiverFee;

                    Map<String, Object> exchangeReceiverFeeMap = calculateExchangeReceiverFee(walletSetup, totalAmount);
                    Double exchangeReceiverFee = (Double) exchangeReceiverFeeMap.get(FEE_KEY);
                    TransactionTaxType exchangeReceiverFeeType = (TransactionTaxType) exchangeReceiverFeeMap.get(FEE_TYPE_KEY);

                    totalAmount -= exchangeReceiverFee;

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("isDefault", 1);
                    params.put("toCurrency", toCurrencyTypeId);
                    params.put("oneCurrency", setupCurrencyTypeId);
                    params.put("lastOne", 1);

                    List<ExchangeRate> exchangeRates = exchangeRateManager.getByParams(params);
                    if (Utils.isEmpty(exchangeRates)) {
                        String msg = "Could not found exchangeRates, with params, params = " + params;
                        throw new InternalErrorException(msg);
                    }

                    ExchangeRate selectedExchangeRate = exchangeRates.get(0);
                    Double rateAmount = selectedExchangeRate.getBuy();

                    Double totalTax = receiverFee + exchangeReceiverFee;
                    Double totalAmountPrice = totalAmount * rateAmount;

                    Double receiverFeePrice = receiverFee * rateAmount;
                    Double exchangeReceiverFeePrice = exchangeReceiverFee * rateAmount;
                    Double totalTaxPrice = totalTax * rateAmount;


                    if (totalAmountPrice <= 0) {
                        throw new InternalErrorException(MESSAGE_LESS_REQUEST);
                    }

                    Exchange transferFeeExchange = new Exchange(receiverFee, setupCurrencyType, rateAmount, setupCurrencyType, receiverFeePrice, toCurrencyType, currentDate, toWalletId, selectedExchangeRate.getId());
                    Exchange exchangeFeeExchange = new Exchange(exchangeReceiverFee, setupCurrencyType, rateAmount, setupCurrencyType, exchangeReceiverFeePrice, toCurrencyType, currentDate, toWalletId, selectedExchangeRate.getId());
                    Exchange totalAmountExchange = new Exchange(totalAmount, setupCurrencyType, rateAmount, setupCurrencyType, totalAmountPrice, toCurrencyType, currentDate, toWalletId, selectedExchangeRate.getId());

                    TransactionTax transactionTax = new TransactionTax(toWalletId, walletSetup.getId(), currentDate,
                            totalTax, totalTaxPrice,
                            receiverFee, receiverFeePrice, receiverFeeType, transferFeeExchange,
                            exchangeReceiverFee, exchangeReceiverFeePrice, exchangeReceiverFeeType, exchangeFeeExchange,
                            null, null, null, null,
                            null, false);

                    //TransactionState transactionState, Date actionDate, Wallet wallet, WalletSetup walletSetup,
                    //Double frozenAmount, Double totalAmount, Double totalAmountPrice, Exchange totalAmountExchange,
                    //Double transferExchangeToSetupCurrency, Double transferExchangeToSetupCurrencyPrice, Exchange transferExchangeToSetupCurrencyExchange, Exchange transferExchangeToSetupCurrencyPriceExchange,
                    // TransactionTax tax) {
                    TransactionProcess transactionAction = new TransactionProcess(currentDate, toWalletId, walletSetup.getId(),
                            totalAmount, totalAmount, totalAmountPrice, totalAmountExchange, null,null,
                            transactionTax);

                    transaction.setToTransactionProcess(transactionAction);
                    transaction.setToTotalPrice(totalAmountPrice);
                    transaction.setToTotalPriceCurrencyType(toWallet.getCurrencyType());
                }
            }
        }
        //</editor-fold>
        else {

            if (fromWallet != null) {

                CurrencyType fromCurrencyType = fromWallet.getCurrencyType();
                int fromCurrencyTypeId = fromWallet.getCurrencyType().getId();
                Long fromWalletId = fromWallet.getId();

                //<editor-fold desc="productCurrencyTypeId == fromCurrencyTypeId">
                if (productCurrencyTypeId == fromCurrencyTypeId) {

                    boolean isFromCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(fromCurrencyTypeId);
                    if (!isFromCurrencyTypeSupported) {
                        throw new PermissionDeniedException(String.format("From user currency type %s not supported ", fromCurrencyType.getName()));
                    }

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("isDefault", 1);
                    params.put("toCurrency", fromCurrencyTypeId);
                    params.put("oneCurrency", setupCurrencyTypeId);
                    params.put("lastOne", 1);

                    List<ExchangeRate> exchangeRates = exchangeRateManager.getByParams(params);
                    if (Utils.isEmpty(exchangeRates)) {
                        String msg = "Could not found exchangeRates, with params, params = " + params;
                        throw new InternalErrorException(msg);
                    }

                    ExchangeRate selectedExchangeRate = exchangeRates.get(0);
                    Double rateAmount = selectedExchangeRate.getBuy();

                    Double transferExchangeToSetupCurrency = productAmount / rateAmount;

                    Map<String, Object> transferFeeMap = calculateTransferFee(walletSetup, transferExchangeToSetupCurrency);
                    TransactionTaxType transferFeeType = (TransactionTaxType) transferFeeMap.get(FEE_TYPE_KEY);
                    Double transferFee = (Double) transferFeeMap.get(FEE_KEY);

                    Double setupAmount = transferExchangeToSetupCurrency + transferFee;

                    Map<String, Object> exchangeTransferFeeMap = calculateExchangeTransferFee(walletSetup, setupAmount);
                    Double exchangeTransferFee = (Double) exchangeTransferFeeMap.get(FEE_KEY);
                    TransactionTaxType exchangeTransferFeeTaxType = (TransactionTaxType) exchangeTransferFeeMap.get(FEE_TYPE_KEY);

                    setupAmount += exchangeTransferFee;
                    Double transferFeePrice = transferFee * rateAmount;
                    Double exchangeTransferFeePrice = exchangeTransferFee * rateAmount;

                    Double totalTax = transferFee + exchangeTransferFee;
                    Double totalTaxPrice = transferFeePrice + exchangeTransferFeePrice;

                    Double totalAmount = transferExchangeToSetupCurrency + totalTax;
                    Double totalAmountPrice = productAmount + totalTaxPrice;
                    if (availableAmount < totalAmountPrice) {
                        throw new InternalErrorException(MESSAGE_LESS_MONEY);
                    }

                    Exchange transferFeeExchange = new Exchange(transferFee, setupCurrencyType, rateAmount, setupCurrencyType, transferFeePrice, fromCurrencyType, currentDate, fromWalletId, selectedExchangeRate.getId());
                    Exchange exchangeFeeExchange = new Exchange(exchangeTransferFee, setupCurrencyType, rateAmount, setupCurrencyType, exchangeTransferFeePrice, fromCurrencyType, currentDate, fromWalletId, selectedExchangeRate.getId());
                    Exchange productExchange = new Exchange(transferExchangeToSetupCurrency, setupCurrencyType, rateAmount, setupCurrencyType, productAmount, fromCurrencyType, currentDate, fromWalletId, selectedExchangeRate.getId());

                    //Long walletId, Long walletSetupId, Date paymentDate,
                    // Double totalSetupTax, Double totalSetupTaxPrice, Exchange totalSetupTaxExchange,
                    // Double transferTax, Double transferTaxPrice, TransactionTaxType transferTaxType, Exchange transferTaxExchange,
                    // Double transferExchangeTax, Double transferExchangeTaxPrice, TransactionTaxType transferExchangeTaxType, Exchange transferExchangeTaxExchange,
                    // Double transferExchangeToSetupCurrencyTax, Double transferExchangeToSetupCurrencyTaxPrice, TransactionTaxType transferExchangeToSetupCurrencTaxType, Exchange transferExchangeToSetupCurrencyTaxExchange) {
                    TransactionTax transactionTax = new TransactionTax(fromWalletId, walletSetup.getId(), currentDate,
                            totalTax, totalTaxPrice,
                            transferFee, transferFeePrice, transferFeeType, transferFeeExchange,
                            exchangeTransferFee, exchangeTransferFeePrice, exchangeTransferFeeTaxType, exchangeFeeExchange,
                            null,null,null,null,
                            null, false);

                    //TransactionProcess(TransactionState transactionState, Date actionDate, Long walletId, Long walletSetupId, Double setupAmount, CurrencyType setupAmountCurrencyType, Exchange setupAmountExchange, Double priceAmount,  CurrencyType priceAmountCurrencyType, Exchange priceAmountExchange, TransactionTax tax, TransactionTax taxForExchange) {

                    //TransactionState transactionState, Date actionDate, Wallet wallet, WalletSetup walletSetup,
                    //Double frozenAmount, Double totalAmount, Double totalAmountPrice, Exchange totalAmountExchange,
                    //Double transferExchangeToSetupCurrency, Double transferExchangeToSetupCurrencyPrice, Exchange transferExchangeToSetupCurrencyExchange, Exchange transferExchangeToSetupCurrencyPriceExchange,
                    // TransactionTax tax) {
                    TransactionProcess transactionAction = new TransactionProcess(currentDate, fromWalletId, walletSetup.getId(),
                            totalAmountPrice, totalAmount, totalAmountPrice, productExchange, null,null,
                            transactionTax);
                    transaction.setFromTransactionProcess(transactionAction);
                    transaction.setFromTotalPrice(totalAmountPrice);
                    transaction.setFromTotalPriceCurrencyType(fromWallet.getCurrencyType());
                    transaction.setSetupAmount(totalAmount);

                }
                //</editor-fold>
                else {
                    throw new PermissionDeniedException(MESSAGE_NOT_SUPPORTED_CURRENCY);
                }
            }

            if (toWallet != null) {
                CurrencyType toCurrencyType = toWallet.getCurrencyType();
                int toCurrencyTypeId = toWallet.getCurrencyType().getId();
                Long toWalletId = toWallet.getId();
                //<editor-fold desc="productCurrencyTypeId == toCurrencyTypeId">
                if (productCurrencyTypeId == toCurrencyTypeId) {

                    boolean isToCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(toCurrencyTypeId);
                    if (!isToCurrencyTypeSupported) {
                        throw new PermissionDeniedException(String.format("To user currency type %s not supported ", toCurrencyType.getName()));
                    }

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("isDefault", 1);
                    params.put("toCurrency", toCurrencyTypeId);
                    params.put("oneCurrency", setupCurrencyTypeId);
                    params.put("lastOne", 1);

                    List<ExchangeRate> exchangeRates = exchangeRateManager.getByParams(params);
                    if (Utils.isEmpty(exchangeRates)) {
                        String msg = "Could not found exchangeRates, with params, params = " + params;
                        throw new InternalErrorException(msg);
                    }

                    ExchangeRate selectedExchangeRate = exchangeRates.get(0);
                    Double rateAmount = selectedExchangeRate.getBuy();

                    Double receiverExchangeToSetupCurrency = productAmount / rateAmount;

                    Map<String, Object> receiverFeeMap = calculateReceiverFee(walletSetup, receiverExchangeToSetupCurrency);
                    TransactionTaxType receiverFeeType = (TransactionTaxType) receiverFeeMap.get(FEE_TYPE_KEY);
                    Double receiverFee = (Double) receiverFeeMap.get(FEE_KEY);

                    Double setupAmount = receiverExchangeToSetupCurrency - receiverFee;

                    Map<String, Object> exchangeReceiverFeeMap = calculateExchangeReceiverFee(walletSetup, setupAmount);
                    Double exchangeReceiverFee = (Double) exchangeReceiverFeeMap.get(FEE_KEY);
                    TransactionTaxType exchangeReceiverFeeType = (TransactionTaxType) exchangeReceiverFeeMap.get(FEE_TYPE_KEY);

                    setupAmount -= exchangeReceiverFee;

                    Double receiverFeePrice = receiverFee * rateAmount;
                    Double exchangeReceiverFeePrice = exchangeReceiverFee * rateAmount;

                    Double totalTax = receiverFee + exchangeReceiverFee;
                    Double totalTaxPrice = receiverFeePrice + exchangeReceiverFeePrice;

                    Double totalAmount = receiverExchangeToSetupCurrency + totalTax;
                    Double totalAmountPrice = productAmount - totalTaxPrice;
                    if (totalAmountPrice <= 0) {
                        throw new InternalErrorException(MESSAGE_LESS_REQUEST);
                    }

                    Exchange receiverFeeExchange = new Exchange(receiverFee, setupCurrencyType, rateAmount, setupCurrencyType, receiverFeePrice, toCurrencyType, currentDate, toWalletId, selectedExchangeRate.getId());
                    Exchange exchangeFeeExchange = new Exchange(exchangeReceiverFee, setupCurrencyType, rateAmount, setupCurrencyType, exchangeReceiverFeePrice, toCurrencyType, currentDate, toWalletId, selectedExchangeRate.getId());
                    Exchange productExchange = new Exchange(receiverExchangeToSetupCurrency, setupCurrencyType, rateAmount, setupCurrencyType, productAmount, toCurrencyType, currentDate, toWalletId, selectedExchangeRate.getId());

                    //Long walletId, Long walletSetupId, Date paymentDate,
                    // Double totalSetupTax, Double totalSetupTaxPrice, Exchange totalSetupTaxExchange,
                    // Double transferTax, Double transferTaxPrice, TransactionTaxType transferTaxType, Exchange transferTaxExchange,
                    // Double transferExchangeTax, Double transferExchangeTaxPrice, TransactionTaxType transferExchangeTaxType, Exchange transferExchangeTaxExchange,
                    // Double transferExchangeToSetupCurrencyTax, Double transferExchangeToSetupCurrencyTaxPrice, TransactionTaxType transferExchangeToSetupCurrencTaxType, Exchange transferExchangeToSetupCurrencyTaxExchange) {
                    TransactionTax transactionTax = new TransactionTax(toWalletId, walletSetup.getId(), currentDate,
                            totalTax, totalTaxPrice,
                            receiverFee, receiverFeePrice, receiverFeeType, receiverFeeExchange,
                            exchangeReceiverFee, exchangeReceiverFeePrice, exchangeReceiverFeeType, exchangeFeeExchange,
                            null, null, null, null,
                            null, false);

                    //TransactionProcess(TransactionState transactionState, Date actionDate, Long walletId, Long walletSetupId, Double setupAmount, CurrencyType setupAmountCurrencyType, Exchange setupAmountExchange, Double priceAmount,  CurrencyType priceAmountCurrencyType, Exchange priceAmountExchange, TransactionTax tax, TransactionTax taxForExchange) {

                    //TransactionState transactionState, Date actionDate, Wallet wallet, WalletSetup walletSetup,
                    //Double frozenAmount, Double totalAmount, Double totalAmountPrice, Exchange totalAmountExchange,
                    //Double transferExchangeToSetupCurrency, Double transferExchangeToSetupCurrencyPrice, Exchange transferExchangeToSetupCurrencyExchange, Exchange transferExchangeToSetupCurrencyPriceExchange,
                    // TransactionTax tax) {
                    TransactionProcess transactionAction = new TransactionProcess(currentDate, toWalletId, walletSetup.getId(),
                            totalAmountPrice, totalAmount, totalAmountPrice, productExchange, null,null,
                            transactionTax);
                    transaction.setToTransactionProcess(transactionAction);
                    transaction.setToTotalPrice(totalAmountPrice);
                    transaction.setToTotalPriceCurrencyType(toWallet.getCurrencyType());
                    transaction.setSetupAmount(productAmount);

                }
                //</editor-fold>
                else {
                    throw new PermissionDeniedException(MESSAGE_NOT_SUPPORTED_CURRENCY);
                }
            }

        }

        transaction.setTransactionType(TransactionType.WALLET);
        return transaction;
    }

    public synchronized Transaction prepareTransactionForCharge(
            Double productAmount, CurrencyType productCurrencyType,
            Wallet fromWallet,
            Wallet toWallet,
            WalletSetup walletSetup,
            TransactionState transactionState
    ) throws InternalErrorException, PermissionDeniedException {

        Date currentDate = new Date(System.currentTimeMillis());

        if (fromWallet == null && toWallet == null) {
            throw new InternalErrorException(MESSAGE_UNKNOWN_WALLETS);
        }

        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();

        int setupCurrencyTypeId = setupCurrencyType.getId();
        int productCurrencyTypeId = productCurrencyType.getId();

        Transaction transaction = new Transaction();
        transaction.setProductAmount(productAmount);
        transaction.setProductCurrencyType(productCurrencyType);
        transaction.setSetupAmountCurrencyType(setupCurrencyType);
        transaction.setState(TransactionState.CHARGE_AMOUNT);
        transaction.setActionState(transactionState);
        transaction.setOpenedAt(currentDate);

        if (fromWallet != null) {
            transaction.setFromWalletId(fromWallet.getId());
            transaction.setFromWallet(fromWallet);
        } else {
            transaction.setFromWalletSetupId(walletSetup.getId());
            transaction.setFromWalletSetup(walletSetup);
        }

        if (toWallet != null) {
            transaction.setToWalletId(toWallet.getId());
            transaction.setToWallet(toWallet);
        } else {
            transaction.setToWalletSetupId(walletSetup.getId());
            transaction.setToWalletSetup(walletSetup);
        }

        //<editor-fold desc="productCurrencyTypeId == setupCurrencyTypeId">
        if (productCurrencyTypeId == setupCurrencyTypeId) {

            if (fromWallet != null) {

                CurrencyType fromCurrencyType = fromWallet.getCurrencyType();
                int fromCurrencyTypeId = fromWallet.getCurrencyType().getId();
                Long fromWalletId = fromWallet.getId();

                //Double totalAmount = null;
                if (productCurrencyTypeId == fromCurrencyTypeId) {

                    Map<String, Object> transferFeeTypeMap = calculateTransferFee(walletSetup, productAmount);
                    TransactionTaxType taxType = (TransactionTaxType) transferFeeTypeMap.get(FEE_TYPE_KEY);
                    Double transferFee = (Double) transferFeeTypeMap.get(FEE_KEY);

                    Double totalAmount = productAmount + transferFee;

                    //Long walletId, Long walletSetupId, Date paymentDate,
                    // Double totalSetupTax, Double totalSetupTaxPrice,
                    // Double transferTax, Double transferTaxPrice, TransactionTaxType transferTaxType, Exchange transferTaxExchange,
                    // Double transferExchangeTax, Double transferExchangeTaxPrice, TransactionTaxType transferExchangeTaxType, Exchange transferExchangeTaxExchange,
                    // Double transferExchangeToSetupCurrencyTax, Double transferExchangeToSetupCurrencyTaxPrice, TransactionTaxType transferExchangeToSetupCurrencTaxType, Exchange transferExchangeToSetupCurrencyTaxExchange) {
                    TransactionTax transactionTax = new TransactionTax(fromWalletId, walletSetup.getId(), currentDate,
                            transferFee, transferFee,
                            transferFee, transferFee, taxType, null,
                            null, null, null, null,
                            null, null, null, null,
                            null, false);
                    //TransactionState transactionState, Date actionDate, Wallet wallet, WalletSetup walletSetup,
                    //Double frozenAmount, Double totalAmount, Double totalAmountPrice, Exchange totalAmountExchange,
                    //Double transferExchangeToSetupCurrency, Double transferExchangeToSetupCurrencyPrice, Exchange transferExchangeToSetupCurrencyExchange, Exchange transferExchangeToSetupCurrencyPriceExchange,
                    // TransactionTax tax) {
                    TransactionProcess transactionAction = new TransactionProcess(currentDate, fromWalletId, walletSetup.getId(),
                            productAmount, totalAmount, totalAmount, null,null,null,
                            transactionTax);

                    transaction.setFromTransactionProcess(transactionAction);
                    transaction.setFromTotalPrice(totalAmount);
                    transaction.setFromTotalPriceCurrencyType(fromWallet.getCurrencyType());
                    transaction.setSetupAmount(productAmount);
                } else {

                    boolean isFromCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(fromCurrencyTypeId);
                    if (!isFromCurrencyTypeSupported) {
                        throw new PermissionDeniedException(String.format("From user currency type %s not supported ", fromCurrencyType.getName()));
                    }

                    Map<String, Object> transferFeeMap = calculateTransferFee(walletSetup, productAmount);
                    TransactionTaxType transferFeeType = (TransactionTaxType) transferFeeMap.get(FEE_TYPE_KEY);
                    Double transferFee = (Double) transferFeeMap.get(FEE_KEY);

                    Double setupAmount = productAmount + transferFee;

                    Map<String, Object> exchangeTransferFeeMap = calculateExchangeTransferFee(walletSetup, setupAmount);
                    Double exchangeTransferFee = (Double) exchangeTransferFeeMap.get(FEE_KEY);
                    TransactionTaxType exchangeTransferFeeTaxType = (TransactionTaxType) exchangeTransferFeeMap.get(FEE_TYPE_KEY);

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("isDefault", 1);
                    params.put("toCurrency", fromCurrencyTypeId);
                    params.put("oneCurrency", setupCurrencyTypeId);
                    params.put("lastOne", 1);

                    List<ExchangeRate> exchangeRates = exchangeRateManager.getByParams(params);
                    if (Utils.isEmpty(exchangeRates)) {
                        String msg = "Could not found exchangeRates, with params, params = " + params;
                        throw new InternalErrorException(msg);
                    }

                    ExchangeRate selectedExchangeRate = exchangeRates.get(0);
                    Double rateAmount = selectedExchangeRate.getBuy();

                    setupAmount += exchangeTransferFee;

                    Double transferFeePrice = transferFee * rateAmount;
                    Double exchangeTransferFeePrice = exchangeTransferFee * rateAmount;
                    Double setupAmountPrice = setupAmount * rateAmount;

                    Double totalTax = transferFee + exchangeTransferFee;
                    Double totalTaxPrice = transferFeePrice + exchangeTransferFeePrice;

                    Double totalAmount = productAmount + totalTax;

                    Exchange transferFeeExchange = new Exchange(transferFee, setupCurrencyType, rateAmount, setupCurrencyType, transferFeePrice, fromCurrencyType, currentDate, fromWalletId, selectedExchangeRate.getId());
                    Exchange exchangeFeeExchange = new Exchange(exchangeTransferFee, setupCurrencyType, rateAmount, setupCurrencyType, exchangeTransferFeePrice, fromCurrencyType, currentDate, fromWalletId, selectedExchangeRate.getId());
                    Exchange productExchange = new Exchange(setupAmount, setupCurrencyType, rateAmount, setupCurrencyType, setupAmountPrice, fromCurrencyType, currentDate, fromWalletId, selectedExchangeRate.getId());

                    //Long walletId, Long walletSetupId, Date paymentDate,
                    // Double totalSetupTax, Double totalSetupTaxPrice, Exchange totalSetupTaxExchange,
                    // Double transferTax, Double transferTaxPrice, TransactionTaxType transferTaxType, Exchange transferTaxExchange,
                    // Double transferExchangeTax, Double transferExchangeTaxPrice, TransactionTaxType transferExchangeTaxType, Exchange transferExchangeTaxExchange,
                    // Double transferExchangeToSetupCurrencyTax, Double transferExchangeToSetupCurrencyTaxPrice, TransactionTaxType transferExchangeToSetupCurrencTaxType, Exchange transferExchangeToSetupCurrencyTaxExchange) {
                    TransactionTax transactionTax = new TransactionTax(fromWalletId, walletSetup.getId(), currentDate,
                            totalTax, totalTaxPrice,
                            transferFee, transferFeePrice, transferFeeType, transferFeeExchange,
                            exchangeTransferFee, exchangeTransferFeePrice, exchangeTransferFeeTaxType, exchangeFeeExchange,
                            null,null,null,null,
                            null, false);

                    //TransactionProcess(TransactionState transactionState, Date actionDate, Long walletId, Long walletSetupId, Double setupAmount, CurrencyType setupAmountCurrencyType, Exchange setupAmountExchange, Double priceAmount,  CurrencyType priceAmountCurrencyType, Exchange priceAmountExchange, TransactionTax tax, TransactionTax taxForExchange) {

                    //TransactionState transactionState, Date actionDate, Wallet wallet, WalletSetup walletSetup,
                    //Double frozenAmount, Double totalAmount, Double totalAmountPrice, Exchange totalAmountExchange,
                    //Double transferExchangeToSetupCurrency, Double transferExchangeToSetupCurrencyPrice, Exchange transferExchangeToSetupCurrencyExchange, Exchange transferExchangeToSetupCurrencyPriceExchange,
                    // TransactionTax tax) {
                    TransactionProcess transactionAction = new TransactionProcess(currentDate, fromWalletId, walletSetup.getId(),
                            totalAmount, totalAmount, setupAmountPrice, productExchange, null, null,
                            transactionTax);
                    transaction.setFromTransactionProcess(transactionAction);
                    transaction.setFromTotalPrice(setupAmountPrice);
                    transaction.setFromTotalPriceCurrencyType(fromWallet.getCurrencyType());
                    transaction.setSetupAmount(productAmount);
                }
            }

            if (toWallet != null) {
                CurrencyType toCurrencyType = toWallet.getCurrencyType();
                int toCurrencyTypeId = toWallet.getCurrencyType().getId();
                Long toWalletId = toWallet.getId();

                if (productCurrencyTypeId == toCurrencyTypeId) {
                    Map<String, Object> receiverFeeMap = calculateReceiverFee(walletSetup, productAmount);
                    Double receiverFee = (Double) receiverFeeMap.get(FEE_KEY);
                    TransactionTaxType receiverFeeType = (TransactionTaxType) receiverFeeMap.get(FEE_TYPE_KEY);

                    Double totalAmount = productAmount - receiverFee;
                    /*if (totalAmount <= 0) {
                        throw new InternalErrorException(MESSAGE_LESS_REQUEST);
                    }*/

                    TransactionTax transactionTax = new TransactionTax(toWalletId, walletSetup.getId(), currentDate,
                            receiverFee, receiverFee,
                            receiverFee, receiverFee, receiverFeeType, null,
                            null, null, null, null,
                            null, null, null, null,
                            null, false);
                    //TransactionState transactionState, Date actionDate, Wallet wallet, WalletSetup walletSetup,
                    //Double frozenAmount, Double totalAmount, Double totalAmountPrice, Exchange totalAmountExchange,
                    //Double transferExchangeToSetupCurrency, Double transferExchangeToSetupCurrencyPrice, Exchange transferExchangeToSetupCurrencyExchange, Exchange transferExchangeToSetupCurrencyPriceExchange,
                    // TransactionTax tax) {
                    TransactionProcess transactionAction = new TransactionProcess(currentDate, toWalletId, walletSetup.getId(),
                            productAmount, totalAmount, totalAmount, null, null, null,
                            transactionTax);

                    transaction.setToTransactionProcess(transactionAction);
                    transaction.setToTotalPrice(totalAmount);
                    transaction.setToTotalPriceCurrencyType(toWallet.getCurrencyType());
                } else {

                    boolean isToCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(toCurrencyTypeId);
                    if (!isToCurrencyTypeSupported) {
                        throw new PermissionDeniedException(String.format("To user currency type %s not supported ", toCurrencyType.getName()));
                    }
                    Map<String, Object> receiverFeeMap = calculateReceiverFee(walletSetup, productAmount);
                    Double receiverFee = (Double) receiverFeeMap.get(FEE_KEY);
                    TransactionTaxType receiverFeeType = (TransactionTaxType) receiverFeeMap.get(FEE_TYPE_KEY);

                    Double totalAmount = productAmount - receiverFee;

                    Map<String, Object> exchangeReceiverFeeMap = calculateExchangeReceiverFee(walletSetup, totalAmount);
                    Double exchangeReceiverFee = (Double) exchangeReceiverFeeMap.get(FEE_KEY);
                    TransactionTaxType exchangeReceiverFeeType = (TransactionTaxType) exchangeReceiverFeeMap.get(FEE_TYPE_KEY);

                    totalAmount -= exchangeReceiverFee;

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("isDefault", 1);
                    params.put("toCurrency", toCurrencyTypeId);
                    params.put("oneCurrency", setupCurrencyTypeId);
                    params.put("lastOne", 1);

                    List<ExchangeRate> exchangeRates = exchangeRateManager.getByParams(params);
                    if (Utils.isEmpty(exchangeRates)) {
                        String msg = "Could not found exchangeRates, with params, params = " + params;
                        throw new InternalErrorException(msg);
                    }

                    ExchangeRate selectedExchangeRate = exchangeRates.get(0);
                    Double rateAmount = selectedExchangeRate.getBuy();

                    Double totalTax = receiverFee + exchangeReceiverFee;
                    Double totalAmountPrice = totalAmount * rateAmount;

                    Double receiverFeePrice = receiverFee * rateAmount;
                    Double exchangeReceiverFeePrice = exchangeReceiverFee * rateAmount;
                    Double totalTaxPrice = totalTax * rateAmount;


                    /*if (totalAmountPrice <= 0) {
                        throw new InternalErrorException(MESSAGE_LESS_REQUEST);
                    }*/

                    Exchange transferFeeExchange = new Exchange(receiverFee, setupCurrencyType, rateAmount, setupCurrencyType, receiverFeePrice, toCurrencyType, currentDate, toWalletId, selectedExchangeRate.getId());
                    Exchange exchangeFeeExchange = new Exchange(exchangeReceiverFee, setupCurrencyType, rateAmount, setupCurrencyType, exchangeReceiverFeePrice, toCurrencyType, currentDate, toWalletId, selectedExchangeRate.getId());
                    Exchange totalAmountExchange = new Exchange(totalAmount, setupCurrencyType, rateAmount, setupCurrencyType, totalAmountPrice, toCurrencyType, currentDate, toWalletId, selectedExchangeRate.getId());

                    TransactionTax transactionTax = new TransactionTax(toWalletId, walletSetup.getId(), currentDate,
                            totalTax, totalTaxPrice,
                            receiverFee, receiverFeePrice, receiverFeeType, transferFeeExchange,
                            exchangeReceiverFee, exchangeReceiverFeePrice, exchangeReceiverFeeType, exchangeFeeExchange,
                            null, null, null, null,
                            null, false);

                    //TransactionState transactionState, Date actionDate, Wallet wallet, WalletSetup walletSetup,
                    //Double frozenAmount, Double totalAmount, Double totalAmountPrice, Exchange totalAmountExchange,
                    //Double transferExchangeToSetupCurrency, Double transferExchangeToSetupCurrencyPrice, Exchange transferExchangeToSetupCurrencyExchange, Exchange transferExchangeToSetupCurrencyPriceExchange,
                    // TransactionTax tax) {
                    TransactionProcess transactionAction = new TransactionProcess(currentDate, toWalletId, walletSetup.getId(),
                            totalAmount, totalAmount, totalAmountPrice, totalAmountExchange, null, null,
                            transactionTax);

                    transaction.setToTransactionProcess(transactionAction);
                    transaction.setToTotalPrice(totalAmountPrice);
                    transaction.setToTotalPriceCurrencyType(toWallet.getCurrencyType());
                }
            }
        }
        //</editor-fold>
        else {

            if (fromWallet != null) {

                CurrencyType fromCurrencyType = fromWallet.getCurrencyType();
                int fromCurrencyTypeId = fromWallet.getCurrencyType().getId();
                Long fromWalletId = fromWallet.getId();

                //<editor-fold desc="productCurrencyTypeId == fromCurrencyTypeId">
                if (productCurrencyTypeId == fromCurrencyTypeId) {

                    boolean isFromCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(fromCurrencyTypeId);
                    if (!isFromCurrencyTypeSupported) {
                        throw new PermissionDeniedException(String.format("From user currency type %s not supported ", fromCurrencyType.getName()));
                    }

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("isDefault", 1);
                    params.put("toCurrency", fromCurrencyTypeId);
                    params.put("oneCurrency", setupCurrencyTypeId);
                    params.put("lastOne", 1);

                    List<ExchangeRate> exchangeRates = exchangeRateManager.getByParams(params);
                    if (Utils.isEmpty(exchangeRates)) {
                        String msg = "Could not found exchangeRates, with params, params = " + params;
                        throw new InternalErrorException(msg);
                    }

                    ExchangeRate selectedExchangeRate = exchangeRates.get(0);
                    Double rateAmount = selectedExchangeRate.getBuy();

                    Double transferExchangeToSetupCurrency = productAmount / rateAmount;

                    Map<String, Object> transferFeeMap = calculateTransferFee(walletSetup, transferExchangeToSetupCurrency);
                    TransactionTaxType transferFeeType = (TransactionTaxType) transferFeeMap.get(FEE_TYPE_KEY);
                    Double transferFee = (Double) transferFeeMap.get(FEE_KEY);

                    Double setupAmount = transferExchangeToSetupCurrency + transferFee;

                    Map<String, Object> exchangeTransferFeeMap = calculateExchangeTransferFee(walletSetup, setupAmount);
                    Double exchangeTransferFee = (Double) exchangeTransferFeeMap.get(FEE_KEY);
                    TransactionTaxType exchangeTransferFeeTaxType = (TransactionTaxType) exchangeTransferFeeMap.get(FEE_TYPE_KEY);

                    setupAmount += exchangeTransferFee;
                    Double transferFeePrice = transferFee * rateAmount;
                    Double exchangeTransferFeePrice = exchangeTransferFee * rateAmount;

                    Double totalTax = transferFee + exchangeTransferFee;
                    Double totalTaxPrice = transferFeePrice + exchangeTransferFeePrice;

                    Double totalAmount = transferExchangeToSetupCurrency + totalTax;
                    Double totalAmountPrice = productAmount + totalTaxPrice;

                    Exchange transferFeeExchange = new Exchange(transferFee, setupCurrencyType, rateAmount, setupCurrencyType, transferFeePrice, fromCurrencyType, currentDate, fromWalletId, selectedExchangeRate.getId());
                    Exchange exchangeFeeExchange = new Exchange(exchangeTransferFee, setupCurrencyType, rateAmount, setupCurrencyType, exchangeTransferFeePrice, fromCurrencyType, currentDate, fromWalletId, selectedExchangeRate.getId());
                    Exchange productExchange = new Exchange(transferExchangeToSetupCurrency, setupCurrencyType, rateAmount, setupCurrencyType, productAmount, fromCurrencyType, currentDate, fromWalletId, selectedExchangeRate.getId());

                    //Long walletId, Long walletSetupId, Date paymentDate,
                    // Double totalSetupTax, Double totalSetupTaxPrice, Exchange totalSetupTaxExchange,
                    // Double transferTax, Double transferTaxPrice, TransactionTaxType transferTaxType, Exchange transferTaxExchange,
                    // Double transferExchangeTax, Double transferExchangeTaxPrice, TransactionTaxType transferExchangeTaxType, Exchange transferExchangeTaxExchange,
                    // Double transferExchangeToSetupCurrencyTax, Double transferExchangeToSetupCurrencyTaxPrice, TransactionTaxType transferExchangeToSetupCurrencTaxType, Exchange transferExchangeToSetupCurrencyTaxExchange) {
                    TransactionTax transactionTax = new TransactionTax(fromWalletId, walletSetup.getId(), currentDate,
                            totalTax, totalTaxPrice,
                            transferFee, transferFeePrice, transferFeeType, transferFeeExchange,
                            exchangeTransferFee, exchangeTransferFeePrice, exchangeTransferFeeTaxType, exchangeFeeExchange,
                            null, null, null, null,
                            null, false);

                    //TransactionProcess(TransactionState transactionState, Date actionDate, Long walletId, Long walletSetupId, Double setupAmount, CurrencyType setupAmountCurrencyType, Exchange setupAmountExchange, Double priceAmount,  CurrencyType priceAmountCurrencyType, Exchange priceAmountExchange, TransactionTax tax, TransactionTax taxForExchange) {

                    //TransactionState transactionState, Date actionDate, Wallet wallet, WalletSetup walletSetup,
                    //Double frozenAmount, Double totalAmount, Double totalAmountPrice, Exchange totalAmountExchange,
                    //Double transferExchangeToSetupCurrency, Double transferExchangeToSetupCurrencyPrice, Exchange transferExchangeToSetupCurrencyExchange, Exchange transferExchangeToSetupCurrencyPriceExchange,
                    // TransactionTax tax) {
                    TransactionProcess transactionAction = new TransactionProcess(currentDate, fromWalletId, walletSetup.getId(),
                            totalAmountPrice, totalAmount, totalAmountPrice, productExchange, null, null,
                            transactionTax);
                    transaction.setFromTransactionProcess(transactionAction);
                    transaction.setFromTotalPrice(totalAmountPrice);
                    transaction.setFromTotalPriceCurrencyType(fromWallet.getCurrencyType());
                    transaction.setSetupAmount(totalAmount);

                }
                //</editor-fold>
                else {
                    throw new PermissionDeniedException(MESSAGE_NOT_SUPPORTED_CURRENCY);
                }
            }

            if (toWallet != null) {
                CurrencyType toCurrencyType = toWallet.getCurrencyType();
                int toCurrencyTypeId = toWallet.getCurrencyType().getId();
                Long toWalletId = toWallet.getId();
                //<editor-fold desc="productCurrencyTypeId == toCurrencyTypeId">
                if (productCurrencyTypeId == toCurrencyTypeId) {

                    boolean isToCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(toCurrencyTypeId);
                    if (!isToCurrencyTypeSupported) {
                        throw new PermissionDeniedException(String.format("To user currency type %s not supported ", toCurrencyType.getName()));
                    }

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("isDefault", 1);
                    params.put("toCurrency", toCurrencyTypeId);
                    params.put("oneCurrency", setupCurrencyTypeId);
                    params.put("lastOne", 1);

                    List<ExchangeRate> exchangeRates = exchangeRateManager.getByParams(params);
                    if (Utils.isEmpty(exchangeRates)) {
                        String msg = "Could not found exchangeRates, with params, params = " + params;
                        throw new InternalErrorException(msg);
                    }

                    ExchangeRate selectedExchangeRate = exchangeRates.get(0);
                    Double rateAmount = selectedExchangeRate.getBuy();

                    Double receiverExchangeToSetupCurrency = productAmount / rateAmount;

                    Map<String, Object> receiverFeeMap = calculateReceiverFee(walletSetup, receiverExchangeToSetupCurrency);
                    TransactionTaxType receiverFeeType = (TransactionTaxType) receiverFeeMap.get(FEE_TYPE_KEY);
                    Double receiverFee = (Double) receiverFeeMap.get(FEE_KEY);

                    Double setupAmount = receiverExchangeToSetupCurrency - receiverFee;

                    Map<String, Object> exchangeReceiverFeeMap = calculateExchangeReceiverFee(walletSetup, setupAmount);
                    Double exchangeReceiverFee = (Double) exchangeReceiverFeeMap.get(FEE_KEY);
                    TransactionTaxType exchangeReceiverFeeType = (TransactionTaxType) exchangeReceiverFeeMap.get(FEE_TYPE_KEY);

                    setupAmount -= exchangeReceiverFee;

                    Double receiverFeePrice = receiverFee * rateAmount;
                    Double exchangeReceiverFeePrice = exchangeReceiverFee * rateAmount;

                    Double totalTax = receiverFee + exchangeReceiverFee;
                    Double totalTaxPrice = receiverFeePrice + exchangeReceiverFeePrice;

                    Double totalAmount = receiverExchangeToSetupCurrency + totalTax;
                    Double totalAmountPrice = productAmount - totalTaxPrice;
                    /*if (totalAmountPrice <= 0) {
                        throw new InternalErrorException(MESSAGE_LESS_REQUEST);
                    }*/

                    Exchange receiverFeeExchange = new Exchange(receiverFee, setupCurrencyType, rateAmount, setupCurrencyType, receiverFeePrice, toCurrencyType, currentDate, toWalletId, selectedExchangeRate.getId());
                    Exchange exchangeFeeExchange = new Exchange(exchangeReceiverFee, setupCurrencyType, rateAmount, setupCurrencyType, exchangeReceiverFeePrice, toCurrencyType, currentDate, toWalletId, selectedExchangeRate.getId());
                    Exchange productExchange = new Exchange(receiverExchangeToSetupCurrency, setupCurrencyType, rateAmount, setupCurrencyType, productAmount, toCurrencyType, currentDate, toWalletId, selectedExchangeRate.getId());

                    //Long walletId, Long walletSetupId, Date paymentDate,
                    // Double totalSetupTax, Double totalSetupTaxPrice, Exchange totalSetupTaxExchange,
                    // Double transferTax, Double transferTaxPrice, TransactionTaxType transferTaxType, Exchange transferTaxExchange,
                    // Double transferExchangeTax, Double transferExchangeTaxPrice, TransactionTaxType transferExchangeTaxType, Exchange transferExchangeTaxExchange,
                    // Double transferExchangeToSetupCurrencyTax, Double transferExchangeToSetupCurrencyTaxPrice, TransactionTaxType transferExchangeToSetupCurrencTaxType, Exchange transferExchangeToSetupCurrencyTaxExchange) {
                    TransactionTax transactionTax = new TransactionTax(toWalletId, walletSetup.getId(), currentDate,
                            totalTax, totalTaxPrice,
                            receiverFee, receiverFeePrice, receiverFeeType, receiverFeeExchange,
                            exchangeReceiverFee, exchangeReceiverFeePrice, exchangeReceiverFeeType, exchangeFeeExchange,
                            null, null, null, null,
                            null, false);

                    //TransactionProcess(TransactionState transactionState, Date actionDate, Long walletId, Long walletSetupId, Double setupAmount, CurrencyType setupAmountCurrencyType, Exchange setupAmountExchange, Double priceAmount,  CurrencyType priceAmountCurrencyType, Exchange priceAmountExchange, TransactionTax tax, TransactionTax taxForExchange) {

                    //TransactionState transactionState, Date actionDate, Wallet wallet, WalletSetup walletSetup,
                    //Double frozenAmount, Double totalAmount, Double totalAmountPrice, Exchange totalAmountExchange,
                    //Double transferExchangeToSetupCurrency, Double transferExchangeToSetupCurrencyPrice, Exchange transferExchangeToSetupCurrencyExchange, Exchange transferExchangeToSetupCurrencyPriceExchange,
                    // TransactionTax tax) {
                    TransactionProcess transactionAction = new TransactionProcess(currentDate, toWalletId, walletSetup.getId(),
                            totalAmountPrice, totalAmount, totalAmountPrice, productExchange, null, null,
                            transactionTax);
                    transaction.setToTransactionProcess(transactionAction);
                    transaction.setToTotalPrice(totalAmountPrice);
                    transaction.setToTotalPriceCurrencyType(toWallet.getCurrencyType());
                    transaction.setSetupAmount(productAmount);

                }
                //</editor-fold>
                else {
                    throw new PermissionDeniedException(MESSAGE_NOT_SUPPORTED_CURRENCY);
                }
            }

        }

        transaction.setTransactionType(TransactionType.WALLET);
        return transaction;
    }


    protected synchronized void writeLog(User sesUser, Wallet wallet, Exception ex, LogLevel logLevel, LogAction logAction, String msg) {
        Date currentDate = new Date(System.currentTimeMillis());
        WalletLogger walletLogger;
        Long userId = sesUser == null ? null : sesUser.getId();
        Long walletId = wallet == null ? null : wallet.getId();
        if (ex != null) {
            logger.error(ex);
            walletLogger = new WalletLogger(userId, walletId, logLevel, TransactionShellActionOld.class.getName(), logAction, ex.getMessage(), currentDate);
        } else {
            logger.error(msg);
            walletLogger = new WalletLogger(userId, walletId, logLevel, TransactionShellActionOld.class.getName(), logAction, msg, currentDate);
        }
        session.put(ConstantGeneral.ERR_MESSAGE, msg);
        try {
            walletLoggerManager.add(walletLogger);
        } catch (InternalErrorException e) {
            logger.error(e);
        }
    }

    private static Map<String, Object> calculateTransferFee(WalletSetup walletSetup, Double amount) {

        Map<String, Object> transferFeeTypeMap = new HashMap<String, Object>();
        Double transferFee = 0d;
        Double transferPercentAmount = walletSetup.getTransferFeePercent() * amount / 100;

        if (transferPercentAmount < walletSetup.getTransferMinFee()) {
            transferFee = walletSetup.getTransferMinFee();
            transferFeeTypeMap.put(FEE_TYPE_KEY, TransactionTaxType.MIN);
        } else if (transferPercentAmount > walletSetup.getTransferMaxFee()) {
            transferFee = walletSetup.getTransferMaxFee();
            transferFeeTypeMap.put(FEE_TYPE_KEY, TransactionTaxType.MAX);
        } else {
            transferFee = transferPercentAmount;
            transferFeeTypeMap.put(FEE_TYPE_KEY, TransactionTaxType.PERCENT);
        }
        transferFeeTypeMap.put(FEE_KEY, transferFee);
        return transferFeeTypeMap;
    }

    private static Map<String, Object> calculateExchangeTransferFee(WalletSetup walletSetup, Double amount) {

        Map<String, Object> map = new HashMap<String, Object>();
        Double transferFeeExchange = 0d;
        Double exchangePercentAmount = walletSetup.getExchangeTransferFeePercent() * amount / 100;

        if (exchangePercentAmount < walletSetup.getExchangeTransferMinFee()) {
            transferFeeExchange = walletSetup.getExchangeTransferMinFee();
            map.put(FEE_TYPE_KEY, TransactionTaxType.MIN);
        } else if (exchangePercentAmount > walletSetup.getExchangeTransferMaxFee()) {
            transferFeeExchange = walletSetup.getExchangeTransferMaxFee();
            map.put(FEE_TYPE_KEY, TransactionTaxType.MAX);
        } else {
            transferFeeExchange = exchangePercentAmount;
            map.put(FEE_TYPE_KEY, TransactionTaxType.PERCENT);
        }
        map.put(FEE_KEY, transferFeeExchange);
        return map;
    }

    private static Map<String, Object> calculateReceiverFee(WalletSetup walletSetup, Double amount) {
        Map<String, Object> map = new HashMap<String, Object>();
        Double receiverFee = 0d;
        Double receiverPercentAmount = walletSetup.getReceiverFeePercent() * amount / 100;

        if (receiverPercentAmount < walletSetup.getReceiverMinFee()) {
            receiverFee = walletSetup.getReceiverMinFee();
            map.put(FEE_TYPE_KEY, TransactionTaxType.MIN);
        } else if (receiverPercentAmount > walletSetup.getReceiverMaxFee()) {
            receiverFee = walletSetup.getReceiverMaxFee();
            map.put(FEE_TYPE_KEY, TransactionTaxType.MAX);
        } else {
            receiverFee = receiverPercentAmount;
            map.put(FEE_TYPE_KEY, TransactionTaxType.PERCENT);
        }
        map.put(FEE_KEY, receiverFee);
        return map;
    }

    private static Map<String, Object> calculateExchangeReceiverFee(WalletSetup walletSetup, Double amount) {
        Map<String, Object> map = new HashMap<String, Object>();
        Double receiverExchangeFee = 0d;
        Double receiverExchangePercentAmount = walletSetup.getExchangeReceiverFeePercent() * amount / 100;

        if (receiverExchangePercentAmount < walletSetup.getExchangeReceiverMinFee()) {
            receiverExchangeFee = walletSetup.getExchangeReceiverMinFee();
            map.put(FEE_TYPE_KEY, TransactionTaxType.MIN);
        } else if (receiverExchangePercentAmount > walletSetup.getExchangeReceiverMaxFee()) {
            receiverExchangeFee = walletSetup.getExchangeReceiverMaxFee();
            map.put(FEE_TYPE_KEY, TransactionTaxType.MAX);
        } else {
            receiverExchangeFee = receiverExchangePercentAmount;
            map.put(FEE_TYPE_KEY, TransactionTaxType.PERCENT);
        }
        map.put(FEE_KEY, receiverExchangeFee);
        return map;
    }

    protected void prepareCriteria(Map<String, Object> params) {
        //must be exists
        params.put("sendMoneyId", TransactionState.SEND_MONEY.getId());//11
        params.put("requestTransactionId", TransactionState.REQUEST_TRANSACTION.getId());//12
        switch (transactionType) {
            case 1:
                params.put("ttr", transactionType);
                break;
            case 2:
                params.put("tts", transactionType);
                break;
            default:
                params.put("tt", transactionType);
        }

        if (disputeState != 0) {
            params.put("disputeState", disputeState);
        }

        if (disputeState == -1) {
            params.put("disputeStateNY", disputeState);
        }

        if (!Utils.isEmpty(rangeDateGreat)) {
            Date rangeActionDateGreat = Utils.toSimpleDate(rangeDateGreat);
            params.put("rangeActionDateGreat", rangeActionDateGreat);
        }

        if (!Utils.isEmpty(rangeDateLess)) {
            Date rangeActionDateLess = Utils.toSimpleDate(rangeDateLess);
            params.put("rangeActionDateLess", rangeActionDateLess);
        }

        if (!Utils.isEmpty(searchLike)) {
            User user = (User) session.get(ConstantGeneral.SESSION_USER);
            params.put("searchLike", searchLike.trim().toLowerCase());
            params.put("userId", user.getId());
        }

        if (byCurrency != null) {
            params.put("byCurrency", byCurrency);
            if (rangeAmountGreat != null) {
                params.put("rangeAmountGreat", rangeAmountGreat);
            }
            if (rangeAmountLess != null) {
                params.put("rangeAmountLess", rangeAmountLess);
            }
        }
    }

    protected void prepareNotifierCriteria(Map<String, Object> params) {
        params.put("sendMoneyId", TransactionState.SEND_MONEY.getId());
        params.put("requestTransactionId", TransactionState.REQUEST_TRANSACTION.getId());
        params.put("strictStates", new Integer[]{TransactionState.PENDING.getId()});
    }

    public void setSearchLike(String searchLike) {
        this.searchLike = searchLike;
    }

    public void setRangeDateGreat(String rangeDateGreat) {
        this.rangeDateGreat = rangeDateGreat;
    }

    public void setRangeDateLess(String rangeDateLess) {
        this.rangeDateLess = rangeDateLess;
    }

    public void setByCurrency(String byCurrency) {
        try{
            Integer k = Integer.parseInt(byCurrency);
            this.byCurrency = CurrencyType.valueOf(k);
        }catch (Exception e){
            logger.warn(e);
        }
    }

    public void setRangeAmountGreat(Double rangeAmountGreat) {
        this.rangeAmountGreat = rangeAmountGreat;
    }

    public void setRangeAmountLess(Double rangeAmountLess) {
        this.rangeAmountLess = rangeAmountLess;
    }

    public void setTransactionType(int transactionType) {
        this.transactionType = transactionType;
    }

    public void setDisputeState(int disputeState) {
        this.disputeState = disputeState;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
