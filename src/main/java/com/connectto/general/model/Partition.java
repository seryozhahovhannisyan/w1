package com.connectto.general.model;

import com.connectto.general.model.lcp.Language;

/**
 * Created by htdev001 on 6/17/14.
 */
public class Partition {

    private int id;

    private String name;
    private String domainName;
    private String host;
    private String port;
    private String apiKey;

    private String urlFb;
    private String urlGmail;
    private String urlTwitter;

    private String serverName;
    private String partitionServerUrl;
    private String partitionUrl;
    private String partitionLogoDirectory;

    private String partitionEmail;
    private String partitionEmailPassword;

    private String logoPath;

    private String recaptchaSecretKey;
    private String recaptchaClientKey;

    private WalletSetup walletSetup;

    private Language language;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getPartitionServerUrl() {
        return partitionServerUrl;
    }

    public void setPartitionServerUrl(String partitionServerUrl) {
        this.partitionServerUrl = partitionServerUrl;
    }

    public String getPartitionUrl() {
        return partitionUrl;
    }

    public void setPartitionUrl(String partitionUrl) {
        this.partitionUrl = partitionUrl;
    }

    public String getPartitionLogoDirectory() {
        return partitionLogoDirectory;
    }

    public void setPartitionLogoDirectory(String partitionLogoDirectory) {
        this.partitionLogoDirectory = partitionLogoDirectory;
    }

    public String getPartitionEmail() {
        return partitionEmail;
    }

    public void setPartitionEmail(String partitionEmail) {
        this.partitionEmail = partitionEmail;
    }

    public String getPartitionEmailPassword() {
        return partitionEmailPassword;
    }

    public void setPartitionEmailPassword(String partitionEmailPassword) {
        this.partitionEmailPassword = partitionEmailPassword;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getRecaptchaSecretKey() {
        return recaptchaSecretKey;
    }

    public void setRecaptchaSecretKey(String recaptchaSecretKey) {
        this.recaptchaSecretKey = recaptchaSecretKey;
    }

    public WalletSetup getWalletSetup() {
        return walletSetup;
    }

    public void setWalletSetup(WalletSetup walletSetup) {
        this.walletSetup = walletSetup;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public String getRecaptchaClientKey() {
        return recaptchaClientKey;
    }

    public void setRecaptchaClientKey(String recaptchaClientKey) {
        this.recaptchaClientKey = recaptchaClientKey;
    }

    public String getUrlFb() {
        return urlFb;
    }

    public void setUrlFb(String urlFb) {
        this.urlFb = urlFb;
    }

    public String getUrlGmail() {
        return urlGmail;
    }

    public void setUrlGmail(String urlGmail) {
        this.urlGmail = urlGmail;
    }

    public String getUrlTwitter() {
        return urlTwitter;
    }

    public void setUrlTwitter(String urlTwitter) {
        this.urlTwitter = urlTwitter;
    }
}
