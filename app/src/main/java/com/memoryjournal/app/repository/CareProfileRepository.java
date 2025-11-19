package com.memoryjournal.app.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.memoryjournal.app.data.CareProfile;

import java.util.HashMap;
import java.util.Map;

public class CareProfileRepository {
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private final MutableLiveData<CareProfile> activeProfile = new MutableLiveData<>();

    public LiveData<CareProfile> getActiveProfile() {
        return activeProfile;
    }

    public void createProfile(String name, String notes, RepositoryCallback<CareProfile> callback) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("fullName", name);
        payload.put("notes", notes);
        payload.put("profilePhotoUrl", null);
        payload.put("memberAccountIds", java.util.Collections.emptyList());
        payload.put("createdAt", com.google.firebase.Timestamp.now());
        firestore.collection("careProfiles")
            .add(payload)
            .addOnSuccessListener(documentReference -> documentReference.get()
                .addOnSuccessListener(snapshot -> {
                    CareProfile profile = snapshot.toObject(CareProfile.class);
                    activeProfile.postValue(profile);
                    callback.onSuccess(profile);
                }))
            .addOnFailureListener(callback::onError);
    }

    public void listenToProfile(String profileId) {
        DocumentReference doc = firestore.collection("careProfiles").document(profileId);
        doc.addSnapshotListener((value, error) -> {
            if (error != null) return;
            if (value != null && value.exists()) {
                activeProfile.postValue(value.toObject(CareProfile.class));
            }
        });
    }

    public Query queryProfilesForUser(String userId) {
        return firestore.collection("careProfiles").whereArrayContains("memberAccountIds", userId);
    }
}
