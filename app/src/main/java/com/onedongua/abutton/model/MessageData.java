package com.onedongua.abutton.model;

public class MessageData {
    private String id;
    private String name;
    private String message;
    private Long time;

    public MessageData() {
    }

    public MessageData(String id, String name, String message, Long time) {
        this.id = id;
        this.name = name;
        this.message = message;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public Long getTime() {
        return time;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
