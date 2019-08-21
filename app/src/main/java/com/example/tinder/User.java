package com.example.tinder;

public class User {
    private String userID,name,age,profileUrl;

    public User(String userID, String name, String age, String profileUrl) {
        this.userID = userID;
        this.name = name;
        this.age = age;
        this.profileUrl = profileUrl;
    }

    public User(String userID, String name, String age) {
        this.userID = userID;
        this.name = name;
        this.age = age;
    }

    public User(String userID, String name) {
        this.userID = userID;
        this.name = name;
    }

    public String getAge() {
        return age;
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
