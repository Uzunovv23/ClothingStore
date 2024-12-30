package com.nnmfashion.clothingstore.dtos;

public class AuthResponse {
    private String token;

    // Constructor that accepts a String
    public AuthResponse(String token) {
        this.token = token;
    }

    // Default constructor (if needed)
    public AuthResponse() {
    }

    // Getter and setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}