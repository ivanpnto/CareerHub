package com.careerhub.company;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.Repository;

interface CompanyRepository extends Repository<Company, UUID> {

    <S extends Company> S save(S company);

    Optional<Company> findByIdAndUserId(UUID id, UUID userId);

    List<Company> findAllByUserIdOrderByNameAsc(UUID userId);

    List<Company> findAllByUserIdAndNameContainingIgnoreCaseOrderByNameAsc(UUID userId, String name);

    void delete(Company company);

    void flush();
}
