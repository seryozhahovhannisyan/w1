package com.connectto.wallet.model.wallet.lcp;

public enum WalletProfile {
    //https://www.google.com/finance/converter
    ADMIN       (1, "admin"),
    MODERATOR   (2, "moderator"),
    USER        (3, "user"),
    DRIVER      (4, "driver");

    WalletProfile(int id, String profile) {
        this.id = id;
        this.profile = profile;
    }

    public static WalletProfile getDefault() {
        return USER;
    }

    public static synchronized WalletProfile valueOf(final int value) {
        for (WalletProfile e : WalletProfile.values()) {
            if (e.id == value) {
                return e;
            }
        }
        return getDefault();
    }

    private final int id;
    private final String profile;

    public int getId() {
        return id;
    }

    public String getProfile() {
        return profile;
    }
}
