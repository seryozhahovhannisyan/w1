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
public class TransactionMerchantAction extends TransactionBaseAction{

    private static final Logger logger = Logger.getLogger(TransactionMerchantAction.class.getSimpleName());
    //
    protected String tax;
    protected String taxCurrencyType;
    protected String taxType;
    protected String rationalDuration;

    protected Double paidTaxToMerchant;
    protected CurrencyType paidTaxCurrencyType;
    protected TransactionTaxType paidTaxType;
    protected Long rationalSecondsDuration;

    protected synchronized boolean convertMerchantTaxAndTaxCurrency(boolean decripted) {

        boolean valid = true;

        if (Utils.isEmpty(tax)) {
            valid = false;
            String msg = getText("wallet.back.end.message.empty.tax");
            responseDto.addMessage(msg);
        }

        if (Utils.isEmpty(taxCurrencyType)) {
            valid = false;
            String msg = getText("wallet.back.end.message.empty.taxCurrencyType");
            responseDto.addMessage(msg);
        }

        if (Utils.isEmpty(taxType)) {
            valid = false;
            String msg = getText("wallet.back.end.message.empty.taxType");
            responseDto.addMessage(msg);
        }
        if (Utils.isEmpty(rationalDuration)) {
            valid = false;
            String msg = getText("wallet.back.end.message.empty.rationalDuration");
            responseDto.addMessage(msg);
        }

        if (!valid) {
            return valid;
        }

        try {
            if (decripted) {
                tax = TransactionDecripter.decript(tax);
            }
            paidTaxToMerchant = Double.parseDouble(tax);
        } catch (NumberFormatException e) {
            String msg = getText("wallet.back.end.message.empty.currencyType") + " ," + getText("wallet.payment.label.tax") + "=" + tax;
            tax = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        } catch (EncryptException e) {
            String msg = getText("wallet.back.end.message.encription");
            tax = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        }

        try {

            if (decripted) {
                taxCurrencyType = TransactionDecripter.decript(taxCurrencyType);
            }

            int type = Integer.parseInt(taxCurrencyType);
            paidTaxCurrencyType = CurrencyType.valueOf(type);
        } catch (NumberFormatException e) {
            String msg = getText("wallet.back.end.message.empty.incorrect.currencyType") + " ," + getText("wallet.back.end.message.currencyType") + "=" + taxCurrencyType;
            taxCurrencyType = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        } catch (EncryptException e) {
            String msg = getText("wallet.back.end.message.encription");
            taxCurrencyType = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        }

        try {

            if (decripted) {
                taxType = TransactionDecripter.decript(taxType);
            }

            int type = Integer.parseInt(taxType);
            paidTaxType = TransactionTaxType.valueOf(type);
        } catch (NumberFormatException e) {
            String msg = getText("wallet.back.end.message.empty.incorrect.paidTaxType") + " ," + getText("wallet.back.end.message.paidTaxType") + "=" + taxType;
            taxType = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        } catch (EncryptException e) {
            String msg = getText("wallet.back.end.message.encription");
            taxType = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        }

        try {

            if (decripted) {
                rationalDuration = TransactionDecripter.decript(rationalDuration);
            }

            rationalSecondsDuration = Long.parseLong(rationalDuration);
        } catch (NumberFormatException e) {
            String msg = getText("wallet.back.end.message.empty.rationalDuration") + "=" + rationalDuration;
            taxType = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        } catch (EncryptException e) {
            String msg = getText("wallet.back.end.message.encription");
            taxType = null;
            logger.error(e);
            valid = false;
            responseDto.addMessage(msg);
        }

        return valid;
    }

    // Merchant Tax

    protected Map<String, Object> calculateDepositTax(WalletSetup walletSetup, Double amount) {
        Map<String, Object> map = new HashMap<String, Object>();
        Double fee = 0d;
        Double percentAmount = walletSetup.getMerchantDepositFeePercent() * amount / 100;

        if (percentAmount < walletSetup.getMerchantDepositMinFee()) {
            fee = walletSetup.getMerchantDepositMinFee();
            map.put(TAX_TYPE_KEY, TransactionTaxType.MIN);
        } else if (percentAmount > walletSetup.getMerchantDepositMaxFee()) {
            fee = walletSetup.getMerchantDepositMaxFee();
            map.put(TAX_TYPE_KEY, TransactionTaxType.MAX);
        } else {
            fee = percentAmount;
            map.put(TAX_TYPE_KEY, TransactionTaxType.PERCENT);
        }
        map.put(TAX_KEY, fee);
        return map;
    }



    protected Map<String, Object> calculateWithdrawTax(WalletSetup walletSetup, Double amount) {
        Map<String, Object> map = new HashMap<String, Object>();
        Double fee = 0d;
        Double percentAmount = walletSetup.getMerchantWithdrawFeePercent() * amount / 100;

        if (percentAmount < walletSetup.getMerchantWithdrawMinFee()) {
            fee = walletSetup.getMerchantWithdrawMinFee();
            map.put(TAX_TYPE_KEY, TransactionTaxType.MIN);
        } else if (percentAmount > walletSetup.getMerchantWithdrawMaxFee()) {
            fee = walletSetup.getMerchantWithdrawMaxFee();
            map.put(TAX_TYPE_KEY, TransactionTaxType.MAX);
        } else {
            fee = percentAmount;
            map.put(TAX_TYPE_KEY, TransactionTaxType.PERCENT);
        }
        map.put(TAX_KEY, fee);
        return map;
    }



    public void setTax(String tax) {
        this.tax = tax;
    }

    public void setTaxCurrencyType(String taxCurrencyType) {
        this.taxCurrencyType = taxCurrencyType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public void setRationalDuration(String rationalDuration) {
        this.rationalDuration = rationalDuration;
    }
}
