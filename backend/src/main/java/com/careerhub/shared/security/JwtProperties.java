package com.careerhub.shared.security;

import java.time.Duration;
import java.util.Base64;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "careerhub.security.jwt")
public record JwtProperties(String secret, String issuer, Duration accessTokenTtl) {

    // HS256 requires a key of at least 256 bits
    private static final int MIN_SECRET_BYTES = 32;

    public JwtProperties {
        if (secret == null || secret.isBlank()) {
            throw new IllegalArgumentException("JWT secret must be configured.");
        }
        if (decode(secret).length < MIN_SECRET_BYTES) {
            throw new IllegalArgumentException(
                    "JWT secret must be at least " + MIN_SECRET_BYTES + " bytes.");
        }
        if (issuer == null || issuer.isBlank()) {
            throw new IllegalArgumentException("JWT issuer must be configured.");
        }
        if (accessTokenTtl == null || accessTokenTtl.isNegative() || accessTokenTtl.isZero()) {
            throw new IllegalArgumentException("JWT access token TTL must be positive.");
        }
    }

    public byte[] decodedSecret() {
        return decode(secret);
    }

    private static byte[] decode(String secret) {
        try {
            return Base64.getDecoder().decode(secret);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("JWT secret must be Base64-encoded.");
        }
    }

    @Override
    public String toString() {
        return "JwtProperties[secret=****, issuer=" + issuer
                + ", accessTokenTtl=" + accessTokenTtl + "]";
    }
}