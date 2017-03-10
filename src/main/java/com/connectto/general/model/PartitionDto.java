package com.connectto.general.model;

/**
 * Created by Serozh on 2/16/16.
 */
public class PartitionDto {

    private int partitionId;
    private int walletSetupId;
    private String name;
    private String icon;

    public int getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(int partitionId) {
        this.partitionId = partitionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
