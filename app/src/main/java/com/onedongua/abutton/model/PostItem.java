package com.onedongua.abutton.model;

public class PostItem {
    private String content;
    private long time;
    private String from;

    public PostItem() {
    }

    public PostItem(String content, long time, String from) {
        this.content = content;
        this.time = time;
        this.from = from;
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

    @Override
    public String toString() {
        return "NotificationItem{" +
                "content='" + content + '\'' +
                ", time=" + time +
                ", from='" + from + '\'' +
                '}';
    }
}
