package com.memoryjournal.app.ui;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.memoryjournal.app.R;
import com.memoryjournal.app.data.MediaItem;
import com.memoryjournal.app.repository.JournalRepository;
import com.memoryjournal.app.repository.RepositoryCallback;
import com.memoryjournal.app.util.AudioRecorder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class AddEntryActivity extends AppCompatActivity {
    private final JournalRepository journalRepository = new JournalRepository();
    private final List<MediaItem> mediaItems = new ArrayList<>();
    private Timestamp eventDate = Timestamp.now();
    private String profileId;
    private Button selectDateButton;
    private Button recordAudioButton;
    private AudioRecorder audioRecorder;
    private boolean isRecording = false;
    private ActivityResultLauncher<String> imagePicker;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        profileId = getIntent().getStringExtra("profileId");
        if (profileId == null) {
            finish();
            return;
        }

        audioRecorder = new AudioRecorder(this);
        selectDateButton = findViewById(R.id.selectDateButton);
        recordAudioButton = findViewById(R.id.recordAudioButton);
        Button addMediaButton = findViewById(R.id.addMediaButton);
        Button saveButton = findViewById(R.id.saveEntryButton);
        EditText titleInput = findViewById(R.id.titleInput);
        EditText noteInput = findViewById(R.id.noteInput);
        EditText tagInput = findViewById(R.id.tagInput);

        updateDateButtonLabel();

        selectDateButton.setOnClickListener(v -> openDatePicker());
        addMediaButton.setOnClickListener(v -> launchImagePicker());
        recordAudioButton.setOnClickListener(v -> toggleAudioRecording());

        saveButton.setOnClickListener(v -> {
            String title = titleInput.getText().toString();
            String note = noteInput.getText().toString();
            List<String> tags = parseTags(tagInput.getText().toString());
            journalRepository.addEntry(profileId, title, note, eventDate, tags, mediaItems,
                new RepositoryCallback<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        Toast.makeText(AddEntryActivity.this, R.string.add_entry, Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(Exception exception) {
                        Toast.makeText(AddEntryActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        });

        imagePicker = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                uploadImage(uri);
            }
        });
    }

    private void openDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(year, month, dayOfMonth);
            eventDate = new Timestamp(calendar.getTime());
            updateDateButtonLabel();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    private void updateDateButtonLabel() {
        java.text.DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(this);
        selectDateButton.setText(getString(R.string.select_event_date) + ": " + dateFormat.format(eventDate.toDate()));
    }

    private void launchImagePicker() {
        imagePicker.launch("image/*");
    }

    private void uploadImage(Uri uri) {
        StorageReference reference = FirebaseStorage.getInstance().getReference("images/" + UUID.randomUUID());
        reference.putFile(uri)
            .continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return reference.getDownloadUrl();
            })
            .addOnSuccessListener(downloadUri -> {
                mediaItems.add(new MediaItem("image", downloadUri.toString()));
                Toast.makeText(this, R.string.add_entry, Toast.LENGTH_SHORT).show();
            })
            .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show());
    }

    private void toggleAudioRecording() {
        if (!isRecording) {
            try {
                audioRecorder.startRecording();
                isRecording = true;
                recordAudioButton.setText(R.string.stop_recording);
            } catch (IOException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            audioRecorder.stopAndUpload(new RepositoryCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    mediaItems.add(new MediaItem("audio", result));
                    Toast.makeText(AddEntryActivity.this, R.string.record_audio, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onError(Exception exception) {
                    Toast.makeText(AddEntryActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            isRecording = false;
            recordAudioButton.setText(R.string.record_audio);
        }
    }

    private List<String> parseTags(String csv) {
        List<String> tags = new ArrayList<>();
        for (String tag : csv.split(",")) {
            String trimmed = tag.trim();
            if (!trimmed.isEmpty()) {
                tags.add(trimmed);
            }
        }
        return tags;
    }
}
