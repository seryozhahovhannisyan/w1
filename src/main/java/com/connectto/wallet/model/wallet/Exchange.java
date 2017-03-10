package com.connectto.wallet.model.wallet;

import com.connectto.wallet.model.wallet.lcp.CurrencyType;

import java.util.Date;

/**
 * Created by htdev001 on 8/25/14.
 */
public class Exchange {

    private Long id;
    private Double walletAmount;
    private CurrencyType walletCurrencyType;
    private Double rateAmount;
    private CurrencyType rateCurrency;
    private Double resultAmount;
    private CurrencyType resultCurrency;
    private Date exchangeDate;
    //Many to one
    private Wallet wallet;
    private Long walletId;
    //Many to one
    private ExchangeRate exchangeRate;
    private Long exchangeRateId;

    public Exchange() {
    }

    public Exchange(Double walletAmount, CurrencyType walletCurrencyType, Double rateAmount, CurrencyType rateCurrency, Double resultAmount, CurrencyType resultCurrency, Date exchangeDate, Long walletId, Long exchangeRateId) {
        this.walletAmount = walletAmount;
        this.walletCurrencyType = walletCurrencyType;
        this.rateAmount = rateAmount;
        this.rateCurrency = rateCurrency;
        this.resultAmount = resultAmount;
        this.resultCurrency = resultCurrency;
        this.exchangeDate = exchangeDate;
        this.walletId = walletId;
        this.exchangeRateId = exchangeRateId;
    }

    /**
     * ##################################################################################################################
     * Getters Setter
     * ##################################################################################################################
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Double getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(Double walletAmount) {
        this.walletAmount = walletAmount;
    }

    public CurrencyType getWalletCurrencyType() {
        return walletCurrencyType;
    }

    public void setWalletCurrencyType(CurrencyType walletCurrencyType) {
        this.walletCurrencyType = walletCurrencyType;
    }

    public Double getRateAmount() {
        return rateAmount;
    }

    public void setRateAmount(Double rateAmount) {
        this.rateAmount = rateAmount;
    }

    public CurrencyType getRateCurrency() {
        return rateCurrency;
    }

    public void setRateCurrency(CurrencyType rateCurrency) {
        this.rateCurrency = rateCurrency;
    }

    public Double getResultAmount() {
        return resultAmount;
    }

    public void setResultAmount(Double resultAmount) {
        this.resultAmount = resultAmount;
    }

    public CurrencyType getResultCurrency() {
        return resultCurrency;
    }

    public void setResultCurrency(CurrencyType resultCurrency) {
        this.resultCurrency = resultCurrency;
    }

    public Date getExchangeDate() {
        return exchangeDate;
    }

    public void setExchangeDate(Date exchangeDate) {
        this.exchangeDate = exchangeDate;
    }

    public ExchangeRate getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(ExchangeRate exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Long getExchangeRateId() {
        return exchangeRateId;
    }

    public void setExchangeRateId(Long exchangeRateId) {
        this.exchangeRateId = exchangeRateId;
    }
}
