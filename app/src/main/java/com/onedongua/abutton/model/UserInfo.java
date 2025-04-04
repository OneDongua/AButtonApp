package com.onedongua.abutton.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class UserInfo {
    private String id;
    private String type;
    private String name;
    private String email;
    private String hardware;
    private ArrayList<String> disability;
    private ArrayList<String> tag;

    public UserInfo() {
    }

    public UserInfo(String id,
                    String type,
                    String name,
                    String email,
                    String hardware,
                    ArrayList<String> disability,
                    ArrayList<String> tag) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.email = email;
        this.hardware = hardware;
        this.disability = disability;
        this.tag = tag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHardware() {
        return hardware;
    }

    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    public ArrayList<String> getDisability() {
        return disability;
    }

    public void setDisability(ArrayList<String> disability) {
        this.disability = disability;
    }

    public ArrayList<String> getTag() {
        return tag;
    }

    public void setTag(ArrayList<String> tag) {
        this.tag = tag;
    }

    @NonNull
    @Override
    public String toString() {
        return "UserInfo{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", hardware='" + hardware + '\'' +
                ", disability='" + disability + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
