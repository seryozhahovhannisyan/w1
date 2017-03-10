package com.connectto.wallet.model.general;

import com.connectto.wallet.model.general.lcp.LogAction;
import com.connectto.wallet.model.general.lcp.LogLevel;

import java.util.Date;

/**
 * Created by htdev001 on 11/14/14.
 */
public class WalletLogger {

    private Long id;
    private Long userId;
    private Long walletId;

    private Integer partitionId;

    private LogLevel logLevel;
    private String className;
    private String methodName;
    private LogAction logAction;
    private String message;
    private Date date;

    public WalletLogger() {
    }

    public WalletLogger(Long userId, LogLevel logLevel, LogAction logAction, String message, Date date) {
        this.userId = userId;
        this.logLevel = logLevel;
        this.logAction = logAction;
        this.message = message;
        this.date = date;
    }

    public WalletLogger(Long userId, Long walletId, LogLevel logLevel, String className, LogAction logAction, String message, Date date) {
        this.userId = userId;
        this.walletId = walletId;
        this.logLevel = logLevel;
        this.className = className;
        this.logAction = logAction;
        this.message = message;
        this.date = date;
    }

    public WalletLogger(Long userId, Long walletId, LogLevel logLevel, LogAction logAction, String message, Date date) {
        this.userId = userId;
        this.walletId = walletId;
        this.logLevel = logLevel;
        this.logAction = logAction;
        this.message = message;
        this.date = date;
    }

    public WalletLogger(LogLevel logLevel, LogAction logAction, String message, Date date) {
        this.logLevel = logLevel;
        this.logAction = logAction;
        this.message = message;
        this.date = date;
    }

    public WalletLogger(String className, LogLevel logLevel, LogAction logAction, String message, Date date) {
        this.className = className;
        this.logLevel = logLevel;
        this.logAction = logAction;
        this.message = message;
        this.date = date;
    }

    public WalletLogger(Long walletId, String className, LogLevel logLevel, LogAction logAction, String message, Date date) {
        this.className = className;
        this.logLevel = logLevel;
        this.logAction = logAction;
        this.message = message;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Integer getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(Integer partitionId) {
        this.partitionId = partitionId;
    }

    public LogLevel getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public LogAction getLogAction() {
        return logAction;
    }

    public void setLogAction(LogAction logAction) {
        this.logAction = logAction;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
