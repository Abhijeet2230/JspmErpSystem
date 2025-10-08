package in.edu.jspmjscoe.admissionportal.model.internship;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "company_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long companyId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address", length = 1000)
    private String address;

    @Column(name = "website")
    private String website;

    @Column(name = "year_established")
    private Integer yearEstablished;

    @Column(name = "industry_type")
    private String industryType;

    @Column(name = "company_size")
    private String companySize;

    @Column(name = "parent_group")
    private String parentGroup;

    @Column(name = "employee_strength")
    private Integer employeeStrength;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_mobile")
    private String contactMobile;

    @Column(name = "profiles_offered")
    private String profilesOffered;

    @Column(name = "eligible_branches")
    private String eligibleBranches;

    @Column(name = "default_vacancies")
    private Integer defaultVacancies;

    @Column(name = "internship_opportunity")
    private String internshipOpportunity;

    @Column(name = "default_ctc", precision = 12, scale = 2)
    private BigDecimal defaultCtc;

    @Column(name = "default_bond_required")
    private String defaultBondRequired;

    @Column(name = "hiring_process", length = 1000)
    private String hiringProcess;

    @Column(name = "timeline")
    private String timeline;

    @Column(name = "agreement_required")
    private String agreementRequired;

    @Column(name = "hiring_status")
    private String hiringStatus;
}
