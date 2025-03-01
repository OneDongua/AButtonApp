package com.onedongua.abutton.model;

public class PostItem {
    private String id;
    private String title;
    private long time;
    private LocationData location;
    private int commentCount;

    public PostItem() {
    }

    public PostItem(String id, String title, long time, LocationData location, int commentCount) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.location = location;
        this.commentCount = commentCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public LocationData getLocation() {
        return location;
    }

    public void setLocation(LocationData location) {
        this.location = location;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    @Override
    public String toString() {
        return "PostItem{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", time=" + time +
                ", location='" + location + '\'' +
                ", commentCount=" + commentCount +
                '}';
    }
}
