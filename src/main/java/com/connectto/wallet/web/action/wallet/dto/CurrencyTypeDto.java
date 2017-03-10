package com.connectto.wallet.web.action.wallet.dto;

import com.connectto.wallet.model.wallet.lcp.CurrencyType;

/**
 * Created by htdev01 on 12/7/15.
 */
public class CurrencyTypeDto {

    private int id;
    private String code;
    private String name;

    public CurrencyTypeDto(CurrencyType currencyType) {
        if(currencyType == null){
            return;
        }
        this.id = currencyType.getId();
        this.code = currencyType.getCode();
        this.name = currencyType.getName();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
