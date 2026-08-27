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
class JobOfferPersistenceTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldPersistAndRetrieveJobOfferWithCompany() {
        User user = new User(
                "job.offer.test@example.com",
                "hashed-password",
                "John",
                "Doe"
        );

        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        User persistedUser = entityManager.find(User.class, user.getId());

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

        assertThat(persistedJobOffer).isNotNull();
        assertThat(persistedJobOffer.getId()).isNotNull();

        assertThat(persistedJobOffer.getCreatedAt()).isNotNull();
        assertThat(persistedJobOffer.getUpdatedAt()).isNotNull();

        assertThat(persistedJobOffer.getTitle())
                .isEqualTo("Software Engineer");
        assertThat(persistedJobOffer.getDescription())
                .isEqualTo("Develop backend applications.");
        assertThat(persistedJobOffer.getLocation())
                .isEqualTo("Madrid");
        assertThat(persistedJobOffer.getWorkMode())
                .isEqualTo("HYBRID");
        assertThat(persistedJobOffer.getEmploymentType())
                .isEqualTo("FULL_TIME");
        assertThat(persistedJobOffer.getSourceUrl())
                .isEqualTo(
                        "https://acme.example.com/jobs/software-engineer"
                );

        assertThat(persistedJobOffer.getPublishedAt())
                .isEqualTo(publishedAt);
        assertThat(persistedJobOffer.getExpiresAt())
                .isEqualTo(expiresAt);

        assertThat(persistedJobOffer.getCompany())
                .isNotNull();
        assertThat(persistedJobOffer.getCompany().getId())
                .isEqualTo(persistedCompany.getId());
    }
}