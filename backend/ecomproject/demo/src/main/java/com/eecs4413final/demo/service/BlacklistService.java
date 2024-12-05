package com.eecs4413final.demo.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class BlacklistService {

    private final Set<String> blacklistedTokens = new HashSet<>();

    /**
     * Adds a token to the blacklist.
     *
     * @param token The JWT token to blacklist.
     */
    public void blacklistToken(String token) {
        blacklistedTokens.add(token);
    }

    /**
     * Checks if a token is blacklisted.
     *
     * @param token The JWT token to check.
     * @return True if the token is blacklisted, false otherwise.
     */
    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }
}
