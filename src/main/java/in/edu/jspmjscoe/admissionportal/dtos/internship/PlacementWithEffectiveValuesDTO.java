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
public class PlacementWithEffectiveValuesDTO {

    private Long placementId;
    private Long companyId;
    private String companyName;
    private String jobTitle;
    private String description;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal ctc;
    private String bondRequired;
    private Integer vacancies;
    private PostingStatus status;
    
    // Effective values (calculated from parent defaults if child value is null)
    private Integer effectiveVacancies;
    private BigDecimal effectiveCtc;
    private String effectiveBondRequired;
    private String effectiveLocation;
    
    // Company defaults for reference
    private CompanyDTO company;
}