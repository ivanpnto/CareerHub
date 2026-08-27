package com.careerhub.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "interview")
public class Interview extends BaseEntity {

    @Column(name = "scheduled_at", nullable = false)
    private OffsetDateTime scheduledAt;

    @Column(nullable = false, length = 50)
    private String type;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(length = 255)
    private String location;

    @Column(length = 255)
    private String interviewer;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "job_application_id", nullable = false)
    private JobApplication jobApplication;

    protected Interview() {
    }

    public Interview(
            OffsetDateTime scheduledAt,
            String type,
            String status,
            String location,
            String interviewer,
            String notes,
            JobApplication jobApplication
    ) {
        this.scheduledAt = scheduledAt;
        this.type = type;
        this.status = status;
        this.location = location;
        this.interviewer = interviewer;
        this.notes = notes;
        this.jobApplication = jobApplication;
    }

    public OffsetDateTime getScheduledAt() {
        return scheduledAt;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public String getInterviewer() {
        return interviewer;
    }

    public String getNotes() {
        return notes;
    }

    public JobApplication getJobApplication() {
        return jobApplication;
    }
}