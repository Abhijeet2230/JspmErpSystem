package in.edu.jspmjscoe.admissionportal.dtos.internship;

import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
    private String hrName;
    private String industryType;
    private String parentGroup;
    private Integer employeeStrength;
    private String contactEmail;
    private String contactMobile;
    private String profilesOffered;

    @Builder.Default
    private List<String> eligibleBranches = new ArrayList<>();

    private Integer defaultVacancies;
    private BigDecimal defaultCtc;
    private String defaultBondRequired;
    private String hiringProcess;
    private String agreementRequired;
    private String hiringStatus;
}
