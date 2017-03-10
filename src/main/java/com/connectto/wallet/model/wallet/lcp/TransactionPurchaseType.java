package com.connectto.wallet.model.wallet.lcp;

public enum TransactionPurchaseType {

    PURCHASE(1, "purchase"),
    SERVICE(2, "service"),
    OTHER(3, "other");

    TransactionPurchaseType(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public static TransactionPurchaseType getDefault() {
        return OTHER;
    }

    public static synchronized TransactionPurchaseType valueOf(final int value) {
        for (TransactionPurchaseType e : TransactionPurchaseType.values()) {
            if (e.id == value) {
                return e;
            }
        }
        return getDefault();
    }

    public static synchronized TransactionPurchaseType typeOf(final String type) {
        for (TransactionPurchaseType e : TransactionPurchaseType.values()) {
            if (e.type.equalsIgnoreCase(type)) {
                return e;
            }
        }
        return getDefault();
    }

    private final int id;
    private final String type;

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
