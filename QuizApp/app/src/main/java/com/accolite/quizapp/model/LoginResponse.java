package com.accolite.quizapp.model;

/**
 * Created by Kanika Gupta on 20-10-2016.
 */

public class LoginResponse {

    String basicAuthToken;
    User user;
    boolean created;

    public String getBasicAuthToken() {
        return basicAuthToken;
    }

    public void setBasicAuthToken(String basicAuthToken) {
        this.basicAuthToken = basicAuthToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isCreated() {
        return created;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }
}
