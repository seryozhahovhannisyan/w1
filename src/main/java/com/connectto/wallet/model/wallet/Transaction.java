package com.connectto.wallet.model.wallet;

import com.connectto.general.model.WalletSetup;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionState;
import com.connectto.wallet.model.wallet.lcp.TransactionType;

import java.util.Date;
import java.util.List;

/**
 * Created by htdev001 on 8/25/14.
 */
public class Transaction {

    private Long id;

    private Long frozenTransactionId;
    private Transaction frozenTransaction;

    private TransactionState state;
    private TransactionState actionState;

    private Date openedAt;
    private Date closedAt;

    //transaction amount & currency type by selected currency type
    private Double productAmount;
    private CurrencyType productCurrencyType;

    private Double setupAmount;
    private CurrencyType setupAmountCurrencyType;

    private Double fromTotalPrice;
    private CurrencyType fromTotalPriceCurrencyType;

    private Double toTotalPrice;
    private CurrencyType toTotalPriceCurrencyType;

    private TransactionType transactionType;
    private String orderCode;
    private String message;
    private String sessionId;

    private List<TransactionData> transactionDatas;

    private TransactionProcess fromTransactionProcess;
    private TransactionProcess toTransactionProcess;

    private Long fromTransactionProcessId;
    private Long toTransactionProcessId;
    //Many to one
    private Long fromWalletId;//from id
    private Wallet fromWallet;//from
    // Many to one
    private Long fromWalletSetupId;//from id
    private WalletSetup fromWalletSetup;//from

    //Many to one
    private Long toWalletId;//to
    private Wallet toWallet;
    //Many to one
    private Long toWalletSetupId;//to
    private WalletSetup toWalletSetup;

    private List<TransactionDispute> transactionDisputes;
    private List<Long> transactionDisputeIdes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(Double productAmount) {
        this.productAmount = productAmount;
    }

    public CurrencyType getProductCurrencyType() {
        return productCurrencyType;
    }

    public void setProductCurrencyType(CurrencyType productCurrencyType) {
        this.productCurrencyType = productCurrencyType;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<TransactionData> getTransactionDatas() {
        return transactionDatas;
    }

    public void setTransactionDatas(List<TransactionData> transactionDatas) {
        this.transactionDatas = transactionDatas;
    }

    public Long getFromWalletId() {
        return fromWalletId;
    }

    public void setFromWalletId(Long fromWalletId) {
        this.fromWalletId = fromWalletId;
    }

    public Wallet getFromWallet() {
        return fromWallet;
    }

    public void setFromWallet(Wallet fromWallet) {
        this.fromWallet = fromWallet;
    }

    public Long getFromWalletSetupId() {
        return fromWalletSetupId;
    }

    public void setFromWalletSetupId(Long fromWalletSetupId) {
        this.fromWalletSetupId = fromWalletSetupId;
    }

    public WalletSetup getFromWalletSetup() {
        return fromWalletSetup;
    }

    public void setFromWalletSetup(WalletSetup fromWalletSetup) {
        this.fromWalletSetup = fromWalletSetup;
    }

    public Long getToWalletId() {
        return toWalletId;
    }

    public void setToWalletId(Long toWalletId) {
        this.toWalletId = toWalletId;
    }

    public Wallet getToWallet() {
        return toWallet;
    }

    public void setToWallet(Wallet toWallet) {
        this.toWallet = toWallet;
    }

    public Long getToWalletSetupId() {
        return toWalletSetupId;
    }

    public void setToWalletSetupId(Long toWalletSetupId) {
        this.toWalletSetupId = toWalletSetupId;
    }

    public WalletSetup getToWalletSetup() {
        return toWalletSetup;
    }

    public void setToWalletSetup(WalletSetup toWalletSetup) {
        this.toWalletSetup = toWalletSetup;
    }

    public List<TransactionDispute> getTransactionDisputes() {
        return transactionDisputes;
    }

    public void setTransactionDisputes(List<TransactionDispute> transactionDisputes) {
        this.transactionDisputes = transactionDisputes;
    }

    public List<Long> getTransactionDisputeIdes() {
        return transactionDisputeIdes;
    }

    public void setTransactionDisputeIdes(List<Long> transactionDisputeIdes) {
        this.transactionDisputeIdes = transactionDisputeIdes;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Double getSetupAmount() {
        return setupAmount;
    }

    public void setSetupAmount(Double setupAmount) {
        this.setupAmount = setupAmount;
    }

    public CurrencyType getSetupAmountCurrencyType() {
        return setupAmountCurrencyType;
    }

    public void setSetupAmountCurrencyType(CurrencyType setupAmountCurrencyType) {
        this.setupAmountCurrencyType = setupAmountCurrencyType;
    }

    public Double getFromTotalPrice() {
        return fromTotalPrice;
    }

    public void setFromTotalPrice(Double fromTotalPrice) {
        this.fromTotalPrice = fromTotalPrice;
    }

    public Double getToTotalPrice() {
        return toTotalPrice;
    }

    public void setToTotalPrice(Double toTotalPrice) {
        this.toTotalPrice = toTotalPrice;
    }

    public TransactionState getState() {
        return state;
    }

    public void setState(TransactionState state) {
        this.state = state;
    }

    public Date getOpenedAt() {
        return openedAt;
    }

    public void setOpenedAt(Date openedAt) {
        this.openedAt = openedAt;
    }

    public Date getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(Date closedAt) {
        this.closedAt = closedAt;
    }

    public TransactionState getActionState() {
        return actionState;
    }

    public void setActionState(TransactionState actionState) {
        this.actionState = actionState;
    }

    public CurrencyType getFromTotalPriceCurrencyType() {
        return fromTotalPriceCurrencyType;
    }

    public void setFromTotalPriceCurrencyType(CurrencyType fromTotalPriceCurrencyType) {
        this.fromTotalPriceCurrencyType = fromTotalPriceCurrencyType;
    }

    public CurrencyType getToTotalPriceCurrencyType() {
        return toTotalPriceCurrencyType;
    }

    public void setToTotalPriceCurrencyType(CurrencyType toTotalPriceCurrencyType) {
        this.toTotalPriceCurrencyType = toTotalPriceCurrencyType;
    }

    public TransactionProcess getFromTransactionProcess() {
        return fromTransactionProcess;
    }

    public void setFromTransactionProcess(TransactionProcess fromTransactionProcess) {
        this.fromTransactionProcess = fromTransactionProcess;
    }

    public TransactionProcess getToTransactionProcess() {
        return toTransactionProcess;
    }

    public void setToTransactionProcess(TransactionProcess toTransactionProcess) {
        this.toTransactionProcess = toTransactionProcess;
    }

    public Long getFromTransactionProcessId() {
        return fromTransactionProcessId;
    }

    public void setFromTransactionProcessId(Long fromTransactionProcessId) {
        this.fromTransactionProcessId = fromTransactionProcessId;
    }

    public Long getToTransactionProcessId() {
        return toTransactionProcessId;
    }

    public void setToTransactionProcessId(Long toTransactionProcessId) {
        this.toTransactionProcessId = toTransactionProcessId;
    }

    public Long getFrozenTransactionId() {
        return frozenTransactionId;
    }

    public void setFrozenTransactionId(Long frozenTransactionId) {
        this.frozenTransactionId = frozenTransactionId;
    }

    public Transaction getFrozenTransaction() {
        return frozenTransaction;
    }

    public void setFrozenTransaction(Transaction frozenTransaction) {
        this.frozenTransaction = frozenTransaction;
    }
}
