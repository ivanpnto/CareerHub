package com.careerhub.interview;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.careerhub.company.Company;
import com.careerhub.jobapplication.JobApplication;
import com.careerhub.joboffer.JobOffer;
import com.careerhub.user.User;

import java.time.Instant;
import java.time.LocalDate;

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
                LocalDate.parse("2026-08-01"),
                LocalDate.parse("2026-08-01"),
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
                LocalDate.parse("2026-08-15"),
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

        Instant scheduledAt =
                Instant.parse("2026-08-20T09:00:00Z");

        Interview interview = new Interview(
                scheduledAt,
                "VIDEO",
                "SCHEDULED",
                "Google Meet",
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
                .isEqualTo("VIDEO");

        assertThat(persistedInterview.getStatus())
                .isEqualTo("SCHEDULED");

        assertThat(persistedInterview.getLocation())
                .isEqualTo("Google Meet");

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