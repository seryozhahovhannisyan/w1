package com.connectto.general.model;

import com.connectto.general.model.lcp.CoreRole;
import com.connectto.general.model.lcp.Language;
import com.connectto.general.model.lcp.Status;
import com.connectto.general.model.lcp.UserProfile;
import com.connectto.wallet.model.wallet.Wallet;

/**
 * Created by htdev001 on 2/12/14.
 */
public class CoreSystemAdmin {

    private Long id;
    private int partitionId;
    //Personal
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private CoreRole role;
    private String email;
    private String phone;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CoreRole getRole() {
        return role;
    }

    public void setRole(CoreRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}