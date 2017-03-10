package com.connectto.wallet.model.transaction.merchant.deposit;

import com.connectto.wallet.model.wallet.lcp.CurrencyType;
import com.connectto.wallet.model.wallet.lcp.TransactionState;

import java.util.Date;

/**
 * Created by Serozh on 11/2/2016.
 */
public class TransactionDeposit {

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
    private MerchantDeposit merchantDeposit;
    //100 USD
    private Double depositAmount;
    private CurrencyType depositAmountCurrencyType;
    //1 USD
    private Double depositMerchantTotalTax;
    private CurrencyType depositMerchantTotalTaxCurrencyType;
    //94 USD = (94-4 ExT)* 480 AMD = 43200 AMD wallet total including all tax
    private Double walletTotalPrice;
    private CurrencyType walletTotalPriceCurrencyType;
    //2 ReT + 3 WsDT = 5 USD = (5+4 ExT) = 9 USD setup total including tax
    private Double setupTotalAmount;
    private CurrencyType setupTotalAmountCurrencyType;

    //TransactionDepositProcess
    private TransactionDepositProcess processStart;
    private TransactionDepositProcess processEnd;
    //
    private TransactionDepositTax tax;
    //
    private Long merchantDepositId;
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

    public MerchantDeposit getMerchantDeposit() {
        return merchantDeposit;
    }

    public void setMerchantDeposit(MerchantDeposit merchantDeposit) {
        this.merchantDeposit = merchantDeposit;
    }

    public Double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(Double depositAmount) {
        this.depositAmount = depositAmount;
    }

    public CurrencyType getDepositAmountCurrencyType() {
        return depositAmountCurrencyType;
    }

    public void setDepositAmountCurrencyType(CurrencyType depositAmountCurrencyType) {
        this.depositAmountCurrencyType = depositAmountCurrencyType;
    }

    public Double getDepositMerchantTotalTax() {
        return depositMerchantTotalTax;
    }

    public void setDepositMerchantTotalTax(Double depositMerchantTotalTax) {
        this.depositMerchantTotalTax = depositMerchantTotalTax;
    }

    public CurrencyType getDepositMerchantTotalTaxCurrencyType() {
        return depositMerchantTotalTaxCurrencyType;
    }

    public void setDepositMerchantTotalTaxCurrencyType(CurrencyType depositMerchantTotalTaxCurrencyType) {
        this.depositMerchantTotalTaxCurrencyType = depositMerchantTotalTaxCurrencyType;
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

    public TransactionDepositProcess getProcessStart() {
        return processStart;
    }

    public void setProcessStart(TransactionDepositProcess processStart) {
        this.processStart = processStart;
    }

    public TransactionDepositProcess getProcessEnd() {
        return processEnd;
    }

    public void setProcessEnd(TransactionDepositProcess processEnd) {
        this.processEnd = processEnd;
    }

    public TransactionDepositTax getTax() {
        return tax;
    }

    public void setTax(TransactionDepositTax tax) {
        this.tax = tax;
    }

    public Long getMerchantDepositId() {
        return merchantDepositId;
    }

    public void setMerchantDepositId(Long merchantDepositId) {
        this.merchantDepositId = merchantDepositId;
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
