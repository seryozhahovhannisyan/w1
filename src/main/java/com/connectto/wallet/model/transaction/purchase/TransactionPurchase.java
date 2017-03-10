package com.connectto.wallet.model.transaction.purchase;

import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionState;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Serozh on 11/2/2016.
 */
public class TransactionPurchase {

    private Long id;
    //PURCHASE_FREEZE, PURCHASE_CHARGE, PURCHASE_CANCEL, FUTURE_PAYMENT
    private TransactionState finalState;
    //from
    private Long walletId;
    //to
    private Long setupId;
    private int partitionId;

    private Date openedAt;
    private Date closedAt;

    //1000USD transaction amount & currency type by selected currency type
    private Double purchaseAmount;
    private CurrencyType purchaseCurrencyType;
    //580.800 AMD wallet total including all tax
    private Double walletTotalPrice;
    private CurrencyType walletTotalPriceCurrencyType;
    //1210AMD setup total including tax
    private Double setupTotalAmount;
    private CurrencyType setupTotalAmountCurrencyType;

    //Ordered ticket for purchase
    private List<PurchaseTicket> tickets;
    //TransactionPurchaseProcess
    private TransactionPurchaseProcess processStart;
    private TransactionPurchaseProcess processEnd;
    //
    private TransactionPurchaseTax tax;

    private Long processStartId;
    private Long processEndId;
    private Long taxId;

    private boolean isEncoded;
    private String orderCode;
    private String message;
    private String sessionId;

    public void addTicket(PurchaseTicket ticket) {
        if(this.tickets == null){
            this.tickets = new ArrayList<PurchaseTicket>(5);
        }
        this.tickets.add(ticket);
    }

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

    public TransactionState getFinalState() {
        return finalState;
    }

    public void setFinalState(TransactionState finalState) {
        this.finalState = finalState;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Long getSetupId() {
        return setupId;
    }

    public void setSetupId(Long setupId) {
        this.setupId = setupId;
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

    public Double getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(Double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public CurrencyType getPurchaseCurrencyType() {
        return purchaseCurrencyType;
    }

    public void setPurchaseCurrencyType(CurrencyType purchaseCurrencyType) {
        this.purchaseCurrencyType = purchaseCurrencyType;
    }

    public Double getWalletTotalPrice() {
        return walletTotalPrice;
    }

    public void setWalletTotalPrice(Double walletTotalPrice) {
        this.walletTotalPrice = walletTotalPrice;
    }

    public CurrencyType getWalletTotalPriceCurrencyType() {
        return walletTotalPriceCurrencyType;
    }

    public void setWalletTotalPriceCurrencyType(CurrencyType walletTotalPriceCurrencyType) {
        this.walletTotalPriceCurrencyType = walletTotalPriceCurrencyType;
    }

    public Double getSetupTotalAmount() {
        return setupTotalAmount;
    }

    public void setSetupTotalAmount(Double setupTotalAmount) {
        this.setupTotalAmount = setupTotalAmount;
    }

    public CurrencyType getSetupTotalAmountCurrencyType() {
        return setupTotalAmountCurrencyType;
    }

    public void setSetupTotalAmountCurrencyType(CurrencyType setupTotalAmountCurrencyType) {
        this.setupTotalAmountCurrencyType = setupTotalAmountCurrencyType;
    }

    public List<PurchaseTicket> getTickets() {
        return tickets;
    }

    public void setTickets(List<PurchaseTicket> tickets) {
        this.tickets = tickets;
    }

    public TransactionPurchaseProcess getProcessStart() {
        return processStart;
    }

    public void setProcessStart(TransactionPurchaseProcess processStart) {
        this.processStart = processStart;
    }

    public TransactionPurchaseProcess getProcessEnd() {
        return processEnd;
    }

    public void setProcessEnd(TransactionPurchaseProcess processEnd) {
        this.processEnd = processEnd;
    }

    public TransactionPurchaseTax getTax() {
        return tax;
    }

    public void setTax(TransactionPurchaseTax tax) {
        this.tax = tax;
    }

    public Long getProcessStartId() {
        return processStartId;
    }

    public void setProcessStartId(Long processStartId) {
        this.processStartId = processStartId;
    }

    public Long getProcessEndId() {
        return processEndId;
    }

    public void setProcessEndId(Long processEndId) {
        this.processEndId = processEndId;
    }

    public Long getTaxId() {
        return taxId;
    }

    public void setTaxId(Long taxId) {
        this.taxId = taxId;
    }

    public boolean getIsEncode() {
        return isEncoded;
    }

    public void setIsEncode(boolean isEncoded) {
        this.isEncoded = isEncoded;
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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(int partitionId) {
        this.partitionId = partitionId;
    }
}
