package com.connectto.wallet.web.action.wallet.dto;

/**
 * Created by Serozh on 2/3/16.
 */
@Deprecated
public class TransactionReviewDto {

    private String previewMessage;

    //$(".act-res-1").html( ' 1 ' + dto.selectedExchangeRate.oneCurrency + ' = ' + dto.selectedExchangeRate.buy + dto.selectedExchangeRate.toCurrency);
    private String selectedExchangeRate;
    //$(".act-res").html( ' 1 ' + dto.setupExchangeRate.oneCurrency + ' = ' + dto.setupExchangeRate.buy + dto.setupExchangeRate.toCurrency);
    private String setupExchangeRate;
    //$(".cntr-res-n .trt-1").html(dto.amount + ' ' + dto.selectedExchangeRate.toCurrency + ' = ' + dto.transferExchangedAmount + ' ' + dto.selectedExchangeRate.oneCurrency);
    private String transferPrice;
    //$(".cntr-res .tot-tr").html(dto.totalAmount + ' ' + dto.selectedExchangeRate.oneCurrency);
    private String totalAmount;
    //(".cntr-res .trans").html(dto.transferExchangedAmount + ' ' + dto.selectedExchangeRate.oneCurrency);
    private String transferExchangedAmount;
    //$(".cntr-res .trans_cost").html(dto.totalFeesAmount + ' ' + dto.selectedExchangeRate.oneCurrency);
    private String totalFeesAmount;
    //$(".det-tem .trans_fee").html(dto.transferAmount + ' ' + dto.setupExchangeRate.oneCurrency );
    private String transferAmount;
    //$(".det-tem .trans-exchange").html(dto.exchangeTransferAmount + ' ' + dto.setupExchangeRate.oneCurrency );
    private String exchangeTransferAmount;
    //$(".det-tem .exchange_fee").html(dto.exchangeSetupAmount + ' ' + dto.setupExchangeRate.oneCurrency);
    private String exchangeSetupAmount;

    //$(".pdes .tfd").html(dto.transferFee + ' ' + dto.setupExchangeRate.toCurrency + ' / ' + dto.setupExchangeRate.buy + ' ' + dto.setupExchangeRate.oneCurrency + ' = ' + dto.transferAmount + ' ' + dto.setupExchangeRate.oneCurrency);
    private String detailTransferFee;
    //$(".pdes .tefd").html(dto.exchangeTransferFee + ' ' + dto.setupExchangeRate.toCurrency + ' / ' + dto.setupExchangeRate.buy + ' ' + dto.setupExchangeRate.oneCurrency + ' = ' + dto.exchangeTransferAmount + ' ' + dto.setupExchangeRate.oneCurrency);
    private String detailExchangeTransferFee;
    //$(".pdes .efd").html(dto.exchangeSetupFee + ' ' + dto.setupExchangeRate.toCurrency + ' / ' + dto.setupExchangeRate.buy + ' ' + dto.setupExchangeRate.oneCurrency + ' = ' + dto.exchangeSetupAmount + ' ' + dto.setupExchangeRate.oneCurrency);
    private String detailSetupFee;
    //$(".pdes .tcd").html(dto.transferAmount + ' ' + dto.setupExchangeRate.oneCurrency + ' + ' + dto.exchangeTransferAmount + ' ' + dto.setupExchangeRate.oneCurrency + ' + ' + dto.exchangeSetupAmount + ' ' + dto.setupExchangeRate.oneCurrency + ' = ' + dto.totalFeesAmount + ' ' + dto.setupExchangeRate.oneCurrency );
    private String detailTotalAmount;

    public String getPreviewMessage() {
        return previewMessage;
    }

    public void setPreviewMessage(String previewMessage) {
        this.previewMessage = previewMessage;
    }

    public String getSelectedExchangeRate() {
        return selectedExchangeRate;
    }

    public void setSelectedExchangeRate(String selectedExchangeRate) {
        this.selectedExchangeRate = selectedExchangeRate;
    }

    public String getSetupExchangeRate() {
        return setupExchangeRate;
    }

    public void setSetupExchangeRate(String setupExchangeRate) {
        this.setupExchangeRate = setupExchangeRate;
    }

    public String getTransferPrice() {
        return transferPrice;
    }

    public void setTransferPrice(String transferPrice) {
        this.transferPrice = transferPrice;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTransferExchangedAmount() {
        return transferExchangedAmount;
    }

    public void setTransferExchangedAmount(String transferExchangedAmount) {
        this.transferExchangedAmount = transferExchangedAmount;
    }

    public String getTotalFeesAmount() {
        return totalFeesAmount;
    }

    public void setTotalFeesAmount(String totalFeesAmount) {
        this.totalFeesAmount = totalFeesAmount;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getExchangeTransferAmount() {
        return exchangeTransferAmount;
    }

    public void setExchangeTransferAmount(String exchangeTransferAmount) {
        this.exchangeTransferAmount = exchangeTransferAmount;
    }

    public String getExchangeSetupAmount() {
        return exchangeSetupAmount;
    }

    public void setExchangeSetupAmount(String exchangeSetupAmount) {
        this.exchangeSetupAmount = exchangeSetupAmount;
    }

    public String getDetailTransferFee() {
        return detailTransferFee;
    }

    public void setDetailTransferFee(String detailTransferFee) {
        this.detailTransferFee = detailTransferFee;
    }

    public String getDetailExchangeTransferFee() {
        return detailExchangeTransferFee;
    }

    public void setDetailExchangeTransferFee(String detailExchangeTransferFee) {
        this.detailExchangeTransferFee = detailExchangeTransferFee;
    }

    public String getDetailSetupFee() {
        return detailSetupFee;
    }

    public void setDetailSetupFee(String detailSetupFee) {
        this.detailSetupFee = detailSetupFee;
    }

    public String getDetailTotalAmount() {
        return detailTotalAmount;
    }

    public void setDetailTotalAmount(String detailTotalAmount) {
        this.detailTotalAmount = detailTotalAmount;
    }

    @Override
    public String toString() {
        return "TransactionReviewDto{" +
                "previewMessage='" + previewMessage + '\'' +
                ", selectedExchangeRate='" + selectedExchangeRate + '\'' +
                ", setupExchangeRate='" + setupExchangeRate + '\'' +
                ", transferPrice='" + transferPrice + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", transferExchangedAmount='" + transferExchangedAmount + '\'' +
                ", totalFeesAmount='" + totalFeesAmount + '\'' +
                ", transferAmount='" + transferAmount + '\'' +
                ", exchangeTransferAmount='" + exchangeTransferAmount + '\'' +
                ", exchangeSetupAmount='" + exchangeSetupAmount + '\'' +
                ", detailTransferFee='" + detailTransferFee + '\'' +
                ", detailExchangeTransferFee='" + detailExchangeTransferFee + '\'' +
                ", detailSetupFee='" + detailSetupFee + '\'' +
                ", detailTotalAmount='" + detailTotalAmount + '\'' +
                '}';
    }
}
