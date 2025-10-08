package in.edu.jspmjscoe.admissionportal.services.internship;

import in.edu.jspmjscoe.admissionportal.dtos.internship.CompanyDTO;
import in.edu.jspmjscoe.admissionportal.dtos.internship.InternshipPostingDTO;
import in.edu.jspmjscoe.admissionportal.dtos.internship.PlacementDTO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service to calculate effective values for child entities based on parent defaults
 */
@Service
public class EffectiveValueService {

    /**
     * Calculate effective vacancies for internship posting
     */
    public Integer getEffectiveVacancies(InternshipPostingDTO posting, CompanyDTO company) {
        return posting.getVacancies() != null ? posting.getVacancies() : 
               (company != null ? company.getDefaultVacancies() : null);
    }

    /**
     * Calculate effective location for internship posting
     */
    public String getEffectiveLocation(InternshipPostingDTO posting, CompanyDTO company) {
        return posting.getLocation() != null ? posting.getLocation() : 
               (company != null ? company.getAddress() : null);
    }

    /**
     * Calculate effective vacancies for placement
     */
    public Integer getEffectiveVacancies(PlacementDTO placement, CompanyDTO company) {
        return placement.getVacancies() != null ? placement.getVacancies() : 
               (company != null ? company.getDefaultVacancies() : null);
    }

    /**
     * Calculate effective CTC for placement
     */
    public BigDecimal getEffectiveCtc(PlacementDTO placement, CompanyDTO company) {
        return placement.getCtc() != null ? placement.getCtc() : 
               (company != null ? company.getDefaultCtc() : null);
    }

    /**
     * Calculate effective bond requirement for placement
     */
    public String getEffectiveBondRequired(PlacementDTO placement, CompanyDTO company) {
        return placement.getBondRequired() != null ? placement.getBondRequired() : 
               (company != null ? company.getDefaultBondRequired() : null);
    }

    /**
     * Calculate effective location for placement
     */
    public String getEffectiveLocation(PlacementDTO placement, CompanyDTO company) {
        return placement.getLocation() != null ? placement.getLocation() : 
               (company != null ? company.getAddress() : null);
    }
}