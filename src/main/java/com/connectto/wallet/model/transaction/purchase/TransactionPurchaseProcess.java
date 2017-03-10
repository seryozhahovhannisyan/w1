package com.connectto.wallet.model.transaction.purchase;

import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionState;

import java.util.Date;

/**
 * Created by Serozh on 11/9/2016.
 */
public class TransactionPurchaseProcess {

    private Long id;
    //PURCHASE_FREEZE, PURCHASE_CHARGE, PURCHASE_CANCEL, FUTURE_PAYMENT
    private TransactionState state;
    private Date actionDate;
    //from
    private Long walletId;
    //to
    private Long setupId;
    //1000 usd
    private Double purchaseAmount;
    private CurrencyType purchaseAmountCurrencyType;

    //480.000 amd wallet purchase without any tax
    private Double walletPurchasePrice;
    //580.800 amd wallet total including all tax
    private Double walletTotalPrice;
    private CurrencyType walletTotalPriceCurrencyType;

    //1000 usd setup total purchase without any tax
    private Double setupPurchasePrice;
    //1210 usd setup total including tax=1000+100 + 110(1000+100*10%)
    private Double setupTotalPrice;
    private CurrencyType setupTotalPriceCurrencyType;
    //100USD as tax +10 USD as tax exchange=110 USD
    private TransactionPurchaseProcessTax processTax;
    //1000 USD - To AMD + tax
    private TransactionPurchaseExchange exchange;

    private Long processTaxId;
    private Long exchangeId;

    public TransactionPurchaseProcess() {
    }

    public TransactionPurchaseProcess(TransactionState state, Date actionDate, Long walletId, Long setupId, Double purchaseAmount, CurrencyType purchaseAmountCurrencyType, TransactionPurchaseProcessTax processTax) {
        this.state = state;
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;

        this.purchaseAmount = purchaseAmount;
        this.purchaseAmountCurrencyType = purchaseAmountCurrencyType;

        this.walletPurchasePrice = purchaseAmount;
        this.walletTotalPrice = purchaseAmount + processTax.getProcessTaxPrice();
        this.walletTotalPriceCurrencyType = purchaseAmountCurrencyType;

        this.setupPurchasePrice = purchaseAmount;
        this.setupTotalPrice = purchaseAmount + processTax.getProcessTax();
        this.setupTotalPriceCurrencyType = purchaseAmountCurrencyType;

        this.processTax = processTax;
    }

    public TransactionPurchaseProcess(TransactionState state, Date actionDate, Long walletId, Long setupId, Double purchaseAmount, CurrencyType purchaseAmountCurrencyType, Double walletPurchasePrice, Double walletTotalPrice, CurrencyType walletTotalPriceCurrencyType, Double setupPurchasePrice, Double setupTotalPrice, CurrencyType setupTotalPriceCurrencyType, TransactionPurchaseProcessTax processTax, TransactionPurchaseExchange exchange) {
        this.state = state;
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;

        this.purchaseAmount = purchaseAmount;
        this.purchaseAmountCurrencyType = purchaseAmountCurrencyType;

        this.walletPurchasePrice = walletPurchasePrice;
        this.walletTotalPrice = walletTotalPrice;
        this.walletTotalPriceCurrencyType = walletTotalPriceCurrencyType;

        this.setupPurchasePrice = setupPurchasePrice;
        this.setupTotalPrice = setupTotalPrice;
        this.setupTotalPriceCurrencyType = setupTotalPriceCurrencyType;

        this.processTax = processTax;
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

    public Double getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(Double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public CurrencyType getPurchaseAmountCurrencyType() {
        return purchaseAmountCurrencyType;
    }

    public void setPurchaseAmountCurrencyType(CurrencyType purchaseAmountCurrencyType) {
        this.purchaseAmountCurrencyType = purchaseAmountCurrencyType;
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

    public TransactionPurchaseProcessTax getProcessTax() {
        return processTax;
    }

    public void setProcessTax(TransactionPurchaseProcessTax processTax) {
        this.processTax = processTax;
    }

    public TransactionPurchaseExchange getExchange() {
        return exchange;
    }

    public void setExchange(TransactionPurchaseExchange exchange) {
        this.exchange = exchange;
    }

    public Long getProcessTaxId() {
        return processTaxId;
    }

    public void setProcessTaxId(Long processTaxId) {
        this.processTaxId = processTaxId;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public Double getWalletPurchasePrice() {
        return walletPurchasePrice;
    }

    public void setWalletPurchasePrice(Double walletPurchasePrice) {
        this.walletPurchasePrice = walletPurchasePrice;
    }

    public Double getSetupPurchasePrice() {
        return setupPurchasePrice;
    }

    public void setSetupPurchasePrice(Double setupPurchasePrice) {
        this.setupPurchasePrice = setupPurchasePrice;
    }

    @Override
    public String toString() {
        return "TransactionPurchaseProcess{" +
                "id=" + id +
                ", state=" + state +
                ", actionDate=" + actionDate +
                ", walletId=" + walletId +
                ", setupId=" + setupId +
                ", purchaseAmount=" + purchaseAmount +
                ", purchaseAmountCurrencyType=" + purchaseAmountCurrencyType +
                ", walletPurchasePrice=" + walletPurchasePrice +
                ", walletTotalPrice=" + walletTotalPrice +
                ", walletTotalPriceCurrencyType=" + walletTotalPriceCurrencyType +
                ", setupPurchasePrice=" + setupPurchasePrice +
                ", setupTotalPrice=" + setupTotalPrice +
                ", setupTotalPriceCurrencyType=" + setupTotalPriceCurrencyType +
                ", processTax=" + processTax +
                ", exchange=" + exchange +
                ", processTaxId=" + processTaxId +
                ", exchangeId=" + exchangeId +
                '}';
    }
}
