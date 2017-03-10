package com.connectto.wallet.model.transaction.merchant.withdraw;

import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionState;

import java.util.Date;

/**
 * Created by Serozh on 11/9/2016.
 */
public class TransactionWithdrawProcess {

    private Long id;
    // Pending, Cancel, approve
    private TransactionState state;
    private Date actionDate;
    //from
    private Long walletId;
    //to
    private Long setupId;
    //100 usd
    private Double withdrawAmount;
    private CurrencyType withdrawAmountCurrencyType;

    //100 USD wallet 100 * 480 = 48000 AMD
    private Double walletWithdrawPrice;
    //94 USD wallet 94 * 480 = 45120 AMD
    private Double walletTotalPrice;
    private CurrencyType walletTotalPriceCurrencyType;

    //100 usd
    private Double setupWithdrawPrice;
    //5 usd setup total including tax
    private Double setupTotalPrice;
    private CurrencyType setupTotalPriceCurrencyType;
    //2 USD
    private TransactionWithdrawProcessTax processTax;
    //3 USD
    private WalletSetupWithdrawTax setupWithdrawTax;

    //0 USD
    private TransactionWithdrawExchange exchange;

    private Long processTaxId;
    private Long setupWithdrawTaxId;
    private Long exchangeId;

    public TransactionWithdrawProcess() {
    }

    public TransactionWithdrawProcess(TransactionState state, Date actionDate, Long walletId, Long setupId, Double withdrawAmount, CurrencyType withdrawAmountCurrencyType,
                                      TransactionWithdrawProcessTax processTax, WalletSetupWithdrawTax setupWithdrawTax ) {
        this.state = state;
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;

        this.withdrawAmount = withdrawAmount;
        this.withdrawAmountCurrencyType = withdrawAmountCurrencyType;

        this.walletWithdrawPrice = withdrawAmount;
        this.walletTotalPrice = withdrawAmount + processTax.getProcessTaxPrice() + setupWithdrawTax.getWithdrawTaxPrice()  ;
        this.walletTotalPriceCurrencyType = withdrawAmountCurrencyType;

        this.setupWithdrawPrice = withdrawAmount;
        this.setupTotalPrice = processTax.getProcessTaxPrice() + setupWithdrawTax.getWithdrawTaxPrice()  ;
        this.setupTotalPriceCurrencyType = withdrawAmountCurrencyType;

        this.processTax = processTax;
        this.setupWithdrawTax = setupWithdrawTax;
    }

    public TransactionWithdrawProcess(TransactionState state, Date actionDate, Long walletId, Long setupId,
                                     Double withdrawAmount, CurrencyType withdrawAmountCurrencyType,
                                     Double walletWithdrawPrice, Double walletTotalPrice, CurrencyType walletTotalPriceCurrencyType,
                                     Double setupWithdrawPrice, Double setupTotalPrice, CurrencyType setupTotalPriceCurrencyType,
                                     TransactionWithdrawProcessTax processTax, WalletSetupWithdrawTax setupWithdrawTax,
                                     TransactionWithdrawExchange exchange) {
        this.state = state;
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;

        this.withdrawAmount = withdrawAmount;
        this.withdrawAmountCurrencyType = withdrawAmountCurrencyType;

        this.walletWithdrawPrice = walletWithdrawPrice;
        this.walletTotalPrice = walletTotalPrice;
        this.walletTotalPriceCurrencyType = walletTotalPriceCurrencyType;

        this.setupWithdrawPrice = setupWithdrawPrice;
        this.setupTotalPrice = setupTotalPrice;
        this.setupTotalPriceCurrencyType = setupTotalPriceCurrencyType;

        this.processTax = processTax;
        this.setupWithdrawTax = setupWithdrawTax;
        this.exchange = exchange;
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

    public TransactionState getState() {
        return state;
    }

    public void setState(TransactionState state) {
        this.state = state;
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

    public Double getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(Double withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public CurrencyType getWithdrawAmountCurrencyType() {
        return withdrawAmountCurrencyType;
    }

    public void setWithdrawAmountCurrencyType(CurrencyType withdrawAmountCurrencyType) {
        this.withdrawAmountCurrencyType = withdrawAmountCurrencyType;
    }

    public Double getWalletWithdrawPrice() {
        return walletWithdrawPrice;
    }

    public void setWalletWithdrawPrice(Double walletWithdrawPrice) {
        this.walletWithdrawPrice = walletWithdrawPrice;
    }

    public Double getWalletTotalPrice() {
        return walletTotalPrice;
    }

    public void setWalletTotalPrice(Double walletTotalPrice) {
        this.walletTotalPrice = walletTotalPrice;
    }

    public CurrencyType getWalletTotalPriceCurrencyType() {
        return walletTotalPriceCurrencyType;
    }

    public void setWalletTotalPriceCurrencyType(CurrencyType walletTotalPriceCurrencyType) {
        this.walletTotalPriceCurrencyType = walletTotalPriceCurrencyType;
    }

    public Double getSetupWithdrawPrice() {
        return setupWithdrawPrice;
    }

    public void setSetupWithdrawPrice(Double setupWithdrawPrice) {
        this.setupWithdrawPrice = setupWithdrawPrice;
    }

    public Double getSetupTotalPrice() {
        return setupTotalPrice;
    }

    public void setSetupTotalPrice(Double setupTotalPrice) {
        this.setupTotalPrice = setupTotalPrice;
    }

    public CurrencyType getSetupTotalPriceCurrencyType() {
        return setupTotalPriceCurrencyType;
    }

    public void setSetupTotalPriceCurrencyType(CurrencyType setupTotalPriceCurrencyType) {
        this.setupTotalPriceCurrencyType = setupTotalPriceCurrencyType;
    }

    public TransactionWithdrawProcessTax getProcessTax() {
        return processTax;
    }

    public void setProcessTax(TransactionWithdrawProcessTax processTax) {
        this.processTax = processTax;
    }

    public WalletSetupWithdrawTax getSetupWithdrawTax() {
        return setupWithdrawTax;
    }

    public void setSetupWithdrawTax(WalletSetupWithdrawTax setupWithdrawTax) {
        this.setupWithdrawTax = setupWithdrawTax;
    }

    public TransactionWithdrawExchange getExchange() {
        return exchange;
    }

    public void setExchange(TransactionWithdrawExchange exchange) {
        this.exchange = exchange;
    }

    public Long getProcessTaxId() {
        return processTaxId;
    }

    public void setProcessTaxId(Long processTaxId) {
        this.processTaxId = processTaxId;
    }

    public Long getSetupWithdrawTaxId() {
        return setupWithdrawTaxId;
    }

    public void setSetupWithdrawTaxId(Long setupWithdrawTaxId) {
        this.setupWithdrawTaxId = setupWithdrawTaxId;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }
}
