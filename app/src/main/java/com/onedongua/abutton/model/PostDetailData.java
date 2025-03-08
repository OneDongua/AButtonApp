package com.onedongua.abutton.model;

import java.util.List;

public class PostDetailData {
    private int authorId;
    private String title;
    private String content;
    private List<String> disability;
    private List<Comment> comment;
    private int imageCount;
    private LocationData location;

    public PostDetailData() {
    }

    public PostDetailData(int authorId,
                          String title,
                          String content,
                          List<String> disability,
                          List<Comment> comment,
                          int imageCount,
                          LocationData location) {
        this.authorId = authorId;
        this.title = title;
        this.content = content;
        this.disability = disability;
        this.comment = comment;
        this.imageCount = imageCount;
        this.location = location;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getDisability() {
        return disability;
    }

    public void setDisability(List<String> disability) {
        this.disability = disability;
    }

    public List<Comment> getComment() {
        return comment;
    }

    public void setComment(List<Comment> comment) {
        this.comment = comment;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public LocationData getLocation() {
        return location;
    }

    public void setLocation(LocationData location) {
        this.location = location;
    }

    public class Comment {
        private int authorId;
        private String content;
        private long time;
        private int likes;
        private int dislikes;

        public Comment() {
        }

        public Comment(int authorId, String content, long time, int likes, int dislikes) {
            this.authorId = authorId;
            this.content = content;
            this.time = time;
            this.likes = likes;
            this.dislikes = dislikes;
        }

        public int getAuthorId() {
            return authorId;
        }

        public void setAuthorId(int authorId) {
            this.authorId = authorId;
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

        public int getLikes() {
            return likes;
        }

        public void setLikes(int likes) {
            this.likes = likes;
        }

        public int getDislikes() {
            return dislikes;
        }

        public void setDislikes(int dislikes) {
            this.dislikes = dislikes;
        }
    }
}
