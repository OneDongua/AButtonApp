package com.onedongua.abutton.model;

import androidx.annotation.NonNull;

public class NotificationItem {
    private String content;
    private long time;
    private String from;
    private LocationData location;

    public NotificationItem() {
    }

    public NotificationItem(String content,
                            long time,
                            String from,
                            LocationData location) {
        this.content = content;
        this.time = time;
        this.from = from;
        this.location = location;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public LocationData getLocation() {
        return location;
    }

    public void setLocation(LocationData location) {
        this.location = location;
    }

    @NonNull
    @Override
    public String toString() {
        return "NotificationItem{" +
                "content='" + content + '\'' +
                ", time=" + time +
                ", from='" + from + '\'' +
                ", location=" + location +
                '}';
    }
}
