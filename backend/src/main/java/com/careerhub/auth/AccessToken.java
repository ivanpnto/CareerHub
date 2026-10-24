package com.careerhub.auth;

import java.time.Duration;

record AccessToken(String value, Duration expiresIn) {

    @Override
    public String toString() {
        return "AccessToken[value=****, expiresIn=" + expiresIn + "]";
    }
}