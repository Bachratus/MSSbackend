package com.mss.app.security.dto;

public class JwtResponse {

    private final String id_token;

    public JwtResponse(String token) {
        this.id_token = token;
    }

    public String getId_token() {
        return this.id_token;
    }
}