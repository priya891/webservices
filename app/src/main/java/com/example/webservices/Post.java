package com.example.webservices;

import com.google.gson.annotations.SerializedName;

public class Post {
    private String userId;
    private String id;
    private String title;
    private String name;

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public String getProfileImage() {
        return profileImage;
    }

    private String message;
    private String profileImage;

    public String getUserId() {
        return userId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    @SerializedName("body")
    private String text;
}