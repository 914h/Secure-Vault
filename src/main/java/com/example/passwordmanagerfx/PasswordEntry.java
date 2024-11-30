package com.example.passwordmanagerfx;

import java.util.Date;

public class PasswordEntry {
    private int id;
    private int UserID;
    private String title;
    private String username;
    private String email;
    private String password;
    private Date CreatedAt;
    private Date UpdatedAt;



    public PasswordEntry(int id, int userID, String title, String username, String email, String password, Date createdAt, Date updatedAt) {
        this.id = id;
        UserID = userID;
        this.title = title;
        this.username = username;
        this.email = email;
        this.password = password;
        CreatedAt = createdAt;
        UpdatedAt = updatedAt;
    }

    public PasswordEntry() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        CreatedAt = createdAt;
    }

    public Date getUpdatedAt() {
        return UpdatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        UpdatedAt = updatedAt;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }
}
