package com.careerhub.auth;

final class PasswordLimits {

    // BCrypt only processes the first 72 bytes; Spring Security rejects longer input
    static final int BCRYPT_MAX_BYTES = 72;

    private PasswordLimits() {
    }
}