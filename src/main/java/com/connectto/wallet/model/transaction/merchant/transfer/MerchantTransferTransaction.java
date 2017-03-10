package com.connectto.wallet.model.transaction.merchant.transfer;

import com.connectto.general.model.TsmCompany;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;

import java.util.Date;

/**
 * Created by Serozh on 11/2/2016.
 */
public class MerchantTransferTransaction {

    private Long id;
    //
    private Long transferTicketId;
    private MerchantTransferTicket transferTicket;
    //from
    private TsmCompany tsmCompany;
    private Integer tsmCompanyId;
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

    public MerchantTransferTicket getTransferTicket() {
        return transferTicket;
    }

    public void setTransferTicket(MerchantTransferTicket transferTicket) {
        this.transferTicket = transferTicket;
    }

    public TsmCompany getTsmCompany() {
        return tsmCompany;
    }

    public void setTsmCompany(TsmCompany tsmCompany) {
        this.tsmCompany = tsmCompany;
    }

    public Integer getTsmCompanyId() {
        return tsmCompanyId;
    }

    public void setTsmCompanyId(Integer tsmCompanyId) {
        this.tsmCompanyId = tsmCompanyId;
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
