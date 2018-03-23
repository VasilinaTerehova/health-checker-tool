package com.epam.facade.model.projection.impl;

import com.epam.facade.model.projection.CredentialsProjection;

public class CredentialsProjectionImpl implements CredentialsProjection {
    private String username;
    private String password;

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
