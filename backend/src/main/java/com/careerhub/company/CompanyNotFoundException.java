package com.careerhub.company;

import com.careerhub.shared.error.NotFoundException;

public class CompanyNotFoundException extends NotFoundException {

    public CompanyNotFoundException() {
        super("Company not found.");
    }
}
