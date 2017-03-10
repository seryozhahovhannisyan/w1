package com.connectto.wallet.model.wallet;

import java.util.Date;

/**
 * Created by Serozh on 4/13/16.
 */
public class BlockedUser {

    private Long id;
    private Long ownerId;
    private Long blockedId;

    private String title;
    private String content;

    private Date blockedAt;
    private Date unblockedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Long getBlockedId() {
        return blockedId;
    }

    public void setBlockedId(Long blockedId) {
        this.blockedId = blockedId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getBlockedAt() {
        return blockedAt;
    }

    public void setBlockedAt(Date blockedAt) {
        this.blockedAt = blockedAt;
    }

    public Date getUnblockedAt() {
        return unblockedAt;
    }

    public void setUnblockedAt(Date unblockedAt) {
        this.unblockedAt = unblockedAt;
    }
}
