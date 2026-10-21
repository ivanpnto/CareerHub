package com.careerhub.user;

import com.careerhub.shared.persistence.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Locale;
import java.util.Objects;

@Entity
@Table(name = "\"user\"")
public class User extends BaseEntity {

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    protected User() {
    }

    public User(
            String email,
            String passwordHash,
            String firstName,
            String lastName
    ) {
        this.email = normalizeEmail(email);
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static String normalizeEmail(String email) {
        Objects.requireNonNull(email, "email must not be null");
        return email.trim().toLowerCase(Locale.ROOT);
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}