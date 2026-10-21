package com.careerhub.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserRegistrationService service;

    @BeforeEach
    void setUp() {
        service = new UserRegistrationService(userRepository, passwordEncoder);
    }

    @Test
    void shouldRegisterUserWithNormalizedEmailAndHashedPassword() {
        when(userRepository.existsByEmail("ivan@example.com")).thenReturn(false);
        when(passwordEncoder.encode("secret-password")).thenReturn("{bcrypt}hashed");
        when(userRepository.saveAndFlush(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User user = service.register(new RegisterUserCommand(
                "  Ivan@Example.com ", "secret-password", "Ivan", "Pinto"));

        assertThat(user.getEmail()).isEqualTo("ivan@example.com");
        assertThat(user.getPasswordHash()).isEqualTo("{bcrypt}hashed");
        assertThat(user.getFirstName()).isEqualTo("Ivan");
        assertThat(user.getLastName()).isEqualTo("Pinto");
    }

    @Test
    void shouldRejectAlreadyRegisteredEmailWithoutHashingPassword() {
        when(userRepository.existsByEmail("ivan@example.com")).thenReturn(true);

        assertThatThrownBy(() -> service.register(new RegisterUserCommand(
                "Ivan@Example.com", "secret-password", "Ivan", "Pinto")))
                .isInstanceOf(EmailAlreadyRegisteredException.class);

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).saveAndFlush(any(User.class));
    }
}