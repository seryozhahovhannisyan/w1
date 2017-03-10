package com.connectto.general.model;

import com.connectto.general.model.lcp.Language;
import com.connectto.general.model.lcp.Status;
import com.connectto.general.model.lcp.UserProfile;
import com.connectto.wallet.model.wallet.Wallet;

/**
 * Created by htdev001 on 2/12/14.
 */
public class User {

    private Long id;
    private int partitionId;
    private Partition partition;
    //Personal
    private String name;
    private String surname;
    //
    private String email;
    private String phoneCode;
    private String phone;
    private String password;
    //
    private Long walletId;
    private Wallet wallet;
    //
    private String photo;
    private Account currentAccount;

    private UserProfile tsmProfile;

    private Language language;

    private Status status;

    //##################################################################################################################
    // GETTERS & SETTERS
    //##################################################################################################################


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(int partitionId) {
        this.partitionId = partitionId;
    }

    public Partition getPartition() {
        return partition;
    }

    public void setPartition(Partition partition) {
        this.partition = partition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public void setCurrentAccount(Account currentAccount) {
        this.currentAccount = currentAccount;
    }

    public UserProfile getTsmProfile() {
        return tsmProfile;
    }

    public void setTsmProfile(UserProfile tsmProfile) {
        this.tsmProfile = tsmProfile;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", partitionId=" + partitionId +
                ", partition=" + partition +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", phoneCode='" + phoneCode + '\'' +
                ", phone='" + phone + '\'' +
                ", password='" + password + '\'' +
                ", walletId=" + walletId +
                ", wallet=" + wallet +
                ", photo='" + photo + '\'' +
                ", currentAccount=" + currentAccount +
                ", tsmProfile=" + tsmProfile +
                ", language=" + language +
                ", status=" + status +
                '}';
    }
}