package com.careerhub.company;

import java.util.Objects;

import com.careerhub.shared.persistence.BaseEntity;
import com.careerhub.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "company")
public class Company extends BaseEntity {

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 500)
    private String website;

    @Column(length = 150)
    private String industry;

    @Column(length = 255)
    private String headquarters;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    protected Company() {
    }

    public Company(
            String name,
            String website,
            String industry,
            String headquarters,
            String notes,
            User user
    ) {
        this.user = Objects.requireNonNull(user, "user must not be null");
        apply(name, website, industry, headquarters, notes);
    }

    public void update(
            String name,
            String website,
            String industry,
            String headquarters,
            String notes
    ) {
        apply(name, website, industry, headquarters, notes);
    }

    public String getName() {
        return name;
    }

    public String getWebsite() {
        return website;
    }

    public String getIndustry() {
        return industry;
    }

    public String getHeadquarters() {
        return headquarters;
    }

    public String getNotes() {
        return notes;
    }

    public User getUser() {
        return user;
    }

    private void apply(
            String name,
            String website,
            String industry,
            String headquarters,
            String notes
    ) {
        this.name = requireText(name, "name");
        this.website = stripToNull(website);
        this.industry = stripToNull(industry);
        this.headquarters = stripToNull(headquarters);
        this.notes = stripToNull(notes);
    }

    private static String requireText(String value, String field) {
        String stripped = stripToNull(value);
        if (stripped == null) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return stripped;
    }

    private static String stripToNull(String value) {
        if (value == null) {
            return null;
        }
        String stripped = value.strip();
        return stripped.isEmpty() ? null : stripped;
    }
}
