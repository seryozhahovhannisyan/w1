package com.connectto.wallet.model.transaction.merchant.withdraw;


import java.util.Date;

/**
 * Created by Serozh on 10/19/2016.
 */
public class MerchantWithdraw {

    private Long id;
    private Long itemId;
    private String name;
    private String description;

    private Date startAt;
    private Date rationalStopAt;
    private Date endAt;

    private MerchantWithdrawTax merchantWithdrawTax;
    private Long merchantWithdrawTaxId;

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

    public MerchantWithdrawTax getMerchantWithdrawTax() {
        return merchantWithdrawTax;
    }

    public void setMerchantWithdrawTax(MerchantWithdrawTax merchantWithdrawTax) {
        this.merchantWithdrawTax = merchantWithdrawTax;
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

    public Long getMerchantWithdrawTaxId() {
        return merchantWithdrawTaxId;
    }

    public void setMerchantWithdrawTaxId(Long merchantWithdrawTaxId) {
        this.merchantWithdrawTaxId = merchantWithdrawTaxId;
    }
}

