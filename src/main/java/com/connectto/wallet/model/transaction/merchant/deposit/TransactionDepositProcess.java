package com.connectto.wallet.model.transaction.merchant.deposit;

import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionState;

import java.util.Date;

/**
 * Created by Serozh on 11/9/2016.
 */
public class TransactionDepositProcess {

    private Long id;
    // Pending, Cancel, approve
    private TransactionState state;
    private Date actionDate;
    //from
    private Long walletId;
    //to
    private Long setupId;
    //100 usd
    private Double depositAmount;
    private CurrencyType depositAmountCurrencyType;

    //100 USD wallet 100 * 480 = 48000 AMD
    private Double walletDepositPrice;
    //94 USD wallet 94 * 480 = 45120 AMD
    private Double walletTotalPrice;
    private CurrencyType walletTotalPriceCurrencyType;

    //100 usd
    private Double setupDepositPrice;
    //5 usd setup total including tax
    private Double setupTotalPrice;
    private CurrencyType setupTotalPriceCurrencyType;
    //2 USD
    private TransactionDepositProcessTax processTax;
    //3 USD
    private WalletSetupDepositTax setupDepositTax;

    //0 USD
    private TransactionDepositExchange exchange;

    private Long processTaxId;
    private Long setupDepositTaxId;
    private Long exchangeId;

    public TransactionDepositProcess() {
    }

    public TransactionDepositProcess(TransactionState state, Date actionDate, Long walletId, Long setupId, Double depositAmount, CurrencyType depositAmountCurrencyType,
                                      TransactionDepositProcessTax processTax, WalletSetupDepositTax setupDepositTax ) {
        this.state = state;
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;

        this.depositAmount = depositAmount;
        this.depositAmountCurrencyType = depositAmountCurrencyType;

        this.walletDepositPrice = depositAmount;
        this.walletTotalPrice = depositAmount + processTax.getProcessTaxPrice() + setupDepositTax.getDepositTaxPrice()  ;
        this.walletTotalPriceCurrencyType = depositAmountCurrencyType;

        this.setupDepositPrice = depositAmount;
        this.setupTotalPrice = processTax.getProcessTaxPrice() + setupDepositTax.getDepositTaxPrice()  ;
        this.setupTotalPriceCurrencyType = depositAmountCurrencyType;

        this.processTax = processTax;
        this.setupDepositTax = setupDepositTax;
    }

    public TransactionDepositProcess(TransactionState state, Date actionDate, Long walletId, Long setupId,
                                      Double depositAmount, CurrencyType depositAmountCurrencyType,
                                      Double walletDepositPrice, Double walletTotalPrice, CurrencyType walletTotalPriceCurrencyType,
                                      Double setupDepositPrice, Double setupTotalPrice, CurrencyType setupTotalPriceCurrencyType,
                                      TransactionDepositProcessTax processTax, WalletSetupDepositTax setupDepositTax,
                                      TransactionDepositExchange exchange) {
        this.state = state;
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;

        this.depositAmount = depositAmount;
        this.depositAmountCurrencyType = depositAmountCurrencyType;

        this.walletDepositPrice = walletDepositPrice;
        this.walletTotalPrice = walletTotalPrice;
        this.walletTotalPriceCurrencyType = walletTotalPriceCurrencyType;

        this.setupDepositPrice = setupDepositPrice;
        this.setupTotalPrice = setupTotalPrice;
        this.setupTotalPriceCurrencyType = setupTotalPriceCurrencyType;

        this.processTax = processTax;
        this.setupDepositTax = setupDepositTax;
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

    public Double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(Double depositAmount) {
        this.depositAmount = depositAmount;
    }

    public CurrencyType getDepositAmountCurrencyType() {
        return depositAmountCurrencyType;
    }

    public void setDepositAmountCurrencyType(CurrencyType depositAmountCurrencyType) {
        this.depositAmountCurrencyType = depositAmountCurrencyType;
    }

    public Double getWalletDepositPrice() {
        return walletDepositPrice;
    }

    public void setWalletDepositPrice(Double walletDepositPrice) {
        this.walletDepositPrice = walletDepositPrice;
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

    public Double getSetupDepositPrice() {
        return setupDepositPrice;
    }

    public void setSetupDepositPrice(Double setupDepositPrice) {
        this.setupDepositPrice = setupDepositPrice;
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

    public TransactionDepositProcessTax getProcessTax() {
        return processTax;
    }

    public void setProcessTax(TransactionDepositProcessTax processTax) {
        this.processTax = processTax;
    }

    public WalletSetupDepositTax getSetupDepositTax() {
        return setupDepositTax;
    }

    public void setSetupDepositTax(WalletSetupDepositTax setupDepositTax) {
        this.setupDepositTax = setupDepositTax;
    }

    public TransactionDepositExchange getExchange() {
        return exchange;
    }

    public void setExchange(TransactionDepositExchange exchange) {
        this.exchange = exchange;
    }

    public Long getProcessTaxId() {
        return processTaxId;
    }

    public void setProcessTaxId(Long processTaxId) {
        this.processTaxId = processTaxId;
    }

    public Long getSetupDepositTaxId() {
        return setupDepositTaxId;
    }

    public void setSetupDepositTaxId(Long setupDepositTaxId) {
        this.setupDepositTaxId = setupDepositTaxId;
    }

    public Long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(Long exchangeId) {
        this.exchangeId = exchangeId;
    }
}
