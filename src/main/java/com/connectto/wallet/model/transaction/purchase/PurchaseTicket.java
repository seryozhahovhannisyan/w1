package com.connectto.wallet.model.transaction.purchase;

import com.connectto.wallet.model.wallet.lcp.TransactionPurchaseType;

/**
 * Created by Serozh on 10/19/2016.
 */
public class PurchaseTicket {

    private Long id;
    private Long itemId;
    private Long transactionId;
    private TransactionPurchaseType purchaseType;
    private String name;
    private String description;



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

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public TransactionPurchaseType getPurchaseType() {
        return purchaseType;
    }

    public void setPurchaseType(TransactionPurchaseType purchaseType) {
        this.purchaseType = purchaseType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}