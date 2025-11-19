package com.memoryjournal.app.util;

import android.content.Context;

import com.google.firebase.Timestamp;

import java.text.DateFormat;

public final class DateFormatter {
    private DateFormatter() {
    }

    public static String format(Context context, Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        DateFormat dateFormat = android.text.format.DateFormat.getMediumDateFormat(context);
        return dateFormat.format(timestamp.toDate());
    }
}
