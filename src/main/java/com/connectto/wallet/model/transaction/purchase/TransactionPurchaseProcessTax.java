package com.connectto.wallet.model.transaction.purchase;

import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionTaxType;

import java.util.Date;

/**
 * Created by Serozh on 11/9/2016.
 */
public class TransactionPurchaseProcessTax {

    private Long id;
    private Date actionDate;
    //Many to one
    private Long walletId;
    private Long setupId;
    //100 usd
    private Double processTax;
    private Double processTaxTotal;
    private CurrencyType processTaxCurrencyType;

    //48.000 amd wallet total including all tax
    private Double processTaxPrice;
    private Double processTaxPriceTotal;
    private CurrencyType processTaxPriceCurrencyType;

    private TransactionTaxType processTaxType;
    private TransactionPurchaseExchange exchange;

    private Long exchangeId;

    public TransactionPurchaseProcessTax() {
    }

    public TransactionPurchaseProcessTax(Date actionDate, Long walletId, Long setupId, Double processTax, CurrencyType processTaxCurrencyType, TransactionTaxType processTaxType) {
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;

        this.processTax = processTax;
        this.processTaxCurrencyType = processTaxCurrencyType;

        this.processTaxPrice = processTax;
        this.processTaxPriceCurrencyType = processTaxCurrencyType;

        this.processTaxType = processTaxType;

        this.calculateTotal();
    }

    public TransactionPurchaseProcessTax(Date actionDate, Long walletId, Long setupId, Double processTax, CurrencyType processTaxCurrencyType, Double processTaxPrice, CurrencyType processTaxPriceCurrencyType, TransactionTaxType processTaxType, TransactionPurchaseExchange exchange) {
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;
        this.processTax = processTax;
        this.processTaxCurrencyType = processTaxCurrencyType;
        this.processTaxPrice = processTaxPrice;
        this.processTaxPriceCurrencyType = processTaxPriceCurrencyType;
        this.processTaxType = processTaxType;
        this.exchange = exchange;
        this.calculateTotal();
    }

    private void calculateTotal(){
        this.processTaxTotal = this.processTax;
        this.processTaxPriceTotal = this.processTaxPrice;
        if(this.exchange != null){
            this.processTaxTotal += this.exchange.getExchangeTax().getExchangeTax();
            this.processTaxPriceTotal += this.exchange.getExchangeTax().getExchangeTaxPrice();
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

    public Double getProcessTax() {
        return processTax;
    }

    public void setProcessTax(Double processTax) {
        this.processTax = processTax;
    }

    public Double getProcessTaxTotal() {
        return processTaxTotal;
    }

    public void setProcessTaxTotal(Double processTaxTotal) {
        this.processTaxTotal = processTaxTotal;
    }

    public CurrencyType getProcessTaxCurrencyType() {
        return processTaxCurrencyType;
    }

    public void setProcessTaxCurrencyType(CurrencyType processTaxCurrencyType) {
        this.processTaxCurrencyType = processTaxCurrencyType;
    }

    public Double getProcessTaxPrice() {
        return processTaxPrice;
    }

    public void setProcessTaxPrice(Double processTaxPrice) {
        this.processTaxPrice = processTaxPrice;
    }

    public Double getProcessTaxPriceTotal() {
        return processTaxPriceTotal;
    }

    public void setProcessTaxPriceTotal(Double processTaxPriceTotal) {
        this.processTaxPriceTotal = processTaxPriceTotal;
    }

    public CurrencyType getProcessTaxPriceCurrencyType() {
        return processTaxPriceCurrencyType;
    }

    public void setProcessTaxPriceCurrencyType(CurrencyType processTaxPriceCurrencyType) {
        this.processTaxPriceCurrencyType = processTaxPriceCurrencyType;
    }

    public TransactionTaxType getProcessTaxType() {
        return processTaxType;
    }

    public void setProcessTaxType(TransactionTaxType processTaxType) {
        this.processTaxType = processTaxType;
    }

    public TransactionPurchaseExchange getExchange() {
        return exchange;
    }

    public void setExchange(TransactionPurchaseExchange exchange) {
        this.exchange = exchange;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }
}
