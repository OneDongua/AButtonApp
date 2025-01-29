package com.onedongua.abutton.model;

import androidx.annotation.NonNull;

import java.util.List;

public class NotificationData {
    private int count;

    private List<NotificationItem> notifications;

    public NotificationData() {
    }

    public NotificationData(int count, List<NotificationItem> notifications) {
        this.count = count;
        this.notifications = notifications;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<NotificationItem> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationItem> notifications) {
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public String toString() {
        return "NotificationData{" +
                "count=" + count +
                ", notifications=" + notifications +
                '}';
    }
}


