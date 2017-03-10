package com.connectto.wallet.model.wallet;

import com.connectto.wallet.model.wallet.lcp.TransactionType;

import java.util.Date;

/**
 * Created by Serozh on 2/4/16.
 */
public class CreditCard {

    private Long id;
    private Long walletId;
    private Long userId;
    //
    private String holderName;
    private String number;
    private Date expiryDate;
    private String cvv;
    private TransactionType transactionType;
    //optional billingAddress
    private String country;
    private String zip;
    private String state;
    private String city;
    //
    private Boolean isEnabled;
    private Boolean isDeleted;
    private Boolean isBlocked;
    private Integer priority;
    //
    private Date createdAt;
    private Date updatedAt;
    private String updatedDesc;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getHolderName() {
        return holderName;
    }

    public void setHolderName(String holderName) {
        this.holderName = holderName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedDesc() {
        return updatedDesc;
    }

    public void setUpdatedDesc(String updatedDesc) {
        this.updatedDesc = updatedDesc;
    }

    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public void setIsBlocked(Boolean blocked) {
        isBlocked = blocked;
    }

    public void initDesc(CreditCard updatedCreditCard) {

        StringBuilder stringBuilder = new StringBuilder("; Credit card updated at ");
        stringBuilder.append(updatedAt);

        if (city != null ? !city.equals(updatedCreditCard.city) : updatedCreditCard.city != null) stringBuilder.append(" city ").append(city).append(" -> ").append(updatedCreditCard.city);
        if (country != null ? !country.equals(updatedCreditCard.country) : updatedCreditCard.country != null) stringBuilder.append(" country ").append(country).append(" -> ").append(updatedCreditCard.country);
        if (cvv != null ? !cvv.equals(updatedCreditCard.cvv) : updatedCreditCard.cvv != null) stringBuilder.append(" cvv ").append(cvv).append(" -> ").append(updatedCreditCard.cvv);
        if (expiryDate != null ? !expiryDate.equals(updatedCreditCard.expiryDate) : updatedCreditCard.expiryDate != null) stringBuilder.append(" expiryDate ").append(expiryDate).append(" -> ").append(updatedCreditCard.expiryDate);
        if (holderName != null ? !holderName.equals(updatedCreditCard.holderName) : updatedCreditCard.holderName != null) stringBuilder.append(" holderName ").append(holderName).append(" -> ").append(updatedCreditCard.holderName);
        if (number != null ? !number.equals(updatedCreditCard.number) : updatedCreditCard.number != null) stringBuilder.append(" number ").append(number).append(" -> ").append(updatedCreditCard.number);
        if (state != null ? !state.equals(updatedCreditCard.state) : updatedCreditCard.state != null) stringBuilder.append(" state ").append(state).append(" -> ").append(updatedCreditCard.state);
        if (transactionType != updatedCreditCard.transactionType) stringBuilder.append(" transactionType ").append(transactionType).append(" -> ").append(updatedCreditCard.transactionType);
        if (zip != null ? !zip.equals(updatedCreditCard.zip) : updatedCreditCard.zip != null) stringBuilder.append(" zip ").append(zip).append(" -> ").append(updatedCreditCard.zip);

        updatedDesc = stringBuilder.toString();
    }


}
