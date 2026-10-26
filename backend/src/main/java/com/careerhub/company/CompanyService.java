package com.careerhub.company;

import java.util.List;
import java.util.UUID;

import com.careerhub.user.UserLookupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final UserLookupService userLookupService;

    CompanyService(CompanyRepository companyRepository, UserLookupService userLookupService) {
        this.companyRepository = companyRepository;
        this.userLookupService = userLookupService;
    }

    @Transactional
    public Company create(UUID userId, CompanyCommand command) {
        Company company = new Company(
                command.name(),
                command.website(),
                command.industry(),
                command.headquarters(),
                command.notes(),
                userLookupService.getReference(userId)
        );
        return companyRepository.save(company);
    }

    @Transactional(readOnly = true)
    public List<Company> findAll(UUID userId, String name) {
        if (name == null || name.isBlank()) {
            return companyRepository.findAllByUserIdOrderByNameAsc(userId);
        }
        return companyRepository.findAllByUserIdAndNameContainingIgnoreCaseOrderByNameAsc(
                userId, name.strip());
    }

    @Transactional(readOnly = true)
    public Company get(UUID userId, UUID companyId) {
        return findOwned(userId, companyId);
    }

    @Transactional
    public Company update(UUID userId, UUID companyId, CompanyCommand command) {
        Company company = findOwned(userId, companyId);
        company.update(
                command.name(),
                command.website(),
                command.industry(),
                command.headquarters(),
                command.notes()
        );
        // Flush so that @PreUpdate refreshes updatedAt before the entity is returned
        companyRepository.flush();
        return company;
    }

    @Transactional
    public void delete(UUID userId, UUID companyId) {
        Company company = findOwned(userId, companyId);
        companyRepository.delete(company);
        // Flush so that a foreign key violation surfaces inside this method
        companyRepository.flush();
    }

    private Company findOwned(UUID userId, UUID companyId) {
        return companyRepository.findByIdAndUserId(companyId, userId)
                .orElseThrow(CompanyNotFoundException::new);
    }
}
