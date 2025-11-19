package com.memoryjournal.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.memoryjournal.app.R;
import com.memoryjournal.app.data.CareProfile;
import com.memoryjournal.app.data.JournalRecord;
import com.memoryjournal.app.repository.CareProfileRepository;
import com.memoryjournal.app.repository.JournalRepository;
import com.memoryjournal.app.repository.RepositoryCallback;

import java.util.List;

public class CareProfileActivity extends AppCompatActivity implements JournalEntryAdapter.OnEntryClickListener {
    private final CareProfileRepository careProfileRepository = new CareProfileRepository();
    private final JournalRepository journalRepository = new JournalRepository();
    private JournalEntryAdapter adapter;
    private TextView statsView;
    private String profileId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_profile);

        TextView header = findViewById(R.id.profileHeader);
        TextView notes = findViewById(R.id.profileNotes);
        statsView = findViewById(R.id.profileStats);
        Button addEntryButton = findViewById(R.id.addEntryButton);
        RecyclerView recyclerView = findViewById(R.id.entryRecyclerView);

        adapter = new JournalEntryAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        profileId = getIntent().getStringExtra("profileId");
        if (profileId == null) {
            showProfileCreationDialog();
        } else {
            subscribeToProfile(profileId);
        }

        journalRepository.getEntriesLiveData().observe(this, this::renderEntries);
        addEntryButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddEntryActivity.class);
            intent.putExtra("profileId", profileId);
            startActivity(intent);
        });

        careProfileRepository.getActiveProfile().observe(this, profile -> {
            if (profile == null) {
                return;
            }
            profileId = profile.getProfileId();
            header.setText(profile.getFullName());
            notes.setText(profile.getNotes());
            journalRepository.listenToEntries(profileId);
        });
    }

    private void renderEntries(List<JournalRecord> journalRecords) {
        adapter.submitList(journalRecords);
        if (statsView != null) {
            statsView.setText(getString(R.string.profile_stats, journalRecords == null ? 0 : journalRecords.size()));
        }
    }

    private void subscribeToProfile(String profileId) {
        careProfileRepository.listenToProfile(profileId);
        journalRepository.listenToEntries(profileId);
    }

    private void showProfileCreationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.create_profile);
        builder.setMessage(R.string.create_profile_message);
        builder.setPositiveButton(android.R.string.ok, (dialog, which) ->
            careProfileRepository.createProfile("New Care Profile", "Tap notes to edit", new RepositoryCallback<CareProfile>() {
                @Override
                public void onSuccess(CareProfile result) {
                    profileId = result.getProfileId();
                    journalRepository.listenToEntries(profileId);
                }

                @Override
                public void onError(Exception exception) {
                    Toast.makeText(CareProfileActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                }
            })
        );
        builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    @Override
    public void onEntrySelected(JournalRecord record) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(record.getHeading());
        builder.setMessage(record.getNoteBody());
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
    }
}
