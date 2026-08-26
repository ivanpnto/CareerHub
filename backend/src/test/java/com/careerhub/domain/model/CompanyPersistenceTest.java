package com.careerhub.domain.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class CompanyPersistenceTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void shouldPersistAndRetrieveCompanyWithUser() {
        User user = new User(
                "company.owner@example.com",
                "hashed-password",
                "John",
                "Doe"
        );

        entityManager.persist(user);
        entityManager.flush();
        entityManager.clear();

        User persistedUser = entityManager.find(User.class, user.getId());

        Company company = new Company(
                "Acme Corporation",
                "https://acme.example.com",
                "Technology",
                "Madrid",
                "Example company",
                persistedUser
        );

        entityManager.persist(company);
        entityManager.flush();
        entityManager.clear();

        Company persistedCompany =
                entityManager.find(Company.class, company.getId());

        assertThat(persistedCompany).isNotNull();
        assertThat(persistedCompany.getId()).isNotNull();

        assertThat(persistedCompany.getCreatedAt()).isNotNull();
        assertThat(persistedCompany.getUpdatedAt()).isNotNull();

        assertThat(persistedCompany.getName())
                .isEqualTo("Acme Corporation");
        assertThat(persistedCompany.getWebsite())
                .isEqualTo("https://acme.example.com");
        assertThat(persistedCompany.getIndustry())
                .isEqualTo("Technology");
        assertThat(persistedCompany.getHeadquarters())
                .isEqualTo("Madrid");
        assertThat(persistedCompany.getNotes())
                .isEqualTo("Example company");

        assertThat(persistedCompany.getUser())
                .isNotNull();
        assertThat(persistedCompany.getUser().getId())
                .isEqualTo(persistedUser.getId());
    }
}