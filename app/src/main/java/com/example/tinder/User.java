package com.example.tinder;

public class User {
    private String userID,name,profileUrl;

    public User(String userID, String name, String profileUrl) {
        this.userID = userID;
        this.name = name;
        this.profileUrl = profileUrl;
    }

    public User(String userID, String name) {
        this.userID = userID;
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }
}
