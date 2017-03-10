package com.connectto.wallet.web.action.wallet.dto;

import com.connectto.wallet.model.wallet.ExchangeRate;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;

/**
 * Created by htdev001 on 12/4/14.
 */
public class TransferDto {

    private CurrencyType selectedCurrencyType;

    private ExchangeRate selectedExchangeRate;
    private ExchangeRate setupExchangeRate;

    //incoming money with selected currency
    private Double amount;

    //the fee for payment transfer with setup currency
    private Double transferFee;
    //the amount for payment transfer fee with own currency
    private Double transferAmount;
    //the exchanged transfer amount with  selected amount
    private Double transferExchangedAmount;

    //for first exchange action, with corresponding selected currency // can be another or setup currency
    private Double exchangeTransferFee;
    //for transferring exchange for first exchange with wallet currency
    private Double exchangeTransferAmount;

    //for second exchange action, with setup currency
    private Double exchangeSetupFee;
    //for second exchange with wallet currency
    private Double exchangeSetupAmount;

    //outgoing money with selected currency, included both fees
    private Double totalFees;
    private Double totalFeesAmount;
    private Double totalAmount;

    public CurrencyType getSelectedCurrencyType() {
        return selectedCurrencyType;
    }

    public void setSelectedCurrencyType(CurrencyType selectedCurrencyType) {
        this.selectedCurrencyType = selectedCurrencyType;
    }

    public ExchangeRate getSelectedExchangeRate() {
        return selectedExchangeRate;
    }

    public void setSelectedExchangeRate(ExchangeRate selectedExchangeRate) {
        this.selectedExchangeRate = selectedExchangeRate;
    }

    public ExchangeRate getSetupExchangeRate() {
        return setupExchangeRate;
    }

    public void setSetupExchangeRate(ExchangeRate setupExchangeRate) {
        this.setupExchangeRate = setupExchangeRate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getTransferFee() {
        return transferFee;
    }

    public void setTransferFee(Double transferFee) {
        this.transferFee = transferFee;
    }

    public Double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Double transferAmount) {
        this.transferAmount = transferAmount;
    }

    public Double getTransferExchangedAmount() {
        return transferExchangedAmount;
    }

    public void setTransferExchangedAmount(Double transferExchangedAmount) {
        this.transferExchangedAmount = transferExchangedAmount;
    }

    public Double getExchangeTransferFee() {
        return exchangeTransferFee;
    }

    public void setExchangeTransferFee(Double exchangeTransferFee) {
        this.exchangeTransferFee = exchangeTransferFee;
    }

    public Double getExchangeTransferAmount() {
        return exchangeTransferAmount;
    }

    public void setExchangeTransferAmount(Double exchangeTransferAmount) {
        this.exchangeTransferAmount = exchangeTransferAmount;
    }

    public Double getExchangeSetupFee() {
        return exchangeSetupFee;
    }

    public void setExchangeSetupFee(Double exchangeSetupFee) {
        this.exchangeSetupFee = exchangeSetupFee;
    }

    public Double getExchangeSetupAmount() {
        return exchangeSetupAmount;
    }

    public void setExchangeSetupAmount(Double exchangeSetupAmount) {
        this.exchangeSetupAmount = exchangeSetupAmount;
    }

    public Double getTotalFees() {
        return totalFees;
    }

    public void setTotalFees(Double totalFees) {
        this.totalFees = totalFees;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Double getTotalFeesAmount() {
        return totalFeesAmount;
    }

    public void setTotalFeesAmount(Double totalFeesAmount) {
        this.totalFeesAmount = totalFeesAmount;
    }

    @Override
    public String toString() {
        return "TransferDto{" +
                "selectedCurrencyType=" + selectedCurrencyType +
                ", selectedExchangeRate=" + selectedExchangeRate +
                ", setupExchangeRate=" + setupExchangeRate +
                ", amount=" + amount +
                ", transferFee=" + transferFee +
                ", transferAmount=" + transferAmount +
                ", transferExchangedAmount=" + transferExchangedAmount +
                ", exchangeTransferFee=" + exchangeTransferFee +
                ", exchangeTransferAmount=" + exchangeTransferAmount +
                ", exchangeSetupFee=" + exchangeSetupFee +
                ", exchangeSetupAmount=" + exchangeSetupAmount +
                ", totalFees=" + totalFees +
                ", totalFeesAmount=" + totalFeesAmount +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
