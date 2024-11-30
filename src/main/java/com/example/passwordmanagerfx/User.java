package com.example.passwordmanagerfx;

public class User {
    private int id;
    private String name;
    private String password;

    public User() {
    }

    public User(String password, String name, int id) {
        this.password = password;
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
