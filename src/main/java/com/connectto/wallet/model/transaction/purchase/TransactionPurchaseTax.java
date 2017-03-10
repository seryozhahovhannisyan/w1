package com.connectto.wallet.model.transaction.purchase;

import java.util.Date;

/**
 * Created by Serozh on 11/2/2016.
 */
public class TransactionPurchaseTax {

    private Long id;
    private Date actionDate;

    private Long walletId;
    private Long setupId;
    //210 USD
    private Double totalTax;
    //100.800 AMD
    private Double totalTaxPrice;
    //process 100 USD
    private TransactionPurchaseProcessTax processTax;
    //process tax exchange tax 10
    private TransactionPurchaseExchangeTax processTaxExchangeTax;
    //exchange tax  100 USD
    private TransactionPurchaseExchangeTax exchangeTax;

    private Long processTaxId;
    private Long processTaxExchangeTaxId;
    private Long exchangeTaxId;

    private boolean isPaid;

    public TransactionPurchaseTax() {
    }

    public TransactionPurchaseTax(Date actionDate, Long walletId, Long setupId, TransactionPurchaseProcessTax processTax) {
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;
        this.processTax = processTax; ;
        this.calculateTotalTax();
    }

    public TransactionPurchaseTax(Date actionDate, Long walletId, Long setupId, TransactionPurchaseProcessTax processTax, TransactionPurchaseExchangeTax exchangeTax) {
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;
        this.processTax = processTax;
        this.exchangeTax = exchangeTax;
        this.calculateTotalTax();
    }

    public TransactionPurchaseTax(Date actionDate, Long walletId, Long setupId, TransactionPurchaseProcessTax processTax, TransactionPurchaseExchangeTax processTaxExchangeTax, TransactionPurchaseExchangeTax exchangeTax) {
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;
        this.processTax = processTax;
        this.processTaxExchangeTax = processTaxExchangeTax;
        this.exchangeTax = exchangeTax;
        this.calculateTotalTax();
    }

    private void calculateTotalTax() {

        this.totalTax = this.processTax.getProcessTax();
        this.totalTaxPrice = this.processTax.getProcessTaxPrice();

        if (this.exchangeTax != null) {
            this.totalTax += this.exchangeTax.getExchangeTax();
            this.totalTaxPrice += this.exchangeTax.getExchangeTaxPrice();
        }

        if (this.processTaxExchangeTax != null) {
            this.totalTax += this.processTaxExchangeTax.getExchangeTax();
            this.totalTaxPrice += this.processTaxExchangeTax.getExchangeTaxPrice();
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

    public TransactionPurchaseProcessTax getProcessTax() {
        return processTax;
    }

    public void setProcessTax(TransactionPurchaseProcessTax processTax) {
        this.processTax = processTax;
    }

    public TransactionPurchaseExchangeTax getProcessTaxExchangeTax() {
        return processTaxExchangeTax;
    }

    public void setProcessTaxExchangeTax(TransactionPurchaseExchangeTax processTaxExchangeTax) {
        this.processTaxExchangeTax = processTaxExchangeTax;
    }

    public TransactionPurchaseExchangeTax getExchangeTax() {
        return exchangeTax;
    }

    public void setExchangeTax(TransactionPurchaseExchangeTax exchangeTax) {
        this.exchangeTax = exchangeTax;
    }

    public Long getProcessTaxId() {
        return processTaxId;
    }

    public void setProcessTaxId(Long processTaxId) {
        this.processTaxId = processTaxId;
    }

    public Long getProcessTaxExchangeTaxId() {
        return processTaxExchangeTaxId;
    }

    public void setProcessTaxExchangeTaxId(Long processTaxExchangeTaxId) {
        this.processTaxExchangeTaxId = processTaxExchangeTaxId;
    }

    public Long getExchangeTaxId() {
        return exchangeTaxId;
    }

    public void setExchangeTaxId(Long exchangeTaxId) {
        this.exchangeTaxId = exchangeTaxId;
    }

    public boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(boolean paid) {
        isPaid = paid;
    }

    @Override
    public String toString() {
        return "TransactionPurchaseTax{" +
                "id=" + id +
                ", actionDate=" + actionDate +
                ", walletId=" + walletId +
                ", setupId=" + setupId +
                ", totalTax=" + totalTax +
                ", totalTaxPrice=" + totalTaxPrice +
                ", processTax=" + processTax +
                ", processTaxExchangeTax=" + processTaxExchangeTax +
                ", exchangeTax=" + exchangeTax +
                ", processTaxId=" + processTaxId +
                ", processTaxExchangeTaxId=" + processTaxExchangeTaxId +
                ", exchangeTaxId=" + exchangeTaxId +
                ", isPaid=" + isPaid +
                '}';
    }
}
