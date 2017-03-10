package com.connectto.wallet.model.wallet;


import java.sql.Date;

public class CreditCardTransfer {
    private long id;

    private long transactionPurchaseId;

    private long creditCardId;
    private double transferAmount;
    private String transferResponseCode;
    private String transferResponseMsg;
    private Date transferDt;

    public String getTransferResponseMsg() {
        return transferResponseMsg;
    }

    public void setTransferResponseMsg(String transferResponseMsg) {
        this.transferResponseMsg = transferResponseMsg;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTransactionPurchaseId() {
        return transactionPurchaseId;
    }

    public void setTransactionPurchaseId(long transactionPurchaseId) {
        this.transactionPurchaseId = transactionPurchaseId;
    }

    public long getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(long creditCardId) {
        this.creditCardId = creditCardId;
    }

    public double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(double transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransferResponseCode() {
        return transferResponseCode;
    }

    public void setTransferResponseCode(String transferResponseCode) {
        this.transferResponseCode = transferResponseCode;
    }

    public Date getTransferDt() {
        return transferDt;
    }

    public void setTransferDt(Date transferDt) {
        this.transferDt = transferDt;
    }

}
