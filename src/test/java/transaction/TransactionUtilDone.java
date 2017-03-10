package transaction;

import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.WalletSetup;
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
 * Created by Serozh on 11/11/2016.
 */
public class TransactionUtilDone {

    private static final String TAX_KEY = "tax";
    private static final String TAX_TYPE_KEY = "type";
    private static final String MESSAGE_UNKNOWN_WALLETS = "Please choose receiver wallet or sender ";
    private static final String MESSAGE_LESS_MONEY = "Less money ";
    private static final String MESSAGE_LESS_REQUEST = "Aveli ";
    private static final String MESSAGE_NOT_SUPPORTED_CURRENCY = "The currency not supported ";

    public static void simpleTransactionPurchaseWithSameCurrencies(TransactionPurchase transactionPurchase,
                                                                   Wallet wallet,
                                                                   WalletSetup walletSetup,
                                                                   Double purchaseAmount,
                                                                   Double availableAmount, Date currentDate, TransactionState transactionState) throws InternalErrorException {

        Long walletId = wallet.getId();
        Long setupId = walletSetup.getId();
        CurrencyType currencyType = walletSetup.getCurrencyType();

        Map<String, Object> purchaseTaxTypeMap = calculatePurchaseTax(walletSetup, purchaseAmount);
        TransactionTaxType taxType = (TransactionTaxType) purchaseTaxTypeMap.get(TAX_TYPE_KEY);
        Double taxAmount = (Double) purchaseTaxTypeMap.get(TAX_KEY);

        Double totalAmount = purchaseAmount + taxAmount;
        if (availableAmount < totalAmount) {
            //todo toCreditCard +=
            throw new InternalErrorException(MESSAGE_LESS_MONEY);
        }

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


    public static void otherWalletCurrency(TransactionPurchase transactionPurchase,
                                           Wallet wallet,
                                           WalletSetup walletSetup,
                                           Double purchaseAmount,
                                           Double availableAmount, Date currentDate, TransactionState transactionState) throws InternalErrorException {

        ExchangeRate selectedExchangeRate = DemoCreator.initExchangeRate();//exchangeRates.get(0);
        Double rateAmount = selectedExchangeRate.getBuy();

        Long walletId = wallet.getId();
        Long setupId = walletSetup.getId();
        Long rateId = selectedExchangeRate.getId();

        CurrencyType walletCurrencyType = wallet.getCurrencyType();
        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();

        Double purchasePrice = purchaseAmount * rateAmount;//480.000AMD
        Map<String, Object> purchaseProcessTaxMap = TransactionUtilDone.calculatePurchaseTax(walletSetup, purchaseAmount);//1000 USD
        TransactionTaxType purchaseProcessTaxType = (TransactionTaxType) purchaseProcessTaxMap.get(TAX_TYPE_KEY);
        Double purchaseProcessTax = (Double) purchaseProcessTaxMap.get(TAX_KEY);//100 USD
        Double purchaseProcessTaxPrice = purchaseProcessTax * rateAmount;
        TransactionPurchaseExchangeTax purchaseProcessExchangeTax = new TransactionPurchaseExchangeTax(currentDate, walletId, setupId, purchaseProcessTax, setupCurrencyType, purchaseProcessTaxPrice, walletCurrencyType, purchaseProcessTaxType);
        TransactionPurchaseExchange purchaseProcessExchange = new TransactionPurchaseExchange(walletId, setupId, rateId, currentDate, purchaseProcessTax, setupCurrencyType, rateAmount, walletCurrencyType, purchaseProcessTaxPrice, walletCurrencyType, purchaseProcessExchangeTax);

        Map<String, Object> processTaxExchangeTaxMap = calculateExchangeTax(walletSetup, purchaseProcessTax);//100USD
        TransactionTaxType processTaxExchangeTaxType = (TransactionTaxType) processTaxExchangeTaxMap.get(TAX_TYPE_KEY);
        Double processTaxExchangeTaxTax = (Double) processTaxExchangeTaxMap.get(TAX_KEY);//10 USD
        Double processTaxExchangeTaxPrice = processTaxExchangeTaxTax * rateAmount;//4800AMD
        TransactionPurchaseExchangeTax processTaxExchangeTax = new TransactionPurchaseExchangeTax(currentDate, walletId, setupId, processTaxExchangeTaxTax, setupCurrencyType, processTaxExchangeTaxPrice, walletCurrencyType, processTaxExchangeTaxType);
        TransactionPurchaseExchange processTaxExchangeTaxExchange = new TransactionPurchaseExchange(walletId, setupId, rateId, currentDate, processTaxExchangeTaxTax, setupCurrencyType, rateAmount, walletCurrencyType, processTaxExchangeTaxPrice, walletCurrencyType, processTaxExchangeTax);

        Map<String, Object> exchangePurchaseMap = calculateExchangeTax(walletSetup, purchaseAmount);//1000 USD
        TransactionTaxType exchangePurchaseType = (TransactionTaxType) exchangePurchaseMap.get(TAX_TYPE_KEY);
        Double exchangePurchase = (Double) exchangePurchaseMap.get(TAX_KEY);//100 USD
        Double exchangePurchasePrice = exchangePurchase * rateAmount;//48.000 AMD
        TransactionPurchaseExchangeTax purchaseExchangeTax = new TransactionPurchaseExchangeTax(currentDate, walletId, setupId, exchangePurchase, setupCurrencyType, exchangePurchasePrice, walletCurrencyType, exchangePurchaseType);

        Double totalTaxAmount = purchaseProcessTax + exchangePurchase + processTaxExchangeTaxTax;
        Double totalTaxPrice = purchaseProcessTaxPrice + exchangePurchasePrice + processTaxExchangeTaxPrice;

        Double purchaseTotalAmount = purchaseAmount + totalTaxAmount;
        Double purchaseTotalPrice = purchasePrice + totalTaxPrice;

        if (availableAmount < purchaseTotalPrice) {
            throw new InternalErrorException(MESSAGE_LESS_MONEY);
        }


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

    public static void otherPurchaseCurrency(TransactionPurchase transactionPurchase,
                                             Wallet wallet,//USD
                                             WalletSetup walletSetup,//USD
                                             Double purchaseAmount, CurrencyType purchaseCurrencyType,//480.000 AMD
                                             Double availableAmount, Date currentDate, TransactionState transactionState) throws InternalErrorException {

        ExchangeRate selectedExchangeRate = DemoCreator.initExchangeRate();//exchangeRates.get(0);
        Double rateAmount = selectedExchangeRate.getBuy();//480

        Long walletId = wallet.getId();
        Long setupId = walletSetup.getId();
        Long rateId = selectedExchangeRate.getId();

        CurrencyType walletCurrencyType = wallet.getCurrencyType();
        CurrencyType setupCurrencyType = walletSetup.getCurrencyType();

        Double purchasePrice = purchaseAmount / rateAmount;//1.000 USD
        Map<String, Object> purchaseProcessTaxMap = TransactionUtilDone.calculatePurchaseTax(walletSetup, purchasePrice);//1000 USD
        TransactionTaxType purchaseProcessTaxType = (TransactionTaxType) purchaseProcessTaxMap.get(TAX_TYPE_KEY);
        Double purchaseProcessTax = (Double) purchaseProcessTaxMap.get(TAX_KEY);//100 USD
        TransactionPurchaseExchangeTax purchaseProcessExchangeTax = new TransactionPurchaseExchangeTax(currentDate, walletId, setupId, purchaseProcessTax, setupCurrencyType, purchaseProcessTax, walletCurrencyType, purchaseProcessTaxType);
        TransactionPurchaseExchange purchaseProcessExchange = new TransactionPurchaseExchange(walletId, setupId, rateId, currentDate, purchasePrice, setupCurrencyType, rateAmount, walletCurrencyType,
                purchaseAmount, purchaseCurrencyType, purchasePrice, walletCurrencyType, purchaseProcessExchangeTax);

        Map<String, Object> exchangePurchaseMap = TransactionUtilDone.calculateExchangeTax(walletSetup, purchasePrice);//1000 USD
        TransactionTaxType exchangePurchaseType = (TransactionTaxType) exchangePurchaseMap.get(TAX_TYPE_KEY);
        Double exchangePurchase = (Double) exchangePurchaseMap.get(TAX_KEY);//100 USD
        TransactionPurchaseExchangeTax purchaseExchangeTax = new TransactionPurchaseExchangeTax(currentDate, walletId, setupId, exchangePurchase, setupCurrencyType, exchangePurchase, walletCurrencyType, exchangePurchaseType);

        Double totalTaxAmount = purchaseProcessTax + exchangePurchase ;
        Double purchaseTotalAmount = purchasePrice + totalTaxAmount;

        if (availableAmount < purchaseTotalAmount) {
            throw new InternalErrorException(MESSAGE_LESS_MONEY);
        }

        TransactionPurchaseProcessTax processTax = new TransactionPurchaseProcessTax(currentDate, walletId, setupId, purchaseProcessTax, setupCurrencyType, purchaseProcessTax, walletCurrencyType, purchaseProcessTaxType, null);
        TransactionPurchaseTax purchaseTax = new TransactionPurchaseTax(currentDate, walletId, setupId,
                processTax, purchaseExchangeTax);

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

    public static Map<String, Object> calculateExchangeTax(WalletSetup walletSetup, Double amount) {

        Map<String, Object> map = new HashMap<String, Object>();
        Double purchesTaxExchange = 0d;
        Double exchangePercentAmount = walletSetup.getExchangeTransferFeePercent() * amount / 100;

        if (exchangePercentAmount < walletSetup.getExchangeTransferMinFee()) {
            purchesTaxExchange = walletSetup.getExchangeTransferMinFee();
            map.put(TAX_TYPE_KEY, TransactionTaxType.MIN);
        } else if (exchangePercentAmount > walletSetup.getExchangeTransferMaxFee()) {
            purchesTaxExchange = walletSetup.getExchangeTransferMaxFee();
            map.put(TAX_TYPE_KEY, TransactionTaxType.MAX);
        } else {
            purchesTaxExchange = exchangePercentAmount;
            map.put(TAX_TYPE_KEY, TransactionTaxType.PERCENT);
        }
        map.put(TAX_KEY, purchesTaxExchange);
        return map;
    }

    public static Map<String, Object> calculatePurchaseTax(WalletSetup walletSetup, Double amount) {

        Map<String, Object> purchaseTaxTypeMap = new HashMap<String, Object>();
        Double purchaseTax = 0d;
        Double purchasePercentAmount = walletSetup.getTransferFeePercent() * amount / 100;

        if (purchasePercentAmount < walletSetup.getTransferMinFee()) {
            purchaseTax = walletSetup.getTransferMinFee();
            purchaseTaxTypeMap.put(TAX_TYPE_KEY, TransactionTaxType.MIN);
        } else if (purchasePercentAmount > walletSetup.getTransferMaxFee()) {
            purchaseTax = walletSetup.getTransferMaxFee();
            purchaseTaxTypeMap.put(TAX_TYPE_KEY, TransactionTaxType.MAX);
        } else {
            purchaseTax = purchasePercentAmount;
            purchaseTaxTypeMap.put(TAX_TYPE_KEY, TransactionTaxType.PERCENT);
        }
        purchaseTaxTypeMap.put(TAX_KEY, purchaseTax);
        return purchaseTaxTypeMap;
    }

}
