package com.connectto.wallet.model.wallet;


import com.connectto.general.model.User;
import com.connectto.wallet.model.wallet.lcp.DisputeState;

import java.util.Date;
import java.util.List;

/**
 * Created by htdev001 on 8/25/14.
 */
public class TransactionDispute {

    private Long id;
    private Date disputedAt;
    private String reason;
    private String content;
    private String answer;
    private Date answeredAt;
    //Many to one
    private Long transactionId;
    private Transaction transaction;

    //Many to one
    private Long walletExchangeId;
    private WalletExchange walletExchange;
    //One to many
    private List<Long> disputeIdes;
    private List<TransactionDispute> disputes;
    //Many to one
    private Long disputeId;
    private TransactionDispute dispute;
    //Many to one
    private Long disputedById;
    private User disputedBy;
    //Many to one
    private int answeredPartitionUserId;
    private Long answeredById;
    private User answeredBy;
    //
    private DisputeState state;
    private List<TransactionData> transactionDatas;

    /**
     * ##################################################################################################################
     * Getters Setter
     * ##################################################################################################################
     */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDisputedAt() {
        return disputedAt;
    }

    public void setDisputedAt(Date disputedAt) {
        this.disputedAt = disputedAt;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Date getAnsweredAt() {
        return answeredAt;
    }

    public void setAnsweredAt(Date answeredAt) {
        this.answeredAt = answeredAt;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public List<Long> getDisputeIdes() {
        return disputeIdes;
    }

    public void setDisputeIdes(List<Long> disputeIdes) {
        this.disputeIdes = disputeIdes;
    }

    public List<TransactionDispute> getDisputes() {
        return disputes;
    }

    public void setDisputes(List<TransactionDispute> disputes) {
        this.disputes = disputes;
    }

    public Long getDisputeId() {
        return disputeId;
    }

    public void setDisputeId(Long disputeId) {
        this.disputeId = disputeId;
    }

    public TransactionDispute getDispute() {
        return dispute;
    }

    public void setDispute(TransactionDispute dispute) {
        this.dispute = dispute;
    }

    public Long getDisputedById() {
        return disputedById;
    }

    public void setDisputedById(Long disputedById) {
        this.disputedById = disputedById;
    }

    public User getDisputedBy() {
        return disputedBy;
    }

    public void setDisputedBy(User disputedBy) {
        this.disputedBy = disputedBy;
    }

    public Long getAnsweredById() {
        return answeredById;
    }

    public void setAnsweredById(Long answeredById) {
        this.answeredById = answeredById;
    }

    public User getAnsweredBy() {
        return answeredBy;
    }

    public void setAnsweredBy(User answeredBy) {
        this.answeredBy = answeredBy;
    }

    public DisputeState getState() {
        return state;
    }

    public void setState(DisputeState state) {
        this.state = state;
    }

    public int getAnsweredPartitionUserId() {
        return answeredPartitionUserId;
    }

    public void setAnsweredPartitionUserId(int answeredPartitionUserId) {
        this.answeredPartitionUserId = answeredPartitionUserId;
    }

    public List<TransactionData> getTransactionDatas() {
        return transactionDatas;
    }

    public void setTransactionDatas(List<TransactionData> transactionDatas) {
        this.transactionDatas = transactionDatas;
    }

    public Long getWalletExchangeId() {
        return walletExchangeId;
    }

    public void setWalletExchangeId(Long walletExchangeId) {
        this.walletExchangeId = walletExchangeId;
    }

    public WalletExchange getWalletExchange() {
        return walletExchange;
    }

    public void setWalletExchange(WalletExchange walletExchange) {
        this.walletExchange = walletExchange;
    }
}
