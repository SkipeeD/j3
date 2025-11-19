package com.memoryjournal.app.data;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

import java.util.ArrayList;
import java.util.List;

public class JournalRecord {
    @DocumentId
    private String recordId;
    private String profileId;
    private String createdBy;
    private String heading;
    private String noteBody;
    private Timestamp eventDate;
    private Timestamp insertedAt;
    private List<String> categories = new ArrayList<>();
    private List<MediaItem> mediaList = new ArrayList<>();

    public JournalRecord() {
    }

    public JournalRecord(String recordId, String profileId, String createdBy, String heading,
                         String noteBody, Timestamp eventDate, Timestamp insertedAt,
                         List<String> categories, List<MediaItem> mediaList) {
        this.recordId = recordId;
        this.profileId = profileId;
        this.createdBy = createdBy;
        this.heading = heading;
        this.noteBody = noteBody;
        this.eventDate = eventDate;
        this.insertedAt = insertedAt;
        this.categories = categories;
        this.mediaList = mediaList;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getProfileId() {
        return profileId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getHeading() {
        return heading;
    }

    public String getNoteBody() {
        return noteBody;
    }

    public Timestamp getEventDate() {
        return eventDate;
    }

    public Timestamp getInsertedAt() {
        return insertedAt;
    }

    public List<String> getCategories() {
        return categories;
    }

    public List<MediaItem> getMediaList() {
        return mediaList;
    }
}
