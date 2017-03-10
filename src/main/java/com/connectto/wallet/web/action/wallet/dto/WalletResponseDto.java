package com.connectto.wallet.web.action.wallet.dto;

import com.connectto.general.model.lcp.ResponseStatus;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by htdev01 on 11/3/15.
 */
@XmlRootElement
public class WalletResponseDto {

    private TransactionDto  transactionDto;

    private Float cashAmount;

    private ResponseStatus status;

    private List<String> messages;

    public void addMessage(String message) {
        if (messages == null) {
            messages = new ArrayList<String>();
        }
        messages.add(message);
    }

    public void cleanMessages() {
        if (messages != null) {
            messages.clear();
        }
        status = null;
        transactionDto =  null;
        cashAmount = null;
    }

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public TransactionDto getTransactionDto() {
        return transactionDto;
    }

    public void setTransactionDto(TransactionDto transactionDto) {
        this.transactionDto = transactionDto;
    }

    public Float getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(Float cashAmount) {
        this.cashAmount = cashAmount;
    }

    @Override
    public String toString() {
        return "WalletResponseDto{" +
                "transactionDto=" + transactionDto +
                ", cashAmount=" + cashAmount +
                ", status=" + status +
                ", messages=" + messages +
                '}';
    }
}
