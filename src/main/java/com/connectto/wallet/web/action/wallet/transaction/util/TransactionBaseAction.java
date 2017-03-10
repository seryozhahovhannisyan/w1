package com.connectto.wallet.web.action.wallet.transaction.util;

import com.connectto.general.exception.InternalErrorException;
import com.connectto.general.model.ResponseDto;
import com.connectto.general.model.WalletSetup;
import com.connectto.general.util.ShellAction;
import com.connectto.general.util.Utils;
import com.connectto.wallet.dataaccess.service.wallet.IExchangeRateManager;
import com.connectto.wallet.encryption.EncryptException;
import com.connectto.wallet.model.wallet.ExchangeRate;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionTaxType;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Serozh on 11/17/2016.
 */
public class TransactionBaseAction extends ShellAction {

    private static final Logger logger = Logger.getLogger(TransactionBaseAction.class.getSimpleName());

    protected final String TAX_KEY = "tax";
    protected final String TAX_TYPE_KEY = "type";

    protected ResponseDto responseDto;
    private IExchangeRateManager exchangeRateManager;

    protected String sessionId;
    //
    protected String amount;
    protected String currencyType;
    protected Double productAmount;
    protected CurrencyType productCurrencyType;

    protected synchronized ExchangeRate getDefaultExchangeRate(CurrencyType toCurrency, CurrencyType oneCurrency) throws InternalErrorException{
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("isDefault", 1);
        params.put("toCurrency", toCurrency.getId());
        params.put("oneCurrency", oneCurrency.getId());
        params.put("lastOne", 1);

        List<ExchangeRate> exchangeRates = exchangeRateManager.getByParams(params);
        if (Utils.isEmpty(exchangeRates)) {
            String msg = "Could not found exchangeRates, with params, params = " + params;
            throw new InternalErrorException(msg);
        }

        return exchangeRates.get(0);
    }

    protected synchronized boolean convertAmountAndCurrency(boolean decripted) {

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
            if (decripted) {
                amount = TransactionDecripter.decript(amount);
            }
            productAmount = Double.parseDouble(amount);
        } catch (NumberFormatException e) {
            String msg = getText("wallet.back.end.message.empty.currencyType") + " ," + getText("wallet.payment.label.Amount") + "=" + amount;
            amount = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        } catch (EncryptException e) {
            String msg = getText("wallet.back.end.message.encription");
            amount = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        }

        try {

            if (decripted) {
                currencyType = TransactionDecripter.decript(currencyType);
            }

            int type = Integer.parseInt(currencyType);
            productCurrencyType = CurrencyType.valueOf(type);
        } catch (NumberFormatException e) {
            String msg = getText("wallet.back.end.message.empty.incorrect.currencyType") + " ," + getText("wallet.back.end.message.currencyType") + "=" + currencyType;
            currencyType = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        } catch (EncryptException e) {
            String msg = getText("wallet.back.end.message.encription");
            currencyType = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        }

        return valid;
    }

    protected Map<String, Object> calculateTransferTax(WalletSetup walletSetup, Double amount) {

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

    protected Map<String, Object> calculateTransferExchangeTax(WalletSetup walletSetup, Double amount) {

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

    protected Map<String, Object> calculateReceiverTax(WalletSetup walletSetup, Double amount) {
        Map<String, Object> map = new HashMap<String, Object>();
        Double receiverFee = 0d;
        Double receiverPercentAmount = walletSetup.getReceiverFeePercent() * amount / 100;

        if (receiverPercentAmount < walletSetup.getReceiverMinFee()) {
            receiverFee = walletSetup.getReceiverMinFee();
            map.put(TAX_TYPE_KEY, TransactionTaxType.MIN);
        } else if (receiverPercentAmount > walletSetup.getReceiverMaxFee()) {
            receiverFee = walletSetup.getReceiverMaxFee();
            map.put(TAX_TYPE_KEY, TransactionTaxType.MAX);
        } else {
            receiverFee = receiverPercentAmount;
            map.put(TAX_TYPE_KEY, TransactionTaxType.PERCENT);
        }
        map.put(TAX_KEY, receiverFee);
        return map;
    }

    protected Map<String, Object> calculateReceiverExchangeTax(WalletSetup walletSetup, Double amount) {
        Map<String, Object> map = new HashMap<String, Object>();
        Double receiverExchangeFee = 0d;
        Double receiverExchangePercentAmount = walletSetup.getExchangeReceiverFeePercent() * amount / 100;

        if (receiverExchangePercentAmount < walletSetup.getExchangeReceiverMinFee()) {
            receiverExchangeFee = walletSetup.getExchangeReceiverMinFee();
            map.put(TAX_TYPE_KEY, TransactionTaxType.MIN);
        } else if (receiverExchangePercentAmount > walletSetup.getExchangeReceiverMaxFee()) {
            receiverExchangeFee = walletSetup.getExchangeReceiverMaxFee();
            map.put(TAX_TYPE_KEY, TransactionTaxType.MAX);
        } else {
            receiverExchangeFee = receiverExchangePercentAmount;
            map.put(TAX_TYPE_KEY, TransactionTaxType.PERCENT);
        }
        map.put(TAX_KEY, receiverExchangeFee);
        return map;
    }



    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public void setResponseDto(ResponseDto responseDto) {
        this.responseDto = responseDto;
    }

    public void setExchangeRateManager(IExchangeRateManager exchangeRateManager) {
        this.exchangeRateManager = exchangeRateManager;
    }

}
