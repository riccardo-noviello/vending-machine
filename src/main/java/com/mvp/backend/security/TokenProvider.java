package com.mvp.backend.security;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TokenProvider {

    /**
     * Produces a dummy token for now
     * @param username
     * @return
     */
    public String generateToken(String username) {
        return "Bearer " +  username + UUID.randomUUID();
    }

}
