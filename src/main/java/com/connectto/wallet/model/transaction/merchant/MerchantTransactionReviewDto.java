package com.connectto.wallet.model.transaction.merchant;

import com.connectto.wallet.model.wallet.lcp.CurrencyType;

import java.util.Date;

/**
 * Created by Serozh on 12/25/2016.
 */
public class MerchantTransactionReviewDto {

    private String transactionId;
    private String actionDate;

    private String walletId;
    private String setupId;

    //94 USD = (94-4 ExT)* 480 AMD = 43200 AMD wallet total including all tax
    private String walletTotalPrice;
    private String walletCurrencyType;
    //2 ReT + 3 WsDT = 5 USD = (5+4 ExT) = 9 USD setup total including tax
    private String setupTotalAmount;
    private String setupCurrencyType;

    //

    /*
    * #################################################################################################################
    * ########################################        GETTER & SETTER       ###########################################
    * #################################################################################################################
    */

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getActionDate() {
        return actionDate;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

    public String getSetupId() {
        return setupId;
    }

    public void setSetupId(String setupId) {
        this.setupId = setupId;
    }

    public String getWalletTotalPrice() {
        return walletTotalPrice;
    }

    public void setWalletTotalPrice(String walletTotalPrice) {
        this.walletTotalPrice = walletTotalPrice;
    }

    public String getWalletCurrencyType() {
        return walletCurrencyType;
    }

    public void setWalletCurrencyType(String walletCurrencyType) {
        this.walletCurrencyType = walletCurrencyType;
    }

    public String getSetupTotalAmount() {
        return setupTotalAmount;
    }

    public void setSetupTotalAmount(String setupTotalAmount) {
        this.setupTotalAmount = setupTotalAmount;
    }

    public String getSetupCurrencyType() {
        return setupCurrencyType;
    }

    public void setSetupCurrencyType(String setupCurrencyType) {
        this.setupCurrencyType = setupCurrencyType;
    }
}
