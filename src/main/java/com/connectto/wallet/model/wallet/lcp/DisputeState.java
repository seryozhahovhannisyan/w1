package com.connectto.wallet.model.wallet.lcp;

public enum DisputeState {

    PENDING(1, "pending"),
    APPROVE(2, "approve"),
    REJECT(3, "reject"),
    ClOSE(4, "close");


    DisputeState(int id, String state) {
        this.id = id;
        this.state = state;
    }

    public static DisputeState getDefault() {
        return null;
    }

    public static synchronized DisputeState valueOf(final int value) {
        for (DisputeState e : DisputeState.values()) {
            if (e.id == value) {
                return e;
            }
        }
        return getDefault();
    }

    private final int id;
    private final String state;

    public int getId() {
        return id;
    }

    public String getState() {
        return state;
    }
}
