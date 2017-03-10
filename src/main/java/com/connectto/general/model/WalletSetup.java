package com.connectto.general.model;

import com.connectto.general.util.Utils;
import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by htdev001 on 11/21/14.
 */
public class WalletSetup {

    private Long id;
    private int partitionId;

    private CurrencyType currencyType;
    private Double balance;
    private Double frozenAmount;
    private Double receivingAmount;
    private Double transferTaxAmount;

    private Double transferFeePercent;
    private Double transferMinFee;
    private Double transferMaxFee;

    private Double receiverFeePercent;
    private Double receiverMinFee;
    private Double receiverMaxFee;

    private Double merchantDepositFeePercent;
    private Double merchantDepositMinFee;
    private Double merchantDepositMaxFee;

    private Double merchantWithdrawFeePercent;
    private Double merchantWithdrawMinFee;
    private Double merchantWithdrawMaxFee;

    private Double exchangeTransferFeePercent;
    private Double exchangeTransferMinFee;
    private Double exchangeTransferMaxFee;

    private Double exchangeReceiverFeePercent;
    private Double exchangeReceiverMinFee;
    private Double exchangeReceiverMaxFee;

    private List<CurrencyType> availableRates;
    private String availableRateValues;

    private List<TransactionType> availableCards;
    private String availableCardValues;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(int partitionId) {
        this.partitionId = partitionId;
    }

    public CurrencyType getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(CurrencyType currencyType) {
        this.currencyType = currencyType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Double getTransferFeePercent() {
        return transferFeePercent;
    }

    public void setTransferFeePercent(Double transferFeePercent) {
        this.transferFeePercent = transferFeePercent;
    }

    public Double getTransferMinFee() {
        return transferMinFee;
    }

    public void setTransferMinFee(Double transferMinFee) {
        this.transferMinFee = transferMinFee;
    }

    public Double getTransferMaxFee() {
        return transferMaxFee;
    }

    public void setTransferMaxFee(Double transferMaxFee) {
        this.transferMaxFee = transferMaxFee;
    }

    public Double getReceiverFeePercent() {
        return receiverFeePercent;
    }

    public void setReceiverFeePercent(Double receiverFeePercent) {
        this.receiverFeePercent = receiverFeePercent;
    }

    public Double getReceiverMinFee() {
        return receiverMinFee;
    }

    public void setReceiverMinFee(Double receiverMinFee) {
        this.receiverMinFee = receiverMinFee;
    }

    public Double getReceiverMaxFee() {
        return receiverMaxFee;
    }

    public void setReceiverMaxFee(Double receiverMaxFee) {
        this.receiverMaxFee = receiverMaxFee;
    }

    public Double getExchangeTransferFeePercent() {
        return exchangeTransferFeePercent;
    }

    public void setExchangeTransferFeePercent(Double exchangeTransferFeePercent) {
        this.exchangeTransferFeePercent = exchangeTransferFeePercent;
    }

    public Double getExchangeTransferMinFee() {
        return exchangeTransferMinFee;
    }

    public void setExchangeTransferMinFee(Double exchangeTransferMinFee) {
        this.exchangeTransferMinFee = exchangeTransferMinFee;
    }

    public Double getExchangeTransferMaxFee() {
        return exchangeTransferMaxFee;
    }

    public void setExchangeTransferMaxFee(Double exchangeTransferMaxFee) {
        this.exchangeTransferMaxFee = exchangeTransferMaxFee;
    }

    public Double getExchangeReceiverFeePercent() {
        return exchangeReceiverFeePercent;
    }

    public void setExchangeReceiverFeePercent(Double exchangeReceiverFeePercent) {
        this.exchangeReceiverFeePercent = exchangeReceiverFeePercent;
    }

    public Double getExchangeReceiverMinFee() {
        return exchangeReceiverMinFee;
    }

    public void setExchangeReceiverMinFee(Double exchangeReceiverMinFee) {
        this.exchangeReceiverMinFee = exchangeReceiverMinFee;
    }

    public Double getExchangeReceiverMaxFee() {
        return exchangeReceiverMaxFee;
    }

    public void setExchangeReceiverMaxFee(Double exchangeReceiverMaxFee) {
        this.exchangeReceiverMaxFee = exchangeReceiverMaxFee;
    }

    public Double getFrozenAmount() {
        return frozenAmount;
    }

    public void setFrozenAmount(Double frozenAmount) {
        this.frozenAmount = frozenAmount;
    }

    public Double getReceivingAmount() {
        return receivingAmount;
    }

    public void setReceivingAmount(Double receivingAmount) {
        this.receivingAmount = receivingAmount;
    }


    public List<CurrencyType> getAvailableRates() {
        return availableRates;
    }

    public void setAvailableRates(List<CurrencyType> availableRates) {
        this.availableRates = availableRates;
    }

    public String getAvailableRateValues() {
        return availableRateValues;
    }

    public List<CurrencyType> parseAvailableRates() {
        if(!Utils.isEmpty(availableRateValues)){
            this.availableRates = new ArrayList<CurrencyType>();

            String[] ars = availableRateValues.split(",");

            for(String s : ars){
                CurrencyType type = CurrencyType.valueOf(Integer.parseInt(s.trim()));
                if(type !=  null){
                    availableRates.add(type);
                }
            }

            return availableRates;
        }

        return null;
    }


    public boolean isCurrencyTypeSupported(int currencyTypeId) {
        if(!Utils.isEmpty(availableRateValues)){
            parseAvailableRates();
            if(Utils.isEmpty(availableRates)){
                return false;
            }
            for (CurrencyType ct : availableRates) {
                if (ct.getId() == currencyTypeId) {
                    return true;
                }
            }
        }
        return false;
    }


    public List<TransactionType> parseAvailableCards() {
        if(!Utils.isEmpty(availableCardValues)){
            this.availableCards = new ArrayList<TransactionType>();

            String[] ars = availableCardValues.split(",");

            for(String s : ars){
                TransactionType type = TransactionType.valueOf(Integer.parseInt(s));
                if(type !=  null){
                    availableCards.add(type);
                }
            }

            return availableCards;
        }

        return null;
    }

    public boolean isCreditCardSupported(int creditCardId) {
        if(!Utils.isEmpty(availableCardValues)){

            parseAvailableCards();

            if(Utils.isEmpty(availableCards)){
                return false;
            }

            for (TransactionType tt : availableCards) {
                if (tt.getId() == creditCardId) {
                    return true;
                }
            }
        }

        return false;
    }

    public void setAvailableRateValues(String availableRateValues) {
        this.availableRateValues = availableRateValues;
    }

    public List<TransactionType> getAvailableCards() {
        return availableCards;
    }

    public void setAvailableCards(List<TransactionType> availableCards) {
        this.availableCards = availableCards;
    }

    public String getAvailableCardValues() {
        return availableCardValues;
    }

    public void setAvailableCardValues(String availableCardValues) {
        this.availableCardValues = availableCardValues;
    }

    public Double getTransferTaxAmount() {
        return transferTaxAmount;
    }

    public void setTransferTaxAmount(Double transferTaxAmount) {
        this.transferTaxAmount = transferTaxAmount;
    }

    public Double getMerchantDepositFeePercent() {
        return merchantDepositFeePercent;
    }

    public void setMerchantDepositFeePercent(Double merchantDepositFeePercent) {
        this.merchantDepositFeePercent = merchantDepositFeePercent;
    }

    public Double getMerchantDepositMinFee() {
        return merchantDepositMinFee;
    }

    public void setMerchantDepositMinFee(Double merchantDepositMinFee) {
        this.merchantDepositMinFee = merchantDepositMinFee;
    }

    public Double getMerchantDepositMaxFee() {
        return merchantDepositMaxFee;
    }

    public void setMerchantDepositMaxFee(Double merchantDepositMaxFee) {
        this.merchantDepositMaxFee = merchantDepositMaxFee;
    }

    public Double getMerchantWithdrawFeePercent() {
        return merchantWithdrawFeePercent;
    }

    public void setMerchantWithdrawFeePercent(Double merchantWithdrawFeePercent) {
        this.merchantWithdrawFeePercent = merchantWithdrawFeePercent;
    }

    public Double getMerchantWithdrawMinFee() {
        return merchantWithdrawMinFee;
    }

    public void setMerchantWithdrawMinFee(Double merchantWithdrawMinFee) {
        this.merchantWithdrawMinFee = merchantWithdrawMinFee;
    }

    public Double getMerchantWithdrawMaxFee() {
        return merchantWithdrawMaxFee;
    }

    public void setMerchantWithdrawMaxFee(Double merchantWithdrawMaxFee) {
        this.merchantWithdrawMaxFee = merchantWithdrawMaxFee;
    }

}