package in.edu.jspmjscoe.admissionportal.dtos.internship;

import in.edu.jspmjscoe.admissionportal.model.internship.PostingStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlacementDTO {

    private Long placementId;
    private Long companyId;
    private String companyName; // for display purposes
    private String jobTitle;
    private String description;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal ctc;
    private String bondRequired;
    private Integer vacancies;
    private PostingStatus status;
    private Long referredByUserId;
    private String referredByUsername; // for display purposes
    
    // Effective value methods
    public Integer getEffectiveVacancies(CompanyDTO company) {
        return vacancies != null ? vacancies : (company != null ? company.getDefaultVacancies() : null);
    }
    
    public BigDecimal getEffectiveCtc(CompanyDTO company) {
        return ctc != null ? ctc : (company != null ? company.getDefaultCtc() : null);
    }
    
    public String getEffectiveBondRequired(CompanyDTO company) {
        return bondRequired != null ? bondRequired : (company != null ? company.getDefaultBondRequired() : null);
    }
    
    public String getEffectiveLocation(CompanyDTO company) {
        return location != null ? location : (company != null ? company.getAddress() : null);
    }
}
