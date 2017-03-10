package com.connectto.wallet.model.transaction.transfer;

import com.connectto.wallet.model.transaction.purchase.PurchaseTicket;
import com.connectto.wallet.model.transaction.purchase.TransactionPurchaseProcess;
import com.connectto.wallet.model.transaction.purchase.TransactionPurchaseTax;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionState;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Serozh on 11/2/2016.
 */
public class TransferTransaction {

    private Long id;
    //
    private Long transferTicketId;
    private TransferTicket transferTicket;
    //from
    private Long coreSystemAdminId;
    private Long walletSetupId;

    //to
    private Long walletId;

    private Date actionDate;

    //1000USD transfer amount
    private Double transferAmount;
    private CurrencyType transferAmountCurrencyType;

    private boolean isEncoded;


    /*
     * #################################################################################################################
     * ########################################        GETTER & SETTER       ###########################################
     * #################################################################################################################
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTransferTicketId() {
        return transferTicketId;
    }

    public void setTransferTicketId(Long transferTicketId) {
        this.transferTicketId = transferTicketId;
    }

    public TransferTicket getTransferTicket() {
        return transferTicket;
    }

    public void setTransferTicket(TransferTicket transferTicket) {
        this.transferTicket = transferTicket;
    }

    public Long getCoreSystemAdminId() {
        return coreSystemAdminId;
    }

    public void setCoreSystemAdminId(Long coreSystemAdminId) {
        this.coreSystemAdminId = coreSystemAdminId;
    }

    public Long getWalletSetupId() {
        return walletSetupId;
    }

    public void setWalletSetupId(Long walletSetupId) {
        this.walletSetupId = walletSetupId;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Date getActionDate() {
        return actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public Double getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(Double transferAmount) {
        this.transferAmount = transferAmount;
    }

    public CurrencyType getTransferAmountCurrencyType() {
        return transferAmountCurrencyType;
    }

    public void setTransferAmountCurrencyType(CurrencyType transferAmountCurrencyType) {
        this.transferAmountCurrencyType = transferAmountCurrencyType;
    }

    public boolean getIsEncoded() {
        return isEncoded;
    }

    public void setIsEncoded(boolean encoded) {
        isEncoded = encoded;
    }

}
