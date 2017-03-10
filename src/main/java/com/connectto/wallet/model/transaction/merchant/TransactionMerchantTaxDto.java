package com.connectto.wallet.model.transaction.merchant;

import com.connectto.wallet.model.wallet.lcp.CurrencyType;

import java.util.Date;

/**
 * Created by Serozh on 12/25/2016.
 */
public class TransactionMerchantTaxDto {

    private Long id;
    private Date actionDate;
    //Many to one
    private Long walletId;
    private CurrencyType walletCurrencyType;

    private Long setupId;
    private CurrencyType setupCurrencyType;
    //800 USD
    private Double paidTaxToWalletSetup;

    //1 USD = 480 AMD
    private Double paidTaxToWalletSetupPrice;

    /*
    * #################################################################################################################
    * ########################################        GETTER & SETTER       ###########################################
    * #################################################################################################################
    */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public CurrencyType getWalletCurrencyType() {
        return walletCurrencyType;
    }

    public void setWalletCurrencyType(CurrencyType walletCurrencyType) {
        this.walletCurrencyType = walletCurrencyType;
    }

    public Long getSetupId() {
        return setupId;
    }

    public void setSetupId(Long setupId) {
        this.setupId = setupId;
    }

    public CurrencyType getSetupCurrencyType() {
        return setupCurrencyType;
    }

    public void setSetupCurrencyType(CurrencyType setupCurrencyType) {
        this.setupCurrencyType = setupCurrencyType;
    }

    public Double getPaidTaxToWalletSetup() {
        return paidTaxToWalletSetup;
    }

    public void setPaidTaxToWalletSetup(Double paidTaxToWalletSetup) {
        this.paidTaxToWalletSetup = paidTaxToWalletSetup;
    }

    public Double getPaidTaxToWalletSetupPrice() {
        return paidTaxToWalletSetupPrice;
    }

    public void setPaidTaxToWalletSetupPrice(Double paidTaxToWalletSetupPrice) {
        this.paidTaxToWalletSetupPrice = paidTaxToWalletSetupPrice;
    }
}
