package com.example.ecommerce;

public class User {
    public String userId;
    public String name;
    public String email;
    public String phone;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String userId, String name, String email, String phone) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}




