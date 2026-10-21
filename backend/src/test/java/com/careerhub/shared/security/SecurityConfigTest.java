package com.careerhub.shared.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void shouldRejectUnauthenticatedRequestsToProtectedEndpoints() throws Exception {
        mockMvc.perform(get("/api/v1/companies"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldNotBlockUnauthenticatedRequestsToAuthEndpoints() throws Exception {
        int statusCode = mockMvc.perform(post("/api/v1/auth/register"))
                .andReturn()
                .getResponse()
                .getStatus();

        assertThat(statusCode).isNotIn(401, 403);
    }

    @Test
    void shouldHashPasswordsWithBcryptByDefault() {
        String hash = passwordEncoder.encode("secret-password");

        assertThat(hash).startsWith("{bcrypt}");
        assertThat(passwordEncoder.matches("secret-password", hash)).isTrue();
        assertThat(passwordEncoder.matches("wrong-password", hash)).isFalse();
    }
}