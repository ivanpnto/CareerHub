package com.careerhub.domain.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserPersistenceTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldPersistAndRetrieveUser() {
        User user = new User(
                "john.doe@example.com",
                "hashed-password",
                "John",
                "Doe"
        );

        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        User persistedUser = entityManager.find(User.class, user.getId());

        assertThat(persistedUser).isNotNull();
        assertThat(persistedUser.getId()).isNotNull();
        assertThat(persistedUser.getId()).isInstanceOf(UUID.class);

        assertThat(persistedUser.getCreatedAt()).isNotNull();
        assertThat(persistedUser.getUpdatedAt()).isNotNull();

        assertThat(persistedUser.getEmail())
                .isEqualTo("john.doe@example.com");
        assertThat(persistedUser.getPasswordHash())
                .isEqualTo("hashed-password");
        assertThat(persistedUser.getFirstName())
                .isEqualTo("John");
        assertThat(persistedUser.getLastName())
                .isEqualTo("Doe");
    }
}