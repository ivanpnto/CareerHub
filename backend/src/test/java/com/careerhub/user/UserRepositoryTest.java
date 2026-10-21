package com.careerhub.user;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldFindUserByEmail() {
        User user = new User("jane@example.com", "hashed-password", "Jane", "Doe");
        entityManager.persistAndFlush(user);
        entityManager.clear();

        Optional<User> found = userRepository.findByEmail("jane@example.com");

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(user.getId());
    }

    @Test
    void shouldReturnEmptyWhenEmailIsNotRegistered() {
        assertThat(userRepository.findByEmail("unknown@example.com")).isEmpty();
    }

    @Test
    void shouldReportWhetherEmailExists() {
        entityManager.persistAndFlush(
                new User("john@example.com", "hashed-password", "John", "Doe"));

        assertThat(userRepository.existsByEmail("john@example.com")).isTrue();
        assertThat(userRepository.existsByEmail("other@example.com")).isFalse();
    }
}