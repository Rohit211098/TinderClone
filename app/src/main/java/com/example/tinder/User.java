package com.example.tinder;

public class User {
    private String userID,name;

    public User(String userID, String name) {
        this.userID = userID;
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }
}
