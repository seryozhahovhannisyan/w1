package com.connectto.wallet.model.transaction.merchant;

import java.util.Date;

/**
 * Created by Serozh on 12/25/2016.
 */
public class TransactionMerchantDto {

    private Long itemId;
    private String name;
    private String description;

    private Date startAt;
    private Date endAt;

    private TransactionMerchantTaxDto merchantTaxDto;
    private Long merchantTaxDtoId;

    /*
    * #################################################################################################################
    * ########################################        GETTER & SETTER       ###########################################
    * #################################################################################################################
    */

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

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public TransactionMerchantTaxDto getMerchantTaxDto() {
        return merchantTaxDto;
    }

    public void setMerchantTaxDto(TransactionMerchantTaxDto merchantTaxDto) {
        this.merchantTaxDto = merchantTaxDto;
    }

    public Long getMerchantTaxDtoId() {
        return merchantTaxDtoId;
    }

    public void setMerchantTaxDtoId(Long merchantTaxDtoId) {
        this.merchantTaxDtoId = merchantTaxDtoId;
    }
}
