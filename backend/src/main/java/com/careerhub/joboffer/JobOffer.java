package com.careerhub.joboffer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.FetchType;

import java.time.LocalDate;

import com.careerhub.company.Company;
import com.careerhub.shared.persistence.BaseEntity;

@Entity
@Table(name = "job_offer")
public class JobOffer extends BaseEntity {

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String location;

    @Column(name = "work_mode", length = 50)
    private String workMode;

    @Column(name = "employment_type", length = 50)
    private String employmentType;

    @Column(name = "source_url", length = 500)
    private String sourceUrl;

    @Column(name = "published_at")
    private LocalDate publishedAt;

    @Column(name = "expires_at")
    private LocalDate expiresAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    protected JobOffer() {
    }

    public JobOffer(
            String title,
            String description,
            String location,
            String workMode,
            String employmentType,
            String sourceUrl,
            LocalDate publishedAt,
            LocalDate expiresAt,
            Company company
    ) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.workMode = workMode;
        this.employmentType = employmentType;
        this.sourceUrl = sourceUrl;
        this.publishedAt = publishedAt;
        this.expiresAt = expiresAt;
        this.company = company;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getWorkMode() {
        return workMode;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public LocalDate getPublishedAt() {
        return publishedAt;
    }

    public LocalDate getExpiresAt() {
        return expiresAt;
    }

    public Company getCompany() {
        return company;
    }
}