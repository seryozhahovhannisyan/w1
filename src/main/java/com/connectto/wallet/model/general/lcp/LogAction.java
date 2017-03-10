package com.connectto.wallet.model.general.lcp;

/**
 * Created by htdev001 on 3/12/14.
 */
public enum LogAction {

    CREATE(1, "create"),
    READ(2, "read"),
    UPDATE(3, "update"),
    DELETE(4, "delete"),
    INSERT(5, "insert"),
    UTIL(6, "util"),
    ATTACH(7, "attach");

    LogAction(final int key, final String value) {
        this.value = value;
        this.key = key;
    }

    public static LogAction getDefault() {
        return null;
    }

    public static synchronized LogAction valueOfKey(final int key) {
        for (LogAction e : LogAction.values()) {
            if (e.key == key) {
                return e;
            }
        }
        return getDefault();
    }

    public static synchronized LogAction valueOF(final String val) {
        for (LogAction e : LogAction.values()) {
            if (e.getValue().equals(val)) {
                return e;
            }
        }
        return getDefault();
    }

    private final String value;
    private final int key;

    public String getValue() {
        return value;
    }

    public int getKey() {
        return key;
    }

}
