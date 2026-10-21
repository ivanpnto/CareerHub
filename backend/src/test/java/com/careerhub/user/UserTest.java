package com.careerhub.user;

import java.util.Locale;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    @Test
    void shouldNormalizeEmailWhenCreatingUser() {
        User user = new User("  Ivan.Pinto@Example.COM ", "hash", "Ivan", "Pinto");

        assertThat(user.getEmail()).isEqualTo("ivan.pinto@example.com");
    }

    @Test
    void shouldRejectNullEmail() {
        assertThatThrownBy(() -> User.normalizeEmail(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("email must not be null");
    }

    @Test
    void shouldNormalizeEmailIndependentlyOfDefaultLocale() {
        Locale originalLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr"));

            assertThat(User.normalizeEmail("IVAN@EXAMPLE.COM"))
                    .isEqualTo("ivan@example.com");
        } finally {
            Locale.setDefault(originalLocale);
        }
    }
}