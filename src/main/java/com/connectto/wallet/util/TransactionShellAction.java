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
import com.connectto.wallet.model.wallet.*;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionState;
import com.connectto.wallet.model.wallet.lcp.TransactionTaxType;
import com.connectto.wallet.model.wallet.lcp.TransactionType;
import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by Serozh on 2/15/16.
 */
public class TransactionShellAction extends ShellAction {


    private static final Logger logger = Logger.getLogger(TransactionShellAction.class.getSimpleName());

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
    protected String walletExchangeId;

    protected String sessionId;


    public void setExchangeRateManager(IExchangeRateManager exchangeRateManager) {
        this.exchangeRateManager = exchangeRateManager;
    }

    public void setWalletLoggerManager(IWalletLoggerManager walletLoggerManager) {
        this.walletLoggerManager = walletLoggerManager;
    }

    protected WalletExchange createWalletExchange(
            CurrencyType exchangeCurrencyType,
            Wallet myWallet,
            WalletSetup walletSetup,
            List<Transaction> pendingTransactions
    ) throws InternalErrorException, PermissionDeniedException {

        if (exchangeCurrencyType.getId() == myWallet.getId()){
            throw new PermissionDeniedException("You selected same currency");
        }

        //<editor-fold desc="initBlock">
        Date currentDate = new Date(System.currentTimeMillis());
        Long myWalletId = myWallet.getId();

        if (myWallet == null ) {
            throw new InternalErrorException(MESSAGE_UNKNOWN_WALLETS);
        }

        boolean isExchangeCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(exchangeCurrencyType.getId());
        if (!isExchangeCurrencyTypeSupported) {
            throw new PermissionDeniedException(String.format("The selected currency type %s not supported ", exchangeCurrencyType.getName()));
        }

        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();
        CurrencyType myCurrencyType = myWallet.getCurrencyType();

        int setupCurrencyTypeId = setupCurrencyType.getId();
        int myCurrencyTypeId = myCurrencyType.getId();
        int exchangeCurrencyTypeId = exchangeCurrencyType.getId();

        Double currentBalance = myWallet.getMoney();
        Double frozenAmount = myWallet.getFrozenAmount();
        Double receivingAmount = myWallet.getReceivingAmount();

        Double availableAmount = currentBalance - frozenAmount;
        if (availableAmount <= 0) {
            throw new InternalErrorException(MESSAGE_LESS_MONEY);
        }

        Double totalAmount = currentBalance + frozenAmount + receivingAmount;

        //</editor-fold>

        //todo frezze , recieved and other tax
        Map<String, Object> totalAmountFeeMap = calculateExchangeTransferFee(walletSetup, totalAmount);

        Double  totalExchangeFee = (Double) totalAmountFeeMap.get(FEE_KEY);
        TransactionTaxType  totalExchangeFeeTaxType = (TransactionTaxType) totalAmountFeeMap.get(FEE_TYPE_KEY);

        Exchange totalExchange = null;
        Double totalExchangeFeePrice = totalExchangeFee;

        if(exchangeCurrencyTypeId != setupCurrencyTypeId){
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("isDefault", 1);
            params.put("toCurrency", exchangeCurrencyTypeId);
            params.put("oneCurrency", setupCurrencyTypeId);
            params.put("lastOne", 1);

            List<ExchangeRate> exchangeRates = exchangeRateManager.getByParams(params);
            if (Utils.isEmpty(exchangeRates)) {
                String msg = "Could not found exchangeRates, with params, params = " + params;
                throw new InternalErrorException(msg);
            }

            ExchangeRate selectedExchangeRate = exchangeRates.get(0);
            Double rateAmount = selectedExchangeRate.getBuy();

            totalExchangeFeePrice = totalExchangeFee * rateAmount;
            totalExchange = new Exchange(totalExchangeFee, setupCurrencyType, rateAmount, setupCurrencyType, totalExchangeFeePrice, myCurrencyType, currentDate, myWalletId, selectedExchangeRate.getId());

        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("isDefault", 1);
        params.put("toCurrency", exchangeCurrencyTypeId);
        params.put("oneCurrency", myCurrencyTypeId);
        params.put("lastOne", 1);

        List<ExchangeRate> exchangeRates = exchangeRateManager.getByParams(params);
        if (Utils.isEmpty(exchangeRates)) {
            String msg = "Could not found exchangeRates, with params, params = " + params;
            throw new InternalErrorException(msg);
        }

        ExchangeRate selectedExchangeRate = exchangeRates.get(0);
        Double rateAmount = selectedExchangeRate.getBuy();

        Double newMoney = currentBalance - totalExchangeFee;
        if(newMoney < 0){
            String msg = "Current Balance and Exchange tax difference is less  0" ;
            throw new InternalErrorException(msg);
        }

        Double newMoneyExchanged = newMoney * rateAmount;
        Double frozenAmountExchanged = frozenAmount * rateAmount;
        Double receivingAmountExchanged = receivingAmount * rateAmount;


        Exchange moneyExchange = new Exchange(currentBalance, myCurrencyType, rateAmount, myCurrencyType, newMoneyExchanged, exchangeCurrencyType, currentDate, myWalletId, selectedExchangeRate.getId());
        Exchange frozenAmountExchange = new Exchange(frozenAmount, myCurrencyType, rateAmount, myCurrencyType, frozenAmountExchanged, exchangeCurrencyType, currentDate, myWalletId, selectedExchangeRate.getId());
        Exchange receivingAmountExchange = new Exchange(receivingAmount, myCurrencyType, rateAmount, myCurrencyType, receivingAmountExchanged, exchangeCurrencyType, currentDate, myWalletId, selectedExchangeRate.getId());

        List<WalletExchangePending> walletExchangePendings = new ArrayList<WalletExchangePending>();

        for(Transaction transaction : pendingTransactions){
            WalletExchangePending walletExchangePending = new WalletExchangePending();
            walletExchangePending.setTransactionId(transaction.getId());
            walletExchangePending.setWalletId(myWalletId);

            if (myWalletId.equals(transaction.getFromWalletId())){

                Double fromTotalPrice = transaction.getFromTotalPrice();
                Double newFromTotalPrice = fromTotalPrice * rateAmount;

                Exchange fromTotalPriceExchange = new Exchange(fromTotalPrice, myCurrencyType, rateAmount, myCurrencyType, newFromTotalPrice, exchangeCurrencyType, currentDate, myWalletId, selectedExchangeRate.getId());

                walletExchangePending.setNewFromTotalPrice(newFromTotalPrice);
                walletExchangePending.setNewFromTotalPriceCurrencyType(exchangeCurrencyType.getId());
                walletExchangePending.setOldFromTotalPrice(fromTotalPrice);
                walletExchangePending.setOldFromTotalPriceCurrencyType(myCurrencyType.getId());
                walletExchangePending.setFromTotalPriceExchange(fromTotalPriceExchange);


            } else if(myWalletId.equals(transaction.getToWalletId())){
                Double toTotalPrice = transaction.getToTotalPrice();
                Double newToTotalPrice = toTotalPrice * rateAmount;

                Exchange toTotalPriceExchange = new Exchange(toTotalPrice, myCurrencyType, rateAmount, myCurrencyType, newToTotalPrice, exchangeCurrencyType, currentDate, myWalletId, selectedExchangeRate.getId());

                walletExchangePending.setNewToTotalPrice(newToTotalPrice);
                walletExchangePending.setNewToTotalPriceCurrencyType(exchangeCurrencyType.getId());
                walletExchangePending.setOldToTotalPrice(toTotalPrice);
                walletExchangePending.setOldToTotalPriceCurrencyType(myCurrencyType.getId());
                walletExchangePending.setToTotalPriceExchange(toTotalPriceExchange);
            }
            walletExchangePendings.add(walletExchangePending);
        }

        WalletExchange walletExchange = new WalletExchange();
        walletExchange.setWalletId(myWallet.getId());
        walletExchange.setWalletSetupId(walletSetup.getId());
        walletExchange.setWallet(myWallet);
        walletExchange.setWalletExchangePendings(walletExchangePendings);

        walletExchange.setMoney(currentBalance);

        walletExchange.setNewMoneyPaidTax(newMoney);
        walletExchange.setNewMoneyPaidTaxPrice(newMoneyExchanged);
        walletExchange.setNewMoneyPaidTaxExchange(moneyExchange);

        walletExchange.setFrozenAmount(frozenAmount);
        walletExchange.setFrozenAmountPrice(frozenAmountExchanged);
        walletExchange.setFrozenAmountExchange(frozenAmountExchange);

        walletExchange.setReceivingAmount(receivingAmount);
        walletExchange.setReceivingAmountPrice(receivingAmountExchanged);
        walletExchange.setReceivingAmountExchange(receivingAmountExchange);

        walletExchange.setTotalTax(totalExchangeFeePrice);
        walletExchange.setTotalTaxPrice(totalExchangeFee);
        walletExchange.setTotalExchange(totalExchange);
        walletExchange.setTotalTaxType(totalExchangeFeeTaxType);

        walletExchange.setOldCurrencyType(myCurrencyType);
        walletExchange.setNewCurrencyType(exchangeCurrencyType);

        walletExchange.setExchangedAt(currentDate);

        return walletExchange;
    }

    protected Transaction createTransaction(
            Double productAmount, CurrencyType productCurrencyType,
            Wallet fromWallet,
            Wallet toWallet,
            WalletSetup walletSetup,
            TransactionState transactionState
    ) throws InternalErrorException, PermissionDeniedException {

        //<editor-fold desc="initBlock">
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

        boolean isProductCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(productCurrencyType.getId());
        if (!isProductCurrencyTypeSupported) {
            throw new PermissionDeniedException(String.format("The selected currency type %s not supported ", productCurrencyType.getName()));
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
            CurrencyType fct = fromWallet.getCurrencyType();
            boolean isFromCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(fct.getId());
            if (!isFromCurrencyTypeSupported) {
                throw new PermissionDeniedException(String.format("From user currency type %s not supported ", fct.getName()));
            }
        } else {
            transaction.setFromWalletSetupId(walletSetup.getId());
            transaction.setFromWalletSetup(walletSetup);
        }

        if (toWallet != null) {
            transaction.setToWalletId(toWallet.getId());
            transaction.setToWallet(toWallet);
            CurrencyType tct = toWallet.getCurrencyType();
            boolean isToCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(tct.getId());
            if (!isToCurrencyTypeSupported) {
                throw new PermissionDeniedException(String.format("To user currency type %s not supported ", tct.getName()));
            }
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
        //</editor-fold>

        if (productCurrencyTypeId == setupCurrencyTypeId) {
            if (fromWallet != null) {
                CurrencyType fromCurrencyType = fromWallet.getCurrencyType();
                int fromCurrencyTypeId = fromCurrencyType.getId();
                if (productCurrencyTypeId == fromCurrencyTypeId) {
                    equalsProductSetupFromCurrencies(transaction, fromWallet, walletSetup, productAmount, availableAmount, currentDate);
                } else {
                    equalsProductSetupDifFromCurrencies(transaction, fromWallet, walletSetup, productAmount, availableAmount, currentDate);
                }
            }

            if (toWallet != null) {

                CurrencyType toCurrencyType = toWallet.getCurrencyType();
                int toCurrencyTypeId = toCurrencyType.getId();

                if (productCurrencyTypeId == toCurrencyTypeId) {
                    equalsProductSetupToCurrencies(transaction, toWallet, walletSetup, productAmount, currentDate);
                } else {
                    equalsProductSetupDifToCurrencies(transaction, toWallet, walletSetup, productAmount, currentDate);
                }
            }
        } else {

            if (fromWallet != null) {

                Long fromWalletId = fromWallet.getId();

                CurrencyType fromCurrencyType = fromWallet.getCurrencyType();

                int fromCurrencyTypeId = fromCurrencyType.getId();

                if (productCurrencyTypeId == fromCurrencyTypeId) {
                    equalsProductFromCurrencies(transaction, fromWallet, walletSetup, productAmount, availableAmount, currentDate);
                } else {

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("isDefault", 1);
                    params.put("toCurrency", productCurrencyType.getId());
                    params.put("oneCurrency", setupCurrencyTypeId);
                    params.put("lastOne", 1);

                    List<ExchangeRate> exchangeRates = exchangeRateManager.getByParams(params);
                    if (Utils.isEmpty(exchangeRates)) {
                        String msg = "Could not found exchangeRates, with params, params = " + params;
                        throw new InternalErrorException(msg);
                    }

                    ExchangeRate selectedExchangeRate = exchangeRates.get(0);
                    Double rateAmount = selectedExchangeRate.getBuy();

                    Double overlayProduct = productAmount / rateAmount;

                    Map<String, Object> overlayExchangeMap = calculateExchangeTransferFee(walletSetup, overlayProduct);

                    Double overlayProductExchangeTax = (Double) overlayExchangeMap.get(FEE_KEY);
                    TransactionTaxType overlayProductTaxType = (TransactionTaxType) overlayExchangeMap.get(FEE_TYPE_KEY);

                    Double overlayProductTotal = overlayProduct + overlayProductExchangeTax;

                    Exchange overlayProductExchange = new Exchange(productAmount, productCurrencyType, rateAmount, setupCurrencyType, overlayProduct, setupCurrencyType, currentDate, fromWalletId, selectedExchangeRate.getId());

                    if (setupCurrencyTypeId == fromCurrencyTypeId) {
                        TransactionTax overlayTax = new TransactionTax(fromWalletId, walletSetup.getId(), currentDate,
                                null, null,
                                null, null, null, null,
                                null, null, null, null,
                                overlayProductExchangeTax, overlayProductExchangeTax, overlayProductTaxType, null,
                                null, false);

                        overlayTransactionTo(transaction, overlayTax, overlayProductExchange, fromWallet, walletSetup, overlayProduct, currentDate);

                    } else {

                        Map<String, Object> paramsPrice = new HashMap<String, Object>();
                        paramsPrice.put("isDefault", 1);
                        paramsPrice.put("toCurrency", setupCurrencyTypeId);
                        paramsPrice.put("oneCurrency", fromCurrencyTypeId);
                        paramsPrice.put("lastOne", 1);

                        List<ExchangeRate> exchangeRatesPrice = exchangeRateManager.getByParams(paramsPrice);
                        if (Utils.isEmpty(exchangeRatesPrice)) {
                            String msg = "Could not found exchangeRates, with params, params = " + paramsPrice;
                            throw new InternalErrorException(msg);
                        }

                        ExchangeRate selectedExchangeRatePrice = exchangeRatesPrice.get(0);
                        Double rateAmountPrice = selectedExchangeRatePrice.getBuy();

                        Double overlayProductPrice = overlayProduct / rateAmountPrice;
                        Double overlayProductTaxPrice = overlayProductExchangeTax / rateAmountPrice;

                        Exchange overlayProductPriceExchange = new Exchange(overlayProductPrice, fromCurrencyType, rateAmountPrice, fromCurrencyType, overlayProduct, setupCurrencyType, currentDate, fromWalletId, selectedExchangeRate.getId());
                        Exchange overlayProductTaxPriceExchange = new Exchange(overlayProductTaxPrice, fromCurrencyType, rateAmountPrice, fromCurrencyType, overlayProductExchangeTax, setupCurrencyType, currentDate, fromWalletId, selectedExchangeRate.getId());

                        TransactionTax overlayTax = new TransactionTax(fromWalletId, walletSetup.getId(), currentDate,
                                null, null,
                                null, null, null, null,
                                null, null, null, null,
                                overlayProductExchangeTax, overlayProductTaxPrice, overlayProductTaxType, overlayProductTaxPriceExchange,
                                null, false);

                        overlayTransactionFrom(transaction, overlayTax, overlayProductExchange, overlayProductPriceExchange, fromWallet, walletSetup, overlayProduct, availableAmount, currentDate);

                    }
                }
            }

            if (toWallet != null) {

                Long toWalletId = toWallet.getId();

                CurrencyType toCurrencyType = toWallet.getCurrencyType();
                int toCurrencyTypeId = toCurrencyType.getId();
                if (productCurrencyTypeId == toCurrencyTypeId) {
                    equalsProductToCurrencies(transaction, toWallet, walletSetup, productAmount, currentDate);
                } else {

                    Map<String, Object> params = new HashMap<String, Object>();
                    params.put("isDefault", 1);
                    params.put("toCurrency", productCurrencyType.getId());
                    params.put("oneCurrency", setupCurrencyTypeId);
                    params.put("lastOne", 1);

                    List<ExchangeRate> exchangeRates = exchangeRateManager.getByParams(params);
                    if (Utils.isEmpty(exchangeRates)) {
                        String msg = "Could not found exchangeRates, with params, params = " + params;
                        throw new InternalErrorException(msg);
                    }

                    ExchangeRate selectedExchangeRate = exchangeRates.get(0);
                    Double rateAmount = selectedExchangeRate.getBuy();

                    Double overlayProduct = productAmount / rateAmount;

                    Map<String, Object> overlayExchangeMap = calculateExchangeReceiverFee(walletSetup, overlayProduct);
                    Double overlayProductExchangeTax = (Double) overlayExchangeMap.get(FEE_KEY);
                    TransactionTaxType overlayProductExchangeTaxType = (TransactionTaxType) overlayExchangeMap.get(FEE_TYPE_KEY);

                    Double overlayExchangeTaxPrice = overlayProductExchangeTax * rateAmount;
                    Exchange overlayExchange = new Exchange(overlayProductExchangeTax, setupCurrencyType, rateAmount, setupCurrencyType, overlayExchangeTaxPrice, productCurrencyType, currentDate, toWalletId, selectedExchangeRate.getId());


                    if (setupCurrencyTypeId == toCurrencyTypeId) {
                        TransactionTax overlayTax = new TransactionTax(toWalletId, walletSetup.getId(), currentDate,
                                null, null,
                                null, null, null, null,
                                null, null, null, null,
                                overlayProductExchangeTax, overlayProductExchangeTax, overlayProductExchangeTaxType, null,
                                null, false);

                        overlayTransactionFrom(transaction, overlayTax, overlayExchange, fromWallet, walletSetup, overlayProduct, availableAmount, currentDate);

                    } else {

                        Map<String, Object> paramsPrice = new HashMap<String, Object>();
                        paramsPrice.put("isDefault", 1);
                        paramsPrice.put("toCurrency", setupCurrencyTypeId);
                        paramsPrice.put("oneCurrency", toCurrencyTypeId);
                        paramsPrice.put("lastOne", 1);

                        List<ExchangeRate> exchangeRatesPrice = exchangeRateManager.getByParams(paramsPrice);
                        if (Utils.isEmpty(exchangeRatesPrice)) {
                            String msg = "Could not found exchangeRates, with params, params = " + paramsPrice;
                            throw new InternalErrorException(msg);
                        }

                        ExchangeRate selectedExchangeRatePrice = exchangeRatesPrice.get(0);
                        Double rateAmountPrice = selectedExchangeRatePrice.getBuy();

                        Double overlayProductPrice = overlayProduct / rateAmountPrice;
                        Double overlayProductTaxPrice = overlayProductExchangeTax / rateAmountPrice;

                        Exchange overlayProductPriceExchange = new Exchange(overlayProductPrice, toCurrencyType, rateAmountPrice, toCurrencyType, overlayProduct, setupCurrencyType, currentDate, toWalletId, selectedExchangeRate.getId());
                        Exchange overlayProductTaxPriceExchange = new Exchange(overlayProductTaxPrice, toCurrencyType, rateAmountPrice, toCurrencyType, overlayProductExchangeTax, setupCurrencyType, currentDate, toWalletId, selectedExchangeRate.getId());

                        TransactionTax overlayTax = new TransactionTax(toWalletId, walletSetup.getId(), currentDate,
                                null, null,
                                null, null, null, null,
                                null, null, null, null,
                                overlayProductExchangeTax, overlayProductTaxPrice, overlayProductExchangeTaxType, overlayProductTaxPriceExchange,
                                null, false);

                        overlayTransactionTo(transaction, overlayTax, overlayExchange, overlayProductPriceExchange, toWallet, walletSetup, overlayProduct, currentDate);

                    }

                    /*TransactionTax transactionTax = new TransactionTax(toWalletId, walletSetup.getId(), currentDate,
                            null, null,
                            null, null, null, null,
                            null, null, null, null,
                            overlayExchangeTax, overlayExchangeTaxPrice, overlayExchangeTaxType, overlayExchange,
                            null, false);

                    overlayTransactionTo(transaction, transactionTax, toWallet, walletSetup, productAmount, currentDate);*/
                    //throw new PermissionDeniedException(MESSAGE_NOT_SUPPORTED_CURRENCY);
                }
            }
        }

        transaction.setTransactionType(TransactionType.WALLET);
        return transaction;
    }

    private  void equalsProductSetupFromCurrencies(Transaction transaction,
                                                         Wallet fromWallet,
                                                         WalletSetup walletSetup,
                                                         Double productAmount,
                                                         Double availableAmount, Date currentDate) throws InternalErrorException {

        Long fromWalletId = fromWallet.getId();

        Map<String, Object> transferFeeTypeMap = calculateTransferFee(walletSetup, productAmount);
        TransactionTaxType taxType = (TransactionTaxType) transferFeeTypeMap.get(FEE_TYPE_KEY);
        Double transferFee = (Double) transferFeeTypeMap.get(FEE_KEY);

        Double totalAmount = productAmount + transferFee;
        if (availableAmount < totalAmount) {
            throw new InternalErrorException(MESSAGE_LESS_MONEY);
        }

        TransactionTax transactionTax = new TransactionTax(fromWalletId, walletSetup.getId(), currentDate, transferFee, transferFee, transferFee, transferFee, taxType, null, null, null, null, null, null, null, null, null, null, false);
        TransactionProcess transactionAction = new TransactionProcess(currentDate, fromWalletId, walletSetup.getId(), productAmount, totalAmount, totalAmount, null, null, null, transactionTax);

        transaction.setFromTransactionProcess(transactionAction);
        transaction.setFromTotalPrice(totalAmount);
        transaction.setFromTotalPriceCurrencyType(fromWallet.getCurrencyType());
        transaction.setSetupAmount(productAmount);
    }

    private  void equalsProductSetupDifFromCurrencies(Transaction transaction,
                                                            Wallet fromWallet,
                                                            WalletSetup walletSetup,
                                                            Double productAmount,
                                                            Double availableAmount, Date currentDate) throws InternalErrorException {

        Long fromWalletId = fromWallet.getId();

        CurrencyType fromCurrencyType = fromWallet.getCurrencyType();
        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();

        int fromCurrencyTypeId = fromCurrencyType.getId();
        int setupCurrencyTypeId = setupCurrencyType.getId();

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
                exchangeTransferFee, exchangeTransferFeePrice, exchangeTransferFeeTaxType, exchangeFeeExchange,
                null, null, null, null,
                null, false);

        TransactionProcess transactionAction = new TransactionProcess(currentDate, fromWalletId, walletSetup.getId(),
                totalAmount, totalAmount, setupAmountPrice, productExchange, null, null,
                transactionTax);
        transaction.setFromTransactionProcess(transactionAction);
        transaction.setFromTotalPrice(setupAmountPrice);
        transaction.setFromTotalPriceCurrencyType(fromWallet.getCurrencyType());
        transaction.setSetupAmount(productAmount);
    }

    private  void equalsProductFromCurrencies(Transaction transaction,
                                                    Wallet fromWallet,
                                                    WalletSetup walletSetup,
                                                    Double productAmount,
                                                    Double availableAmount, Date currentDate) throws InternalErrorException {

        Long fromWalletId = fromWallet.getId();

        CurrencyType fromCurrencyType = fromWallet.getCurrencyType();
        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();

        int fromCurrencyTypeId = fromCurrencyType.getId();
        int setupCurrencyTypeId = setupCurrencyType.getId();

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

        TransactionTax transactionTax = new TransactionTax(fromWalletId, walletSetup.getId(), currentDate,
                totalTax, totalTaxPrice,
                transferFee, transferFeePrice, transferFeeType, transferFeeExchange,
                exchangeTransferFee, exchangeTransferFeePrice, exchangeTransferFeeTaxType, exchangeFeeExchange,
                null, null, null, null,
                null, false);

        TransactionProcess transactionAction = new TransactionProcess(currentDate, fromWalletId, walletSetup.getId(),
                totalAmountPrice, totalAmount, totalAmountPrice, productExchange, null, null,
                transactionTax);
        transaction.setFromTransactionProcess(transactionAction);
        transaction.setFromTotalPrice(totalAmountPrice);
        transaction.setFromTotalPriceCurrencyType(fromWallet.getCurrencyType());
        transaction.setSetupAmount(totalAmount);
    }

    private  void overlayTransactionFrom(Transaction transaction,
                                               TransactionTax overlayTax,
                                               Exchange overlayProductExchange,
                                               Wallet fromWallet,
                                               WalletSetup walletSetup,
                                               Double productAmount,
                                               Double availableAmount, Date currentDate) throws InternalErrorException {

        Double overlayExchangeTax = overlayTax.getOverlayExchangeTax();
        Double overlayExchangeTaxPrice = overlayTax.getOverlayExchangeTaxPrice();
        TransactionTaxType overlayExchangeTaxType = overlayTax.getOverlayExchangeTaxType();
        Exchange overlayExchange = overlayTax.getOverlayExchange();

        Long fromWalletId = fromWallet.getId();

        Map<String, Object> transferFeeTypeMap = calculateTransferFee(walletSetup, productAmount);
        TransactionTaxType taxType = (TransactionTaxType) transferFeeTypeMap.get(FEE_TYPE_KEY);
        Double transferFee = (Double) transferFeeTypeMap.get(FEE_KEY);

        Double totalAmount = productAmount + transferFee;
        if (availableAmount < totalAmount) {
            throw new InternalErrorException(MESSAGE_LESS_MONEY);
        }

        TransactionTax transactionTax = new TransactionTax(fromWalletId, walletSetup.getId(), currentDate, transferFee, transferFee, transferFee, transferFee, taxType, null, null, null, null, null,
                overlayExchangeTax, overlayExchangeTaxPrice, overlayExchangeTaxType, overlayExchange,
                null, false);
        TransactionProcess transactionAction = new TransactionProcess(currentDate, fromWalletId, walletSetup.getId(), productAmount, totalAmount, totalAmount, null, overlayProductExchange, null, transactionTax);

        transaction.setFromTransactionProcess(transactionAction);
        transaction.setFromTotalPrice(totalAmount);
        transaction.setFromTotalPriceCurrencyType(fromWallet.getCurrencyType());
        transaction.setSetupAmount(productAmount);
    }

    private  void overlayTransactionFrom(Transaction transaction,
                                               TransactionTax overlayTransactionTax,
                                               Exchange overlayProductExchange,
                                               Exchange overlayProductPriceExchange,
                                               Wallet fromWallet,
                                               WalletSetup walletSetup,
                                               Double productAmount,
                                               Double availableAmount, Date currentDate) throws InternalErrorException {

        Double overlayExchangeTax = overlayTransactionTax.getOverlayExchangeTax();
        Double overlayExchangeTaxPrice = overlayTransactionTax.getOverlayExchangeTaxPrice();
        TransactionTaxType overlayExchangeTaxType = overlayTransactionTax.getOverlayExchangeTaxType();
        Exchange overlayExchange = overlayTransactionTax.getOverlayExchange();

        Long fromWalletId = fromWallet.getId();

        CurrencyType fromCurrencyType = fromWallet.getCurrencyType();
        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();

        int fromCurrencyTypeId = fromCurrencyType.getId();
        int setupCurrencyTypeId = setupCurrencyType.getId();

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
                exchangeTransferFee, exchangeTransferFeePrice, exchangeTransferFeeTaxType, exchangeFeeExchange,
                overlayExchangeTax, overlayExchangeTaxPrice, overlayExchangeTaxType, overlayExchange,
                null, false);

        TransactionProcess transactionProcess = new TransactionProcess(currentDate, fromWalletId, walletSetup.getId(),
                totalAmount, totalAmount, setupAmountPrice, productExchange, overlayProductExchange, overlayProductPriceExchange,
                transactionTax);
        transaction.setFromTransactionProcess(transactionProcess);
        transaction.setFromTotalPrice(setupAmountPrice);
        transaction.setFromTotalPriceCurrencyType(fromWallet.getCurrencyType());
        transaction.setSetupAmount(productAmount);
    }


    private  void equalsProductSetupToCurrencies(Transaction transaction,
                                                       Wallet toWallet,
                                                       WalletSetup walletSetup,
                                                       Double productAmount,
                                                       Date currentDate) throws InternalErrorException {

        Long toWalletId = toWallet.getId();

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

        TransactionProcess transactionAction = new TransactionProcess(currentDate, toWalletId, walletSetup.getId(),
                productAmount, totalAmount, totalAmount, null, null, null,
                transactionTax);

        transaction.setToTransactionProcess(transactionAction);
        transaction.setToTotalPrice(totalAmount);
        transaction.setToTotalPriceCurrencyType(toWallet.getCurrencyType());
    }

    private  void equalsProductSetupDifToCurrencies(Transaction transaction,
                                                          Wallet toWallet,
                                                          WalletSetup walletSetup,
                                                          Double productAmount,
                                                          Date currentDate) throws InternalErrorException {

        Long toWalletId = toWallet.getId();

        CurrencyType toCurrencyType = toWallet.getCurrencyType();
        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();

        int toCurrencyTypeId = toCurrencyType.getId();
        int setupCurrencyTypeId = setupCurrencyType.getId();

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

        TransactionProcess transactionAction = new TransactionProcess(currentDate, toWalletId, walletSetup.getId(),
                totalAmount, totalAmount, totalAmountPrice, totalAmountExchange, null, null,
                transactionTax);

        transaction.setToTransactionProcess(transactionAction);
        transaction.setToTotalPrice(totalAmountPrice);
        transaction.setToTotalPriceCurrencyType(toWallet.getCurrencyType());

    }

    private  void equalsProductToCurrencies(Transaction transaction,
                                                  Wallet toWallet,
                                                  WalletSetup walletSetup,
                                                  Double productAmount,
                                                  Date currentDate) throws InternalErrorException {

        Long toWalletId = toWallet.getId();

        CurrencyType toCurrencyType = toWallet.getCurrencyType();
        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();

        int toCurrencyTypeId = toCurrencyType.getId();
        int setupCurrencyTypeId = setupCurrencyType.getId();

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

        TransactionProcess transactionAction = new TransactionProcess(currentDate, toWalletId, walletSetup.getId(),
                totalAmountPrice, totalAmount, totalAmountPrice, productExchange, null, null,
                transactionTax);
        transaction.setToTransactionProcess(transactionAction);
        transaction.setToTotalPrice(totalAmountPrice);
        transaction.setToTotalPriceCurrencyType(toWallet.getCurrencyType());
        transaction.setSetupAmount(productAmount);

    }

    private  void overlayTransactionTo(Transaction transaction,
                                             TransactionTax overlayTransactionTax,
                                             Exchange overlayProductExchange,
                                             Wallet toWallet,
                                             WalletSetup walletSetup,
                                             Double productAmount,
                                             Date currentDate) throws InternalErrorException {

        Double overlayExchangeTax = overlayTransactionTax.getOverlayExchangeTax();
        Double overlayExchangeTaxPrice = overlayTransactionTax.getOverlayExchangeTaxPrice();
        TransactionTaxType overlayExchangeTaxType = overlayTransactionTax.getOverlayExchangeTaxType();

        Long toWalletId = toWallet.getId();

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
                overlayExchangeTax, overlayExchangeTaxPrice, overlayExchangeTaxType, overlayProductExchange,
                null, false);

        TransactionProcess transactionAction = new TransactionProcess(currentDate, toWalletId, walletSetup.getId(),
                productAmount, totalAmount, totalAmount, null, overlayProductExchange, null,
                transactionTax);

        transaction.setToTransactionProcess(transactionAction);
        transaction.setToTotalPrice(totalAmount);
        transaction.setToTotalPriceCurrencyType(toWallet.getCurrencyType());
    }

    private  void overlayTransactionTo(Transaction transaction,
                                             TransactionTax overlayTransactionTax,
                                             Exchange overlayProductExchange,
                                             Exchange overlayProductPriceExchange,
                                             Wallet toWallet,
                                             WalletSetup walletSetup,
                                             Double productAmount,
                                             Date currentDate) throws InternalErrorException {

        Double overlayExchangeTax = overlayTransactionTax.getOverlayExchangeTax();
        Double overlayExchangeTaxPrice = overlayTransactionTax.getOverlayExchangeTaxPrice();
        TransactionTaxType overlayExchangeTaxType = overlayTransactionTax.getOverlayExchangeTaxType();


        Long toWalletId = toWallet.getId();

        CurrencyType toCurrencyType = toWallet.getCurrencyType();
        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();

        int toCurrencyTypeId = toCurrencyType.getId();
        int setupCurrencyTypeId = setupCurrencyType.getId();

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
                overlayExchangeTax, overlayExchangeTaxPrice, overlayExchangeTaxType, overlayProductExchange,
                null, false);

        TransactionProcess transactionAction = new TransactionProcess(currentDate, toWalletId, walletSetup.getId(),
                totalAmount, totalAmount, totalAmountPrice, totalAmountExchange, overlayProductExchange, overlayProductPriceExchange,
                transactionTax);

        transaction.setToTransactionProcess(transactionAction);
        transaction.setToTotalPrice(totalAmountPrice);
        transaction.setToTotalPriceCurrencyType(toWallet.getCurrencyType());

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

    private Map<String, Object> calculateExchangeTransferFee(WalletSetup walletSetup, Double amount) {

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
        }

        if (rangeAmountGreat != null) {
            params.put("rangeAmountGreat", rangeAmountGreat);
        }
        if (rangeAmountLess != null) {
            params.put("rangeAmountLess", rangeAmountLess);
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

    public String getWalletExchangeId() {
        return walletExchangeId;
    }

    public void setWalletExchangeId(String walletExchangeId) {
        this.walletExchangeId = walletExchangeId;
    }
}
