package com.careerhub.auth;

import com.careerhub.user.User;
import com.careerhub.user.UserRegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
class AuthController {

    private final UserRegistrationService userRegistrationService;
    private final LoginService loginService;

    AuthController(UserRegistrationService userRegistrationService, LoginService loginService) {
        this.userRegistrationService = userRegistrationService;
        this.loginService = loginService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        User user = userRegistrationService.register(request.toCommand());
        return RegisterResponse.from(user);
    }

    @PostMapping("/login")
    LoginResponse login(@Valid @RequestBody LoginRequest request) {
        AccessToken token = loginService.login(request.email(), request.password());
        return LoginResponse.from(token);
    }
}