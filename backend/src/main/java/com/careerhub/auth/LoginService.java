package com.careerhub.auth;

import com.careerhub.user.UserLookupService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
class LoginService {

    private final DaoAuthenticationProvider authenticationProvider;
    private final AccessTokenIssuer accessTokenIssuer;

    LoginService(
            UserLookupService userLookupService,
            PasswordEncoder passwordEncoder,
            AccessTokenIssuer accessTokenIssuer
    ) {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService(userLookupService));
        provider.setPasswordEncoder(passwordEncoder);

        this.authenticationProvider = provider;
        this.accessTokenIssuer = accessTokenIssuer;
    }

    AccessToken login(String email, String password) {
        Authentication authentication = authenticationProvider.authenticate(
                UsernamePasswordAuthenticationToken.unauthenticated(email, password));

        AuthenticatedUser user = (AuthenticatedUser) authentication.getPrincipal();
        return accessTokenIssuer.issue(user.id());
    }

    private static UserDetailsService userDetailsService(UserLookupService userLookupService) {
        return email -> userLookupService.findByEmail(email)
                .map(AuthenticatedUser::from)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }
}