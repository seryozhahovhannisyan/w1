package com.connectto.general.model;

import java.util.List;

/**
 * Created by htdev001 on 6/4/15.
 */
public class TsmCompany {

    private int id;
    private int partitionId;
    private int convertToMerchant;
    private String secretKey;
    private String clientKey;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(int partitionId) {
        this.partitionId = partitionId;
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

    public int getConvertToMerchant() {
        return convertToMerchant;
    }

    public void setConvertToMerchant(int convertToMerchant) {
        this.convertToMerchant = convertToMerchant;
    }

    @Override
    public String toString() {
        return "TsmCompany{" +
                "id=" + id +
                ", partitionId=" + partitionId +
                ", convertToMerchant=" + convertToMerchant +
                ", secretKey='" + secretKey + '\'' +
                ", clientKey='" + clientKey + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
