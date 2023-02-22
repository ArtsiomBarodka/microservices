package com.my.app.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

public class SecurityUtils {
    public static String getAuthToken() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final Jwt jwt = ((Jwt) authentication.getPrincipal());
        return jwt.getTokenValue();
    }

    private SecurityUtils() {}
}
