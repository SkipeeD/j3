package com.memoryjournal.app.data;

public class MediaItem {
    private String type;
    private String url;

    public MediaItem() {
    }

    public MediaItem(String type, String url) {
        this.type = type;
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }
}
