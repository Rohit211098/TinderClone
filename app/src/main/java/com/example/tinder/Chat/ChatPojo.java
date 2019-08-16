package com.example.tinder.Chat;

public class ChatPojo {
    private String message;


   private Boolean currentUser;

    public ChatPojo(String message, Boolean currentUser) {
        this.message = message;
        this.currentUser = currentUser;
    }

    public String getMessage() {
        return message;
    }

    public Boolean getCurrentUser() {
        return currentUser;
    }
}
