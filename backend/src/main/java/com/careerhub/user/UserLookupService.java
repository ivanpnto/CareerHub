package com.careerhub.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserLookupService {

    private final UserRepository userRepository;

    UserLookupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(User.normalizeEmail(email));
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public User getReference(UUID id) {
        return userRepository.getReferenceById(id);
    }
}
