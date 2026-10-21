package com.careerhub.shared.validation;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class Utf8ByteSizeValidatorTest {

    private static final Validator VALIDATOR =
            Validation.buildDefaultValidatorFactory().getValidator();

    record Sample(@Utf8ByteSize(max = 4) String value) {
    }

    @Test
    void shouldAcceptValueExactlyAtByteLimit() {
        // "ññ": 2 characters, 4 UTF-8 bytes
        assertThat(VALIDATOR.validate(new Sample("\u00F1\u00F1"))).isEmpty();
    }

    @Test
    void shouldRejectValueOverByteLimitEvenWhenShortInCharacters() {
        // "ñña": 3 characters, 5 UTF-8 bytes
        assertThat(VALIDATOR.validate(new Sample("\u00F1\u00F1a"))).hasSize(1);
    }

    @Test
    void shouldTreatNullAsValid() {
        assertThat(VALIDATOR.validate(new Sample(null))).isEmpty();
    }
}