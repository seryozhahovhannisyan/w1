package com.connectto.wallet.model.transaction.merchant.deposit;

import java.util.Date;

/**
 * Created by Serozh on 11/2/2016.
 */
public class TransactionDepositTax {

    private Long id;
    private Date actionDate;

    private Long walletId;
    private Long setupId;
    //6 USD (6 + 4 ExT)
    private Double totalTax;
    //6 USD (6 + 4 ExT) USD * 480 AMD = 48000 MAD
    private Double totalTaxPrice;
    //process 2 USD
    private TransactionDepositProcessTax processTax;
    //exchange tax  0 or 4 USD
    private TransactionDepositExchangeTax exchangeTax;
    //3 USD
    private WalletSetupDepositTax setupDepositTax;
    //1 USD
    private MerchantDepositTax merchantDepositTax;

    private Long processTaxId;
    private Long exchangeTaxId;
    private Long setupDepositTaxId;
    private Long merchantDepositTaxId;

    private boolean isPaid;

    public TransactionDepositTax() {
    }

    public TransactionDepositTax(Date actionDate, Long walletId, Long setupId,
                                  TransactionDepositProcessTax processTax,
                                  WalletSetupDepositTax setupDepositTax,
                                  MerchantDepositTax merchantDepositTax) {
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;
        this.processTax = processTax;
        this.setupDepositTax = setupDepositTax;
        this.merchantDepositTax = merchantDepositTax;

        this.calculateTotalTax();
    }

    public TransactionDepositTax(Date actionDate, Long walletId, Long setupId,
                                  TransactionDepositProcessTax processTax,
                                  WalletSetupDepositTax setupDepositTax,
                                  MerchantDepositTax merchantDepositTax,
                                  TransactionDepositExchangeTax exchangeTax) {
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;
        this.processTax = processTax;
        this.setupDepositTax = setupDepositTax;
        this.merchantDepositTax = merchantDepositTax;
        this.exchangeTax = exchangeTax;
        this.calculateTotalTax();
    }

    private void calculateTotalTax() {

        this.totalTax = this.processTax.getProcessTax() + this.setupDepositTax.getDepositTax();//+ this.merchantDepositTax.getDepositTax();
        this.totalTaxPrice = this.processTax.getProcessTaxPrice() + this.setupDepositTax.getDepositTaxPrice();// + this.merchantDepositTax.getDepositTaxPrice();

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

    public TransactionDepositProcessTax getProcessTax() {
        return processTax;
    }

    public void setProcessTax(TransactionDepositProcessTax processTax) {
        this.processTax = processTax;
    }

    public TransactionDepositExchangeTax getExchangeTax() {
        return exchangeTax;
    }

    public void setExchangeTax(TransactionDepositExchangeTax exchangeTax) {
        this.exchangeTax = exchangeTax;
    }

    public WalletSetupDepositTax getSetupDepositTax() {
        return setupDepositTax;
    }

    public void setSetupDepositTax(WalletSetupDepositTax setupDepositTax) {
        this.setupDepositTax = setupDepositTax;
    }

    public MerchantDepositTax getMerchantDepositTax() {
        return merchantDepositTax;
    }

    public void setMerchantDepositTax(MerchantDepositTax merchantDepositTax) {
        this.merchantDepositTax = merchantDepositTax;
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

    public Long getSetupDepositTaxId() {
        return setupDepositTaxId;
    }

    public void setSetupDepositTaxId(Long setupDepositTaxId) {
        this.setupDepositTaxId = setupDepositTaxId;
    }

    public Long getMerchantDepositTaxId() {
        return merchantDepositTaxId;
    }

    public void setMerchantDepositTaxId(Long merchantDepositTaxId) {
        this.merchantDepositTaxId = merchantDepositTaxId;
    }

    public boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean paid) {
        isPaid = paid;
    }
}
