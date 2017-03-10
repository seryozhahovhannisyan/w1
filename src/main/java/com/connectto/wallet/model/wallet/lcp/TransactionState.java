package com.connectto.wallet.model.wallet.lcp;

public enum TransactionState {

    EXCHANGE_BALANCE(17, "approved", "when somebody starts transaction for pass to money"),
    PENDING(15, "pending transaction", "when somebody starts transaction for pass to money"),
    PASSED_TO_CHARGE(16, "passed to charge", "when somebody starts transaction for pass to money"),
    ////////////// NEW

    SEND_MONEY(1, "sendmoney money", "when somebody starts transaction for pass to money"),
    REQUEST_TRANSACTION(2, "request transaction", "when somebody starts transaction for ask to money"),

    APPROVED(3, "approved", "when transaction is successfully done and approved"),
    CANCEL(4, "cancel", "when transaction canceled by user"),
    REJECTED(5, "rejected transaction", "when transaction was not approved"),
    REJECTED_BY_ADMIN(6, "rejected by admin", "when a transaction rejected by admin "),

    PURCHASE_FREEZE(7, "Purchase freeze transaction", "Purchase transaction freeze"),
    PURCHASE_CANCEL(8, "Purchase cancel transaction", "Purchase transaction cancel"),
    PURCHASE_CHARGE(9, "Purchase charge transaction", "Purchase transaction charge"),

    MERCHANT_START(20, "Merchant start transaction", "Merchant start transaction"),
    MERCHANT_TIMEOUT(21, "Merchant timeout transaction", "Merchant timeout transaction"),
    MERCHANT_CANCEL(22, "Merchant cancel transaction", "Merchant cancel transaction"),
    MERCHANT_APPROVE(23, "Merchant approve transaction", "Merchant approve transaction"),

    CHARGE_AMOUNT(10, "charge_amount", "when transaction is successfully done and transferring was completed walletSetup receiving_amount will minused and balance plussed immediately"),
    FUTURE_PAYMENT(11, "future payment", "when driver get cash money we will keep future payment that he will pay thiers administrator");

    TransactionState(int id, String state, String description) {
        this.id = id;
        this.state = state;
        this.description = description;
    }

    public static TransactionState getDefault() {
        return null;
    }

    public static synchronized TransactionState valueOf(final int value) {
        for (TransactionState e : TransactionState.values()) {
            if (e.id == value) {
                return e;
            }
        }
        return getDefault();
    }

    private final int id;
    private final String state;
    private final String description;

    public int getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getDescription() {
        return description;
    }
}
