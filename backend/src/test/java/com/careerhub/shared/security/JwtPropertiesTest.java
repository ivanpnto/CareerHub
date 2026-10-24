package com.careerhub.shared.security;

import java.time.Duration;
import java.util.Base64;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtPropertiesTest {

    private static final Duration TTL = Duration.ofMinutes(15);

    @Test
    void shouldAcceptSecretOf256Bits() {
        JwtProperties properties = new JwtProperties(secretOfBytes(32), "careerhub", TTL);

        assertThat(properties.decodedSecret()).hasSize(32);
    }

    @Test
    void shouldRejectSecretShorterThan256Bits() {
        assertThatThrownBy(() -> new JwtProperties(secretOfBytes(31), "careerhub", TTL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least 32 bytes");
    }

    @Test
    void shouldRejectSecretThatIsNotBase64() {
        assertThatThrownBy(() -> new JwtProperties("not base64 !!", "careerhub", TTL))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Base64");
    }

    @Test
    void shouldNotExposeSecretInToString() {
        String secret = secretOfBytes(32);

        assertThat(new JwtProperties(secret, "careerhub", TTL).toString())
                .doesNotContain(secret);
    }

    private static String secretOfBytes(int length) {
        return Base64.getEncoder().encodeToString(new byte[length]);
    }
}