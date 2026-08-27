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
class TaskPersistenceTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldPersistAndRetrieveTaskWithJobApplication() {
        User user = new User(
                "task.test@example.com",
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
                OffsetDateTime.parse("2026-08-15T09:30:00+02:00"),
                "HIGH",
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

        OffsetDateTime dueAt =
                OffsetDateTime.parse("2026-08-28T17:00:00+02:00");

        Task task = new Task(
                "Prepare technical interview",
                "Review Spring Boot and JPA concepts.",
                dueAt,
                "PENDING",
                "HIGH",
                persistedUser,
                persistedApplication
        );

        entityManager.persist(task);
        entityManager.flush();
        entityManager.clear();

        Task persistedTask =
                entityManager.find(Task.class, task.getId());

        assertThat(persistedTask).isNotNull();
        assertThat(persistedTask.getId()).isNotNull();

        assertThat(persistedTask.getCreatedAt()).isNotNull();
        assertThat(persistedTask.getUpdatedAt()).isNotNull();

        assertThat(persistedTask.getTitle())
                .isEqualTo("Prepare technical interview");

        assertThat(persistedTask.getDescription())
                .isEqualTo("Review Spring Boot and JPA concepts.");

        assertThat(persistedTask.getDueAt())
                .isEqualTo(dueAt);

        assertThat(persistedTask.getStatus())
                .isEqualTo("PENDING");

        assertThat(persistedTask.getPriority())
                .isEqualTo("HIGH");

        assertThat(persistedTask.getUser())
                .isNotNull();

        assertThat(persistedTask.getUser().getId())
                .isEqualTo(persistedUser.getId());

        assertThat(persistedTask.getJobApplication())
                .isNotNull();

        assertThat(persistedTask.getJobApplication().getId())
                .isEqualTo(persistedApplication.getId());
    }

    @Test
    void shouldPersistTaskWithoutJobApplication() {
        User user = new User(
                "standalone.task@example.com",
                "hashed-password",
                "Jane",
                "Doe"
        );

        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        User persistedUser =
                entityManager.find(User.class, user.getId());

        Task task = new Task(
                "Update CV",
                "Review and update the current CV.",
                null,
                "PENDING",
                "MEDIUM",
                persistedUser,
                null
        );

        entityManager.persist(task);
        entityManager.flush();
        entityManager.clear();

        Task persistedTask =
                entityManager.find(Task.class, task.getId());

        assertThat(persistedTask).isNotNull();
        assertThat(persistedTask.getJobApplication()).isNull();
        assertThat(persistedTask.getDueAt()).isNull();
    }
}