package in.edu.jspmjscoe.admissionportal.dtos.internship;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDTO {

    private Long companyId;
    private String name;
    private String address;
    private String website;
    private Integer yearEstablished;
    private String industryType;
    private String companySize;
    private String parentGroup;
    private Integer employeeStrength;
    private String contactEmail;
    private String contactMobile;
    private String profilesOffered;
    private String eligibleBranches;
    private Integer defaultVacancies;
    private String internshipOpportunity;
    private BigDecimal defaultCtc;
    private String defaultBondRequired;
    private String hiringProcess;
    private String timeline;
    private String agreementRequired;
    private String hiringStatus;
}
