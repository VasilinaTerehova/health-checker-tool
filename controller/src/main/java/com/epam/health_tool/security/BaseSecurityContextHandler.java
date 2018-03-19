package com.epam.health_tool.security;

import com.epam.health_tool.authenticate.impl.ClusterAuthentication;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class BaseSecurityContextHandler {
    protected static ClusterAuthentication getCredentialsFromSecurityContext() throws AuthenticationException {
        Authentication loggedAuthentication = SecurityContextHolder.getContext().getAuthentication();
        if (loggedAuthentication instanceof ClusterAuthentication) {
            return (ClusterAuthentication) loggedAuthentication;
        }

        throw new BadCredentialsException("Another authentication!");
    }
}
