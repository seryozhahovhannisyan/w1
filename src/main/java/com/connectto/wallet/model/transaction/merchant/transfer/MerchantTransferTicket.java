package com.connectto.wallet.model.transaction.merchant.transfer;


/**
 * Created by Serozh on 10/19/2016.
 */
public class MerchantTransferTicket {

    private Long id;
    private Long merchantTransactionId;
    private Long merchantCompanyId;
    private Long walletId;
    private String name;
    private String description;
    //
    private String secretKey;
    private String clientKey;

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

    public Long getMerchantTransactionId() {
        return merchantTransactionId;
    }

    public void setMerchantTransactionId(Long merchantTransactionId) {
        this.merchantTransactionId = merchantTransactionId;
    }

    public Long getMerchantCompanyId() {
        return merchantCompanyId;
    }

    public void setMerchantCompanyId(Long merchantCompanyId) {
        this.merchantCompanyId = merchantCompanyId;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
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

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }
}