package com.school.haitamelattar.freeob.model;

import static android.R.attr.id;

/**
 * Created by haitamelattar on 07-10-17.
 */

public class User {
    private Long id = null;
    private String email;
    private String authToken;

    public User(Long id, String email, String authToken) {
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

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
