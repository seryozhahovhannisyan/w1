package com.connectto.wallet.web.action.wallet.dto;

import com.connectto.general.model.PartitionDto;
import com.connectto.general.model.UserDto;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;

import java.util.Date;

/**
 * Created by htdev01 on 11/3/15.
 */
public class TransactionDto {
    //
    private Long transactionId;
    private String orderCode;

    private Long userWalletId;
    private UserDto userDto;

    private Integer walletSetupId;
    private PartitionDto partitionDto;

    private boolean isDebited;
    private boolean isCredited;
    private boolean allowDelay;
    //
    private Double price;
    private CurrencyType priceCurrency;
    //old
    private Double amount;
    private int currencyType;
    //
    private Date openedAt;
    private Date closedAt;

    //
    private int disputeState;
    private Long disputeId;

    public TransactionDto() {
    }

    public TransactionDto(Double amount, Integer currencyType) {
        this.price = amount;
        this.priceCurrency = CurrencyType.valueOf(currencyType);
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    public PartitionDto getPartitionDto() {
        return partitionDto;
    }

    public void setPartitionDto(PartitionDto partitionDto) {
        this.partitionDto = partitionDto;
    }

    public boolean isDebited() {
        return isDebited;
    }

    public void setDebited(boolean isDebited) {
        this.isDebited = isDebited;
    }

    public boolean isCredited() {
        return isCredited;
    }

    public void setCredited(boolean isCredited) {
        this.isCredited = isCredited;
    }

    public boolean isAllowDelay() {
        return allowDelay;
    }

    public void setAllowDelay(boolean allowDelay) {
        this.allowDelay = allowDelay;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public CurrencyType getPriceCurrency() {
        return priceCurrency;
    }

    public void setPriceCurrency(CurrencyType priceCurrency) {
        this.priceCurrency = priceCurrency;
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

    public int getDisputeState() {
        return disputeState;
    }

    public void setDisputeState(int disputeState) {
        this.disputeState = disputeState;
    }

    public Long getDisputeId() {
        return disputeId;
    }

    public void setDisputeId(Long disputeId) {
        this.disputeId = disputeId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public int getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(int currencyType) {
        this.currencyType = currencyType;
    }

    public Long getUserWalletId() {
        return userWalletId;
    }

    public void setUserWalletId(Long userWalletId) {
        this.userWalletId = userWalletId;
    }

    public Integer getWalletSetupId() {
        return walletSetupId;
    }

    public void setWalletSetupId(Integer walletSetupId) {
        this.walletSetupId = walletSetupId;
    }
}
