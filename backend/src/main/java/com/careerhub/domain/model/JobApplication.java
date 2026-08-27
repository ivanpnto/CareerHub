package com.careerhub.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "job_application")
public class JobApplication extends BaseEntity {

    @Column(name = "position_title", nullable = false, length = 255)
    private String positionTitle;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(name = "applied_at")
    private OffsetDateTime appliedAt;

    @Column(nullable = false, length = 50)
    private String priority;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "job_offer_id")
    private JobOffer jobOffer;

    protected JobApplication() {
    }

    public JobApplication(
            String positionTitle,
            String status,
            OffsetDateTime appliedAt,
            String priority,
            String notes,
            User user,
            Company company,
            JobOffer jobOffer
    ) {
        this.positionTitle = positionTitle;
        this.status = status;
        this.appliedAt = appliedAt;
        this.priority = priority;
        this.notes = notes;
        this.user = user;
        this.company = company;
        this.jobOffer = jobOffer;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public String getStatus() {
        return status;
    }

    public OffsetDateTime getAppliedAt() {
        return appliedAt;
    }

    public String getPriority() {
        return priority;
    }

    public String getNotes() {
        return notes;
    }

    public User getUser() {
        return user;
    }

    public Company getCompany() {
        return company;
    }

    public JobOffer getJobOffer() {
        return jobOffer;
    }
}