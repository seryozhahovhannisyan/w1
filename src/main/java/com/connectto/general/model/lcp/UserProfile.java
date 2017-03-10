package com.connectto.general.model.lcp;

/**
 * Created by htdev001 on 2/24/14.
 */
public enum UserProfile {

    NONE            (0, "none",""),
    ADMIN           (1, "admin",""),
    MODERATOR       (2, "moderator",""),
    USER            (3, "user",""),
    DISTRIBUTOR     (4, "distributor",""),
    CUSTOMER        (5, "customer",""),
    MONITOR         (7, "monitor",""),
    UNKNOWN         (10, "unknown",""),
    //
    DRIVER          (6, "driver","for vshoo.com became a driver case"),
    LOCATION        (9, "location","for vshoo.com became a location case"),
    COMPANY         (8, "company","for vshoo.com became a company case"),
    PARTITION      (11, "partition","for CoreSystemAdmin login/entry case"),
    VSHOO_CUSTOM   (12, "custom","for vshoo.com registration case"),
    INVALID_TRANSPORTATION_STATUS (13, "invalid_transportation_status","PLease, Connect to admins for update your status");

    UserProfile(int key, String profile, String description) {
        this.key = key;
        this.profile = profile;
        this.description = description;
    }

    public static UserProfile getDefaultProfile() {
        return UserProfile.NONE;
    }

    public static UserProfile valueOf(int key) {

        for (UserProfile userProfile : UserProfile.values()) {
            if (userProfile.getKey() == key) {
                return userProfile;
            }
        }

        return null;
    }

    public int getKey() {
        return key;
    }

    public String getProfile() {
        return profile;
    }

    public String getDescription() {
        return description;
    }

    private int key;
    private String profile;
    private String description;

}
