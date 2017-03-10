package com.connectto.wallet.model.transaction.merchant.withdraw;

import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionState;

import java.util.Date;

/**
 * Created by Serozh on 11/2/2016.
 */
public class TransactionWithdraw {

    private Long id;
    // Pending, Cancel, approve
    private TransactionState finalState;
    //from
    private Long walletId;
    //to
    private Long setupId;

    private Date openedAt;
    private Date closedAt;

    //100 USD transaction amount & currency type by selected currency type
    private MerchantWithdraw merchantWithdraw;
    //100 USD
    private Double withdrawAmount;
    private CurrencyType withdrawAmountCurrencyType;
    //1 USD
    private Double withdrawMerchantTotalTax;
    private CurrencyType withdrawMerchantTotalTaxCurrencyType;
    //94 USD = (94-4 ExT)* 480 AMD = 43200 AMD wallet total including all tax
    private Double walletTotalPrice;
    private CurrencyType walletTotalPriceCurrencyType;
    //2 ReT + 3 WsDT = 5 USD = (5+4 ExT) = 9 USD setup total including tax
    private Double setupTotalAmount;
    private CurrencyType setupTotalAmountCurrencyType;

    //TransactionWithdrawProcess
    private TransactionWithdrawProcess processStart;
    private TransactionWithdrawProcess processEnd;
    //
    private TransactionWithdrawTax tax;
    //
    private Long merchantWithdrawId;
    private Long processStartId;
    private Long processEndId;
    private Long taxId;

    private boolean isEncoded;
    private String orderCode;

    /*
     * #################################################################################################################
     * ########################################        GETTER & SETTER       ###########################################
     * #################################################################################################################
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionState getFinalState() {
        return finalState;
    }

    public void setFinalState(TransactionState finalState) {
        this.finalState = finalState;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Long getSetupId() {
        return setupId;
    }

    public void setSetupId(Long setupId) {
        this.setupId = setupId;
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

    public MerchantWithdraw getMerchantWithdraw() {
        return merchantWithdraw;
    }

    public void setMerchantWithdraw(MerchantWithdraw merchantWithdraw) {
        this.merchantWithdraw = merchantWithdraw;
    }

    public Double getWithdrawAmount() {
        return withdrawAmount;
    }

    public void setWithdrawAmount(Double withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    public CurrencyType getWithdrawAmountCurrencyType() {
        return withdrawAmountCurrencyType;
    }

    public void setWithdrawAmountCurrencyType(CurrencyType withdrawAmountCurrencyType) {
        this.withdrawAmountCurrencyType = withdrawAmountCurrencyType;
    }

    public Double getWithdrawMerchantTotalTax() {
        return withdrawMerchantTotalTax;
    }

    public void setWithdrawMerchantTotalTax(Double withdrawMerchantTotalTax) {
        this.withdrawMerchantTotalTax = withdrawMerchantTotalTax;
    }

    public CurrencyType getWithdrawMerchantTotalTaxCurrencyType() {
        return withdrawMerchantTotalTaxCurrencyType;
    }

    public void setWithdrawMerchantTotalTaxCurrencyType(CurrencyType withdrawMerchantTotalTaxCurrencyType) {
        this.withdrawMerchantTotalTaxCurrencyType = withdrawMerchantTotalTaxCurrencyType;
    }

    public Double getWalletTotalPrice() {
        return walletTotalPrice;
    }

    public void setWalletTotalPrice(Double walletTotalPrice) {
        this.walletTotalPrice = walletTotalPrice;
    }

    public CurrencyType getWalletTotalPriceCurrencyType() {
        return walletTotalPriceCurrencyType;
    }

    public void setWalletTotalPriceCurrencyType(CurrencyType walletTotalPriceCurrencyType) {
        this.walletTotalPriceCurrencyType = walletTotalPriceCurrencyType;
    }

    public Double getSetupTotalAmount() {
        return setupTotalAmount;
    }

    public void setSetupTotalAmount(Double setupTotalAmount) {
        this.setupTotalAmount = setupTotalAmount;
    }

    public CurrencyType getSetupTotalAmountCurrencyType() {
        return setupTotalAmountCurrencyType;
    }

    public void setSetupTotalAmountCurrencyType(CurrencyType setupTotalAmountCurrencyType) {
        this.setupTotalAmountCurrencyType = setupTotalAmountCurrencyType;
    }

    public TransactionWithdrawProcess getProcessStart() {
        return processStart;
    }

    public void setProcessStart(TransactionWithdrawProcess processStart) {
        this.processStart = processStart;
    }

    public TransactionWithdrawProcess getProcessEnd() {
        return processEnd;
    }

    public void setProcessEnd(TransactionWithdrawProcess processEnd) {
        this.processEnd = processEnd;
    }

    public TransactionWithdrawTax getTax() {
        return tax;
    }

    public void setTax(TransactionWithdrawTax tax) {
        this.tax = tax;
    }

    public Long getMerchantWithdrawId() {
        return merchantWithdrawId;
    }

    public void setMerchantWithdrawId(Long merchantWithdrawId) {
        this.merchantWithdrawId = merchantWithdrawId;
    }

    public Long getProcessStartId() {
        return processStartId;
    }

    public void setProcessStartId(Long processStartId) {
        this.processStartId = processStartId;
    }

    public Long getProcessEndId() {
        return processEndId;
    }

    public void setProcessEndId(Long processEndId) {
        this.processEndId = processEndId;
    }

    public Long getTaxId() {
        return taxId;
    }

    public void setTaxId(Long taxId) {
        this.taxId = taxId;
    }

    public boolean isEncoded() {
        return isEncoded;
    }

    public void setEncoded(boolean encoded) {
        isEncoded = encoded;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
