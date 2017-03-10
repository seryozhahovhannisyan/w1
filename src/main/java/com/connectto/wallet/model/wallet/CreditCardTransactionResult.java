package com.connectto.wallet.model.wallet;

/**
 * Created by Serozh on 11/25/2016.
 */
public class CreditCardTransactionResult {

    private long  transferId;

    private boolean transactionError;

    private boolean transactionApproved;

    private String EXactRespCode;

    private String EXactMessage;

    private String bankRespCode;

    private String bankMessage;

    private String bankRespCode2;

    private String sequenceNo;

    private String AVS;

    private String CVV2;

    private String retrievalRefNo;

    private String CAVVResponse;

    private String amountRequested;

    private String merchantName;

    private String merchantAddress;

    private String merchantCity;

    private String merchantProvince;

    private String merchantCountry;

    private String merchantPostal;

    private String merchantURL;

    private String currentBalance;

    private String previousBalance;

    private String CTR;


    public long getTransferId() {
        return transferId;
    }

    public void setTransferId(long transferId) {
        this.transferId = transferId;
    }

    public boolean getTransactionError() {
        return transactionError;
    }

    public void setTransactionError(boolean transactionError) {
        this.transactionError = transactionError;
    }

    public boolean getTransactionApproved() {
        return transactionApproved;
    }

    public void setTransactionApproved(boolean transactionApproved) {
        this.transactionApproved = transactionApproved;
    }

    public String getEXactRespCode() {
        return EXactRespCode;
    }

    public void setEXactRespCode(String EXactRespCode) {
        this.EXactRespCode = EXactRespCode;
    }

    public String getEXactMessage() {
        return EXactMessage;
    }

    public void setEXactMessage(String EXactMessage) {
        this.EXactMessage = EXactMessage;
    }

    public String getBankRespCode() {
        return bankRespCode;
    }

    public void setBankRespCode(String bankRespCode) {
        this.bankRespCode = bankRespCode;
    }

    public String getBankMessage() {
        return bankMessage;
    }

    public void setBankMessage(String bankMessage) {
        this.bankMessage = bankMessage;
    }

    public String getBankRespCode2() {
        return bankRespCode2;
    }

    public void setBankRespCode2(String bankRespCode2) {
        this.bankRespCode2 = bankRespCode2;
    }

    public String getSequenceNo() {
        return sequenceNo;
    }

    public void setSequenceNo(String sequenceNo) {
        this.sequenceNo = sequenceNo;
    }

    public String getAVS() {
        return AVS;
    }

    public void setAVS(String AVS) {
        this.AVS = AVS;
    }

    public String getCVV2() {
        return CVV2;
    }

    public void setCVV2(String CVV2) {
        this.CVV2 = CVV2;
    }

    public String getRetrievalRefNo() {
        return retrievalRefNo;
    }

    public void setRetrievalRefNo(String retrievalRefNo) {
        this.retrievalRefNo = retrievalRefNo;
    }

    public String getCAVVResponse() {
        return CAVVResponse;
    }

    public void setCAVVResponse(String CAVVResponse) {
        this.CAVVResponse = CAVVResponse;
    }

    public String getAmountRequested() {
        return amountRequested;
    }

    public void setAmountRequested(String amountRequested) {
        this.amountRequested = amountRequested;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public String getMerchantCity() {
        return merchantCity;
    }

    public void setMerchantCity(String merchantCity) {
        this.merchantCity = merchantCity;
    }

    public String getMerchantProvince() {
        return merchantProvince;
    }

    public void setMerchantProvince(String merchantProvince) {
        this.merchantProvince = merchantProvince;
    }

    public String getMerchantCountry() {
        return merchantCountry;
    }

    public void setMerchantCountry(String merchantCountry) {
        this.merchantCountry = merchantCountry;
    }

    public String getMerchantPostal() {
        return merchantPostal;
    }

    public void setMerchantPostal(String merchantPostal) {
        this.merchantPostal = merchantPostal;
    }

    public String getMerchantURL() {
        return merchantURL;
    }

    public void setMerchantURL(String merchantURL) {
        this.merchantURL = merchantURL;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public String getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(String previousBalance) {
        this.previousBalance = previousBalance;
    }

    public String getCTR() {
        return CTR;
    }

    public void setCTR(String CTR) {
        this.CTR = CTR;
    }
}
