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
public class InternshipPostingDTO {

    private Long internshipId;
    private Long companyId;
    private String companyName; // for display purposes
    private String title;
    private String description;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal stipend;
    private Integer vacancies;
    private PostingStatus status;
    
    // Effective value methods
    public Integer getEffectiveVacancies(CompanyDTO company) {
        return vacancies != null ? vacancies : (company != null ? company.getDefaultVacancies() : null);
    }
    
    public String getEffectiveLocation(CompanyDTO company) {
        return location != null ? location : (company != null ? company.getAddress() : null);
    }
}
