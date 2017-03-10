package com.connectto.wallet.model.transaction.merchant.withdraw;

import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionTaxType;

import java.util.Date;

/**
 * Created by Serozh on 11/9/2016.
 */
public class MerchantWithdrawTax {

    private Long id;
    private Date actionDate;
    //Many to one
    private Long walletId;
    private Long setupId;
    //1USD
    private Double paidTaxToMerchant;
    private CurrencyType paidTaxCurrencyType;
    //1 USD
    private Double withdrawTax;
    private Double withdrawTaxTotal;
    private CurrencyType withdrawTaxCurrencyType;

    //1 USD = 480 AMD
    private Double withdrawTaxPrice;
    private Double withdrawTaxPriceTotal;
    private CurrencyType withdrawTaxPriceCurrencyType;

    private TransactionTaxType withdrawTaxType;
    private TransactionWithdrawExchange exchange;

    private Long exchangeId;

    public MerchantWithdrawTax() {
    }


    /**
     * When wallet & setup and merchant currencies have same values
     */
    public MerchantWithdrawTax(Date actionDate, Long walletId, Long setupId, Double withdrawTax, CurrencyType withdrawTaxCurrencyType, TransactionTaxType withdrawTaxType) {
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;

        this.paidTaxToMerchant = withdrawTax;
        this.paidTaxCurrencyType = withdrawTaxCurrencyType;

        this.withdrawTax = withdrawTax;
        this.withdrawTaxCurrencyType = withdrawTaxCurrencyType;

        this.withdrawTaxPrice = withdrawTax;
        this.withdrawTaxPriceCurrencyType = withdrawTaxCurrencyType;

        this.withdrawTaxType = withdrawTaxType;

        this.withdrawTaxTotal = this.withdrawTax;
        this.withdrawTaxPriceTotal = this.withdrawTaxPrice;
    }

    /**
     * Used for recalculation
     */
    public MerchantWithdrawTax(Date actionDate, Long walletId, Long setupId, Double withdrawTax, CurrencyType withdrawTaxCurrencyType, Double withdrawTaxPrice, CurrencyType withdrawTaxPriceCurrencyType, TransactionTaxType withdrawTaxType, TransactionWithdrawExchange exchange) {
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.setupId = setupId;

        this.paidTaxToMerchant = withdrawTax;
        this.paidTaxCurrencyType = withdrawTaxCurrencyType;

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

    public Double getPaidTaxToMerchant() {
        return paidTaxToMerchant;
    }

    public void setPaidTaxToMerchant(Double paidTaxToMerchant) {
        this.paidTaxToMerchant = paidTaxToMerchant;
    }

    public CurrencyType getPaidTaxCurrencyType() {
        return paidTaxCurrencyType;
    }

    public void setPaidTaxCurrencyType(CurrencyType paidTaxCurrencyType) {
        this.paidTaxCurrencyType = paidTaxCurrencyType;
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

    @Override
    public String toString() {
        return "MerchantWithdrawTax{" +
                "id=" + id +
                ", actionDate=" + actionDate +
                ", walletId=" + walletId +
                ", setupId=" + setupId +
                ", paidTaxToMerchant=" + paidTaxToMerchant +
                ", paidTaxCurrencyType=" + paidTaxCurrencyType +
                ", withdrawTax=" + withdrawTax +
                ", withdrawTaxTotal=" + withdrawTaxTotal +
                ", withdrawTaxCurrencyType=" + withdrawTaxCurrencyType +
                ", withdrawTaxPrice=" + withdrawTaxPrice +
                ", withdrawTaxPriceTotal=" + withdrawTaxPriceTotal +
                ", withdrawTaxPriceCurrencyType=" + withdrawTaxPriceCurrencyType +
                ", withdrawTaxType=" + withdrawTaxType +
                ", exchange=" + exchange +
                ", exchangeId=" + exchangeId +
                '}';
    }
}
