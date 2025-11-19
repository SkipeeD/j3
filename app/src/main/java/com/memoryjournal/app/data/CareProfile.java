package com.memoryjournal.app.data;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.List;

public class CareProfile {
    @DocumentId
    private String profileId;
    private String fullName;
    private String profilePhotoUrl;
    private List<String> memberAccountIds = new ArrayList<>();
    private String notes;
    private Timestamp createdAt;

    public CareProfile() {
    }

    public CareProfile(String profileId, String fullName, String profilePhotoUrl,
                       List<String> memberAccountIds, String notes, Timestamp createdAt) {
        this.profileId = profileId;
        this.fullName = fullName;
        this.profilePhotoUrl = profilePhotoUrl;
        this.memberAccountIds = memberAccountIds;
        this.notes = notes;
        this.createdAt = createdAt;
    }

    public String getProfileId() {
        return profileId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public List<String> getMemberAccountIds() {
        return memberAccountIds;
    }

    public String getNotes() {
        return notes;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
