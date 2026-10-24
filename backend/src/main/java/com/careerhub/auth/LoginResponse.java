package com.careerhub.auth;

public record LoginResponse(String accessToken, String tokenType, long expiresIn) {

    static LoginResponse from(AccessToken token) {
        return new LoginResponse(token.value(), "Bearer", token.expiresIn().toSeconds());
    }

    @Override
    public String toString() {
        return "LoginResponse[accessToken=****, tokenType=" + tokenType
                + ", expiresIn=" + expiresIn + "]";
    }
}