package com.careerhub.company;

public record CompanyCommand(
        String name,
        String website,
        String industry,
        String headquarters,
        String notes
) {
}
