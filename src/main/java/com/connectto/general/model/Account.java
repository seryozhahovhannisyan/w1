package com.connectto.general.model;

import java.util.Date;

/**
 * Created by Serozh on 3/14/16.
 */
public class Account {

    private Long id;
    private Long userId;

    private String lastUrl;
    private boolean savePassword;
    // the special name of account for example home, workplace, phone, tv and etc
    private String name;

    private String imei;
    private String imsi;
    //device
    //browser & OS
    private String userAgent;
    private String requestAccept;

    private Date loginDate;
    private Date logoutDate;
    //Login
    private String loginKey;
    private String oldLoginKey;

    private String sessionId;
    private String oldSessionId;

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

    public String getLastUrl() {
        return lastUrl;
    }

    public void setLastUrl(String lastUrl) {
        this.lastUrl = lastUrl;
    }

    public boolean getSavePassword() {
        return savePassword;
    }

    public void setSavePassword(boolean savePassword) {
        this.savePassword = savePassword;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getRequestAccept() {
        return requestAccept;
    }

    public void setRequestAccept(String requestAccept) {
        this.requestAccept = requestAccept;
    }

    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    public Date getLogoutDate() {
        return logoutDate;
    }

    public void setLogoutDate(Date logoutDate) {
        this.logoutDate = logoutDate;
    }

    public String getLoginKey() {
        return loginKey;
    }

    public void setLoginKey(String loginKey) {
        this.loginKey = loginKey;
    }

    public String getOldLoginKey() {
        return oldLoginKey;
    }

    public void setOldLoginKey(String oldLoginKey) {
        this.oldLoginKey = oldLoginKey;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getOldSessionId() {
        return oldSessionId;
    }

    public void setOldSessionId(String oldSessionId) {
        this.oldSessionId = oldSessionId;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", lastUrl='" + lastUrl + '\'' +
                ", savePassword=" + savePassword +
                ", name='" + name + '\'' +
                ", imei='" + imei + '\'' +
                ", imsi='" + imsi + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", requestAccept='" + requestAccept + '\'' +
                ", loginDate=" + loginDate +
                ", logoutDate=" + logoutDate +
                ", loginKey='" + loginKey + '\'' +
                ", oldLoginKey='" + oldLoginKey + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", oldSessionId='" + oldSessionId + '\'' +
                '}';
    }
}
