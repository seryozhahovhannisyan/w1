package com.connectto.wallet.model.transaction.transfer;


/**
 * Created by Serozh on 10/19/2016.
 */
public class TransferTicket {

    private Long id;
    private Long itemId;
    private Long walletId;
    private String name;
    private String description;
    //
    private String systemAdminUsername;
    private String systemAdminPassword;
    private Integer systemAdminPartitionId;


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

    public String getSystemAdminUsername() {
        return systemAdminUsername;
    }

    public void setSystemAdminUsername(String systemAdminUsername) {
        this.systemAdminUsername = systemAdminUsername;
    }

    public String getSystemAdminPassword() {
        return systemAdminPassword;
    }

    public void setSystemAdminPassword(String systemAdminPassword) {
        this.systemAdminPassword = systemAdminPassword;
    }

    public Integer getSystemAdminPartitionId() {
        return systemAdminPartitionId;
    }

    public void setSystemAdminPartitionId(Integer systemAdminPartitionId) {
        this.systemAdminPartitionId = systemAdminPartitionId;
    }
}