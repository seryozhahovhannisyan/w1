package com.connectto.wallet.model.wallet;

import com.connectto.wallet.model.wallet.lcp.CurrencyType;

import java.util.Date;

/**
 * Created by htdev001 on 9/11/14.
 */
public class ExchangeRate {

    private Long id;
    private CurrencyType oneCurrency;
    private CurrencyType toCurrency;
    private Double buy;
    private Double sell;
    private Date updatedDate;

    private int sourceId;
    private ExchangeSource source;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CurrencyType getOneCurrency() {
        return oneCurrency;
    }

    public void setOneCurrency(CurrencyType oneCurrency) {
        this.oneCurrency = oneCurrency;
    }

    public CurrencyType getToCurrency() {
        return toCurrency;
    }

    public void setToCurrency(CurrencyType toCurrency) {
        this.toCurrency = toCurrency;
    }

    public Double getBuy() {
        return buy;
    }

    public void setBuy(Double buy) {
        this.buy = buy;
    }

    public Double getSell() {
        return sell;
    }

    public void setSell(Double sell) {
        this.sell = sell;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public int getSourceId() {
        return sourceId;
    }

    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public ExchangeSource getSource() {
        return source;
    }

    public void setSource(ExchangeSource source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return "ExchangeRate{" +
                "id=" + id +
                ", oneCurrency=" + oneCurrency +
                ", toCurrency=" + toCurrency +
                ", buy=" + buy +
                ", sell=" + sell +
                ", updatedDate=" + updatedDate +
                ", sourceId=" + sourceId +
                ", source=" + source +
                '}';
    }
}
