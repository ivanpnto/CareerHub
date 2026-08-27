package com.careerhub.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;

@Entity
@Table(name = "task")
public class Task extends BaseEntity {

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "due_at")
    private OffsetDateTime dueAt;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(nullable = false, length = 50)
    private String priority;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "job_application_id")
    private JobApplication jobApplication;

    protected Task() {
    }

    public Task(
            String title,
            String description,
            OffsetDateTime dueAt,
            String status,
            String priority,
            User user,
            JobApplication jobApplication
    ) {
        this.title = title;
        this.description = description;
        this.dueAt = dueAt;
        this.status = status;
        this.priority = priority;
        this.user = user;
        this.jobApplication = jobApplication;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public OffsetDateTime getDueAt() {
        return dueAt;
    }

    public String getStatus() {
        return status;
    }

    public String getPriority() {
        return priority;
    }

    public User getUser() {
        return user;
    }

    public JobApplication getJobApplication() {
        return jobApplication;
    }
}