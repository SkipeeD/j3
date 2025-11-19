package com.memoryjournal.app.data;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class Account {
    private String accountId;
    private String email;
    private String displayLabel;
    private String accessLevel;
    private String linkedCareProfile;
    private Timestamp createdAt;

    public Account() {
    }

    public Account(String accountId, String email, String displayLabel, String accessLevel,
                   String linkedCareProfile, Timestamp createdAt) {
        this.accountId = accountId;
        this.email = email;
        this.displayLabel = displayLabel;
        this.accessLevel = accessLevel;
        this.linkedCareProfile = linkedCareProfile;
        this.createdAt = createdAt;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayLabel() {
        return displayLabel;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public String getLinkedCareProfile() {
        return linkedCareProfile;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setLinkedCareProfile(String linkedCareProfile) {
        this.linkedCareProfile = linkedCareProfile;
    }

    @NonNull
    @Override
    public String toString() {
        return displayLabel + " (" + accessLevel + ")";
    }
}
