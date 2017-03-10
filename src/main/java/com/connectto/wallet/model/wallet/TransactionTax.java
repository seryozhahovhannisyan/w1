package com.connectto.wallet.model.wallet;

import com.connectto.wallet.model.wallet.lcp.TransactionTaxType;

import java.util.Date;

/**
 * Created by htdev001 on 11/25/14.
 */
public class TransactionTax {

    private Long id;

    private Long walletId;
    private Long walletSetupId;

    private Date createdAt;
    private Date paymentDate;

    //included all taxes on wallet setup currency
    private Double totalAmountTax;
    private Double totalAmountTaxPrice;

    //all taxes have wallet setup currency, and taxPrice exchanged user wallet cuuerncy
    //tax for general transfer
    private Double transferTax;
    private Double transferTaxPrice;
    private TransactionTaxType transferTaxType;
    private Exchange transferTaxExchange;
    private Long transferTaxExchangeId;
    //tax for general transfer
    private Double transferExchangeTax;
    private Double transferExchangeTaxPrice;
    private TransactionTaxType transferExchangeTaxType;
    private Exchange transferExchangeTaxExchange;
    private Long transferExchangeTaxExchangeId;
    //tax for general transfer
    private Double overlayExchangeTax;
    private Double overlayExchangeTaxPrice;
    private TransactionTaxType overlayExchangeTaxType;
    private Exchange overlayExchange;
    private Long overlayExchangeId;


    private Boolean isPaid;

    public TransactionTax() {
    }

    public TransactionTax(Long walletId, Long walletSetupId, Date createdAt, Double totalSetupTax, Double totalSetupTaxPrice,
                          Double transferTax, Double transferTaxPrice, TransactionTaxType transferTaxType, Exchange transferTaxExchange,
                          Double transferExchangeTax, Double transferExchangeTaxPrice, TransactionTaxType transferExchangeTaxType, Exchange transferExchangeTaxExchange,
                          Double overlayExchangeTax, Double overlayExchangeTaxPrice, TransactionTaxType overlayExchangeTaxType, Exchange overlayExchange,
                          Date paymentDate, Boolean isPaid) {

        this.walletId = walletId;
        this.walletSetupId = walletSetupId;
        this.createdAt = createdAt;

        this.totalAmountTax = totalSetupTax;
        this.totalAmountTaxPrice = totalSetupTaxPrice;

        this.transferTax = transferTax;
        this.transferTaxPrice = transferTaxPrice;
        this.transferTaxType = transferTaxType;
        this.transferTaxExchange = transferTaxExchange;

        this.transferExchangeTax = transferExchangeTax;
        this.transferExchangeTaxPrice = transferExchangeTaxPrice;
        this.transferExchangeTaxType = transferExchangeTaxType;
        this.transferExchangeTaxExchange = transferExchangeTaxExchange;

        this.overlayExchangeTax = overlayExchangeTax;
        this.overlayExchangeTaxPrice = overlayExchangeTaxPrice;
        this.overlayExchangeTaxType = overlayExchangeTaxType;
        this.overlayExchange = overlayExchange;

        this.paymentDate = paymentDate;
        this.isPaid = isPaid;
    }

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

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getTotalAmountTax() {
        return totalAmountTax;
    }

    public void setTotalAmountTax(Double totalAmountTax) {
        this.totalAmountTax = totalAmountTax;
    }

    public Double getTotalAmountTaxPrice() {
        return totalAmountTaxPrice;
    }

    public void setTotalAmountTaxPrice(Double totalAmountTaxPrice) {
        this.totalAmountTaxPrice = totalAmountTaxPrice;
    }

    public Double getTransferTax() {
        return transferTax;
    }

    public void setTransferTax(Double transferTax) {
        this.transferTax = transferTax;
    }

    public Double getTransferTaxPrice() {
        return transferTaxPrice;
    }

    public void setTransferTaxPrice(Double transferTaxPrice) {
        this.transferTaxPrice = transferTaxPrice;
    }

    public TransactionTaxType getTransferTaxType() {
        return transferTaxType;
    }

    public void setTransferTaxType(TransactionTaxType transferTaxType) {
        this.transferTaxType = transferTaxType;
    }

    public Exchange getTransferTaxExchange() {
        return transferTaxExchange;
    }

    public void setTransferTaxExchange(Exchange transferTaxExchange) {
        this.transferTaxExchange = transferTaxExchange;
    }

    public Long getTransferTaxExchangeId() {
        return transferTaxExchangeId;
    }

    public void setTransferTaxExchangeId(Long transferTaxExchangeId) {
        this.transferTaxExchangeId = transferTaxExchangeId;
    }

    public Double getTransferExchangeTax() {
        return transferExchangeTax;
    }

    public void setTransferExchangeTax(Double transferExchangeTax) {
        this.transferExchangeTax = transferExchangeTax;
    }

    public Double getTransferExchangeTaxPrice() {
        return transferExchangeTaxPrice;
    }

    public void setTransferExchangeTaxPrice(Double transferExchangeTaxPrice) {
        this.transferExchangeTaxPrice = transferExchangeTaxPrice;
    }

    public TransactionTaxType getTransferExchangeTaxType() {
        return transferExchangeTaxType;
    }

    public void setTransferExchangeTaxType(TransactionTaxType transferExchangeTaxType) {
        this.transferExchangeTaxType = transferExchangeTaxType;
    }

    public Exchange getTransferExchangeTaxExchange() {
        return transferExchangeTaxExchange;
    }

    public void setTransferExchangeTaxExchange(Exchange transferExchangeTaxExchange) {
        this.transferExchangeTaxExchange = transferExchangeTaxExchange;
    }

    public Long getTransferExchangeTaxExchangeId() {
        return transferExchangeTaxExchangeId;
    }

    public void setTransferExchangeTaxExchangeId(Long transferExchangeTaxExchangeId) {
        this.transferExchangeTaxExchangeId = transferExchangeTaxExchangeId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(Boolean isPaid) {
        this.isPaid = isPaid;
    }

    public Double getOverlayExchangeTax() {
        return overlayExchangeTax;
    }

    public void setOverlayExchangeTax(Double overlayExchangeTax) {
        this.overlayExchangeTax = overlayExchangeTax;
    }

    public Double getOverlayExchangeTaxPrice() {
        return overlayExchangeTaxPrice;
    }

    public void setOverlayExchangeTaxPrice(Double overlayExchangeTaxPrice) {
        this.overlayExchangeTaxPrice = overlayExchangeTaxPrice;
    }

    public TransactionTaxType getOverlayExchangeTaxType() {
        return overlayExchangeTaxType;
    }

    public void setOverlayExchangeTaxType(TransactionTaxType overlayExchangeTaxType) {
        this.overlayExchangeTaxType = overlayExchangeTaxType;
    }

    public Exchange getOverlayExchange() {
        return overlayExchange;
    }

    public void setOverlayExchange(Exchange overlayExchange) {
        this.overlayExchange = overlayExchange;
    }

    public Long getOverlayExchangeId() {
        return overlayExchangeId;
    }

    public void setOverlayExchangeId(Long overlayExchangeId) {
        this.overlayExchangeId = overlayExchangeId;
    }
}