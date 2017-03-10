package com.connectto.wallet.model.wallet;

import com.connectto.general.model.WalletSetup;

import java.util.Date;

/**
 * Created by htdev001 on 8/25/14.
 */
public class TransactionProcess {

    private Long id;
    private Date actionDate;
    //Many to one
    private Long walletId;
    private Wallet wallet;
    //Many to one
    private Long walletSetupId;
    private WalletSetup walletSetup;

    //transaction amount & currency type by wallet currency type
    private Double frozenAmount;
    private Double totalAmount;
    private Double totalAmountPrice;

    private Exchange totalAmountExchange;
    private Long totalAmountExchangeId;

    private Exchange overlayProductExchange;
    private Long overlayProductExchangeId;

    private Exchange overlayProductPriceExchange;
    private Long overlayProductPriceExchangeId;

    private TransactionTax tax;
    private Long taxId;


    public TransactionProcess() {
    }

    public TransactionProcess(Date actionDate, Long walletId, Long walletSetupId,
                              Double frozenAmount, Double totalAmount, Double totalAmountPrice,
                              Exchange totalAmountExchange, Exchange overlayProductExchange, Exchange overlayProductPriceExchange,
                              TransactionTax tax) {
        this.actionDate = actionDate;
        this.walletId = walletId;
        this.walletSetupId = walletSetupId;
        this.frozenAmount = frozenAmount;
        this.totalAmount = totalAmount;
        this.totalAmountPrice = totalAmountPrice;
        this.totalAmountExchange = totalAmountExchange;

        this.overlayProductExchange = overlayProductExchange;
        this.overlayProductPriceExchange = overlayProductPriceExchange;

        /*this.transferExchangeToSetupCurrency = transferExchangeToSetupCurrency;
        this.transferExchangeToSetupCurrencyPrice = transferExchangeToSetupCurrencyPrice;
        this.transferExchangeToSetupCurrencyExchange = transferExchangeToSetupCurrencyExchange;
        this.transferExchangeToSetupCurrencyPriceExchange = transferExchangeToSetupCurrencyPriceExchange;*/
        this.tax = tax;
    }

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

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Long getWalletSetupId() {
        return walletSetupId;
    }

    public void setWalletSetupId(Long walletSetupId) {
        this.walletSetupId = walletSetupId;
    }

    public WalletSetup getWalletSetup() {
        return walletSetup;
    }

    public void setWalletSetup(WalletSetup walletSetup) {
        this.walletSetup = walletSetup;
    }

    public Double getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(Double frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getTotalAmountPrice() {
        return totalAmountPrice;
    }

    public void setTotalAmountPrice(Double totalAmountPrice) {
        this.totalAmountPrice = totalAmountPrice;
    }

    public Exchange getTotalAmountExchange() {
        return totalAmountExchange;
    }

    public void setTotalAmountExchange(Exchange totalAmountExchange) {
        this.totalAmountExchange = totalAmountExchange;
    }

    public Long getTotalAmountExchangeId() {
        return totalAmountExchangeId;
    }

    public void setTotalAmountExchangeId(Long totalAmountExchangeId) {
        this.totalAmountExchangeId = totalAmountExchangeId;
    }

    public TransactionTax getTax() {
        return tax;
    }

    public void setTax(TransactionTax tax) {
        this.tax = tax;
    }

    public Long getTaxId() {
        return taxId;
    }

    public void setTaxId(Long taxId) {
        this.taxId = taxId;
    }

    public Exchange getOverlayProductExchange() {
        return overlayProductExchange;
    }

    public void setOverlayProductExchange(Exchange overlayProductExchange) {
        this.overlayProductExchange = overlayProductExchange;
    }

    public Long getOverlayProductExchangeId() {
        return overlayProductExchangeId;
    }

    public void setOverlayProductExchangeId(Long overlayProductExchangeId) {
        this.overlayProductExchangeId = overlayProductExchangeId;
    }

    public Exchange getOverlayProductPriceExchange() {
        return overlayProductPriceExchange;
    }

    public void setOverlayProductPriceExchange(Exchange overlayProductPriceExchange) {
        this.overlayProductPriceExchange = overlayProductPriceExchange;
    }

    public Long getOverlayProductPriceExchangeId() {
        return overlayProductPriceExchangeId;
    }

    public void setOverlayProductPriceExchangeId(Long overlayProductPriceExchangeId) {
        this.overlayProductPriceExchangeId = overlayProductPriceExchangeId;
    }
}
