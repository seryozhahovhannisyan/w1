package transaction;


import com.connectto.general.exception.EntityNotFoundException;
import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.exception.InvalidParameterException;
import com.connectto.general.exception.PermissionDeniedException;
import com.connectto.general.model.WalletSetup;
import com.connectto.general.util.resourse.BeanProvider;
import com.connectto.wallet.dataaccess.service.transaction.merchant.ITransactionWithdrawManager;
import com.connectto.wallet.model.transaction.merchant.withdraw.TransactionWithdraw;
import com.connectto.wallet.model.transaction.purchase.*;
import com.connectto.wallet.model.wallet.ExchangeRate;
import com.connectto.wallet.model.wallet.Wallet;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionState;
import com.connectto.wallet.model.wallet.lcp.TransactionTaxType;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Serozh on 11/10/2016.
 */
public class TransactionUtil {

    private static final String TAX_KEY = "tax";
    private static final String TAX_TYPE_KEY = "type";
    private static final String MESSAGE_UNKNOWN_WALLETS = "Please choose receiver wallet or sender ";
    private static final String MESSAGE_LESS_MONEY = "Less money ";
    private static final String MESSAGE_LESS_REQUEST = "Aveli ";
    private static final String MESSAGE_NOT_SUPPORTED_CURRENCY = "The currency not supported ";
    //
    private static int partitionId = 14;
    private static String userId = "436";
    private static String purchaseAmount = "480000";
    private static String purchaseCurrencyType = "" + CurrencyType.AMD.getId();
//    private static String purchaseAmount = "1000";
//    private static String purchaseCurrencyType = "" + CurrencyType.USD.getId();

    private static String sessionId = "TU03W9QGWYO0NN11L5RO28DMCHS73QP0";

    public static void main(String[] args) throws InvalidParameterException, PermissionDeniedException, InternalErrorException, EntityNotFoundException {
//        ITransactionWithdrawManager withdrawManager = BeanProvider.getTransactionWithdrawManager();
//        Map<String,Object> map = new HashMap<String,Object>();
//        map.put("walletId", 25);
//        map.put("finalState", TransactionState.PENDING.getId());
//
//        TransactionWithdraw transactionWithdraw = withdrawManager.getUniqueByParams(map);
//        System.out.println(transactionWithdraw);
    }

    public static TransactionPurchase initDemoTransactionPurchase()  throws InvalidParameterException, PermissionDeniedException, InternalErrorException {
        Wallet wallet = DemoCreator.initWallet(CurrencyType.USD, userId);
        WalletSetup walletSetup = DemoCreator.initWalletSetup(CurrencyType.USD, partitionId);
        PurchaseTicket purchaseTicket = DemoCreator.initPurchase();
        Double pAmount = Double.parseDouble(purchaseAmount);
        CurrencyType pCurrencyType = CurrencyType.valueOf(Integer.parseInt(purchaseCurrencyType));
        TransactionPurchase transactionPurchase = createTransaction(pAmount, pCurrencyType, wallet, walletSetup, purchaseTicket, TransactionState.PURCHASE_FREEZE, sessionId);
        transactionPurchase.setFinalState(TransactionState.PURCHASE_FREEZE);
        transactionPurchase.setIsEncode(false);
        transactionPurchase.setMessage("msg");
        transactionPurchase.setOrderCode("orderCode");
        return transactionPurchase;
    }


    public static TransactionPurchase createTransaction(
            Double purchaseAmount, CurrencyType purchaseCurrencyType,
            Wallet wallet, WalletSetup walletSetup, PurchaseTicket purchaseTicket,
            TransactionState transactionState,
            String sessionId
    ) throws InternalErrorException, PermissionDeniedException {

        //<editor-fold desc="initBlock">
        Date currentDate = new Date(System.currentTimeMillis());


        if (wallet == null || walletSetup == null) {
            throw new InternalErrorException(MESSAGE_UNKNOWN_WALLETS);
        }

        boolean isPurchaseCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(purchaseCurrencyType.getId());
        if (!isPurchaseCurrencyTypeSupported) {
            throw new PermissionDeniedException(String.format("The selected currency type %s not supported ", purchaseCurrencyType.getName()));
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
        transactionPurchase.setWalletId(wallet.getId());
        transactionPurchase.addTicket(purchaseTicket);

        boolean isFromCurrencyTypeSupported = walletSetup.isCurrencyTypeSupported(walletCurrencyTypeId);
        if (!isFromCurrencyTypeSupported) {
            throw new PermissionDeniedException(String.format("Wallet currency type %s not supported ", walletCurrencyType.getName()));
        }

        Double currentBalance = wallet.getMoney();
        Double frozenAmount = wallet.getFrozenAmount();

        Double availableAmount = currentBalance - frozenAmount;
        if (availableAmount <= 0) {
            //todo open credit card balance, count += all and download from credit card
            throw new InternalErrorException(MESSAGE_LESS_MONEY);
        }
        //</editor-fold>

        if (purchaseCurrencyTypeId == setupCurrencyTypeId) {
            if (purchaseCurrencyTypeId == walletCurrencyTypeId) {
                TransactionUtilDone.simpleTransactionPurchaseWithSameCurrencies(transactionPurchase, wallet, walletSetup, purchaseAmount, availableAmount, currentDate, transactionState);
            } else {
                TransactionUtilDone.otherWalletCurrency(transactionPurchase, wallet, walletSetup, purchaseAmount, availableAmount, currentDate, transactionState);
            }
        } else {
            //<editor-fold desc="elseBlock">

            if (setupCurrencyTypeId == walletCurrencyTypeId) {
                TransactionUtilDone.otherPurchaseCurrency(transactionPurchase, wallet, walletSetup, purchaseAmount, purchaseCurrencyType, availableAmount, currentDate, transactionState);
            } else if (purchaseCurrencyTypeId == walletCurrencyTypeId){
                otherSetupCurrency(transactionPurchase, wallet, walletSetup, purchaseAmount, availableAmount, currentDate, transactionState);
            } else {
                throw new PermissionDeniedException("You could not use currency" + purchaseCurrencyType);
            }
            //</editor-fold>
        }
        return transactionPurchase;
    }


    public static void otherSetupCurrency(TransactionPurchase transactionPurchase,
                                           Wallet wallet,//AMD
                                           WalletSetup walletSetup,//USD
                                           Double purchaseAmount,//AMD
                                           Double availableAmount, Date currentDate, TransactionState transactionState) throws InternalErrorException {

        ExchangeRate selectedExchangeRate = DemoCreator.initExchangeRate();//exchangeRates.get(0);
        Double rateAmount = selectedExchangeRate.getBuy();

        Long walletId = wallet.getId();
        Long setupId = walletSetup.getId();
        Long rateId = selectedExchangeRate.getId();

        CurrencyType walletCurrencyType = wallet.getCurrencyType();
        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();

        Double setupAmount = purchaseAmount / rateAmount;//1000 USD
        Map<String, Object> purchaseProcessTaxMap = TransactionUtilDone.calculatePurchaseTax(walletSetup, setupAmount);//1000 USD
        TransactionTaxType purchaseProcessTaxType = (TransactionTaxType) purchaseProcessTaxMap.get(TAX_TYPE_KEY);
        Double purchaseProcessTax = (Double) purchaseProcessTaxMap.get(TAX_KEY);//100 USD
        Double purchaseProcessTaxPrice = purchaseProcessTax * rateAmount;
        TransactionPurchaseExchangeTax purchaseProcessExchangeTax = new TransactionPurchaseExchangeTax(currentDate, walletId, setupId, purchaseProcessTax, setupCurrencyType, purchaseProcessTaxPrice, walletCurrencyType, purchaseProcessTaxType);
        TransactionPurchaseExchange purchaseProcessExchange = new TransactionPurchaseExchange(walletId, setupId, rateId, currentDate, setupAmount, setupCurrencyType, rateAmount, walletCurrencyType, purchaseAmount, walletCurrencyType, purchaseProcessExchangeTax);

        Map<String, Object> processTaxExchangeTaxMap = TransactionUtilDone.calculateExchangeTax(walletSetup, purchaseProcessTax);//100USD
        TransactionTaxType processTaxExchangeTaxType = (TransactionTaxType) processTaxExchangeTaxMap.get(TAX_TYPE_KEY);
        Double processTaxExchangeTaxTax = (Double) processTaxExchangeTaxMap.get(TAX_KEY);//10 USD
        Double processTaxExchangeTaxPrice = processTaxExchangeTaxTax * rateAmount;//4800AMD
        TransactionPurchaseExchangeTax processTaxExchangeTax = new TransactionPurchaseExchangeTax(currentDate, walletId, setupId, processTaxExchangeTaxTax, setupCurrencyType, processTaxExchangeTaxPrice, walletCurrencyType, processTaxExchangeTaxType);
        TransactionPurchaseExchange processTaxExchangeTaxExchange = new TransactionPurchaseExchange(walletId, setupId, rateId, currentDate, processTaxExchangeTaxTax, setupCurrencyType, rateAmount, walletCurrencyType, processTaxExchangeTaxPrice, walletCurrencyType, processTaxExchangeTax);

        Map<String, Object> exchangePurchaseMap = TransactionUtilDone.calculateExchangeTax(walletSetup, setupAmount);//1000 USD
        TransactionTaxType exchangePurchaseType = (TransactionTaxType) exchangePurchaseMap.get(TAX_TYPE_KEY);
        Double exchangePurchase = (Double) exchangePurchaseMap.get(TAX_KEY);//100 USD
        Double exchangePurchasePrice = exchangePurchase * rateAmount;//48.000 AMD
        TransactionPurchaseExchangeTax purchaseExchangeTax = new TransactionPurchaseExchangeTax(currentDate, walletId, setupId, exchangePurchase, setupCurrencyType, exchangePurchasePrice, walletCurrencyType, exchangePurchaseType);

        Double totalTaxAmount = purchaseProcessTax + exchangePurchase + processTaxExchangeTaxTax;
        Double totalTaxPrice = purchaseProcessTaxPrice + exchangePurchasePrice + processTaxExchangeTaxPrice;

        Double purchaseTotalAmount = setupAmount+ totalTaxAmount;
        Double purchaseTotalPrice = purchaseAmount  + totalTaxPrice;

        if (availableAmount < purchaseTotalPrice) {
            throw new InternalErrorException(MESSAGE_LESS_MONEY);
        }

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

}
