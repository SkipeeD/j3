package com.memoryjournal.app.util;

import android.Manifest;
import android.content.Context;
import android.media.MediaRecorder;
import android.net.Uri;

import androidx.annotation.RequiresPermission;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.memoryjournal.app.repository.RepositoryCallback;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AudioRecorder {
    private final Context context;
    private MediaRecorder recorder;
    private File outputFile;

    public AudioRecorder(Context context) {
        this.context = context.getApplicationContext();
    }

    @RequiresPermission(Manifest.permission.RECORD_AUDIO)
    public void startRecording() throws IOException {
        outputFile = File.createTempFile("memory-audio", ".m4a", context.getCacheDir());
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(outputFile.getAbsolutePath());
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        recorder.prepare();
        recorder.start();
    }

    public void stopAndUpload(RepositoryCallback<String> callback) {
        if (recorder == null) {
            callback.onError(new IllegalStateException("Recorder not started"));
            return;
        }
        recorder.stop();
        recorder.release();
        recorder = null;
        StorageReference reference = FirebaseStorage.getInstance()
            .getReference("audio/" + UUID.randomUUID());
        reference.putFile(Uri.fromFile(outputFile))
            .continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return reference.getDownloadUrl();
            })
            .addOnSuccessListener(uri -> callback.onSuccess(uri.toString()))
            .addOnFailureListener(callback::onError);
    }
}
