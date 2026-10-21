package com.careerhub.user;

public record RegisterUserCommand(
        String email,
        String password,
        String firstName,
        String lastName
) {

    @Override
    public String toString() {
        return "RegisterUserCommand[email=" + email
                + ", password=****, firstName=" + firstName
                + ", lastName=" + lastName + "]";
    }
}