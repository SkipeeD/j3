package com.memoryjournal.app.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.memoryjournal.app.data.JournalRecord;
import com.memoryjournal.app.data.MediaItem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JournalRepository {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final MutableLiveData<List<JournalRecord>> entriesLiveData = new MutableLiveData<>();

    public LiveData<List<JournalRecord>> getEntriesLiveData() {
        return entriesLiveData;
    }

    public void listenToEntries(String profileId) {
        CollectionReference collection = firestore.collection("careProfiles")
            .document(profileId)
            .collection("journalRecords");
        collection.orderBy("eventDate", Query.Direction.DESCENDING)
            .addSnapshotListener((snapshots, error) -> {
                if (error != null) return;
                if (snapshots != null) {
                    entriesLiveData.postValue(snapshots.toObjects(JournalRecord.class));
                }
            });
    }

    public void addEntry(String profileId, String title, String noteBody, Timestamp eventDate,
                         List<String> tags, List<MediaItem> mediaItems,
                         RepositoryCallback<Void> callback) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("heading", title);
        payload.put("noteBody", noteBody);
        payload.put("eventDate", eventDate);
        payload.put("insertedAt", Timestamp.now());
        payload.put("categories", tags);
        payload.put("mediaList", mediaItems);
        CollectionReference collection = firestore.collection("careProfiles")
            .document(profileId)
            .collection("journalRecords");
        collection.add(payload)
            .addOnSuccessListener(documentReference -> callback.onSuccess(null))
            .addOnFailureListener(callback::onError);
    }

    public Query searchEntries(String profileId, String keyword) {
        CollectionReference collection = firestore.collection("careProfiles")
            .document(profileId)
            .collection("journalRecords");
        return collection.orderBy("heading")
            .startAt(keyword)
            .endAt(keyword + '\uf8ff');
    }
}
