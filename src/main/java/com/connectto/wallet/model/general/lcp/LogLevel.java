package com.connectto.wallet.model.general.lcp;

/**
 * Created by htdev001 on 3/12/14.
 */
public enum LogLevel {

    OFF(1, "off"),
    FATAL(2, "fatal"),
    ERROR(3, "error"),
    WARN(4, "warn"),
    INFO(5, "info"),
    DEBUG(6, "debug"),
    TRACE(7, "trace"),
    ALL(8, "all");

    LogLevel(final int key, final String value) {
        this.value = value;
        this.key = key;
    }

    public static LogLevel getDefault() {
        return LogLevel.ALL;
    }

    public static synchronized LogLevel valueOfKey(final int key) {
        for (LogLevel e : LogLevel.values()) {
            if (e.key == key) {
                return e;
            }
        }
        return getDefault();
    }

    public static synchronized LogLevel valueOF(final String val) {
        for (LogLevel e : LogLevel.values()) {
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
