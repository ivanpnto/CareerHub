package com.careerhub.domain.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class InterviewPersistenceTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldPersistAndRetrieveInterviewWithJobApplication() {
        User user = new User(
                "interview.test@example.com",
                "hashed-password",
                "John",
                "Doe"
        );

        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        User persistedUser =
                entityManager.find(User.class, user.getId());

        Company company = new Company(
                "Acme Corporation",
                "https://acme.example.com",
                "Technology",
                "Madrid",
                null,
                persistedUser
        );

        entityManager.persist(company);
        entityManager.flush();
        entityManager.clear();

        Company persistedCompany =
                entityManager.find(Company.class, company.getId());

        JobOffer jobOffer = new JobOffer(
                "Software Engineer",
                "Develop backend applications.",
                "Madrid",
                "HYBRID",
                "FULL_TIME",
                "https://acme.example.com/jobs/software-engineer",
                OffsetDateTime.parse("2026-08-01T10:00:00+02:00"),
                OffsetDateTime.parse("2026-09-01T10:00:00+02:00"),
                persistedCompany
        );

        entityManager.persist(jobOffer);
        entityManager.flush();
        entityManager.clear();

        JobOffer persistedJobOffer =
                entityManager.find(JobOffer.class, jobOffer.getId());

        JobApplication application = new JobApplication(
                "Software Engineer",
                "APPLIED",
                OffsetDateTime.parse("2026-08-15T09:30:00+02:00"),
                "HIGH",
                null,
                persistedUser,
                persistedCompany,
                persistedJobOffer
        );

        entityManager.persist(application);
        entityManager.flush();
        entityManager.clear();

        JobApplication persistedApplication =
                entityManager.find(
                        JobApplication.class,
                        application.getId()
                );

        OffsetDateTime scheduledAt =
                OffsetDateTime.parse("2026-08-20T11:00:00+02:00");

        Interview interview = new Interview(
                scheduledAt,
                "TECHNICAL",
                "SCHEDULED",
                "Madrid office",
                "Jane Smith",
                "Technical interview with the backend team.",
                persistedApplication
        );

        entityManager.persist(interview);
        entityManager.flush();
        entityManager.clear();

        Interview persistedInterview =
                entityManager.find(Interview.class, interview.getId());

        assertThat(persistedInterview).isNotNull();
        assertThat(persistedInterview.getId()).isNotNull();

        assertThat(persistedInterview.getCreatedAt()).isNotNull();
        assertThat(persistedInterview.getUpdatedAt()).isNotNull();

        assertThat(persistedInterview.getScheduledAt())
                .isEqualTo(scheduledAt);

        assertThat(persistedInterview.getType())
                .isEqualTo("TECHNICAL");

        assertThat(persistedInterview.getStatus())
                .isEqualTo("SCHEDULED");

        assertThat(persistedInterview.getLocation())
                .isEqualTo("Madrid office");

        assertThat(persistedInterview.getInterviewer())
                .isEqualTo("Jane Smith");

        assertThat(persistedInterview.getNotes())
                .isEqualTo(
                        "Technical interview with the backend team."
                );

        assertThat(persistedInterview.getJobApplication())
                .isNotNull();

        assertThat(persistedInterview.getJobApplication().getId())
                .isEqualTo(persistedApplication.getId());
    }
}