package com.memoryjournal.app.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.memoryjournal.app.R;
import com.memoryjournal.app.data.JournalRecord;
import com.memoryjournal.app.util.DateFormatter;

import java.util.ArrayList;
import java.util.List;

public class JournalEntryAdapter extends RecyclerView.Adapter<JournalEntryAdapter.EntryViewHolder> {
    public interface OnEntryClickListener {
        void onEntrySelected(JournalRecord record);
    }

    private final OnEntryClickListener listener;
    private final List<JournalRecord> data = new ArrayList<>();

    public JournalEntryAdapter(OnEntryClickListener listener) {
        this.listener = listener;
    }

    public void submitList(List<JournalRecord> newData) {
        data.clear();
        if (newData != null) {
            data.addAll(newData);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_journal_entry, parent, false);
        return new EntryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        JournalRecord record = data.get(position);
        holder.bind(record);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class EntryViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView date;
        private final TextView tags;

        EntryViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.entryTitle);
            date = itemView.findViewById(R.id.entryDate);
            tags = itemView.findViewById(R.id.entryTags);
        }

        void bind(JournalRecord record) {
            title.setText(record.getHeading());
            date.setText(DateFormatter.format(itemView.getContext(), record.getEventDate()));
            tags.setText(android.text.TextUtils.join(", ", record.getCategories()));
            itemView.setOnClickListener(v -> listener.onEntrySelected(record));
        }
    }
}
