package com.school.haitamelattar.freeob.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by haitamelattar on 07-10-17.
 */

public class User {
    @SerializedName("User_id")
    @Expose
    private Long id = null;

    @SerializedName("Email")
    @Expose
    private String email;

    @SerializedName("AuthToken")
    @Expose
    private String authToken;

    @SerializedName("Name")
    @Expose
    private String name;

    @SerializedName("Bio")
    @Expose
    private String bio;

    public User(Long id, String email, String authToken) {
        this.id = id;
        this.email = email;
        this.authToken = authToken;
    }

    public User(String name, String bio) {
        this.name = name;
        this.bio = bio;
    }

    public User(Long id, String email) {
        this.id = id;
        this.email = email;
        this.authToken = authToken;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
