package in.edu.jspmjscoe.admissionportal.repositories.internship;

import in.edu.jspmjscoe.admissionportal.model.internship.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findByNameContainingIgnoreCase(String name);

    List<Company> findByIndustryType(String industryType);

    List<Company> findByHiringStatus(String hiringStatus);

    Optional<Company> findByWebsite(String website);

    boolean existsByNameAndYearEstablished(String name, Integer yearEstablished);

    // Updated methods for default fields
    List<Company> findByDefaultVacanciesGreaterThan(Integer minVacancies);

    List<Company> findByDefaultCtcBetween(BigDecimal minCtc, BigDecimal maxCtc);

    List<Company> findByDefaultBondRequired(String bondRequired);

    List<Company> findByAgreementRequired(String agreementRequired);

    List<Company> findByInternshipOpportunity(String internshipOpportunity);
}


