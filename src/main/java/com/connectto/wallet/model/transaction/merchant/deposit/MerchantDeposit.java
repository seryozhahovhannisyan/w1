package com.connectto.wallet.model.transaction.merchant.deposit;


import java.util.Date;

/**
 * Created by Serozh on 10/19/2016.
 */
public class MerchantDeposit {

    private Long id;
    private Long itemId;
    private String name;
    private String description;

    private Date startAt;
    private Date rationalStopAt;
    private Date endAt;

    private MerchantDepositTax merchantDepositTax;
    private Long merchantDepositTaxId;

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

    public MerchantDepositTax getMerchantDepositTax() {
        return merchantDepositTax;
    }

    public void setMerchantDepositTax(MerchantDepositTax merchantDepositTax) {
        this.merchantDepositTax = merchantDepositTax;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getRationalStopAt() {
        return rationalStopAt;
    }

    public void setRationalStopAt(Date rationalStopAt) {
        this.rationalStopAt = rationalStopAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public Long getMerchantDepositTaxId() {
        return merchantDepositTaxId;
    }

    public void setMerchantDepositTaxId(Long merchantDepositTaxId) {
        this.merchantDepositTaxId = merchantDepositTaxId;
    }
}

