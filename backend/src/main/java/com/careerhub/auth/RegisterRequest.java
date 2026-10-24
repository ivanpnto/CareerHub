package com.careerhub.auth;

import com.careerhub.shared.validation.Utf8ByteSize;
import com.careerhub.user.RegisterUserCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank @Size(min = 8, max = 64)
        @Utf8ByteSize(max = PasswordLimits.BCRYPT_MAX_BYTES) String password,
        @NotBlank @Size(max = 100) String firstName,
        @NotBlank @Size(max = 100) String lastName
) {

    RegisterUserCommand toCommand() {
        return new RegisterUserCommand(email, password, firstName, lastName);
    }

    @Override
    public String toString() {
        return "RegisterRequest[email=" + email
                + ", password=****, firstName=" + firstName
                + ", lastName=" + lastName + "]";
    }
}