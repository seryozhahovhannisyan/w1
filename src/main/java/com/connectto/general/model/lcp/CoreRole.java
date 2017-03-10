package com.connectto.general.model.lcp;

/**
 * Created by htdev001 on 2/24/14.
 */
public enum CoreRole {

    NONE                (0, "none"),
    root                (1, "root"),
    System_Admin        (2, "System Admin"),
    Manager             (3, "Manager"),
    User                (4, "User"),
    CustomerService     (5, "CustomerService"),
    Tech_Support        (6, "driver"),
    MONITOR             (7, "Monitor"),
    StudioManager       (8, "StudioManager"),
    AssetLibraryManager (9, "AssetLibraryManager"),
    AssetLibraryUser    (10, "AssetLibraryUser");

    CoreRole(int roleId, String role) {
        this.roleId = roleId;
        this.role = role;
    }

    public CoreRole getDefaultCoreRole() {
        return CoreRole.NONE;
    }

    public static CoreRole valueOf(final int roleId) {

        for (CoreRole userProfile : CoreRole.values()) {
            if (userProfile.getRoleId() == roleId) {
                return userProfile;
            }
        }

        return null;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getRole() {
        return role;
    }

    private int roleId;
    private String role;

}
