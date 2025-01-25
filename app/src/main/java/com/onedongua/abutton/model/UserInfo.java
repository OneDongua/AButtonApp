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

    // 无参构造函数
    public UserInfo() {
    }

    // 有参构造函数
    public UserInfo(String id, String type, String name, String email, ArrayList<String> disability) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.email = email;
        this.hardware = hardware;
        this.disability = disability;
    }

    // Getter 和 Setter 方法
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

    // toString 方法
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
                '}';
    }
}
