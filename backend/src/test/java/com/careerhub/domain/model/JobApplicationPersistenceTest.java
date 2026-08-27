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
class JobApplicationPersistenceTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldPersistAndRetrieveJobApplicationWithJobOffer() {
        User user = new User(
                "application.test@example.com",
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
                "Example company",
                persistedUser
        );

        entityManager.persist(company);
        entityManager.flush();
        entityManager.clear();

        Company persistedCompany =
                entityManager.find(Company.class, company.getId());

        OffsetDateTime publishedAt =
                OffsetDateTime.parse("2026-08-01T10:00:00+02:00");

        OffsetDateTime expiresAt =
                OffsetDateTime.parse("2026-09-01T10:00:00+02:00");

        JobOffer jobOffer = new JobOffer(
                "Software Engineer",
                "Develop backend applications.",
                "Madrid",
                "HYBRID",
                "FULL_TIME",
                "https://acme.example.com/jobs/software-engineer",
                publishedAt,
                expiresAt,
                persistedCompany
        );

        entityManager.persist(jobOffer);
        entityManager.flush();
        entityManager.clear();

        JobOffer persistedJobOffer =
                entityManager.find(JobOffer.class, jobOffer.getId());

        OffsetDateTime appliedAt =
                OffsetDateTime.parse("2026-08-15T09:30:00+02:00");

        JobApplication application = new JobApplication(
                "Software Engineer",
                "APPLIED",
                appliedAt,
                "HIGH",
                "Application submitted through the company website.",
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

        assertThat(persistedApplication).isNotNull();
        assertThat(persistedApplication.getId()).isNotNull();

        assertThat(persistedApplication.getCreatedAt()).isNotNull();
        assertThat(persistedApplication.getUpdatedAt()).isNotNull();

        assertThat(persistedApplication.getPositionTitle())
                .isEqualTo("Software Engineer");

        assertThat(persistedApplication.getStatus())
                .isEqualTo("APPLIED");

        assertThat(persistedApplication.getAppliedAt())
                .isEqualTo(appliedAt);

        assertThat(persistedApplication.getPriority())
                .isEqualTo("HIGH");

        assertThat(persistedApplication.getNotes())
                .isEqualTo(
                        "Application submitted through the company website."
                );

        assertThat(persistedApplication.getUser())
                .isNotNull();

        assertThat(persistedApplication.getUser().getId())
                .isEqualTo(persistedUser.getId());

        assertThat(persistedApplication.getCompany())
                .isNotNull();

        assertThat(persistedApplication.getCompany().getId())
                .isEqualTo(persistedCompany.getId());

        assertThat(persistedApplication.getJobOffer())
                .isNotNull();

        assertThat(persistedApplication.getJobOffer().getId())
                .isEqualTo(persistedJobOffer.getId());
    }

    @Test
    void shouldPersistJobApplicationWithoutJobOffer() {
        User user = new User(
                "application.without.offer@example.com",
                "hashed-password",
                "Jane",
                "Doe"
        );

        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        User persistedUser =
                entityManager.find(User.class, user.getId());

        Company company = new Company(
                "Another Corporation",
                null,
                null,
                null,
                null,
                persistedUser
        );

        entityManager.persist(company);
        entityManager.flush();
        entityManager.clear();

        Company persistedCompany =
                entityManager.find(Company.class, company.getId());

        JobApplication application = new JobApplication(
                "Backend Developer",
                "APPLIED",
                null,
                "MEDIUM",
                null,
                persistedUser,
                persistedCompany,
                null
        );

        entityManager.persist(application);
        entityManager.flush();
        entityManager.clear();

        JobApplication persistedApplication =
                entityManager.find(
                        JobApplication.class,
                        application.getId()
                );

        assertThat(persistedApplication).isNotNull();
        assertThat(persistedApplication.getJobOffer()).isNull();
    }
}