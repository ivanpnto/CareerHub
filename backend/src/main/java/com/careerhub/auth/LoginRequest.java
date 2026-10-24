package com.careerhub.auth;

import com.careerhub.shared.validation.Utf8ByteSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank @Size(max = 255) String email,
        @NotBlank @Utf8ByteSize(max = PasswordLimits.BCRYPT_MAX_BYTES) String password
) {

    @Override
    public String toString() {
        return "LoginRequest[email=" + email + ", password=****]";
    }
}