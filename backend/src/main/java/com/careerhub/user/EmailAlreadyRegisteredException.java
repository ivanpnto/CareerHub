package com.careerhub.user;

import com.careerhub.shared.error.ConflictException;

public class EmailAlreadyRegisteredException extends ConflictException {

    public EmailAlreadyRegisteredException() {
        super("Email address is already registered.");
    }
}