package com.memoryjournal.app.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthRepository {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    private final MutableLiveData<FirebaseUser> authState = new MutableLiveData<>();

    public AuthRepository() {
        auth.addAuthStateListener(firebaseAuth -> authState.postValue(firebaseAuth.getCurrentUser()));
    }

    public LiveData<FirebaseUser> getAuthState() {
        return authState;
    }

    public void login(String email, String password, RepositoryCallback<FirebaseUser> callback) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener(result -> callback.onSuccess(result.getUser()))
            .addOnFailureListener(callback::onError);
    }

    public void register(String email, String password, String displayName, String role,
                         RepositoryCallback<FirebaseUser> callback) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener(result -> saveAccountMetadata(result, displayName, role, callback))
            .addOnFailureListener(callback::onError);
    }

    private void saveAccountMetadata(@NonNull AuthResult result, String displayName, String role,
                                     RepositoryCallback<FirebaseUser> callback) {
        FirebaseUser user = result.getUser();
        if (user == null) {
            callback.onError(new IllegalStateException("User is null"));
            return;
        }
        Map<String, Object> payload = new HashMap<>();
        payload.put("email", user.getEmail());
        payload.put("displayLabel", displayName);
        payload.put("accessLevel", role);
        payload.put("linkedCareProfile", null);
        payload.put("createdAt", com.google.firebase.Timestamp.now());
        DocumentReference doc = firestore.collection("accounts").document(user.getUid());
        doc.set(payload)
            .addOnSuccessListener(unused -> callback.onSuccess(user))
            .addOnFailureListener(callback::onError);
    }

    public void logout() {
        auth.signOut();
    }

    public FirebaseUser currentUser() {
        return auth.getCurrentUser();
    }
}
