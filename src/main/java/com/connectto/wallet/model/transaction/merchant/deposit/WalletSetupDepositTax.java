package com.connectto.wallet.model.transaction.merchant.deposit;

import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionTaxType;

import java.util.Date;

/**
 * Created by Serozh on 11/9/2016.
 */
public class WalletSetupDepositTax {

    private Long id;
    private Date actionDate;
    //Many to one
    private Long walletId;
    private Long setupId;
    //3 USD
    private Double depositTax;
    private Double depositTaxTotal;
    private CurrencyType depositTaxCurrencyType;

    ///3 USD * 480 AMD = 1440 AMD wallet total including all tax
    private Double depositTaxPrice;
    private Double depositTaxPriceTotal;
    private CurrencyType depositTaxPriceCurrencyType;

    private TransactionTaxType depositTaxType;
    private TransactionDepositExchange exchange;

    private Long exchangeId;

    public WalletSetupDepositTax() {
    }

    //When are not any exchange
    public WalletSetupDepositTax(Date actionDate, Long walletId, Long setupId, Double depositTax, CurrencyType depositTaxCurrencyType, TransactionTaxType depositTaxType) {
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;

        this.depositTax = depositTax;
        this.depositTaxCurrencyType = depositTaxCurrencyType;

        this.depositTaxPrice = depositTax;
        this.depositTaxPriceCurrencyType = depositTaxCurrencyType;

        this.depositTaxType = depositTaxType;

        this.depositTaxTotal = depositTax;
        this.depositTaxPriceTotal = depositTax;
    }

    // When need to exchange tax for pay
    public WalletSetupDepositTax(Date actionDate, Long walletId, Long setupId, Double depositTax, CurrencyType depositTaxCurrencyType, Double depositTaxPrice, CurrencyType depositTaxPriceCurrencyType, TransactionTaxType depositTaxType, TransactionDepositExchange exchange) {
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;
        this.depositTax = depositTax;
        this.depositTaxCurrencyType = depositTaxCurrencyType;
        this.depositTaxPrice = depositTaxPrice;
        this.depositTaxPriceCurrencyType = depositTaxPriceCurrencyType;
        this.depositTaxType = depositTaxType;
        this.exchange = exchange;
        this.depositTaxTotal = depositTax;
        this.depositTaxPriceTotal = depositTaxPrice;
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

    public Double getDepositTax() {
        return depositTax;
    }

    public void setDepositTax(Double depositTax) {
        this.depositTax = depositTax;
    }

    public Double getDepositTaxTotal() {
        return depositTaxTotal;
    }

    public void setDepositTaxTotal(Double depositTaxTotal) {
        this.depositTaxTotal = depositTaxTotal;
    }

    public CurrencyType getDepositTaxCurrencyType() {
        return depositTaxCurrencyType;
    }

    public void setDepositTaxCurrencyType(CurrencyType depositTaxCurrencyType) {
        this.depositTaxCurrencyType = depositTaxCurrencyType;
    }

    public Double getDepositTaxPrice() {
        return depositTaxPrice;
    }

    public void setDepositTaxPrice(Double depositTaxPrice) {
        this.depositTaxPrice = depositTaxPrice;
    }

    public Double getDepositTaxPriceTotal() {
        return depositTaxPriceTotal;
    }

    public void setDepositTaxPriceTotal(Double depositTaxPriceTotal) {
        this.depositTaxPriceTotal = depositTaxPriceTotal;
    }

    public CurrencyType getDepositTaxPriceCurrencyType() {
        return depositTaxPriceCurrencyType;
    }

    public void setDepositTaxPriceCurrencyType(CurrencyType depositTaxPriceCurrencyType) {
        this.depositTaxPriceCurrencyType = depositTaxPriceCurrencyType;
    }

    public TransactionTaxType getDepositTaxType() {
        return depositTaxType;
    }

    public void setDepositTaxType(TransactionTaxType depositTaxType) {
        this.depositTaxType = depositTaxType;
    }

    public TransactionDepositExchange getExchange() {
        return exchange;
    }

    public void setExchange(TransactionDepositExchange exchange) {
        this.exchange = exchange;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }
}
