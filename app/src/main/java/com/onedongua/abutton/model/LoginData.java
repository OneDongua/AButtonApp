package com.onedongua.abutton.model;

import androidx.annotation.NonNull;

public class LoginData {
    private String email;
    private String password;

    // 无参构造函数
    public LoginData() {
    }

    // 有参构造函数
    public LoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter 和 Setter 方法
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // toString 方法
    @NonNull
    @Override
    public String toString() {
        return "LoginData{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
