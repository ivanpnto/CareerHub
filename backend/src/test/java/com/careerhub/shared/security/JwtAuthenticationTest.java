package com.careerhub.shared.security;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class JwtAuthenticationTest {

    private static final String PROTECTED_URL = "/api/v1/companies";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Test
    void shouldAcceptValidToken() throws Exception {
        Instant now = Instant.now();
        String token = encode(jwtEncoder, "careerhub", now, now.plus(15, ChronoUnit.MINUTES));

        int statusCode = mockMvc.perform(get(PROTECTED_URL).header("Authorization", "Bearer " + token))
                .andReturn()
                .getResponse()
                .getStatus();

        assertThat(statusCode).isNotIn(401, 403);
    }

    @Test
    void shouldRejectExpiredToken() throws Exception {
        Instant now = Instant.now();
        String token = encode(jwtEncoder, "careerhub",
                now.minus(30, ChronoUnit.MINUTES), now.minus(10, ChronoUnit.MINUTES));

        mockMvc.perform(get(PROTECTED_URL).header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectTokenFromAnotherIssuer() throws Exception {
        Instant now = Instant.now();
        String token = encode(jwtEncoder, "another-system", now, now.plus(15, ChronoUnit.MINUTES));

        mockMvc.perform(get(PROTECTED_URL).header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectTokenSignedWithAnotherKey() throws Exception {
        byte[] otherKey = new byte[32];
        new SecureRandom().nextBytes(otherKey);
        JwtEncoder otherEncoder = new NimbusJwtEncoder(
                new ImmutableSecret<>(new SecretKeySpec(otherKey, "HmacSHA256")));

        Instant now = Instant.now();
        String token = encode(otherEncoder, "careerhub", now, now.plus(15, ChronoUnit.MINUTES));

        mockMvc.perform(get(PROTECTED_URL).header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectMalformedToken() throws Exception {
        mockMvc.perform(get(PROTECTED_URL).header("Authorization", "Bearer not-a-jwt"))
                .andExpect(status().isUnauthorized());
    }

    private static String encode(JwtEncoder encoder, String issuer, Instant issuedAt, Instant expiresAt) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(UUID.randomUUID().toString())
                .issuedAt(issuedAt)
                .expiresAt(expiresAt)
                .build();
        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    @Test
    void shouldReturnProblemDetailWithBearerChallengeWhenTokenIsMissing() throws Exception {
        mockMvc.perform(get(PROTECTED_URL))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Authentication failed."))
                .andExpect(header().string("WWW-Authenticate", startsWith("Bearer")));
    }

    @Test
    void shouldReturnProblemDetailWithInvalidTokenChallengeWhenTokenIsExpired() throws Exception {
        Instant now = Instant.now();
        String token = encode(jwtEncoder, "careerhub",
                now.minus(30, ChronoUnit.MINUTES), now.minus(10, ChronoUnit.MINUTES));

        mockMvc.perform(get(PROTECTED_URL).header("Authorization", "Bearer " + token))
                .andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_PROBLEM_JSON))
                .andExpect(jsonPath("$.detail").value("Authentication failed."))
                .andExpect(header().string("WWW-Authenticate", containsString("invalid_token")));
    }
}