package com.connectto.wallet.model.transaction.merchant.withdraw;

import java.util.Date;

/**
 * Created by Serozh on 11/2/2016.
 */
public class TransactionWithdrawTax {

    private Long id;
    private Date actionDate;

    private Long walletId;
    private Long setupId;
    //6 USD (6 + 4 ExT)
    private Double totalTax;
    //6 USD (6 + 4 ExT) USD * 480 AMD = 48000 MAD
    private Double totalTaxPrice;
    //process 2 USD
    private TransactionWithdrawProcessTax processTax;
    //exchange tax  0 or 4 USD
    private TransactionWithdrawExchangeTax exchangeTax;
    //3 USD
    private WalletSetupWithdrawTax setupWithdrawTax;
    //1 USD
    private MerchantWithdrawTax merchantWithdrawTax;

    private Long processTaxId;
    private Long exchangeTaxId;
    private Long setupWithdrawTaxId;
    private Long merchantWithdrawTaxId;

    private boolean isPaid;

    public TransactionWithdrawTax() {
    }

    public TransactionWithdrawTax(Date actionDate, Long walletId, Long setupId,
                                 TransactionWithdrawProcessTax processTax,
                                 WalletSetupWithdrawTax setupWithdrawTax,
                                 MerchantWithdrawTax merchantWithdrawTax) {
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;
        this.processTax = processTax;
        this.setupWithdrawTax = setupWithdrawTax;
        this.merchantWithdrawTax = merchantWithdrawTax;

        this.calculateTotalTax();
    }

    public TransactionWithdrawTax(Date actionDate, Long walletId, Long setupId,
                                 TransactionWithdrawProcessTax processTax,
                                 WalletSetupWithdrawTax setupWithdrawTax,
                                 MerchantWithdrawTax merchantWithdrawTax,
                                 TransactionWithdrawExchangeTax exchangeTax) {
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;
        this.processTax = processTax;
        this.setupWithdrawTax = setupWithdrawTax;
        this.merchantWithdrawTax = merchantWithdrawTax;
        this.exchangeTax = exchangeTax;
        this.calculateTotalTax();
    }

    private void calculateTotalTax() {

        this.totalTax = this.processTax.getProcessTax() + this.setupWithdrawTax.getWithdrawTax();//+ this.merchantWithdrawTax.getWithdrawTax();
        this.totalTaxPrice = this.processTax.getProcessTaxPrice() + this.setupWithdrawTax.getWithdrawTaxPrice();// + this.merchantWithdrawTax.getWithdrawTaxPrice();

        if (this.exchangeTax != null) {
            this.totalTax += this.exchangeTax.getExchangeTax();
            this.totalTaxPrice += this.exchangeTax.getExchangeTaxPrice();
        }

    }

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

    public Long getSetupId() {
        return setupId;
    }

    public void setSetupId(Long setupId) {
        this.setupId = setupId;
    }

    public Double getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(Double totalTax) {
        this.totalTax = totalTax;
    }

    public Double getTotalTaxPrice() {
        return totalTaxPrice;
    }

    public void setTotalTaxPrice(Double totalTaxPrice) {
        this.totalTaxPrice = totalTaxPrice;
    }

    public TransactionWithdrawProcessTax getProcessTax() {
        return processTax;
    }

    public void setProcessTax(TransactionWithdrawProcessTax processTax) {
        this.processTax = processTax;
    }

    public TransactionWithdrawExchangeTax getExchangeTax() {
        return exchangeTax;
    }

    public void setExchangeTax(TransactionWithdrawExchangeTax exchangeTax) {
        this.exchangeTax = exchangeTax;
    }

    public WalletSetupWithdrawTax getSetupWithdrawTax() {
        return setupWithdrawTax;
    }

    public void setSetupWithdrawTax(WalletSetupWithdrawTax setupWithdrawTax) {
        this.setupWithdrawTax = setupWithdrawTax;
    }

    public MerchantWithdrawTax getMerchantWithdrawTax() {
        return merchantWithdrawTax;
    }

    public void setMerchantWithdrawTax(MerchantWithdrawTax merchantWithdrawTax) {
        this.merchantWithdrawTax = merchantWithdrawTax;
    }

    public Long getProcessTaxId() {
        return processTaxId;
    }

    public void setProcessTaxId(Long processTaxId) {
        this.processTaxId = processTaxId;
    }

    public Long getExchangeTaxId() {
        return exchangeTaxId;
    }

    public void setExchangeTaxId(Long exchangeTaxId) {
        this.exchangeTaxId = exchangeTaxId;
    }

    public Long getSetupWithdrawTaxId() {
        return setupWithdrawTaxId;
    }

    public void setSetupWithdrawTaxId(Long setupWithdrawTaxId) {
        this.setupWithdrawTaxId = setupWithdrawTaxId;
    }

    public Long getMerchantWithdrawTaxId() {
        return merchantWithdrawTaxId;
    }

    public void setMerchantWithdrawTaxId(Long merchantWithdrawTaxId) {
        this.merchantWithdrawTaxId = merchantWithdrawTaxId;
    }

    public boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean paid) {
        isPaid = paid;
    }
}
