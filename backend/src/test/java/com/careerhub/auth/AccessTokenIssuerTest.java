package com.careerhub.auth;

import java.time.Duration;
import java.util.UUID;

import com.careerhub.shared.security.JwtProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AccessTokenIssuerTest {

    @Autowired
    private AccessTokenIssuer accessTokenIssuer;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private JwtProperties properties;

    @Test
    void shouldIssueTokenAcceptedByApplicationDecoder() {
        UUID userId = UUID.randomUUID();

        Jwt jwt = jwtDecoder.decode(accessTokenIssuer.issue(userId).value());

        assertThat(jwt.getSubject()).isEqualTo(userId.toString());
        assertThat(jwt.getClaimAsString(JwtClaimNames.ISS)).isEqualTo(properties.issuer());
    }

    @Test
    void shouldExpireAfterConfiguredTtl() {
        AccessToken token = accessTokenIssuer.issue(UUID.randomUUID());

        Jwt jwt = jwtDecoder.decode(token.value());

        assertThat(Duration.between(jwt.getIssuedAt(), jwt.getExpiresAt()))
                .isEqualTo(properties.accessTokenTtl());
        assertThat(token.expiresIn()).isEqualTo(properties.accessTokenTtl());
    }

    @Test
    void shouldNotExposeTokenValueInToString() {
        AccessToken token = accessTokenIssuer.issue(UUID.randomUUID());

        assertThat(token.toString()).doesNotContain(token.value());
    }
}