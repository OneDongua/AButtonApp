package com.onedongua.abutton.model;

import androidx.annotation.NonNull;

import java.util.List;

public class PostData {
    private LocationData location;
    private String title;
    private List<String> disability;
    private int count;
    private long modTime;

    public PostData() {
    }

    public PostData(LocationData location,
                    String title,
                    List<String> disability,
                    int count,
                    long modTime) {
        this.location = location;
        this.title = title;
        this.disability = disability;
        this.count = count;
        this.modTime = modTime;
    }

    public LocationData getLocation() {
        return location;
    }

    public void setLocation(LocationData location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getDisability() {
        return disability;
    }

    public void setDisability(List<String> disability) {
        this.disability = disability;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getModTime() {
        return modTime;
    }

    public void setModTime(long mod_time) {
        this.modTime = mod_time;
    }

    @NonNull
    @Override
    public String toString() {
        return "PostData{" +
                "location='" + location + '\'' +
                ", title='" + title + '\'' +
                ", disability=" + disability +
                ", count=" + count +
                ", mod_time=" + modTime +
                '}';
    }
}

