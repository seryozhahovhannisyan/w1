package com.connectto.wallet.model.transaction.merchant.withdraw;

import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionTaxType;

import java.util.Date;

/**
 * Created by Serozh on 11/9/2016.
 */
public class WalletSetupWithdrawTax {

    private Long id;
    private Date actionDate;
    //Many to one
    private Long walletId;
    private Long setupId;
    //3 USD
    private Double withdrawTax;
    private Double withdrawTaxTotal;
    private CurrencyType withdrawTaxCurrencyType;

    ///3 USD * 480 AMD = 1440 AMD wallet total including all tax
    private Double withdrawTaxPrice;
    private Double withdrawTaxPriceTotal;
    private CurrencyType withdrawTaxPriceCurrencyType;

    private TransactionTaxType withdrawTaxType;
    private TransactionWithdrawExchange exchange;

    private Long exchangeId;

    public WalletSetupWithdrawTax() {
    }

    //When are not any exchange
    public WalletSetupWithdrawTax(Date actionDate, Long walletId, Long setupId, Double withdrawTax, CurrencyType withdrawTaxCurrencyType, TransactionTaxType withdrawTaxType) {
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;

        this.withdrawTax = withdrawTax;
        this.withdrawTaxCurrencyType = withdrawTaxCurrencyType;

        this.withdrawTaxPrice = withdrawTax;
        this.withdrawTaxPriceCurrencyType = withdrawTaxCurrencyType;

        this.withdrawTaxType = withdrawTaxType;

        this.withdrawTaxTotal = withdrawTax;
        this.withdrawTaxPriceTotal = withdrawTax;
    }

    // When need to exchange tax for pay
    public WalletSetupWithdrawTax(Date actionDate, Long walletId, Long setupId, Double withdrawTax, CurrencyType withdrawTaxCurrencyType, Double withdrawTaxPrice, CurrencyType withdrawTaxPriceCurrencyType, TransactionTaxType withdrawTaxType, TransactionWithdrawExchange exchange) {
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;
        this.withdrawTax = withdrawTax;
        this.withdrawTaxCurrencyType = withdrawTaxCurrencyType;
        this.withdrawTaxPrice = withdrawTaxPrice;
        this.withdrawTaxPriceCurrencyType = withdrawTaxPriceCurrencyType;
        this.withdrawTaxType = withdrawTaxType;
        this.exchange = exchange;
        this.withdrawTaxTotal = withdrawTax;
        this.withdrawTaxPriceTotal = withdrawTaxPrice;
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

    public Double getWithdrawTax() {
        return withdrawTax;
    }

    public void setWithdrawTax(Double withdrawTax) {
        this.withdrawTax = withdrawTax;
    }

    public Double getWithdrawTaxTotal() {
        return withdrawTaxTotal;
    }

    public void setWithdrawTaxTotal(Double withdrawTaxTotal) {
        this.withdrawTaxTotal = withdrawTaxTotal;
    }

    public CurrencyType getWithdrawTaxCurrencyType() {
        return withdrawTaxCurrencyType;
    }

    public void setWithdrawTaxCurrencyType(CurrencyType withdrawTaxCurrencyType) {
        this.withdrawTaxCurrencyType = withdrawTaxCurrencyType;
    }

    public Double getWithdrawTaxPrice() {
        return withdrawTaxPrice;
    }

    public void setWithdrawTaxPrice(Double withdrawTaxPrice) {
        this.withdrawTaxPrice = withdrawTaxPrice;
    }

    public Double getWithdrawTaxPriceTotal() {
        return withdrawTaxPriceTotal;
    }

    public void setWithdrawTaxPriceTotal(Double withdrawTaxPriceTotal) {
        this.withdrawTaxPriceTotal = withdrawTaxPriceTotal;
    }

    public CurrencyType getWithdrawTaxPriceCurrencyType() {
        return withdrawTaxPriceCurrencyType;
    }

    public void setWithdrawTaxPriceCurrencyType(CurrencyType withdrawTaxPriceCurrencyType) {
        this.withdrawTaxPriceCurrencyType = withdrawTaxPriceCurrencyType;
    }

    public TransactionTaxType getWithdrawTaxType() {
        return withdrawTaxType;
    }

    public void setWithdrawTaxType(TransactionTaxType withdrawTaxType) {
        this.withdrawTaxType = withdrawTaxType;
    }

    public TransactionWithdrawExchange getExchange() {
        return exchange;
    }

    public void setExchange(TransactionWithdrawExchange exchange) {
        this.exchange = exchange;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }
}
