package com.careerhub.shared.security;

import java.lang.reflect.Method;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CurrentUserIdArgumentResolverTest {

    private final CurrentUserIdArgumentResolver resolver = new CurrentUserIdArgumentResolver();

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldSupportOnlyAnnotatedUuidParameters() throws Exception {
        assertThat(resolver.supportsParameter(parameter("annotatedUuid", UUID.class))).isTrue();
        assertThat(resolver.supportsParameter(parameter("plainUuid", UUID.class))).isFalse();
        assertThat(resolver.supportsParameter(parameter("annotatedString", String.class))).isFalse();
    }

    @Test
    void shouldResolveUserIdFromJwtSubject() throws Exception {
        UUID userId = UUID.randomUUID();
        authenticateWithSubject(userId.toString());

        assertThat(resolver.resolveArgument(parameter("annotatedUuid", UUID.class), null, null, null))
                .isEqualTo(userId);
    }

    @Test
    void shouldRejectRequestWithoutJwtAuthentication() {
        assertThatThrownBy(() ->
                resolver.resolveArgument(parameter("annotatedUuid", UUID.class), null, null, null))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    void shouldRejectSubjectThatIsNotUuid() {
        authenticateWithSubject("not-a-uuid");

        assertThatThrownBy(() ->
                resolver.resolveArgument(parameter("annotatedUuid", UUID.class), null, null, null))
                .isInstanceOf(AuthenticationException.class);
    }

    private static void authenticateWithSubject(String subject) {
        Jwt jwt = Jwt.withTokenValue("token")
                .header("alg", "HS256")
                .subject(subject)
                .build();
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(jwt));
    }

    private static MethodParameter parameter(String methodName, Class<?> type) throws Exception {
        Method method = SampleController.class.getDeclaredMethod(methodName, type);
        return new MethodParameter(method, 0);
    }

    @SuppressWarnings("unused")
    static class SampleController {

        void annotatedUuid(@CurrentUserId UUID userId) {
        }

        void plainUuid(UUID userId) {
        }

        void annotatedString(@CurrentUserId String userId) {
        }
    }
}