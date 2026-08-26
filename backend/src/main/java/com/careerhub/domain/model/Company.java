package com.careerhub.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @ManyToOne
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
        this.name = name;
        this.website = website;
        this.industry = industry;
        this.headquarters = headquarters;
        this.notes = notes;
        this.user = user;
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
}