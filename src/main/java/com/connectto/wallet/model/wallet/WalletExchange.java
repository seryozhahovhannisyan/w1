package com.connectto.wallet.model.wallet;

import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionTaxType;

import java.util.Date;
import java.util.List;

/**
 * Created by htdev001 on 8/25/14.
 */
public class WalletExchange {

    private Long id;

    private Long walletId;
    private Long walletSetupId;
    private Wallet wallet;
    private List<WalletExchangePending> walletExchangePendings;
    private List<Long> walletExchangePendingIdes;

    private Double money;
    private Double newMoneyPaidTax;
    private Double newMoneyPaidTaxPrice;
    private Exchange newMoneyPaidTaxExchange;
    private Long newMoneyPaidTaxExchangeId;

    private Double frozenAmount;
    private Double frozenAmountPrice;
    private Exchange frozenAmountExchange;
    private Long frozenAmountExchangeId;

    private Double receivingAmount;
    private Double receivingAmountPrice;
    private Exchange receivingAmountExchange;
    private Long receivingAmountExchangeId;

    private Double totalTax;
    private Double totalTaxPrice;
    private Exchange totalExchange;
    private Long totalExchangeId;

    private TransactionTaxType totalTaxType;

    private CurrencyType oldCurrencyType;
    private CurrencyType newCurrencyType;

    private Date exchangedAt;

    private int disputeState;
    private Long disputeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Long getWalletSetupId() {
        return walletSetupId;
    }

    public void setWalletSetupId(Long walletSetupId) {
        this.walletSetupId = walletSetupId;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public List<WalletExchangePending> getWalletExchangePendings() {
        return walletExchangePendings;
    }

    public void setWalletExchangePendings(List<WalletExchangePending> walletExchangePendings) {
        this.walletExchangePendings = walletExchangePendings;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }

    public Double getNewMoneyPaidTax() {
        return newMoneyPaidTax;
    }

    public void setNewMoneyPaidTax(Double newMoneyPaidTax) {
        this.newMoneyPaidTax = newMoneyPaidTax;
    }

    public Double getNewMoneyPaidTaxPrice() {
        return newMoneyPaidTaxPrice;
    }

    public void setNewMoneyPaidTaxPrice(Double newMoneyPaidTaxPrice) {
        this.newMoneyPaidTaxPrice = newMoneyPaidTaxPrice;
    }

    public Exchange getNewMoneyPaidTaxExchange() {
        return newMoneyPaidTaxExchange;
    }

    public void setNewMoneyPaidTaxExchange(Exchange newMoneyPaidTaxExchange) {
        this.newMoneyPaidTaxExchange = newMoneyPaidTaxExchange;
    }

    public Double getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(Double frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public Double getFrozenAmountPrice() {
        return frozenAmountPrice;
    }

    public void setFrozenAmountPrice(Double frozenAmountPrice) {
        this.frozenAmountPrice = frozenAmountPrice;
    }

    public Exchange getFrozenAmountExchange() {
        return frozenAmountExchange;
    }

    public void setFrozenAmountExchange(Exchange frozenAmountExchange) {
        this.frozenAmountExchange = frozenAmountExchange;
    }

    public Double getReceivingAmount() {
        return receivingAmount;
    }

    public void setReceivingAmount(Double receivingAmount) {
        this.receivingAmount = receivingAmount;
    }

    public Double getReceivingAmountPrice() {
        return receivingAmountPrice;
    }

    public void setReceivingAmountPrice(Double receivingAmountPrice) {
        this.receivingAmountPrice = receivingAmountPrice;
    }

    public Exchange getReceivingAmountExchange() {
        return receivingAmountExchange;
    }

    public void setReceivingAmountExchange(Exchange receivingAmountExchange) {
        this.receivingAmountExchange = receivingAmountExchange;
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

    public Exchange getTotalExchange() {
        return totalExchange;
    }

    public void setTotalExchange(Exchange totalExchange) {
        this.totalExchange = totalExchange;
    }

    public TransactionTaxType getTotalTaxType() {
        return totalTaxType;
    }

    public void setTotalTaxType(TransactionTaxType totalTaxType) {
        this.totalTaxType = totalTaxType;
    }

    public CurrencyType getOldCurrencyType() {
        return oldCurrencyType;
    }

    public void setOldCurrencyType(CurrencyType oldCurrencyType) {
        this.oldCurrencyType = oldCurrencyType;
    }

    public CurrencyType getNewCurrencyType() {
        return newCurrencyType;
    }

    public void setNewCurrencyType(CurrencyType newCurrencyType) {
        this.newCurrencyType = newCurrencyType;
    }

    public Date getExchangedAt() {
        return exchangedAt;
    }

    public void setExchangedAt(Date exchangedAt) {
        this.exchangedAt = exchangedAt;
    }

    public Long getNewMoneyPaidTaxExchangeId() {
        return newMoneyPaidTaxExchangeId;
    }

    public void setNewMoneyPaidTaxExchangeId(Long newMoneyPaidTaxExchangeId) {
        this.newMoneyPaidTaxExchangeId = newMoneyPaidTaxExchangeId;
    }

    public Long getFrozenAmountExchangeId() {
        return frozenAmountExchangeId;
    }

    public void setFrozenAmountExchangeId(Long frozenAmountExchangeId) {
        this.frozenAmountExchangeId = frozenAmountExchangeId;
    }

    public Long getReceivingAmountExchangeId() {
        return receivingAmountExchangeId;
    }

    public void setReceivingAmountExchangeId(Long receivingAmountExchangeId) {
        this.receivingAmountExchangeId = receivingAmountExchangeId;
    }

    public Long getTotalExchangeId() {
        return totalExchangeId;
    }

    public void setTotalExchangeId(Long totalExchangeId) {
        this.totalExchangeId = totalExchangeId;
    }

    public int getDisputeState() {
        return disputeState;
    }

    public void setDisputeState(int disputeState) {
        this.disputeState = disputeState;
    }

    public Long getDisputeId() {
        return disputeId;
    }

    public void setDisputeId(Long disputeId) {
        this.disputeId = disputeId;
    }

    public List<Long> getWalletExchangePendingIdes() {
        return walletExchangePendingIdes;
    }

    public void setWalletExchangePendingIdes(List<Long> walletExchangePendingIdes) {
        this.walletExchangePendingIdes = walletExchangePendingIdes;
    }
}