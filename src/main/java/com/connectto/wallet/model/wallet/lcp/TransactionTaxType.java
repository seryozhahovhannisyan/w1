package com.connectto.wallet.model.wallet.lcp;

public enum TransactionTaxType {

    MIN         (1, "min"),
    MAX         (2, "max"),
    PERCENT     (3, "percent");

    TransactionTaxType(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public static synchronized TransactionTaxType valueOf(final int value) {
        for (TransactionTaxType e : TransactionTaxType.values()) {
            if (e.id == value) {
                return e;
            }
        }
        return null;
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
