package com.careerhub.company;

import java.time.Instant;

import com.careerhub.joboffer.JobOffer;
import com.careerhub.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class CompanyServiceTest {

    @Autowired
    private CompanyService companyService;

    @PersistenceContext
    private EntityManager entityManager;

    private User owner;
    private User otherUser;

    @BeforeEach
    void setUp() {
        owner = persistUser("company.owner@example.com");
        otherUser = persistUser("company.other@example.com");
    }

    @Test
    void shouldCreateCompanyWithNormalizedFields() {
        Company company = companyService.create(owner.getId(),
                new CompanyCommand("  Acme  ", "   ", null, "Madrid", ""));
        entityManager.flush();
        entityManager.clear();

        Company found = companyService.get(owner.getId(), company.getId());

        assertThat(found.getName()).isEqualTo("Acme");
        assertThat(found.getWebsite()).isNull();
        assertThat(found.getIndustry()).isNull();
        assertThat(found.getHeadquarters()).isEqualTo("Madrid");
        assertThat(found.getNotes()).isNull();
    }

    @Test
    void shouldNotAllowAccessToCompanyOfAnotherUser() {
        Company company = companyService.create(owner.getId(), command("Acme"));

        assertThatThrownBy(() -> companyService.get(otherUser.getId(), company.getId()))
                .isInstanceOf(CompanyNotFoundException.class);
        assertThatThrownBy(() -> companyService.update(otherUser.getId(), company.getId(), command("Hijacked")))
                .isInstanceOf(CompanyNotFoundException.class);
        assertThatThrownBy(() -> companyService.delete(otherUser.getId(), company.getId()))
                .isInstanceOf(CompanyNotFoundException.class);
    }

    @Test
    void shouldListOnlyOwnCompaniesOrderedByName() {
        companyService.create(owner.getId(), command("Zeta"));
        companyService.create(owner.getId(), command("Acme"));
        companyService.create(otherUser.getId(), command("Other"));

        assertThat(companyService.findAll(owner.getId(), null))
                .extracting(Company::getName)
                .containsExactly("Acme", "Zeta");
    }

    @Test
    void shouldFilterCompaniesByNameIgnoringCase() {
        companyService.create(owner.getId(), command("Acme Corporation"));
        companyService.create(owner.getId(), command("Globex"));

        assertThat(companyService.findAll(owner.getId(), "acme"))
                .extracting(Company::getName)
                .containsExactly("Acme Corporation");
    }

    @Test
    void shouldTreatLikeWildcardsInFilterAsLiteralCharacters() {
        companyService.create(owner.getId(), command("Acme"));

        assertThat(companyService.findAll(owner.getId(), "%")).isEmpty();
    }

    @Test
    void shouldUpdateCompanyKeepingCreatedAtAndRefreshingUpdatedAt() {
        Company company = companyService.create(owner.getId(), command("Acme"));
        entityManager.flush();
        Instant createdAt = company.getCreatedAt();
        Instant originalUpdatedAt = company.getUpdatedAt();

        Company updated = companyService.update(owner.getId(), company.getId(),
                new CompanyCommand("Acme Corp", "https://acme.example.com", null, null, null));

        assertThat(updated.getUpdatedAt()).isAfter(originalUpdatedAt);

        entityManager.clear();
        Company reloaded = companyService.get(owner.getId(), company.getId());

        assertThat(reloaded.getName()).isEqualTo("Acme Corp");
        assertThat(reloaded.getWebsite()).isEqualTo("https://acme.example.com");
        assertThat(reloaded.getCreatedAt()).isEqualTo(createdAt);
        assertThat(reloaded.getUpdatedAt()).isEqualTo(updated.getUpdatedAt());
    }

    @Test
    void shouldDeleteCompany() {
        Company company = companyService.create(owner.getId(), command("Acme"));

        companyService.delete(owner.getId(), company.getId());

        assertThatThrownBy(() -> companyService.get(owner.getId(), company.getId()))
                .isInstanceOf(CompanyNotFoundException.class);
    }

    @Test
    void shouldNotDeleteCompanyWithJobOffers() {
        Company company = companyService.create(owner.getId(), command("Acme"));
        entityManager.persist(new JobOffer(
                "Backend Developer", null, null, null, null, null, null, null, company));
        entityManager.flush();
        entityManager.clear();

        assertThatThrownBy(() -> companyService.delete(owner.getId(), company.getId()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    private User persistUser(String email) {
        User user = new User(email, "{bcrypt}hash", "Test", "User");
        entityManager.persist(user);
        return user;
    }

    private static CompanyCommand command(String name) {
        return new CompanyCommand(name, null, null, null, null);
    }
}
