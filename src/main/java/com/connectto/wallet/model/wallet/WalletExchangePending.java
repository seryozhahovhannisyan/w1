package com.connectto.wallet.model.wallet;

/**
 * Created by Serozh on 4/24/16.
 */
public class WalletExchangePending {

    private Long id;

    private Long walletExchangeId;
    private Long transactionId;
    private Long walletId;

    private Double oldFromTotalPrice;
    private Double newFromTotalPrice;
    private Integer oldFromTotalPriceCurrencyType;
    private Integer newFromTotalPriceCurrencyType;
    private Exchange fromTotalPriceExchange;
    private Long fromTotalPriceExchangeId;

    private Double oldToTotalPrice;
    private Double newToTotalPrice;
    private Integer oldToTotalPriceCurrencyType;
    private Integer newToTotalPriceCurrencyType;
    private Exchange toTotalPriceExchange;
    private Long toTotalPriceExchangeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWalletExchangeId() {
        return walletExchangeId;
    }

    public void setWalletExchangeId(Long walletExchangeId) {
        this.walletExchangeId = walletExchangeId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Double getOldFromTotalPrice() {
        return oldFromTotalPrice;
    }

    public void setOldFromTotalPrice(Double oldFromTotalPrice) {
        this.oldFromTotalPrice = oldFromTotalPrice;
    }

    public Double getNewFromTotalPrice() {
        return newFromTotalPrice;
    }

    public void setNewFromTotalPrice(Double newFromTotalPrice) {
        this.newFromTotalPrice = newFromTotalPrice;
    }

    public Integer getOldFromTotalPriceCurrencyType() {
        return oldFromTotalPriceCurrencyType;
    }

    public void setOldFromTotalPriceCurrencyType(Integer oldFromTotalPriceCurrencyType) {
        this.oldFromTotalPriceCurrencyType = oldFromTotalPriceCurrencyType;
    }

    public Integer getNewFromTotalPriceCurrencyType() {
        return newFromTotalPriceCurrencyType;
    }

    public void setNewFromTotalPriceCurrencyType(Integer newFromTotalPriceCurrencyType) {
        this.newFromTotalPriceCurrencyType = newFromTotalPriceCurrencyType;
    }

    public Exchange getFromTotalPriceExchange() {
        return fromTotalPriceExchange;
    }

    public void setFromTotalPriceExchange(Exchange fromTotalPriceExchange) {
        this.fromTotalPriceExchange = fromTotalPriceExchange;
    }

    public Long getFromTotalPriceExchangeId() {
        return fromTotalPriceExchangeId;
    }

    public void setFromTotalPriceExchangeId(Long fromTotalPriceExchangeId) {
        this.fromTotalPriceExchangeId = fromTotalPriceExchangeId;
    }

    public Double getOldToTotalPrice() {
        return oldToTotalPrice;
    }

    public void setOldToTotalPrice(Double oldToTotalPrice) {
        this.oldToTotalPrice = oldToTotalPrice;
    }

    public Double getNewToTotalPrice() {
        return newToTotalPrice;
    }

    public void setNewToTotalPrice(Double newToTotalPrice) {
        this.newToTotalPrice = newToTotalPrice;
    }

    public Integer getOldToTotalPriceCurrencyType() {
        return oldToTotalPriceCurrencyType;
    }

    public void setOldToTotalPriceCurrencyType(Integer oldToTotalPriceCurrencyType) {
        this.oldToTotalPriceCurrencyType = oldToTotalPriceCurrencyType;
    }

    public Integer getNewToTotalPriceCurrencyType() {
        return newToTotalPriceCurrencyType;
    }

    public void setNewToTotalPriceCurrencyType(Integer newToTotalPriceCurrencyType) {
        this.newToTotalPriceCurrencyType = newToTotalPriceCurrencyType;
    }

    public Exchange getToTotalPriceExchange() {
        return toTotalPriceExchange;
    }

    public void setToTotalPriceExchange(Exchange toTotalPriceExchange) {
        this.toTotalPriceExchange = toTotalPriceExchange;
    }

    public Long getToTotalPriceExchangeId() {
        return toTotalPriceExchangeId;
    }

    public void setToTotalPriceExchangeId(Long toTotalPriceExchangeId) {
        this.toTotalPriceExchangeId = toTotalPriceExchangeId;
    }
}